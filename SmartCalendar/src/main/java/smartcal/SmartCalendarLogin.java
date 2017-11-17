package smartcal;


import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

 

public class SmartCalendarLogin extends HttpServlet {

	static {
        //ObjectifyService.register();
    }
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3275371858742144640L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        //String content = req.getParameter("content");
        //ObjectifyService.ofy().save().entity().now(); 
        
        resp.sendRedirect("/calendar.jsp");

    }

}

/*
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user != null) {
            resp.setContentType("text/plain");
            resp.getWriter().println("Hello, " + user.getNickname());
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
*/