package br.ufrn.imd.model;

import com.google.gson.annotations.SerializedName;

public abstract class Task {
	
	@SerializedName("name")
	protected String name;
    @SerializedName("description")
	protected String description;
    @SerializedName("isDone")
    protected boolean isDone;
    @SerializedName("groupName")
    protected String groupName;
    @SerializedName("type")
    protected String type;
	
	public Task(String name, String description, boolean isDone, String groupName, String type) {
		this.name = name;
		this.description = description;
		this.isDone = isDone;
		this.groupName = groupName;
		this.type = type;
	}
	
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public abstract void changeName(String name);
	public abstract void changeDescription(String description);
	
	@Override
	public String toString() {
	    return "Tarefa{" +
	            "name='" + name + '\'' +
	            ", description='" + description + '\'' +
	            ", isDone=" + isDone +
	            ", groupName='" + groupName + '\'' +
	            ", type='" + type + '\'' +
	            '}';
	}

}
