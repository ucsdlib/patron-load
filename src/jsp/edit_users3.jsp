<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAuthorizeUsers()) {
		session.setAttribute("message", "to edit users");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verifypassword = request.getParameter("verifypassword");

		if (password.equals(verifypassword)) {
			
			int result = patronLoad.editUserFromRequest(request);
			patronLoad.loadUserPermissions(patronLoad.getCurrentUser());

			if (result == 0) {
				session.setAttribute("message", "User data has been changed!");
			}

		} else {
			// password do not match
			session.setAttribute("message", "Error: passwords do not match!");
		}

		%> <jsp:forward page="edit_users2.jsp" /> <%
	}
	
%>

