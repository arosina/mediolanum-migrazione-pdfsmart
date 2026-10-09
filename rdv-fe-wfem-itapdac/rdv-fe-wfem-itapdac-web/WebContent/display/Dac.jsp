<%@ page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@ page import="com.atosorigin.wfem.controller.Configuration"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.util.DocumentoAssegnoTools"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);

	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDAC");
	template.setPageName("Dac");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	DacModel model = (DacModel)template.getPageDataModel();
	ClientSessionContext csc = model.getUserSessionContext().getClientSessionContext();
	AgenteModel agenteRiferimento = model.getAgenteRiferimento();
	DocumentoModel documento = model.getDocumento();
%>

<%@ include file="../documento/DefinizioneVariabili.html"%>

<%	
	
	idDacCorrente = model.getIdDac().toString();
	readonly = template.getModality() == Template.READ_MODALITY ? true : false;

	boolean isPromotore = model.getUfficio().equals(Costanti.UFFICIO_RETE) ? true : false;
	if(isPromotore)
		canMakeForOther = agenteRiferimento.getCanMakeForOtherFb().booleanValue() ? true : false;
	else
		canMakeForOther = true;
	String autoSpuntataLabel="";
	if(model.getIsAutoSpuntata().booleanValue())
		autoSpuntataLabel="(Implic. spuntato)";
	
	boolean isDacSede = false;
	if(model.getTipoDac().equals(Costanti.TIPO_DAC_SEDE))
		isDacSede = true;

	String autore = "";
	if(isDacSede){
		autore = model.getCodUtenteIns().toString();
		if(!model.getUffMittente().isNull())
			autore += " - "+model.getDescValue("uffMittente");
	}else{
		if(!agenteRiferimento.getCodAgente().isNull() && (!readonly || model.getApriPritAttivo().booleanValue())){
			autore += "Cod area: "+agenteRiferimento.getCodArea()+" - Cod. Agenzia: "+agenteRiferimento.getCodAgenzia()+": "+agenteRiferimento.getDatiAgenzia();
		}else{
			autore += model.getCodUtenteIns().toString();
			if(!model.getAgenteRiferimento().getCodAgente().isNull())
				autore += " - "+model.getAgenteRiferimento().getNominativo();
			else
				autore += " - "+model.getDescValue("uffMittente");
		}
	}
	
	String stato = model.getDescValue("stato");
	if(model.getFlagReplica().equals("D"))
		stato = "Da replicare";
%>
<html>

<head>
<title>Prit</title>
<%=template.getHeader()%>
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/MandatoryFields.css" type="text/css">
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Labels.css" type="text/css">

<script src="<%=template.getWebApp()%>/resources/javascript/plichi.js"></script>
<script src="<%=template.getWebApp()%>/resources/javascript/stampa.js"></script>
<script src="<%=template.getWebApp()%>/resources/javascript/ErroriDocumenti.js"></script>
<script src="<%=template.getWebApp()%>/display/Dac.js"></script>
<script src="<%=template.getWebApp()%>/documento/Documento.js"></script>
<script src="<%=template.getWebApp()%>/popup/popup.js"></script>

<script>
<%=model.getHtmlJavascriptCampiObbligatori()%>
function startApriPritBC(button){
	document.startApriPritBCForm.idDac.value = button.getAttribute("idDac");
	document.startApriPritBCForm.submit();
}
</script>
</head>

<body  style="margin:0;">

<% if(!isPromotore){ %>
	<div  style="display:none;position:absolute;left:200;top:250;z-index:5000;" id="helpStato">
		<%@ include file="./HelpStato.html"%>
	</div>
<% } %>

<div id="firmeClienteIframeContainer" style="display:none;">
<iframe id="firmeClienteIframe" name="firmeClienteIframe" codMediolanum="" src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" width="100%" height="100%" frameborder="0"></iframe>
</div>
<div id="firmeAgenteIframeContainer" style="display:none;">
<iframe id="firmeAgenteIframe" name="firmeAgenteIframe" codMediolanum="" src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" width="100%" height="100%" frameborder="0"></iframe>
</div>

<form name="startApriPritBCForm" method="post" action="call.wfem" style="display:none;" target="_blank">
	<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.display.StartApriPritBC.executeProcessOnNewStack">
	<input type="hidden" name="idDac" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="gobackForm" method="post" action="call.wfem" style="display:none;">
	<input type="hidden" name="wfemCmd" value="executeLastDisplay">
	<input type="hidden" name="readRequest" value="true">
	<input type="hidden" name="doSearch" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="nuovaDacDopoInviaForm" method="post" action="call.wfem" style="display:none;">
	<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.business.GestioneDacAttiva.execute">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
    <%=model.htmlParams("","")%>
</form>

<form name="datiRead" id="datiRead" style="display:none;">
  <input type="hidden" name="isPromotore" value="<%=isPromotore%>"></input>
  <input type="hidden" name="isOffline" value="<%=Configuration.getInstance().isOfflineEnvironment()%>"></input>
  <input type="hidden" name="fnc" value="<%=model.getFnc()%>"></input>
  <input type="hidden" name="isAutoSpuntata" value="<%=model.getIsAutoSpuntata()%>"></input>
  <input type="hidden" name="idDac" value="<%=model.getIdDac()%>"></input>
  <input type="hidden" name="tipoDac" value="<%=model.getTipoDac()%>"></input>

  <!--  x gestione colorazione documenti di sede -->
  <input type="hidden" name="constIdDacDocFuoriDac" value="<%=Costanti.ID_DAC_X_DOC_FUORI_DAC%>"></input>
  <input type="hidden" name="isDacSede" value="<%=isDacSede%>"></input>
  <input type="hidden" name="isDacLavorata" value="<%=model.getStato().equals(Costanti.STATO_LAVORATA)%>"></input>
  <input type="hidden" name="isDacInLavorazione" value="<%=model.getStato().equals(Costanti.STATO_APERTA)%>"></input>

  <input type="hidden" name="codReteAgenteRiferimento" value="<%=model.getAgenteRiferimento().getCodRete()%>"></input>
  <input type="hidden" name="codAgenteRiferimento" value="<%=model.getAgenteRiferimento().getCodAgente()%>"></input>
  <input type="hidden" name="nominativoAgenteRiferimento" value="<%=model.getAgenteRiferimento().getNominativo()%>"></input>
  <input type="hidden" name="codMediolanumAgenteRiferimento" value="<%=model.getAgenteRiferimento().getCodMediolanum()%>"></input>
  <input type="hidden" name="flagReplicaDac" value="<%=model.getFlagReplica()%>"></input>
  <input type="hidden" name="readonly" value="<%=readonly%>"></input>
  <input type="hidden" name="mgmPlichi" value="<%=model.isMgmPlichi()%>"></input>
  <input type="hidden" name="refreshable" value="<%=model.isRefreshable()%>"></input>
  <input type="hidden" name="isGestoreBarcode" value="<%=model.isGestoreBarcode()%>"></input>
</form>

<table width="99%" height="100%">
  <% if(model.getElencoBC().size() > 0){ %>
  <tr>
    <td align="left">
    	<table width="100%" class="text" style="background-color:#F9FAAD;">
    	  <tr>
    	  	<td>
    	  		<table cellpadding="0" cellspacing="0" class="text" style="font-weight:bold;">
	    	  		<tr>
						<td>Controlla i Prit in preparazione dai tuoi Banker consultant&nbsp;&nbsp;</td>
			    	  	<% for(int i=0;i<model.getElencoBC().size();i++){ 
			    	  		AgenteBCModel bc = (AgenteBCModel)model.getElencoBC().get(i);
			    	  	%>
			    	  	<td>
			    	  		<% if(bc.getNumDocumentiInDac().intValue() == 0){ %>
			    	  			<input type="button" class="disabledAction" value="<%=bc.getTestoPulsanteAperturaPrit()%>">
			    	  		<% }else{ %>
			    	  			<%=template.action("startApriPritBC","text='"+bc.getTestoPulsanteAperturaPrit()+"' idDac='"+bc.getIdDac()+"' onexecute='startApriPritBC(this);'")%>
			    	  		<% } %>    
			    	  	</td>
			    	  	<% } %>    	  	
	    	  		</tr>
    	  		</table>
    	  	</td>
    	  </tr>
    	</table>
    </td>
  </tr>
  <% } %>
  
  <tr>
    <td>
    	<table width="100%" class="text" style="background-color:#d0e4ef;font-weight:bold;">
    	  <tr>
    	    <% if(model.getFnc().equals(Costanti.FNC_SPUNTA)){ %>
    		    <td nowrap="nowrap">Spunta documenti <%=model.getDescValue("ufficio")%> - Prit n° <%=model.getIdDac()%>&nbsp;<%=autoSpuntataLabel%></td>
	   	    <% }else{ %>
    		    <td nowrap="nowrap" style="padding-left:3;"><%=model.isStoricizzato() ? "STORICO:&nbsp;" : ""%><%=isDacSede ? "Dac" : "Prit"%> n° <%=model.getIdDac()%>&nbsp;-&nbsp;<%=agenteRiferimento.getCodAgente() %>&nbsp;<%=agenteRiferimento.getNominativo()%></td>
    	    <% } %>
    	    <td width="40%">
    	    	<table cellpadding="0" cellspacing="0">
    	    	  <tr>
    	    		<td nowrap="nowrap" class="text" width="100%" style="visibility:hidden;">
    	    		  	&nbsp;<b><%=template.getProperty("tipoDac."+model.getTipoDac().toString())%></b>
    	    		</td>
    	    		<td align="right" id="dacToolbarCont">
		    	      <table cellpadding="0" cellspacing="0">
		    	        <tr>
			    	    <% if(model.isShowBack()){ %>
		    	          <td><%=template.action("goback","style='width:120;'")%></td>
			    	    <% } %>
			    	    <% if(model.getApriPritAttivo().booleanValue()){ %>
			    	      <td style="height:30;">&nbsp;</td>
			    	    <% }else if(!readonly){ %>
			    	      <td><%=model.htmlToolbar(template)%></td>
						<% }else{ %>
							<% if(model.isNuovaDacDopoInvia()){
								model.setNuovaDacDopoInvia(false);
							%>
			    	          	<td><%=template.action("nuovaDacDopoInvia","style='width:100;'")%></td>
							<% } %>
							<% if(!model.getStato().equals(Costanti.STATO_INCORSO)){ %>
								<% if(isDacSede){ %>
				    	          	<td><%=template.action("stampaDac","text='Stampa Dac' style='width:100;'")%></td>
								<% }else{ %>
				    	          	<td><%=template.action("stampaDac","text='Stampa Prit' style='width:100;'")%></td>
								<% } %>
							<% } %>
						<% } %>
		    	        </tr>
		    	      </table>
    	    		</td>
    	    	  </tr>
    	        </table>
    	    </td>
    	  </tr>
     	  <% if(model.getApriPritAttivo().booleanValue() || (!readonly && isPromotore)){ %>
   	      <tr>
   	    	<td colspan="2">&nbsp;<%=autore%></td>
   	      </tr>
   	      <% } %>
    	</table>
    </td>
  </tr>
  
  <tr>
    <td id="dacCont" align="center">
		<form name="dati" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="checkCodDescFields" value="false">
    	<table width="98%" cellpadding="0" cellspacing="0">
    	  <tr>
			<td>
			<% if(readonly && !model.getApriPritAttivo().booleanValue()){ %>
				<% if(!isDacSede){ // Prit %>
			    	<table width="100%" style="table-layout:fixed;">
			    	  <tr>
			    	    <td class="label">Autore</td><td class="value" colspan="2">&nbsp;<%=autore%></td>
			    	    <td class="label">Inviato il</td><td class="value" colspan="2">&nbsp;<%=model.getDataOraEmissione()%></td>
			    	  </tr>
			    	  <tr>
			    	    <td class="label">Stato</td><td class="value" colspan="2">&nbsp;<%=stato%></td>
			    	    <td class="label">Esito</td><td class="value" colspan="2">&nbsp;<%=model.getDescValue("esito")%></td>
			    	  </tr>
			    	  <tr>
			    	    <td class="label">Ricevuto il</td><td class="value">&nbsp;<%=model.getDataOraLavorazione()%></td>
			    	    <td class="label">Dove</td><td class="value">&nbsp;<%=model.getDescValue("uffLavorazione")%></td>
			    	    <td class="label">Da</td><td class="value">&nbsp;<%=model.getCodUtenteLavorazione()%></td>
			    	  </tr>
			    	  <tr>
			    	    <td class="label">Spuntato il</td><td class="value">&nbsp;<%=model.getDataOraSpunta()%></td>
			    	    <td class="label">Dove</td><td class="value">&nbsp;<%=model.getDescValue("uffSpunta")%></td>
			    	    <td class="label">Da</td><td class="value">&nbsp;<%=model.getCodUtenteSpunta()%></td>
			    	  </tr>
			    	  <tr>
			    	    <td class="label">Documenti ricevuti il</td><td class="value" colspan="2">&nbsp;<%=model.getDataRicezioneDocumenti()%></td>
			    	    <td class="label">Spedizione</td><td class="value" colspan="2">&nbsp;<%=model.getDescValue("codTipoSpedizione")%></td>
			    	  </tr>
			    	  <tr>
			    	    <td class="label">Note Autore</td><td class="value" colspan="2">&nbsp;<%=model.getNoteAutore()%></td>
			    	    <td class="label">Note Operatore</td><td class="value" colspan="2">&nbsp;<%=model.getNoteOperatore()%></td>
			    	  </tr>
			    	</table>
				<% }else{ // Dac %>
			    	<table width="100%" style="table-layout:fixed;">
			    	  <tr>
			    	    <td class="label">Autore</td><td class="value">&nbsp;<%=autore%></td>
			    	    <td class="label">Spedita il</td><td class="value">&nbsp;<%=model.getDataOraEmissione()%></td>
			    	    <% 
			    	    	String descrDest = model.getDescValue("uffDestinatario");
			    	    	if(model.isSmistatore() && !model.getBox().isNull())
			    	    		descrDest += "<br>&nbsp;Box: "+model.getDescValue("box");
			    	    %>
			    	    <td class="label">Per</td><td class="value">&nbsp;<%=descrDest%></td>
			    	  </tr>
			    	  <tr>
			    	    <td class="label">Ricevuta il</td><td class="value">&nbsp;<%=model.getDataOraLavorazione()%></td>
			    	    <td class="label">Dove</td><td class="value">&nbsp;<%=model.getDescValue("uffLavorazione")%></td>
			    	    <td class="label">Da</td><td class="value">&nbsp;<%=model.getCodUtenteLavorazione()%></td>
			    	  </tr>
			    	  <tr>
			    	    <td class="label">Note Autore</td><td class="value">&nbsp;<%=model.getNoteAutore()%></td>
			    	    <td class="label">Note Operatore</td><td class="value">&nbsp;<%=model.getNoteOperatore()%></td>
			    	    <td class="label">Spedizione</td><td class="value">&nbsp;<%=model.getDescValue("codTipoSpedizione")%></td>
			    	  </tr>
			    	</table>
				<% } %>
			<% }else{ %>
		    	<table width="100%">
		    	  <tr>
		    		<td><%=template.field("codTipoSpedizione","onchange='doSalvaDac();' showempty='false' labelwidth='80'")%></td>
		    		<% if(model.getFnc().equals(Costanti.FNC_SPUNTA)){ %>
			    	    <td><%=template.field("noteAutore","modality='read' labelwidth='60' size='40' maxlength='50'")%></td>
			    	    <td><%=template.field("noteOperatore","onchange='doSalvaDac();' labelwidth='60' size='40' maxlength='50'")%></td>
		    		<% }else{ %>
			    	    <td><%=template.field("noteAutore","onchange='doSalvaDac();' labelwidth='50' size='40' maxlength='50'")%></td>
			    	    <td>&nbsp;</td>
		    		<% } %>
		    		<td>
		    	    <% if(!isPromotore){
		    	    	model.setDatRicDocAsCombo();
		    	    %>
			    		<%=template.field("dataRicezioneDocumenti","onchange='doSalvaDac();' showempty='false' labelwidth='100'")%>
		    	    <% } %>
		    	    </td>
		    	  </tr>
		    	</table>
			<% } %>
	    	</td>
    	  </tr>
    	</table>
    	</form>
    </td>
  </tr>
  
  <% if(!readonly){ %>
  <tr>
    <td id="upDocumentiCont">
    	<table width="100%" style="table-layout:fixed;">
    	  <tr>
    	    <td>
    	    	<table>
    	    	   <tr>
    	    	     <td><%=template.action("inserisciDocumenti")%></td>
		    	    <% if(documento.isVisible() && !documento.getIdDocumento().isNull() && !documento.isDocumentoAssegno()){ %>
			   	    	<td><%=template.action("clonaDocumento")%></td>
		    	    <% }else{ %>
			   	    	<td><%=template.action("clonaDocumento","enabled='false'")%></td>
		    	    <% } %>
    	    	   </tr>
    	    	</table>
    	    </td>
    	    <% if(model.getDocumenti().size() > 0){ %>
	    	    <% if(model.getFnc().equals(Costanti.FNC_SPUNTA)){ %>
	    	    	<%if(model.isDocAssegniAbilitati()){%>
	    	    		<td class="text" style="font-size:10;">[Prit generato con gestione assegni]</td>
	    	    	<%}else{%>
	    	    		<td class="text" style="font-size:10;">[Prit generato senza gestione assegni]</td>
	    	    	<%}%>
	    	    <% } %>
	    	    <td></td>
    	    <% } %>
    	  </tr>
    	</table>
    </td>
  </tr>
  <% } %>

  <% if(model.getErroriDocumento().size() > 0){
		cols = "cols='#progressivo,barcode,erroreDescr,dataIns,daAutorizzareAction,dataAutorizzazione,inSpedizione,#daAutorizzare' ";
		dim  = "colswidths='15%,*,15%,15%,15%,8%' "; 	%>
		<tr>
			<td width="100%" align="center" id="tdErroriDocumento">
			<form name="erroriForm" id="erroriForm" method="post" action="call.wfem" style="margin:0;">
			<input type="hidden" name="wfemCmd" value="">
			<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
			<input type="hidden" name="erroreDocumento_progressivo" value="">
			<input type="hidden" name="checkCodDescFields" value="false">
				<%=template.grid("erroriDocumento", cols + dim + 
														"width='90%' height='120' " +
														"title='Errori presenti nella DAC' " +
														"onnewcell='newCellErrDoc(this);' " +
														"onnewrow='newRowErrDoc(this);' " + 
														"selection='none'") %>
			</form>
			</td>
		</tr>
  <% } %>
  
  <tr>
    <td height="100%" id="documentiCont">
		<form name="documentiForm" method="post" action="call.wfem" style="height:100%;margin:0;">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="checkCodDescFields" value="false">
    	<%
    		String footerhtml = "<table class=\"text\" width=\"100%\" style=\"table-layout:fixed;\">"+
    								"<tr>"+
    								   "<td>&nbsp;</td>"+
    								   "<td align=\"center\">Righe totali: "+model.getDocumenti().size()+"</td>"+
    								   "<td align=\"right\" style=\"font-size:14;\"><b>Importo tot: "+model.getImporto()+" &euro;</b>&nbsp;</td>"+
    								"</tr>"+
    							"</table>";
   			if(isPromotore){
   	   			cols = "cols='#idDocumento,#codAggregatore,#isFirstInPlico,#isLastInPlico,#esito,#codProdotto,#datiAssegno_importo,#datiAssegno_codDivisa,"+
   	   						 "idPlico,descrProdotto,numeroContratto,codInforeteEsterno,descrOperazione,descrEsito,"+
   	   						 "agente_codAgente,cliente_nominativo' ";
				dim  = "colswidths='5%,18%,10%,14%,18%,10%,8%,*' ";				
   			}else{
   	   			cols = "cols='#idDocumento,#codAggregatore,#isFirstInPlico,#isLastInPlico,#esito,#hasControlloFirmeClienteAttivo,#hasControlloFirmeAgenteAttivo,"+
						 "#esitoFirmaCliente,#esitoFirmaAgente,#codProdotto,#datiAssegno_importo,#datiAssegno_codDivisa,";
				dim =  "colswidths='";

   				if (model.getTipoDac().equals(Costanti.TIPO_DAC_SEDE)) {
   	   	   			cols += "#aggiuntoInRicezione,#nonPervenuto,#idDac,idPlico,barcode,descrProdotto,numeroContratto,codInforeteEsterno,descrOperazione,agente_codAgente";
   	   	   			dim  += "8%,8%,15%,12%,12%,16%,16%";
   				} else {
   	   	   			cols += "imgFase,idPlico,barcode,descrProdotto,numeroContratto,codInforeteEsterno,descrOperazione,descrEsito,agente_codAgente";
   	   	   			dim  += "3%,5%,8%,15%,12%,12%,15%,10%,9%";
   				}
   	   			
   	   			if(!model.getTipoDac().equals(Costanti.TIPO_DAC_CARTOLINE)){
   	   				cols += ",cliente_nominativo";
   	   				dim  += ",*";
   	   			}
   	   			
   	   			cols += "' ";
   	   			dim  += "' ";
   			}
    	%>
    	<%=template.grid("documenti",cols+dim+
    								 "width='100%' height='100%' "+
    								 "title='"+model.htmlTitoloGrigliaDocumenti()+"' "+
    								 "footerhtml='"+footerhtml+"' "+
    								 "cellheight='30' "+
    								 "counterwidth='26' "+
	    							 "cellsnowrap='false' "+
    								 "selection='single' "+
    								 "sortable='false' "+
    								 "norowsmsg='Nessun documento presente' "+
    								 "onclick='apriDocumento(this);' "+
    								 "onnewheader='onNewHeaderDac(this);' "+
    								 "onnewrow='onNewRowDac(this);' "+
    								 "onnewcell='onNewCellDac(this);'")%>
		</form>
  	</td>
  </tr>
  <tr>
   <td align="center" id="legenda">
  	<% if(model.getDocumenti().size() > 0){ %>
	   	<table class="text"><tr>
	   		<% if(isDacSede && (model.getStato().equals(Costanti.STATO_APERTA) || model.getStato().equals(Costanti.STATO_LAVORATA))) { %>
				<td><%=template.getProperty("azioneSuDocumento.nessuna")%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkAzioneSuDocumento.nessuna")%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("azioneSuDocumento.ricevuto")%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkAzioneSuDocumento.ricevuto")%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("azioneSuDocumento.aggiuntoInRicezione")%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkAzioneSuDocumento.aggiuntoInRicezione")%>;width:15;">&nbsp;</td><td width="25"></td>
				<td><%=template.getProperty("azioneSuDocumento.nonPervenuto")%></td><td style="border:solid 1px gray;background-color:<%=template.getProperty("bkAzioneSuDocumento.nonPervenuto")%>;width:15;">&nbsp;</td><td width="25"></td>
			<% } %>
	   	</tr></table>
	  <% } %>
   </td>
  </tr>
    	  
  <tr>
  	<td align="center" valign="middle">
		<%@ include file="../documento/Documento.html"%>
  	</td>
  </tr>
  
  <% if(model.hasCommandErrors()){ %>
  <tr>
  	<td align="center" valign="middle">
		<%=template.getMessagesAndErrors()%>
  	</td>
  </tr>
  <% } %>

</table>

<% if(model.isShowAlert()){ %>
	<script>alert("<%=model.getMsg()%>");</script>
<%  model.setShowAlert(false);
	model.setMsg("");
	} %>
	
<div  style="display:none;" id="finalsDacScriptsCont">
<table><tr><td>
	<script>
	selezionaDocumento("<%=documento.getIdDocumento()%>");
	isDacSpuntabile = <%=model.isSpuntabile()%>;
	<% if(model.getErrorString().length() > 0){ %>
		alert("<%=model.getErrorString()%>");
	<% } %>
	</script>
</td></tr></table>
</div>

<div  style="display:none;" id="finalsDocScriptsCont">
<table><tr><td>
    <script>

	<% if(documento.isTabFirmeClienteVisible()){ // Caricamento delle immagini delle firme %>
	    startLoadFirmaCliente();
	<% } %>
	<% if(documento.isTabFirmeAgenteVisible()){ %>
	    startLoadFirmaAgente();
    <% } %>
    
    <% String tabToSelect =  documento.getTabName().toString();  // Selezione del tab opportuno %>
    <% 
	   if(!readonly && !documento.hasCommandErrors() && !documento.getEsito().isNull()){
	      if(documento.getHasControlloFirmeClienteAttivo().booleanValue() && documento.getEsitoFirmaCliente().isNull()){
	    	  tabToSelect = "tabFirmeCliente";
	      }else if(documento.getHasControlloFirmeAgenteAttivo().booleanValue() && documento.getEsitoFirmaAgente().isNull()){
	    	  tabToSelect = "tabFirmeAgente";
		  } 
	   } 
	%>
   	selectDocJSTab("<%=tabToSelect%>");
    
	selezionaDocumento("<%=documento.getIdDocumento()%>");

    try{
    	if(document.getElementById("documento_barcode").value == ''){document.getElementById("documento_barcode").focus();document.getElementById("documento_barcode").select();}
    }catch(e){
    	try{
    		if(document.getElementById("documento_numeroContratto").value == ''){document.getElementById("documento_numeroContratto").focus();document.getElementById("documento_numeroContratto").select();}
    	}catch(e){}
    }
    
    <% if(!readonly){ %>
		showImpostaCassettaPredefinita();
	    try{
			<% if(documento.isDocumentoAssegno()){ %>
		        evidenziaCampiObbligatori('0');
			<% }else{ %>
	    	    evidenziaCampiObbligatori(document.getElementById("documento_codOperazione").value);
			<% } %>
	    }catch(e){}
	<% } %>
	
	<% if(model.getDocumento().isVisualizzaAlert()){ %>
		alert("Attenzione: non risulta un contratto elettronico per questa operazione.");
	<%  model.getDocumento().setVisualizzaAlert(false);
	} %>
    </script>
</td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>
