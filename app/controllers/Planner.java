/**
 * 
 */
package controllers;

import static play.data.Form.form;

import java.util.*;

import play.*;
import play.mvc.*;
import models.*;
import models.Display;
import views.html.*;
import views.html.display.*;
import play.data.*;
import play.data.Form.*;

/**
 * @author Kaixi
 *
 */
public class Planner extends Controller{

static Form<Display> centerForm = Form.form(Display.class);
	
	@Security.Authenticated(CalSecurity.class)
	public static Result displayPlanner(){
		User currentUser = User.find.where().eq("email",session().get("email")).findUnique();
		Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY,0);
    	cal.set(Calendar.MINUTE,0);
    	cal.set(Calendar.SECOND,0);
    	cal.set(Calendar.MILLISECOND,0);
    	Date center = cal.getTime();
		
		if(centerForm.hasErrors()) {
		    return badRequest(planner.render((Display.toDisplay(center, currentUser)), centerForm, currentUser));
		}
		else if(centerForm.bindFromRequest().get().centerDate == null) {
			return ok(planner.render((Display.toDisplay(center, currentUser)), centerForm, currentUser));
		}
		else {
			center = centerForm.bindFromRequest().get().centerDate;

		    return ok(planner.render((Display.toDisplay(center, currentUser)), centerForm, currentUser)); 
		}
		
	}
	
	
}
