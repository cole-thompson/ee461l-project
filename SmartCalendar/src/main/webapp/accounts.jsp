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
	 
	 <%ObjectifyService.register(smartcal.UserAccount.class); 
	 
	 UserService userService = UserServiceFactory.getUserService();
     User user = userService.getCurrentUser();
    
	 smartcal.UserAccount currentUserAccount = ObjectifyService.ofy().load().type(smartcal.UserAccount.class).filter("user", user).first().now();%>

  	<body class="bg-light">
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
			        	<a class="nav-link" href="/accounts.jsp">Account</a>
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
				
				<span class="navbar-text text-muted mr-3">Hello, <%=(currentUserAccount.getUsername())%>! </span>
				<a class="btn btn-sm btn-outline-danger" href="<%=(userService.createLogoutURL(request.getRequestURI()))%>">Sign Out</a>
			
			</div>	
		</nav>

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
		
  	</body>