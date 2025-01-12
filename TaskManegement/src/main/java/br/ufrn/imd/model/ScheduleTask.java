package br.ufrn.imd.model;

import java.time.LocalDate;

public class ScheduleTask extends Task {
	private LocalDate date;
	
	public ScheduleTask(String name, String description, boolean isDone, String groupName, String type, LocalDate date) {
		super(name, description, isDone, groupName, type);
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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
	
	public void changeDate(LocalDate date) {
		setDate(date);
		System.out.println("Data final da tarefa alterada com sucesso!");
	}
	
	@Override
	public String toString() {
	    return "TarefaMarcada{" +
	            "name='" + name + '\'' +
	            ", description='" + description + '\'' +
	            ", isDone=" + isDone +
	            ", groupName='" + groupName + '\'' +
	            ", type='" + type + '\'' +
	            ", date=" + date + 
	            '}';
	}


}
