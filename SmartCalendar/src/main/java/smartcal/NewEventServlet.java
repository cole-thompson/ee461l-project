package smartcal;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
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
		
		//get FriendsList
       	FriendsList flist = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
       	if(flist == null){
       		System.out.println(user + " didnt have a friendslist for some reason, creating it now");
       		flist = new FriendsList(user);
       		ObjectifyService.ofy().save().entity(flist);
       	}else{
       		System.out.println("friendslist found: \n" + flist);		// THIS STATEMENT IS TO DEBUG, PRINTS THE WHOLE FRIENDSLIST. CAN BE REMOVED
       	}
       	
       	//check all the different forms in newevent.jsp
		
       	if (checkForm1(req, user, flist)) {resp.sendRedirect("/newevent.jsp");}
       	else if (checkNewOption(req, user)) {resp.sendRedirect("/newevent.jsp");}
       	else if (checkForm2(req, user)) {resp.sendRedirect("/calendar.jsp");}
       	
		//System.out.println("worked");
		
		
	}
	 
	//Stage1: Invitation creation
	
	private boolean checkForm1(HttpServletRequest req, User creator, FriendsList f) {
		boolean pressed = false;
		
		if (req.getParameter("part1submit") != null) {
			//Stuff that creates Invitation
			String eventName = req.getParameter("eventname");
			String eventType = req.getParameter("eventtype");
			
			List<User> eventFriends = new ArrayList<User>();
			List<User> friends = f.getFriends();
			
			if(eventName == null || eventType == null) {
				//indicate that you need to fill in the fields
				System.out.println("One or more fields have not been filed in");
				return true;
			}
			
			Invitation invite = new Invitation(creator);
			invite.setName(eventName);
			
			for(int i = 0;  i < friends.size(); i++) {
				String name = "friend" + i;
				if(req.getParameter(name) != null) {
					eventFriends.add(f.getFriends().get(i));
				}
			}
			
			invite.setFriends(eventFriends);
			
			if(eventType.equals("Generic")) {
				System.out.println("Event Type: Generic");
				invite.setType(Invitation.Type.G);
				
			}
			else if(eventType.equals("Movie")) {
				System.out.println("Event Type: Movie");
				invite.setType(Invitation.Type.M);
			}
			else {
				System.out.println("Event Type: Invalid");
			}
			
			invite.nextStage();
			ObjectifyService.ofy().save().entity(invite); 
    		pressed = true;
        }	
		
		return pressed;
	}
	
	//Stage2: Confirm invitation complete
		private boolean checkNewOption(HttpServletRequest req, User creator) {
			boolean pressed = false;
			if (req.getParameter("newoption") != null) {
		       	Invitation invitation = ObjectifyService.ofy().load().type(Invitation.class).filter("creator", creator).filter("finished", false).first().now();
				if (invitation == null) {
					return true;
				}
				String eventLoc = req.getParameter("location");
				
				InvitationOption option = new InvitationOption();
				
				if (eventLoc != null) {
					option.setLocation(eventLoc);
				}
				//TODO handle times, all day
				
				System.out.println("o:" + (option==null) + " i:" + (invitation==null));
				invitation.addOption(option);
				ObjectifyService.ofy().save().entity(invitation); 
				pressed = true;
	        }
			return pressed;
		}
	
	//Stage2: Confirm invitation complete
	private boolean checkForm2(HttpServletRequest req, User creator) {
		boolean pressed = false;
		if (req.getParameter("part2submit") != null) {
			System.out.println("Finishing invitation");
	       	Invitation invitation = ObjectifyService.ofy().load().type(Invitation.class).filter("creator", creator).filter("finished", false).first().now();
	       	//TODO send invitation to people
	       	
	       	
	       	invitation.finishCreation(); 	
	       	ObjectifyService.ofy().save().entity(invitation); 
    		pressed = true;
        }
		return pressed;
	}
}
