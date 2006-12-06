<%@ page import="java.io.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAuthorizeUsers()) {
		session.setAttribute("message", "to add users");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {
		int result = patronLoad.addUserFromRequest(request);

		if (result == 0) {
			session.setAttribute("message", "User has been added!");
		}

		if (result == 1) {
			session.setAttribute("message", "Error: passwords do not match!");
		}

		if (result == 2) {
			session.setAttribute("message", "Error: username already exists!");
		}

		%> <jsp:forward page="add_users.jsp" /> <%
	}
%>