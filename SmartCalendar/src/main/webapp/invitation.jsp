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
	 
	 <%UserService userService = UserServiceFactory.getUserService();
	 User user = userService.getCurrentUser();
	 
	 ObjectifyService.register(smartcal.InvitationsList.class);
	 ObjectifyService.register(smartcal.Invitation.class);
	 
	 InvitationsList currentUserInvitations = ObjectifyService.ofy().load().type(smartcal.InvitationsList.class).filter("user", user).first().now();
	 smartcal.Invitation toDisplay = currentUserInvitations.getDisplayedInvitation();
	  %>
	 
	 <body>
		 <%int i = 0;
		 for(smartcal.InvitationOption op : toDisplay.getOptions()){
 				String loc = op.getLocation();
 				String time = op.getTimeString();
 				i++;%>
 				<form> 			
 					<div class="form-control form-check">
				  		<label class="form-check-label">
						    <input class="form-check-input" type="checkbox" name="friend<%=(i)%>" value="friend<%=(i)%>"> Option <%=i %>
						    	
					  	</label>
					  	<div><p><%=loc %></p></div>
				    	<div><p><%=time %></p></div>
					</div>
 					
 	 			</form>
		<%}%>		
	 </body>
 </html>