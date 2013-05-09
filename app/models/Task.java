package models;

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
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<DateHelper> allDates = new ArrayList<DateHelper>();
    
    public boolean repeatsMon = false;
    public boolean repeatsTues = false;
    public boolean repeatsWed = false;
    public boolean repeatsThurs = false;
    public boolean repeatsFri = false;
    public boolean repeatsSat = false;
    public boolean repeatsSun = false;
	
	@Constraints.Required
	//@Formats.NonEmpty("pattern")
	public String start;
	
	public int startTime;
    
    @Constraints.Required
    public String end;
    
    public int endTime;
    
    public String place;
    
    public String notes;
	
	@ManyToOne
	public User owner;
	
	private static LinkedList<String> dayTracker = new LinkedList<String>();
	private static final int MIL_PER_DAY = 86400000;
	private static final int MIL_PER_WEEK = 86400000 * 7;
	
	public static Model.Finder<Long,Task> find = new Model.Finder(Long.class,Task.class);
    
	public static List<Task> findTasksFor(String userEmail)	{
		return Task.find.where().eq("owner.email",userEmail).findList();
	}
	
	public static Task add(Task t, User u, String start, String end, Date d)	{
		t.owner = u;
		// convert start, end to int types
		int startTime = convertTimeToInt(start);
		int endTime = convertTimeToInt(end);
		t.startTime = startTime;
		t.endTime = endTime;
		DateHelper startDateHelper = new DateHelper(d, t);
		t.allDates.add(startDateHelper);
		addRepeatingDates(t,startDateHelper.date);

		return t;
	}

	private static void initializeDayTracker()	{
		for (Day dayOfWeek : Day.values())	{
			dayTracker.add(dayOfWeek.getText());
		}
	}

	private static Date getDateOfNextRepeatingDay(long original, String startDay, String dayToFindNext)	{
		System.out.println("start date");
		System.out.println(new Date(original).toString());
		System.out.println("dayToFindNext");
		System.out.println(dayToFindNext);
		int i = 0;
		int index = dayTracker.indexOf(startDay);
		System.out.println("index");
		System.out.println(index);
		while (true)	{
			i++;
			index = (index+1) % 7;
			if (dayTracker.get(index).equals(dayToFindNext))	{
				Date nextStartDate = new Date(original + (i * MIL_PER_DAY));
				return nextStartDate;
			}
		}
	}

	/**
	 * Returns a Task with relevant dates for all weeks, if repeatsWeekly is set to true.
	 * @param t The Task to update the allDates field for
	 * @param d The date of the first occurrence of the repeating task
	 * @return
	 */
	private static Task addRepeatingDates(Task t, Date d)	{
		Task.initializeDayTracker();
		long startNumMil = d.getTime();
		long endNumMil = t.repeatUntil.getTime();
		ArrayList<Date> startDates = new ArrayList<Date>();
		startDates.add(d);
		String startDay = d.toString().substring(0,3);

		if (t.repeatsMon && !(startDay.equals("Mon")))	{
			startDates.add(getDateOfNextRepeatingDay(startNumMil, startDay, "Mon"));
		}
		if (t.repeatsTues && !(startDay.equals("Tue")))	{
			startDates.add(getDateOfNextRepeatingDay(startNumMil, startDay, "Tue"));
		}
		if (t.repeatsWed && !(startDay.equals("Wed")))	{
			startDates.add(getDateOfNextRepeatingDay(startNumMil, startDay, "Wed"));
		}
		if (t.repeatsThurs && !(startDay.equals("Thu")))	{
			startDates.add(getDateOfNextRepeatingDay(startNumMil, startDay, "Thu"));
		}
		if (t.repeatsFri && !(startDay.equals("Fri")))	{
			startDates.add(getDateOfNextRepeatingDay(startNumMil, startDay, "Fri"));
		}
		if (t.repeatsSat && !(startDay.equals("Sat")))	{
			startDates.add(getDateOfNextRepeatingDay(startNumMil, startDay, "Sat"));
		}
		if (t.repeatsSun && !(startDay.equals("Sun")))	{
			startDates.add(getDateOfNextRepeatingDay(startNumMil, startDay, "Sun"));
		}

		System.out.println("before adding repeating dates, let us check start and end of period");
		System.out.println(new Date(startNumMil));
		System.out.println(new Date(endNumMil));
		for (Date firstOfRepeating : startDates)	{
			long millisecCounter = firstOfRepeating.getTime();
			while (millisecCounter <= endNumMil)	{
				Date toBeAdded = new Date(millisecCounter);
				DateHelper toBeAddedHelper = new DateHelper(toBeAdded,t);
				t.allDates.add(toBeAddedHelper);
				//toBeAddedHelper.save();
				Ebean.save(toBeAddedHelper);
				System.out.println("just added "+ toBeAddedHelper.date.toString());
				millisecCounter += MIL_PER_WEEK;
			}
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
    	System.out.println("inside Task.findTasksOnDay");
    	System.out.println("searching for tasks on "+date.toString());
    	List<Task> allTasksForCurrentUser = findTasksFor(Application.getCurrentUserEmail());
        List<Task> tasksThatDay = new ArrayList<Task>();
    	for (Task t : allTasksForCurrentUser)	{
    		System.out.println(t.title);
    		List<DateHelper> helpersForThisTask = DateHelper.find.where().eq("task.id",t.id).findList();
    		System.out.println("here are the dates that this task occurs on:");
    		System.out.println("helpersForThisTask:");
    		System.out.println(helpersForThisTask.size());
    		System.out.println("t.allDates.size()");
    		System.out.println(t.allDates.size());
    		ArrayList<Date> datesForThisTask = new ArrayList<Date>();
    		for (DateHelper dh :helpersForThisTask)	{
    			System.out.println(dh.date.toString());
    			datesForThisTask.add(dh.date);
    		}
    		System.out.println("size of datesForThisTask:");
    		System.out.println(datesForThisTask.size());
    		if (datesForThisTask.contains(date))	{
    			System.out.println("this is a relevant task: "+t.title);
    			tasksThatDay.add(t);
    		}
    	}
    	return tasksThatDay;
    }
    
    public static void delete(Long id)	{
    	find.ref(id).delete();
    }
	
}
