package models;
import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

/*
 *  @Entity annotation marks this class as a managed Ebean entity.
 *  All fields of this class will be automatically persisted to the database.
 *  By extending play.db.ebean.Model, this class gets access to JPA helpers.
 *  JPA (Java Persistence API) is a Java framework for managing relational data in apps using Java Platform.
 */
@Entity
public class User extends Model {
	@Id
	public String email;
	public String name;
	public String password;
	// this field will be used to make queries
	public static Finder<String,User> find = new Finder<String,User>(String.class,User.class);
	
	public User(String email, String name, String password)	{
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	/*
	 * This method returns the User if the email and password submitted in the form are correct.
	 */
	public static User authenticate(String email,String password)	{
		return find.where().eq("email", email).eq("password", password).findUnique();
	}
}
