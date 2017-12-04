<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
   <head><title>Fandango Test</title></head>
   
   <body>
      Fandango Output<br/>
      <%
      	smartcal.GracenoteMovieManager g = new smartcal.GracenoteMovieManager();
      	String date = "2017-12-02";
      	String zip = "78705";
		smartcal.MovieDatabase m = g.makeMoviesRequest(date, zip);
        out.println(m.toString());
      %>
   </body>
</html>