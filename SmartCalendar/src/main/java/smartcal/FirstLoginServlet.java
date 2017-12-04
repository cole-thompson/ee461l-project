package smartcal;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class FirstLoginServlet extends HttpServlet {
	
	static {
        ObjectifyService.register(FriendsList.class);
        ObjectifyService.register(InvitationsList.class);
        ObjectifyService.register(UserAccount.class);
        ObjectifyService.register(CalEventList.class);
    }
	
	 public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 if (req.getParameter("setname") != null) {
			 User user = UserServiceFactory.getUserService().getCurrentUser();
			 String name = req.getParameter("username");
			 
			 System.out.println("first login for " + name);
			 
			 UserAccount account = new UserAccount(user, name);
			 InvitationsList invites = new InvitationsList(user);
			 FriendsList friends = new FriendsList(user);
			 CalEventList events = new CalEventList(user);
			 //ObjectifyService.ofy().save().entities(account, invites, friends);
			 ObjectifyService.ofy().save().entity(account);
			 ObjectifyService.ofy().save().entity(invites);
			 ObjectifyService.ofy().save().entity(friends);
			 ObjectifyService.ofy().save().entity(events);
			 System.out.println("Successfully created all lists!");
		 }
		 
		 resp.sendRedirect("/calendar.jsp");
	 }
}