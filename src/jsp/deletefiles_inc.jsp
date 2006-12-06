<%@ page import="java.io.*, java.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>

<%
	if (!patronLoad.canAccessFullFiles()) {
		session.setAttribute("message", "to create an incremental file");
		%> <jsp:forward page="not_permitted.jsp"/> <%
	} else {

		String webinfDir = patronLoad.getContextDir() + "WEB-INF" + File.separator;
		//String marcFilesDir =  webinfDir + File.separator + "marc_files" + File.separator;
		String marcFilesDir =  application.getInitParameter("marcFilePath");

		for (Enumeration en=request.getParameterNames(); en.hasMoreElements();) {
			    String name = (String)en.nextElement();
			    String value = request.getParameter(name);

			    name = name.substring(7, name.length());

			    File myF = new File( marcFilesDir + name);
			    if (value.equals("on")) myF.delete();
		}

		%> <jsp:forward page="inc_daily_files.jsp"/> <%
	
	}  // close else
%>