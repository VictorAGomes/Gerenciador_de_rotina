package br.ufrn.imd.view;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import br.ufrn.imd.dao.*;
import br.ufrn.imd.model.Task;
import br.ufrn.imd.model.ScheduleTask;
import br.ufrn.imd.model.RecurringTask;
import br.ufrn.imd.model.SimpleTask;

public class App {
    public static void main(String[] args) throws IOException {
    	TaskDAO taskDAO = new TaskDAO();
    	TaskService taskService = new TaskService(taskDAO);
        TasksFilters tasksFilters = new TasksFilters();
        
        // Tentar adicionar uma nova tarefa
        try {
        	Task t1 = new ScheduleTask(null, null, false, null, null, null);
            t1.setDescription("fazer matricula com desconto");
            t1.setName("matricula senac");
            ((ScheduleTask) t1).setDate(LocalDate.of(2025, 01, 15));
            t1.setDone(false);
            t1.setGroupName("francês");
            t1.setType("TarefaMarcada");
            
            Task t2 = new ScheduleTask(null, null, false, null, null, null);
            t2.setDescription("fazer filtros no DAO");
            t2.setName("projetoLP");
            ((ScheduleTask) t2).setDate(LocalDate.of(2025, 01, 12));
            t2.setDone(false);
            t2.setGroupName("estudo");
            t2.setType("TarefaMarcada");
            
            Task t3 = new RecurringTask(null, null, false, null, null, 0, null);
            t3.setDescription("quintas feiras 18:40-22:20");
            t3.setName("aula ciencia de dados");
            ((RecurringTask) t3).setRecurrence(7);
            ((RecurringTask) t3).setRecurrenceType("dias");
            t3.setDone(false);
            t3.setGroupName("aulas");
            t3.setType("TarefaRecorrente");
            
            Task t4 = new SimpleTask(null, null, false, null, null);
            t4.setDescription("terminar de ler");
            t4.setName("The Spanish Love Deception");
            t4.setDone(false);
            t4.setGroupName("leitura");
            t4.setType("TarefaSimples");
            
            Task t5 = new SimpleTask(null, null, false, null, null);
            t5.setDescription("terminar");
            t5.setName("atividade Spanish");
            t5.setDone(true);
            t5.setGroupName("estudo");
            t5.setType("TarefaSimples");
            
            taskService.addTask(t1);
            taskService.addTask(t2);
            taskService.addTask(t3);
            taskService.addTask(t4);
            taskService.addTask(t5);
             
        } catch (Exception e) {
            System.err.println("Erro ao adicionar tarefa: " + e.getMessage());
        }

        // Tentar listar todas as tarefas
        try {
            List<Task> tasks = taskService.listTasks();
            if (tasks.isEmpty()) {
                System.out.println("Nenhuma tarefa encontrada.");
            } else {
                for (Task t : tasks) {
                   System.out.println(t);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar tarefas: " + e.getMessage());
        }
        

		tasksFilters.printFilteredTasks(tasksFilters.filterName(taskService.listTasks(), "spanish"));
		tasksFilters.printFilteredTasks(tasksFilters.filterDate(taskService.listTasks(), LocalDate.now()));
		tasksFilters.printFilteredTasks(tasksFilters.filterDate(taskService.listTasks(), LocalDate.of(2025, 01, 15)));
		tasksFilters.printFilteredTasks(tasksFilters.filterType(taskService.listTasks(), "TarefaSimples"));
		tasksFilters.printFilteredTasks(tasksFilters.filterDone(taskService.listTasks()));
		tasksFilters.printFilteredTasks(tasksFilters.filterNotDone(taskService.listTasks()));
		tasksFilters.printFilteredTasks(tasksFilters.filterGroup(taskService.listTasks(), "estu"));
		tasksFilters.printFilteredTasks(tasksFilters.filterGroup(taskService.listTasks(), "estudar"));
		tasksFilters.printFilteredTasks(tasksFilters.filterRecurrence(taskService.listTasks(), 7, "dia"));
		tasksFilters.printFilteredTasks(tasksFilters.filterRecurrence(taskService.listTasks(), 7, "dias"));

		try {
            taskDAO.deleteTask("atividade Spanish");
            System.out.println("Tarefa apagada com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao apagar a tarefa: " + e.getMessage());
        }
    }
}