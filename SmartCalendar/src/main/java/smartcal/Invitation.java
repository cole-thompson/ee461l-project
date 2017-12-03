package smartcal;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Invitation {
	@Id Long id;
	@Index User creator;
	
	//true when it has been sent out
	@Index boolean finished;
	
	/* 1 = name type friends
	 * 2 = time location etc
	 */
	@Index int stage;
	
	List<User> friends;

	
	public Invitation(User creator) {
		this.creator = creator;

		stage = 1;
		friends = new ArrayList();
	}
	
	public boolean addFriend(User user) {
		if (stage == 1) {
			friends.add(user);
			return true;
		}
		else {
			return false;
		}
	}
	
	
}
