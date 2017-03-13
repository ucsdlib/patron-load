<%@ page import="java.io.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAuthorizeUsers()) {
		session.setAttribute("message", "to create all employee file");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {
%>

<html>

<body background="../images/background.gif" text="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF" LINK="#FFFFFF">

<br>
<h2>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Employee CSV file created!!</b>
</font>
</h2>

<br><br>

<%
	patronLoad.doAllEmployee();
%>

</body>
</html>

<%
	}  // close else
%>