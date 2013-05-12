package models;
import static play.data.Form.form;

import javax.persistence.*;

import play.data.Form;
import play.db.ebean.*;
import play.mvc.Result;

import com.avaje.ebean.*;

import controllers.routes;

/**
 * The User Model stores information for session management.
 * @author Emily Chen (ec2805) & Kaixi Wu (kw2503)
 *
 */
@Entity
public class User extends Model {
	@Id
	public String email;
	public String name;
	public String password;
	
	public static Finder<String,User> find = new Finder<String,User>(String.class,User.class);
	
	/**
	 * Sets the fields of the user.
	 * @param email a String for the user's email
	 * @param name a String for the user's name
	 * @param password a String for the user's password
	 */
	public User(String email, String name, String password)	{
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	/**
	 * This method returns the User if the email and password submitted in the form are correct.
	 * @return the User
	 */
	public static User authenticate(String email,String password)	{
		return find.where().eq("email", email).eq("password", password).findUnique();
	}
}
