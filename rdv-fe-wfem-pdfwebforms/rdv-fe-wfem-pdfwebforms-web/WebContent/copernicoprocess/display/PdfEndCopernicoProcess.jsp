<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="com.atosorigin.wfem.types.IntegerType"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="com.atosorigin.wfem.types.AbstractType"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	PdfPersonModel firstFilledPerson =  model.firstFilledPerson();
	String codCliente = "";
	if(firstFilledPerson != null && !firstFilledPerson.getNdg().isNull())
		codCliente = "&codCliente="+firstFilledPerson.getNdg();
	
 	String closeBrowserInstances = "";
	if(!model.getPdfData().getIsOnSameStack().booleanValue())
		closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");

	model.setScrollXValue(new IntegerType());
	model.setScrollYValue(new IntegerType());
	
	boolean showStampa = false;
	if(model.getPdfData().getCompilationModes().isNull()){
		if(!model.pdfIsInCartaChimica() && model.mainPdfAnag().getPdfIsStampaEnabled().booleanValue()){
	    	showStampa = true;
		}
	}else{
		if(!model.pdfIsInCartaChimica() && model.getPdfData().getCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA) >= 0){
	    	showStampa = true;
		}
	}
	
	String nacUrlDomain = model.getNacUrlDomain();
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
.messaggio {
	color: #3e89bd; 
	font-family: Segoe UI; 
	font-size: 15px; 
	font-style: normal; 
	font-weight: bold; 
	text-decoration: none;
}
</style>

<%=template.getHeader()%>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageFields.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPage.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageDataentryUtil.js"></script>

<script>
var jsPdfInstanceId = "<%=model.getPdfData().getPdfInstanceId()%>";
function doShowdata(){
	var url = "call.wfem?wfemCmd=prgm.pdfwebforms.display.PdfResultDataOnNewWindow.execute&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
	return false;
}
function doGoHome(){
	document.goHomeForm.submit();
	return false;
}
function doBack(){
	<% if(!model.getPdfData().getGobackEndUrl().isNull()){ %>
		<% if(model.getPdfData().getGobackEndUrlOnTop().booleanValue()){ %>
			top.location.href="<%=model.getPdfData().getGobackEndUrl()%>";
		<% }else{ %>
			location.href="<%=model.getPdfData().getGobackEndUrl()%>";
		<% } %>
	<% } %>
	return false;
}
function aggiornaWidgetNac(){
	<% if(nacUrlDomain.length() > 0){ %>
		try{		
			top.postMessage('{"action":"refreshWidget","value":"med_nac_sidebar_customer_master"}', "<%=nacUrlDomain%>"); 
		}catch(e){}
		try{
			top.postMessage('{"action":"refreshWidget","value":"med_nac_sidebar_customer_informations"}', "<%=nacUrlDomain%>");		
		}catch(e){}
	<% } %>
}
function doTornaACarrello(){
	try{		
		parent.tornaAlCarrello();
	}catch(e){}
}
function onLoadBodyFnc(){
	aggiornaWidgetNac();
	sendPdfCompletedProcessEvent({"pdfInstanceId":"<%=model.getPdfData().getPdfInstanceId()%>", "pdfCompilationMode":"<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO%>"});
}
</script>
<%@ include file="../../display/PdfReady.html"%>
</head>

<body onload="onLoadBodyFnc();">

<% if(!model.isTestMode()){ %>
	<iframe src='call.wfem?wfemCmd=prgm.pdfwebforms.copernicoprocess.business.EndCopernicoProcess.execute&readRequest=false&pdfInstanceId=<%=model.getPdfData().getPdfInstanceId()%>' style='display:none;'></iframe>
<% } %>

<form name="goHomeForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoHome.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="dati" id="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfInstanceId" value="<%=model.getPdfData().getPdfInstanceId()%>">
<input type="hidden" name="scrollYValue" value="">
<input type="hidden" name="scrollXValue" value="">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
</form>

<center>
<table class="htmlContainer" style="height:auto;">
	<tr>
		<td style="padding:10;">
			<% if(model.getMessaggioFineOperazione() != null){ %>
				<table class="testo" width="100%">
						<tr><td><%=model.getMessaggioFineOperazione()%></td></tr>
				</table>
			<% } %>
		</td>
	</tr>
	
	<tr>
		<td align="center" height="100%" valign="top">
			<table class="testo" style="height:300px;"><tr>
				<td>
					<span class="titolo">La proposta è stata inviata correttamente.</span><br><br>
					Puoi visualizzarla nelle sezioni di consultazione delle proposte di contratto a tua disposizione
				</td>
			</tr></table>
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
								<% if(model.isTestMode()){ %>
									<td><div class="normalButton whiteButton" onclick="doGoHome();">Home</div></td>
								<% }else if(!model.getPdfData().getIdCarrello().isNull() && model.getPdfData().getPdfEnvironment().equals("CARRELLO") && !model.reportAdeguatezzaPassatoDalChiamate()){ %>
						    		<td><div class="normalButton whiteButton" onclick="doTornaACarrello();">Torna al Carrello</div></td>
								<% }else if(!model.getPdfData().getGobackEndLabel().isNull() && !model.getPdfData().getGobackEndUrl().isNull()){ %>
						    		<td><div class="normalButton whiteButton" onclick="doBack();"><%=model.getPdfData().getGobackEndLabel()%></div></td>
								<% } %>								
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
						<table>
							<tr>
								<% if(model.isTestMode()){ %>
									<td><div class="normalButton" onclick="doShowdata();">Dati</div></td>
								<% } %>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>	
</table>
</center>

<%=template.getFooter()%>
</body>
</html>
