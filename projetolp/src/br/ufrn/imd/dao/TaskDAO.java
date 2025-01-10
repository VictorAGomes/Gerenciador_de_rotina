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
            ArrayList<Task> tasks = loadTasks();
            tasks.add(task);
            // Salvar a lista atualizada no arquivo
            try (FileWriter writer = new FileWriter(FILE_NAME, false)) {
                gson.toJson(tasks, writer);
            }
            System.out.println("Tarefa salva com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar tarefa: " + e.getMessage());
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
