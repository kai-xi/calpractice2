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
	
    public static Result index() {
        return ok(index.render(Task.find.all()));
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
    		session("email",loginForm.get().email);
    		return redirect(routes.Application.index());
    	}
    }
    
    
}
