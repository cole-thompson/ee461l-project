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
	 	<title>Smart Calendar</title>
	 	<!-- Required meta tags -->
	 	<meta charset="utf-8">
	 	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
		<!-- Bootstrap CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	 	
	 	<!-- Our custom css -->
	 	<link rel="stylesheet" type="text/css" href="calendar.css">
	 	
	 	
	 </head>

  	<body class="bg-light">
  	
  		<%
		//Google sign-in initialization 
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
  		
  		ObjectifyService.register(smartcal.UserAccount.class);
  		
  		if (user == null) {
  			%>
  			<div class="container border border-primary p-3 m-3 bg-white">
  				<div class="row"><div class="col">
  					<h1>Welcome to <span class="text-primary">Smart Calendar!</span> Please Sign In</h1>
  				</div></div>
  				<div class="row"><div class="col">
					<a class="btn btn-sm btn-success w-100" href="<%=(userService.createLoginURL(request.getRequestURI()))%>" role="button">Sign in</a>
				</div></div>
			</div>
			<%
  		}
  		else {
			smartcal.UserAccount accountData = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).filter("user", user).first().now();
  		
  			if (accountData == null) {
				accountData = new smartcal.UserAccount(user, user.getNickname());
				ObjectifyService.ofy().save().entity(accountData);
				System.out.println("your username is now: " + accountData.getUsername());
				%>
				<div class="container border border-primary p-3 m-3 bg-white">
	  				<div class="row"><div class="col">
	  					<h1 class="">It's your first time using <span class="text-primary">Smart Calendar!</span></h1>
	  				</div></div>
	  				<div class="row"><div class="col">
	  					<form action="/firstlogin" method="post">
	  						<div class="form-group">
			       				<div class="input-group">	<!-- form groups style inputs like our month/year switcher -->					    			
					    			<span class="input-group-addon">Please select a nickname</span>
					    			<input name="username" class="form-control">
									<button name="setname" class="input-group-addon">Set Name</button>
					    		</div>
					    	</div>
							
						</form>
					</div></div>
				</div>
				<%
				
  			}
  			else {
  				
  		%>
  		<nav class="navbar navbar-expand-lg p-1 navbar-light bg-light sticky-top border border-top-0 border-left-0 border-right-0 border-primary" border-width="thick">
  			<a class="navbar-brand" href="#">Smart Calendar</a>
  			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
		    	<span class="navbar-toggler-icon"></span>
		 	</button>
		 	
		 	<div class="collapse navbar-collapse" id="navbarContent">
			    <ul class="navbar-nav mr-auto">
			    	<li class="nav-item">
			        	<a class="nav-link" href="/calendar.jsp">Home</a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="#">Account</a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="/friends.jsp">Social</a>
			      	</li>
			      	<li class="nav-item active">
			        	<a class="nav-link" href="/newevent.jsp">New Event<span class="sr-only">(current)</span></a>
			      	</li>
			    </ul>
				
				<span class="navbar-text text-muted mr-3">Hello, <%=(accountData.getUsername())%>! </span>
				<a class="btn btn-sm btn-outline-danger" href="<%=(userService.createLogoutURL(request.getRequestURI()))%>">Sign Out</a>
			
			</div>	
		</nav>
	
		<%
		//main method initialization stuff
		
       	ObjectifyService.register(smartcal.FriendsList.class);
		ObjectifyService.register(smartcal.Invitation.class);
		
       	//grabbing FriendsList for the current user
       	smartcal.FriendsList flist = ObjectifyService.ofy().load().type(smartcal.FriendsList.class).filter("user", user).first().now();
       	if(flist == null){
       		System.out.println(user + " didnt have a friendslist for some reason, creating it now");
       		flist = new smartcal.FriendsList(user);
       		ObjectifyService.ofy().save().entity(flist);
       	}else{
       		System.out.println("friendslist found: \n" + flist);		// THIS STATEMENT IS TO DEBUG, PRINTS THE WHOLE FRIENDSLIST. CAN BE REMOVED
       	}
       	
       	smartcal.Invitation invitation = ObjectifyService.ofy().load().type(smartcal.Invitation.class).filter("creator", user).filter("finished", false).first().now();
       	
       	%>
       	
       	<!-- STAGE 1 -->
       	<%if(invitation == null) {%>
       		<div class="container">
	       		<div class="row"><div class="col-md">
	       			<h2><span class="text-primary">New Event</span></h2>
	       		</div></div>
	       		
	       		<div class="row"><div class="col-md">
	       		<!-- Main Event Creation Stuff -->	
	       		<table class="table table-bordered table-light">
	       			<thead class="thead-dark"><tr class="d-flex w-100">
	      		  			<th class="w-100">Choose a Name and Type</th>
	      			</tr> </thead> 
	      			<tbody>
	      			<tr><td>
			       		<form action="/newevent" name="stage1" method="post"> 
			       			
			       			<div class="form-group">
			       				<div class="input-group">	<!-- form groups style inputs like our month/year switcher -->
					    			<span class="input-group-addon">Event Name</span>
					    			<input name="eventname" class="form-control">
					    		</div>
					    	</div>
					    	
					    	<div class="form-group input-group">
							    <span class="input-group-addon"><label>Event Type</label></span>
							  	<div class="form-control form-check">
									
								  	<label class="form-check form-check-label ml-1"><input class="form-check-input" type="radio" name="eventtype" value="Generic">Generic</label>
								</div>
								<div class="form-control form-check">
									
								  	<label class="form-check form-check-label ml-1"><input class="form-check-input" type="radio" name="eventtype" value="Movie">Movie</label>
								</div>
	  						</div>
	  						
	  						<div class="form-group form-group-lg">
		  						<div class="input-group">
									<span class="input-group-addon"><label>Invite Friends</label></span>
		  							<% List<User> friends = flist.getFriends();
		  							if (friends.size() == 0) { %>
		  								<div class="form-control form-check">
									  		<label class="form-check-label">You have no friends.</label>
										</div>
		  							<%}
		  							for (int i = 0; i < friends.size(); i++) {
		  								if (friends.get(i) != null) {%>											
		  								<div class="form-control form-check">
									  		<label class="form-check-label">
										    <input class="form-check-input" type="checkbox" name="friend<%=(i)%>" value="friend<%=(i)%>"> <%=friends.get(i).getNickname()%>
										  	</label>
										</div>
										<%}%>
		  							<%}%>
			  					</div>
	  						</div>
	  						
	  						<button name="part1submit" class="form-control form-control-lg btn btn-success" type="submit">Start Invitation</button>
	  					
			       		</form>	
				   </td></tr>
				   </tbody>	
				</table>
	       		</div></div>
	       	</div> 	
	    
	    
	    
	    <!-- STAGE 2 -->
     	<%} 
     	else if (invitation.getStarted()) { %>
       		<div class="container">
	       		<div class="row"><div class="col-md">
	       			<h2><span class="text-primary"><%=(invitation.getName())%></span></h2>
	       		</div></div>
	       		
	       		<!-- Print info from stage 1 -->
	       		<div class="row"><div class="col-md">
	       		<table class="table table-bordered table-light">
	       			<thead class="thead-dark"><tr class="d-flex w-100">
	      		  			<th class="w-100">People</th>
	      			</tr> </thead> 
	      			<tbody><tr><td>
					    <% List<User> friends = invitation.getFriends();
		  				if (friends.size() != 0) { %>
		    				<ul class="list-group">
	  							<%
	  							for (int i = 0; i < friends.size(); i++) { 
	  								if (friends.get(i) != null) {%>
									<li class="list-group-item w-100"> <%=friends.get(i).getNickname()%></li>
									<%}%>
								<%} %>
  							</ul>
					    <%}%>				
				   </td></tr></tbody>	
				</table>
				</div></div>
				
				<%if (invitation.getType() == smartcal.Invitation.Type.G) {%>
				<!-- GENERIC TYPE -->
				
				<!-- List Existing Options -->
				<% List<smartcal.InvitationOption> options = invitation.getOptions();
				int i = 0;
				if (options != null && options.size() > 0)  { %>
					<div class="row"><div class="col-md">
	       			<table class="table table-bordered table-light">
		       			<thead class="thead-dark"><tr class="d-flex w-100">
		      		  			<th class="w-100">Options</th>
		      			</tr> </thead> 
		      			<tbody><tr><td>  
		      				<% for (smartcal.InvitationOption option : options) {
							i++; %>
		      				<div class="form-group">
			       				<div class="input-group">
			       					<span class="input-group-addon"><%=(i)%></span>
			       					<span class="input-group-addon">Location</span>
				    				<input readonly name="eventname" class="form-control bg-white" value="<%=(option.getLocation())%>">
					    			<span class="input-group-addon">Time</span>
					    			<input readonly name="eventname" class="form-control bg-white" value="<%=(option.getTimeString())%>">
					    		</div>
					    	</div>
					    	<%} %>
						</td></tr></tbody>
					</table>
					</div></div>
				<%} %>
				
				<!-- Add New Option -->
				<div class="row"><div class="col-md">
	       		<table class="table table-bordered table-light">
	       			<thead class="thead-dark"><tr class="d-flex w-100">
	      		  			<th class="w-100">Add an Option</th>
	      			</tr> </thead> 
	      			<tbody><tr><td>  
				       	<form action="/newevent" name="stage2" method="post"> 	
				       		<div class="form-group">
			       				<div class="input-group">	<!-- form groups style inputs like our month/year switcher -->
					    			<span class="input-group-addon">Location</span>
					    			<input name="location" class="form-control">
					    		</div>
					    	</div>
					    	<div class="form-group">
			       				<div class="input-group">	<!-- form groups style inputs like our month/year switcher -->
					    			<span class="input-group-addon">Start Day</span>
					    			<input type="date" name="startday" class="form-control">
					    			<span class="input-group-addon">End Day</span>
					    			<input type="date" name="endday" class="form-control">
					    			<span class="input-group-addon bg-white">
								  		<label class="form-check-label">
									    <input id="allday" name="allday" class="form-check-input" type="checkbox">All Day
									  	</label>
									</span>
					    		</div>
					    	</div>
				       		<div class="form-group">
			       				<div class="input-group">	<!-- form groups style inputs like our month/year switcher -->
					    			<span class="input-group-addon">Start Time</span>
					    			<input type="time" id="starttime" name="starttime" class="form-control">
					    			<span class="input-group-addon">End Time</span>
					    			<input type="time" id="endtime" name="endtime" class="form-control">
					    		</div>
					    	</div>
				       	
				       		<div class="form-group">
		  						<div class="input-group">
									
			  					</div>
	  						</div>
	  						
				       		<button name="newoption" class="form-control form-control-lg btn-outline" type="submit">Add Option</button>
				       	</form>
	      				</td></tr></tbody>
	       		</table>
	       		</div></div> 
	       		
	       		<% } else if (invitation.getType() == smartcal.Invitation.Type.M) { %>
	       		<!-- MOVIE TYPE -->
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		<%} %>
	       		
	       		<div class="row"><div class="col-md">
	       			<form action="/newevent" name="finishInvitation" method="post"> 	
				       		<button name="part2submit" class="form-control form-control-lg btn-lg btn-success" type="submit">Finish Invitation</button>
				       	</form>
	       		</div></div>      		
       		</div>
       	<%} } } %>
       	
       	<script>
		 	document.getElementById('allday').onchange = function() {
		 		document.getElementById('starttime').disabled = this.checked;
		 	    document.getElementById('endtime').disabled = this.checked;
		 	};
	 	</script>
       	
  
		<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
	    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
		
	</body>
</html>
