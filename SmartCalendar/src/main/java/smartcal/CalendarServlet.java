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

    }
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get the current user
    	User user = UserServiceFactory.getUserService().getCurrentUser();
    	
    	//check the changeMonth form in calendar.jsp
        updateCalendarDisplayMonth(user, req);
        
        
        
        //redirect back to calendar.jsp
        resp.sendRedirect("/calendar.jsp");
    }
    
    
    /* check the changeMonth form in calendar.jsp
     * updates the user's UserDisplayData in objectify, updates the events to display
     */
    private void updateCalendarDisplayMonth(User user, HttpServletRequest req) {
        //check the changeMonth form - grab month and year from form
    	int displayMonth = UserDisplayData.getMonthInt(req.getParameter("formMonth"));
        int displayYear = UserDisplayData.getYear(req.getParameter("formYear"));
        
        //check the "Today" button
        if (req.getParameter("today") != null) {
        	displayMonth = -1;
        	displayYear = -1;
        }
        
    	//get UserDisplayData for the User, create new one if needed
       	UserDisplayData display = ObjectifyService.ofy().load().type(UserDisplayData.class).filter("user", user).first().now();
       	if (display == null) {
       		display = new UserDisplayData(user);
       	}
       	
        //update UserDisplayData for the User
        display.setDisplayMonth(displayMonth);
        display.setDisplayYear(displayYear);
        
        //print user display info to console
       	System.out.println(getUserName(user) + " is displaying calendar for " + (display.getDisplayMonth() + 1) + "/" + display.getDisplayYear());
        
       	//populate the list of all the events in the display month
       	display.loadDisplayEvents();
       	
        //save in objectify
        ObjectifyService.ofy().save().entity(display); 
    }
    
    private String getUserName(User user) {
    	//get the user's nickname if needed;
        String username = "";
        if (user != null) {
        	username = user.getNickname();
        }
        else {
        	username = "[unknown user]";
        }
        return username;
    }
  
}