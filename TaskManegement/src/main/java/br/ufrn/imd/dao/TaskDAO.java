package br.ufrn.imd.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.ufrn.imd.model.Task;

public class TaskDAO implements ITaskDAO {
	private static final String FILE_NAME = "tasks.json";
	private Gson gson;

	public TaskDAO() {
		this.gson = new GsonBuilder()
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(Task.class, new TaskTypeAdapter())
				.setPrettyPrinting()
				.create();
	}
	
	//inicializa o json na primeira vez que abre o app
	@Override
	public void initialize() throws IOException {
		File file = new File(FILE_NAME);
		if(!file.exists()) {
			try (FileWriter writer = new FileWriter(file)){
				gson.toJson(new ArrayList<Task>(), writer);
			}
		}
	}

	@Override
	public void saveTask(Task task) {
		try {
			// Carregar as tarefas existentes do JSON
			ArrayList<Task> tasks = loadTasks();

			// Procurar pela tarefa correspondente (com base no nome ou outro identificador único)
			boolean taskUpdated = false;
			for (int i = 0; i < tasks.size(); i++) {
				Task existingTask = tasks.get(i);
				if (existingTask.getName().equals(task.getName())) { // Comparação por nome
					tasks.set(i, task); // Atualizar a tarefa existente
					taskUpdated = true;
					break;
				}
			}

			// Se a tarefa não foi encontrada, adicioná-la à lista
			if (!taskUpdated) {
				tasks.add(task);
			}

			// Salvar a lista atualizada no JSON
			try (FileWriter writer = new FileWriter(FILE_NAME, false)) {
				gson.toJson(tasks, writer);
			}
			System.out.println("Tarefa salva ou atualizada com sucesso!");
		} catch (Exception e) {
			System.err.println("Erro ao salvar tarefa: " + e.getMessage());
		}
	}
	
	@Override
	public ArrayList<Task> loadTasks() throws IOException {
	    try (FileReader reader = new FileReader(FILE_NAME)) {
	        Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
	        ArrayList<Task> tasks = gson.fromJson(reader, listType);
	        return tasks;
	    } catch (IOException e) {
	        System.err.println("Erro ao carregar tarefas: " + e.getMessage());
	        return new ArrayList<>(); //lista vazia em caso de erro
	    }
	}
	public void updateTask(Task updatedTask) throws IOException {
		ArrayList<Task> tasks = loadTasks(); // Carregar todas as tarefas do JSON
		boolean found = false;

		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			// Verificar por um identificador único, como nome ou outra propriedade
			if (task.getName().equals(updatedTask.getName())) {
				tasks.set(i, updatedTask); // Substituir a tarefa existente pela atualizada
				found = true;
				break;
			}
		}

		if (found) {
			try (FileWriter writer = new FileWriter(FILE_NAME, false)) {
				gson.toJson(tasks, writer); // Salvar as tarefas atualizadas no arquivo JSON
			}
		} else {
			System.err.println("Tarefa não encontrada para atualização: " + updatedTask.getName());
		}
	}
	
	 public void deleteTask(String name) throws IOException {
         List<Task> tasks = loadTasks();

	        List<Task> update = tasks.stream()
	            .filter(task -> !task.getName().equals(name))
	            .collect(Collectors.toList());

	        // Salvar a lista atualizada de volta ao arquivo JSON
	        FileWriter writer = new FileWriter(FILE_NAME);
	        gson.toJson(update, writer);
	        writer.close();
	    }
	        
}
