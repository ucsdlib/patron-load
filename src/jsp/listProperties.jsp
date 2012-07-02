<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>
<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>
<html>
<head>
</head>
<body>
<body background="../images/background.gif">
<%
	if(request.getAttribute("action")!= null && ((String)request.getAttribute("action")).equals("edit"))
	{
		String msg = (String)request.getAttribute("message");
		if(msg != null)
		{
			%>
			<font color="RED"> <%=msg%> </font>
			<% 
		}
	}
	String typ = request.getParameter("target");
	String link = "../WEB-INF/pub/data1/import/htdocs/patronload/" + typ;
	String listName = "";
	String col1 = "";
	String col2 = "";
	String col3 = "";
	if(typ.equals("emp_affiliations.properties"))
	{
		listName = "Employee Affiliations Properties";
		col1 = "Department Code";
		col2 = "Library Code";
		col3 = "New Library Code";
	}
	else if(typ.equals("employee_types.properties"))
	{
		listName = "Employee Types Properties";
		col1 = "Staff";
		col2 = "Group";
		col3 = "New Group";
	}
	else
	{
		listName = "Patron Load Properties";
		col1 = "Department Code";
		col2 = "Library Code";
		col3 = "New Library Code";
	}
	String dLink = "downloadPropertiesServlet.do?fileName="+ typ;
%>
<div id="main" style="width:400;margin:auto;position:relative;text-align:center;">
<font face="Verdana, Arial, sans-serif" size=2  color="#FFFFFF">
<b><%=listName%></b>
</br>
<% if(!listName.equals("Patron Load Properties")) {%>
<a href=<%=dLink%> style="position:absolute;right:0px"> Download here</a>  
<%} %>
</font>
</br>
<table border="1" style="width:400;margin:auto;text-align:center;">
<tr style="color:#FFFFFF"">
<th><%=col1%></th>
<th><%=col2%></th>
<th><%=col3%></th>
<th>Action</th>
<th>Action2</th>
</tr>
<tr style="color:#FFFFFF">
<form action="editProperties.jsp" method="get">
<td><input type="text" name="propName" style="width:40"/></td>
<td> </td>
<td><input type="text" name="newPropValue" style="width:60"/></td>
<input type="hidden" name="target" value="<%=request.getParameter("target")%>"/>
<input type="hidden" value="edit" name="action">
<td><input type="submit" value="Add"/></td>
</form>
</tr>
<%

Map<String, String> map = patronLoad.getPropertiesSet(request.getParameter("target")); 
for (Map.Entry<String, String> entry : map.entrySet())
{
	String propName = entry.getKey();
	String propValue = entry.getValue();
	if(propName.length() == 4)
	{
	%>
	<tr style="color:#FFFFFF">
	<td><%=propName%></td>
	<td><%=propValue%></td>
	<form action="editProperties.jsp" method="get">
	<td><input type="text" name="newPropValue" style="width:60"/></td>
	<input type="hidden" name="propName" value="<%=propName%>" />
	<input type="hidden" name="target" value="<%=request.getParameter("target")%>"/>
	<input type="hidden" value="edit" name="action">
	<td><input type="submit" value="Update"/></td> 
	</form>
	<form action="editProperties.jsp" method="get">
	<input type="hidden" name="propName" value="<%=propName%>" />
	<input type="hidden" name="target" value="<%=request.getParameter("target")%>"/>
	<input type="hidden" value="delete" name="action">
	<td><input type="submit" value="Delete"/></td> 
	</form>
	</tr>
	<%
	}
}
%>
</table>
</div>

</body>
</html>