<%@page import="moke.menu2.model.PortalPageModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"></jsp:useBean>

<html>
<%
	PortalPageModel model = (PortalPageModel)template.getPageDataModel();
	String commandName = model.getWfemCommand().toString();
	String params = model.getPortalPageParams().toString();
	if (commandName.length() > 0 && params.length() > 0)
		commandName = commandName+"?"+params;
%>
<head>
<link rel='stylesheet' type='text/css' href='/wfemlayout/wlt/css/page.css'/>
<script>top.stopRequest();</script>
</head>

<body <%=(commandName.length() > 0 ? "onload='document.startForm.submit();'" : "")%>>
<form name="startForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="moke.menu2.business.AttivaFunzione.executeProcess">
<input type="hidden" name="commandType" value="COMMAND">
<input type="hidden" name="commandName" value="<%=commandName%>">
</form>

<% if(commandName.length() > 0){ %>
	<div style='position:absolute;top:35%;left:35%;z-index:1001;'>
			<div style='z-index:2001;position:relative;width:300;height:80;'>
				<table width='100%' height='100%' bgcolor='#F0F0F0' class='cornerAll' style='border:solid 1px #1A458F;'>
					<tr>
						<td align='center' valign='middle' style='font-family:Arial;font-size:11pt;color: #1A458F;'>
							<img src='/wfemlayout/wlt/images/wait.gif'/>&nbsp;&nbsp;Attendere prego...
						</td>
					</tr>
				</table>
			</div>
			<div class='cornerAll divwaitOpacityStyle' style='z-index:2000;position:absolute;left:10;top:10;width:300;height:80;'></div>
	</div>
<%}else{%>
	<div style='position:absolute;top:35%;left:35%;z-index:1001;'>
			<div style='z-index:2001;position:relative;width:300;height:80;'>
				<table width='100%' height='100%' bgcolor='#F0F0F0' class='cornerAll' style='border:solid 1px #1A458F;'>
					<tr>
						<td align='center' valign='middle' style='font-family:Arial;font-size:11pt;color: #1A458F;'>
							Wfem command for Portal Page [<%=model.getPortalPageName()%>] not found
						</td>
					</tr>
				</table>
			</div>
			<div class='cornerAll divwaitOpacityStyle' style='z-index:2000;position:absolute;left:10;top:10;width:300;height:80;'></div>
	</div>

<%}%>
</body>
</html>