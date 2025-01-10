package br.ufrn.imd.model;

public class SimpleTask extends Task{

	public SimpleTask(String name, String description, boolean isDone, String groupName, String type) {
		super(name, description, isDone, groupName, type);
	}

	@Override
	public void changeName(String name) {
		setName(name);
		System.out.println("Nome da tarefa alterado com sucesso!");			
	}

	@Override
	public void changeDescription(String description) {
		setDescription(description);
		System.out.println("Descrição da tarefa alterada com sucesso!");			
	}
	
	@Override
	public String toString() {
	    return "TarefaSimples{" +
	            "name='" + name + '\'' +
	            ", description='" + description + '\'' +
	            ", isDone=" + isDone +
	            ", groupName='" + groupName + '\'' +
	            ", type='" + type + '\'' +
	            '}';
	}

}
