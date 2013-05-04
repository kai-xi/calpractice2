package models;
import static play.data.Form.form;

import java.util.*;

import play.data.Form;
import play.data.format.Formats;
import play.db.ebean.*;
import play.mvc.Result;

import javax.persistence.*;

import controllers.CalSecurity;

@Entity
public class Task extends Model	{
	@Id
	public Long id;
	public String desc;
	public boolean done=false;
	//@Formats.DateTime(pattern="MM/dd/yyyy")
	public Date date;
	/*
	 * startTime, endTime specified by number of minutes [int] since midnight, in half hour increments
	 * and can take one of the following values:
	 * 0 (midnight), 30 (12:30am), 60 (1am), 90 (1:30am), 120 (2am), 150 (2:30am), 180 (3am),
	 * 210 (3:30am), 240 (4am), 270 (4:30am), 300 (5am), 330 (5:30am), 360 (6am), 390 (6:30am),
	 * 420 (7am), 450 (7:30am), 480 (8am), 510 (8:30am), 540 (9am), 570 (9:30am),
	 * 600 (10am), 630 (10:30am), 660 (11am), 690 (11:30am), 720 (noon),
	 * 750 (12:30pm), 780 (1pm), 810 (1:30pm), 840 (2pm), 870 (2:30pm), 900 (3pm),
	 * 930 (3:30pm), 960 (4pm), 990 (4:30pm), 1020 (5pm), 1050 (5:30pm), 1080 (6pm),
	 * 1110 (6:30pm), 1140 (7pm), 1170 (7:30pm), 1200 (8pm), 1230 (8:30pm), 1260 (9pm),
	 * 1290 (9:30pm), 1320 (10pm), 1350 (10:30pm), 1380 (11pm), 1410 (11:30pm)
	 */
	public int startTime;
	public int endTime;
	@ManyToOne
	public User owner;
	
	public static Model.Finder<Long,Task> find = new Model.Finder(Long.class,Task.class);
    
	public static List<Task> findTasksFor(String user)	{
		return Task.find.where().eq("done", false).eq("owner.email",user).findList();
	}
	
	public static Task add(Task t, User u)	{
		t.owner = u;
		return t;
	}
	
	public static Map<Integer,String> timeSlots() {
        LinkedHashMap<Integer,String> options = new LinkedHashMap<Integer,String>();
        options.put(1,"one");
        options.put(2,"two");
        return options;
    }
	
	public static Map<Integer,String> alternateTimeSlots() {
        LinkedHashMap<Integer,String> options = new LinkedHashMap<Integer,String>();
        options.put(3,"three");
        options.put(4,"four");
        return options;
    }
}
