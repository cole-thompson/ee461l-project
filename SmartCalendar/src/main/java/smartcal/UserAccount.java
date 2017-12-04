package smartcal;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UserAccount {
	@Id Long id;
	@Index User user;
	@Index private String username;
	private String email;
	
	private UserAccount() {
		this.user =  null;
		this.username = new String();
	}
	
	public UserAccount(User user, String username) {
		this.user = user;
		this.username = username;
		if(this.user == null) {
			this.email = null;
		}else {
			this.email = user.getEmail();
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
