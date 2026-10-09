<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="prgm.pdfwebforms.sostituzioni.*"%>
<%@page import="prgm.pdfwebforms.basket.Basket"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	PdfDataModel pdfData = model.getPdfData();
	PreferenzeClienteSostituzioniModel pref = model.getPreferenzeClienteSostituzioni();
	
 	String closeBrowserInstances = "";
	if(!model.getPdfData().getIsOnSameStack().booleanValue())
		closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");
	
	boolean hasMaterialeInPriips = model.mainPdfAnag().getTipoPriips().equals(PdfAnagModel.TIPO_PRIIPS_SEMPLICE) || model.mainPdfAnag().getTipoPriips().equals(PdfAnagModel.TIPO_PRIIPS_PREVIDENZA); 
	boolean hasPrevidenzaInPriips = model.mainPdfAnag().getTipoPriips().equals(PdfAnagModel.TIPO_PRIIPS_PREVIDENZA); 
	boolean hasPreferenzeInPriips = model.mainPdfAnag().getHasPreferenzeInPriips().booleanValue(); 
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
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Fonts.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Dialog.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfAccessibility.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfPriipsPage.js"></script>
<script>
var jsHasMaterialeInPriips = <%=hasMaterialeInPriips%>;
var jsHasPrevidenzaInPriips = <%=hasPrevidenzaInPriips%>;
var jsHasPreferenzeInPriips = <%=hasPreferenzeInPriips%>;

function doBack(){
	<% if(!model.getPdfData().getGobackUrl().isNull()){ %>
		wait();
		location.href="<%=model.getPdfData().getGobackUrl()%>";
	<% } %>
	return false;
}
</script>
</head>

<body>

<center>
<table class="htmlContainer">

	<tr>
		<td style="padding:10;">
			<table width="100%">
				<tr>
					<td valign="top">
						<table class="titolo">
							<% if(hasMaterialeInPriips && hasPreferenzeInPriips){ %>
								<tr><td>Dichiarazione consegna materiale precontrattuale e scelta delle preferenze cliente</td></tr>
							<% }else if(hasMaterialeInPriips){ %>
								<tr><td>Dichiarazione consegna materiale precontrattuale</td></tr>
							<% }else if(hasPreferenzeInPriips){ %>
								<tr><td>Scelta delle preferenze cliente</td></tr>
							<% } %>

							<% if(hasPrevidenzaInPriips){ %>
								<tr><td>Informazioni aggiuntive sulle richieste ed esigenze dell'aderente</td></tr>
							<% } %>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table class="testo">
							<tr>
								<td>
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
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td height="100%" align="center">
			<div style="height:100%;width:100%;position:relative;overflow-y:auto;">
			<form name="dati" id="dati" method="post" action="call.wfem" style="height:100%; margin:0;">
				<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoOnPriips.execute">
				<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
				<input type="hidden" name="checkCodDescFields" value="false">
				<input type="hidden" name="manageChangedFields" value="false">
				<input type="hidden" name="resetErrors" value="false">
				<input type="hidden" name="resetWarnings" value="false">
				<input type="hidden" name="resetMessages" value="false">
				<table class="testo" width="80%">
				
					<tr><td align="center" style="padding-top:20px;"><img src="<%=template.getWebApp()%>/images/logoMediolanum.png"></td></tr>
										
					<% if(hasMaterialeInPriips){ %>
						<tr><td class="testoAzzurro" style="padding-top:40px;">DICHIARAZIONE CONSEGNA MATERIALE PRECONTRATTUALE</td></tr>
						<tr><td style="font-weight: bold;padding-top:15px;">Dichiaro di aver consegnato al cliente, prima della sottoscrizione, copia dei documenti nel formato dallo stesso richiesto, sotto indicato:</td></tr>
						<tr>
							<td align="center">
								<table class="testo">
									<tr>
										<td valign="top">
											<input type="radio" name="pdfData_priipsTipoSupportoMaterialeContrattuale" value="<%=PdfDataModel.PRIIPS_SUPPORTO_CARTACEO%>"
													<%if(pdfData.getPriipsTipoSupportoMaterialeContrattuale().equals(PdfDataModel.PRIIPS_SUPPORTO_CARTACEO)){%>checked<%}%>>
										</td>
										<td>
											su supporto cartaceo
										</td>
									</tr>
									<tr>
										<td valign="top">
											<input type="radio" name="pdfData_priipsTipoSupportoMaterialeContrattuale" value="<%=PdfDataModel.PRIIPS_SUPPORTO_NON_CARTACEO%>"
													<%if(pdfData.getPriipsTipoSupportoMaterialeContrattuale().equals(PdfDataModel.PRIIPS_SUPPORTO_NON_CARTACEO)){%>checked<%}%>>
										</td>
										<td>
											in formato elettronico pdf, archiviato in un supporto duraturo (es. CD-ROM, pen-drive, ecc).
											Il cliente dichiara a tal fine di disporre di adeguati strumenti tecnici e conoscenze che consentano di consultare e 
											gestire autonomamente i documenti in formato file elettronico PDF archiviato su supporto duraturo
										</td>
									</tr>
								</table>
							</td>
						</tr>
					<% } %>
					
					<% if(hasPrevidenzaInPriips){ %>
						<tr><td class="testoAzzurro" style="padding-top:30px;">INFORMAZIONI AGGIUNTIVE SULLE RICHIESTE ED ESIGENZE DELL'ADERENTE</td></tr>
						<tr>
							<td style="padding-top:10px;">
								Le presenti informazioni aggiuntive hanno lo scopo di acquisire nell'interesse del cliente alcune ulteriori informazioni utili
								per completare la valutazione di adeguatezza e congruità del contratto che intende sottoscrivere rispetto ai suoi bisogni assicurativi. 
							</td>
						</tr>
						<tr><td style="padding-top:20px;">Informazioni sulle aspettative in relazione al contratto:</td></tr>
						<tr>
							<td style="padding-left:10px;">
								<table class="testo"><tr>
									<td>Tolleranza alla volatilità e aspettativa di rendimento</td>
									<td><%=template.field("pdfData_priipsTolleranzaVolatilita","style='font-size:16px;color:#333333;'") %></td>
									<td style="width:30px;"></td>
									<td>Orizzonte temporale indicativo</td>
									<td><%=template.field("pdfData_priipsOrizzonteTemporale","style='font-size:16px;color:#333333;'") %></td>
								</tr></table>
							</td>
						</tr>
					<% } %>

					<% if(hasPreferenzeInPriips){ %>
						<tr><td class="testoAzzurro" style="padding-top:30px;">Preferenza Cliente</td></tr>
						<% if(!pref.getResultCode().equals("0")){ %>
							<tr><td style="font-weight: bold;padding-top:15px; color:red;"><%=pref.getResultDescription()%></td></tr>
						<% }else{ %>
							<% for(int i=0;i<pref.getElencoPreferenze().size();i++){ %>
								<% PreferenzaClienteSostituzioniModel p = (PreferenzaClienteSostituzioniModel)pref.getElencoPreferenze().get(i); %>
									<tr><td>
										<table class="testo"><tr>
											<td><%=template.field("preferenzeClienteSostituzioni_elencoPreferenze"+i+"_isSelezionata")%></td>
											<td><%=p.getDescrizione()%></td>
										</tr></table>
									</td></tr>
							<% } %>
						<% } %>
					<% } %>
					
				</table>
			</form>
			</div>
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
								<% if(Basket.isFirstDispoPdf(model) && !model.getPdfData().getGobackUrl().isNull() && model.getPdfData().getPdfIndex().intValue() == 0){ %>
									<% if(!model.getPdfData().getGobackLabel().isNull()){ %>
							    		<td><div class="normalButton whiteButton" onclick="doBack();"><%=model.getPdfData().getGobackLabel()%></div></td>
									<% } %>
						    	<% } %>
								<% if(!Basket.isFirstDispoPdf(model) && model.getPdfData().getPdfIndex().intValue() == 0){ %>
									<td><div class="normalButton whiteButton" onclick="doPrevPdf();">Indietro</div></td>
								<% } %>
						    	<td></td>
							</tr>
						</table>
					</td>				
					<% if(model.getIsRete().booleanValue() && pdfData.getPdfStatus().equals(PdfInstanceModel.STATO_BOZZA) && !Basket.getDataOraUltimaModifica(model).isNull()){ %>
						<td class="testoPiccolissimo" style="color:#192D6E;width:100%;vertical-align:middle;padding-bottom:10px;">
							<% if(!pdfData.getCodRuoloImpersonato().isNull() && !pdfData.getCodRuoloImpersonato().equals("FB")){ %>
								Ruolo impersonato: <%=pdfData.getCodRuoloImpersonato()%><br>
							<% } %>
							Ultimo aggiornamento: <%=Basket.getDataOraUltimaModifica(model)%><br>
							Effettuato da: <%=Basket.getCodUtenteUltimaModifica(model)%>
						</td>
					<% } %>
					<td align="right" style="padding:10;padding-top:3;">
						<table>
							<tr>
						    	<td><div class="normalButton" onclick="doContinua();">Continua</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

</table>
</center>

<div id="errorReportMessage" style="display:none;">
	<table height="100%" width="100%">
		<tr>
			<td valign="top" height="100%" style="padding: 10;">
				<table>
					<tr>
						<td class="testo" id="errorMsg"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center">
				<table><tr>
					<td><div class="normalButton" onclick="$('#errorReportMessage').dialog('destroy');">Ok</div></td>
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

