/**
 * 
 */
package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import play.data.validation.Constraints;
import play.db.ebean.Model;

/**
 * @author Kaixi
 *
 */
public class Display extends Model {
	
	
	public Date centerDate;
	
    public static List<List<Task>> toDisplay(Date day){
    	
    	List<List<Task>> tasksToDisplay = new ArrayList<List<Task>>();
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(day);
    	cal.set(Calendar.HOUR_OF_DAY,0);
    	cal.set(Calendar.MINUTE,0);
    	cal.set(Calendar.SECOND,0);
    	cal.set(Calendar.MILLISECOND,0);
    	
    	cal.add(Calendar.DATE, -1);
    	Date before = cal.getTime();
    	tasksToDisplay.add(Task.findTasksOnDay(before));
    	
    	cal.add(Calendar.DATE, 1);
    	Date center = cal.getTime();
    	tasksToDisplay.add(Task.findTasksOnDay(center));
    	
    	cal.add(Calendar.DATE, 1);
    	Date day2 = cal.getTime();
    	tasksToDisplay.add(Task.findTasksOnDay(day2));
    	
    	cal.add(Calendar.DATE, 1);
    	Date day3 = cal.getTime();
    	tasksToDisplay.add(Task.findTasksOnDay(day3));
    	
    	return tasksToDisplay;
    }
	
}
