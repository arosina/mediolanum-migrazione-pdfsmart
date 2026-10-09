<%@page import="java.math.BigDecimal"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlFieldsDrawer"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
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

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function doBack(){
	<% if(!model.getPdfData().getGobackUrl().isNull()){ %>
		wait();
		location.href="<%=model.getPdfData().getGobackUrl()%>";
	<% } %>
	return false;
}
</script>

<%@ include file="./PdfReady.html"%>
</head>

<body>

<% if(!model.isTestMode() && !model.getPdfData().getIsOnSameStack().booleanValue()){ %>
	<iframe src='call.wfem?wfemCmd=closeThread&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>' style='display:none;'></iframe>
<% } %>

<center>
<table class="htmlContainer">
	<tr>
		<td align="center" class="msg" height="100%"><%=model.getInitialErrorMsg()%></td>
	</tr>
	
	<% if(!model.getPdfData().getGobackUrl().isNull()){ %>
		<tr><td style="padding-top: 10;"><div class="line">&nbsp;</div></td></tr>
		<tr>
			<td align="left" style="padding:10;padding-top:3;">
				<table>
					<tr>
						<% if(!model.getPdfData().getGobackLabel().isNull()){ %>
							<td><div class="normalButton" onclick="doBack();"><%=model.getPdfData().getGobackLabel()%></div></td>
						<% }else{ %>
							<td><div class="normalButton" onclick="doBack();">Indietro</div></td>
						<% } %>
					</tr>
				</table>
			</td>
	  	</tr>
  	<% } %>
</table>
</center>
</body>
</html>
