package smartcal;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import java.io.IOException;
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
        //get current User
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        //get the user's nickname if needed;
        String username = "";
        if (user != null) {
        	username = user.getNickname();
        }
                
        //grab month and year from form
        int displayYear = UserDisplayData.getYear(req.getParameter("formYear"));
        int displayMonth = UserDisplayData.getMonthInt(req.getParameter("formMonth"));
        
        //update UserDisplayData for the User
       	UserDisplayData display = ObjectifyService.ofy().load().type(UserDisplayData.class).filter("user", user).first().now();
       	if (display == null) {
       		display = new UserDisplayData(user);
       	}
        display.setDisplayMonth(displayMonth);
        display.setDisplayYear(displayYear);
        
        //System.out.println(display.getDisplayYear() + " " + display.getDisplayMonth());
        ObjectifyService.ofy().save().entity(display); 
        
        //redirect back to jsp
        resp.sendRedirect("/calendar.jsp");
    }
    
    
}