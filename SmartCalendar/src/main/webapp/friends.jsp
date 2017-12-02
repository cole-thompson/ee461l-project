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
	 	<title>Friends and Events</title>
	 	<!-- Required meta tags -->
	 	<meta charset="utf-8">
	 	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
		<!-- Bootstrap CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	 	
	 	<!-- Our custom css -->
	 	<link rel="stylesheet" type="text/css" href="calendar.css">
	 	
	 </head>
	 
	 <body>

	 	<!-- Begin Navbar -->
		<nav class="navbar navbar-expand-lg p-1 navbar-light bg-light sticky-top border border-top-0 border-left-0 border-right-0 border-primary" border-width="thick">
  			<a class="navbar-brand" href="#">Smart Calendar</a>
  			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
		    	<span class="navbar-toggler-icon"></span>
		 	</button>
		 	
		 	<div class="collapse navbar-collapse" id="navbarContent">
			    <ul class="navbar-nav mr-auto">
			    	<li class="nav-item active">
			        	<a class="nav-link" href="calendar.jsp">Home <span class="sr-only">(current)</span></a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="#">Account</a>
			      	</li>
			      	<li class="nav-item">
			        	<a class="nav-link" href="#">Friends</a>
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
	 		</div>
 		</nav>
 		
 		
 		<!-- Tables -->
 		<table class="w-100"> <!-- Parent table -->
 		<tr>
 		<td class="w-50" style="top:0"> <!-- Start table 1 -->
 			<table class="table table-bordered table-light table-hover w-100">
	 			<thead class="thead-dark col-md-6">
						<tr><th>Friends</th></tr>
				</thead>
 			</table>
	 		<div style="max-height: 500px;	height:500px;	overflow-y:auto">
	 		<table class="table table-bordered table-light table-hover w-100">	
				
	           	
	           	<tbody style="top:0">		<!-- Iterate through the table, place numbers in proper locations -->
	           	
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
	          	
	          	</tbody>
	          	
			</table></div>
		</td>
		<td class="w-50" style="top:0"> <!-- Start table2 -->
			<table class="table table-bordered table-light table-responsive-md table-hover w-100">
	 			<thead class="thead-dark col-md-6">
						<tr><th>Invites</th></tr>
				</thead>
 			</table>
			<div style="max-height: 500px;	height:500px;	overflow-y:auto">
			<table class="table table-bordered table-light table-responsive-md table-hover w-100">
				<tbody style="top:0">		<!-- Iterate through the table, place numbers in proper locations -->
				
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					<tr><td><p>bloop</p></td></tr>
					
					
		          	
	          	</tbody>
	          	
	 		</table>
	 		</div>
 		</td>
 		</tr>
 		</table> <!-- End of the parent table -->
 		
 		
	 </body>