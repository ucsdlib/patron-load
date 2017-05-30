<%@ page import="java.io.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<html>

<body background="../images/background.gif" text="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF" LINK="#FFFFFF">

<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Download patron files - incremental database files</b>
</font>

<br><br>


<form action="deletefiles_inc.jsp" method="post">

<table>
<tr>

<td width="180" align="center">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>File name</b>
<hr>
</font>
</td>

<td width="180" align="center">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Last Modified</b>
<hr>
</font>
</td>

<td width="75" align="center">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Size</b>
<hr>
</font>
</td>

<td width="75" align="center">
<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
<b>Remove?</b>
<hr>
</font>
</td>
</tr>

<%= patronLoad.getMarcFileNames("inc") %>

</table>

<br>

<center>
<input type="submit" value="remove files">
</center>


</form>


</body>
</html>
