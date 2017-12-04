package smartcal;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

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
        ObjectifyService.register(FriendsList.class);
        ObjectifyService.register(Invitation.class);
        ObjectifyService.register(InvitationsList.class);
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
       	else if (checkNewOptionG(req, user)) {resp.sendRedirect("/newevent.jsp");}
       	else if (checkNewOptionM(req, user)) {resp.sendRedirect("/newevent.jsp");}
       	else if (checkForm2(req, user)) {resp.sendRedirect("/friends.jsp");}
       	
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
					User friend = friends.get(i);
					System.out.println("adding friend to invitation " + friend);
					eventFriends.add(friend);
				}
			}
			eventFriends.add(creator);
			invite.setFriends(eventFriends);
			System.out.println("people in new event: " + eventFriends);
			
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
	private boolean checkNewOptionG(HttpServletRequest req, User creator) {
		boolean pressed = false;
		if (req.getParameter("newoption") != null) {
	       	Invitation invitation = ObjectifyService.ofy().load().type(Invitation.class).filter("creator", creator).filter("finished", false).first().now();
			if (invitation == null) {
				return true;
			}

			InvitationOption option = new InvitationOption();
			
			String eventLoc = req.getParameter("location");
			if (eventLoc != null) {
				option.setLocation(eventLoc);
			}
			
			Date startTimeCal = null;
			Date endTimeCal = null;
			
			if (req.getParameter("allday") == null) {
				option.setAllDay(false);
			}
			else {
				option.setAllDay(true);
			}
			
			String startDay = req.getParameter("startday");
			if (startDay != null) {
				//System.out.println(startDay);
				if (!option.getAllDay()) {
					String startTime = req.getParameter("starttime");
					if (startTime != null) {
						//System.out.println(startTime);
						startTimeCal = dayTimeStringToCal(startDay, startTime);
					}
					else {
						startTimeCal = dayStringToCal(startDay);
					}
				}
				else {
					startTimeCal = dayStringToCal(startDay);
				}
			}
			
			String endDay = req.getParameter("endday");
			if (endDay != null) {
				//System.out.println(endDay);
				if(!option.getAllDay()) {						
					String endTime = req.getParameter("endtime");
					if (endTime != null) {
						//System.out.println(endTime);
						endTimeCal = dayTimeStringToCal(endDay, endTime);
					}
					else {
						endTimeCal = dayStringToCal(endDay);
					}
				}
				else {
					endTimeCal = dayStringToCal(endDay);
				}
			}
			
			option.setStartTime(startTimeCal);
			option.setEndTime(endTimeCal);
			
			System.out.println("NEW GENERIC OPTION location: " + eventLoc + "time: " + option.getTimeString() + " allday: " + option.getAllDay());

			invitation.addOption(option);
			ObjectifyService.ofy().save().entity(invitation); 
			pressed = true;
        }
		return pressed;
	}
	
	private boolean checkNewOptionM(HttpServletRequest req, User creator) {
		boolean pressed = false;
		if (req.getParameter("newoption") != null) {
	       	Invitation invitation = ObjectifyService.ofy().load().type(Invitation.class).filter("creator", creator).filter("finished", false).first().now();
			if (invitation == null) {
				return true;
			}

			//Change to Movie Option class
			InvitationOption option = new InvitationOption();
			
			//get info from form elements, create option, add to invitation
			
			
			
			
			
			
			
			System.out.println("NEW MOVIE OPTION location: " + option.getLocation() + "time: " + option.getTimeString());

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
	       	invitation.finishCreation(); 	
	    	invitation.sendInvitation();
	       	ObjectifyService.ofy().save().entity(invitation);
    		pressed = true;
        }
		return pressed;
	}
	
	private Date dayStringToCal(String day) {
		Date cal = null;
		try {
			if (day.length() == 10) {
				int y = Integer.parseInt(day.substring(0, 4));
				int m = Integer.parseInt(day.substring(5, 7));
				int d = Integer.parseInt(day.substring(8));
				cal = new Date(y, m - 1, d);
			}
		}
		catch (NumberFormatException e) {}
		
		return cal;
	}
	
	private Date dayTimeStringToCal(String day, String time) {
		Date cal = null;
		try {
			if (day.length() == 10 && time.length() == 5) {
				int y = Integer.parseInt(day.substring(0, 4));
				int m = Integer.parseInt(day.substring(5, 7));
				int d = Integer.parseInt(day.substring(8));
				int h = Integer.parseInt(time.substring(0, 2));
				int min = Integer.parseInt(time.substring(3, 5));				
				cal = new Date(y, m - 1, d, h, min);
			}
		}
		catch (NumberFormatException e) {}
		
		return cal;
	}
}
