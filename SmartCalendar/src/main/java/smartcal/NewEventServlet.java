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
 * Servlet to handle New Event creation
 */


public class NewEventServlet extends HttpServlet{
	static {
        ObjectifyService.register(CalEvent.class);
        ObjectifyService.register(UserDisplayData.class);
    }
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//get the current user
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
       	//check all the different forms in newevent.jsp
       	if (checkForm1(req)) {}
       	else if (checkForm2(req)) {}
		
		//System.out.println("worked");
		
		resp.sendRedirect("/newevent.jsp");
	}
	 
	//Stage1: Invitation creation
	
	private boolean checkForm1(HttpServletRequest req) {
		boolean pressed = false;
		
		if (req.getParameter("part1submit") != null) {
			//Stuff that creates Invitation
			String eventName = req.getParameter("eventname");
			
			String eventType = req.getParameter("eventtype");
			if(eventType.equals("Generic")) {
				System.out.println("Event Type: Generic");
			}
			else if(eventType.equals("Movie")) {
				System.out.println("Event Type: Movie");
			}
			else {
				System.out.println("Event Type: Invalid");
			}
    		pressed = true;
        }	
		
		return pressed;
	}
	
	//Stage1: Confirm invitation complete
	
	private boolean checkForm2(HttpServletRequest req) {
		boolean pressed = false;
		//hypothetical middle buttons for 'completing' an Option
		
		
		if (req.getParameter("part2submit") != null) {
			//Stuff that sends out the invitation
    		pressed = true;
        }
		
		return pressed;
	}
}
