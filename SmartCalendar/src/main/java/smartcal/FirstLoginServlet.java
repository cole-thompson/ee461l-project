package smartcal;

import java.io.IOException;
import java.util.List;

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
        ObjectifyService.register(UserDisplayData.class);
        ObjectifyService.register(Invitation.class);
    }
	
	 public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 User user = UserServiceFactory.getUserService().getCurrentUser();
		 if (req.getParameter("setname") != null) {
			 createAccount(req, user);
		 }
		 else if (req.getParameter("deleteuserinfo") != null) {
			 deleteAccount(user);
		 }
		 resp.sendRedirect("/calendar.jsp");
	 }
	 
	 public void createAccount(HttpServletRequest req, User user) {
		 
		 String name = req.getParameter("username");
		 
		 System.out.println("first login for " + name);
		 
		 UserAccount account = new UserAccount(user, name);
		 InvitationsList invites = new InvitationsList(user);
		 FriendsList friends = new FriendsList(user);
		 CalEventList events = new CalEventList(user);
		 //ObjectifyService.ofy().save().entities(account, invites, friends);
		 ObjectifyService.ofy().save().entity(account).now();
		 ObjectifyService.ofy().save().entity(invites).now();
		 ObjectifyService.ofy().save().entity(friends).now();
		 ObjectifyService.ofy().save().entity(events).now();
		 System.out.println("Successfully created all lists!");
	 }
	 
	 public void deleteAccount(User user) {
		 FriendsList f = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
		 if (f != null) {
			 for (User friend : f.getFriends()) {
				 FriendsList friendList = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", friend).first().now();
				 if (friendList != null) {
					 friendList.removeFriend(friend);
					 ObjectifyService.ofy().save().entity(friendList).now();
				 }
			 }
			 ObjectifyService.ofy().delete().entity(f).now();
			}
		 CalEventList e = ObjectifyService.ofy().load().type(CalEventList.class).filter("user", user).first().now();
		 if (e != null) {
			 for (CalEvent event : e.getEvents()) {
				 List<User> people = event.getPeople();
				 people.remove(user);
				 for (User friend : people) {
					 CalEventList fEventList = ObjectifyService.ofy().load().type(CalEventList.class).filter("user", friend).first().now();
					 if (fEventList != null) { 
						 for (CalEvent friendEvent: fEventList.getEvents()) {
							 friendEvent.getPeople().remove(user);
						 }
						 ObjectifyService.ofy().save().entity(fEventList).now();
					 
					 }
				 }
			 }
			 ObjectifyService.ofy().delete().entity(e).now();
		 }
		 InvitationsList i = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", user).first().now();
		 if (i != null) {
			 for (Invitation invite : i.getInvitations()) {
				 invite.removePerson(user);
				 ObjectifyService.ofy().save().entity(invite).now();
			 }
			 ObjectifyService.ofy().delete().entity(i).now();
		 }
		 
		 UserAccount a = ObjectifyService.ofy().load().type(UserAccount.class).filter("user", user).first().now();
		 if (a != null) {ObjectifyService.ofy().delete().entity(a).now();}
		 UserDisplayData d = ObjectifyService.ofy().load().type(UserDisplayData.class).filter("user", user).first().now();
		 if (d != null) {ObjectifyService.ofy().delete().entity(d).now();}
		 
		 List<Invitation> invitations = ObjectifyService.ofy().load().type(Invitation.class).filter("creator", user).list();
		 if (invitations != null) {
			 for (Invitation invite : invitations) {
				 ObjectifyService.ofy().delete().entity(invite).now();
			 }
		 }
	 }
}