package smartcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.google.appengine.api.users.User;
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
	private int displayMonth;		//auto current month = -1
	private int displayYear;		//auto current year = -1
	
	public UserDisplayData() {
		this.user = null;
		displayMonth = -1;
		displayYear = -1;
		displayEvents = new HashMap<Integer, ArrayList<CalEvent>>();
	}
	
	//test
	public void createTestEvent() {
		ArrayList<CalEvent> l = new ArrayList<CalEvent>();
		CalEvent e = new CalEvent(user, Calendar.getInstance(), Calendar.getInstance());
		l.add(e);
		displayEvents.put(10, l);
	}
	
	public UserDisplayData(User user) {
		this.user = user;
		displayMonth = -1;
		displayYear = -1;
		displayEvents = new HashMap<Integer, ArrayList<CalEvent>>();
	}
	
	/* populate displayEvents
	 * loads a User's events for the display month/year into displayEvents list with objectify
	 */
	public void loadDisplayEvents() {
		//TODO
	}
	
	public ArrayList<CalEvent> getDisplayEvents(int day) {
		return displayEvents.get(day);
	}
	
	
	/* GETTERS AND SETTERS */
	
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
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

}

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
