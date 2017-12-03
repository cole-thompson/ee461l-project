package smartcal;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/*
 * Servlet to handle stuff from the Calendar module
 */

public class CalendarServlet extends HttpServlet {
	static {
        ObjectifyService.register(CalEvent.class);
        ObjectifyService.register(UserDisplayData.class);
        ObjectifyService.register(UserAccount.class);

    }
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get the current user
    	User user = UserServiceFactory.getUserService().getCurrentUser();
    	
    	UserAccount userAccount = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).filter("user", user).first().now();
        if (userAccount == null) {
        	//TODO redirect to a new page
        	userAccount = new UserAccount(user, user.getNickname());
        }
    	
    	//get UserDisplayData for the User, create new one if needed
       	UserDisplayData display = ObjectifyService.ofy().load().type(UserDisplayData.class).filter("user", userAccount).first().now();
       	if (display == null) {
       		display = new UserDisplayData(userAccount);
       	}
    	
       	//TODO there is probably a better way to do this part
       	//basically only 1 form can redirect here at a time, so handle the one that comes in
       	
       	//check all the different forms in calendar.jsp
       	if (checkTodayButton(display, req)) {}
       	else if (checkWeekLRButtons(display, req)) {}
       	else if (checkViewButtons(display, req)) {}
       	else {
       		updateCalendarDisplayMonth(display, req);
       	}
        
        //save in objectify
        ObjectifyService.ofy().save().entity(display); 
        
        //redirect back to calendar.jsp
        resp.sendRedirect("/calendar.jsp");
    }
    
    /* check the today button
     * sets parameters to -1
     * TODO considering using something different for auto current day
     */
    private boolean checkTodayButton(UserDisplayData display, HttpServletRequest req) {
    	boolean pressed = false;
    	//check the "Today" button
        if (req.getParameter("today") != null) {
        	display.setDisplayMonth(-1);
            display.setDisplayYear(-1);
            display.setDisplayDate(-1);
            display.setDisplayWeek(-1);
            pressed = true;
        }
    	System.out.println(getUserName(display.getUser()) + " is viewing current day");
    	return pressed;
    }
    
    /* Update Month Week Day view, changeView form
     * Check buttons, return true if they were pressed
     */
    private boolean checkViewButtons(UserDisplayData display, HttpServletRequest req) {
    	boolean pressed = false;
    	if (req.getParameter("monthView-btn") != null) {
    		display.setDisplayView(0);
    		pressed = true;
        }
    	else if (req.getParameter("weekView-btn") != null) {
    		display.setDisplayView(1);
    		pressed = true;
        }
    	else if (req.getParameter("dayView-btn") != null) {
    		display.setDisplayView(2);
    		pressed = true;
        }
    	System.out.println(getUserName(display.getUser()) + " is displaying view: " + display.getDisplayView());
    	return pressed;
    }
    
    private boolean checkWeekLRButtons(UserDisplayData display, HttpServletRequest req) {
    	boolean pressed = false;
    	//check the "Today" button
        if (req.getParameter("left") != null) {
        	display.changeWeek(false);
        	pressed = true;
        }
        if (req.getParameter("right") != null) {
        	display.changeWeek(true);
        	pressed = true;
        }
    	return pressed;
    }
    
    /* check the changeMonth form in calendar.jsp
     * updates the user's UserDisplayData in objectify, updates the events to display
     */
    private void updateCalendarDisplayMonth(UserDisplayData display, HttpServletRequest req) {
        //check the changeMonth form - grab month and year from form
    	int displayMonth = UserDisplayData.getMonthInt(req.getParameter("formMonth"));
        int displayYear = UserDisplayData.getYear(req.getParameter("formYear"));
        
        //update UserDisplayData for the User
        display.setDisplayMonth(displayMonth);
        display.setDisplayYear(displayYear);
        display.setDisplayDate(1);
        display.setDisplayWeek(1);
        
       	System.out.println(getUserName(display.getUser()) + " is displaying calendar for " + (display.getDisplayMonth() + 1) + "/" + display.getDisplayYear());
    }
    
    private String getUserName(UserAccount user) {
    	//get the user's nickname if needed;
        String username = "";
        if (user != null) {
        	username = user.getUsername();
        }
        else {
        	username = "[unknown user]";
        }
        return username;
    }
  
}