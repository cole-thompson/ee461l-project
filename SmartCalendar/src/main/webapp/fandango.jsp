<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
   <head><title>Fandango Test</title></head>
   
   <body>
      Fandango Output<br/>
      <%
      	String zip = "78705";
		String output = smartcal.FandangoManager.makeRequest(String.format("op=theatersbypostalcodesearch&postalcode=%s", zip));
        out.println(output);
      %>
   </body>
</html>