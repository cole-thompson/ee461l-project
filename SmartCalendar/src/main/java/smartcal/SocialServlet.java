package smartcal;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class SocialServlet extends HttpServlet {
	
	static {
        ObjectifyService.register(CalEventList.class);
        ObjectifyService.register(FriendsList.class);
        ObjectifyService.register(Invitation.class);
        ObjectifyService.register(InvitationsList.class);
    }
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get the current user
    	User user = UserServiceFactory.getUserService().getCurrentUser();
    	FriendsList currentUserFriends;
    	
    	if(req.getParameter("addfriend") != null){
    		currentUserFriends = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
    		if(currentUserFriends == null) {
    			System.out.println("no friends list detected, making one");
    			currentUserFriends = new FriendsList(user);
    			ObjectifyService.ofy().save().entity(currentUserFriends);
    		}
    		String toAdd = req.getParameter("friendname");
    		UserAccount friendAccount = ObjectifyService.ofy().load().type(UserAccount.class).filter("username", toAdd).first().now();
    		if(friendAccount == null) {
    			System.out.println("Friend doesn't have an account??????");
    		}else {
	    		boolean worked = currentUserFriends.addFriend(friendAccount.getUser());
	    		ObjectifyService.ofy().save().entity(currentUserFriends);
	    		System.out.println(worked);
    		}
    		resp.sendRedirect("/friends.jsp");
    	}else if(req.getParameter("removefriend") != null){
    		currentUserFriends = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
    		String toRemove = req.getParameter("friendname");
    		if(currentUserFriends == null) {
    			System.out.println("no friends list detected, making one");
    			currentUserFriends = new FriendsList(user);
    			ObjectifyService.ofy().save().entity(currentUserFriends);
    		}
    		UserAccount friendAccount = ObjectifyService.ofy().load().type(UserAccount.class).filter("username", toRemove).first().now();
    		if(friendAccount == null) {
    			System.out.println("Friend shouldn't have ever made it on your list since they have no list");
    		}
    		boolean worked = currentUserFriends.removeFriend(friendAccount.getUser());
    		ObjectifyService.ofy().save().entity(currentUserFriends);
    		System.out.println(worked);
    		resp.sendRedirect("/friends.jsp");
    	}else if (req.getParameter("sendoptions") != null) {
    		if (sendOptionVote(req, user)) {
    			resp.sendRedirect("/invitation.jsp");
    		}
    		else {
    			resp.sendRedirect("/friends.jsp");
    		}
    	}else if (req.getParameter("finalizeinvitation") != null) {
    		finalizeInvitation(req, user);
    		resp.sendRedirect("/calendar.jsp");
    	}else {
    		InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", user).first().now();
    		for(int i = 0; i < currentUserInvitations.getInvitations().size(); i++) {
    			if(req.getParameter("invitation" + i) != null){
    				Invitation current = currentUserInvitations.getInvitations().get(i);
    				currentUserInvitations.setDisplayedInvitation(current);
    				resp.sendRedirect("/invitation.jsp");
    				System.out.println("clickedInvitation: " + i);
    				ObjectifyService.ofy().save().entity(currentUserInvitations);
    			}
    		}
    		
    	}

    }
    
    public boolean sendOptionVote(HttpServletRequest req, User user) {
    	InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", user).first().now();
    	int index = currentUserInvitations.getInvitations().indexOf(currentUserInvitations.getDisplayedInvitation());
    	System.out.println("logging a vote from " + user.getNickname() + " invitation found index=" + index);
    	if (index == -1) {
    		return false;
    	}
    	Invitation displayInvitation = currentUserInvitations.getInvitations().get(index);
    	
    	if (!displayInvitation.hasPersonVoted(user)) {
    		if (displayInvitation.getType() == Invitation.Type.G) {
		    	List<InvitationOption> options = displayInvitation.getOptions();
    			System.out.println("found options " + options);
		    	for (int i = 0; i < options.size(); i++) {
		    		if (req.getParameter("option" + i) != null) {
		    			options.get(i).addAvailablePerson(user);
		    			for (User u : displayInvitation.getFriends()) {
		    				if (!user.equals(u)) {
			    		    	InvitationsList invs = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", u).first().now();
			    		    	int invIndex = invs.getInvitations().indexOf(displayInvitation);
			    		    	invs.getInvitations().get(invIndex).personVoted(user);
			    		    	int optIndex = invs.getInvitations().get(invIndex).getOptions().indexOf(options.get(i));
			    		    	invs.getInvitations().get(invIndex).getOptions().get(optIndex).addAvailablePerson(user);
			    		    	ObjectifyService.ofy().save().entity(invs).now();
		    				}
		    			}
		    		}
		    	}
    		}
    		else if (displayInvitation.getType() == Invitation.Type.M) {
    			List<MovieOption> options = displayInvitation.getMovieOptions();
		    	for (int i = 0; i < options.size(); i++) {
		    		if (req.getParameter("option" + i) != null) {
		    			System.out.println("voting for option " + options);
		    			options.get(i).addAvailablePerson(user);
		    			for (User u : displayInvitation.getFriends()) {
		    				if (!user.equals(u)) {
			    		    	InvitationsList invs = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", u).first().now();
			    		    	int invIndex = invs.getInvitations().indexOf(displayInvitation);
			    		    	invs.getInvitations().get(invIndex).personVoted(user);
			    		    	int optIndex = invs.getInvitations().get(invIndex).getMovieOptions().indexOf(options.get(i));
			    		    	invs.getInvitations().get(invIndex).getMovieOptions().get(optIndex).addAvailablePerson(user);
			    		    	ObjectifyService.ofy().save().entity(invs).now();
		    				}
		    			}
		    		}
		    	}
    		}
    		currentUserInvitations.setDisplayedInvitation(displayInvitation);
    		displayInvitation.personVoted(user);
    		ObjectifyService.ofy().save().entity(currentUserInvitations).now();
    	}
    	return true;
    }
    
    public void finalizeInvitation(HttpServletRequest req, User user) {
    	InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", user).first().now();
    	int index = currentUserInvitations.getInvitations().indexOf(currentUserInvitations.getDisplayedInvitation());
    	Invitation displayInvitation = currentUserInvitations.getInvitations().get(index);
    	
    	if (displayInvitation.getType() == Invitation.Type.G) {
    		List<InvitationOption> options = displayInvitation.getOptions();
        	for (int i = 0; i < options.size(); i++) {
        		if (req.getParameter("option" + i) != null) {
        			InvitationOption option = options.get(i);
        			CalEvent event = CalEvent.invitationOptionToEvent(displayInvitation, option);
        			for (User u : displayInvitation.getFriends()) {
        				sendInvitation(displayInvitation, event, u);
        			}
        			currentUserInvitations.removeInvitation(displayInvitation);
        			ObjectifyService.ofy().save().entity(currentUserInvitations).now();
        		}	
        	}
    	}
    	else if (displayInvitation.getType() == Invitation.Type.M) {
    		List<MovieOption> options = displayInvitation.getMovieOptions();
        	for (int i = 0; i < options.size(); i++) {
        		if (req.getParameter("option" + i) != null) {
        			MovieOption option = options.get(i);
        			CalEvent event = CalEvent.invitationMovieOptionToEvent(displayInvitation, option);
        			for (User u : displayInvitation.getFriends()) {
        				sendInvitation(displayInvitation, event, u);
        			}
        			currentUserInvitations.removeInvitation(displayInvitation);
        			ObjectifyService.ofy().save().entity(currentUserInvitations).now();
        		}	
        	}
    	}
    }
    
    private void sendInvitation(Invitation i, CalEvent e, User u) {
    	CalEventList friendEventList = ObjectifyService.ofy().load().type(CalEventList.class).filter("user", u).first().now();
		friendEventList.addEvent(e);
		ObjectifyService.ofy().save().entity(friendEventList).now();
		InvitationsList friendInviteList = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", u).first().now();
		friendInviteList.getInvitations().remove(i);
		ObjectifyService.ofy().save().entity(friendInviteList).now();
    }
    

}
