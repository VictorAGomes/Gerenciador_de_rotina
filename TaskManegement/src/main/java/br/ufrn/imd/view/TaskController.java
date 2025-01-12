package br.ufrn.imd.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import br.ufrn.imd.model.*;
import br.ufrn.imd.dao.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskController {
    // FXML Components
    @FXML private TabPane tabPane;
    @FXML private Tab homeTab, meuDiaTab, atividadesTab;
    @FXML private TextField taskNameTextField, taskDescriptionTextField, taskGroupTextField;
    @FXML private ChoiceBox<String> recurrenceChoiceBox;
    @FXML private CheckBox recurringCheckBox, deadlineCheckBox, filterDoneCheckBox, filterNotDoneCheckBox;
    @FXML private TextField deadlineTextField, filterNameField, filterGroupField;
    @FXML private DatePicker filterDatePicker;
    @FXML private Label urgentTask1, urgentTask2, urgentTask3;
    @FXML private VBox atividadesBox, meudiaVbox, activitiesVBox;
    @FXML private Button addTaskButton, cancelButton, homeButton, meuDiaButton, atividadesButton, applyFiltersButton, cleanFiltersButton;

    private TaskDAO taskDAO;

    private TasksFilters tasksFilters = new TasksFilters();

    @FXML
    private void initialize() {
        loadTodayActivities();
        loadUrgentTasks();

        // Configurações iniciais, como preenchimento de ChoiceBox
        recurrenceChoiceBox.getItems().addAll("Diariamente", "Semanalmente", "Mensalmente");

        // Inicializar o DAO e carregar atividades
        taskDAO = new TaskDAO();
        try {
            taskDAO.initialize(); // Garante que o JSON é criado se não existir
            loadActivitiesFromJson();
        } catch (IOException e) {
            System.err.println("Erro ao inicializar o TaskDAO: " + e.getMessage());
        }
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        tabPane.getSelectionModel().select(homeTab);
        System.out.println("Botão HOME clicado.");
    }

    @FXML
    private void handleMeuDiaButton(ActionEvent event) {
        tabPane.getSelectionModel().select(meuDiaTab);
        System.out.println("Botão MEU DIA clicado.");
        // Lógica adicional para a aba "Meu Dia"
    }

    @FXML
    private void handleAtividadesButton(ActionEvent event) {
        tabPane.getSelectionModel().select(atividadesTab);
        System.out.println("Botão ATIVIDADES clicado.");
        // Lógica adicional para a aba "Atividades"
    }

    public TaskController() {
        this.taskDAO = new TaskDAO();
        try {
            taskDAO.initialize();
        } catch (IOException e) {
            System.err.println("Erro ao inicializar o arquivo JSON: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddTaskButton(ActionEvent event) {
        try {
            // Validação dos campos obrigatórios
            if (taskNameTextField.getText() == null || taskNameTextField.getText().trim().isEmpty() ||
                    taskDescriptionTextField.getText() == null || taskDescriptionTextField.getText().trim().isEmpty() ||
                    taskGroupTextField.getText() == null || taskGroupTextField.getText().trim().isEmpty()) {
                showAlert("ATENÇÃO", "Os campos Nome, Descrição e Grupo são obrigatórios!", Alert.AlertType.WARNING);
                return;
            }

            Task task;

            // Verifica se é uma tarefa marcada (com prazo)
            if (deadlineCheckBox.isSelected() && !deadlineTextField.getText().isEmpty()) {
                LocalDate deadline;
                try {
                    deadline = LocalDate.parse(deadlineTextField.getText()); // Assumindo formato yyyy-MM-dd
                } catch (DateTimeParseException e) {
                    showAlert("Erro", "Data inválida! Use o formato yyyy-MM-dd.",Alert.AlertType.ERROR);
                    return;
                }

                task = new ScheduleTask(
                        taskNameTextField.getText(),
                        taskDescriptionTextField.getText(),
                        false, // Não concluída inicialmente
                        taskGroupTextField.getText(),
                        "TarefaMarcada",
                        deadline
                );

            } else if (recurringCheckBox.isSelected()) {
                // Verifica se é uma tarefa recorrente
                if (recurrenceChoiceBox.getValue() == null) {
                    showAlert("Erro", "Selecione o tipo de recorrência.",Alert.AlertType.ERROR);
                    return;
                }

                task = new RecurringTask(
                        taskNameTextField.getText(),
                        taskDescriptionTextField.getText(),
                        false,
                        taskGroupTextField.getText(),
                        "TarefaRecorrente",
                        1, // Número de repetições
                        recurrenceChoiceBox.getValue()
                );

            } else {
                // Caso contrário, é uma tarefa simples
                task = new SimpleTask(
                        taskNameTextField.getText(),
                        taskDescriptionTextField.getText(),
                        false,
                        taskGroupTextField.getText(),
                        "TarefaSimples"
                );
            }

            // Salva a tarefa no arquivo JSON
            taskDAO.saveTask(task);
            loadTodayActivities();
            loadUrgentTasks();
            loadActivitiesFromJson();

            // Limpa os campos da interface
            clearFields();

            showAlert("Sucesso", "Tarefa adicionada com sucesso!",Alert.AlertType.CONFIRMATION);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Ocorreu um erro ao criar a tarefa.",Alert.AlertType.WARNING);
        }
    }


    @FXML
    private void handleCancelButton(ActionEvent event) {
        clearFields();
        System.out.println("Criação de tarefa cancelada.");
    }

    private void clearFields() {
        // Limpa os campos da interface
        taskNameTextField.clear();
        taskDescriptionTextField.clear();
        taskGroupTextField.clear();
        recurringCheckBox.setSelected(false);
        recurrenceChoiceBox.setValue(null);
        deadlineCheckBox.setSelected(false);
        deadlineTextField.clear();
    }

    private void showAlert(String title, String message,Alert.AlertType alertType) {
        // Mostra uma mensagem de erro ou alerta
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void loadUrgentTasks() {
        try {
            // Carregar tarefas salvas
            ArrayList<Task> tasks = taskDAO.loadTasks();

            // Filtrar apenas tarefas marcadas (com prazo) e ordená-las pelo prazo mais próximo
            List<ScheduleTask> urgentTasks = tasks.stream()
                    .filter(task -> task instanceof ScheduleTask) // Apenas ScheduleTask
                    .map(task -> (ScheduleTask) task) // Converter para ScheduleTask
                    .filter(task -> task.getDate().isAfter(LocalDate.now()) || task.getDate().isEqual(LocalDate.now())) // Filtrar prazos futuros ou hoje
                    .sorted(Comparator.comparing(ScheduleTask::getDate)) // Ordenar pelo prazo mais próximo
                    .limit(3) // Pegar apenas as 3 primeiras
                    .collect(Collectors.toList());

            // Atualizar os labels com as informações das tarefas urgentes
            updateUrgentTaskLabels(urgentTasks);

        } catch (IOException e) {
            System.err.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }

    private void updateUrgentTaskLabels(List<ScheduleTask> urgentTasks) {
        // Atualizar os labels com os dados das tarefas
        if (urgentTasks.size() > 0) {
            urgentTask1.setText(formatTaskDisplay(urgentTasks.get(0)));
        } else {
            urgentTask1.setText("Nenhuma tarefa urgente.");
        }

        if (urgentTasks.size() > 1) {
            urgentTask2.setText(formatTaskDisplay(urgentTasks.get(1)));
        } else {
            urgentTask2.setText("");
        }

        if (urgentTasks.size() > 2) {
            urgentTask3.setText(formatTaskDisplay(urgentTasks.get(2)));
        } else {
            urgentTask3.setText("");
        }
    }

    private String formatTaskDisplay(ScheduleTask task) {
        return String.format(
                "Nome: %s\nPrazo: %s\nGrupo: %s",
                task.getName(),
                task.getDate(),
                task.getGroupName()
        );
    }

    private void loadActivitiesFromJson() {
        try {
            // Carregar as tarefas do JSON
            ArrayList<Task> tasks = taskDAO.loadTasks();

            // Adicionar cada tarefa ao VBox
            for (Task task : tasks) {
                String name = task.getName();
                String description = task.getDescription();
                String group = task.getGroupName();
                String deadline = task instanceof ScheduleTask ?
                        ((ScheduleTask) task).getDate().toString() : "Sem prazo";
                String recurrence = task instanceof RecurringTask ?
                        ((RecurringTask) task).getRecurrenceType() : "Nenhuma";
                boolean isDone = task.isDone();

                // Adiciona a tarefa ao VBox
                addActivityBox(name, description, group, deadline, recurrence, isDone, task);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar tarefas do JSON: " + e.getMessage());
        }
    }

    private void addActivityBox(String name, String description, String group, String deadline, String recurrence, boolean isDone, Task task) {
        // Criação do HBox
        HBox activityBox = new HBox();
        activityBox.setPrefSize(637, 129); // Dimensão fixa
        activityBox.setStyle(isDone
                ? "-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-padding: 10;"
                : "-fx-background-color: #2F4A5F; -fx-border-radius: 10; -fx-padding: 10;");

        // Criação do Pane dentro do HBox para layout manual
        Pane activityPane = new Pane();
        activityPane.setPrefSize(634, 129); // Mesma altura que o HBox

        // Criação das Labels
        Label nameLabel = new Label("NOME: " + name);
        nameLabel.setLayoutX(14);
        nameLabel.setLayoutY(14);
        nameLabel.setPrefSize(150, 30);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label descriptionLabel = new Label("DESCRIÇÃO: " + description);
        descriptionLabel.setLayoutX(14);
        descriptionLabel.setLayoutY(44);
        descriptionLabel.setPrefSize(500, 30);
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label groupLabel = new Label("GRUPO: " + group);
        groupLabel.setLayoutX(14);
        groupLabel.setLayoutY(74);
        groupLabel.setPrefSize(150, 30);
        groupLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label deadlineLabel = new Label("PRAZO: " + deadline);
        deadlineLabel.setLayoutX(194);
        deadlineLabel.setLayoutY(74);
        deadlineLabel.setPrefSize(150, 30);
        deadlineLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label recurrenceLabel = new Label("RECORRÊNCIA: " + recurrence);
        recurrenceLabel.setLayoutX(364);
        recurrenceLabel.setLayoutY(74);
        recurrenceLabel.setPrefSize(200, 30); // Ajuste a largura conforme necessário
        recurrenceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        // Criação do CheckBox
        CheckBox finishCheckBox = new CheckBox("FINALIZAR");
        finishCheckBox.setLayoutX(500);
        finishCheckBox.setLayoutY(15);
        finishCheckBox.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        finishCheckBox.setSelected(isDone);

        // Adicionar ação ao CheckBox
        finishCheckBox.setOnAction(event -> {
            boolean checked = finishCheckBox.isSelected();
            // Atualizar o estilo do HBox
            activityBox.setStyle(checked
                    ? "-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-padding: 10;"
                    : "-fx-background-color: #2F4A5F; -fx-border-radius: 10; -fx-padding: 10;");

            // Atualizar o estado da tarefa no objeto
            task.setDone(checked);
            loadTodayActivities();
            loadUrgentTasks();
            loadActivitiesFromJson();

            // Salvar no JSON
            try {
                taskDAO.updateTask(task); // Certifique-se de que o método updateTask funciona corretamente
                System.out.println("Tarefa atualizada no JSON: " + task.getName());
            } catch (IOException e) {
                System.err.println("Erro ao atualizar tarefa no JSON: " + e.getMessage());
            }
        });
        // Criação do Button para excluir atividade
        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(500);
        deleteButton.setLayoutY(45);
        deleteButton.setPrefSize(100, 30);
        deleteButton.setStyle("-fx-text-fill: black; -fx-font-size: 14;");

        // Ação do botão de excluir
        deleteButton.setOnAction(event -> {
            // Remover do VBox
            activitiesVBox.getChildren().remove(activityBox);

            // Remover do JSON
            try {
                taskDAO.deleteTask(task.getName()); // Método que remove pelo nome ou ID
            } catch (IOException e) {
                System.err.println("Erro ao excluir tarefa: " + e.getMessage());
            }
            loadTodayActivities();
            loadUrgentTasks();
            loadActivitiesFromJson();
        });
        // Adicionar todos os componentes ao Pane
        activityPane.getChildren().addAll(nameLabel, descriptionLabel, groupLabel, deadlineLabel, recurrenceLabel, finishCheckBox,deleteButton);

        // Adicionar o Pane ao HBox
        activityBox.getChildren().add(activityPane);

        // Adicionar o HBox ao VBox existente (activitiesVBox)
        activitiesVBox.getChildren().add(activityBox);
    }

    @FXML
    private void handleApplyFilters() {
        try {
            // Carrega todas as tarefas do JSON
            ArrayList<Task> allTasks = taskDAO.loadTasks();
            ArrayList<Task> filteredTasks = new ArrayList<>(allTasks);

            // Aplica os filtros
            if (!filterNameField.getText().isEmpty()) {
                filteredTasks = tasksFilters.filterName(filteredTasks, filterNameField.getText());
            }

            if (!filterGroupField.getText().isEmpty()) {
                filteredTasks = tasksFilters.filterGroup(filteredTasks, filterGroupField.getText());
            }

            if (filterDatePicker.getValue() != null) {
                filteredTasks = tasksFilters.filterDate(filteredTasks, filterDatePicker.getValue());
            }

            if (filterDoneCheckBox.isSelected()) {
                filteredTasks = tasksFilters.filterDone(filteredTasks);
            } else if (filterNotDoneCheckBox.isSelected()) {
                filteredTasks = tasksFilters.filterNotDone(filteredTasks);
            }

            // Atualiza a exibição das tarefas
            activitiesVBox.getChildren().clear();
            for (Task task : filteredTasks) {
                String name = task.getName();
                String description = task.getDescription();
                String group = task.getGroupName();
                String deadline = task instanceof ScheduleTask ? ((ScheduleTask) task).getDate().toString() : "Sem prazo";
                String recurrence = task instanceof RecurringTask ? ((RecurringTask) task).getRecurrenceType() : "Nenhuma";
                boolean isDone = task.isDone();

                // Adiciona a tarefa filtrada na exibição
                addActivityBox(name, description, group, deadline, recurrence, isDone, task);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }
    @FXML
    private void handleCleanFiltersButton() {
        // Limpar filtros aplicados
        System.out.println("Limpando os filtros...");

        // Limpar os campos
        filterNameField.clear();
        filterGroupField.clear();
        filterDoneCheckBox.setSelected(false);
        filterNotDoneCheckBox.setSelected(false);
        filterDatePicker.setValue(null);


        // Recarregar a lista original de tarefas sem filtros
        try {
            ArrayList<Task> allTasks = taskDAO.loadTasks(); // Carrega todas as tarefas do JSON
            activitiesVBox.getChildren().clear(); // Limpa o VBox
            for (Task task : allTasks) {
                // Adiciona cada tarefa ao VBox
                addActivityBox(
                        task.getName(),
                        task.getDescription(),
                        task.getGroupName(),
                        task instanceof ScheduleTask ? ((ScheduleTask) task).getDate().toString() : "Sem prazo",
                        task instanceof RecurringTask ? ((RecurringTask) task).getRecurrenceType() : "Sem recorrência",
                        task.isDone(),
                        task
                );
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar as tarefas: " + e.getMessage());
        }
    }

    private void loadTodayActivities() {
        try {
            // Carregar todas as tarefas
            ArrayList<Task> allTasks = taskDAO.loadTasks();
            LocalDate today = LocalDate.now(); // Data atual

            // Filtrar as tarefas marcadas para hoje
            ArrayList<Task> todayTasks = new ArrayList<>();
            for (Task task : allTasks) {
                if (task instanceof ScheduleTask) {
                    ScheduleTask scheduleTask = (ScheduleTask) task;
                    if (scheduleTask.getDate().equals(today)) {
                        todayTasks.add(task);
                    }
                }
            }

            // Atualizar o VBox da aba "Meu Dia"
            meudiaVbox.getChildren().clear();
            for (Task task : todayTasks) {
                String name = task.getName();
                String description = task.getDescription();
                String group = task.getGroupName();
                String deadline = task instanceof ScheduleTask ? ((ScheduleTask) task).getDate().toString() : "Sem prazo";
                String recurrence = task instanceof RecurringTask ? ((RecurringTask) task).getRecurrenceType() : "Nenhuma";
                boolean isDone = task.isDone();

                // Adicionar a tarefa ao VBox
                addActivityToMeudiaVbox(name, description, group, deadline, recurrence, isDone, task);
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar tarefas do dia: " + e.getMessage());
        }
    }

    private void addActivityToMeudiaVbox(String name, String description, String group, String deadline, String recurrence, boolean isDone, Task task) {
        // Criação do HBox
        HBox activityBox = new HBox();
        activityBox.setPrefSize(637, 129); // Dimensão fixa
        activityBox.setStyle("-fx-background-color: #2F4A5F; -fx-background-radius: 10; -fx-padding: 10;");

        // Criação do Pane dentro do HBox para layout manual
        Pane activityPane = new Pane();
        activityPane.setPrefSize(634, 129); // Mesma altura que o HBox

        // Criação das Labels
        Label nameLabel = new Label("NOME: " + name);
        nameLabel.setLayoutX(14);
        nameLabel.setLayoutY(14);
        nameLabel.setPrefSize(150, 30);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label descriptionLabel = new Label("DESCRIÇÃO: " + description);
        descriptionLabel.setLayoutX(14);
        descriptionLabel.setLayoutY(44);
        descriptionLabel.setPrefSize(500, 30);
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label groupLabel = new Label("GRUPO: " + group);
        groupLabel.setLayoutX(14);
        groupLabel.setLayoutY(74);
        groupLabel.setPrefSize(150, 30);
        groupLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label deadlineLabel = new Label("PRAZO: " + deadline);
        deadlineLabel.setLayoutX(194);
        deadlineLabel.setLayoutY(74);
        deadlineLabel.setPrefSize(150, 30);
        deadlineLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        Label recurrenceLabel = new Label("RECORRÊNCIA: " + recurrence);
        recurrenceLabel.setLayoutX(364);
        recurrenceLabel.setLayoutY(74);
        recurrenceLabel.setPrefSize(200, 30);
        recurrenceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        // Criação do CheckBox
        CheckBox finishCheckBox = new CheckBox("FINALIZAR");
        finishCheckBox.setLayoutX(500);
        finishCheckBox.setLayoutY(15);
        finishCheckBox.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        finishCheckBox.setSelected(isDone);



        // Ação do CheckBox
        finishCheckBox.setOnAction(event -> {
            boolean checked = finishCheckBox.isSelected();
            if (checked) {
                activityBox.setStyle("-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-padding: 10;");
            } else {
                activityBox.setStyle("-fx-background-color: #2F4A5F; -fx-border-radius: 10; -fx-padding: 10;");
            }

            // Atualizar o estado da tarefa no JSON
            task.setDone(checked);
            try {
                taskDAO.updateTask(task); // Certifique-se de que o método updateTask funciona corretamente
                System.out.println("Tarefa atualizada no JSON: " + task.getName());
            } catch (IOException e) {
                System.err.println("Erro ao atualizar tarefa no JSON: " + e.getMessage());
            }
        });

        // Criação do Button para excluir atividade
        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(500);
        deleteButton.setLayoutY(45);
        deleteButton.setPrefSize(100, 30);
        deleteButton.setStyle("-fx-text-fill: black; -fx-font-size: 14;");

        // Ação do botão de excluir
        deleteButton.setOnAction(event -> {
            // Remover do VBox
            meudiaVbox.getChildren().remove(activityBox);

            // Remover do JSON
            try {
                taskDAO.deleteTask(task.getName()); // Método que remove pelo nome ou ID
            } catch (IOException e) {
                System.err.println("Erro ao excluir tarefa: " + e.getMessage());
            }
        });

        // Adicionar todos os componentes ao Pane
        activityPane.getChildren().addAll(nameLabel, descriptionLabel, groupLabel, deadlineLabel, recurrenceLabel, finishCheckBox, deleteButton);

        // Adicionar o Pane ao HBox
        activityBox.getChildren().add(activityPane);

        // Adicionar o HBox ao VBox da aba "Meu Dia"
        meudiaVbox.getChildren().add(activityBox);
    }

}
