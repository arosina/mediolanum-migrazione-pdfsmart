<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="com.atosorigin.wfem.types.DateType"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.model.ParamsModel"%>
<%@ page import="prgm.ita.p.dac.model.DocumentoModel"%>
<%@ page import="prgm.ita.p.dac.ricerche.sede.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);

	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACRICERCHE");
	template.setPageName("RicercaDoc");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setLabelWidth("100");
	template.setJSCombo(false);
	
	RicercaDocModel model = (RicercaDocModel)template.getPageDataModel();	 
	
	DocumentoModel documento = model.getDocumento();
	
	DateType ieri = Tools.today();
	ieri.addDays(-1);
%>

<%@ include file="../../documento/DefinizioneVariabili.html"%>

<html>

<head>
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Labels.css" type="text/css">
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tab.css" type="text/css">
<%=template.getHeader()%>
<script>
	var jsIeri = "<%=ieri%>";
	var isSede = <%=!model.getUfficio().equals(Costanti.UFFICIO_RETE)%>;
</script>
<script src="<%=template.getWebApp()%>/resources/javascript/stampa.js"></script>
<script src="<%=template.getWebApp()%>/documento/Documento.js"></script>
<script src="<%=template.getWebApp()%>/ricerche/sede/RicercaDoc.js"></script>
<script src="<%=template.getWebApp()%>/popup/popup.js"></script>
</head>

<body>

<div id="firmeClienteIframeContainer" style="display:none;">
<iframe id="firmeClienteIframe" name="firmeClienteIframe" codMediolanum="" src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" width="100%" height="100%" frameborder="0"></iframe>
</div>
<div id="firmeAgenteIframeContainer" style="display:none;">
<iframe id="firmeAgenteIframe" name="firmeAgenteIframe" codMediolanum="" src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" width="100%" height="100%" frameborder="0"></iframe>
</div>

<div id="divFrmDati" style="display:none;">
	<form name="dati" id="dati" method="post" action="call.wfem" style="display:none;">
	<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.ricerche.sede.LeggiDocumento.execute">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	<input type="hidden" name="tabNum" value="">
	<input type="hidden" name="documento_idDocumento" value="">
	<input type="hidden" name="verticalVideoHeight" value="">
	<%=model.htmlParams("","")%>
	</form>
</div>

<table class="text" width="100%" height="100%">

  <tr>
   <td valign="top" align="center">
   
		<form name="ricerca" method="post" action="call.wfem" style="margin:0px;">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="doSearch" value="">
		<input type="hidden" name="daoAccessName" value="ricercaDoc">
		<input type="hidden" name="tabNum" value="">
		<input type="hidden" name="tipoRicerca" value="<%=model.getTipoRicerca()%>">
		
        <table width="95%" height="235" cellspacing="0" cellpadding="0" id="JSRicercaDocTab">
		  <tr>
		      <td>
		        <table width="100%" cellspacing="0" cellpadding="0">
		        <tr>
		        <%
		         	String tab1 = "Ricerca Documenti";
	         		String tab2 = "Ricerca Documenti - Ricerca Avanzata";
		        %>
		          <td nowrap class="htab" valign="bottom" id="JSRicercaDocTab0" onclick="selectRicercaDocJSTab('JSRicercaDocTab',0);">&nbsp;<%=tab1%>&nbsp;</td>
		          <td nowrap class="htab" valign="bottom" id="JSRicercaDocTab1" onclick="selectRicercaDocJSTab('JSRicercaDocTab',1);">&nbsp;<%=tab2%>&nbsp;</td>
		      	  <td width="100%" style="border-bottom: 1px solid silver;">&nbsp;</td>
				</tr>
				</table>
			  </td>
		  </tr>			
		  <tr>
		  	  <td valign="top" height="100%" class="htabContent">
			    <table width="100%" height="100%" cellspacing="0" cellpadding="0">
			       <tr>
					  <td height="100%" align="center" style="display:none;" id="JSRicercaDocTab0El">
						<%@ include file="./include/RicercaDoc.html"%>
					  </td>
					  <td height="100%" align="center" style="display:none;" id="JSRicercaDocTab1El">
						<%@ include file="./include/RicercaDocAvanzata.html"%>
					  </td>
				   </tr>
				   <tr><td colspan="2" valign="middle"><hr></td></tr>
				   <tr>
				      <td colspan="2">
				   		<table width="100%" class="text" style="table-layout:fixed;"><tr>
				   			<td style="visibility: hidden" id="msgCampoObb"><i>Compilare almeno un campo contrassegnato con (*)</i></td>
				   			<td><%=template.action("ricercaAction")%></td>
				   			<td align="right"><span id="pulisciCampiRicercaDocCont" style="visibility:hidden;text-decoration:underline;cursor:pointer;" onclick="pulisciCampiRicercaDoc();">Pulisci campi di ricerca</span></td>
				   		</tr></table>
				   	  </td>
				   </tr>
			  	   <% if(!model.getUfficio().equals(Costanti.UFFICIO_RETE)){ %>
				   <tr>
				      <td colspan="2" class="text">
					   Per consultare
					   documenti ricevuti a partire dal 1/7 è necessario utilizzare
					   l'applicativo MOM.  La consultazione dello storico per la ricerca prit e
					   ricerca documenti sarà disponibile sull'attuale applicazione				      	
				      </td>
				   </tr>
				   <% } %>
				</table>
			    <script>
			    	selectRicercaDocJSTab("JSRicercaDocTab",<%=model.getTabNum()%>);
			    	try{
				    	document.getElementById("parametri_barcode").focus();
				    	document.getElementById("parametri_barcode").select();
			    	}catch(e){}
			    </script>
		  	  </td>
		  </tr>		
		</table>
		</form>
   </td>
  </tr>
  
  <tr><td height="10"></td></tr>

  <tr>
   <td id="elencoCont" height="100%" align="center">
		<%if(!model.isPrimaAttivazione()){
				cols = "cols='#idDocumento,"+
						     "barcode,numeroContratto,descrProdotto,descrOperazione,descrStato,descrUbicazione,descrEsito,"+
						     "dataOraCambioStato,agente_nominativoConCodice' ";
				dim = "colswidths='8%,13%,*,*,*,*,8%,11%,*' ";
				
		%>
			<table width="100%" height="100%" cellpadding="0" cellspacing="0">
			  <tr>
			     <td height="100%">
					<%=template.grid("documenti",cols+dim+
												"title='Elenco documenti. Seleziona il documento desiderato per visualizzarne il dettaglio' "+
												"onclick='selectDoc(this);' "+
												"onnewcell='onNewCell(this);' "+
												"selection='single' "+
												"width='100%' height='100%' ")%>
			     </td>
				</tr>
			</table>
		 <%}%>
   </td>
  </tr>

  <tr>
   <td align="center" id="docCont" valign="middle">
		<%@ include file="../../documento/Documento.html"%>
   </td>
  </tr>
  
</table>

<div  style="display:none;" id="finalsDocScriptsCont">
<table><tr><td>
    <script>
	    <% if(!documento.getEsitoFirmaCliente().isNull()){ %>
	    	startLoadFirmaCliente();
	    <% } %>
	    
	    <% if(!documento.getEsitoFirmaAgente().isNull()){ %>
	    	startLoadFirmaAgente();
	    <% } %>
	    
    	selectDocJSTab("tabDati");
    </script>
</td></tr></table>
</div>

<script>document.dati.verticalVideoHeight.value = screen.availHeight;</script>

<%=template.getFooter()%>
</body>
</html>
<% model.setPrimaAttivazione(false); %>
