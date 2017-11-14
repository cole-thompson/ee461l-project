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
  <title>Calendar</title>
   <!-- Required meta tags -->
   <meta charset="utf-8">
   <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

   <!-- Bootstrap CSS -->
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
 </head>

  <body>
	<%
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
	private String getMonthName(int m) {
		String name = "";
		switch (m) {
		case 0:
			name = "January";break;
		case 1:
			name = "February";break;
		case 2:
			name = "March";break;
		case 3:
			name = "April";break;
		case 4:
			name = "May";break;
		case 5:
			name = "June";break;
		case 6:
			name = "July";break;
		case 7:
			name = "August";break;
		case 8:
			name = "September";break;
		case 9:
			name = "October";break;
		case 10:
			name = "November";break;
		case 11:
			name = "December";break;
		}
		return name;
	}
	%>

	<%
	//create java calendar, get current date
	Calendar currentCal = new GregorianCalendar();
	int currentDate = currentCal.get(Calendar.DATE);
	int currentMonth = currentCal.get(Calendar.MONTH);
	int currentYear = currentCal.get(Calendar.YEAR);
	
	//get year and month from forms
	
	int displayYear, displayMonth, displayDate = 0;
	/* try {
    	displayYear = Integer.parseInt(request.getParameter("formYear"));
    	displayMonth = Integer.parseInt(request.getParameter("formMonth"));
    	displayDate = 1;
    } */
    //catch(Exception e) {
    	displayYear = currentYear;
    	displayMonth = currentMonth;
    	displayDate = currentDate;
    //}
    
	/*
	 * get info about the month
	 * numDays = number of days in the month
	 * firstDayOfWeek = day of week on the 1st of the month
	 * dayOffset = number of days between Sunday and firstDayOfWeek
	 * numWeeks = number of weeks in the month
	 */
	Calendar displayCal = new GregorianCalendar(displayYear, displayMonth, 1);
	int numDays = displayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
	int firstDayOfWeek = displayCal.get(Calendar.DAY_OF_WEEK);
	displayCal = new GregorianCalendar(displayYear, displayMonth, numDays);
	int numWeeks = displayCal.get(Calendar.WEEK_OF_MONTH);
	displayCal = new GregorianCalendar(displayYear, displayMonth, displayDate);
	
	
	%>
	
  <h1>Calendar</h1>

  <!-- http://getbootstrap.com/docs/4.0/content/tables/ -->
  <div class="container">
  
  	<!-- Print Tests for the Calendar Info -->
  	<!--  
  	<div class="row"> <div class="col-sm"> year: <%=(displayYear)%></div></div>
    <div class="row"> <div class="col-sm"> month: <%=(displayMonth)%></div></div>
    <div class="row"> <div class="col-sm"> date: <%=(displayDate)%></div></div>
    <div class="row"> <div class="col-sm"> numDays: <%=(numDays)%></div></div>
    <div class="row"> <div class="col-sm"> firstDayOfWeek: <%=(firstDayOfWeek)%></div></div>
    <div class="row"> <div class="col-sm"> numWeeks: <%=(numWeeks)%></div></div>
	-->
	
	<div class="row"> <div class="col-xl"> <h2><%=(getMonthName(displayMonth))%></h2> </div></div>
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
          <tbody>
          <% int day = 1;
          for (int week = 0; week < numWeeks; week++) { %>
            <tr>
            <% for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) { 
            	if (day == displayDate) {
            		%><td class ="table-primary"><%
            	}
            	else {
            		%><td><%
            	}
            	
            	if ((day <= numDays) && !(week == 0 && dayOfWeek < firstDayOfWeek)) { %>
            		<%=(day)%>
            		<%day++;
            	} %>
            	</td>
            	<%}%>
            </tr>
      	  <%} %>
            
          </tbody>
        </table>
      </div>
    </div>
  </div>

  </body>
</html>
