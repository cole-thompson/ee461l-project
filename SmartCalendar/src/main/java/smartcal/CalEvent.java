package smartcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.users.User;

/*
 * Very basic event class that contains a user, start, and end time.
 * people: list of people in this event
 * add key for user
 */



public class CalEvent {
	Long id;
	private User creator;
	
	private Date startTime;
	private Date endTime;
	private List<User> people;
	
	private String location;
	private String name;
	private boolean allDay;

	public CalEvent() {
		this.creator = null;
		this.startTime = null;
		this.endTime = null;
		this.people = new ArrayList<User>();
		this.people.add(this.creator);
		this.location = "";
	}

	public CalEvent(User creator, Date startTime, Date endTime) {
		this();
		this.creator = creator;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void addUser(User user) {
		people.add(user);
	}
	
	public void removeUser(User user) {
		people.remove(user);
	}
	
	/* Something to print on the GUI
	 * meant for visualizing events that last <1 day */
	public String getTimeString() {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startTime);
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(endTime);
		
		int startHour = startCal.get(Calendar.HOUR);
		int startAmPm = startCal.getMaximum(Calendar.AM_PM);
		
		int endHour = endCal.get(Calendar.HOUR);
		int endAmPm = endCal.getMaximum(Calendar.AM_PM);
		
		String s = startHour + getAmPmString(startAmPm);
		s += "-";
		s += endHour + getAmPmString(endAmPm);
		return s;
	}
	
	public String getTimeStringFull() {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startTime);
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(endTime);
		
		int startHour = startCal.get(Calendar.HOUR);
		int startMinute = startCal.get(Calendar.MINUTE);
		int startAmPm = startCal.getMaximum(Calendar.AM_PM);
		
		int endHour = endCal.get(Calendar.HOUR);
		int endMinute = endCal.get(Calendar.MINUTE);
		int endAmPm = endCal.getMaximum(Calendar.AM_PM);
		
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
	
	public static CalEvent invitationOptionToEvent(Invitation invitation, InvitationOption option) {
		CalEvent event = new CalEvent(invitation.getCreator(), option.getStartTime(), option.getEndTime());
		event.setLocation(option.getLocation());
		event.setAllDay(option.getAllDay());
		event.setName(invitation.getName());
		return event;
	}
	
	/* GETTERS AND SETTERS */
	
	public List<User> getPeople() {
		return people;
	}

	public void setPeople(List<User> people) {
		this.people = people;
	}
	
	public User getCreator() {
        return creator;
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

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
}
