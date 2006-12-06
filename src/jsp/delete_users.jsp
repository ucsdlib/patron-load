<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAuthorizeUsers()) {
		session.setAttribute("message", "to delete users!");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {
%>


<html>

<head>

<script language="JavaScript">
	function confirm_delete() {
		result = confirm("Are you sure you want to delete this user?");
		
		if (result) {
			document.forms['delete_form'].submit();
		} else {
			window.location='delete_users.jsp';
		}
	}
</script>


</head>

<body background="../images/background.gif" text="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF" LINK="#FFFFFF">

<br>

<center>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b><a href="add_users.jsp">Add users</a></b>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<b><a href="edit_users.jsp">Edit users</a></b>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<font color="red">
<b>Delete users</b>
</font>

<hr>
</font>
</center>

<br><br>


<form action="delete_users2.jsp" method="post" name="delete_form">

<center>
<b>User to delete:</b> &nbsp;&nbsp;&nbsp;

<%= patronLoad.getUserSelectBox() %>

<br><br>
<input type="button" value="delete user" onClick="javascript:confirm_delete('Are you sure?');">

</center>
</form>

</body>
</html>

<%
	}  // close else
%>