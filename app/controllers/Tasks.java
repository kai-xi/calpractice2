package controllers;
import static play.data.Form.form;

import java.util.Date;
import java.util.List;

import play.*;
import play.mvc.*;
import models.*;
import views.html.*;
//import views.html.display.planner;
import views.html.display.*;
import views.html.tasks.*;
import views.html.users.createUserForm;
import play.data.*;
import play.data.Form.*;

/**
 * The Tasks Controller creates, saves, and deletes tasks.
 * @author Kaixi Wu (kw2503) & Emily Chen (ec2805)
 *
 */
public class Tasks extends Controller	{
	
	/**
     * Display the new task form.
     * @return an ok Result to render the createTaskForm view
     */
	public static Result create() {
        Form<Task> taskForm = form(Task.class);
        User currentUser = User.find.where().eq("email",session().get("email")).findUnique();
        return ok(
                createTaskForm.render(taskForm, currentUser)
            );
    }

	/**
     * Handle new task form submission 
     * @return a bad request if the task form has errors, else a redirect to Planner.displayPlanner()
     */
    public static Result save() {
    	User owner = User.find.where().eq("email",session().get("email")).findUnique();
        Form<Task> taskForm = form(Task.class).bindFromRequest();
        
        if(taskForm.hasErrors()) {
            return badRequest(createTaskForm.render(taskForm, owner));
        }
        String start = taskForm.get().start;
        String end = taskForm.get().end;
        Date date = taskForm.get().date;
        
        Task toBeAdded = Task.add(taskForm.get(),owner, start, end, date);
        toBeAdded.save();
        flash("success", taskForm.get().title + " has been added for " + owner.name);
        
        return redirect(routes.Planner.displayPlanner());
    }
    
    /**
     * Deletes a task.
     * @param id the Long id of the task to be deleted
     * @return a redirect to Planner.displayPlanner()
     */
    public static Result delete(Long id) {
        Task.delete(id);
        return redirect(routes.Planner.displayPlanner());
    }
}
