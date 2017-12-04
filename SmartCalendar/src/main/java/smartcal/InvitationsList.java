package smartcal;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class InvitationsList {
	@Id Long id;
	@Index private User user;
	private List<Invitation> invitations;
	private Invitation displayedInvitation;
	
	public InvitationsList() {
		setUser(null);
		setInvitations(new ArrayList<Invitation>());
	}
	
	public InvitationsList(User user) { 
		setUser(user);
		setInvitations(new ArrayList<Invitation>());
	}

	public User getUser() {
		return user;
	}

	void setUser(User user) {
		this.user = user;
	}

	public List<Invitation> getInvitations() {
		return invitations;
	}

	public void setInvitations(ArrayList<Invitation> arrayList) {
		this.invitations = arrayList;
	}

	public Invitation getDisplayedInvitation() {
		return displayedInvitation;
	}

	public void setDisplayedInvitation(Invitation displayedInvitation) {
		this.displayedInvitation = displayedInvitation;
	}
	
	public boolean addInvitation(Invitation newInvite) {
		if(newInvite == null){
			throw new IllegalArgumentException("new friend reference was null");
		}else if(!invitations.contains(newInvite)) {
			invitations.add(newInvite);
			return true;
		}
		return false;	//reaching this if signifies duplicate friend, invitationslists should not have duplicates.		
	}
	
	public boolean removeInvitation(Invitation oldInvite){
		if(oldInvite == null) {
			throw new IllegalArgumentException("new friend reference was null");
		}else if(!invitations.contains(oldInvite)) {
			return false; 	//friend is not present in the list
		}
		invitations.remove(oldInvite);
		return true;	//by reaching this boolean statement, the friend is successfully removed
	}
		

	@Override
	public String toString() {
		String allInvites = "";
		for(Invitation i : invitations) {
			//creator, name, type, up to three people, then ellipses
			smartcal.Invitation.Type t = i.getType();
			String typeString = "";
			if(t == null) {
				typeString = "None";
			}
			else if (t.equals(smartcal.Invitation.Type.G)) {
				typeString = "Generic";
			}
			else if (t.equals(smartcal.Invitation.Type.M)) {
				typeString = "Movie";
			}
			
			String invitedString = "";
			List<User> invitedFriends = i.getFriends();
			if(invitedFriends.size() > 3) {
				for(int j = 0; j < 3; j++) {
					if(j < 2) {
						invitedString += invitedFriends.get(j).getNickname() + ", ";
					}
					else {
						invitedString += invitedFriends.get(j).getNickname() + "...";
					}
				}
			}
			else if(invitedFriends.size() > 0){
				for(int k = 0 ; k < invitedFriends.size(); k++) {
					if(k < invitedFriends.size() - 1) {
						invitedString += invitedFriends.get(k).getNickname() + ", ";
					}
					else {
						invitedString += invitedFriends.get(k).getNickname();
					}
				}
			}
			else {
				invitedString = "None";
			}
			
			allInvites += "Event: " + i.getName() + " Event Type: " + typeString + " Created by: "+ i.getCreator().getNickname() + " Others Invited: " + invitedString + "\n"; //sequential list of Invitations. Maybe not needed due to objectify stuff.
		}
		return allInvites;
	}


	

}
