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
	    if (user != null) {
	      	pageContext.setAttribute("user", user);
			%>
			<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
			<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
			<%
	    } else {
			%>
			<p>Hello!
			<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a></p>
			<%
	    }
		%>
	
		<%!
		/*
		 * zero-indexed months, can take Calendar.Month inputs
		 */
		private String getMonthName(Integer m) {
			if (m == null)
				return "";
			String name = "";
			switch (m) {
				case 0: 	name = "January";break;
				case 1: 	name = "February";break;
				case 2: 	name = "March";break;
				case 3: 	name = "April";break;
				case 4: 	name = "May";break;
				case 5: 	name = "June";break;
				case 6: 	name = "July";break;
				case 7: 	name = "August";break;
				case 8: 	name = "September";break;
				case 9: 	name = "October";break;
				case 10: 	name = "November";break;
				case 11: 	name = "December";break;
			}
			return name;
		}
		%>
		
		<%!
		private int getMonthInt(String mString) {
			if (mString == null)
				return -1;
			int m = -1;
			switch (mString) {
				case "January": 	m = 0;break;
				case "February": 	m = 1;break;
				case "March": 		m = 2;break;
				case "April": 		m = 3;break;
				case "May": 		m = 4;break;
				case "June": 		m = 5;break;
				case "July": 		m = 6;break;
				case "August": 		m = 7;break;
				case "September": 	m = 8;break;
				case "October": 	m = 9;break;
				case "November": 	m = 10;break;
				case "December": 	m = 11;break;
				default: 			m = -1;break;
			}
			return m;
		}
		%>
		
		<%!
		private int getYear(String yearString) {
			int maxYear = 4000;
			int minYear = 0;
			int year = -1;
			try {
				//check bounds of year
				year = Integer.parseInt(yearString);
				if (year > maxYear || year < minYear) {
					year = -1;
				}
			}
			catch (Exception e) {
				year = -1;
			}
			return year;
		}
		
		%>
	
		<%
		//main method initialization stuff
		
		//create java calendar, get current day/month/year
		Calendar currentCal = new GregorianCalendar();
		int currentDate = currentCal.get(Calendar.DATE);
		int currentMonth = currentCal.get(Calendar.MONTH);
		int currentYear = currentCal.get(Calendar.YEAR);
		
		/* Form inputs - 
		 * if the month/year forms have content, set the display month/year
		 * If forms are empty or invalid, display current month/year
		 */
		int displayYear, displayMonth = 0;
	   	if ((displayMonth = getMonthInt(request.getParameter("formMonth"))) == -1) {
	   		displayMonth = currentMonth;
	   	}
		if ((displayYear = getYear(request.getParameter("formYear"))) == -1) {
			displayYear = currentYear;
	   	}
		
		/* year up/down buttons input
		 * check if year up/down buttons were pressed, adjust displayYear
		 */
		 /*
		if(request.getParameter("downYear") != null) {
			displayYear--;
		}
		else if(request.getParameter("upYear") != null) {
			displayYear++;
		}*/
	
	    
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
	  <div class="container">	<!-- This div has 2 parts: header row (month name, forms) and the actual calendar -->
		
		<!-- Header Row: Drop down menus to change month and year-->
		<form action="calendar" name="changeMonth" method="post">
		<div class="row"> 
			<div class="col-sm"> <h2><%=(getMonthName(displayMonth))%></h2> </div>
			<div class="col-sm">
				<div class="form-group">
	    		<select name="formMonth" onchange="changeMonth.submit()" class="form-control form-control-lg">
	    			<% for (int m = 0; m < 12; m++) { 
	    				if (m == displayMonth) { %><option selected="selected" class="bg-primary text-light"> <% }
	    				else { %><option><% } %>
	    				<%=(getMonthName(m))%>
	    				</option>
	  				<%} %>
				</select>
				</div>
			</div>
			<div class="col-sm">
				<div class="form-group">
					<div class="input-group">   				
		    			<input name="formYear" onchange="changeMonth.submit()" class="form-control form-control-lg" value="<%=(displayYear)%>">
		    			<span class="input-group-addon">
		    				<div class="btn-group" role="group">
			    				<button type="submit" class="btn btn-secondary" name="downYear" onclick="document.getElementById('formYear').value = '<%=(displayYear - 1)%>';">&#9660;</button>
		  						<button type="submit" class="btn btn-secondary" name="upYear" onclick="document.getElementById('formYear').value = '<%=(displayYear + 1)%>';">&#9650;</button>
		  					</div>
		    			</span>
		    		</div>
				</div>
			</div>
		</div>
	    </form>
	    
	    <!-- Calendar table -->
		<div class="row">
	    	<div class="col-xl">
	        	<table class="table table-bordered">
	          		<thead>
	            	<tr class="table-primary">
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
