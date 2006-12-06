<%@ page import="java.io.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canCreateFullFile()) {
		session.setAttribute("message", "to create a full file");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {
%>

<html>

<body background="../images/background.gif" text="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF" LINK="#FFFFFF">


<script>
	input_box=confirm("Creating a FULL student file may take several minutes!\nAre you sure you want to continue?");

	if (input_box == true) {
	
		alert("Please wait patiently until the process is completed.\nNo further messages will be displayed until process is complete.");
		window.location="create_full_file2.jsp";
	} else {
		window.location="inc_daily_files.jsp";
	}
</script>

</body>
</html>

<%
	}  // close else
%>