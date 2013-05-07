package controllers;
import static play.data.Form.form;
import play.*;
import play.mvc.*;
import models.*;
import views.html.*;
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
        System.out.println("inside Tasks.create");
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
            return badRequest(createTaskForm.render(taskForm, owner));
        }
        String start = taskForm.get().start;
        String end = taskForm.get().end;
        System.out.println("inside controllers.Tasks.save");
        System.out.println(start);
        System.out.println(end);
        
        Task toBeAdded = Task.add(taskForm.get(),owner, start, end);
        toBeAdded.save();
        System.out.println("date");
        System.out.println(toBeAdded.date.toString());
        System.out.format("start time %d",toBeAdded.startTime);
        flash("success", taskForm.get().title + " has been added for " + owner.name);
        
        return redirect(routes.Planner.displayPlanner());
    }
}
