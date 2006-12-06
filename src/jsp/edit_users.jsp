<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAuthorizeUsers()) {
		session.setAttribute("message", "to edit users!");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {
%>

<html>

<body background="../images/background.gif" text="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF" LINK="#FFFFFF">

<br>

<center>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b><a href="add_users.jsp">Add users</a></b>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<font color="red">
<b>Edit users</b>
</font>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<b><a href="delete_users.jsp">Delete users</a></b>

<hr>
</font>
</center>

<br><br>

<form action="edit_users2.jsp" method="post">

<center>
<b>User to edit:</b> &nbsp;&nbsp;&nbsp;

<%= patronLoad.getUserSelectBox() %>

<br><br>
<input type="submit" value="edit user">

</center>
</form>

</body>
</html>

<%
	}  // close else
%>
