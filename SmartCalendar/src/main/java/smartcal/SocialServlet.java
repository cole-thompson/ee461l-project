package smartcal;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class SocialServlet {
	
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get the current user
    	User user = UserServiceFactory.getUserService().getCurrentUser();
    	
    	if(req.getParameter("addfriend") != null){
    		FriendsList currentUserFriends = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
    		String toAdd = req.getParameter("friendname");
    		UserAccount friendAccount = ObjectifyService.ofy().load().type(UserAccount.class).filter("username", toAdd).first().now();
    		currentUserFriends.addFriend(friendAccount.getUser());
    	}else if(req.getParameter("removefriend") != null){
    		FriendsList currentUserFriends = ObjectifyService.ofy().load().type(FriendsList.class).filter("user", user).first().now();
    		String toAdd = req.getParameter("friendname");
    		UserAccount friendAccount = ObjectifyService.ofy().load().type(UserAccount.class).filter("username", toAdd).first().now();
    		currentUserFriends.removeFriend(friendAccount.getUser());
    	}
    	
        resp.sendRedirect("/friends.jsp");
    }

}
