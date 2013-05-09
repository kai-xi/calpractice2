/**
 * 
 */
package models;

import java.util.*;
import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

import controllers.Application;

@Entity
public class DateHelper extends Model {
	@Id
    public Long id;
	public Date date;

	@ManyToOne
	public Task task;

	public static Model.Finder<Long,DateHelper> find = new Model.Finder(Long.class,DateHelper.class);

	public DateHelper(Date d, Task t)	{
		this.date = d;
		this.task = t;
	}
}
