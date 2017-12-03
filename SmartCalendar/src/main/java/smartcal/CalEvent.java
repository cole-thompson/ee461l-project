package smartcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/*
 * Very basic event class that contains a user, start, and end time.
 * people: list of people in this event
 * add key for user
 */


@Entity
public class CalEvent {
	@Id Long id;
	@Index UserAccount creator;
	
	@Index private Calendar startTime;
	@Index private Calendar endTime;
	@Index private List<UserAccount> people;
	
	private String location;
	private String name;
	


	public CalEvent(UserAccount creator, Calendar startTime, Calendar endTime) {
		this.creator = creator;
		this.startTime = startTime;
		this.endTime = endTime;
		people = new ArrayList<UserAccount>();
		people.add(this.creator);
		location = "";
	}
	
	public void addUser(UserAccount user) {
		people.add(user);
	}
	
	/* Something to print on the GUI
	 * meant for visualizing events that last <1 day */
	public String getTimeString() {
		int startHour = startTime.get(Calendar.HOUR);
		int startAmPm = startTime.getMaximum(Calendar.AM_PM);
		
		int endHour = endTime.get(Calendar.HOUR);
		int endAmPm = endTime.getMaximum(Calendar.AM_PM);
		
		String s = startHour + getAmPmString(startAmPm);
		s += "-";
		s += endHour + getAmPmString(endAmPm);
		return s;
	}
	
	public String getTimeStringFull() {
		int startHour = startTime.get(Calendar.HOUR);
		int startMinute = startTime.get(Calendar.MINUTE);
		int startAmPm = startTime.getMaximum(Calendar.AM_PM);
		
		int endHour = endTime.get(Calendar.HOUR);
		int endMinute = endTime.get(Calendar.MINUTE);
		int endAmPm = endTime.getMaximum(Calendar.AM_PM);
		
		String s = startHour + ":" + startMinute + getAmPmString(startAmPm);
		s += " - ";
		s += endHour + ":" + endMinute + getAmPmString(endAmPm);
		return s;
	}
	
	private String getAmPmString(int ampm) {
		String s = "";
		switch (ampm) {
			case (Calendar.PM): s = "PM"; break;
			case (Calendar.AM): s = "AM"; break;
			default:break;
		}
		return s;
	}
	
	/* GETTERS AND SETTERS */
	
	public UserAccount getCreator() {
        return creator;
    }
	
	public Calendar getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	public Calendar getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
