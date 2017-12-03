package smartcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.users.User;



public class InvitationOption {

	
	private String location;
	private Calendar startTime;
	private Calendar endTime;
	private boolean allDay;
	
	public InvitationOption() {
		setLocation("undecided");
		setAllDay(true);
	}
	
	public InvitationOption(String loc) {
		setLocation(loc);
		setAllDay(true);
	}
	public InvitationOption(Calendar start, Calendar end) {
		setLocation("undecided");
		setStartTime(start);
		setEndTime(end);
		setAllDay(false);
	}
	public InvitationOption(String loc, Calendar start, Calendar end) {
		setLocation(loc);
		setStartTime(start);
		setEndTime(end);
		setAllDay(false);
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
		setAllDay(false);
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
		setAllDay(false);
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
	
	
}
