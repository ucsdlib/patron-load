<%@ page import="java.io.*, java.util.*, javax.naming.*, edu.ucsd.library.util.*"%>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>


<%
	// taken from index2.jsp
	String contextDir = request.getRealPath("") + File.separator;
	patronLoad.setContextDir(contextDir);
	String contextUrl = javax.servlet.http.HttpUtils.getRequestURL(request).toString();
	contextUrl = contextUrl.substring(0, contextUrl.lastIndexOf("/")+1);
	patronLoad.setContextUrl(contextUrl);
	
	String webinfDir = contextDir + "WEB-INF" + File.separator;
	
	InitialContext jndi = new InitialContext();
	
	String marcFilePath = application.getInitParameter("marcFilePath");
	
	//patronLoad.setPathToProperties(webinfDir + "patron_load.properties");
	
	patronLoad.setPathToProperties(marcFilePath + "patron_load.properties");
	
	String pathToProperties = patronLoad.getPathToProperties();
	
	//patronLoad.setMarcFilesDir(webinfDir + "marc_files" + File.separator);
	patronLoad.setMarcFilesDir(marcFilePath);
	patronLoad.setPathToACLFile(webinfDir + "acl" + File.separator + "users.acl");
	
	String contextURL = javax.servlet.http.HttpUtils.getRequestURL(request).toString();
	contextURL = contextURL.substring(0, contextURL.lastIndexOf('/')) + "/";
	
	//session.setAttribute("contextDIR", request.getRealPath("") + File.separator);
	//session.setAttribute("contextURL", contextURL);
%>



<%
    session.setAttribute("username", "user");
		
    patronLoad.setCurrentUser(userName);
    response.sendRedirect("jsp/validuser.jsp");		
%>
