<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="com.atosorigin.wfem.types.IntegerType"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
<%@page import="com.atosorigin.wfem.types.AbstractType"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
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
#main-container:focus {
    outline: none;
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
	sendPdfCompletedProcessEvent({"pdfInstanceId":"<%=model.getPdfData().getPdfInstanceId()%>", "pdfCompilationMode":"<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE%>"});
	trapPageFocus();
	document.getElementById('main-container').focus();
}
</script>
<%@ include file="../../display/PdfReady.html"%>
</head>

<body onload="onLoadBodyFnc();">

<%=prgm.pdfwebforms.core.PdfEndProcessLogDrawer.draw(model,"prgm.pdfwebforms.signprocess.business.EndSignProcess","")%>

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
<div class="htmlContainer" style="width:auto;padding:20;text-align:left;">
	<div tabindex="-1" id="main-container" style="width:100%;height:100%;display:grid;grid-template-columns:100%;grid-template-rows:auto 1fr auto auto;row-gap:10px;">
		<div class="testo" style="padding:20px;padding-left:10px;padding-bottom:10px;">
			<span class="titolo"><%=model.getTitoloFineOperazione()==null?"La compilazione è terminata":model.getTitoloFineOperazione()%></span>
			<% if(model.getMessaggioFineOperazione() != null){ %>
				<br><%=model.getMessaggioFineOperazione()%>
			<% } %>
		</div>
		<div id="pdfObj">
		</div>
		<div class="line">&nbsp;</div>		
		<div>
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
		</div>
	</div>
</div>
</center>

<script>
<% if(model.isTestMode()){ %>
	includePdfObject("pdfObj","call.wfem?wfemCmd=prgm.pdfwebforms.stream.PdfTestContentAsBinary.execute&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>");
<% }else{ %>
	includePdfObject("pdfObj","call.wfem?wfemCmd=prgm.pdfwebforms.stream.PdfInstanceContentAsBinary.execute&inProcessPdfInstanceContentAsBinary=true&pdfInstanceId=<%=Basket.getElencoIdDispoOK(model)%>&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>");
<% } %>
</script>

<%=template.getFooter()%>
</body>
</html>
