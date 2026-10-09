<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="com.atosorigin.wfem.command.CommandWarning"%>
<%@page import="com.atosorigin.wfem.types.DoubleType"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function onLoadBodyFnc(){
	trapPageFocus();
	document.getElementById('main-container').focus();
}
function doBack(){
	wait();
	document.goBackForm.submit();
	return false;
}
function doProsegui(){
	wait();
	document.goOnForm.submit();
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
#main-container:focus {
    outline: none;
}
</style>
<%@ include file="./PdfReady.html"%>
</head>

<body onload="onLoadBodyFnc();">

<form name="goBackForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoBackOnError.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="goOnForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="<%=model.getSkipWarningCommandName()%>.execute">
<input type="hidden" name="skipCommandWarnings" value="true">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<div class="htmlContainer" style="width:auto;padding:20;text-align:left;">
	<div tabindex="-1" id="main-container">
		<div class="titolo" style="padding:10px;">Attenzione</div>
		<div class="testo" style="padding-bottom:20px;padding-left:10px;">
			<% if(model.hasCommandErrors()){ %>
				<% for(int i=0;i<model.getCommandErrors().size();i++){ %>
					<%=template.getProperty((CommandError)model.getCommandErrors().get(i))%><br>
				<% } %>
			<% }else if(model.hasCommandWarnings()){ %>
				<% for(int i=0;i<model.getCommandWarnings().size();i++){ %>
					<%=template.getProperty((CommandWarning)model.getCommandWarnings().get(i))%></br>
				<% } %>
			<% } %>
		</div>
		<div class="line">&nbsp;</div>
		<div>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td style="padding:10;padding-top:3;">
						<table>
							<tr>
								<% if(!model.hasCommandErrors() && model.hasCommandWarnings()){ %>
									<td><div class="normalButton whiteButton" onclick="doBack();">Annulla</div></td>
								<% }else{ %>
									<td><div class="normalButton whiteButton" onclick="doBack();">Indietro</div></td>
								<% } %>
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
						<% if(!model.hasCommandErrors() && model.hasCommandWarnings() && model.getSkipWarningCommandName() != null){ %>
							<table>
								<tr>
									<td><div class="normalButton" onclick="doProsegui();">Prosegui</div></td>
								</tr>
							</table>
						<% } %>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
</center>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>
<% model.setSkipWarningCommandName(null); %>