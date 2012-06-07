<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>
<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<html>
<head>
<%
	String key = request.getParameter("propName");
	String value = request.getParameter("newPropValue");
	String target = request.getParameter("target");
	int output = patronLoad.setPropertiesFile(target, key, value); 
	request.setAttribute("action","edit");
	if(output == 1)
		request.setAttribute("message", "Properties Value successfully changed!");
 	if(output == 0)
		request.setAttribute("message", "New Properties was added");
	
%>
<jsp:forward page="listProperties.jsp" />

</head>
<body>

</body>
</html>