<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
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
	
	boolean showWayoutCarta = false;
	boolean showWayoutCopernico = false;
	if(model.isWayoutEnabled()){
		if((model.getPdfData().getPdfEnvironment().equals(PdfDataModel.ENVIRONMENT_CATALOGO_MODULI) || model.getPdfData().getPdfEnvironment().equals(PdfDataModel.ENVIRONMENT_CATALOGO_OPERAZIONI)) && model.isErrorOnSign()){
			if(model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA) >= 0 || 
			   model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA) >= 0){   		
				showWayoutCarta = true;
			}
			if(model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) >= 0){   		
				showWayoutCopernico = true;
			}
		}
		// Per ora il wayout non è ne nello switch ne nel carrello (non essendoci ancora copernico)
		if(model.getPdfData().getIsSwitch().booleanValue() || !model.getPdfData().getIdCarrello().isNull()){
			showWayoutCarta = false;
			showWayoutCopernico = false;
		}
	}
	
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
var jsENVIRONMENT_CATALOGO_MODULI = "<%=PdfDataModel.ENVIRONMENT_CATALOGO_MODULI%>";
var jsPdfEnvironment = "<%=model.getPdfData().getPdfEnvironment()%>";
var isPdfIsInCartaChimica = <%=model.pdfIsInCartaChimica()%>;

function onLoadBodyFnc(){
	trapPageFocus();
	document.getElementById('main-container').focus();
}
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
function doInviaInSede(){
	openModalPopup("prgm.pdfwebforms.display.PdfSendProcessAlert",
			  	   "BrowserInstance="+__getBrowserInstance(),
	  			   null,doInviaInSedeCallback,"Avviso",350,450,true);
	return false;
}
function doInviaInSedeCallback(obj){
	if(obj == null)
		return;
	wait();	
	document.goOnWayoutForm.codiciOperazionePritPerMultioperazione.value = obj.codiciOperazionePritPerMultioperazione;
	<% if(model.isInBasket()){ %>
	document.goOnWayoutForm.wfemCmd.value = "prgm.pdfwebforms.sendprocess.business.StartSendBasketProcess.execute";
	<% }else{ %>
	document.goOnWayoutForm.wfemCmd.value = "prgm.pdfwebforms.sendprocess.business.StartSendProcess.execute";
	<% } %>
	document.goOnWayoutForm.submit();
	return;
}

function doCopernico(){
	wait();
	<% if(model.isInBasket()){ %>
	document.goOnWayoutForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.business.StartCopernicoBasketProcess.execute";
	<% }else{ %>
	document.goOnWayoutForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.business.StartCopernicoProcess.execute";
	<% } %>
	document.goOnWayoutForm.submit();
	return;
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
<%@ include file="../../display/PdfReady.html"%>
</head>

<body onload="onLoadBodyFnc();">

<form name="goHomeForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoBackOnError.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="goOnWayoutForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="flagWayout" value="true">
<input type="hidden" name="codiciOperazionePritPerMultioperazione" value="">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<div class="htmlContainer" style="width:auto;padding:20;text-align:left;">
	<div tabindex="-1" id="main-container">
		<div class="titolo" style="padding-bottom:20px;">Indisponibilità servizio Firma digitale</div>
		<div class="testo" style="padding-bottom:50px;">
			<% if(showWayoutCarta || showWayoutCopernico){ %>
				<% if(showWayoutCarta && showWayoutCopernico){ %>
					Siamo Spiacenti il servizio di Firma Digitale al momento non è disponibile.<br>
					È comunque possibile continuare la disposizione inviando la proposta con Copernico, oppure procedere con la stampa del PDF da far firmare al cliente e l'invio in sede tramite Contratto Elettronico.
				<% }else if(showWayoutCarta){ %>
					Siamo spiacenti, il servizio di Firma digitale al momento non è disponibile.<br>
					È comunque possibile procedere con la stampa del PDF da far firmare al cliente e l'invio in sede tramite Contratto Elettronico.
				<% }else if(showWayoutCopernico){ %>
					Siamo Spiacenti il servizio di Firma Digitale al momento non è disponibile.<br>
					È comunque possibile continuare la disposizione inviando la proposta con Copernico.
				<% } %>
			<% }else{ %>
					<span class="testoAzzurro"><b>Attenzione</b></span><br><br>
					<% for(int i=0;i<model.getCommandErrors().size();i++){ %>
						<%=template.getProperty((CommandError)model.getCommandErrors().get(i))%><br>
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
						<% if(showWayoutCarta || showWayoutCopernico){ %>
							<table>
								<tr>
									<% if(showWayoutCopernico){ %>
					    				<td><div class="normalButton" onclick="doCopernico();">Copernico</div></td>
					    			<% } %>
									<% if(showWayoutCarta){ %>
					    				<td><div class="normalButton" onclick="doInviaInSede();">Invia in sede</div></td>
					    			<% } %>
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
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;" role="status" aria-live="polite" aria-hidden="true">
	<div class="waitAnchorContainer">
		<img id="waitAnchor" alt="Pagina in caricamento" src="<%=template.getWebApp()%>/jsWait/loading_0.gif">
	</div>
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
</html><% model.setErrorOnSign(false); %>
