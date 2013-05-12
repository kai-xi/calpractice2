package controllers;

import static play.data.Form.form;

import javax.persistence.PersistenceException;

import play.*;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import views.html.users.*;

/**
 * This controller handles the onboarding for new users.
 * @author EmilyChen
 *
 */
public class Users extends Controller {
    
	/**
     * Display the new user form.
     */
    public static Result create() {
        Form<User> userForm = form(User.class);
        return ok(
            createUserForm.render(userForm)
        );
    }
    
	/**
     * Handle new user form submission 
     */
    public static Result save() {
    	Form<User> userForm = form(User.class).bindFromRequest();
		if(userForm.hasErrors()) {
            return badRequest(createUserForm.render(userForm));
        }
    	try	{
    		userForm.get().save();
    	}	catch (PersistenceException primkeyNotUnique)	{
    		flash("error", "An account already exists for the email: " + userForm.get().email);
    		return badRequest(createUserForm.render(userForm));
    	}
    	flash("success", "Welcome, " + userForm.get().name + "! :o)");
        return redirect(routes.Application.login());
    }
}