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
	private List<User> availablePeople;
	private String optionName;
	
	public InvitationOption() {
		setLocation("undecided");
		setAllDay(true);
		setAvailablePeople(new ArrayList<User>());
		setStartTime(null);
		setEndTime(null);
		setOptionName("option");
	}
	
	public InvitationOption(String loc) {
		this();
		setLocation(loc);
	}
	public InvitationOption(Date start, Date end) {
		this();
		setStartTime(start);
		setEndTime(end);
		setAllDay(false);
	}
	public InvitationOption(String loc, Date start, Date end) {
		this();
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

	public List<User> getAvailablePeople() {
		return availablePeople;
	}

	public void setAvailablePeople(List<User> availablePeople) {
		this.availablePeople = availablePeople;
	}
	
	public void addAvailablePerson(User availablePerson) {
		this.availablePeople.add(availablePerson);
	}
	
	public int numAvailablePeople() {
		return this.availablePeople.size();
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	
	public static Date dayStringToDate(String day) {
		Date cal = null;
		try {
			if (day.length() == 10) {
				int y = Integer.parseInt(day.substring(0, 4));
				int m = Integer.parseInt(day.substring(5, 7));
				int d = Integer.parseInt(day.substring(8));
				cal = new Date(y, m - 1, d);
			}
		}
		catch (NumberFormatException e) {}
		
		return cal;
	}
	
	public static Date dayTimeStringToDate(String day, String time) {
		Date cal = null;
		try {
			if (day.length() == 10 && time.length() == 5) {
				int y = Integer.parseInt(day.substring(0, 4));
				int m = Integer.parseInt(day.substring(5, 7));
				int d = Integer.parseInt(day.substring(8));
				int h = Integer.parseInt(time.substring(0, 2));
				int min = Integer.parseInt(time.substring(3, 5));				
				cal = new Date(y, m - 1, d, h, min);
			}
		}
		catch (NumberFormatException e) {}
		
		return cal;
	}
}
