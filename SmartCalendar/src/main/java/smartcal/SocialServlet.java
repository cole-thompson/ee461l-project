package smartcal;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class SocialServlet extends HttpServlet {
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get the current user
    	User user = UserServiceFactory.getUserService().getCurrentUser();
    	FriendsList currentUserFriends;
    	
    	if(req.getParameter("addfriend") != null){
    		currentUserFriends = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
    		String toAdd = req.getParameter("friendname");
    		UserAccount friendAccount = ObjectifyService.ofy().load().type(UserAccount.class).filter("username", toAdd).first().now();
    		if(friendAccount == null) {
    			System.out.println("Friend doesn't have an account??????");
    		}
    		boolean worked = currentUserFriends.addFriend(friendAccount.getUser());
    		ObjectifyService.ofy().save().entity(currentUserFriends);
    		System.out.println(worked);
    	}else if(req.getParameter("removefriend") != null){
    		currentUserFriends = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
    		String toAdd = req.getParameter("friendname");
    		UserAccount friendAccount = ObjectifyService.ofy().load().type(UserAccount.class).filter("username", toAdd).first().now();
    		if(friendAccount == null) {
    			System.out.println("Friend shouldn't have ever made it on your list since they have no list");
    		}
    		boolean worked = currentUserFriends.removeFriend(friendAccount.getUser());
    		ObjectifyService.ofy().save().entity(currentUserFriends);
    		System.out.println(worked);
    	}
    	
        resp.sendRedirect("/friends.jsp");
    }

}
