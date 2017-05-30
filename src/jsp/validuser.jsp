<%@ page errorPage="error_pages/error.jsp" %>

<%
	if (session.getAttribute("username") == null) {
		%> <jsp:forward page="error_pages/no_cookie_support.jsp"/> <%
	}
%>

<html>
<head>

<title>
UCSD Libraries Student Database Download Software
</title>

<script src="../html/scripts/detect_browser.js"></script>

<script language="Javascript">
<!--
	function resize(win) {
		if (is_ie4up) {
			win.resizeTo(500,400);
		} else if (is_nav4) { 
			win.resizeTo(500,250);
		}
	}

	function openWin(link, win) {
		return (window.open(link,'actionwin','width=750,height=600,resizable=no,toolbar=no,directories=no,menubar=no,location=no') );
	}
//-->
</script>

</head>

<body BGCOLOR="#333333" LINK="#FF9933" ALINK="#FF9933" VLINK="#FF9933" onLoad="resize(this.window);">

	<script language="JavaScript">
		var mywindow = openWin('patron_load.jsp', this.window);
	</script>

	<table>
	<tr>
	<td bgcolor="#6600FF" width="900" height="30" align="center">
	<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
	&nbsp;
	
	</font>
	</td>
	</tr>
	</table>
	
	<br><br>
	
	<center>
	<br>

	<form action="logout.jsp" method="post" name="form1">
	<input type="submit" name="logout" value="click here to logout" onClick="mywindow.close();">
	</form>
	
	
	<br><br><br>
	<font color="#FFFFFF" size="-1">
	please post your bug reports <a href="bug-report/bug_report.jsp" target="_new">here</a>
	</font>
	
	</center>

</body>
</html>