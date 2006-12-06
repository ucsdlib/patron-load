<%@ page errorPage="error.jsp" %>

<html>
<head>

<title>
UCSD Libraries Student Database Download Software
</title>

<script language="Javascript">

	function resize(win) {
		win.resizeTo(500,400);
	}

	function openWin(link, win) {
		return (window.open(link,'actionwin','width=750,height=600,resizable=no,toolbar=no,directories=no,menubar=no,location=no') );
	}

</script>
</head>

<body BGCOLOR="#333333" onLoad="resize(this.window);">

	<table>
	<tr>
	<td bgcolor="#FF0000" width="900" height="30" align="center">
	<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
	<b>Your browser does not accept cookies!</b>
	</font>
	</td>
	</tr>
	</table>
	
	<center>
	<form>
	<input type="button" value="Try Again" onClick="window.location='/patronload/index.jsp';">
	<input type="button" value="Close Window" onClick="window.close();">
	</form>
	</center>
	
</body>
</html>