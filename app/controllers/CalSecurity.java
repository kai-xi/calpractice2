package controllers;
import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

/**
 * This class implements an authenticator by extending the
 * Play framework's built-in Security.Authenticator.
 * When a method is annotated with @Security.Authenticated(T),
 * getUsername() retrieves the User identifier attribute (in our case, the email address) 
 * from the HTTP context (the browser's session cookie). HTTP requests without cookies are stateless
 * but when the browser sends a request with a cookie, that cookie contains session state information
 * for the server.
 * If null, the person issuing the request is not a logged in user and is redirected to
 * the login page; else, the annotated request is allowed to proceed.
 * @author EmilyChen
 *
 */
public class CalSecurity extends Security.Authenticator {
	@Override
	public String getUsername(Context ctx)	{
		System.out.println("inside getUsername");
		String userEmail = ctx.session().get("email");
		System.out.println(userEmail);
		return ctx.session().get("email");
	}
	
	@Override
	public Result onUnauthorized(Context ctx)	{
		System.out.println("inside onUnauthorized");
		String userEmail = Http.Context.current().session().get("email");
		System.out.println(userEmail);
		return redirect(routes.Application.login());
	}
}
