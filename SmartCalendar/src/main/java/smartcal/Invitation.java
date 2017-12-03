package smartcal;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Invitation {
	@Id Long id;
	@Index User user;
	
	public Invitation() {
		
	}
}
