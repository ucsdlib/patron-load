<html>

<head>

</head>

<BODY background="../images/background.gif">

<br>

<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FF0000">
<center>
<b>Sorry, but you are not allowed <%= (String)(session.getAttribute("message")) %></b>
</center>
</font>

</body>

</html>

<% session.removeAttribute("message"); %>