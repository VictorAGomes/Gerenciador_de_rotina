package br.ufrn.imd.model;

public class RecurringTask extends Task{

	private int recurrence;
	private String recurrenceType;
	
	public RecurringTask(String name, String description, boolean isDone, String groupName, String type, int recurrence, String recurrenceType) {
		super(name, description, isDone, groupName, type);
		this.recurrence = recurrence;
		this.recurrenceType = recurrenceType;
	}

	public int getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(int recurrence) {
		this.recurrence = recurrence;
	}

	public String getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
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
	
	public void changeRecurrence(int recurrence) {
		setRecurrence(recurrence);
		System.out.println("Recorrência alterada com sucesso!");
	}
	
	public void changeRecurrenceType(String recurrenceType) {
		setRecurrenceType(recurrenceType);
		System.out.println("Tipo da recorrência alterado com sucesso!");
	}

	@Override
	public String toString() {
	    return "TarefaRecorrente{" +
	            "name='" + name + '\'' +
	            ", description='" + description + '\'' +
	            ", isDone=" + isDone +
	            ", groupName='" + groupName + '\'' +
	            ", type='" + type + '\'' +
	            ", recurrence=" + recurrence + recurrenceType +
	            '}';
	}

}
