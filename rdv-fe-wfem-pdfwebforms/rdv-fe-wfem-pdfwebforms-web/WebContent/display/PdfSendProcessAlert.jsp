<%@page import="prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse"%>
<%@page import="prgm.pdfwebforms.drivers.PdfDriverCaller"%>
<%@page import="prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse"%>
<%@page import="prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
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
	int numCombo = 0;
	if((!model.getIsSede().booleanValue() && !model.getPdfData().getIsVolatile().booleanValue()) || model.isTestMode()){
		for(int i=0;i<model.getPdfAnags().size();i++){
			PdfAnagModel anag = model.getPdfAnags().get(i);
			if(anag.getPdfCodOperazionePrit().intValue() == PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE)
				numCombo++;
		}
	}

	try{
		ProvidePritDataResponse pritData = PdfDriverCaller.callProvidePritData(model.getUserSessionContext().getClientSessionContext(), model);
		if(pritData != null && !pritData.isIncludiRigheConfigurate()) // Se le righe vengono gestite dal driver non gestisco le operazioni multiple
			numCombo=0;
	}catch(Throwable t){
		numCombo=0;
	}
	
	// Se siamo in un basket non gestiamo le multioperazioni
	if(model.isInBasket())
		numCombo=0;
	
	// Il link alla raccomandazione lo mostriamo solo se il ramo IDD è PREVIDENZA ed è stata generata
	boolean isRamoIddPrevidenza = model.getIddCallModel() != null && 
						   		  model.getIddCallModel().getInput() != null && 
						   		  model.getIddCallModel().getInput().getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA;
	boolean showLinkRaccomandazioneIdd = isRamoIddPrevidenza && !model.getIddCallModel().getIdRaccomandazioneIdd().isNull();
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Fonts.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>

<script src="<%=template.getWebApp()%>/display/PdfAccessibility.js"></script>
<% if(showLinkRaccomandazioneIdd){ %>
<script src="<%=template.getWebApp()%>/display/OpenRaccomandazioneIdd.js"></script>
<% } %>
<script>
var numPdf = <%=model.getPdfAnags().size()%>;
function selectOpe(){
	$("#okButton").removeClass("normalButtonDisab");
	for(var i=0;i<numPdf;i++){
		if(document.getElementById("listaCodiciOperazionePritPerMultioperazione"+i+"_pdfCodOperazionePrit").value == ""){
			$("#okButton").addClass("normalButtonDisab");
			return;			
		}
	}
}
function doOk(but){
	if($(but).hasClass("normalButtonDisab"))
		return;
	
	var linkRaccomandazioneIdd = document.getElementById("linkRaccomandazioneIdd");
	if(linkRaccomandazioneIdd != null){
		if(linkRaccomandazioneIdd.getAttribute("isRaccomandazioneIddClicked") != "true"){
			alert("Per proseguire è necessario prima prendere visione della Raccomandazione Personalizzata");
			return;
		}
	}
	
	var ret = new Object();
	var codiciOpe = "";
	for(var i=0;i<numPdf;i++){
		var combo = document.getElementById("listaCodiciOperazionePritPerMultioperazione"+i+"_pdfCodOperazionePrit");
		if(combo == null)
			break;
		var codOpe = combo.value;
		codiciOpe += codOpe+",";
	}
	if(codiciOpe != "")
		codiciOpe = codiciOpe.substring(0,codiciOpe.length-1);
	ret.codiciOperazionePritPerMultioperazione = codiciOpe;
	closeModalPopup(ret);	
}
function doCancel(){
	closeModalPopup(null);	
}

$( document ).ready(function() {
	
	$(".normalButton").attr({"tabindex":"0", "role":"button"});
	
	try{
		$("[onclick]").bind({
			keypress: function(event){
				if(event.which == 13 || event.which == 32){
					this.click();
				}
			}			
		});
	}catch(e){}

});
</script>
</head>

<body>

<table width="100%" height="100%">
	<tr>
		<td height="100%" valign="top">
			<% String fineMsg = "Confermi di voler procedere?"; %>
			<% if(numCombo == 0){ %>
				<table class="testo" width="100%" cellpadding="0" cellspacing="0">
					<tr><td><%=model.getMessaggioPdfAlert()%></td></tr>
					<tr><td>Confermi di voler procedere?</td></tr>
				</table>
			<% }else{ %>
				<table class="testo" width="100%" height="100%" cellpadding="0" cellspacing="0">
					<tr><td><%=model.getMessaggioPdfAlert()%></td></tr>
					<tr>
						<td style="padding-bottom: 10;">
							<% if(numCombo > 1){ %>
								Per poter confermare seleziona i codici operazione dei seguenti moduli:
							<% }else{ %>
								Per poter confermare seleziona il codice operazione del modulo:
							<% } %>
						</td>
					</tr>
					<tr>
						<td height="100%">
							<div style="position:relative;height:100%;width:100%;margin:0 auto;overflow:auto;">
							<table width="100%" class="testo" cellpadding="0" cellspacing="0">
								<% for(int i=0;i<model.getPdfAnags().size();i++){
									PdfAnagModel anag = model.getPdfAnags().get(i);%>
									<% if(anag.getPdfCodOperazionePrit().intValue() == PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE){ %>
										<tr>
											<td style="font-size: 11px;"><%=anag.getPdfDescr()%></td>
										</tr>
										<tr>
											<td><%=template.field("listaCodiciOperazionePritPerMultioperazione"+i+"_pdfCodOperazionePrit","style='width:100%;' onchange='selectOpe();'")%></td>
										</tr>
									<% }else{ %>
										<tr>
											<td>
												<input type="hidden" value="asis" id="listaCodiciOperazionePritPerMultioperazione<%=i%>_pdfCodOperazionePrit">
											</td>
										</tr>
									<% } %>
								<% } %>
							</table>
							</div>
						</td>
					</tr>
				</table>
			<% } %>
		</td>
	</tr>
	<% if(showLinkRaccomandazioneIdd){ %>
		<tr>
			<td>
				<table>
					<tr>
						<td class="testo" style="padding-bottom:30;">
							<span role="link" id="linkRaccomandazioneIdd" onclick="openRaccomandazioneIdd(this);" 
								  tabindex="0"
								  style="text-decoration:underline;cursor:pointer;"
								  isRaccomandazioneIddClicked="false">
								<u>Raccomandazione personalizzata e Modulo Unico Precontrattuale (Mup) per prodotti assicurativi IVASS n.40/2018</u>
							</span>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	<% } %>
	<tr>
		<td align="center" style="padding-top:5;">
			<table>
				<tr>
					<td><div id="cancelButton" class="normalButton whiteButton" style="width:80px;" onclick="doCancel();">Annulla</div></td>
					<td id="buttonSpace">&nbsp;&nbsp;</td>
					<td><div id="okButton" class="normalButton<%=(numCombo>0?" normalButtonDisab":"")%>" style="width:80px;" onclick="doOk(this);">Ok</div></td>
				</tr>
			</table>
		</td>
  	</tr>
</table>
</body>
</html>
<% model.setMessaggioPdfAlert(null); %>
