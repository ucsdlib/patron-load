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
	if(typ.equals("emp_affiliations.properties"))
	{
		listName = "Employee Affiliations Properties";
		col1 = "Department Code";
		col2 = "Library Code";
	}
	else if(typ.equals("employee_types.properties"))
	{
		listName = "Employee Types Properties";
		col1 = "Staff";
		col2 = "Group";
	}
	else
	{
		listName = "Patron Load Properties";
		col1 = "Department Code";
		col2 = "Library Code";
	}
	String dLink = "downloadPropertiesServlet.do?fileName="+ typ;
%>
<font face="Verdana, Arial, sans-serif" size=2  color="#FFFFFF" align="center">
<center><b><%=listName%></b></center>

</br>
<a href=<%=dLink%> style="margin-left:326px;"> Download here</a>  
</font>
</br>
<table border="1" width="300" align="center">
<tr style="color:#FFFFFF; text-align:center">
<th><%=col1%></th>
<th><%=col2%></th>
<th>New Value</th>
<th>Action</th>
</tr>
<tr style="color:#FFFFFF;text-align:center"">
<form action="editProperties.jsp" method="get">
<td><input type="text" name="propName" style="width:40"/></td>
<td> </td>
<td><input type="text" name="newPropValue" style="width:60"/></td>
<input type="hidden" name="target" value="<%=request.getParameter("target")%>"/>
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
	<tr style="color:#FFFFFF;text-align:center">
	<td><%=propName%></td>
	<td><%=propValue%></td>
	<form action="editProperties.jsp" method="get">
	<td><input type="text" name="newPropValue" style="width:60"/></td>
	<input type="hidden" name="propName" value="<%=propName%>" />
	<input type="hidden" name="target" value="<%=request.getParameter("target")%>"/>
	<td><input type="submit" value="Edit"/></td> 
	</form>
	</tr>
	<%
	}
}
%>
</table>


</body>
</html>