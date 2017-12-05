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
        ObjectifyService.register(UserDisplayData.class);
        ObjectifyService.register(FriendsList.class);
        ObjectifyService.register(Invitation.class);
        ObjectifyService.register(InvitationsList.class);
    }
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//get the current user
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if(user == null) {
			System.out.println("No user detected for some reason, failed to execute NewEventServlet");
			return;
		}
		
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
			else if (invitation.getType() != Invitation.Type.G) {
				return false;
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
						startTimeCal = InvitationOption.dayTimeStringToDate(startDay, startTime);
					}
					else {
						startTimeCal = InvitationOption.dayStringToDate(startDay);
					}
				}
				else {
					startTimeCal = InvitationOption.dayStringToDate(startDay);
				}
			}
			
			String endDay = req.getParameter("endday");
			if (endDay != null) {
				//System.out.println(endDay);
				if(!option.getAllDay()) {						
					String endTime = req.getParameter("endtime");
					if (endTime != null) {
						//System.out.println(endTime);
						endTimeCal = InvitationOption.dayTimeStringToDate(endDay, endTime);
					}
					else {
						endTimeCal = InvitationOption.dayStringToDate(endDay);
					}
				}
				else {
					endTimeCal =InvitationOption.dayStringToDate(endDay);
				}
			}
			
			option.setStartTime(startTimeCal);
			option.setEndTime(endTimeCal);
			
			System.out.println("NEW GENERIC OPTION location: " + eventLoc + "time: " + option.getStartTime() + " allday: " + option.getAllDay());

			invitation.addOption(option);
			ObjectifyService.ofy().save().entity(invitation); 
			pressed = true;
        }
		return pressed;
	}
	
	private boolean checkNewOptionM(HttpServletRequest req, User creator) {
		boolean pressed = false;
		if (req.getParameter("newoption") != null) {
			System.out.println("Adding a new movie option");
	       	Invitation invitation = ObjectifyService.ofy().load().type(Invitation.class).filter("creator", creator).filter("finished", false).first().now();
			if (invitation == null) {
				return true;
			}
			else if (invitation.getType() != Invitation.Type.M) {
				return false;
			}
			
			String showtimeString = "";
			if ((showtimeString = req.getParameter("showtimes")) != null) {
				System.out.println("A showtime was selected: " + showtimeString);
				MovieOption option = invitation.findMovieOptionNotFinished();
				List<Movie> movies = option.getSearchResults();
				int movieNum = 0;
				int stNum = 0;
				try {
					int mid = showtimeString.indexOf("-");
					movieNum = Integer.parseInt(showtimeString.substring(1, mid));
					stNum = Integer.parseInt(showtimeString.substring(mid + 2));
					System.out.println("new option: found showtime");
				} catch (NumberFormatException | IndexOutOfBoundsException e) {
					movieNum = 0;
					stNum = 0;
				}
				Showtime st = movies.get(movieNum).getShowtimes().get(stNum);
				
				option.setOption(st);
				option.setFinished(true);
				ObjectifyService.ofy().save().entity(invitation); 
				System.out.println("NEW MOVIE OPTION location: " + option.getLocation() + "time: " + option.getStartTime());
			}

			ObjectifyService.ofy().save().entity(invitation); 
			pressed = true;
        }
		else if (req.getParameter("searchmovies") != null) {
			System.out.println("Searching for movies");
			Invitation invitation = ObjectifyService.ofy().load().type(Invitation.class).filter("creator", creator).filter("finished", false).first().now();
			if (invitation == null) {
				return true;
			}
			
			MovieOption option = new MovieOption();
			System.out.println("option in search" + option);
			
			
				int zip = 0;
				int radius = 0;
				try {
					zip = Integer.parseInt(req.getParameter("zipcode"));
					radius = Integer.parseInt(req.getParameter("zipradius"));
				}
				catch (NumberFormatException e) {
					
				}
				String startDay = req.getParameter("startday");
				String endDay = req.getParameter("endday");
				option.searchMovies(zip, radius, startDay, endDay);
				option.setSearched(true);
				invitation.addOption(option);
				System.out.println("Movies Found: " + option.getSearchResults());
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
	
}
