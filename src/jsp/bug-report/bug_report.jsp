<html>
<head>
<title>
Patron Load: Bug Report
</title>
</head>

<body BGCOLOR="#333333" TEXT="#FFFFFF" LINK="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF">

<center>

<table>
<tr>
<td bgcolor="#6600FF" width="900" height="30" align="center">
<font face="Verdana, Arial, sans-serif" size=3 class="fontNormal" color="#FFFFFF">
<b>
Patronload Bug Report Submission
</b>
</font>
</td>
</tr>
</table>

<form action="http://rohan.ucsd.edu/formmailer/FormMailer" method="post" name="form1">
<input type="hidden" name="xslfile_email" value="patronload_bug_report.xsl">
<input type="hidden" name="redirect" value="../../patronload/html/ty.html">
<input type="hidden" name="settingsfile" value="patronload_bug_report.xml">

<table border="1">

<tr>
<td bgcolor="#6600FF">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
Name
</font>
</td>
<td>
<input type="text" name="name" size="60">
</td>
</tr>

<tr>
<td bgcolor="#6600FF">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
Email address
</font>
</td>
<td>
<input type="text" name="email" size="60">
</td>
</tr>

<tr>
<td bgcolor="#6600FF">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
Error code/msg
</font>
</td>
<td>
<input type="text" name="errorcode" size="60">
</td>
</tr>

<tr>
<td bgcolor="#6600FF">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
Browser
</font>
</td>
<td>
<select name="browser">
<option value="Internet_Explorer">Internet Explorer
<option value="Netscape">Netscape
<option value="Other">Other

</select>
&nbsp;&nbsp;
version: <input type="text" name="browser_version" size="5">
&nbsp;&nbsp;
OS/version: <input type="text" name="platform" size="20">
</td>
</tr>

<tr>
<td bgcolor="#6600FF">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
Problem description
</font>
</td>
<td>
<textarea rows="10" cols="60" name="problem_description" wrap="hard">
Please give detailed steps to reproduce the problem.
</textarea>
</td>
</tr>

<tr>
<td bgcolor="#6600FF">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal">
Priority
</font>
</td>
<td>
<center>
<input type="radio" name="priority" value="high">high
<input type="radio" name="priority" value="med">medium
<input type="radio" name="priority" value="low">low
</center>
</td>

</tr>

</table>

<br>
<input type="submit" name="submit" value="submit form">
<input type="reset" value="reset form">

</form>

</center>

</body>
</html>
