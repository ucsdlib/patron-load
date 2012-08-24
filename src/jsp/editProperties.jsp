<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>
<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<html>
<head>
<%
	String key = request.getParameter("propName");
	String value = request.getParameter("newPropValue");
	String target = request.getParameter("target");
	if( request.getParameter("action").equals("delete"))
	{
		request.setAttribute("action","edit");
		patronLoad.delProperties(target, key); 
		request.setAttribute("message", "Key is deleted!");
	}
	else if( request.getParameter("action").equals("add"))
	{
		if(patronLoad.hasPropertiesKey(target,key))
		{
			request.setAttribute("action","keyInvalid");
			request.setAttribute("propName",key);
			request.setAttribute("newPropValue",value);
			request.setAttribute("target",target);
		}
		else
		{
			request.setAttribute("action","edit");
			patronLoad.setPropertiesFile(target, key, value); 
			request.setAttribute("message", "Key is added.");
		}
			
	}
	else
	{
		request.setAttribute("action","edit");
		patronLoad.setPropertiesFile(target, key, value); 
		request.setAttribute("message", "Key is added.");
		
	}
	
%>
<jsp:forward page="listProperties.jsp" />

</head>
<body>

</body>
</html>