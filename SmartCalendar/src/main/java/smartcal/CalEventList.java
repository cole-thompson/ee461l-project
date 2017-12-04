package smartcal;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class CalEventList {
	@Id Long id;
	@Index private User user;
	private List<CalEvent> events;
	
	public CalEventList() {
		this.user = null;
		events = new ArrayList<CalEvent>();
	}
	
	public CalEventList(User user) {
		this.user = user;
		events = new ArrayList<CalEvent>();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CalEvent> getEvents() {
		return events;
	}

	public void setEvents(List<CalEvent> events) {
		this.events = events;
	}
	
	public void addEvent(CalEvent event) {
		this.events.add(event);
	}
	
	@Override
	public String toString() {
		String allEvents = "";
		for(CalEvent ev : events) {
			allEvents += ev.getName() + " " + ev.getTimeStringFull() + "\n";
		}
		return allEvents;
	}
}
