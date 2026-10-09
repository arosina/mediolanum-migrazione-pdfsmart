<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.publisher.common.CostantiPublisher"%>
<%@page import="prgm.pdfwebforms.publisher.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfConfiguration");
	template.setLabelAlign("right");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setJSCombo(false);
	
	PdfConfigurationModel confModel = (PdfConfigurationModel)template.getPageDataModel();
	PdfAnagModel pdfAnag = confModel.getPdfAnag();
	PdfModuloModel pdfModulo = pdfAnag.getPdfModulo();
	boolean isConPubblicazioni = confModel.getPdfPubblicationList().size() > 0 ? true : false;
	String areaDescr = pdfAnag.getDescValue("pdfArea");
	if(areaDescr == null || areaDescr.length() == 0)
		areaDescr = pdfAnag.getPdfArea().toString();
	
	String pdfIdAsString = "";
	if(!pdfAnag.getPdfId().isNull()){
		pdfIdAsString = "<i>(id: "+Tools.unFillSx(pdfAnag.getPdfId().toString(), '0')+")</i>";
	}
%>

<html>

<head>
<style>
form{
	margin: 0;
}
</style>
<%=template.getHeader()%> 
<script src="<%=template.getWebApp()%>/publisher/display/PdfConfiguration.js"></script>
<script>var jsProfiloUtente = "<%=confModel.getProfiloUtente()%>";</script>
</head>

<body>

<form name="datireadForm" style="display:none;">
<input type="hidden" name="pdfId" value="<%=pdfAnag.getPdfId()%>">
<input type="hidden" name="pdfCode" value="<%=pdfAnag.getPdfCode()%>">
<input type="hidden" name="pdfPublicationId" value="<%=pdfAnag.getPdfPublicationId()%>">
<input type="hidden" name="pdfOriginalPubId" value="<%=pdfAnag.getPdfOriginalPubId()%>">
<input type="hidden" name="pdfPublishTime" value="<%=pdfAnag.getPdfPublishTime()%>">
<input type="hidden" name="isWorkingAreaHidden" value="<%=confModel.isWorkingAreaHidden()%>">
<input type="hidden" name="pdfNumPages" value="<%=confModel.getPdfAnag().getPdfNumPages()%>">
</form>

<form name="gobackForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="executeLastDisplay">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="selezionaPubblicazioneForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.EditaPubblicazione.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfAnag_pdfPublicationId" value="">
</form>

<form name="eliminaPubblicazioneForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.EliminaPubblicazione.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfAnag_pdfPublicationId" value="">
<input type="hidden" name="pdfAnag_pdfMomVersion" value="">
</form>

<form name="archiviaPubblicazioneForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.ArchiviaPubblicazione.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfAnag_pdfPublicationIdForArch" value="">
</form>

<form name="ripristinaPubblicazioneForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.RipristinaPubblicazione.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfAnag_pdfPublicationIdForArch" value="">
</form>

<form name="annullaModificheForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.AnnullaModificheConf.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<table width="100%">

<tr>
    <td>
		<table width="100%" class="toolbar" cellpadding="0" cellspacing="2">
		  <tr>
		     <td style="padding-left:10;">
		     	<div style="position:relative;z-index:10;">
		     		<div id="datiModuloCont" style="position:absolute;left:0;top:50;display:none;">
							<table class="text" style="background-color:white;border:solid 1px silver;width:400;">
								<tr><td colspan="2" align="right"><span id="datiModuloContCloser" style="cursor:pointer;" title="chiudi">X</span></td></tr>
								<tr>
									<td valign="top">
										<fieldset class="fieldsgroup">
											<legend class="text" style="font-weight: bold;">
												Dati generali
											</legend>
											<table cellpadding="2" cellspacing="1" class="text">				
												<tr>
													<td style="white-space:nowrap;"><b>Tipo Modulo:</b></td>
													<td style="white-space:nowrap;"><%=pdfModulo.getTipoModulo()%></td>
												</tr>
												<tr>
													<td align="left"><b>Segmento:</b></td>
													<td align="left"><%=pdfModulo.getSegmento()%></td>
												</tr>
												<tr>
													<td align="left"><b>Valido dal:</b></td>
													<td align="left"><%=(pdfModulo.getDataInizioValidita().isNull()?"---":""+pdfModulo.getDataInizioValidita())%></td>
												</tr>
												<tr>
													<td align="left" valign="top"><b>Al:</b></td>
													<td align="left"><%=(pdfModulo.getDataFineValidita().isNull()?"---":""+pdfModulo.getDataFineValidita())%></td>
												</tr>
												<tr>
													<td align="left" valign="top"><b>Pubblicazione corrente in vigore dal:</b></td>
													<td align="left"><%=(pdfModulo.getDataUltimaPubblicazione().isNull()?"---":""+pdfModulo.getDataUltimaPubblicazione())%></td>
												</tr>
											</table>
										</fieldset>				
									</td>
								</tr>
						</table>
		     		</div>
		     	</div>
		     	<script>$('#datiModuloContCloser').bind("click",function(){$('#datiModuloCont').hide();})</script>
		     	
		     	<b>
			     	<% if(!isConPubblicazioni){ %>
			     		Nuovo Modulo Compilabile <%=pdfAnag.getIsFromCatalogoModuli().booleanValue()?"da catalogo":""%>
			     	<% }else{ %>
			     		Modulo compilabile <%=pdfAnag.getIsFromCatalogoModuli().booleanValue()?"da catalogo":""%>
			     	<% } %>
			     	<% if(!pdfAnag.getIsFromCatalogoModuli().booleanValue()){ %>
			     		area <%=areaDescr%>&nbsp;&nbsp;<span style="font-weight:normal;"><%=pdfIdAsString%></span>
			     	<% } %>
		     	</b>
				
				<% if(!pdfAnag.getPdfId().isNull() && !pdfAnag.getPdfCode().isNull() && pdfAnag.getIsFromCatalogoModuli().booleanValue()){ %>
		     		<br><%=pdfAnag.getPdfId()%> - <%=pdfAnag.getPdfCode()%>
		     	<% } %>

				<% if(pdfAnag.getIsFromCatalogoModuli().booleanValue()){ %>
		     		&nbsp;<span style="text-decoration:underline;cursor:pointer;" onclick="$('#datiModuloCont').show();">(clicca qui per i dati modulo)</span>
		     	<% } %>
		     	
				<% if(pdfAnag.getIsFromCatalogoModuli().booleanValue()){ %>
			     	<br><%=pdfAnag.getPdfDescr()%>
		     	<% } %>
		     </td>
		     
		  	 <td align="right">
		  	    <table class="text" cellpadding="0" cellspacing="2"><tr>
		     	 	<% if(!isConPubblicazioni){ %>
			     	 	<td><%=template.action("gobackAction","style='width: 80;'")%></td>
			     	 	<td><%=template.action("salvaAction","style='width: 80;'")%></td>
		     	 		<td><%=template.action("pubblicaComeNuovaPubblicazione","text='Pubblica' style='width: 80;' "+(pdfAnag.getPdfNumPages().intValue() > 0 && !pdfAnag.getPdfContent().isNull() ? "":"enabled='false'"))%></td>
		     	 	<% }else{ %>
		     	 		<td><%=template.action("gobackAction","style='width: 120;'")%></td>
			     	 	<td><%=template.action("aggiornaAnagAction","text='Salva dati modulo' style='width: 120;'")%></td>
			     	 	<% if(confModel.isAreaCrafter()){ %>
			     	 	<td><%=template.action("popolaCrafterAction","text='Rigenera dati Crafter' style='width: 120;'")%></td>
			     	 	<% } %>
		     	 	<% } %>
				</tr></table>
		     </td>
		  </tr>
		</table>
    </td>
</tr>

<tr>
   <td>
	<%=template.getMessagesAndErrors()%>
   </td>
</tr>		 

<% if(isConPubblicazioni){ %>
  <tr>
  	<td align="center">
		<form name="datiAnag" method="post" action="call.wfem">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<%@ include file="./PdfAnagData.html"%>
		</form>
  	</td>
  </tr>
  <tr><td style="height:10px;"></td></tr>
  <tr>
    <td style="padding-left: 8px;padding-right: 10px;">
    	<%
    		String archiv = "";
    		if(confModel.getPdfNumArchiviazioni().intValue() > 0)
    			archiv = " (<span onclick=\"openPdfArchivedList();\" style=\"cursor:pointer;text-decoration:underline;font-weight:normal;\">"+confModel.getPdfNumArchiviazioni()+" archiviati</span>)";

        	String cols = "pdfPublicationId,pdfAcroformVersion,pdfDriverVersion,pdfEdition,pdfMomVersion,pdfPubStartDate,pdfDataFineAccettazionePubblPrec,pdfFileName,pdfNumPages,pdfHasPublicationNotes,comandi,#pdfHasValidationWarningMessage";
        	String colswidths = "6%,8%,8%,8%,8%,9%,9%,*,6%,4%,28%";
    	%>
	   	<%=template.grid("pdfPubblicationList",""+
	   									  "cols='"+cols+"' "+
	   									  "colswidths='"+colswidths+"' "+
	   									  "cellheight='25' "+
	  									  "sortable='false' "+
	  									  "selection='none' "+
	  									  "headernowrap='false' "+
	  									  "headerheight='40' "+
	  									  "showcounter='false' "+
	  									  "showbottomarea='false' "+
	  									  "title='Pubblicazioni"+archiv+"' "+
	  									  "onnewcell='onNewCell(this);' "+
	  									  "decorator='prgm.pdfwebforms.publisher.display.PdfConfiguration' "+
	  									  "height='160' width='100%'")%>
    </td>
  </tr>
  
  <% if(!confModel.isWorkingAreaHidden()){ %>
	  <tr>
	  	<td>
			<%@ include file="./PdfPublicationData.html"%>
	  	</td>
	  </tr>
  <% } %>
  
<% }else{ %>

	  <tr>
	  	<td>
			<%@ include file="./PdfNewPublicationData.html"%>
		</td>
	</tr>

<% } %>

  <tr><td><table width="100%" class="toolbar" cellpadding="0" cellspacing="2"><tr><td>&nbsp;</td></tr></table></td></tr>

  <tr>
    <td>
		<%=template.getMessagesAndErrors()%>
    </td>
  </tr>
</table>

<% if(confModel.hasCommandErrors()){ %>
	<script>alert("Operazione non effettuata. Verificare gli errori in pagina");</script>
<% } %>

<div id="codAgenteVerificaPdf" style="display:none;">
	<div style="height:100%;width:100%;position:relative;overflow:auto;">
		<div style="position:absolute;left:0;top:0;">
			<table class="text" width="100%">
				<tr><td>Codice Family Banker <input class="inputField" type="text" id="codAgenteVerificaPdfField"></td></tr>
				<tr><td>(se vuoto viene utilizzato l'agente di riferimento)</td></tr>
				<tr><td align="center" style="padding-top:5;"><span style="font-size:16;text-decoration:underline;cursor:pointer;" onclick="gotVerificaPdf();">simula</span></td></tr>
			</table>
		</div>
	</div>
</div>

<% if(confModel.getPdfValidationWarningMessage().length() > 0 || confModel.getPdfValidationErrorMessage().length() > 0){ %>
	<div id="errorReportMessage">
		<div style="height:400;width:520;position:relative;overflow:auto;">
			<div class="text" style="position:absolute;left:0;top:0;">
				<%=confModel.getPdfValidationWarningMessage()%>
				<%=confModel.getPdfValidationErrorMessage()%>
			</div>
		</div>
	</div>
	<% 
		confModel.setPdfValidationWarningMessage("");
		confModel.setPdfValidationErrorMessage("");
	%>
	<script>
		$("#errorReportMessage").dialog({
			autoOpen: true, 
			modal: true,
			height: 410,
			width: 550,
			closeOnEscape: false,
			title: "Avviso"
		   });
	</script>
<% } %>

<% if(!pdfAnag.getPdfValidationWarningMessage().isNull()){ %>
	<div id="validationWarningReportMessage" style="display:none;">
		<div style="height:400;width:520;position:relative;overflow:auto;">
			<div class="text" style="position:absolute;left:0;top:0;">
				<%=pdfAnag.getPdfValidationWarningMessage()%>
			</div>
		</div>
	</div>
<% } %>

<% if(confModel.getCrafterErrorMessage().length() > 0){ %>
	<script>alert("<%=confModel.getCrafterErrorMessage()%>");</script>
	<% confModel.setCrafterErrorMessage(""); %>
<% } %>

<%=template.getFooter()%>
</body>
</html>
