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
	 	<title>Calendar</title>
	 	<!-- Required meta tags -->
	 	<meta charset="utf-8">
	 	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
		<!-- Bootstrap CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	 	
	 	<!-- Our custom css -->
	 	<link rel="stylesheet" type="text/css" href="calendar.css">
	 	
	 </head>

  	<body class="bg-light">
  	
  		<nav class="navbar navbar-expand-lg navbar-light bg-light border border-top-0 border-left-0 border-right-0 border-primary" border-width="thick">
  			<a class="navbar-brand" href="#">Calendar</a>
  			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
		    	<span class="navbar-toggler-icon"></span>
		 	</button>
		 	
		 	<div class="collapse navbar-collapse" id="navbarContent">
			    <ul class="navbar-nav mr-auto">
			    	<li class="nav-item active">
			        	<a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="#">Account</a>
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
			<span class="navbar-text text-muted m-2">Hello!</span>
			<a class="btn btn-outline-success" href="<%=(userService.createLoginURL(request.getRequestURI()))%>" role="button">Sign in</a>
			<%
	    }
	    else {
	    	username = "" + user.getNickname();
	      	pageContext.setAttribute("user", user);
			%>
			<span class="navbar-text text-muted m-2">Hello, ${fn:escapeXml(user.nickname)}! </span>
			<a class="btn btn-outline-danger" href="<%=(userService.createLogoutURL(request.getRequestURI()))%>">Sign Out</a>
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
		
		//load the events in displayMonth for a user into the display object
	    display.loadDisplayEvents();
				
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
		
	  
	  <!-- Calendar Container - contains the calendar, forms to change view, navs -->
	  <div class="container p-1" >	<!-- This div has 2 parts: header row (month name, forms) and the actual calendar -->
		
		<!-- Header/Navigation Row: Drop down menus to change month and year-->
		<div class="row m-1 align-middle"> 
			<!-- Month name/number -->
			<div class="col-md-auto">
				<h2><span class="text-primary"><%=(displayMonth + 1)%></span> <span class="text-secondary"><%=(smartcal.UserDisplayData.getMonthName(displayMonth))%></span></h2>
			</div>
			
			<!-- Month/Week/Day view tab navigation -->
			<div class="col-md-auto">
				<ul class="nav nav-tabs" id="calendarViewTab" role="tablist">
				  <li class="nav-item">
				    <a class="nav-link active" id="monthView-tab" data-toggle="tab" href="#monthView" role="tab" aria-controls="monthView" aria-selected="true">Month</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" id="weekView-tab" data-toggle="tab" href="#weekView" role="tab" aria-controls="weekView" aria-selected="false">Week</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" id="dayView-tab" data-toggle="tab" href="#dayView" role="tab" aria-controls="dayView" aria-selected="false">Day</a>
				  </li>
				</ul>
			</div>
			
			<!-- Change display month/year forms -->
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
			
			<!-- Today button form -->
			<div class="col-md-auto"><form action="/calendar" name="toToday" method="post">
				<button name="today" class="btn btn-lg btn-success text-light" type="submit">Today</button>
			</form></div>
		</div>
	    


			
	    <!-- Calendar View tables - Tab contents for month/week/day views -->
		<div class="row m-1">
	    	<div class="col-xl tab-content" id="calendarViewContent">
	    	
	    		<!-- Month View Tab -->
		  		<div class="tab-pane fade show active" id="monthView" role="tabpanel" aria-labelledby="monthView-tab">
		        	<table class="table table-bordered table-light table-responsive-md">
		          		<thead class="thead-dark w-100">
		            	<tr>
		            		<%for (int i = 0; i < 7; i++) {
								String dayName = smartcal.UserDisplayData.getDayName(i); %>
				            	<th scope="col" width="14%"><%=(dayName)%></th>
			              	<%}%>
		            	</tr>
		          		</thead>
			         	<tbody class="w-100">		<!-- Iterate through the table, place numbers in proper locations -->
			          	<% int day = 0;
			          	for (int week = 0; week < numWeeks; week++) { %>
			            	<tr>
			            	<% for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) { 
			            		if ((day == currentDate) && (displayMonth == currentMonth) && (displayYear == currentYear)) {
				            		//current day
				            		%><td width="14%" class ="table-success"><%
			            		}
			            		else { %><td width="14%"><% }
			            	
			            		if ((day < numDays) && !(week == 0 && dayOfWeek < firstDayOfWeek)) { %>
			            			<%day++;%>
			            			<%=(day)%>		<!-- This is where day number is printed, might need some styling-->
			            			<!-- contents of a calendar cell - add stuff about events here -->
			            			<!-- TODO -->
			            			<% ArrayList<smartcal.CalEvent> todaysEvents = display.getDisplayEvents(day); 
			            			
			            			//display preview of first event that day - eventually loop through and display all of them
			            			%><ul class="list-group"><%
			            			int numEventsToDisplay = 3;
			            			int numEvents = (todaysEvents == null)?0:todaysEvents.size();
			            			int e;
			            			for (e = 0; e < numEventsToDisplay; e++) {
			            				//invisible elements help keep sizing constant
			            				if (e >= numEvents) {
			            					%> <li class="list-group-item invisible"></li> <%
			            				}
			            				else if (numEvents > numEventsToDisplay && e == numEvents - 2) {
			            					//if there are more events than can be shown, indicate that there are more
			            					%> <li class="list-group-item">(more events)</li> <%
			            				}
			            				else {
			            					smartcal.CalEvent event = todaysEvents.get(e);
			            					%><li class="list-group-item"><%=(event.getTimeString() + " " + event.getName())%></li><%
			            				}
			            			}
			            			%></ul> <%
			            		} %>
			            	<%}%>
			            	</tr>
			      	  	<%} %>
			          	</tbody>
		        	</table>
		        </div>
		        
		        <!-- Week View Tab -->
      		  	<div class="tab-pane fade" id="weekView" role="tabpanel" aria-labelledby="weekView-tab">
      		  		<table class="table"><thead class="thead-dark w-100"><tr><th scope="col">This Week</th></tr></thead></table>
      		  		
      		  		<div class="card-columns">
						<%for (int i = 0; i < 7; i++) {
							String dayName = smartcal.UserDisplayData.getDayName(i);
							%>	
							<div class="card">
								<div class="card-header d-flex">
									<div><h5># <strong><%=(dayName)%></strong></h5></div>
							    	<div class="ml-auto"><button class="btn btn-sm btn-outline-secondary ml-auto" role="tab" id="<%=(dayName)%>Heading" type="button" data-toggle="collapse" data-target="#<%=(dayName)%>Events" aria-expanded="true" aria-controls="<%=(dayName)%>Events">...</button></div>
							    </div>
							    <div id="<%=(dayName)%>Events" class="collapse show" role="tabpanel" aria-labelledby="<%=(dayName)%>Heading">
							    	<div class="card-body">
										<p><%=(dayName)%> Event 1</p>
										<p><%=(dayName)%> Event 2</p>
							      	</div>
							    </div>
						  	</div>
						<%} %>
					</div>					  	
      		  	</div>
      		  	
      		  	<!-- Day View Tab -->
				<div class="tab-pane fade" id="dayView" role="tabpanel" aria-labelledby="dayView-tab">
					<table class="table table-bordered table-light table-responsive-md">
						<thead class="thead-dark">
							<tr><th colspan="2" scope="col">Day View</th></tr>
						</thead>
		            	<tbody class="w-100">		<!-- Iterate through the table, place numbers in proper locations -->
		            	<col width="130">
		            	<col width="30">
				          	<%int displayTime;%>
				          	<%String timeSuffix; %>
				          	<%int numHoursPerDay = 24;%>
				          	<%for (int hour = 0; hour < numHoursPerDay; hour++) { %>
				            	<tr style="text-align:center;height:10px"><td>
				            	<%if(hour == 0) {%>
				            		<%displayTime = 12; //unique behaviors of "0th" time instance and 12AM midnight%>
				            		<%timeSuffix = "AM"; %>
				            	<%}else if(hour > 12){%>				            		
				            		<%displayTime = hour - 12;%>
				            		<%timeSuffix = "PM"; %>
			            		<%}else{ %>
			            			<%displayTime = hour; %>
			            			<%timeSuffix = "AM"; %>
		            			<%} %>
			            		<p><%=displayTime%> <%=timeSuffix%></p></td>	
			            		<td><p>Event Name</p></td>	            		
				            	</tr>
				            	<%displayTime=0; %>
				      	  	<%} %>
			          	</tbody>
					</table>
				</div>
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
