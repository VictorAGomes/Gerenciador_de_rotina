package br.ufrn.imd.dao;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import br.ufrn.imd.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskTypeAdapter extends TypeAdapter<Task> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
    @Override
    public void write(JsonWriter out, Task task) throws IOException {
        out.beginObject();
        out.name("name").value(task.getName());
        out.name("description").value(task.getDescription());
        out.name("isDone").value(task.isDone());
        out.name("groupName").value(task.getGroupName());
        out.name("type").value(task.getType());

        if (task instanceof ScheduleTask) {
            ScheduleTask scheduleTask = (ScheduleTask) task;
            out.name("date").value(scheduleTask.getDate().format(FORMATTER));
        } else if (task instanceof RecurringTask) {
            RecurringTask recurringTask = (RecurringTask) task;
            out.name("recorrencia").value(recurringTask.getRecurrence());
        }
        out.endObject();
    }

    @Override
    public Task read(JsonReader in) throws IOException {
        in.beginObject();
        String type = null;
        String name = null;
        String description = null;
        boolean isDone = false;
        LocalDate date = null;
        String recurrenceType = null;
        int recurrence = 0;
        String groupName = null;

        while (in.hasNext()) {
        	String nameField = in.nextName();
        	
            switch (nameField) {
                case "type":
                    type = in.nextString();
                    break;
                case "name":
                    name = in.nextString();
                    break;
                case "description":
                	description = in.nextString();
                	break;
                case "isDone":
                	isDone = in.nextBoolean();
                	break;
                case "date":
                    date = LocalDate.parse(in.nextString(), FORMATTER); 
                    break;
                case "recurrenceType":
                    recurrenceType = in.nextString();
                    break;
                case "recurrence":
                    recurrence = in.nextInt();
                    break;
                case "groupName":
                	groupName = in.nextString();
                	break;
            }
        }
        in.endObject();
     

        switch (type) {
            case "TarefaSimples":
                return new SimpleTask(name, description, isDone, groupName, type);
            case "TarefaMarcada":
                return new ScheduleTask(name, description, isDone, groupName, type, date);
            case "TarefaRecorrente":
                return new RecurringTask(name, description, isDone, groupName, type, recurrence, recurrenceType);
            default:
                throw new IllegalArgumentException("Tipo desconhecido: " + type);
        }
    }
}
