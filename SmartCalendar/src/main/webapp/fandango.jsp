<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
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
        			<img src="NoMovieImage.png" alt="No Movie Image Found" width="500" height="377">
        		<% } else { 
        			out.println(m.getImgUrl());%>
        			<img src="<%=m.getImgUrl()%>" alt="<%=m.getTitle()%>" width="500" height="377">
        		<%}
        }
      %>
   </body>
</html>