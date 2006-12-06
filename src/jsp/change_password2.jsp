<%@ page import="java.io.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	String oldpassword = request.getParameter("oldpassword");
	String newpassword = request.getParameter("newpassword");
	String verifypassword = request.getParameter("verifypassword");

	int result = patronLoad.changePassword(oldpassword, newpassword, verifypassword);
			
	if (result == 0) {
		session.setAttribute("message", "Password successfully changed!");
	}

	if (result == 1) {
		session.setAttribute("message", "Error: passwords do not match!");
	}

	if (result == 2) {
		session.setAttribute("message", "Error: incorrect password entered!");
	}
%>

<jsp:forward page="change_password.jsp" />