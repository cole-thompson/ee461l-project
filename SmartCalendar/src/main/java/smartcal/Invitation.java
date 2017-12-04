package smartcal;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Invitation {
	@Id Long id;
	@Index User creator;

	//true when it has been sent out
	@Index boolean finished;
	
	//true when "start invitation" pressed
	@Index boolean started;
	

	public enum Type {G, M};
	
	private List<User> friends;
	private Type type;
	private String name;
	
	private List<InvitationOption> options;
	private List<User> peopleVotedForOptions;

	public Invitation() {
		started = false;
		finished = false;
		options = new ArrayList<InvitationOption>();
		friends = new ArrayList<User>();
		peopleVotedForOptions = new ArrayList<User>();
		type = Type.G;
		
		
	}
	
	public Invitation(User creator) {
		this();
		this.creator = creator;
	}
	
	public List<User> getFriends() {
		return friends;
	}
	
	public boolean setFriends(List<User> friends) {
		if (!started) {
			this.friends = friends;
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
		started = true;
	}
	
	public void finishCreation() {
		finished = true;
	}
	
	public void sendInvitation() {
		for (User u : friends) {
			InvitationsList invitations = ObjectifyService.ofy().load().type(InvitationsList.class).filter("user", u).first().now();
	       	if(invitations == null){
	       		System.out.println(u + " didnt have an invitations list, creating it now");
	       		invitations = new InvitationsList(u);
	       	}else{
	       		System.out.println(u.getNickname() + " invitations list found: \n" + invitations);		// THIS STATEMENT IS TO DEBUG, PRINTS THE WHOLE FRIENDSLIST. CAN BE REMOVED
	       	}
	       	invitations.addInvitation(this);
	       	ObjectifyService.ofy().save().entity(invitations);
		}
	}
	
	public void personVoted(User u) {
		if (friends.contains(u)) {
			peopleVotedForOptions.add(u);
		}
	}
	
	public boolean hasPersonVoted(User u) {
		return peopleVotedForOptions.contains(u);
	}
	
	public int numPeopleVoted() {
		return peopleVotedForOptions.size();
	}
	
	public int numPeople() {
		return friends.size();
	}
	
	//GETTERS AND SETTERS
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean getStarted() {
		return this.started;
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
