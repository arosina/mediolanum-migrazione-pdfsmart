<%@page import="com.atosorigin.wfem.types.BooleanType"%>
<%@page import="prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean"%>
<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.core.PdfPredefinedFields"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
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
 	String closeBrowserInstances = "";
	if(!model.getPdfData().getIsOnSameStack().booleanValue())
		closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");
	
	int numPdfSignFiels = model.getNumDeclaredPdfSignFields();

	String labelInviaInSede = model.getPdfData().getInviaInSedeButtonLabel().isNull() ? "Invia in sede" : model.getPdfData().getInviaInSedeButtonLabel().toString();
	String labelFirmaDigitale = "Firma digitale";
	if(model.getPdfData().getFirmaDigitaleButtonLabel().isNull()){
		if(numPdfSignFiels == 0)
			labelFirmaDigitale = "Invia in digitale";
	}else{
		labelFirmaDigitale = model.getPdfData().getFirmaDigitaleButtonLabel().toString();
	}
	String labelCopernico = model.getPdfData().getCopernicoButtonLabel().isNull() ? "Copernico" : model.getPdfData().getCopernicoButtonLabel().toString();
	String amlMessage = model.getMessaggioPdfAmlAlert();
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
<script src="<%=template.getWebApp()%>/display/PdfPreviewContainer.js"></script>

<script>
var jsIsInBasket = <%=model.isInBasket()%>;
var jsIsAssistenteFB = "<%=model.getUserSessionContext().getClientSessionContext().isAssistenteFB()%>";
var jsNumPdfSignFiels = "<%=numPdfSignFiels%>";
var jsIsSede = "<%=model.getIsSede()%>";
var jsNumPdf = <%=model.getPdfAnags().size()%>;
var jsENVIRONMENT_CATALOGO_MODULI = "<%=PdfDataModel.ENVIRONMENT_CATALOGO_MODULI%>";
var jsENVIRONMENT_CATALOGO_OPERAZIONI = "<%=PdfDataModel.ENVIRONMENT_CATALOGO_OPERAZIONI%>";
var jsPdfEnvironment = "<%=model.getPdfData().getPdfEnvironment()%>";
var jsMODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA%>";
var jsMODALITA_SOTTOSCRIZIONE_CARTA_LIBERA = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA%>";
var jsMODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE%>";
var jsMODALITA_SOTTOSCRIZIONE_COPERNICO = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO%>";
var isPdfIsInCartaChimica = <%=model.pdfIsInCartaChimica()%>;
var jsHasProcessoControlliCompleti = <%=model.hasProcessoControlliCompleti()%>;

var jsPdfCodProdottoPrit = "<%=model.mainPdfAnag().getPdfCodProdottoPrit()%>";
var jsPdfCodOperazionePrit = "<%=model.mainPdfAnag().getPdfCodOperazionePrit()%>";
var jsPRIT_MILTIOPERAZIONE_CODE = "<%=PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE%>";
var jsHasLayerCollocamentoADistanza = <%=Basket.hasLayerCollocamentoADistanza(model)%>;
var jsTipoDistanzaCollocamento = <%=Basket.getTipoDistanzaCollocamento(model)==null?"null":("'"+Basket.getTipoDistanzaCollocamento(model)+"'")%>;

function onPageLoad(){
	document.getElementById('main-container').focus();
}
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

<body onload="onPageLoad();">

<% 
	String gotoLastCmd = "prgm.pdfwebforms.business.GotoLastPdf"; 
	if(model.getCoraModel() != null)
		gotoLastCmd = "prgm.pdfwebforms.aml.CoraPage"; 
	else if(model.getAmlModel() != null)
		gotoLastCmd = "prgm.pdfwebforms.aml.AmlPage"; 
%>
<form name="goLastPdfForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="<%=gotoLastCmd%>.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<%if(model.getAmlModel() != null){ %>
<input type="hidden" name="amlModel_tabSelezionato" value="">
<% } %>
</form>

<form name="goOnForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="codiciOperazionePritPerMultioperazione" value="">
<input type="hidden" name="isFirmaADistanza" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="dati" id="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="scrollYValue" value="">
<input type="hidden" name="scrollXValue" value="">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
</form>

<center>
<table tabindex="-1" id="main-container" class="htmlContainer">
	<tr>
		<td style="padding:10;">
			<table width="100%">
				<tr>
					<td>
						<table class="testo">
							<tr>
								<td class="titolo">Verifica del modulo</td>
							</tr>
							<tr>
								<td id="pdfTitle" <%=(model.isInBasket()?"style='display:none;'":"")%>>
								<% String title = model.getPdfData().getPdfTitle().isNull()?model.getPdfAnag().getPdfCode()+" "+model.getPdfAnag().getPdfDescr():model.getPdfData().getPdfTitle().toString(); %>
								<% if(!model.getPdfData().getPdfInstanceId().isNull()){ %>
									<%=title%>
									<br><span style="font-size:12px;">N.&nbsp;<%=model.getPdfData().getPdfInstanceId()%></span>
								<% }else{ %>
									<%=title%>
								<% } %>
								</td>
							</tr>
						</table>
					</td>
					<td align="right">
						<table class="testo">
							<tr>
								<td>
									<script>
									if(navigator.userAgent.indexOf('iPad') != -1){
										document.write("per visualizzare il modulo su IPad <span style='cursor:pointer;' onclick='openStampa();'><u><b>clicca qui</b></u></span>");
									}
									</script>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td id="pdfObj" height="100%" tabindex="-1">
		</td>
	</tr>
	
	<% if(!model.isOperatoreMOM()){ %>
	<tr><td style="padding-top: 10;"><div class="line">&nbsp;</div></td></tr>
	
	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td style="padding:10;padding-top:3;">
						<table>
							<tr>
								<% if(model.isTestMode()){ %>
								
				    				<td><div class="normalButton" onclick="doGoLastPdf();">Indietro</div></td>
				    				
								<% }else{ %>
								
									<% if(model.getPdfData().getSkipDataentry().booleanValue()){ %>							
										<% if(!model.getPdfData().getGobackUrl().isNull() && model.getPdfData().getPdfIndex().intValue() == 0){ %>
											<% if(!model.getPdfData().getGobackLabel().isNull()){ %>
									    		<td><div id="indietroButton" class="normalButton whiteButton" onclick="doBack();"><%=model.getPdfData().getGobackLabel()%></div></td>
											<% }else{ %>
									    		<td><div id="indietroButton" class="normalButton whiteButton" onclick="doBack();">Indietro</div></td>
											<% } %>
								    	<% } %>
							    	<% }else{ %>
					    				<td><div id="indietroButton" class="normalButton whiteButton" onclick="doGoLastPdf();">Indietro</div></td>
							    	<% } %>
								
								<% } %>
								
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
						<table>
							<tr>
								<% if(model.isTestMode()){ %>
									
									<% 
										boolean fdEnab = false;
										boolean cdEnab = false;
										boolean invEnab = false;
										boolean stampaEnab = false;

					    		 		if(model.isFirmaDigitaleAccessibile() && model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) >= 0){   		
						    				fdEnab = true;
						    		   	}
					    		 		if(model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) >= 0){   		
						    				cdEnab = true;
						    		   	}
					    				if(model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA)  >= 0 || 
					    				   model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA) >= 0){   		
						    				invEnab = true;
						    			}
								    	if(!model.pdfIsInCartaChimica() && model.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA) >= 0){
								    		stampaEnab = true;
							    	   	}
							    	%>

									<% if(numPdfSignFiels > 0){ %>
						    			<td><div class="normalButton" onclick="doFirma();">Firma digitale<%=(fdEnab?"":" (d)")%></div></td>
					    			<% }else{ %>
						    			<td><div class="normalButton" onclick="doCartaDigitale();">Invia in digitale<%=(cdEnab?"":" (d)")%></div></td>
					    			<% } %>
					    			<td><div class="normalButton" onclick="doInviaInSede();">Invia in sede<%=(invEnab?"":" (d)")%></div></td>
							    	<td><div class="normalButton" onclick="openStampa();">Stampa<%=(stampaEnab?"":" (d)")%></div></td>
					    			
								<% }else{ %>
								
									<%@ include file="./PdfPreviewContainerRightButtons.html"%>
									
								<% } %>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% } %>
	
</table>
</center>

<% if(amlMessage != null && amlMessage.length() > 0){ %>
<div id="amlMessageDialog" style="display:none;">
	<table height="100%" width="100%">
		<tr>
			<td height="100%">
				<div style="height:100%;width:100%;position:relative;overflow-y:auto;">
					<table>
						<tr>
							<td style="vertical-align: top;">
								<img src="<%=template.getWebApp()%>/images/alertAml.png">
							</td>
							<td class="testo">
								<%=amlMessage%>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td align="center">
				<table><tr>
					<td><div class="normalButton" onclick="closeAmlPopup();">Ok</div></td>
				</tr></table>
			</td>
		</tr>
	</table>
</div>
<% } %>


<% 
	model.setSkipCommandWarnings(new BooleanType(false));
	model.setLastSpeakOnPageLoad("");
%>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<script>
includePdfObject("pdfObj","call.wfem?wfemCmd=prgm.pdfwebforms.stream.PdfPreview.execute&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>");
<% if(model.isOperatoreMOM()){ %>
	try{
		var data = { "msgXchanger": true, "channelMsgName": "PdfWebForms.ConfermaModificaDocumentoMOMEvent", "channelMsgData": {"conferma":"true"} }; 
		top.postMessage(JSON.stringify(data),"*");
	}catch(e){}
<% } %>
</script>

<%=template.getFooter()%>
</body>
</html>
