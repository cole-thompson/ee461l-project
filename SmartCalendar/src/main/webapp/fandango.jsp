<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
   <head><title>Fandango Test</title></head>
   
   <body>
      Fandango Output<br/>
      <%
      	String date = "2017-12-06";
    		String zip = "78705";
      	smartcal.MovieDatabase g = new smartcal.MovieDatabase(date, zip);
        for(smartcal.Movie m : g.getMovies()) {
        		if(m.getImgUrl() == null) {
        			%>
        			<img src="NoMovieImage.png">
        		<% } else {
        			
        		}
        }
      %>
   </body>
</html>