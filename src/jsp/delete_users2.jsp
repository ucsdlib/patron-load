<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAuthorizeUsers()) {
		session.setAttribute("message", "to delete users!");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {
		String username = request.getParameter("username");
		patronLoad.removeUser(username);
		%> <jsp:forward page="delete_users.jsp" /> <%
	}
%>

