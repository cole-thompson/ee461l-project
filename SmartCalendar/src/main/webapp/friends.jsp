<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
	<head>
	 	<title>Social</title>
	 	<!-- Required meta tags -->
	 	<meta charset="utf-8">
	 	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
		<!-- Bootstrap CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	 	
	 	<!-- Our custom css -->
	 	<link rel="stylesheet" type="text/css" href="calendar.css">
	 	
	 </head>
	 
	 <body>

	 	<!-- Begin Navbar -->
		<nav class="navbar navbar-expand-lg p-1 navbar-light bg-light sticky-top border border-top-0 border-left-0 border-right-0 border-primary" border-width="thick">
  			<a class="navbar-brand" href="#">Smart Calendar</a>
  			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
		    	<span class="navbar-toggler-icon"></span>
		 	</button>
		 	
		 	<div class="collapse navbar-collapse" id="navbarContent">
			    <ul class="navbar-nav mr-auto">
			    	<li class="nav-item active">
			        	<a class="nav-link" href="/calendar.jsp">Home <span class="sr-only">(current)</span></a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="#">Account</a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="/friends.jsp">Social</a>
			      	</li>
			      	<!-- 
			      	<li class="nav-item dropdown">
			        	<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Dropdown</a>
			        	<div class="dropdown-menu" aria-labelledby="navbarDropdown">
				          	<a class="dropdown-item" href="#">Action</a>
				          	<div class="dropdown-divider"></div>
				          	<a class="dropdown-item" href="#">Something else here</a>
			        	</div>
			      	</li>
			      	 -->
			      	<li class="nav-item">
			        	<a class="nav-link" href="/newevent.jsp">New Event</a>
			      	</li>
			    </ul>	 
	 		</div>
 		</nav>
 		
 		
 		<!-- Initialization stuff i.e. registering userAccounts, pulling the full list of users into an account list, 
 			obtaining a reference to the currently logged in user  -->
 		<%UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    ObjectifyService.register(smartcal.UserAccount.class);
 		ObjectifyService.register(smartcal.FriendsList.class);
 		ObjectifyService.register(smartcal.InvitationsList.class);
 		
	    List<smartcal.UserAccount> accounts = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).list(); // This is all the accounts in Objectify
	    smartcal.UserAccount currentUserAccount = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).filter("user", user).first().now();
	    smartcal.FriendsList mattFriends = new smartcal.FriendsList();
	    smartcal.UserAccount someUser = currentUserAccount;
	    /* if(currentUserAccount == null){
	    	System.out.println("Something is very wrong, you don't seem to have account data, creating it now");
	    	currentUserAccount = new smartcal.UserAccount(user, "goose");
	    	ObjectifyService.ofy().save().entity(currentUserAccount);
	    }else if(currentUserAccount.getUsername().equals("goose")){
	    	//DEBUGGING STUFF, PLEASE TAKE OUT BEFORE DEPLOYMENT --------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    	mattFriends = ObjectifyService.ofy().load().type(smartcal.FriendsList.class).filter("user", user).first().now();
	    	someUser = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).list().get(0);
	    	if(mattFriends.getFriends().size() < 1){
	    		mattFriends.addFriend(someUser.getUser());
	    	}
	    	System.out.println(mattFriends);
	    	ObjectifyService.ofy().save().entity(mattFriends);
	    } */
	    %>
 		<!-- search invitiation in ofy, look for finished (filter for it) -->
 		
 		<!-- Tables -->
 		<div class="container">
 		
 			<div class="row"><div class="col w-100">
	 			<table class="table table-bordered table-light table-hover w-100">
		 			<thead class="thead-dark col-md-6">
							<tr><th class="w-50">Friends</th><th class="w-50">Invitations</th></tr>
					</thead>
	 			</table>
	 		</div></div>
	 		
 			<div class="row">
 			
 			<div class="col w-50"><div style="max-height: 500px;	height:500px;	overflow-y:auto">
	 		<table class="table table-bordered table-light table-hover w-100">	
	           	<tbody style="top:0">
	           	
					<tr><td><p>bloop</p></td></tr>
	          		<%List<User> currentUserFriends = ObjectifyService.ofy().load().type(smartcal.FriendsList.class).filter("user", user).first().now().getFriends();
	          		for(User friend : currentUserFriends){
	          			smartcal.UserAccount friendAccount = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).filter("user", friend).first().now();
	          			if(friendAccount == null){
	          				System.out.println("friendAccount was null (this means that some friend on your friends list doesn't have an account??), breaking from loop");
	          				break;
	          			}
          				String friendName = friendAccount.getUsername();%>
	          		
	          			<tr><td><p><%=(friendName) %>	</p></td></tr>
	          		<%}%>
	          		
	          	</tbody>
	          	
			</table></div>
			
			<form action="/social" name="add-or-remove" method="post"> 	
	 		
				<div class="input-group">	
					<span class="input-group-addon" id="usernamelabel">Username</span>
					<input name="friendname" class="form-control">
				</div>
				<button name="addfriend" class="form-control form-control-lg btn btn-success" type="submit">Add Friend</button>
				<button name="removefriend" class="form-control form-control-lg btn btn-success" type="submit">Remove Friend</button>
			</form>
			
			</div>
		
		
			<!-- Start table2 -->
			<div class="col w-50"><div style="max-height: 500px;	height:500px;	overflow-y:auto">
			<table class="table table-bordered table-light table-responsive-md table-hover w-100">
				<tbody style="top:0">			
	          		<%smartcal.InvitationsList currentUserInvitationsList = ObjectifyService.ofy().load().type(smartcal.InvitationsList.class).filter("user", user).first().now();
	          		
	          		if (currentUserInvitationsList != null) {
		          		List<smartcal.Invitation> currentUserInvitations = currentUserInvitationsList.getInvitations();
		          		for(smartcal.Invitation inv : currentUserInvitations){ 
		          			smartcal.UserAccount friendAccount = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).filter("user", inv.getCreator()).first().now();
		          			if(friendAccount == null){
		          				System.out.println("friendAccount was null (this means that some friend on your friends list doesn't have an account??), breaking from loop");
		          				break;
		          			}
	          				String friendName = friendAccount.getUsername();%>
		          			<tr><td><span class="text-primary"><%=(inv.getName())%></span><span class="text-secondary"><%=("\tfrom: " + friendName)%></span></p></td></tr>
		          		<%}
		          	}%>
		          	
	          	</tbody>
	          	
	 		</table>
	 		</div>
		
			
			</div>
 		</div>
 	</div>
 		
	 </body>