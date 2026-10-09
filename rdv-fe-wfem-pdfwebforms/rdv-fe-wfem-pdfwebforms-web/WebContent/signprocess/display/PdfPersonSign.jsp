<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse"%>
<%@page import="prgm.pdfwebforms.reportadeguatezza.Costanti"%>
<%@page import="prgm.pdfwebforms.signprocess.common.SignUtility"%>
<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="prgm.pdfwebforms.core.PdfPredefinedFields"%>
<%@page import="prgm.pdfwebforms.model.PdfPersonSignDataModel"%>
<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
<%@page import="prgm.pdfwebforms.basket.Basket"%>
<%@page import="prgm.pdfwebforms.basket.BasketElement"%>
<%@page import="com.atosorigin.wfem.types.*"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="com.atosorigin.wfem.coddesc.CodDescDataList"%>
<%@page import="prgm.pdfwebforms.core.PdfMyDialogDrawer"%>
<%@page import="prgm.pdfwebforms.materialeprecontrattuale.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	PdfPersonModel personaCorrente = model.getPersonaCorrente();
	PdfPersonSignDataModel signData = personaCorrente.getSignData();  
	
	boolean hasReportAdeguatezza = !personaCorrente.isAgente() && 
									model.isFirstSignPage() && 
									signData.isStepPinSuperato() && 
									model.getIdReportAdeguatezza().length() > 0 && !model.reportAdeguatezzaPassatoDalChiamate();


	// Il link alla raccomandazione lo mostriamo solo se il ramo IDD è PREVIDENZA ed è stata generata
	boolean isRamoIddPrevidenza = model.getIddCallModel() != null && 
						   		  model.getIddCallModel().getInput() != null && 
						   		  model.getIddCallModel().getInput().getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA;
	boolean showLinkRaccomandazioneIdd = !personaCorrente.isAgente() && 
										  model.isFirstSignPage() && 
										  signData.isStepPinSuperato() &&
										  isRamoIddPrevidenza && 
										  !model.getIddCallModel().getIdRaccomandazioneIdd().isNull();
	
	String linkReportAdeguatezzaTextEND = "Report di Adeguatezza"; 
	String linkReportAdeguatezzaTextWAIT = "Report di Adeguatezza in elaborazione";
	String linkReportAdeguatezzaTextERROR = Costanti.MESSAGGIO_ERRORE;
	
	boolean isLastPdfInDispo = !model.isMultiPdf() ? true : model.getPdfData().getPdfIndex().intValue()==model.getPdfData().getPdfs().size()-1;
	String htmlMaterialePrecontrattuale = MaterialePrecontrattualeHtmlDrawer.htmlMaterialePrecontrattuale(model);
	String htmlMaterialePrecontrattualeAccessorio = MaterialePrecontrattualeAccessorioHtmlDrawer.htmlMaterialePrecontrattuale(model);
%>
<html lang="it">
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
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>

<script>
var jsIsInBasket = <%=model.isInBasket()%>;
var isTIPO_PRIIPS_PREVIDENZA = "<%=PdfAnagModel.TIPO_PRIIPS_PREVIDENZA%>";
var jsTipoPriips = "<%=model.mainPdfAnag().getTipoPriips()%>";
</script>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageFields.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageDataentryUtil.js"></script>
<script src="<%=template.getWebApp()%>/signprocess/display/PdfPersonSign.js"></script>
<script src="<%=template.getWebApp()%>/display/MaterialePrecontrattuale.js"></script>

<%@ include file="../../display/PdfReady.html"%>

<script src="<%=template.getWebApp()%>/display/OpenReportsAdeguatezza.js"></script>
<% if(hasReportAdeguatezza){ %>
	<script>
	var linkReportAdeguatezzaTextEND = "<%=linkReportAdeguatezzaTextEND%>"; 
	var linkReportAdeguatezzaTextWAIT = "<%=linkReportAdeguatezzaTextWAIT%>";
	var linkReportAdeguatezzaTextERROR = "<%=linkReportAdeguatezzaTextERROR%>";
	var linksReportAdeguatezza = <%=model.writeLinksReportAdeguatezza()%>;
	var jsIdReportAdeguatezza = "<%=model.getIdReportAdeguatezza()%>"
	var maxNumRetryReportAdeguatezza = <%=model.getMaxRecuperaReportAdeguatezzaRetryCount()%>;
	</script>
	<script src="<%=template.getWebApp()%>/signprocess/display/ReportAdeguatezza.js"></script>
	<% if(model.getStatoRecueroReportAdeguatezza().equals("WAIT")){ %>
		<script>callRefreshReportAdeguatezza();</script>
	<% } %>
<% } %>
<% if(showLinkRaccomandazioneIdd){ %>
<script src="<%=template.getWebApp()%>/display/OpenRaccomandazioneIdd.js"></script>
<% } %>
</head>

<body onload="onPageLoad();">
<%@ include file="./include/BusinessUnitOtpBasket.html"%>

<form name="erroreReportAdeguatezza" id="erroreReportAdeguatezza" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.reportadeguatezza.PdfErrorsOnReportAdeguatezza.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="scrollYValue" value="">
<input type="hidden" name="scrollXValue" value="">
</form>

<form name="dati" id="dati" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="modelHashCode" value="<%=model.hashCode()%>">
<input type="hidden" name="dataHashCode" value="<%=model.getPdfData().getPdfIndex()%>">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="scrollYValue" value="">
<input type="hidden" name="scrollXValue" value="">
<input type="hidden" name="personaCorrente_signData_leggiIlContrattoClicked" value="<%=personaCorrente.isAgente()?"true":signData.getLeggiIlContrattoClicked()%>">

<center>
<div class="htmlContainer">
	<div style="margin:5px;">
		<% if(model.hasCommandWarnings()){ %>
			<div class="titolo">ATTENZIONE<br><%=model.getCommandWarnings().get(0)%></div>
			<% model.resetCommandWarnings(); %> 
		<% } %>
		<div style="height:100%;">
		<%if(!signData.isStepPinSuperato()){%>
			<%@ include file="./include/Pin.html"%>
		<%}else{%>
			<%if(model.isInBasket()){%>
				<%@ include file="./include/SignFlagsBasket.html"%>
			<%}else{%>
				<%@ include file="./include/Otp.html"%>
			<%}%>
		<%}%>
		</div>
	</div>
</div>
</center>
</form>

<% if(model.hasCommandErrors()){ %>
	<%=PdfMyDialogDrawer.drawDialogStartHtml(template,
		"errorReportMessage",
		"Avviso",
		"350","550",
		"Chiudi")%>
		<div class="testo" style="width:100%;height:100%;display:grid;grid-template-columns:100%;grid-template-rows:1fr auto;">
			<div>
				<div style="height:100%;width:100%;position:relative;overflow:auto;">
					<div style="position:absolute;left:0;top:0;">
						<% for(int i=0;i<model.getCommandErrors().size();i++){ %>
							<%=template.getProperty((CommandError)model.getCommandErrors().get(i))%><br>
						<% } %>
					</div>
				</div>
			</div>
			<div style="margin:auto;">
				<div role="button" class="normalButton" onclick="closeMyDialog('errorReportMessage');">Ok</div>
			</div>
		</div>
	<%=PdfMyDialogDrawer.drawDialogEndHtml()%>
	<% model.resetCommandErrors(); %>
	<script>
		openMyDialog("errorReportMessage");
	</script>
<% } %>

<% if(SignUtility.mostraOtp(model) && signData.isStepFlagSignSuperato()){ %>
	<%=PdfMyDialogDrawer.drawDialogStartHtml(template,
		"otpDialog",
		"Riepilogo e firma",
		"520","850")%>
		<form name="otpForm" id="otpForm" method="post" action="call.wfem" style="margin:0px;">
			<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.signprocess.business.GoOnBasketSignProcess.execute">
			<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
			<input type="hidden" name="modelHashCode" value="<%=model.hashCode()%>">
			<input type="hidden" name="dataHashCode" value="<%=model.getPdfData().getPdfIndex()%>">
			<%@ include file="./include/OtpBasket.html"%>
		</form>
	<%=PdfMyDialogDrawer.drawDialogEndHtml()%>
	<script>openMyDialog("otpDialog");</script>
<% } %>

<%=PdfMyDialogDrawer.drawDialogStartHtml(template,
	"signAlertMessage",
	"Avviso",
	"220","380",
	"Chiudi","closeSignAlert();")%>
	<div class="testo" style="width:100%;height:100%;display:grid;grid-template-columns:100%;grid-template-rows:1fr auto;">
		<div id="signAlertMessageText"></div>
		<div style="margin:auto;">
			<div role="button" class="normalButton" id="signAlertMessageOkButton" onclick="closeSignAlert();">Ok</div>
		</div>
	</div>
<%=PdfMyDialogDrawer.drawDialogEndHtml()%>

<%=PdfMyDialogDrawer.drawDialogStartHtml(template,
	"leggiContrattoAlertMessage",
	"Avviso",
	"220","380")%>
	<div class="testo" style="width:100%;height:100%;display:grid;grid-template-columns:100%;grid-template-rows:1fr auto;">
		<div>
			 Il file PDF che stai per scaricare è fornito esclusivamente per scopi di accessibilità.
			 Ti informiamo che non può essere utilizzato per l'apposizione delle firme e l'invio in sede
		</div>
		<div style="margin:auto;">
			<div role="button" class="normalButton" id="leggiIlContrattoOkButton" onclick="confirmLeggiIlContrattoAlert();">Ok</div>
		</div>
	</div>
<%=PdfMyDialogDrawer.drawDialogEndHtml()%>

<%=PdfMyDialogDrawer.drawDialogStartHtml(template,
	"pdfPriipsPrevidenzaAlert",
	"Avviso",
	"430","700")%>
	<div class="testo" style="width:100%;height:100%;display:grid;grid-template-columns:100%;grid-template-rows:1fr auto;">
		<div style="display:grid;grid-template-columns:100%;">
			<div>Dichiaro di aver ricevuto e di aver preso visione del materiale precontrattuale sopra riportato.</div>
			<div style="font-weight: bold;">CONFERMA INFORMAZIONI AGGIUNTIVE SULLE RICHIESTE ED ESIGENZE DELL'ADERENTE</div>
			<div>
				Le presenti informazioni aggiuntive hanno lo scopo di acquisire nell'interesse del cliente alcune ulteriori informazioni utili
				per completare la valutazione di adeguatezza e congruità del contratto che intende sottoscrivere rispetto ai suoi bisogni assicurativi. 
			</div>
			<div>
				Tolleranza alla volatilità e aspettativa di rendimento: <b><%=model.getPdfData().getDescValue("priipsTolleranzaVolatilita")%></b>
				<br>
				Orizzonte temporale indicativo: <b><%=model.getPdfData().getDescValue("priipsOrizzonteTemporale")%></b>
			</div>
			<div>
				Per procedere alla generazione della raccomandazione è necessario confermare le informazioni aggiuntive
				sulle richieste ed esigenze dell'aderente.
			</div>
			<div>
				Confermo che le informazioni sopra riportate sono quelle da me precedentemente comunicate al Family Banker.
			</div>
		</div>
		<div style="margin:auto;">
			<div role="button" tabindex="0" id="pdfPriipsPrevidenzaAlertOkButton" class="normalButton" style="width:80px;" onclick="confirmOpenRaccomandazioneIddOnSign();">Ok</div>
		</div>
	</div>
<%=PdfMyDialogDrawer.drawDialogEndHtml()%>	

<% if(htmlMaterialePrecontrattuale.length() > 0){ %>
	<%@ include file="../../display/MaterialePrecontrattuale.html"%>
<% } %>
<% if(htmlMaterialePrecontrattualeAccessorio.length() > 0){ %>
	<%@ include file="../../display/MaterialePrecontrattualeAccessorio.html"%>
<% } %>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;" role="status" aria-live="polite" aria-hidden="true">
	<div class="waitAnchorContainer">
		<img id="waitAnchor" alt="Pagina in caricamento" src="<%=template.getWebApp()%>/jsWait/loading_0.gif">
	</div>
</div>	

<%=template.getFooter()%>
</body>
</html>