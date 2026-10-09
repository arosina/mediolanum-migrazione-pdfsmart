<%@page import="java.util.*"%>
<%@page import="prgm.ita.p.dac.facade.Costanti"%>
<%@page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<html>

<% 	
	template.setWlt(true);
	template.setApplCode("ITAPDAC");
	template.setJSCombo(false);

	DacModel model = (DacModel)template.getPageDataModel();

 	boolean isPromotore = model.getUfficio().equals(Costanti.UFFICIO_RETE) ? true : false;
 	
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
		autore += model.getCodUtenteIns().toString();
		if(!model.getAgenteRiferimento().getCodAgente().isNull())
			autore += " - "+model.getAgenteRiferimento().getNominativo();
		else
			autore += " - "+model.getDescValue("uffMittente");
	}
	
	String stato = model.getDescValue("stato");
	if(model.getFlagReplica().equals("D"))
		stato = "Da replicare";
%>

<head>
<%=template.getHeader()%>
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Labels.css" type="text/css">
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tab.css" type="text/css">
<script src="<%=template.getWebApp()%>/resources/javascript/plichi.js"></script>
<script src="<%=template.getWebApp()%>/display/Dac.js"></script>
</head>

<body>

<form name="datiRead" id="datiRead" style="display:none;">
  <input type="hidden" name="isPromotore" value="<%=isPromotore%>"></input>
  <input type="hidden" name="readonly" value="true"></input>
  <input type="hidden" name="isGestoreBarcode" value="false"></input>

  <!--  x gestione colorazione documenti di sede -->
  <input type="hidden" name="constIdDacDocFuoriDac" value="<%=Costanti.ID_DAC_X_DOC_FUORI_DAC%>"></input>
  <input type="hidden" name="isDacSede" value="<%=isDacSede%>"></input>
  <input type="hidden" name="isDacLavorata" value="<%=model.getStato().equals(Costanti.STATO_LAVORATA)%>"></input>
  <input type="hidden" name="isDacInLavorazione" value="<%=model.getStato().equals(Costanti.STATO_APERTA)%>"></input>
  <input type="hidden" name="tipoDac" value="<%=model.getTipoDac()%>"></input>
</form>

<table height="100%" width="100%">

 <tr>
   <td width="100%">
     <table class="text" width="100%" style="font-weight:bold;background-color:<%=template.getProperty("bkTipoDac."+model.getTipoDac().toString())%>;">
       <tr>
		   <td nowrap="nowrap">
		   	 <%=model.isStoricizzato() ? "STORICO:&nbsp;" : ""%>Logistica documenti <%=model.getDescValue("ufficio")%> - <%=isDacSede ? "Dac" : "Prit"%> n&deg; <%=model.getIdDac()%>&nbsp;<%=autoSpuntataLabel%>
		   </td>
		   <td align="right">
		     <%=template.getProperty("tipoDac."+model.getTipoDac().toString())%>
		   </td>
       </tr>
     </table>
   </td>
 </tr>
 
 <tr>
   <td width="100%">
	<% if(!isDacSede){ // Prit %>
    	<table style="table-layout:fixed;" width="100%">
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
    	<table style="table-layout:fixed;" width="100%">
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
  </td>
 </tr>
 
 <%
	template.setLabelCodePrefix("Documento."); 
 %>
 <tr>
   <td height="100%" width="100%">
	<%
		String cols = "";
		String dim = "";
	
		String footerhtml = "<table class=\"text\" width=\"100%\" style=\"table-layout:fixed;\">"+
							"<tr>"+
							   "<td>&nbsp;</td>"+
							   "<td align=\"center\">Righe totali: "+model.getDocumenti().size()+"</td>"+
							   "<td align=\"right\" style=\"font-size:14;\"><b>Importo tot: "+model.getImporto()+" &euro;</b>&nbsp;</td>"+
							"</tr>"+
							"</table>";
		
		if(isPromotore && !model.getTipoDac().equals(Costanti.TIPO_DAC_SEDE)){
   	   		cols = "cols='#idDocumento,#codAggregatore,#isFirstInPlico,#isLastInPlico,#codProdotto,#datiAssegno_importo,#datiAssegno_codDivisa,"+
   	   					 "idPlico,descrProdotto,numeroContratto,codInforeteEsterno,descrOperazione,descrEsito,"+
   	   					 "agente_codAgente,cliente_nominativo' ";
			dim  = "colswidths='5%,18%,10%,14%,18%,10%,8%,*' ";				
   		}else{
   	   		cols = "cols='#idDocumento,#codAggregatore,#isFirstInPlico,#isLastInPlico,#esito,"+
   	   					 "#esitoFirmaCliente,#esitoFirmaAgente,#codProdotto,#datiAssegno_importo,#datiAssegno_codDivisa,";
   	   		dim =  "colswidths='";
   	   		
			if (model.getTipoDac().equals(Costanti.TIPO_DAC_SEDE)) {
   	   	   		cols += "#aggiuntoInRicezione,#nonPervenuto,#idDac,idPlico,barcode,descrProdotto,numeroContratto,codInforeteEsterno,descrOperazione,agente_codAgente";
				dim  += "5%,8%,15%,13%,13%,15%,13%";
   			} else {
				cols += "idPlico,barcode,descrProdotto,numeroContratto,codInforeteEsterno,descrOperazione,descrEsito,agente_codAgente";
				dim  += "5%,8%,15%,11%,12%,15%,8%,8%";
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
    								 "cellheight='30' "+
	 								 "title='"+model.htmlTitoloGrigliaDocumenti()+"' "+
    								 "counterwidth='26' "+
	 								 "selection='none' "+
    								 "sortable='false' "+
    								 "footerhtml='"+footerhtml+"' "+
	 								 "norowsmsg='Nessun documento presente' "+
    								 "onnewrow='onNewRowQuickDac(this);' "+
									 "onnewcell='onNewCellQuickDac(this);'")%>
	
  </td>
 </tr>
   <tr>
   <td align="center" id="legenda" width="100%">
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
</table>

<%=template.getFooter()%>
</body>
</html>
