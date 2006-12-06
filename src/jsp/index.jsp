<%@ page errorPage="error_pages/error.jsp" %>

<html>
<head>

<title>
UCSD Libraries Patron Database Download Software
</title>

<script src="scripts/detect_browser.js"></script>

</head>

<body BGCOLOR="#333333">

<script langauge="JavaScript">

	// test browser version
	if (!is_nav4up && !is_ie4up) {
		alert("This application has been tested to work with IE 4.0+ and NS 4.0+ only.\nYou are using an unknown browser which may result in unsatisfactory performance.");
	}

	// if JavaScript is enabled, to go real logon page
	window.location.replace("index2.jsp");
</script>

	<table>
	<tr>
	<td bgcolor="#FF0000" width="900" height="30" align="center">
	<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
	<b>Your browser does not support javascript. Javascript must be enabled to continue!</b>
	</font>
	</td>
	</tr>
	</table>
</body>

</html>
