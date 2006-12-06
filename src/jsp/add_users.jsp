<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAuthorizeUsers()) {
		session.setAttribute("message", "to add users");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {
%>

<html>

<body background="../images/background.gif" text="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF" LINK="#FFFFFF">

<br>

<center>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<font color="red">
<b>Add users</b>
</font>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<b><a href="edit_users.jsp">Edit users</a></b>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<b><a href="delete_users.jsp">Delete users</a></b>

<hr>
</font>
</center>

<br>

<%
	if (session.getAttribute("message") != null) {
		String msg = (String) session.getAttribute("message");
%>
		<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FF0000">
		<center>
		<%=msg%>
		</center>
		</font>
<%
		session.removeAttribute("message");
	}
%>



<form action="add_user2.jsp" method="post">

<table align="center">

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Username: &nbsp;&nbsp;&nbsp;&nbsp;</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="text" name="username" size="20">
</font>
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Password: &nbsp;&nbsp;&nbsp;&nbsp;</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="password" name="password" size="20">
</font>
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Verify password: &nbsp;&nbsp;&nbsp;&nbsp;</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="password" name="verifypassword" size="20">
</font>
</td>
</tr>

<tr>
<td>&nbsp;</td>
<td>&nbsp;</td>
</tr>

<tr>
<td colspan="2">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<center>
<b>Set permissions for user</b>
</center>
</font>
<hr>
</td>

</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Able to create a full file:</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="checkbox" name="createfullfile">
</font>
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Able to authorize/edit users:</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="checkbox" name="authorizedusers">
</font>
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Able to access incremental files:</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="checkbox" name="incrementalfiles">
</font>
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Able to access full database files:</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="checkbox" name="fulldatabasefiles">
</font>
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Able to change settings:</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="checkbox" name="changesettings">
</font>
</td>
</tr>

</table>

<br><br>
<center>
<input type="submit" value="add user">
<input type="reset" value="reset fields">
</center>

</form>

</body>
</html>

<%
	}  // close else
%>