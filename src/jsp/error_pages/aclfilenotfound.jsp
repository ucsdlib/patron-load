<%@ page errorPage="error.jsp" %>

<html>
<head>

<title>
UCSD Libraries Student Database Download Software
</title>

<script src="../../html/scripts/detect_browser.js"></script>

<script language="Javascript">
<!--
	function resize(win) {
		if (is_ie4up) {
			win.resizeTo(500,400);
		} else if (is_nav4) {
			win.resizeTo(500,250);
		} else if (is_nav6up) {
			win.resizeTo(500,400);
		}
	}

	function openWin(link, win) {
		return (window.open(link,'actionwin','width=750,height=600,resizable=no,toolbar=no,directories=no,menubar=no,location=no') );
	}
//-->
</script>

</head>

<body BGCOLOR="#333333" onLoad="resize(this.window);">

	<table>
	<tr>
	<td bgcolor="#FF0000" width="900" height="30" align="center">
	<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
	<b>ACL file "users.acl" not found!</b>
	</font>
	</td>
	</tr>
	</table>
	
	<center>
	<form>
	<input type="button" value="Try Again" onClick="window.location='/newsmanager/index.jsp';">
	<input type="button" value="Close Window" onClick="window.close();">
	</form>
	</center>
	
</body>
</html>