<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="com.atosorigin.wfem.types.IntegerType"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="com.atosorigin.wfem.types.AbstractType"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.basket.Basket"%>

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
	
	boolean showStampa = true;
	if(model.pdfIsInCartaChimica() || model.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE))
		showStampa = false;
	
	String nacUrlDomain = model.getNacUrlDomain();

	boolean hasReportAdeguatezza = model.getIdReportAdeguatezza().length() > 0 && !model.reportAdeguatezzaPassatoDalChiamate();
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
</style>

<%=template.getHeader()%>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageFields.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPage.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageDataentryUtil.js"></script>

<script>
var jsPdfInstanceId = "<%=model.getPdfData().getPdfInstanceId()%>";
function openPdfModuloCompilato(){
	
	var linkReportAdeguatezza = document.getElementById("linkReportAdeguatezza");
	if(linkReportAdeguatezza != null){
		if(linkReportAdeguatezza.getAttribute("isReportAdeguatezzaClicked") != "true"){
			showAlertErroreReportAdeguatezza("Prima di stampare il contratto è necessario visualizzare il Report di adeguatezza");
			return;
		}
	}
	
	openPdfObject(document.openPdfForm,"Modulo");
}

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
	sendPdfCompletedProcessEvent({"pdfInstanceId":"<%=model.getPdfData().getPdfInstanceId()%>", "pdfCompilationMode":"<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA%>"});
}
</script>

<%@ include file="../../display/PdfReady.html"%>

<% if(hasReportAdeguatezza){ %>
	<script>
	var linksReportAdeguatezza = <%=model.writeLinksReportAdeguatezza()%>;
	var jsIdReportAdeguatezza = "<%=model.getIdReportAdeguatezza()%>"
	</script>
	<script src="<%=template.getWebApp()%>/display/OpenReportsAdeguatezza.js"></script>
	<script src="<%=template.getWebApp()%>/sendprocess/display/ReportAdeguatezza.js"></script>
<% } %>
</head>

<body onload="onLoadBodyFnc();">

<%=prgm.pdfwebforms.core.PdfEndProcessLogDrawer.draw(model,"prgm.pdfwebforms.sendprocess.business.EndSendProcess","")%>

<form name="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfInstanceId" value="<%=model.getPdfData().getPdfInstanceId()%>">
<input type="hidden" name="inProcessPdfInstanceContentAsBinary" value="true">
</form>

<form name="openPdfForm" id="openPdfForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.stream.PdfInstanceContentAsBinary.execute">
<input type="hidden" name="pdfInstanceId" value="<%=Basket.getElencoIdDispoOK(model)%>">
<input type="hidden" name="inProcessPdfInstanceContentAsBinary" value="true">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="goHomeForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoHome.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<table class="htmlContainer" <%=(hasReportAdeguatezza?"style='height:auto;'":"")%>>
	<tr>
		<td style="padding:10;">
			<table width="100%">
				<tr>
					<td valign="top">
						<% if(!hasReportAdeguatezza){ %>
						<table class="testo">
							<tr><td class="titolo"><%=model.getTitoloFineOperazione()==null?"La compilazione è terminata":model.getTitoloFineOperazione()%></td></tr>
							<% if(model.getMessaggioFineOperazione() != null){ %>
								<tr><td class="testoPiccolissimo"><%=model.getMessaggioFineOperazione()%></td></tr>
							<% } %>
						</table>
						<% } %>
					</td>
					<td align="right" valign="top">
						<% if(!model.isTestMode() && showStampa){ %>
							<table class="testo">
								<tr>
									<td>
										<script>
										if(navigator.userAgent.indexOf('iPad') != -1){
											document.write("per visualizzare il modulo su IPad <span style='cursor:pointer;' onclick='openPdfModuloCompilato();'><u><b>clicca qui</b></u></span>");
										}
										</script>
									</td>
								</tr>
							</table>
						<% } %>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<% if(hasReportAdeguatezza){ %>
			<td align="center" height="100%" valign="top">
				<table class="testo" style="height:300px;">
					<tr>
						<td>
							<span class="titolo">Il contratto è stato inviato in Sede.</span><br><br>
							Visualizza il Report di Adeguatezza e procedi alla stampa del Contratto e invialo in Sede firmato dal cliente.
						</td>
					</tr>
					<% if(model.getMessaggioFineOperazione() != null && model.getMessaggioFineOperazione().indexOf("Attenzione") >= 0){ %>
						<tr><td class="testoPiccolissimo" valign="bottom"><%=model.getMessaggioFineOperazione()%></td></tr>
					<% } %>
				</table>
			</td>
		<% }else{ %>
			<td id="pdfObj" height="100%">
			</td>
		<% } %>
	</tr>
	
	<tr><td style="padding-top:10;"><div class="line">&nbsp;</div></td></tr>

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
								<% }else{ %>
							    	<% if(hasReportAdeguatezza){ %>
								    	<td>
								    		<div class="normalButton" id="linkReportAdeguatezza" onclick="openReportAdeguatezza(this);">
								    			Visualizza Report di Adeguatezza
								    		</div>
								    	</td>
									<% } %>
							    	<% if(showStampa){ %>
								    	<td><div class="normalButton" onclick="openPdfModuloCompilato();"><%=(hasReportAdeguatezza?"Stampa Contratto":"Stampa")%></div></td>
							    	<% } %>
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

<% if(!hasReportAdeguatezza){ %>
	<script>
	<% if(model.isTestMode()){ %>
		includePdfObject("pdfObj","call.wfem?wfemCmd=prgm.pdfwebforms.stream.PdfTestContentAsBinary.execute&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>");
	<% }else{ %>
		includePdfObject("pdfObj","call.wfem?wfemCmd=prgm.pdfwebforms.stream.PdfInstanceContentAsBinary.execute&inProcessPdfInstanceContentAsBinary=true&pdfInstanceId=<%=Basket.getElencoIdDispoOK(model)%>&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>");
	<% } %>
	</script>
<% } %>

<div id="avvisoReportAdeguatezzaMessage" style="display:none;">
	<table height="100%" width="100%">
		<tr>
			<td valign="top" height="100%" style="padding: 10;">
				<table>
					<tr>
						<td class="testo">Ti ricordiamo che il Report di Adeguatezza non deve essere inviato in sede</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center">
				<table><tr>
					<td><div class="normalButton" onclick="$('#avvisoReportAdeguatezzaMessage').dialog('close').dialog('destroy');">Ok</div></td>
				</tr></table>
			</td>
		</tr>
	</table>
</div>

<div id="errorReportAdeguatezzaMessage" style="display:none;">
	<table height="100%" width="100%">
		<tr>
			<td valign="top" height="100%" style="padding: 10;">
				<table>
					<tr>
						<td class="testo" id="errorReportAdeguatezzaMessageText"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center">
				<table><tr>
					<td><div class="normalButton" onclick="$('#errorReportAdeguatezzaMessage').dialog('destroy');">Ok</div></td>
				</tr></table>
			</td>
		</tr>
	</table>
</div>

<%=template.getFooter()%>
</body>
</html>
