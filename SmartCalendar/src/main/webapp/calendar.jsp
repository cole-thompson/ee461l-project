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
		<%
		//Google sign-in initialization 
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    String username = "";
	    if (user == null) {
			%>
			<p>Hello!
			<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a></p>
			<%
	    }
	    else {
	    	username = "" + user.getNickname();
	      	pageContext.setAttribute("user", user);
			%>
			<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
			<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
			<%
	    }  
		%>	
	
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
	  
	  <h1>Calendar</h1>
	  <div class="container bg-light border border-primary p-2" >	<!-- This div has 2 parts: header row (month name, forms) and the actual calendar -->
		
		<!-- Header Row: Drop down menus to change month and year-->
		<form action="/calendar" name="changeMonth" method="post">
		<div class="row"> 
			<div class="col-sm"> 
				<table>
				<tr><td><h2><span class="text-primary"><%=(displayMonth)%></span> <span class="text-secondary"><%=(smartcal.UserDisplayData.getMonthName(displayMonth))%></span></h2></td></tr>
				<tr><td><span class="text-secondary"><%=(username)%></span></td></tr>
				</table>
			</div>
			<div class="col-sm">
				<div class="form-group">
					<div class="input-group"> 
					  	<select name="formMonth" onchange="changeMonth.submit()" class="form-control form-control-lg">
		    				<% for (int m = 0; m < 12; m++) { 
		    					if (m == displayMonth) { %><option selected="selected" class="bg-primary text-light"> <% }
		    					else { %><option><% } %>
		    					<%=(smartcal.UserDisplayData.getMonthName(m))%>
		    					</option>
		  					<%} %>
						</select>
						<span class="input-group-addon">/</span>
		    			<input name="formYear" onchange="changeMonth.submit()" class="form-control form-control-lg" value="<%=(displayYear)%>" type="number" min="1000" max="3000">
		    			
		    		</div>
				</div>
			</div>
		</div>
	    </form>
	    	    
	    <!-- Calendar table -->
		<div class="row">
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
	</body>
</html>
