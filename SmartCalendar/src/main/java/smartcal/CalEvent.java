package smartcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/*
 * Very basic event class that contains a user, start, and end time.
 * people: list of people in this event
 */


@Entity
public class CalEvent {
	@Id Long id;
	private User creator;
	private Calendar startTime;
	private Calendar endTime;
	private List<User> people;
	private String location;
	
	public CalEvent(User creator, Calendar startTime, Calendar endTime) {
		this.creator = creator;
		this.startTime = startTime;
		this.endTime = endTime;
		people = new ArrayList<User>();
		people.add(creator);
		location = "";
	}
	
	public void addUser(User user) {
		people.add(user);
	}
	
	
	/* GETTERS AND SETTERS */
	
	public User getCreator() {
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
}
