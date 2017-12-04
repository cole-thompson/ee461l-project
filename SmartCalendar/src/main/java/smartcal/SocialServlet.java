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
    	boolean clickedInvitation = false;
    	
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
    	}else if (req.getParameter("sendoptions") != null) {
    		clickedInvitation = true;
    		sendOptionVote(req, user);
    	}else if (req.getParameter("finalizeinvitation") != null) {
    		finalizeInvitation(req, user);
    	}else {
    		InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", user).first().now();
    		for(int i = 0; i < currentUserInvitations.getInvitations().size(); i++) {
    			if(req.getParameter("invitation" + i) != null){
    				Invitation current = currentUserInvitations.getInvitations().get(i);
    				currentUserInvitations.setDisplayedInvitation(current);
    				clickedInvitation = true;
    				ObjectifyService.ofy().save().entity(currentUserInvitations);
    			}
    		}
    		
    	}
    	
    	System.out.println("clickedInvitation: " + clickedInvitation);
    	
    	if(clickedInvitation) {
    		System.out.println("redirection to invitation");
    		resp.sendRedirect("/invitation.jsp");
    	}else {
    		resp.sendRedirect("/friends.jsp");
    	}

    }
    
    public void sendOptionVote(HttpServletRequest req, User user) {
    	InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", user).first().now();
    	Invitation displayInvitation = currentUserInvitations.getDisplayedInvitation();
    	if (!displayInvitation.hasPersonVoted(user)) {
	    	List<InvitationOption> options = displayInvitation.getOptions();
	    	for (int i = 0; i < options.size(); i++) {
	    		if (req.getParameter("option" + i) != null) {
	    			options.get(i).addAvailablePerson(user);
	    		}
	    	}
	    	displayInvitation.personVoted(user);
	    	ObjectifyService.ofy().save().entity(currentUserInvitations).now();
    	}
    }
    
    public void finalizeInvitation(HttpServletRequest req, User user) {
    	InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", user).first().now();
    	Invitation displayInvitation = currentUserInvitations.getDisplayedInvitation();
    	List<InvitationOption> options = displayInvitation.getOptions();
    	for (int i = 0; i < options.size(); i++) {
    		if (req.getParameter("option" + i) != null) {
    			InvitationOption option = options.get(i);
    			CalEvent event = CalEvent.invitationOptionToEvent(displayInvitation, option);
    			for (User u : displayInvitation.getFriends()) {
    				CalEventList friendEventList = ObjectifyService.ofy().load().type(CalEventList.class).filter("user", u).first().now();
    				friendEventList.addEvent(event);
    				ObjectifyService.ofy().save().entity(friendEventList).now();
    				InvitationsList friendInviteList = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", u).first().now();
    				friendInviteList.getInvitations().remove(displayInvitation);
    				ObjectifyService.ofy().save().entity(friendInviteList).now();
    			}
    			currentUserInvitations.removeInvitation(displayInvitation);
    			ObjectifyService.ofy().save().entity(currentUserInvitations).now();
    		}	
    	}
    }
    

}
