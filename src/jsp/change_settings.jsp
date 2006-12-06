<%@ page import="java.io.*, java.util.*, edu.ucsd.library.util.*, java.text.*" %>
<%@ page errorPage="error_pages/error.jsp" %>

<jsp:useBean id="patronLoad" class="edu.ucsd.library.patronload.beans.patronload_bean" scope="session"/>


<%
	if (!patronLoad.canChangeSettings()) {
		session.setAttribute("message", "to change settings");
		%> <jsp:forward page="not_permitted.jsp"/> <%
} else {
%>


<html>
	
	<body background="../images/background.gif">
		
		<br>
		
		<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
			<b>Change Settings</b>
			<hr>
		</font>
		<br>
		
		<form action="change_settings2.jsp" method="post">
			
			<table align="center">
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>CURRENT academic quarter code: &nbsp;&nbsp;&nbsp;&nbsp;</b>
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<%= patronLoad.getQuarterCode() + " - " + patronLoad.getQuarterCodeLetter(patronLoad.getQuarterCode())%>
						</font>
					</td>
				</tr>
				
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>CURRENT student expiration date:</b>
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<%= patronLoad.getExpiredate_graduate() %>
						</font>
					</td>
				</tr>
				
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>Current Employee1 expiration date:</b><br/>
							(Faculty, Post Docs, and Library Staff)
							
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<%= patronLoad.getExpiredate_employee1() %>
						</font>
					</td>
				</tr>
				
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>Current Employee2 expiration date:</b><br/>
							(Regular Staff)
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<%= patronLoad.getExpiredate_employee2() %>
						</font>
					</td>
				</tr>
				
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>NEW academic quarter code:</b>
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							
							<select name="quartercode">
								<%
								    Calendar calendar = Calendar.getInstance();
								    Format formatter;
								    formatter = new SimpleDateFormat("yyyy");
								    String dateString = formatter.format(new java.util.Date());
								    int year = Integer.parseInt(dateString);
										
								    for (int j=year-1; j < year+2; j++) {
								        for (int i=0; i < 4; i++) {
								            String quarter = "";
														
								            if (i==0) quarter = "FA";
								            if (i==1) quarter = "WI";
								            if (i==2) quarter = "SP";
								            if (i==3) quarter = "SU";

								            String jj = (new Integer(j)).toString();
								            jj = jj.substring(2, 4);
								            int tmp = Integer.parseInt(jj);
								            tmp++;
								            if (tmp < 10) {
								                jj = "0" + tmp;
								            } else {
								                jj = "" + tmp;
								            }
								            quarter = quarter + jj;

								            if (patronLoad.getQuarterCode().trim().equals(quarter)) {
								                out.println("<option selected value=\"" + quarter + "\">" + quarter + " - " + patronLoad.getQuarterCodeLetter(quarter));
								            } else {
								                out.println("<option value=\"" + quarter + "\">" + quarter + " - " + patronLoad.getQuarterCodeLetter(quarter));
								            }
								        }
								    }
								%>
							</select>
							
						</font>
					</td>
				</tr>
				
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>NEW student expiration date:</b>
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							
							<select name="expiration2">
								<%
								    for (int j=year; j < year+3; j++) {
								        for (int i=0; i < 3; i++) {
								            String jj = (new Integer(j)).toString();
								            jj = jj.substring(2, 4);
								            String exp_grad = "";
								            if (i==0) exp_grad = "03-31-" + jj;
								            if (i==1) exp_grad = "07-31-" + jj;
								            if (i==2) exp_grad = "12-31-" + jj;
								            if (patronLoad.getExpiredate_graduate().trim().equals(exp_grad)) {
								                out.println("<option selected value=\"" + exp_grad + "\">" + exp_grad);
								            } else {
								                out.println("<option value=\"" + exp_grad + "\">" + exp_grad);
								            }
								        }
								    }
								%>
							</select>
						</font>
					</td>
				</tr>
				
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>NEW employee1 expiration date:</b>
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<select name="employee1_exp">
								<%
								    for (int j=year; j < year+10; j++) {
								            String jj = Integer.toString(j);
								            jj = jj.substring(2, 4);
								            String exp_emp1 = "12-31-" + jj;
								            if (patronLoad.getExpiredate_employee1().trim().equals(exp_emp1)) {
								                out.println("<option selected value=\"" + exp_emp1 + "\">" + exp_emp1);
								            } else {
								                out.println("<option value=\"" + exp_emp1 + "\">" + exp_emp1);
								            }
								    }
								%>
							</select>
						</font>
					</td>
				</tr>
				
				<tr>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<b>NEW employee2 expiration date:</b>
						</font>
					</td>
					<td>
						<font face="Verdana, Arial, sans-serif" size=2 class="fontNormal" color="#FFFFFF">
							<select name="employee2_exp">
								<%
								    for (int j=year; j < year+10; j++) {
								            String jj = Integer.toString(j);
								            jj = jj.substring(2, 4);
								            String exp_emp2 = "12-31-" + jj;
								            if (patronLoad.getExpiredate_employee2().trim().equals(exp_emp2)) {
								                out.println("<option selected value=\"" + exp_emp2 + "\">" + exp_emp2);
								            } else {
								                out.println("<option value=\"" + exp_emp2 + "\">" + exp_emp2);
								            }
								    }
								%>
							</select>
						</font>
					</td>
				</tr>
				
				
				
			</table>
			
			<br><br>
			<center>
				<input type="submit" value="save changes">
			</center>
			
		</form>
		
	</body>
</html>

<%
	}  // close else
%>