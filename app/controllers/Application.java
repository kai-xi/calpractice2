package controllers;

import static play.data.Form.form;
import play.*;
import play.mvc.*;
import models.*;
import views.html.*;
import play.data.*;
import play.data.Form.*;

/**
 * This is the Application Controller. It is involved with login/logout, 
 * and passes the correct user information for the index view to be rendered.
 * @author Kaixi Wu (kw2503) & Emily Chen (ec2805)
 *
 */
public class Application extends Controller {
	
	/**
	 * A static inner class that form() can wrap
	 */
	public static class Login	{
		public String email;
		public String password;
		
		/**
		 * If a user exists with that email and password, 
		 * returns no error message. Else, returns an error message to be rendered
		 * in login view.
		 * @return null if user exists, "invalid login" if not
		 */
		public String validate()	{
			if (User.authenticate(email,password)==null)	{
				return "invalid login";
			}
			return null;
		}
	}
	
	/**
	 * Gets the user for the current session and sends
	 * the user's info to be rendered in the index view
	 * @return ok result
	 */
	@Security.Authenticated(CalSecurity.class)
    public static Result index() {
        return ok(index.render(Task.findTasksFor(request().username()),User.find.byId(request().username())));
    }
    
	/**
	 * Renders login form
	 * @return ok Result
	 */
    public static Result login()	{
    	return ok(login.render(Form.form(Login.class)));
    }
    
    /**
     * Creates a new session with the user email if the login form has no errors.
     * @return badRequest if loginForm has errors, else redirects to index() action
     */
    public static Result authenticate()	{
    	Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
    	if (loginForm.hasErrors())	{
    		return badRequest(login.render(loginForm));
    	}	else	{
    		session().clear();
    		session("email",loginForm.get().email);
    		return redirect(routes.Application.index());
    	}
    }
    
    /**
     * Clears the current session.
     * @return redirect to login() action
     */
    public static Result logout()	{
    	session().clear();
    	flash("success","Logging out");
    	return redirect(routes.Application.login());
    }
    
    /**
     * Gets the email of the session user.
     * @return a String of the user's email
     */
    public static String getCurrentUserEmail()	{
    	return session().get("email");
    }
}
