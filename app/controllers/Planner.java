/**
 * 
 */
package controllers;

import static play.data.Form.form;

import java.text.SimpleDateFormat;
import java.util.*;

import play.*;
import play.mvc.*;
import models.*;
import views.html.*;
import views.html.display.*;
import play.data.*;
import play.data.Form.*;
import play.db.ebean.Model;

/**
 * The Planner Controller sends requested dates to the planner view.
 * @author Kaixi Wu (kw2503) & Emily Chen (ec2805)
 *
 */
public class Planner extends Controller{

static Form<Display> centerForm = Form.form(Display.class);

	/**
	 * A static inner class for Form to wrap.
	 */
	public static class Display {
		public Date centerDate;
		
		/**
		 * Creates a List of four Lists of Tasks using a center date.
		 * The first list is the tasks for the previous day. The second is for the center date.
		 * The third if for the day after the center date, and the fourth is for two days after the center date.
		 */
		public static List<List<Task>> toDisplay(Date day){
	    	
	    	List<List<Task>> tasksToDisplay = new ArrayList<List<Task>>();
	    	
	    	Calendar cal = Calendar.getInstance();
	    	cal.setTime(day);
	    	cal.set(Calendar.HOUR_OF_DAY,0);
	    	cal.set(Calendar.MINUTE,0);
	    	cal.set(Calendar.SECOND,0);
	    	cal.set(Calendar.MILLISECOND,0);
	    	
	    	cal.add(Calendar.DATE, -1);
	    	Date before = cal.getTime();
	    	tasksToDisplay.add(Task.findTasksOnDay(before));
	    	
	    	cal.add(Calendar.DATE, 1);
	    	Date center = cal.getTime();
	    	tasksToDisplay.add(Task.findTasksOnDay(center));
	    	
	    	cal.add(Calendar.DATE, 1);
	    	Date day2 = cal.getTime();
	    	tasksToDisplay.add(Task.findTasksOnDay(day2));
	    	
	    	cal.add(Calendar.DATE, 1);
	    	Date day3 = cal.getTime();
	    	tasksToDisplay.add(Task.findTasksOnDay(day3));
	    	
	    	return tasksToDisplay;
	    }
	}
	
	/**
	 * Gets information about the centerDate (the middle date to be displayed in the planner view)
	 * from the centerForm that is submitted by the user.
	 * If the form has errors, then the user must resubmit it.
	 * If the form is not submitted, then the default center date is the current date.
	 * Otherwise, the center date is set to the user's submitted date.
	 * The toDisplay method of Display is called to get the lists of tasks.
	 * The tasks for each day, the center form, the current user, 
	 * and the date to be displayed for the center date are sent to the planner view to be rendered.
	 * @return badRequest if the form has errors, else ok Result to the planner view
	 */
	@Security.Authenticated(CalSecurity.class)
	public static Result displayPlanner(){
		User currentUser = User.find.where().eq("email",session().get("email")).findUnique();
		Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY,0);
    	cal.set(Calendar.MINUTE,0);
    	cal.set(Calendar.SECOND,0);
    	cal.set(Calendar.MILLISECOND,0);
    	Date center = cal.getTime();
    	
    	String dateDisplayed = new SimpleDateFormat("EE").format(center) + ", ";
		dateDisplayed += new SimpleDateFormat("MM/dd/yy").format(center);
		
		if(centerForm.hasErrors()) {
		    return badRequest(planner.render((Display.toDisplay(center)), centerForm, currentUser, dateDisplayed));
		}
		else if(centerForm.bindFromRequest().get().centerDate == null) {
			return ok(planner.render((Display.toDisplay(center)), centerForm, currentUser, dateDisplayed));
		}
		else {
			center = centerForm.bindFromRequest().get().centerDate;
			dateDisplayed = new SimpleDateFormat("EE").format(center) + ", ";
			dateDisplayed += new SimpleDateFormat("MM/dd/yy").format(center);
		    return ok(planner.render((Display.toDisplay(center)), centerForm, currentUser, dateDisplayed)); 
		}
		
	}
	
	
}
