package controllers;

import static play.data.Form.form;
import play.*;
import play.mvc.*;
import models.*;
import views.html.*;
import play.data.*;
import play.data.Form.*;

public class Application extends Controller {
	
	/*
	 * A static inner class that form() can wrap
	 */
	public static class Login	{
		public String email;
		public String password;
		
		public String validate()	{
			if (User.authenticate(email,password)==null)	{
				return "invalid login";
			}
			return null;
		}
	}
	
	@Security.Authenticated(CalSecurity.class)
    public static Result index() {
		/*
		 * Task.findTasksFor(String userEmail)
		 * play.mvc.Http.Context.request() returns the current request, a play.Http.Request instance.
		 * play.Http.Request.username() returns the username for the request.
		 * 
		 * Model.Finder.byId() returns an instance of that model byId
		 */
        return ok(index.render(Task.findTasksFor(request().username()),User.find.byId(request().username())));
    }
    
    public static Result login()	{
    	return ok(login.render(Form.form(Login.class)));
    }
    
    public static Result authenticate()	{
    	Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
    	if (loginForm.hasErrors())	{
    		return badRequest(login.render(loginForm));
    	}	else	{
    		session().clear();
    		// create a session with an email attribute, will fetch this attribute
    		session("email",loginForm.get().email);
    		return redirect(routes.Application.index());
    	}
    }
    
    public static Result logout()	{
    	session().clear();
    	// flash similar to session but scope lasts only until next request
    	flash("success","Logging out");
    	return redirect(routes.Application.login());
    }    
}
