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
<td bgcolor='#6092C3' ONCLICK="parent.frames['workarea'].location='download_employees.jsp.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="download_employees.jsp" target="workarea">Download CSV Files</a></b>
</font>
</td>
</tr>

<tr>
<td bgcolor='#6092C3' ONCLICK="parent.frames['workarea'].location='create_all_employee.jsp.jsp'" onMouseOver="this.style.backgroundColor='#6161C2'; this.style.color='white'; this.style.cursor='hand';" onMouseOut="this.style.backgroundColor='#6092C3'; this.style.color='black'">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
<img src="../images/downarrow.gif">
<b><a href="create_all_employee.jsp" target="workarea">Create CSV File</a></b>
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


</BODY>
</HTML>