package smartcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class MovieOption {
	@Id Long id;
	@Index UserAccount creator;

	private String location;
	private Calendar startTime;
	private Calendar endTime;
	private boolean allDay;
	
	public MovieOption(String loc) {
		setLocation(loc);
		setAllDay(true);
	}
	public MovieOption(Calendar start, Calendar end) {
		setLocation("undecided");
		setStartTime(start);
		setEndTime(end);
	}
	public MovieOption(String loc, Calendar start, Calendar end) {
		setLocation(loc);
		setStartTime(start);
		setEndTime(end);
	}
	
	//Getters and Setters

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}	
}
