<%@page import="prgm.pdfwebforms.validation.CallPdfValidationEvent"%>
<%@page import="prgm.pdfwebforms.drivers.PdfDriverCaller"%>
<%@page import="prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataResponse"%>
<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="prgm.pdfwebforms.mom.CallMomEvent"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.publisher.common.CostantiPublisher"%>
<%@page import="prgm.pdfwebforms.drivers.PdfBasePageDriver"%>
<%@page import="java.io.InputStream"%>
<%@page import="com.atosorigin.wfem.command.CommandMessage"%>
<%@page import="com.atosorigin.wfem.command.CommandWarning"%>
<%@page import="com.atosorigin.wfem.coddesc.CodDescData"%>
<%@page import="com.atosorigin.wfem.coddesc.CodDescDataList"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
<%@page import="prgm.pdfwebforms.core.PdfPredefinedFields"%>
<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="com.atosorigin.wfem.types.*"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.basket.Basket"%>
<%@page import="prgm.pdfwebforms.basket.BasketElement"%>
<%@page import="prgm.pdfwebforms.mom.CostantiMOM"%>
<%@page import="prgm.pdfwebforms.materialeprecontrattuale.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	PdfDataModel pdfData = model.getPdfData();
	PdfAnagModel pdfAnag = model.getPdfAnag();
	
	// Su MOM vieneimpostato redonly e "vince" su tutto quindi anche readonlyDataentry è false
	// Se non si è in readonly readonlyDataentry viene invece pilotato
	boolean readonly = model.getModality() == Template.READ_MODALITY ? true : false;
	boolean readonlyDataentry = readonly;
	if(!readonly)
		readonlyDataentry = model.getPdfData().getReadonlyDataentry().booleanValue();
	
	boolean existJs = false;
	boolean existCss = false;
	String pdfPageDriverClassName = "";
	String pdfPageDriverWebapp = "";
	String pdfPageDriverJs = "";
	String pdfPageDriverCss = "";
	String templateApplCode = "";
	if(model.getPdfData().getPdfPageDriver() != null){
		String driverName = model.getPdfAnag().getPdfDriverName().toString().toLowerCase();
		if(driverName.length() > 0){
			String pdfPageDriverUrl = "";
			pdfPageDriverWebapp = "/PdfWebFormsDrivers-"+driverName;
			String driverVersion = model.getPdfAnag().getPdfDriverVersion().toString().toLowerCase();
			if(driverVersion.length() > 0)
				pdfPageDriverUrl = "/"+driverVersion;
			
			if(application.getContext(pdfPageDriverWebapp) != null){
				if(application.getContext(pdfPageDriverWebapp).getResource(pdfPageDriverUrl+"/PdfPageDriver.css") != null)
					existCss = true;
				if(application.getContext(pdfPageDriverWebapp).getResource(pdfPageDriverUrl+"/PdfPageDriver.js") != null)
					existJs = true;
			}
			
			pdfPageDriverUrl += "/PdfPageDriver.js";
			pdfPageDriverJs = pdfPageDriverWebapp+pdfPageDriverUrl;
			pdfPageDriverCss = pdfPageDriverJs.substring(0,pdfPageDriverJs.length()-2)+"css";
			
			templateApplCode = "PDFWEBFORMSDRIVERS-"+driverName.toUpperCase();
			if(driverVersion.length() > 0)
				templateApplCode += "-"+driverVersion.toUpperCase();
			template.setApplCode(templateApplCode);
			pdfPageDriverClassName = model.getPdfData().getPdfPageDriver().getClass().getName();
		}
	}
	
 	String closeBrowserInstances = "";
	if(!model.getPdfData().getIsOnSameStack().booleanValue())
		closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");
	
	boolean mostraSalva = false;
	if(!model.getPdfData().getIsVolatile().booleanValue() && !model.getPdfData().getHideSaveButton().booleanValue())
		mostraSalva = true;

	String jsPdfCode = pdfAnag.getPdfCode() == null ? "" : pdfAnag.getPdfCode().toString();
	if(jsPdfCode.length() > 0)
		jsPdfCode = jsPdfCode.replaceAll("\\\"", "\\\\\"");
		
	String jsPdfMomCode = pdfAnag.getPdfMomCode() == null ? "" : pdfAnag.getPdfMomCode().toString();
	if(jsPdfMomCode.length() > 0)
		jsPdfMomCode = jsPdfMomCode.replaceAll("\\\"", "\\\\\"");

	String jsPdfDescr = pdfAnag.getPdfDescr() == null ? "" : pdfAnag.getPdfDescr().toString();
	if(jsPdfDescr.length() > 0)
		jsPdfDescr = jsPdfDescr.replaceAll("\\\"", "\\\\\"");
	
	String codDescrAgevolazioneDipendenti = "";
	if(pdfData.getPdfDriver() != null && pdfData.getPdfInfos() != null && pdfData.getPdfInfos().getFieldInfos(PdfPredefinedFields.TIPO_AGEVOLAZIONE).size() == 1){
		ProvideAgevolazioneDipendentiDataResponse agevDipData = PdfDriverCaller.callProvideAgevolazioneDipendentiData(model.getUserSessionContext().getClientSessionContext(), pdfData);
		if(agevDipData != null)
			codDescrAgevolazioneDipendenti = agevDipData.getCodDescrAgevolazioneDipendenti();
	}
	
	String htmlMaterialePrecontrattuale = model.isInBasket() ? MaterialePrecontrattualeHtmlDrawer.htmlMaterialePrecontrattualeDispo(model) : "";
	String htmlMaterialePrecontrattualeAccessorio = model.isInBasket() ? MaterialePrecontrattualeAccessorioHtmlDrawer.htmlMaterialePrecontrattualeDispo(model) : "";
	boolean showMOMCoraFB = model.isOperatoreMOM() && pdfData.getCodDescDataList("coraFb") != null && pdfData.getCodDescDataList("coraFb").getCodDescCount() > 0;
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
<script src="<%=template.getWebApp()%>/jquery/jquery.ui.autocomplete.min.js"></script>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageFields.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPage.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageDataentryUtil.js"></script>
<script src="<%=template.getWebApp()%>/drivers/Pdf.js"></script>
<script src="<%=template.getWebApp()%>/drivers/PdfBasePageDriver.js"></script>
<% if(model.isOperatoreMOM()){ %>
<script src="<%=template.getWebApp()%>/display/PdfMomPage.js"></script>
<% } %>
<% if(model.isPdfOnValidation()){ %>
<script src="<%=template.getWebApp()%>/display/PdfValidationPage.js"></script>
<% } %>
<script src="<%=template.getWebApp()%>/display/MaterialePrecontrattuale.js"></script>

<script>
var jsIsReadonlyModality = <%=readonlyDataentry%>;
var jsIsSede = <%=model.getIsSede()%>;
var jsIsOperatoreMom = <%=model.isOperatoreMOM()%>;
var jsIsInValidazioneMOM = <%=model.isInValidazioneMOM()%>;
var jsIsInInserimentoMOM = <%=model.isInInserimentoMOM()%>;
var jsEscludiVariazioniCliente = <%=model.isInInserimentoMOM()||model.getPdfData().getExternalEntityName().equals(CostantiMOM.MOM_EXTERNAL_IMPORTED_KEY_ENTITY_NAME)%>;
var jsInitToCall = <%=pdfData.isJsInitToCall()?"true":"false"%>; <%pdfData.setJsInitToCall(false);%>
var jsIdCarrello = "<%=pdfData.getIdCarrello()%>";
var jsNumAgevolazione = "<%=pdfData.getNumAgevolazione()%>";
var jsCodAgevolazione = "<%=pdfData.getCodAgevolazione()%>";
var jsPdfCode = "<%=jsPdfCode%>";
var jsPdfMomCode = "<%=jsPdfMomCode%>";
var jsPdfDescr = "<%=jsPdfDescr%>";
var jsPdfPageDriverClass = "<%=pdfPageDriverClassName%>";
var jsPdfPublicationId = "<%=pdfAnag.getPdfPublicationId()%>";
var jsCodiceAgenteFieldName = "<%=PdfPredefinedFields.AGENTE_CODICE%>";
var jsNdgClienteFieldName = "<%=PdfPredefinedFields.CLIENTE_NDG_PREFIX%>";
var jsCognomeClienteFieldName = "<%=PdfPredefinedFields.CLIENTE_COGNOME_PREFIX%>";
var jsNomeClienteFieldName = "<%=PdfPredefinedFields.CLIENTE_NOME_PREFIX%>";
var jsCognomeNomeClienteFieldName = "<%=PdfPredefinedFields.CLIENTE_COGNOME_NOME_PREFIX%>";
var jsErrorOnSomePerson = <%=(model.getErrorOnSomePerson()==PdfPersonModel.NO_ERROR ? "false":"true")%>;
var jsCodAgeImpersonato = "<%=pdfData.getCodAgeImpersonato()%>";
var jsCodRuoloImpersonato = "<%=model.getPdfData().getCodRuoloImpersonato()%>";
var jsCodAgeFiltro = "<%=model.getCodAgeFiltro()%>";
var jscodDescrAgevolazioneDipendenti = "<%=codDescrAgevolazioneDipendenti%>";
var jsIsSwitch = <%=model.getPdfData().getIsSwitch()%>;
var isIsGestioneLegaleRappresentanteAttiva = <%=model.isGestioneLegaleRappresentanteAttiva()%>;
var jsCodiceLegaleRappresentante = "<%=model.getPdfData().getCodiceLegaleRappresentante()%>";
var jsVisiblePagesArray = [<%=pdfData.getVisiblePages()%>];

function doBack(){
	<%if(!model.getPdfData().getGobackUrl().isNull()){%>
		wait();
		setTimeout(function() {
			if(changeRunning){
				afterChangeFnc = doBack;
				return;
			}
			<% if(model.getPdfData().getGobackUrlOnTop().booleanValue()){ %>
				top.location.href="<%=model.getPdfData().getGobackUrl()%>";
			<% }else{ %>
				location.href="<%=model.getPdfData().getGobackUrl()%>";
			<% } %>
		}, submitDelay);		
	<%}%>
	return false;
}
</script>

<%@ include file="./PdfReady.html"%>
<% if(model.getPdfData().getPdfPageDriver() != null){ %>
	<% if(existCss){ %>
		<link rel='stylesheet' type='text/css' href='<%=pdfPageDriverCss%>'/>
	<% } %>
	<%=model.getPdfData().getPdfPageDriver().drawHeader(model, pdfData, pdfAnag)%>
	<% if(pdfPageDriverJs.length() > 0){ %>
		<% String[] jsScripts = model.getGlobalPageDriverJsScript();
		   if(jsScripts != null){
			   for(int i=0;i<jsScripts.length;i++){
				   String jsname = jsScripts[i];
				   if(!jsname.startsWith("/"))
					   jsname = "/"+jsname;
		%>
			   	<script src="<%=pdfPageDriverWebapp%><%=jsname%>"></script>
		   	<% }
		   } %>
		<% if(existJs){ %>
			<script src="<%=pdfPageDriverJs%>"></script>
		<% } %>
	<% } %>
<% } %>
<script>var pdfPageDriver = new PdfPageDriver();</script>
</head>

<body>
<% if(model.getPdfData().getPdfPageDriver() != null){ %>
<form name="datiEventi" id="datiEventi" method="post" action="call.wfem" style="display: none;">
	<input type="hidden" name="wfemCmd" value="">
	<input type="hidden" name="eventName" value="">
	<input type="hidden" name="eventArgs" value="">
	<input type="hidden" name="reloadPageOnEvent" value="false">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">				
	<input type="hidden" name="checkCodDescFields" value="false">
	<input type="hidden" name="manageChangedFields" value="false">				
	<input type="hidden" name="pdfData_editableFields" value="">
	<input type="hidden" name="pdfData_uneditableFields" value="">
	<input type="hidden" name="pdfData_extraMandatoryFields" value="">
	<input type="hidden" name="pdfData_hidedFields" value="">
</form>
<% } %>
<center>
<table class="htmlContainer">

	<% if(!readonly){ %>
	<tr>
		<td>
			<table width="100%">
				<% if(!model.getPdfAnag().getPdfCompilationExampleFileType().isNull()){ %>
					<tr>
						<td align="right" colspan="2">
							<table>
								<tr>
									<td><div class="normalButton" contentType="<%=model.getPdfAnag().getPdfCompilationExampleFileType()%>" onclick="doVisualizzaCompilationExample(this,'<%=model.getPdfAnag().getPdfId()%>');">Visualizza esempio di compilazione</div></td>
								</tr>
							</table>
						</td>
					</tr>
				<% } %>
				<tr>
					<td valign="top">
						<table class="testo">
							<tr><td class="titolo">Compilazione del modulo</td></tr>
							<tr>
								<td id="pdfTitle">
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
					<td align="right" valign="top">
						<table style="border-collapse:collapse;border-spacing:0;">
							<% if(!model.hasProcessoControlliCompleti()){ %>
								<tr>
									<td align="right">
										<table class="testo">
											<tr>
												<td><img style="width:30px;" src="<%=template.getWebApp()%>/images/pdfSenzaControlli.png"></td>
												<td style="color: rgb(204, 23, 137);font-size:12px;">! Modulo privo<br>di controlli</td>
											</tr>
										</table>								
									</td>
								</tr>
							<% } %>
							<tr>
								<td valign="top">
									<% if(htmlMaterialePrecontrattuale.length() > 0){ %>
									<div class="linkAperturaConIcona" style="width:210px;">
										<div><img src="<%=template.getWebApp()%>/images/visualizza_pdf.png"></div>
										<div>
											<span tabindex="-1" onclick="openMaterialePrecontrattualeDialog(true);">
												Materiale precontrattuale obbligatorio
											</span>
										</div>
									<% } %>
								</td>
							</tr>
							<tr>
								<td valign="top">
									<% if(htmlMaterialePrecontrattualeAccessorio.length() > 0){ %>
									<div class="linkAperturaConIcona" style="width:210px;">
										<div><img src="<%=template.getWebApp()%>/images/visualizza_pdf.png"></div>
										<div>
											<span tabindex="-1" onclick="openMaterialePrecontrattualeAccessorioDialog();">
												Materiale precontrattuale accessorio
											</span>
										</div>
									<% } %>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top" colspan="2">
						<table><tr>
							<td style="width:380px;">
								<table cellpadding="0" cellspacing="0"><tr>
									<td class="testo" id="numErrorsMarkers" style="font-size:13px;padding-left:5;display:none;white-space:nowrap;"></td>
									<td class="testo" id="navigateErrorsMarkers" style="font-size:13px;padding-left:5;display:none;">
										<span style="cursor:pointer;text-decoration:underline;" onclick="gotoPriorErrMarker();">Precedente&nbsp;&#9650;</span>
										&nbsp;
										<span style="cursor:pointer;text-decoration:underline;" onclick="gotoNextErrMarker();">Successivo&nbsp;&#9660;</span>
									</td>
								</tr></table>
							</td>
							<td>
								<% if(showMOMCoraFB){ %>
									<table>
										<tr>
											<td class="testo"><b>Valutazione&nbsp;FB:&nbsp;</b></td>
											<td><%=template.field("pdfData_coraFb","onchange='document.dati.pdfData_coraFb.value=this.value;' style='font-family: Segoe UI;font-size: 13px;font-weight: normal;color: #666666;'")%></td>
										</tr>
									</table>
								<% } %>
							</td>
						</tr></table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% }else if(showMOMCoraFB){ %>
	<tr>
		<td align="center">
			<table>
				<tr>
					<td class="testo"><b>Valutazione&nbsp;FB:&nbsp;</b></td>
					<td><%=template.field("pdfData_coraFb","modality='read' style='font-family: Segoe UI;font-size: 13px;font-weight: normal;color: #666666;'")%></td>
				</tr>
			</table>
		</td>
	</tr>
	<% } %>
	
	<tr>
		<td id="pageBody" height="100%" align="center" style="visibility:hidden;">
			<form name="dati" id="dati" method="post" action="call.wfem" style="height:100%; margin:0;">
				<input type="hidden" name="modelHashCode" value="<%=model.hashCode()%>">
				<input type="hidden" name="dataHashCode" value="<%=pdfData.getPdfIndex()%>">
				<input type="hidden" name="wfemCmd" value="">
				<input type="hidden" name="eventName" value="">
				<input type="hidden" name="eventArgs" value="">
				<input type="hidden" name="reloadPageOnEvent" value="true">
				<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
				<input type="hidden" name="scrollYValue" value="">
				<input type="hidden" name="scrollXValue" value="">
				<input type="hidden" name="checkCodDescFields" value="false">
				<input type="hidden" name="manageChangedFields" value="false">
				<div style="display:none;" id="engineGlobalParams">
				<% if(pdfPageDriverJs.length() > 0){ %>
					<input type="hidden" name="pdfData_editableFields" value="<%=model.getPdfData().getEditableFields()%>">
					<input type="hidden" name="pdfData_uneditableFields" value="<%=model.getPdfData().getUneditableFields()%>">
					<input type="hidden" name="pdfData_extraMandatoryFields" value="<%=model.getPdfData().getExtraMandatoryFields()%>">
					<input type="hidden" name="pdfData_hidedFields" value="<%=model.getPdfData().getHidedFields()%>">
				<% } %>
				</div>
				<% if(!model.hasCommandErrors() && model.hasCommandWarnings()){ %>
				<input type="hidden" name="skipCommandWarnings" value="false">
				<% } %>
				<% if(model.isOperatoreMOM()){ %>
				<input type="hidden" name="momEventData_azioneMom" value="">
				<input type="hidden" name="momEventData_readonly" value="">
				<input type="hidden" name="momEventData_valida" value="">
				<input type="hidden" name="momEventData_salva" value="">
				<input type="hidden" name="momEventData_aggiornaDispositiva" value="">
				<input type="hidden" name="momEventData_gotoPdf" value="">
				<input type="hidden" name="momEventData_idPratica" value="">
				<input type="hidden" name="momEventData_confrontaDoppiaSpunta" value="<%=model.getMomEventData().getConfrontaDoppiaSpunta()%>">
				<input type="hidden" name="pdfData_coraFb" value="<%=model.getPdfData().getCoraFb()%>">
				<% } %>
				<% if(model.isPdfOnValidation()){ %>
				<input type="hidden" name="pdfValidationEventData_actionName" value="">
				<input type="hidden" name="pdfValidationEventData_doValidation" value="">
				<input type="hidden" name="pdfValidationEventData_doAdeguatezza" value="">
				<% } %>
				<%PdfHtmlDrawer.drawPdf((java.io.Writer)out,request.getAttribute("BrowserInstance").toString(),template,model,readonlyDataentry);%>				
			</form>
		</td>
	</tr>

	<tr><td style="padding-top: 10;"><div class="line">&nbsp;</div></td></tr>

	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
			<% if(model.isOperatoreMOM()){ %>
				<td style="padding:10;padding-top:3;">
					<table>
						<tr>
							<td><div class="normalButton" onclick="doZoomIn();">Zoom&nbsp;&nbsp;<b>+</b></div></td>
							<td><div class="normalButton" onclick="doZoomOut();">Zoom&nbsp;&nbsp;<b>-</b></div></td>
						</tr>
					</table>
				</td>
				<td align="right" style="padding:10;padding-top:3;">
				</td>
			<% }else{ %>
					<td style="padding:10;padding-top:3;">
						<table>
							<tr>
								<% boolean indietroFatto = false; %>
								<% if(model.getPdfData().getPdfIndex().intValue() == 0){ %>								
									<% if(!indietroFatto && model.mainPdfAnag().getHasPriips().booleanValue()){ 
										indietroFatto=true;
									%>
							    		<td><div class="normalButton whiteButton" onclick="doGobackToPriips();">Indietro</div></td>
									<% }else{ %>
										<% if(Basket.isFirstDispoPdf(model) && !model.getPdfData().getGobackUrl().isNull()){ %>
											<% if(!model.getPdfData().getGobackLabel().isNull()){ %>
									    		<td><div class="normalButton whiteButton" onclick="doBack();"><%=model.getPdfData().getGobackLabel()%></div></td>
											<% }else if(!indietroFatto){ 
												indietroFatto=true;
											%>
									    		<td><div class="normalButton whiteButton" onclick="doBack();">Indietro</div></td>
											<% } %>
								    	<% } %>
							    	<% } %>
						    	
						    	<% } %>
								<% if(!indietroFatto && model.isMultiPdf() && model.getPdfData().getPdfIndex().intValue() > 0){ 
									indietroFatto=true;
								%>
						    		<td><div class="normalButton whiteButton" onclick="doPrevPdf();">Indietro</div></td>
						    	<% } %>
								<% if(!indietroFatto && !Basket.isFirstDispoPdf(model) && model.getPdfData().getPdfIndex().intValue() == 0){ 
									indietroFatto=true;
								%>
									<td><div class="normalButton whiteButton" onclick="doPrevPdf();">Indietro</div></td>
								<% } %>
								<td><div class="normalButton whiteButton" onclick="doZoomIn();">Zoom&nbsp;&nbsp;<b>+</b></div></td>
								<td><div class="normalButton whiteButton" onclick="doZoomOut();">Zoom&nbsp;&nbsp;<b>-</b></div></td>
							</tr>
						</table>
					</td>				
					<% if(model.isTestMode() && model.getProfiloUtente().equals(CostantiPublisher.ProfiliUtente.SVILUPPO)){ %>
						<td class="title" id="positionHelper" align="center"></td>
					<% }else if(model.getIsRete().booleanValue() && pdfData.getPdfStatus().equals(PdfInstanceModel.STATO_BOZZA)){ %>
						<td class="testoPiccolissimo" style="width:100%;vertical-align:middle;padding-bottom:10px;">
							<% if(!pdfData.getCodRuoloImpersonato().isNull() && !pdfData.getCodRuoloImpersonato().equals("FB")){ %>
								Ruolo impersonato: <%=pdfData.getCodRuoloImpersonato()%><br>
							<% } %>
							<%  if(!Basket.getDataOraUltimaModifica(model).isNull()){ %>
								Ultimo aggiornamento: <%=Basket.getDataOraUltimaModifica(model)%><br>
								Effettuato da: <%=Basket.getCodUtenteUltimaModifica(model)%>
							<% } %>
						</td>
					<% } %>
					<td align="right" style="padding:10;padding-top:3;">
						<% if(!readonly){ %>
						<table>
							<tr>
								<% if(model.isTestMode()){ %>
							    	<td><div class="normalButton" onclick="doConferma();">Avanti</div></td>
									<td><div class="normalButton" onclick="doShowdata();">Dati</div></td>
								<% }else{ %>
							    	<% if(mostraSalva){ %>
								    	<td><div class="normalButton" onclick="doSalva();">Salva</div></td>
								    <% } %>
							    	<td><div class="normalButton" onclick="doConferma();">Avanti</div></td>
								<% } %>
							</tr>
						</table>
						<% } %>
					</td>
				<% } %>
				</tr>
			</table>
		</td>
	</tr>

</table>
</center>

<% if(model.isOperatoreMOM()){ %>
	<% if(!model.getMomEventData().getAzioneMom().isNull()){ %>
		<script>
		<%=CallMomEvent.drawJsonCallbackObject(template, model)%>
		callMomCallbackEvent(data);
		<% 
			model.getMomEventData().setAzioneMom(new StringType());
			model.setModality(Template.INSERT_MODALITY);
		%>
		</script>
	<% } %>
<% }else{ %>
	<% if(model.getModality() != Template.READ_MODALITY || model.getPdfValidationEventData().isOnEventCall()){ %>
		<% if(model.hasCommandErrors() || model.hasCommandWarnings() || model.hasCommandMessages()){ %>
		<div id="errorReportMessage">
			<table height="100%" width="100%">
				<tr>
					<td height="100%">
						<div style="height:100%;width:100%;position:relative;overflow:auto;">
							<div style="position:absolute;left:0;top:0;">
								<table>
								<% if(model.hasCommandErrors()){ %>
									<% for(int i=0;i<model.getCommandErrors().size();i++){ %>
										<tr><td><%=template.getProperty((CommandError)model.getCommandErrors().get(i))%></td></tr>
									<% } %>
								<% }else if(model.hasCommandWarnings()){ %>
									<% for(int i=0;i<model.getCommandWarnings().size();i++){ %>
										<tr><td><%=template.getProperty((CommandWarning)model.getCommandWarnings().get(i))%></td></tr>
									<% } %>
								<% }else if(model.hasCommandMessages()){ %>
									<% for(int i=0;i<model.getCommandMessages().size();i++){ %>
										<tr><td><%=template.getProperty((CommandMessage)model.getCommandMessages().get(i))%></td></tr>
									<% } %>
								<% } %>
								</table>
							</div>
						</div>
					</td>
				</tr>
				<% if(!model.isPdfOnValidation() && !model.hasCommandErrors() && model.hasCommandWarnings()){ %>
				<tr>
					<td align="center">
						<table><tr>
							<td><div class="normalButton whiteButton" onclick="$('#errorReportMessage').dialog('destroy');">Annulla</div></td>
							<td><div class="normalButton" onclick="doProsegui();">Prosegui</div></td>
						</tr></table>
					</td>
				</tr>
				<% } %>
			</table>
		</div>
		<script>
			$("#errorReportMessage").dialog({
				autoOpen: true, 
				modal: true,
				width: 550,
				height: 350,
				closeOnEscape: false,
				title: "Avviso",
				closeText: "Chiudi"
			   });
		</script>
		<% } %>
	<% } %>
<% } %>

<% if(model.getPdfValidationEventData().isOnEventCall()){ %>
	<script>
	<%=CallPdfValidationEvent.drawJsonCallbackObject(template,model)%>
	callValidationCallbackEvent(data);
	<% 
		model.getPdfValidationEventData().setOnEventCall(false);
	%>
	</script>
<% } %>

<% model.resetCommandErrors(); %>
<% model.resetCommandWarnings(); %>
<% model.resetCommandMessages(); %>
<% model.setSkipCommandWarnings(new BooleanType(false)); %>


<% if(htmlMaterialePrecontrattuale.length() > 0){ %>
	<%@ include file="./MaterialePrecontrattuale.html"%>
<% } %>
<% if(htmlMaterialePrecontrattualeAccessorio.length() > 0){ %>
	<%@ include file="./MaterialePrecontrattualeAccessorio.html"%>
<% } %>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>

