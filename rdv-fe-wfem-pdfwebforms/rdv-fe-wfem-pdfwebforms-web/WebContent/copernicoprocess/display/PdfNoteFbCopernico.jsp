<%@page import="prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse"%>
<%@page import="prgm.pdfwebforms.reportadeguatezza.Costanti"%>
<%@page import="prgm.pdfwebforms.signprocess.common.SignUtility"%>
<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="prgm.pdfwebforms.model.PdfPersonSignDataModel"%>
<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();

	boolean hasReportAdeguatezza = model.getIdReportAdeguatezza().length() > 0 && !model.reportAdeguatezzaPassatoDalChiamate();

	// Il link alla raccomandazione lo mostriamo solo se il ramo IDD è PREVIDENZA ed è stata generata
	boolean isRamoIddPrevidenza = model.getIddCallModel() != null && 
						   		  model.getIddCallModel().getInput() != null && 
						   		  model.getIddCallModel().getInput().getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA;
	boolean showLinkRaccomandazioneIdd = isRamoIddPrevidenza && !model.getIddCallModel().getIdRaccomandazioneIdd().isNull();
	
	String linkReportAdeguatezzaTextEND = "Report di Adeguatezza"; 
	String linkReportAdeguatezzaTextWAIT = "Report di Adeguatezza in elaborazione";
	String linkReportAdeguatezzaTextERROR = Costanti.MESSAGGIO_ERRORE;
	
	boolean notaEditabile = model.getPdfNoteFBCopernico().isEditable();
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
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageFields.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPageDataentryUtil.js"></script>
<script>
function doGoBack(){
	wait();
	document.goBackForm.submit();
	return false;
}
function doConferma(){
	
	var linkReportAdeguatezza = document.getElementById("linkReportAdeguatezza");
	if(linkReportAdeguatezza != null){
		var statoReport = document.getElementById("linkReportAdeguatezza").getAttribute("statoReportAdeguatezza");
		if(statoReport == "WAIT"){
			showAlertErroreReportAdeguatezza("Attendere il completamento dell'elaborazione del Report di Adeguatezza");
			return false;
		}else if(statoReport == "ERROR"){
			showAlertErroreReportAdeguatezza($("#linkReportAdeguatezza").html());
			return false;
		}
	}
	
	wait();
	document.dati.submit();
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

<% if(hasReportAdeguatezza){ %>
	<script>
	var linkReportAdeguatezzaTextEND = "<%=linkReportAdeguatezzaTextEND%>"; 
	var linkReportAdeguatezzaTextWAIT = "<%=linkReportAdeguatezzaTextWAIT%>";
	var linkReportAdeguatezzaTextERROR = "<%=linkReportAdeguatezzaTextERROR%>";
	var linksReportAdeguatezza = <%=model.writeLinksReportAdeguatezza()%>;
	var jsIdReportAdeguatezza = "<%=model.getIdReportAdeguatezza()%>"
	var maxNumRetryReportAdeguatezza = <%=model.getMaxRecuperaReportAdeguatezzaRetryCount()%>;
	</script>
	<script src="<%=template.getWebApp()%>/display/OpenReportsAdeguatezza.js"></script>
	<script src="<%=template.getWebApp()%>/copernicoprocess/display/ReportAdeguatezza.js"></script>
	<% if(model.getStatoRecueroReportAdeguatezza().equals("WAIT")){ %>
		<script>callRefreshReportAdeguatezza();</script>
	<% } %>
<% } %>
<% if(showLinkRaccomandazioneIdd){ %>
<script src="<%=template.getWebApp()%>/display/OpenRaccomandazioneIdd.js"></script>
<% } %>
</head>

<body>

<form name="erroreReportAdeguatezza" id="erroreReportAdeguatezza" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.reportadeguatezza.PdfErrorsOnReportAdeguatezza.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="scrollYValue" value="">
<input type="hidden" name="scrollXValue" value="">
</form>

<form name="goBackForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoBackOnError.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="dati" id="dati" method="post" action="call.wfem">
<% if(model.isInBasket()){ %>
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.copernicoprocess.business.GoOnCopernicoBasketProcess.execute">
<% }else{ %>
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.copernicoprocess.business.GoOnCopernicoProcess.execute">
<% } %>
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="scrollYValue" value="">
<input type="hidden" name="scrollXValue" value="">

<center>
<table class="htmlContainer" style="height:auto;">

	<tr>
		<td style="padding:10;" valign="top">
			<table width="100%">
			    <tr>
			    	<td valign="top">
						<table width="100%">
							<tr><td class="titolo" style="padding-left:10;">Invia la proposta con Copernico</td></tr>
							<% if(model.getMessaggioCopernico() != null && !model.getMessaggioCopernico().isEmpty()){ %>
								<tr><td class="testo" style="font-size: 13px;padding-left:10;"><%=model.getMessaggioCopernico()%></td></tr>
							<% } %>
							<tr>
								<td style="padding-left:10;">
									<table width="100%" style="table-layout: fixed;">
										<tr>
											<td class="testo" valign="bottom">
												<% if(notaEditabile){ %>
													Aggiungi il testo di presentazione della proposta
												<% } %>
											</td>
											<td>
											<% if(hasReportAdeguatezza){ %>
												<%
													String linkReportAdeguatezzaText = linkReportAdeguatezzaTextEND; 
													if(model.getStatoRecueroReportAdeguatezza().equals("WAIT"))
														linkReportAdeguatezzaText = linkReportAdeguatezzaTextWAIT;
													else if(model.getStatoRecueroReportAdeguatezza().equals("ERROR"))
														linkReportAdeguatezzaText = linkReportAdeguatezzaTextERROR;
													String linkReportAdeguatezzaStyle = model.getStatoRecueroReportAdeguatezza().equals("WAIT") ? "": "style='text-decoration:underline;cursor:pointer;'";
												%>
												<div class="linkAperturaConIcona">
													<div><img src="<%=template.getWebApp()%>/images/visualizza_pdf.png"></div>
													<div>
														<span role="link" tabindex="0" id="linkReportAdeguatezza" onclick="openReportAdeguatezza(this);"
														  <%=linkReportAdeguatezzaStyle%> 
														  statoReportAdeguatezza="<%=model.getStatoRecueroReportAdeguatezza()%>" 
														  isReportAdeguatezzaClicked="<%=model.isReportAdeguatezzaClicked()%>">
														  <%=linkReportAdeguatezzaText%>
														 </span>
													</div>
												</div>
											<% } %>
											<% if(showLinkRaccomandazioneIdd){ %>
												<div class="linkAperturaConIcona">
													<div><img src="<%=template.getWebApp()%>/images/visualizza_pdf.png"></div>
													<div>
														<span role="link" tabindex="0" id="linkRaccomandazioneIdd" onclick="openRaccomandazioneIdd(this);"
														  isRaccomandazioneIddClicked="<%=model.isRaccomandazioneIddClicked()%>">
															Raccomandazione personalizzata e Modulo Unico Precontrattuale (Mup) per prodotti assicurativi IVASS n.40/2018
														</span>
													</div>
												</div>
											<% } %>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td><%=template.field("pdfNoteFBCopernico","style='width:100%;font-size:12pt;color:#666666;' rows='15' uppercase='false'"+(notaEditabile?"":" modality='read'"))%></td></tr>
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
					    		<td><div class="normalButton whiteButton" onclick="doGoBack();">Indietro</div></td>
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
						<table>
							<tr>
						    	<td><div class="normalButton" onclick="doConferma();">Conferma</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

</table>
</center>
</form>

<div id="errorReportAdeguatezzaMessage" style="display:none;">
	<table height="100%" width="100%">
		<tr>
			<td valign="top" height="100%" style="padding: 10;">
				<table>
					<tr>
						<td class="title" id="errorReportAdeguatezzaMessageText"></td>
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

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>