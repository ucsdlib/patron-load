<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	String quartercode = request.getParameter("quartercode");
		
    //String expiration1  = request.getParameter("expiration1");
                
    //make the undergrads have the same expiration as grads
    //String expiration1  = request.getParameter("expiration2");
    
	String expiration1  = request.getParameter("expiration1");	
    String expiration2  = request.getParameter("expiration2");
               
    String employee1_exp = request.getParameter("employee1_exp");
    String employee2_exp = request.getParameter("employee2_exp");
    
		patronLoad.updateSettings(quartercode, expiration1, expiration2, 
			employee1_exp, employee2_exp);
		
		%> <jsp:forward page="change_settings.jsp"/>


