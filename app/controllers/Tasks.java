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
     * Display the new user form.
     */
    public static Result create() {
        Form<Task> taskForm = form(Task.class);
        System.out.println("inside Tasks.create");
        User currentUser = User.find.where().eq("email",session().get("email")).findUnique();
        System.out.println("printing user name............................");
        System.out.println(currentUser.name);
        return ok(
            createTaskForm.render(taskForm, currentUser, Task.timeSlots())
        );
    }
	
	/**
     * Handle new user form submission 
     */
    public static Result save() {
    	User owner = User.find.where().eq("email",request().username()).findUnique();
        Form<Task> taskForm = form(Task.class).bindFromRequest();
        
        if(taskForm.hasErrors()) {
            return badRequest(createTaskForm.render(taskForm, owner, Task.timeSlots()));
        }
        
        Task toBeAdded = Task.add(taskForm.get(),owner);
        toBeAdded.save();
        flash("success", taskForm.get().desc + " has been added for " + owner.name);
        return redirect(routes.Application.index());
    }
}
