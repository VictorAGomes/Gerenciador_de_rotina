package br.ufrn.imd.dao;

import java.time.LocalDate;
import java.util.ArrayList;

import br.ufrn.imd.model.RecurringTask;
import br.ufrn.imd.model.ScheduleTask;
import br.ufrn.imd.model.Task;

public class TasksFilters {

	public ArrayList<Task> filterName(ArrayList<Task> tasks, String name){
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task t : tasks) {
			if(t.getName().toLowerCase().contains(name.toLowerCase())) {
				filteredTasks.add(t);
			}
		}
		if (!filteredTasks.isEmpty()) {
			System.out.println("Achei essas tarefas com o nome procurado: ");
			return filteredTasks;
		}else {
			System.out.println("Ops! Não encontrei tarefas com esse nome!\n");
			return new ArrayList<>();
		}
		
	}
	
	public ArrayList<Task> filterDate(ArrayList<Task> tasks, LocalDate date){
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task t : tasks) {
			if(t instanceof ScheduleTask) {
				if(((ScheduleTask)t).getDate().equals(date)) {
					filteredTasks.add(t);
				}
			}
		}
		if (!filteredTasks.isEmpty()) {
			System.out.println("Achei essas tarefas com a data procurada: ");
			return filteredTasks;
		}else {
			System.out.println("Ops! Não encontrei tarefas com essa data!\n");
			return new ArrayList<>();
		}
	}
	

	public ArrayList<Task> filterGroup(ArrayList<Task> tasks, String groupName){
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task t : tasks) {
			if(t.getGroupName().toLowerCase().contains(groupName.toLowerCase())) {
				filteredTasks.add(t);
			}
		}
		if (!filteredTasks.isEmpty()) {
			System.out.println("Achei essas tarefas do grupo procurado: ");
			return filteredTasks;
		}else {
			System.out.println("Ops! Não encontrei tarefas do grupo pesquisado!\n");
			return new ArrayList<>();
		}	
	}
	
	public ArrayList<Task> filterType(ArrayList<Task> tasks, String type){
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task t : tasks) {
			if(t.getType().toLowerCase().contains(type.toLowerCase())) {
				filteredTasks.add(t);
			}
		}
		if (!filteredTasks.isEmpty()) {
			System.out.println("Achei essas tarefas do tipo procurado: ");
			return filteredTasks;
		}else {
			System.out.println("Ops! Não encontrei tarefas desse tipo!\n");
			return new ArrayList<>();
		}	
	}
	

	public ArrayList<Task> filterDone(ArrayList<Task> tasks){
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task t : tasks) {
			if(t.isDone()) {
				filteredTasks.add(t);
			}
		}
		if (!filteredTasks.isEmpty()) {
			System.out.println("Achei essas tarefas concluidas: ");
			return filteredTasks;
		}else {
			System.out.println("Ops! Não encontrei tarefas concluidas!\n");
			return new ArrayList<>();
		}	
	}
	
	public ArrayList<Task> filterNotDone(ArrayList<Task> tasks){
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task t : tasks) {
			if(!t.isDone()) {
				filteredTasks.add(t);
			}
		}
		if (!filteredTasks.isEmpty()) {
			System.out.println("Achei essas tarefas não concluidas: ");
			return filteredTasks;
		}else {
			System.out.println("Eba! Não encontrei tarefas não concluidas!\n");
			return new ArrayList<>();
		}	
	}
	
	public ArrayList<Task> filterRecurrence(ArrayList<Task> tasks, int recurrence, String recurrenceType){
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		for(Task t : tasks) {
			if(t instanceof RecurringTask) {
				if(((RecurringTask)t).getRecurrence() == recurrence && ((RecurringTask)t).getRecurrenceType().toLowerCase().contains(recurrenceType.toLowerCase())) {
				filteredTasks.add(t);					
				}
			}
		}
		if (!filteredTasks.isEmpty()) {
			System.out.println("Achei essas tarefas com a recorrência pesquisada: ");
			return filteredTasks;
		}else {
			System.out.println("Ops! Não encontrei tarefas com essa recorrência!\n");
			return new ArrayList<>();
		}	
	}
	
	public void printFilteredTasks(ArrayList<Task> tasks) {
		for(Task t : tasks) {
			System.out.println(t.toString());
			System.out.println("");
		}
	}
	
}
