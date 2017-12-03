package smartcal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.users.User;



public class InvitationOption {

	
	private String location;
	private Date startTime;
	private Date endTime;
	private boolean allDay;
	
	public InvitationOption() {
		setLocation("undecided");
		setAllDay(true);
	}
	
	public InvitationOption(String loc) {
		setLocation(loc);
		setAllDay(true);
	}
	public InvitationOption(Date start, Date end) {
		setLocation("undecided");
		setStartTime(start);
		setEndTime(end);
		setAllDay(false);
	}
	public InvitationOption(String loc, Date start, Date end) {
		setLocation(loc);
		setStartTime(start);
		setEndTime(end);
		setAllDay(false);
	}
	
	public String getTimeString() {
		int length = 16;
		if (allDay) {
			length = 10;
		}
		return getStartTime().toString().substring(0, length) + " to " + getEndTime().toString().substring(0, length);
		
	}
	
	//Getters and Setters

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
	
	
}
