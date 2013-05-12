package controllers;
import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

/**
 * This class implements an authenticator by extending the
 * Play framework's built-in Security.Authenticator.
 * If null, the person issuing the request is not a logged in user and is redirected to
 * the login page; else, the annotated request is allowed to proceed.
 * @author Emily Chen (ec2805) & Kaixi Wu (kw2503)
 *
 */
public class CalSecurity extends Security.Authenticator {
	
	/**
	 * Retrieves the User identifier attribute (in our case, the email address) 
	 * from the HTTP context (the browser's session cookie).
	 * @return 
	 */
	@Override
	public String getUsername(Context ctx)	{
		return ctx.session().get("email");
	}
	
	/**
	 * If getUsername() returns null, the person issuing the request is 
	 * not a logged in user.
	 * @return redirect to the the login() action
	 */
	@Override
	public Result onUnauthorized(Context ctx)	{
		return redirect(routes.Application.login());
	}
}
