<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="com.atosorigin.wfem.command.CommandDataModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	CommandDataModel model = template.getPageDataModel();
%>
<html>
<head>
<style>
body{
  margin: 0;
  padding: 0;
  border: 0;
  height: 100%;
  overflow: hidden;
}
.msg{
	font-family: Segoe UI;
	font-style: normal; 
	font-weight: bold; 
	text-decoration: none;		
	font-size: 12pt;
	color: #666666;
}
</style>
<%=template.getHeader()%>
</head>

<body>

<table width="100%" height="600px" style="background-color:white;">
	<tr>
		<td valign="top">
			<table width='100%'>
			<%	for(int ind=0;ind < model.getCommandErrors().size(); ind++){
					CommandError error = (CommandError)model.getCommandErrors().get(ind);
			%>
					<tr>
						<td align='center'>
							<table class='msg'>
								<tr><td><%=error.toString()%></td></tr>
							</table>
						</td>
					</tr>
			<%	}  %>
			</table>
		</td>
	</tr>
</table>

<%=template.getFooter()%>
</body>
</html>