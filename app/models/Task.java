package models;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import play.db.ebean.*;
import play.db.ebean.Model.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;
import controllers.Application;


@Entity
public class Task extends Model	{
	@Id
    public Long id;
    
    @Constraints.Required
    public String title;
    
    @Constraints.Required
    public String taskType;
    
    @Constraints.Required
	public Date date;
    
    public Date repeatUntil;
 
    public boolean repeatsMon = false;
    public boolean repeatsTues = false;
    public boolean repeatsWed = false;
    public boolean repeatsThurs = false;
    public boolean repeatsFri = false;
    public boolean repeatsSat = false;
    public boolean repeatsSun = false;
	
	@Constraints.Required
	public String start;
	
	public int startTime;
    
    @Constraints.Required
    public String end;
    
    public int endTime;
    
    public String place;
    
    public String notes;
	
	@ManyToOne
	public User owner;
	
	
	public static Model.Finder<Long,Task> find = new Model.Finder(Long.class,Task.class);
    
	public static List<Task> findTasksFor(String userEmail)	{
		return Task.find.where().eq("owner.email",userEmail).orderBy("startTime").findList();
	}
	
	public static Task add(Task t, User u, String start, String end, Date d)	{
		t.owner = u;
		// convert start, end to int types
		int startTime = convertTimeToInt(start);
		int endTime = convertTimeToInt(end);
		t.startTime = startTime;
		t.endTime = endTime;
		if (!t.repeatsMon && !t.repeatsTues && !t.repeatsWed && !t.repeatsThurs &&!t.repeatsFri && !t.repeatsSat && !t.repeatsSun){
			t.repeatUntil = d;
		}
		return t;
	}
	
	private static int convertTimeToInt(String formInput)	{
		String[] data = formInput.split(" ");
		String[] hourAndMin = data[0].split(":");
		int hourData = Integer.parseInt(hourAndMin[0]);
		int minData = Integer.parseInt(hourAndMin[1]);
		int result = 0;

		if (data[1].equals("pm"))	{
			result += 60*12;
		}
		if (data[1].equals("am") && hourData==12)	{
			if (minData==0)	{
			System.out.println("midnight");
			return 0;
			}
			else	{
				hourData = 0;
			}
		}
		result += 60*hourData;
		result += minData;
		return result;
	}
	
    public static List<Task> findTasksOnDay(Date date) {
    	Calendar thisDate = Calendar.getInstance();
    	thisDate.setTime(date);
    	int dayOfWeekNum = thisDate.get(Calendar.DAY_OF_WEEK);
    	
    	List<Task> allTasksForCurrentUser = findTasksFor(Application.getCurrentUserEmail());
    	List<Task> tasksThisDay = new ArrayList<Task>();
    	
    	for(Task task: allTasksForCurrentUser){
    		if (task.date.equals(date)){
    			tasksThisDay.add(task);
    		}
    		else if (task.repeatUntil.after(date) || task.repeatUntil.equals(date)){
    			if (dayOfWeekNum == 1 && task.repeatsSun){
        			tasksThisDay.add(task);
        		}
        		else if (dayOfWeekNum == 2 && task.repeatsMon){
        			tasksThisDay.add(task);
        		}
        		else if (dayOfWeekNum == 3 && task.repeatsTues){
        			tasksThisDay.add(task);
        		}
        		else if (dayOfWeekNum == 4 && task.repeatsWed){
        			tasksThisDay.add(task);
        		}
        		else if (dayOfWeekNum == 5 && task.repeatsThurs){
        			tasksThisDay.add(task);
        		}
        		else if (dayOfWeekNum == 6 && task.repeatsFri){
        			tasksThisDay.add(task);
        		}
        		else if (dayOfWeekNum == 7 && task.repeatsSat){
        			tasksThisDay.add(task);
        		}
    		}
    	}
 
        return tasksThisDay;
    }
    
    public static void delete(Long id)	{
    	find.ref(id).delete();
    }
	
}
