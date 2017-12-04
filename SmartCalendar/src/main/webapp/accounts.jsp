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
<%@ page import="com.googlecode.objectify.Objectify" %>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
	<head>
	 	<title>Account Info</title>
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
			System.out.println("no account data found for: " + user.getNickname());
			%>
			
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
			        	<a class="nav-link active" href="/accounts.jsp">Account<span class="sr-only">(current)</span></a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="/friends.jsp">Social</a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="/newevent.jsp">New Event</a>
			      	</li>
			    </ul>
				
				<span class="navbar-text text-muted mr-3">Hello, <%=(accountData.getUsername())%>! </span>
				<a class="btn btn-sm btn-outline-danger" href="<%=(userService.createLogoutURL(request.getRequestURI()))%>">Sign Out</a>
			
			</div>	
		</nav>

		<div class="container">
		<div class="row"><div class="col-md">
       		<h2><span class="text-primary">Account</span></h2>
       	</div></div>
	       	
	    <div class="row"><div class="col-md">   	
		<table class="table">
		  <thead class="thead-dark">
		    <tr>
		      <th scope="col"> User Info </th>
		      <th scope="col">  </th>
		    </tr>
		  </thead>
		  <tbody>
		    <tr>
		      <th scope="row">Username</th>
		      <td><%=currentUserAccount.getUsername() %></td>

		    </tr>
		    <tr>
		      <th scope="row">Email</th>
		      <td><%=currentUserAccount.getEmail() %></td>
		    </tr>
		  </tbody>
		</table>
		</div></div>
		
		<div class="row"><div class="col-md">
			<form action="/firstlogin" name="deleteAccountForm" method="post">
				<button class="btn btn-danger" name="deleteuserinfo">Delete Account</button>
			</form>
		</div></div>
		
		</div>
  	</body>