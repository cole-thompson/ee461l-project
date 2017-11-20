<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
	<head>
	 	<title>Calendar</title>
	 	<!-- Required meta tags -->
	 	<meta charset="utf-8">
	 	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
		<!-- Bootstrap CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	 </head>

  	<body>
  	
  		<nav class="navbar navbar-expand-lg navbar-light bg-light">
  			<a class="navbar-brand" href="#">Calendar</a>
  			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
		    	<span class="navbar-toggler-icon"></span>
		 	</button>
		 	
		 	<div class="collapse navbar-collapse" id="navbarContent">
		    <ul class="navbar-nav mr-auto">
		      <li class="nav-item active">
		        <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
		      </li>
		      <li class="nav-item dropdown">
		        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Dropdown</a>
		        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
		          <a class="dropdown-item" href="#">Action</a>
		          <div class="dropdown-divider"></div>
		          <a class="dropdown-item" href="#">Something else here</a>
		        </div>
		      </li>
		    </ul>

		<%
		//Google sign-in initialization 
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    String username = "";
	    if (user == null) {
			%>
			<span class="navbar-text">Hello!
			<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a></span>
			<%
	    }
	    else {
	    	username = "" + user.getNickname();
	      	pageContext.setAttribute("user", user);
			%>
			<span class="navbar-text">Hello, ${fn:escapeXml(user.nickname)}! 
			<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">(sign out</a>.)</span>
			<%
	    }  
		%>	
		
		</div>	
		</nav>
	
		<%
		//main method initialization stuff
		ObjectifyService.register(smartcal.CalEvent.class);
        ObjectifyService.register(smartcal.UserDisplayData.class);
        
		//create java calendar, get current day/month/year
		Calendar currentCal = new GregorianCalendar();
		int currentDate = currentCal.get(Calendar.DATE);
		int currentMonth = currentCal.get(Calendar.MONTH);
		int currentYear = currentCal.get(Calendar.YEAR);
				
		//grab user display information from objectify
       	smartcal.UserDisplayData display = ObjectifyService.ofy().load().type(smartcal.UserDisplayData.class).filter("user", user).first().now();
       	if (display == null) {
       		System.out.println("null");
       		display = new smartcal.UserDisplayData(user);
       	}
       	
		//set the display month and year to current if invalid
		int displayYear, displayMonth = 0;
	   	if ((displayMonth = display.getDisplayMonth()) == -1) {
	   		displayMonth = currentMonth;
	   	}
		if ((displayYear = display.getDisplayYear()) == -1) {
			displayYear = currentYear;
	   	}
		
	    
		/*
		 * get info about the month
		 * numDays = number of days in the month
		 * firstDayOfWeek = day of week on the 1st of the month
		 * numWeeks = number of weeks in the month
		 */
		Calendar displayCal = new GregorianCalendar(displayYear, displayMonth, 1);
		int numDays = displayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int firstDayOfWeek = displayCal.get(Calendar.DAY_OF_WEEK);
		displayCal = new GregorianCalendar(displayYear, displayMonth, numDays);
		int numWeeks = displayCal.get(Calendar.WEEK_OF_MONTH);	
		%>
		
	
	  <!-- http://getbootstrap.com/docs/4.0/content/tables/ -->
	  
	  <div class="container bg-light border border-primary p-1" >	<!-- This div has 2 parts: header row (month name, forms) and the actual calendar -->
		
		<!-- Header Row: Drop down menus to change month and year-->
		
		<div class="row m-1 align-middle"> 
			<div class="col-md-auto"> 
				<table>
				<tr><td><h2><span class="text-primary"><%=(displayMonth + 1)%></span> <span class="text-secondary"><%=(smartcal.UserDisplayData.getMonthName(displayMonth))%></span></h2></td></tr>
				<tr><td><span class="text-secondary"><%=(username)%></span></td></tr>
				</table>
			</div>
			
			<div class="col-md">
				<form action="/calendar" name="changeMonth" method="post">
				<div class="form-group form-group-lg">
					<div class="input-group input-group-lg"> 
					  	<select name="formMonth" onchange="changeMonth.submit()" class="form-control form-control-lg">
		    				<% for (int m = 0; m < 12; m++) { 
		    					if (m == displayMonth) { %><option selected="selected" class="bg-primary text-light"> <% }
		    					else if (m == currentMonth && displayYear == currentYear) { %><option class="bg-success text-light"> <% }
		    					else { %><option><% } %>
		    					<%=(smartcal.UserDisplayData.getMonthName(m))%>
		    					</option>
		  					<%} %>
						</select>
						<span class="input-group-addon">/</span>
		    			<input name="formYear" onchange="changeMonth.submit()" class="form-control form-control-lg" value="<%=(displayYear)%>" type="number" min="1000" max="3000">
		    		</div>		    		
				</div>
				</form>
			</div>
			
			<div class="col-md-auto"><form action="/calendar" name="toToday" method="post">
			<button name="today" class="btn btn-lg btn-success text-light" type="submit">Today</button>
			</form></div>
		</div>
	    
	    	    
	    <!-- Calendar table -->
		<div class="row m-1">
	    	<div class="col-xl">
	        	<table class="table table-bordered table-light">
	          		<thead class="thead-dark">
	            	<tr>
		            	<th scope="col">Sunday</th>
		            	<th scope="col">Monday</th>
		              	<th scope="col">Tuesday</th>
		              	<th scope="col">Wednesday</th>
		              	<th scope="col">Thursday</th>
		              	<th scope="col">Friday</th>
		              	<th scope="col">Saturday</th>
	            	</tr>
	          		</thead>
		         	<tbody>		<!-- Iterate through the table, place numbers in proper locations -->
		          	<% int day = 0;
		          	for (int week = 0; week < numWeeks; week++) { %>
		            	<tr>
		            	<% for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) { 
		            		if ((day == currentDate) && (displayMonth == currentMonth) && (displayYear == currentYear)) {
			            		//current day
			            		%><td class ="table-success"><%
		            		}
		            		else { %><td><% }
		            	
		            		if ((day < numDays) && !(week == 0 && dayOfWeek < firstDayOfWeek)) { %>
		            			<%day++;%>
		            			<%=(day)%>		<!-- This is where day number is printed, might need some styling-->
		            			<!-- contents of a calendar cell - add stuff about events here -->
		            			<!-- TODO -->
		            			
		            			
		            		<%} %>
		            		</td>
		            	<%}%>
		            	</tr>
		      	  	<%} %>
		          	</tbody>
	        	</table>
	    	</div>
		</div>
	</div>
  
	<!-- Print Tests for the Calendar Info -->
  	<!--  
  	<div class="row"> <div class="col-sm"> current year: <%=(currentYear)%></div></div>
    <div class="row"> <div class="col-sm"> current month: <%=(currentMonth)%></div></div>
    <div class="row"> <div class="col-sm"> current date: <%=(currentDate)%></div></div>
    <div class="row"> <div class="col-sm"> numDays: <%=(numDays)%></div></div>
    <div class="row"> <div class="col-sm"> firstDayOfWeek: <%=(firstDayOfWeek)%></div></div>
    <div class="row"> <div class="col-sm"> numWeeks: <%=(numWeeks)%></div></div>
	-->	
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
	
	</body>
</html>
