<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>


<html>

<body background="../images/background.gif">

<br>

<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Change Password</b>
<hr>
</font>
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



<form action="change_password2.jsp" method="post">

<table align="center">

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Old password: &nbsp;&nbsp;&nbsp;&nbsp;</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="password" name="oldpassword" size="10">
</font>
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>New password: &nbsp;&nbsp;&nbsp;&nbsp;</b>
</font>
</td>
<td>
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<input type="password" name="newpassword" size="10">
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
<input type="password" name="verifypassword" size="10">
</font>
</td>
</tr>


</table>

<br><br>
<center>
<input type="submit" value="change password">
<input type="reset" value="reset fields">
</center>

</form>

</body>
</html>