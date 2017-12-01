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
  	
  		<nav class="navbar navbar-expand-lg p-1 navbar-light bg-light sticky-top border border-top-0 border-left-0 border-right-0 border-primary" border-width="thick">
  			<a class="navbar-brand" href="#">Smart Calendar</a>
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
			      	<li class="nav-item">
			        	<a class="nav-link" href="#">New Event</a>
			      	</li>
			    </ul>

		<%
		//Google sign-in initialization 
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    String username = "";
	    if (user == null) {
			%>
			<span class="navbar-text text-muted mr-3">Hello!</span>
			<a class="btn btn-sm btn-outline-success" href="<%=(userService.createLoginURL(request.getRequestURI()))%>" role="button">Sign in</a>
			<%
	    }
	    else {
	    	username = "" + user.getNickname();
	      	pageContext.setAttribute("user", user);
			%>
			<span class="navbar-text text-muted mr-3">Hello, ${fn:escapeXml(user.nickname)}! </span>
			<a class="btn btn-sm btn-outline-danger" href="<%=(userService.createLogoutURL(request.getRequestURI()))%>">Sign Out</a>
			<%
	    }  
		%>	
		
		</div>	
		</nav>
	
		<%
		//main method initialization stuff
		ObjectifyService.register(smartcal.CalEvent.class);
        ObjectifyService.register(smartcal.UserDisplayData.class);
        ObjectifyService.register(smartcal.FriendsList.class);
				
		//grab user display information from objectify
       	smartcal.UserDisplayData display = ObjectifyService.ofy().load().type(smartcal.UserDisplayData.class).filter("user", user).first().now();
       	if (display == null) {
       		System.out.println(username + " found no displayData, creating new");
       		display = new smartcal.UserDisplayData(user);
       	}
       	else {
       		System.out.println(username + " found displayData");
       	}
       	
       	//grabbing FriendsList for the current user
       	smartcal.FriendsList flist = ObjectifyService.ofy().load().type(smartcal.FriendsList.class).filter("user", user).first().now();
       	if(flist == null){
       		System.out.println(user + " didnt have a friendslist for some reason, creating it now");
       		flist = new smartcal.FriendsList(user);
       		ObjectifyService.ofy().save().entity(flist);
       	}else{
       		System.out.println("friendslist found: \n" + flist);		// THIS STATEMENT IS TO DEBUG, PRINTS THE WHOLE FRIENDSLIST. CAN BE REMOVED
       	}
       	
       	
       	
     	 //load the events for a user into the display object
	    display.loadDisplayEvents();
       	
     	//View tab - Month(0), Week(1), or Day(2) view
     	int displayView = display.getDisplayView(); 
     			
		//create java calendar, get current day/month/year/week
		Calendar currentCal = new GregorianCalendar();
		int currentDate = currentCal.get(Calendar.DATE);
		int currentMonth = currentCal.get(Calendar.MONTH);
		int currentYear = currentCal.get(Calendar.YEAR);
		int currentWeek = currentCal.get(Calendar.YEAR);		 
     			 
		
		//check if display is set to auto today
	   	if (display.getDisplayMonth() == -1) {
	   		display.displayToday();
	   	}
		
				
		/*
		 * get info about the month
		 * numDays = number of days in the month
		 * firstDayOfWeek = day of week on the 1st of the month
		 * numWeeks = number of weeks in the month
		 */
		Calendar displayCal = new GregorianCalendar(display.getDisplayYear(), display.getDisplayMonth(), 1);
		int numDays = displayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int firstDayOfWeek = displayCal.get(Calendar.DAY_OF_WEEK);
		displayCal = new GregorianCalendar(display.getDisplayYear(), display.getDisplayMonth(), numDays);
		int numWeeks = displayCal.get(Calendar.WEEK_OF_MONTH);	
		displayCal = new GregorianCalendar(display.getDisplayYear(), display.getDisplayMonth(), display.getDisplayDate());

		%>
		
	  
	  <!-- Calendar Container - contains the calendar, forms to change view, navs -->
	  <div class="container p-1" >	<!-- This div has 2 parts: header row (month name, forms) and the actual calendar -->
		
		<!-- Header/Navigation Row: Drop down menus to change month and year-->
		<div class="row m-1 align-middle"> 
			<!-- Month name/number -->
			<div class="col-md-auto">
				<h2><span class="text-primary"><%=(display.getDisplayMonth() + 1)%></span> <span class="text-secondary"><%=(smartcal.UserDisplayData.getMonthName(display.getDisplayMonth()))%></span></h2>
			</div>
			
			<!-- Month/Week/Day view tab navigation -->
			<form action="/calendar" name="changeView" method="post">
			<div class="col-md-auto">
				<ul class="nav nav-tabs" id="calendarViewTab" role="tablist">
				  <li class="nav-item">
				    <button type="submit" class="btn btn-link nav-link <%=((displayView == 0)?"active":"")%>" name="monthView-btn" role="tab">Month</button>
				  </li>
				  <li class="nav-item">
				    <button type="submit" class="btn btn-link nav-link <%=((displayView == 1)?"active":"")%>" name="weekView-btn" role="tab">Week</button>
				  </li>
				  <li class="nav-item">
				    <button type="submit" class="btn btn-link nav-link <%=((displayView == 2)?"active":"")%>" name="dayView-btn" role="tab">Day</button>
				  </li>
				</ul>
			</div>
			</form>
			
			<!-- Change display month/year forms -->
			<div class="col-md" style="min-width: 30%;">
				<form action="/calendar" name="changeMonth" method="post">
				<div class="form-group form-group-lg">
					<div class="input-group input-group-lg"> 
					  	<select name="formMonth" onchange="changeMonth.submit()" class="form-control form-control-lg p-2">
		    				<% for (int m = 0; m < 12; m++) { 
		    					if (m == display.getDisplayMonth()) { %><option selected="selected" class="bg-primary text-light"> <% }
		    					else if (m == currentMonth && display.getDisplayYear() == currentYear) { %><option class="bg-success text-light"> <% }
		    					else { %><option><% } %>
		    					<%=(smartcal.UserDisplayData.getMonthName(m))%>
		    					</option>
		  					<%} %>
						</select>
						<span class="input-group-addon">/</span>
		    			<input name="formYear" onchange="changeMonth.submit()" class="form-control form-control-lg p-2" value="<%=(display.getDisplayYear())%>" type="number" min="1000" max="3000">
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
			<%if (displayView == 0)  {%>
		  		<div id="monthView" role="tabpanel">
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
			            		if ((day + 1 == currentDate) && (display.getDisplayMonth() == currentMonth) && (display.getDisplayYear() == currentYear)) {
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
			            			for (int e = 0; e < numEventsToDisplay; e++) {
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
			            					%><li class="list-group-item p-1"><small><%=(event.getTimeString() + " " + event.getName())%></small></li><%
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
		 	<% } else if (displayView == 1) { 
		 		int displayWeekFirstDay = display.getDisplayWeekStartDate();
		 		%>
      		  	<div id="weekView" role="tabpanel">
  
      		  		<form class="form-inline" action="/calendar" name="viewLeftRight" method="post">
      		  		<table class="table"><thead class="thead-dark"><tr class="d-flex w-100">
      		  			<th class="w-100">This Week</th>	  			
      		  			<!-- Left/Right arrows for navigation -->
      		  			<th width="10%" class="p-2 d-flex justify-content-end">
	    		  		<div class="btn-group btn-group-sm" role="group" aria-label="leftRightButtons">
							<button type="submit" class="btn btn-outline-light btn-sm" name="left">
						  		<span aria-hidden="true">&laquo;</span>
						        <span class="sr-only">Previous</span>
						  	</button>
						  	<button type="submit" class="btn btn-outline-light btn-sm" name="right">
						  		<span aria-hidden="true">&raquo;</span>
						        <span class="sr-only">Next</span>
						  	</button>
						</div>
						</th>
      		  		</tr></thead></table></form>
      		  		
      		  		<!-- Week View Cards -->
      		  		<div class="card-columns">
						<%for (int i = 0; i < 7; i++) {
							int nextDay = displayWeekFirstDay + i;
							if (nextDay >= 1 && nextDay <= numDays) {
								String dayName = smartcal.UserDisplayData.getDayName(i);
								boolean isToday = (nextDay == currentDate) && (display.getDisplayMonth() == currentMonth) && (display.getDisplayYear() == currentYear);
								%>
								<div class="card <%=(isToday?"border-success":"")%>">
									<div class="card-header d-flex">
										<div><h5 class="<%=(isToday?"text-success":"")%>"><%=(displayWeekFirstDay + i)%> <strong><%=(dayName)%></strong></h5></div>
								    	<div class="ml-auto"><button class="btn btn-sm btn-outline-secondary ml-auto" role="tab" id="<%=(dayName)%>Heading" type="button" data-toggle="collapse" data-target="#<%=(dayName)%>Events" aria-expanded="true" aria-controls="<%=(dayName)%>Events">...</button></div>
								    </div>
								    <div id="<%=(dayName)%>Events" class="collapse show" role="tabpanel" aria-labelledby="<%=(dayName)%>Heading">
								    	<div class="card-body">
											<% ArrayList<smartcal.CalEvent> todaysEvents = display.getDisplayEvents(nextDay); 
					            			%><ul class="list-group"><%
					            			int numEventsToDisplay = 10;
					            			int numEvents = (todaysEvents == null)?0:todaysEvents.size();
					            			for (int e = 0; e < numEventsToDisplay; e++) {
					            				if (e >= numEvents) {
					            					//currently not doing anything here, no reason for constant size
					            				}
					            				else if (numEvents > numEventsToDisplay && e == numEvents - 2) {
					            					//if there are more events than can be shown, indicate that there are more
					            					%> <li class="list-group-item p-2">(more events)</li> <%
					            				}
					            				else {
					            					smartcal.CalEvent event = todaysEvents.get(e);
					            					%><li class="list-group-item p-2"><%=(event.getTimeStringFull() + " " + event.getName())%></li><%
					            				}
					            			}
					            			%></ul>								
								      	</div>
								    </div>
							  	</div>
						<%} } %>
					</div>	
									  	
      		  	</div>
      		  	
      		<!-- Day View Tab -->  	
      		<% } else if (displayView == 2) { %>
				<div id="dayView" role="tabpanel">
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
				
			<%} %>
		
			</div>     
		</div>
		
	</div>
  
	<!-- Print Tests for the Calendar Info -->
  	<!--  
  	<div class="row"> <div class="col-sm"> current year: <%=(currentYear)%></div></div>
	-->	
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
	
	</body>
</html>
