package br.ufrn.imd.dao;
import java.io.IOException;
import java.util.ArrayList;
import br.ufrn.imd.model.Task;

public interface ITaskDAO {

	void saveTask(Task task) throws IOException;
	ArrayList<Task> loadTasks() throws IOException;
	void initialize() throws IOException;
}
