<%@ page errorPage="error_pages/error.jsp" %>

<HTML>
<HEAD>
<TITLE>menu</TITLE>

<script src="../html/scripts/detect_browser.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--

function logout() {

	if (is_nav4 || is_nav3) {
		alert("Use the logout button in the other window.");
	} else {

		if (parent.window.opener) {
			parent.window.opener.document.form1.submit(); parent.window.close();
		} else {
			alert("You have closed the parent window. Logging out cannot continue!");
		}
	}
}
// -->
</SCRIPT>

</HEAD>

<BODY background="../images/background.gif">

<br>

<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<center>
<b>Choose an action</b>
<hr>
</center>
</font>

<br>
<table>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='inc_daily_files.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="inc_daily_files.jsp" target="workarea">Incremental Files</a></b>
</font>
</td>
</tr>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='full_database_files.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="full_database_files.jsp" target="workarea">Full Patron Files</a></b>
</font>
</td>
</tr>

<tr>
<td>
&nbsp;
</td>
</tr>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='change_settings.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="change_settings.jsp" target="workarea">Change Settings</a></b>
</font>
</td>
</tr>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='create_full_file.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="create_full_file.jsp" target="workarea">Create a Full File</a></b>
</font>
</td>
</tr>

<tr>
<td>
&nbsp;
</td>
</tr>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='create_inc_employee.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="create_inc_employee.jsp" target="workarea">Incremental Emp.</a></b>
</font>
</td>
</tr>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='create_full_active_employee.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="create_full_active_employee.jsp" target="workarea">Full Active Emp.</a></b>
</font>
</td>
</tr>

<tr>
<td>
&nbsp;
</td>
</tr>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='change_password.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="change_password.jsp" target="workarea">Change Password</a></b>
</font>
</td>
</tr>

<tr>
<td bgcolor='#6092C3' onClick="parent.frames['workarea'].location='add_users.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="add_users.jsp" target="workarea">Authorized Users</a></b>
</font>
</td>
</tr>

<tr>
<td>
&nbsp;
</td>
</tr>

<tr>
<td bgcolor='#6092C3' ONCLICK="parent.frames['workarea'].location='changeProperties.jsp.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="changeProperties.jsp" target="workarea">Change Properties</a></b>
</font>
</td>
</tr>


<tr>
<td>
&nbsp;
</td>
</tr>

<tr>
<td bgcolor='#6092C3' ONCLICK="logout(); return true;" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="" ONCLICK="logout(); return false;">Log off</a></b>
</font>
</td>
</tr>

</table>

<br><br>
<font color="#FFFFFF" size="-1">
<center>
post your bug reports <a href="bug-report/bug_report.jsp" target="_new">here</a>
<br>
build date: @build_date@
</center>
</font>


</BODY>
</HTML>