package br.ufrn.imd.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.ufrn.imd.model.Task;

public class TaskService {
	 private TaskDAO taskDAO;

	    public TaskService(TaskDAO tarefaDAO) {
	        this.taskDAO = tarefaDAO;
	    }

	    public void addTask(Task task) throws IOException {
	        try {
	            List<Task> tasks = taskDAO.loadTasks();
	            tasks.add(task);
	            taskDAO.saveTask(task);
	        } catch (IOException e) {
	            System.err.println("Erro ao adicionar tarefa: " + e.getMessage());
	        }
	    }

	    public ArrayList<Task> listTasks() {
	        try {
	            return taskDAO.loadTasks();
	        } catch (IOException e) {
	            System.err.println("Erro ao listar tarefas: " + e.getMessage());
	            // Retorna uma lista vazia em caso de falha de carregamento.
	            return new ArrayList<>();
	        }
	    }
	    
}
