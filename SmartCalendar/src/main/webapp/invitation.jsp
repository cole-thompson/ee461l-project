<%@page import="smartcal.InvitationsList"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Date" %>
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
			System.out.println("no account data found for: " + user.getNickname());
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
					<div class="row"><div class="col">
						<a class="btn btn-outline-danger" href="<%=(userService.createLogoutURL(request.getRequestURI()))%>">Google Sign Out</a>
					</div></div>
				</div>
				<%
				
  			}
  			else {
  				
  				
  		%>
  		
  		<%! public int getInvitationSize(smartcal.Invitation invitation) {
	    	int size = 0;
	    	if (invitation.getType() == smartcal.Invitation.Type.G) {
       			size = invitation.getOptions().size();
       		}
	    	else if (invitation.getType() == smartcal.Invitation.Type.M) {
       			size = invitation.getMovieOptions().size();
       		}
	    	return size;
	    
	    } %>
	    
  		<nav class="navbar navbar-expand-lg p-1 navbar-light bg-light sticky-top border border-top-0 border-left-0 border-right-0 border-primary" border-width="thick">
  			<a class="navbar-brand" href="/calendar.jsp">Smart Calendar</a>
  			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
		    	<span class="navbar-toggler-icon"></span>
		 	</button>
		 	
		 	<div class="collapse navbar-collapse" id="navbarContent">
			    <ul class="navbar-nav mr-auto">
			    	<li class="nav-item">
			        	<a class="nav-link" href="/calendar.jsp">Home</a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="/accounts.jsp">Account</a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link active" href="/friends.jsp">Social<span class="sr-only">(current)</span></a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="/newevent.jsp">New Event</a>
			      	</li>
			    </ul>
				
				<span class="navbar-text text-muted mr-3">Hello, <%=(accountData.getUsername())%>! </span>
				<a class="btn btn-sm btn-outline-danger" href="<%=(userService.createLogoutURL(request.getRequestURI()))%>">Sign Out</a>
			
			</div>	
		</nav>
		
		
		 <%
		 ObjectifyService.register(smartcal.InvitationsList.class);
		 ObjectifyService.register(smartcal.Invitation.class);
		 
		 InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(smartcal.InvitationsList.class).filter("user", user).first().now();
		 smartcal.Invitation toDisplay = currentUserInvitations.getDisplayedInvitation();
		 %>
		 
		 
		 
		 <div class="container">
		 
		 <div class="row">
	     	<div class="col-md"><h2><span class="text-secondary">Invitation: </span><span class="text-primary"><%=(toDisplay.getName())%></span></h2></div>
	     </div>
		 
		 <%if (!toDisplay.hasPersonVoted(user)) {%>
		 <div class="row"><div class="col-md">
		 	<form action="/social" name="optionsform" method="post">
	     	<table class="table table-bordered table-light">
	       		<thead class="thead-dark"><tr class="d-flex w-100">
	      			<th class="w-100">Select Available Options</th>
	      		</tr> </thead> 
	    		
	    		<tbody>
		    		<tr><td>
					<ul class="list-group">
					 <% if (getInvitationSize(toDisplay) == 0) { %>
					 	<li class="list-group-item w-100"><span class="text-secondary">(None)</span></li>
				 	 <%}
					 int i = 0;
					 if (toDisplay.getType() == smartcal.Invitation.Type.G) {
						 List<smartcal.InvitationOption> options = toDisplay.getOptions();
						 for(smartcal.InvitationOption op : options){
				 				String loc = op.getLocation();
				 				String time = op.getTimeString();
				 				%>
				 				<li class="list-group-item w-100">
				 				<div class="form-group"> 			
							  		<label class="form-check-label">
									    <input class="form-check-input" type="checkbox" name="option<%=(i)%>" value="option<%=(i)%>">
								  		<span class="text-secondary">Location: </span><%=loc %><br />
							    		<%=time %>
								  	</label>
				 	 			</div>
				 	 			</li>
						<%i++;
						}
					 }
					 else if (toDisplay.getType() == smartcal.Invitation.Type.M) {
						 List<smartcal.MovieOption> options = toDisplay.getMovieOptions();
						 for(smartcal.InvitationOption op : options){%>
				 				<li class="list-group-item w-100">
				 				<div class="form-group"> 			
							  		<label class="form-check-label">
									    <input class="form-check-input" type="checkbox" name="option<%=(i)%>" value="option<%=(i)%>">
									    <span class="text-secondary">Movie: </span><%=(op.getOptionName())%><br />
								  		<span class="text-secondary">Theater: </span><%=(op.getLocation())%><br />
							    		<%=(op.getStartTime().toString())%>
								  	</label>
				 	 			</div>
				 	 			</li>
						<%i++;
						}
					 }%>
					 	
					</ul>
					</td></tr>
					
					<tr><td>
						<button name="sendoptions" class="form-control form-control-lg btn-outline-success" type="submit">Send Availabilities</button>
					</td></tr>
				</tbody>	
			</table>
			</form>
		</div></div>
		<% } %>
		
		<div class="row"><div class="col-md">
	     	<table class="table table-bordered table-light">
	       		<tr >
	      			<th colspan="2" class="table-dark">Progress</th>
	      		</tr> 
	      		
	      		<tr ><td colspan="2">
	      			<ul class="list-group">
	      				<li class="list-group-item"><span class="text-secondary">created by </span><span class="text-primary"><%=(smartcal.UserAccount.getNameForUser(toDisplay.getCreator()))%></span></li>
	      			</ul>
	      		</td></tr> 
	      		
	    		<tr>
		    		<td><ul class="list-group">
		    		<li class="list-group-item"><span class="text-primary">Options</span></li>
		    		<% if (toDisplay.getType() == smartcal.Invitation.Type.G) {
		    			List<smartcal.InvitationOption> options = toDisplay.getOptions();
		    			if (getInvitationSize(toDisplay) == 0) { %>
						 <li class="list-group-item"><span class="text-secondary">(None)</span></li>
					 	<%}
						 for(smartcal.InvitationOption op : options){
				 				String loc = op.getLocation();
				 				String time = op.getTimeString();%>
				 				<li class="list-group-item">
							  		<p><span class="text-secondary">Location: </span><%=loc %></p>
						    		<p><span class="text-secondary">When: </span><%=time %></p>
						    		<p>
						    			<span class="text-secondary">People Available (<%=(op.numAvailablePeople())%>): </span>
						    			<%List<User> availablePeople = op.getAvailablePeople();
						    			for (int i = 0; i < availablePeople.size(); i++) {
						    				User u = availablePeople.get(i);%>
						    				<span class="">
						    					<%=(smartcal.UserAccount.getNameForUser(u))%>
						    					<%if (i < availablePeople.size() - 1) { %><%=(", ")%><% } %>
						    				</span>
						    			<%} %>
						    		</p>
				 	 			</li>
						<%}
					 }
					 else if (toDisplay.getType() == smartcal.Invitation.Type.M) {
						 List<smartcal.MovieOption> movieOptions = toDisplay.getMovieOptions();
						 if (getInvitationSize(toDisplay) == 0) { %>
						 <li class="list-group-item"><span class="text-secondary">(None)</span></li>
					 	<%}
						 for(smartcal.MovieOption op : movieOptions){%>
				 				<li class="list-group-item">
				 					<p><span class="text-secondary">Movie: </span><%=(op.getOptionName())%></p>
								  	<p><span class="text-secondary">Theater: </span><%=(op.getLocation())%></p>
							    	<p><%=(op.getStartTime().toString())%></p>
						    		<p>
						    			<span class="text-secondary">People Available (<%=(op.numAvailablePeople())%>): </span>
						    			<%List<User> availablePeople = op.getAvailablePeople();
						    			for (int i = 0; i < availablePeople.size(); i++) {
						    				User u = availablePeople.get(i);%>
						    				<span class="">
						    					<%=(smartcal.UserAccount.getNameForUser(u))%>
						    					<%if (i < availablePeople.size() - 1) { %><%=(", ")%><% } %>
						    				</span>
						    			<%} %>
						    		</p>
				 	 			</li>
						<%}%>
						 
					<% }%>
					 
					 
					 		
					</ul></td>
					
					<td><ul class="list-group">
					<li class="list-group-item">
						<span class="text-primary">People </span>
						<span class="text-secondary"><%=("(" + toDisplay.numPeopleVoted() + "/" + toDisplay.numPeople() + ") voted")%></span>
					</li>
					 <% for(User u : toDisplay.getFriends()){%>
			 			<li class="list-group-item">
			 				<span class=""><%=(smartcal.UserAccount.getNameForUser(u))%></span>
			 				<span class="text-secondary">
			 					<%if(toDisplay.hasPersonVoted(u)) {%><%=(" (has voted)")%><% }
			 					else {%><%=(" (hasn't voted)")%><% }%>
			 				</span>
			 			</li>
					<%}%>		
					</ul></td>
				
				</tr>
			</table>
		</div></div>
		
		<%if(toDisplay.getCreator().equals(user)) {%>
		 <div class="row"><div class="col-md">
		 	<form action="/social" name="decisionform" method="post">
	     	<table class="table table-bordered table-light">
	       		<thead class="thead-dark"><tr class="d-flex w-100">
	      			<th class="w-100">Final Decision</th>
	      		</tr> </thead> 
	    		
	    		<tbody>
		    		<tr><td>
					<ul class="list-group">
					 <% if (getInvitationSize(toDisplay) == 0) { %>
					 	<li class="list-group-item w-100"><span class="text-secondary">(None)</span></li>
				 	 <%}
					 int i = 0;
					 if (toDisplay.getType() == smartcal.Invitation.Type.G) {
						 List<smartcal.InvitationOption> options = toDisplay.getOptions();
						 for(smartcal.InvitationOption op : options){
				 				String loc = op.getLocation();
				 				String time = op.getTimeString();
				 				%>
				 				<li class="list-group-item w-100">
				 				<div class="form-group"> 			
							  		<label class="form-check-label">
									    <input class="form-check-input" type="checkbox" name="option<%=(i)%>" value="option<%=(i)%>">
								  		<span class="text-secondary">Location: </span><%=loc %><br />
							    		<%=time %>
								  	</label>
				 	 			</div>
				 	 			</li>
						<%i++;
						}
					 }
					 else if (toDisplay.getType() == smartcal.Invitation.Type.M){
						 List<smartcal.MovieOption> options = toDisplay.getMovieOptions();
						 for(smartcal.InvitationOption op : options){%>
				 				<li class="list-group-item w-100">
				 				<div class="form-group"> 			
							  		<label class="form-check-label">
									    <input class="form-check-input" type="checkbox" name="option<%=(i)%>" value="option<%=(i)%>">
									    <span class="text-secondary">Movie: </span><%=(op.getOptionName())%><br />
								  		<span class="text-secondary">Theater: </span><%=(op.getLocation())%><br />
							    		<%=(op.getStartTime().toString())%>
								  	</label>
				 	 			</div>
				 	 			</li>
						<%i++;
						}
					 }%>
						
					</ul>
					</td></tr>
					
					<tr><td>
						<button name="finalizeinvitation" class="form-control form-control-lg btn-outline-success" type="submit">Finalize Invitation</button>
					</td></tr>
				</tbody>	
			</table>
			</form>
		</div></div>
		<% } %>
		
		</div>
		<%} }%>	
	 </body>
 </html>