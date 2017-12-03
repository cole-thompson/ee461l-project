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
	

	public enum Type {G, M};
	
	private List<User> friends;
	private Type type;
	private String name;
	
	private List<InvitationOption> options;

	public Invitation() {
		stage = 1;
		friends = new ArrayList<User>();
		type = Type.G;
		finished = false;
		options = new ArrayList<InvitationOption>();
	}
	
	public Invitation(User creator) {
		this();
		this.creator = creator;
	}
	
	public List<User> getFriends() {
		return friends;
	}
	
	public boolean setFriends(List<User> friends) {
		if (stage == 1) {
			friends.addAll(friends);
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getTypeString() {
		String s = "";
		if (type == Type.G) {
			s = "Generic";
		}
		else if (type == Type.M) {
			s = "Movie";
		}
		return s;
	}
	
	public void nextStage() {
		stage++;
	}
	
	public void finishCreation() {
		finished = true;
	}
	
	
	
	
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getStage() {
		return stage;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public List<InvitationOption> getOptions() {
		return options;
	}

	public void setOptions(List<InvitationOption> options) {
		this.options = options;
	}
	
	public void addOption(InvitationOption option) {
		options.add(option);
	}
}
