<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*" %>
<%@ page errorPage="error_pages/error.jsp" %>
<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>
<html>
<head>
<script type="text/javascript">
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}
</script>
</head>

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
	if(request.getAttribute("action")!= null && ((String)request.getAttribute("action")).equals("keyInvalid"))
	{
		%>
		<script type="text/javascript">
			var r = confirm("Key entered is already existed in the Properties File. \n\t     Do you want to replace the old value?");
			var target = getUrlVars()["target"];
			var value = getUrlVars()["newPropValue"];
			var key = getUrlVars()["propName"];
			var npass = "listProperties.jsp?target=" + target;
			var pass = "editProperties.jsp?target=" + target + "&propName=" + key + "&newPropValue=" + value + "&action=edit";
			if(r==false)
				window.location.href = npass;
			else
				window.location.href = pass;
		</script>
		
		
		<%
		
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
<input type="hidden" value="add" name="action">
<td><input type="submit" value="Add"/></td>
</form>
</tr>
<%
Map map = patronLoad.getPropertiesSet(request.getParameter("target"));
Iterator it = map.entrySet().iterator();
String propName;
while(it.hasNext())
{
	Map.Entry pairs = (Map.Entry)it.next();
	//propName = entry.getKey();
	//String propValue = pairs.getValue();
	%>
	<tr style="color:#FFFFFF">
	<td><%=pairs.getKey()%></td>
	<td><%=pairs.getValue()%></td>
	<form action="editProperties.jsp" method="get">
	<td><input type="text" name="newPropValue" style="width:60"/></td>
	<input type="hidden" name="propName" value="<%=pairs.getKey()%>" />
	<input type="hidden" name="target" value="<%=request.getParameter("target")%>"/>
	<input type="hidden" value="edit" name="action">
	<td><input type="submit" value="Update"/></td> 
	</form>
	<form action="editProperties.jsp" method="get">
	<input type="hidden" name="propName" value="<%=pairs.getKey()%>" />
	<input type="hidden" name="target" value="<%=request.getParameter("target")%>"/>
	<input type="hidden" value="delete" name="action">
	<td><input type="submit" value="Delete"/></td> 
	</form>
	</tr>
	<%
	
}
%>
</table>
</div>

</body>
</html>