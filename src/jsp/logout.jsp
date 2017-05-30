<%@ page errorPage="error_pages/error.jsp" %>

<%
	session.invalidate();
	response.sendRedirect("/patronload/login/login.jsp");
%>