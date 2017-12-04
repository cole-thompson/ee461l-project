package smartcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Stringify;
import com.googlecode.objectify.stringifier.Stringifier;

/*
 * Used to keep track of info about the user's current display
 * or retain the information between sessions
 */

@Entity
public class UserDisplayData {
	@Id Long id;
	@Index User user;
	@Stringify(IntegerStringifier.class)
	
	//events are stored for this month, keyed in the map by day
	//easy to look up events for a day when the calendar gui is being created on a loop
	private HashMap<Integer, ArrayList<CalEvent>> displayEvents;
	
	private int displayView;		//0=month view, 1=week view, 2=day view
	private int displayMonth;		//auto current month = -1
	private int displayYear;		//auto current year = -1
	private int displayWeek;		//for week view
	private int displayDate;		//for day view
	

	public UserDisplayData() {
		this.user = null;
		displayView = 0;
		displayEvents = new HashMap<Integer, ArrayList<CalEvent>>();
		displayMonth = -1;
		displayYear = -1;
		displayWeek = -1;
		displayDate = -1;
	}
	
	public UserDisplayData(User user) {
		this();
		this.user = user;
	}
	
	
	/* populate displayEvents
	 * loads a User's events from objectify
	 * for the display view into displayEvents HashMap, store events by date #
	 */
	public void loadDisplayEvents() {
		CalEventList eventList = ObjectifyService.ofy().load().type(CalEventList.class).filter("user", user).first().now();
		List<CalEvent> events = eventList.getEvents();
		for (CalEvent event : events) {
			Date dayDate = event.getStartTime();
			Calendar day = Calendar.getInstance();
			day.setTime(dayDate);
			if (day.get(Calendar.MONTH) == displayMonth) {
				int date = day.get(Calendar.DATE);
				if (getDisplayView() == 2) {
					if (date == displayDate) {
						addDisplayEvent(date, event);
					}
				}
				else {
					addDisplayEvent(date, event);
				}
			}
		}
	}
	
	public ArrayList<CalEvent> getDisplayEvents(int day) {
		return displayEvents.get(day);
	}
	
	public void addDisplayEvent(int date, CalEvent event) {
		ArrayList<CalEvent> dayEvents = displayEvents.get(date);
		if (dayEvents == null) {
			dayEvents = new ArrayList<CalEvent>();
		}
		dayEvents.add(event);
		displayEvents.put(date, dayEvents);
	}
	
	public void displayToday() {
		Calendar c = new GregorianCalendar();
		displayMonth = c.get(Calendar.MONTH);
		displayYear = c.get(Calendar.YEAR);
		displayWeek = c.get(Calendar.WEEK_OF_MONTH);
		displayDate = c.get(Calendar.DATE);
	}
	
	/* Returns the date of the start of this week
	 * If that day is in previous month, return negative number
	 */
	public int getDisplayWeekStartDate() {
		Calendar c;
		if (displayWeek == -1) {
			c = new GregorianCalendar();
			displayToday();
		}
		else {
			c = new GregorianCalendar(displayYear, displayMonth, 1);
			c.set(Calendar.WEEK_OF_MONTH, displayWeek);
		}
		
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		int startDate = c.get(Calendar.DATE);
		
		if (c.get(Calendar.MONTH) != displayMonth) {
			int numDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			startDate = startDate - numDays;
		}
		
		//System.out.println(displayWeek + " " + startDate);
		return startDate;
	}
	
	/* Increment the display week forward or backwards one week
	 * Cycle to next or prev month if needed, set all vars
	 */
	public void changeWeek(boolean forward) {
		Calendar c;
		if (displayWeek == -1) {
			c = new GregorianCalendar();
			displayToday();
		}
		else {
			c = new GregorianCalendar(displayYear, displayMonth, 1);
		}
		
		if (forward) {		//increment week up
			int numDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			c = new GregorianCalendar(displayYear, displayMonth, numDays);
			int numWeeks = c.get(Calendar.WEEK_OF_MONTH);
			if (displayWeek + 1 <= numWeeks) {
				displayWeek++;
			}
			else {
				changeMonth(true);
				displayWeek = 1;
			}
		}
		else {				//increment week down
			if (displayWeek - 1 >= 1) {
				displayWeek--;
			}
			else {
				changeMonth(false);
				c = new GregorianCalendar(displayYear, displayMonth, 1);
				int numDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
				c = new GregorianCalendar(displayYear, displayMonth, numDays);
				int numWeeks = c.get(Calendar.WEEK_OF_MONTH);
				displayWeek = numWeeks;
			}
		}
	}

	public void changeMonth(boolean forward) {
		if (displayMonth == -1) {
			displayToday();
		}
		
		if (forward) {		//increment month up
			if (displayMonth + 1 <= 11) {
				displayMonth++;
			}
			else {
				displayMonth = 1;
				displayYear++;
			}
		}
		else {				//increment month down
			if (displayMonth - 1 >=0) {
				displayMonth--;
			}
			else {
				displayMonth = 11;
				displayYear--;
			}
		}
	}
	
	/* Increment the display day forward or backwards
	 * Cycle to next or prev month if needed, set all vars
	 */
	public void changeDay(boolean forward) {
		//TODO
	}
	
	
	/* GETTERS AND SETTERS */
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setDisplayMonth(int displayMonth) {
		this.displayMonth = displayMonth;
	}
	
	public int getDisplayMonth() {
		return this.displayMonth;
	}
	
	public void setDisplayYear (int displayYear) {
		this.displayYear = displayYear;
	}
	
	public int getDisplayYear() {
		return this.displayYear;
	}

	public int getDisplayWeek() {
		return displayWeek;
	}

	public void setDisplayWeek(int displayWeek) {
		this.displayWeek = displayWeek;
	}

	public int getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(int displayDate) {
		this.displayDate = displayDate;
	}

	public int getDisplayView() {
		return displayView;
	}

	public void setDisplayView(int displayView) {
		if (displayView < 0 || displayView > 2) {
			displayView = 0;
		}
		this.displayView = displayView;
	}

	
	/* STATIC FUNCTIONS to help with displaying month and year with forms*/
	
	/*
	 * zero-indexed months, can take Calendar.Month inputs
	 */
	public static String getMonthName(Integer m) {
		if (m == null)
			return "";
		String name = "";
		switch (m) {
			case 0: 	name = "January";break;
			case 1: 	name = "February";break;
			case 2: 	name = "March";break;
			case 3: 	name = "April";break;
			case 4: 	name = "May";break;
			case 5: 	name = "June";break;
			case 6: 	name = "July";break;
			case 7: 	name = "August";break;
			case 8: 	name = "September";break;
			case 9: 	name = "October";break;
			case 10: 	name = "November";break;
			case 11: 	name = "December";break;
		}
		return name;
	}

	public static int getMonthInt(String mString) {
		if (mString == null)
			return -1;
		int m = -1;
		switch (mString) {
			case "January": 	m = 0;break;
			case "February": 	m = 1;break;
			case "March": 		m = 2;break;
			case "April": 		m = 3;break;
			case "May": 		m = 4;break;
			case "June": 		m = 5;break;
			case "July": 		m = 6;break;
			case "August": 		m = 7;break;
			case "September": 	m = 8;break;
			case "October": 	m = 9;break;
			case "November": 	m = 10;break;
			case "December": 	m = 11;break;
			default: 			m = -1;break;
		}
		return m;
	}

	public static int getYear(String yearString) {
		int maxYear = 4000;
		int minYear = 0;
		int year = -1;
		try {
			//check bounds of year
			year = Integer.parseInt(yearString);
			if (year > maxYear || year < minYear) {
				year = -1;
			}
		}
		catch (Exception e) {
			year = -1;
		}
		return year;
	}

	public static String getDayName(int dayOfWeek) {
		String dayName = "";
		switch (dayOfWeek) {
			case 0: 	dayName = "Sunday";break;
			case 1: 	dayName = "Monday";break;
			case 2: 	dayName = "Tuesday";break;
			case 3: 	dayName = "Wednesday";break;
			case 4: 	dayName = "Thursday";break;
			case 5: 	dayName = "Friday";break;
			case 6: 	dayName = "Saturday";break;
		}
		return dayName;
	}
}

//this is a required class for an Objectify Entity to contain a HashMap with a non-string key
class IntegerStringifier implements Stringifier<Integer> {
    @Override
    public String toString(Integer obj) {
        return obj.toString();
    }

    @Override
    public Integer fromString(String obj) {
        return Integer.parseInt(obj);
    }
}
