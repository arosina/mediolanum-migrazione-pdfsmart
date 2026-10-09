<%@page import="prgm.pdfwebforms.publisher.model.PdfModuloModel"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="com.atosorigin.wfem.types.ListType"%>
<%@page import="prgm.pdfwebforms.catalog.PdfCatalogModel"%>
<%@page import="com.atosorigin.wfem.types.AbstractTypePropertyDescriptor"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfCatalog");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setJSCombo(false);
	
	PdfCatalogModel model = (PdfCatalogModel)template.getPageDataModel();
	PdfAnagModel 	pdfAnag = model.getDettaglioModulo();
	PdfModuloModel 	pdfModulo = pdfAnag.getPdfModulo();
%>

<html>

<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/catalog/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/catalog/style/wfemStyle.css'/>
<script>
$( document ).ready(function() {
	try{
		$(".normalButton").bind({
			click: function(event){
				$(this).addClass("normalButtonSel");
			}
		}).mouseout(
			function(){ $(this).removeClass("normalButtonSel"); }
		).mouseenter(
			function(){ $(this).addClass("normalButtonSel"); }
		);
	}catch(e){}
});
</script>
</head>

<body>

<table cellpadding="0" cellspacing="0" height="100%" width="100%">

	<tr>
		<td height="100%">
			<div style="height:100%;width:100%;position:relative;overflow:auto;">
				<div style="position:absolute;left:0;top:0;">
					<table class="text" cellpadding="0" cellspacing="0" height="100%" width="100%" style="table-layout: fixed;">
						<tr>
							<td style="color:#4a97cd;font-size:14pt;background-color:#f2f1ef;height:40;padding-left:10;">
								Modulo&nbsp;<%=pdfAnag.getPdfCode()%>&nbsp;<%=(pdfAnag.getPdfEdition().isNull()?"":"("+pdfAnag.getPdfEdition()+")")%>
							</td>
						</tr>
						<tr><td style="height:10;"></td></tr>
						<tr>
							<td style="padding-left:10;">
								<b>Tipo di modulo:</b> <%=pdfModulo.getTipoModulo()%><br>
								<b>Segmento:</b> <%=pdfModulo.getSegmento()%><br><br>
								Valido dal: <%=pdfModulo.getDataInizioValidita()%>
							</td>
						</tr>
						
						<% if(pdfAnag.getPdfIsStampaEnabled().booleanValue() || pdfAnag.getPdfIsCartaLiberaEnabled().booleanValue() ||
							  pdfAnag.getPdfIsCartaChimicaEnabled().booleanValue() || pdfAnag.getPdfIsFirmaDigitaleEnabled().booleanValue()){ %>
							<tr><td style="height:10;"></td></tr>
							<tr>
								<td style="color:white;font-size:14pt;background-color:#4a97cd;height:30;padding-left:10;">
									Modalità di compilazione e invio
								</td>
							</tr>
							<% if(pdfAnag.getPdfIsStampaEnabled().booleanValue()){ %>
								<tr>
									<td style="padding-top:10;padding-left:10;">
										<b>Stampa</b><br>
										Al click sul tasto stampa verrà aperto il modulo con i campi compilati. Per perfezionare
										l'operazione occorre verificare la correttezza dei dati riportati, stampare il modulo ed 
										eventualmente completare la compilazione a mano, farlo firmare, inserire la riga di prit 
										e spedirlo in sede.
									</td>
								</tr>
							<% } %>
							<% if(pdfAnag.getPdfIsCartaLiberaEnabled().booleanValue() || pdfAnag.getPdfIsCartaChimicaEnabled().booleanValue()){ %>
								<tr>
									<td style="padding-top:10;padding-left:10;">
										<b>Invia in sede</b><br>
										<% if(pdfAnag.getPdfCodProdottoPrit().isNull() || pdfAnag.getPdfCodOperazionePrit().isNull()){ %>
											Al click sul pulsante "Invia in sede" i dati saranno inviati in sede. 
											Per perfezionare l'operazione occorre, successivamente, stampare il modulo 
											cliccando sul tasto "stampa", farlo firmare, inserire la riga di prit e spedirlo in sede.
										<% }else{ %>
											Al click sul pulsante "Invia in sede" la riga di prit verrà automaticamente caricata 
											e i dati saranno inviati in sede.
											Per perfezionare l'operazione occorre, successivamente, stampare il modulo cliccando 
											sul tasto "stampa", farlo firmare e spedirlo in sede.
										<% } %>
									</td>
								</tr>
							<% } %>
							<% if(pdfAnag.getPdfIsFirmaDigitaleEnabled().booleanValue()){ %>
								<tr>
									<td style="padding-top:10;padding-left:10;">
										<b>Firma digitale</b><br>
										Al click sul tasto "Firma" i dati e il modulo vengono inviati in sede e si procede con il
										processo di Firma Digitale. In sede non deve essere spedito alcun documento.
									</td>
								</tr>
							<% } %>
							<% if(pdfAnag.getPdfIsCopernicoEnabled().booleanValue()){ %>
								<tr>
									<td style="padding-top:10;padding-left:10;padding-right:3;">
										<b>Copernico</b><br>
										Al click sul tasto "Copernico" si procede con l'inserimento del messaggio che comparirà
										nell'Area Personale del Cliente insieme alla proposta inviata. Tutti i contratti inviati
										saranno reperibili in Customer Project nella sezione dedicata alle "Proposte Copernico"
									</td>
								</tr>
							<% } %>
							<tr><td style="padding-top:8;"><div style="height:1px;line-height:1px;font-size:1px;background-color:#cccccc;">&nbsp;</div></td></tr>							
						<% } %>
						
						<% if(pdfAnag.getPdfApplReferences().size() > 0 || !pdfModulo.getNoteOperative().isNull()){ %>
							<tr><td style="height:10;"></td></tr>
						<% } %>
						<% if(pdfAnag.getPdfApplReferences().size() > 0){ %>
							<tr>
								<td style="padding-left:10;">
									<b>Il modulo è compilabile da:&nbsp;</b><%=pdfAnag.htmlApplReferences().toString().replaceAll("\\n","<br>")%>
								</td>
							</tr>
						<% } %>
						<% if(!pdfModulo.getNoteOperative().isNull()){ %>
							<tr>
								<td style="padding-left:10;">
									<table class="text" cellpadding="0" cellspacing="0">
										<tr><td valign="top"><b>Note operative:&nbsp;</b></td><td><%=pdfModulo.getNoteOperative().toString().replaceAll("\\n","<br>")%></td></tr>
									</table>
								</td>
							</tr>
						<% } %>
						
						<% if(pdfModulo.getProdottiModulo().size() > 0){ %>
							<tr><td style="height:10;"></td></tr>
							<tr>
								<td style="padding-left:10;">
									<%=pdfModulo.htmlProdotti()%>
								</td>
							</tr>
						<% } %>
					</table>
				</div>
			</div>
		</td>
	</tr>
	<tr><td style="height:5;"></td></tr>
	<tr>
		<td align="center">
			<table><tr><td><div class="normalButton" onclick="closeModalPopup();">Chiudi</div></td></tr></table>
		</td>
	</tr>

</table>

<%=template.getFooter()%>
</body>
</html>
