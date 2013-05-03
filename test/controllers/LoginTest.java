package controllers;
import org.junit.*;
import static org.junit.Assert.*;
import play.mvc.Result;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class LoginTest {
	@Test
	public void authenticated()	{
		Result result = callAction(controllers.routes.ref.Application.index(),
				fakeRequest().withSession("email","bob@example.com"));
		assertEquals(200,status(result));
	}
	
	@Test
	public void notAuthenticated()	{
		Result result = callAction(controllers.routes.ref.Application.index(),fakeRequest());
		assertEquals(303,status(result));
		assertEquals("/login",header("Location",result));
	}
}
