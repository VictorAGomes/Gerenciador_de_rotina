package br.ufrn.imd.model;

import java.util.List;

public class Group {

	private String groupName;
	private List<Task> groupTasks;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<Task> getGroupTasks() {
		return groupTasks;
	}
	
	public void setGroupTasks(List<Task> groupTasks) {
		this.groupTasks = groupTasks;
	}
	
	public void changeGroupName(String name) {
		setGroupName(name);
		System.out.println("Nome do grupo alterado com sucesso!");
	}
}