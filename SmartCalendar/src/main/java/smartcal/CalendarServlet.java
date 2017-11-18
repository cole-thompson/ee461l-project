package smartcal;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.googlecode.objectify.*;
 
/*
 * Servlet to handle stuff from the Calendar module
 */

public class CalendarServlet extends HttpServlet {
	static {
        //ObjectifyService.register(smartcal.CalEvent.class);
    }
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        
        //String content = req.getParameter("content");
        //ObjectifyService.ofy().save().entity().now(); 
        
        
        resp.sendRedirect("/calendar.jsp");
    }
}