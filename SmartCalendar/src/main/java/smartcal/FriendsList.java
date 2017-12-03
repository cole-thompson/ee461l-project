package smartcal;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class FriendsList {
	@Id Long id;
	@Index private UserAccount user;
	@Index private List<User> friends;
	
	public FriendsList() {
		setUser(null);
		setFriends(new ArrayList<User>());
	}
	
	public FriendsList(UserAccount user) { 
		setUser(user);
		setFriends(new ArrayList<User>());
	}

	UserAccount getUser() {
		return user;
	}

	void setUser(UserAccount user) {
		this.user = user;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<User> friends) {
		this.friends = friends;
	}
	
	public boolean addFriend(User newFriend) {
		if(newFriend == null){
			throw new IllegalArgumentException("new friend reference was null");
		}else if(!friends.contains(newFriend)) {
			friends.add(newFriend);
			return true;
		}
		return false;	//reaching this if signifies duplicate friend, friendslists should not have duplicates.		
	}
	
	public boolean removeFriend(User oldFriend){
		if(oldFriend == null) {
			throw new IllegalArgumentException("new friend reference was null");
		}else if(!friends.contains(oldFriend)) {
			return false; 	//friend is not present in the list
		}
		friends.remove(oldFriend);
		return true;	//by reaching this boolean statement, the friend is successfully removed
	}
	
	@Override
	public String toString() {
		String allFriends = "";
		for(User u : friends) {
			allFriends += user.getEmail() + ": " + user + "\n"; //sequential list of friends. Maybe not needed due to objectify stuff.
		}
		return allFriends;
	}
	

}
