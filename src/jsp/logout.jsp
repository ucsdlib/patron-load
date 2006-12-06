<%@ page errorPage="error_pages/error.jsp" %>

<%
	session.invalidate();
	response.sendRedirect("../index.jsp");
%>