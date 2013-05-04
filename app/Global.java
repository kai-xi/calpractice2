import play.*;
import play.libs.*;
import com.avaje.ebean.*;
import models.*;
import java.util.*;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app)	{
		System.out.println("hello");
		Object all = Yaml.load("initial-data.yml");
		System.out.println("read initial-data");
		//int numUsers = User.find.findRowCount();
		int numTasks = Task.find.findRowCount();
		//System.out.println(numUsers);
		System.out.println(numTasks);
		if (User.find.findRowCount()==0)	{
			Ebean.save((List) all);
		}
		for (Task t:Task.find.all())	{
			String desc = t.title;
			System.out.println(desc);
			Date d = t.date;
			System.out.println(d.toString());
			int start = t.startTime;
			int end = t.endTime;
			System.out.format("%d to %d", start, end);
		}
	}
}