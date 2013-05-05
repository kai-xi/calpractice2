package controllers;

import static play.data.Form.form;
import play.*;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import views.html.users.*;

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
    
    // Note: we need something to make sure the user doesn't create an account with the same user name and email.
    // however... maybe we ought to use username as an id instead of email?
    // unless we just want to ask for their name so we can be all "Hello _NAME_!"
	
	/**
     * Handle new user form submission 
     */
    public static Result save() {
        Form<User> userForm = form(User.class).bindFromRequest();
        if(userForm.hasErrors()) {
            return badRequest(createUserForm.render(userForm));
        }
        userForm.get().save();
        flash("success", "Welcome, " + userForm.get().name + "! :o)");
        return redirect(routes.Application.login());
    }
}