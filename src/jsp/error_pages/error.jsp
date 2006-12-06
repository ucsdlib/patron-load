<%@ page isErrorPage="true" %>

<html>
<head>
<title>
You have encountered an error!
</title>
</head>

<BODY BGCOLOR="#333333" ALINK="#FFFFFF" LINK="#FFFFFF" VLINK="#CCCCCC">

<center>
<h1>

<font face="Verdana, Arial, sans-serif" size=5 class="fontNormal" color="red">
!Error! !Error! !Error!
</font>
</h1>
</center>

<p>

<font color="white" face="Verdana, Arial, sans-serif" size=2 class="fontNormal">

<center>
You have reached this page because there was an internal error.<br>
<br>
Error code:<br>
<font color=\"#5FF756\">
<b>
<%=exception%>
</b>

<%
	if (exception.getClass().getName().equals("java.lang.NullPointerException")) {
		out.println("<br><br>");
		out.println("You may be receiving this error because your session has timed out. Please try to log back in.<br>");
	}
%>
</font>

<br><br>If problems persist, you may post a bug report <a href="/patronload/jsp/bug-report/bug_report.jsp" target="_new">here</a>

</center>

</BODY>

</html>