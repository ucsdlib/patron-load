<%@ page import="java.io.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<html>

<body background="../images/background.gif" text="#FFFFFF" VLINK="#FFFFFF" ALINK="#FFFFFF" LINK="#FFFFFF">


<script>
	input_box=confirm("Creating a FULL employee file may take several minutes!\nAre you sure you want to continue?");

	if (input_box == true) {
	
		alert("Please wait patiently until the process is completed.\nNo further messages will be displayed until process is complete.");
		window.location="create_full_employee2.jsp";
	} else {
		window.location="full_database_files.jsp";
	}
</script>

</body>
</html>