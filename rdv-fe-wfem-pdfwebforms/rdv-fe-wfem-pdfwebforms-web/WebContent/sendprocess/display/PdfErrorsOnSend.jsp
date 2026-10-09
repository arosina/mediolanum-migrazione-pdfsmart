<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="com.atosorigin.wfem.types.DoubleType"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();

 	String closeBrowserInstances = "";
	if(!model.getPdfData().getIsOnSameStack().booleanValue())
		closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");

	boolean showBack = true;
	if(model.getCommandErrors().size() > 0){
		String err0msg = model.getCommandErrors().get(0).toString();
		if(err0msg.indexOf("#NOBACK#") == 0){
			showBack=false;
			err0msg = err0msg.substring("#NOBACK#".length());
			model.getCommandErrors().set(0, new CommandError(err0msg));
		}
	}
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function doGoHome(){
	wait();
	document.goHomeForm.submit();
	return false;
}
function doBack(){
	<% if(!model.getPdfData().getGobackUrl().isNull()){ %>
		wait();
		location.href="<%=model.getPdfData().getGobackUrl()%>";
	<% } %>
	return false;
}
</script>
<style>
body{
	margin: 0;
	padding: 0;
	border: 0;
	height: 100%;
	overflow: hidden;
}
</style>
<%@ include file="../../display/PdfReady.html"%>
</head>

<body>

<form name="goHomeForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoBackOnError.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<table class="htmlContainer" style="height:auto;">
	<tr>
		<td valign="top" height="100%" style="padding: 30;">
			<table>
				<tr>
					<td class="titolo">Attenzione</td>
				</tr>
				<tr>
					<td>
						<table class="testo">
							<% for(int i=0;i<model.getCommandErrors().size();i++){ %>
								<tr><td><%=template.getProperty((CommandError)model.getCommandErrors().get(i))%></td></tr>
							<% } %>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td style="padding-top: 10;"><div class="line">&nbsp;</div></td></tr>
	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td style="padding:10;padding-top:3;">
						<table>
							<tr>
								<% if(model.getPdfData().getSkipDataentry().booleanValue()){ %>							
									<% if(!model.getPdfData().getGobackUrl().isNull()){ %>
										<% if(!model.getPdfData().getGobackLabel().isNull()){ %>
								    		<td><div class="normalButton whiteButton" onclick="doBack();"><%=model.getPdfData().getGobackLabel()%></div></td>
										<% }else{ %>
						    				<td><div class="normalButton whiteButton" onclick="doBack();">Indietro</div></td>
										<% } %>
							    	<% } %>
						    	<% }else if(showBack){ %>
									<td><div class="normalButton whiteButton" onclick="doGoHome();">Indietro</div></td>
						    	<% } %>
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</center>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<% if(model.getXmlSrvSend() != null && model.getXmlSrvReceived() != null){ %>
	<div id="xmlServizi" style="display:none;">
		<table>
			<tr>
				<td>
					<textarea id="xmlSrvSend">
						<%=model.getXmlSrvSend()%>
					</textarea>
				</td>
				<td>
					<textarea id="xmlSrvReceived">
						<%=model.getXmlSrvReceived()%>
					</textarea>
				</td>
			</tr>
		</table>
	</div>
	<%
	model.setXmlSrvSend(null);
	model.setXmlSrvReceived(null);
	%>
<% } %>

<%=template.getFooter()%>
</body>
</html>