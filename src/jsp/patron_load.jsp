<%@ page errorPage="error_pages/error.jsp" %>

<html>
<head>
<title>
UCSD Libraries Student Database Download Software
</title>

<script language="Javascript">
	//window.opener.close();
</script>

<FRAMESET ROWS="120, *">
	<FRAME SRC="../html/header.html" NAME="header" scrolling="NO" frameborder="0" NORESIZE>

	<FRAMESET COLS="200, 600">
		<FRAME SRC="menu.jsp" NAME="menu" SCROLLING="NO" frameborder="0" NORESIZE>
		<FRAME SRC="inc_daily_files.jsp" NAME="workarea" SCROLLING="YES" frameborder="0" NORESIZE>
	</FRAMESET>

<NOFRAMES>
  <BODY>
    This text will appear only if the browser does not support frames.
  </BODY>
</NOFRAMES>

</FRAMESET>

</head>
</html>