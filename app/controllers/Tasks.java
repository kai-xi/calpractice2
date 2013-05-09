package controllers;
import static play.data.Form.form;

import java.util.Date;
import java.util.List;

import play.*;
import play.mvc.*;
import models.*;
import models.Display;
import views.html.*;
//import views.html.display.planner;
import views.html.display.*;
import views.html.tasks.*;
import views.html.users.createUserForm;
import play.data.*;
import play.data.Form.*;

public class Tasks extends Controller	{
	/**
     * Display the new task form.
     */
    public static Result create() {
        Form<Task> taskForm = form(Task.class);
        User currentUser = User.find.where().eq("email",session().get("email")).findUnique();
        System.out.println("printing user name............................");
        System.out.println(currentUser.name);
        return ok(
                createTaskForm.render(taskForm, currentUser)
            );
    }
	
	/**
     * Handle new task form submission 
     */
    public static Result save() {
    	User owner = User.find.where().eq("email",session().get("email")).findUnique();
        Form<Task> taskForm = form(Task.class).bindFromRequest();
        
        if(taskForm.hasErrors()) {
        	System.out.println("task form has errors");
            return badRequest(createTaskForm.render(taskForm, owner));
        }
        String start = taskForm.get().start;
        String end = taskForm.get().end;
        Date date = taskForm.get().date;
        Date repeatUntil = taskForm.get().repeatUntil;
        Task toBeAdded = Task.add(taskForm.get(),owner, start, end, date);
        System.out.println("ABOUT TO SAVE A TASK.");
        System.out.println("task.allDates.size() before saving:");
        System.out.println(toBeAdded.allDates.size());
        toBeAdded.save();
        System.out.println("date");
        System.out.println(toBeAdded.date.toString());
        System.out.format("start time %d",toBeAdded.startTime);
        System.out.format("end time %d",toBeAdded.endTime);
        System.out.println("repeat until");
        System.out.println(repeatUntil.toString());
        flash("success", taskForm.get().title + " has been added for " + owner.name);
        return redirect(routes.Application.index());
    }
    
    public static Result delete(Long id) {
        Task.delete(id);
        return redirect(routes.Application.index());
    }
}
