<%@ page errorPage="error_pages/error.jsp" %>

<html>
<head>

<title>
UCSD Libraries Patron Database Download Software
</title>

<script src="../html/scripts/detect_browser.js"></script>
<script type="text/javascript" src="https://lib-jira.ucsd.edu:8443/s/570923a7ebac454f838812ae4e331542-T/en_USfs3qxk/64016/6/1.4.25/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?locale=en-US&collectorId=b147091c"></script>

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

	function openWin(link) {
		return (window.open(link,'actionwin','width=750,height=600,resizable=no,toolbar=no,directories=no,menubar=no,location=no') );
	}
//-->
</script>

</head>

<body BGCOLOR="#333333" onLoad="resize(this.window);">

<table>
<tr>
<td bgcolor="#6600FF" width="900" height="30" align="center">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Please logon to the Patron Database Download Software</b>
</font>
</td>
</tr>

<tr>
<td>

<form action="authenticate.jsp" method="post">
<br>

<table align="center">
<tr>
<td>
<font face="Verdana, Arial, sans-serif" size="1" class="fontNormal" color="#FFFFFF">
<b>User ID:</b>
</font>
</td>
<td>
<input type="text" name="login">
</td>
</tr>

<tr>
<td>
<font face="Verdana, Arial, sans-serif" size="1" class="fontNormal" color="#FFFFFF">
<b>Password:</b>
</font>
</td>
<td>
<input type="password" name="password">
</td>
</tr>

</table>
<br>
<center>

<input type="submit" value="Login"/>
<input type="reset" value="Clear">
<!--
<input type="button" value="Help" onClick="openWin('link');"/>
-->

<br/>
<br/>
<br/>
<font face="Verdana, Arial, sans-serif" size="1" class="fontNormal" color="#FFFFFF">
build date: @build_date@ @build_time@ <br/>
deployed by: @user_name@
</font>

</center>
</form>
</td>
</tr>
</table>

</body>
</html>