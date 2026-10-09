<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");  
	template.setPageName("PopupClienti"); 
	template.setLabelPosition(template.NO_LABEL);
	template.setJSCombo(false);
	
	PopupClientiModel model = (PopupClientiModel)template.getPageDataModel();
	model.getElenco().setRowsInPage(15);
%>
<html>

<head>
<%=template.getHeader()%>
<script src="/ItaAnagraficaClienti/popup/display/PopupClienti.js"></script>
</head>

<body scroll="no">

<script>
	function onClickEffettivi(){
		document.ricercaClienti.params_tipoRicerca.value='selezionaEffettivi';
		document.getElementById('tdCodMediolanum').style.visibility = '';
		document.getElementById('tdLabelCodMediolanum').style.visibility = '';
	}
	
	function onClickPotenziali(){
		document.ricercaClienti.params_tipoRicerca.value='selezionaPotenziali';
		document.getElementById('tdCodMediolanum').style.visibility = 'hidden';
		document.getElementById('tdLabelCodMediolanum').style.visibility = 'hidden';
	}
</script>

<form name="ricercaClienti" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.popup.display.PopupClienti.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="isPrimaVolta" value="false">
<input type='hidden' name="index" id="index">
<input type='hidden' name="listPropertyName" id="listPropertyName">
<input type="hidden" name="params_tipoRicerca"  value="<%=model.getParams().getTipoRicerca()%>">

<center>
<table width="98%">
 <tr>
  <td>
	<fieldset>
		<legend class="text" style="font-weight: bold;">
		    <img src="<%=template.getWebApp()%>/images/section.gif">
			&nbsp;Ricerca Clienti&nbsp;
			<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" 
			     onclick="pulisci();" style="cursor:pointer;">
		</legend>
		<table align="center" cellpadding="5" cellspacing="5">
		  <tr>
		  	 <% if(model.getParams().getTipoRicerca().equals("selezionaEffettivi") || model.getParams().getTipoRicerca().equals("selezionaPotenziali")){ %>
			     <td>
				        <table width="100%" cellpadding="0" cellspacing="0" class="text">
				          <tr><td nowrap="nowrap"><input id="radio1" name="radio1" type="radio" <%if(model.getParams().getTipoRicerca().equals("selezionaEffettivi")){%>checked<%}%>  onclick="onClickEffettivi();">Clienti Effettivi</td></tr>
				          <tr><td nowrap="nowrap"><input id="radio1" name="radio1" type="radio" <%if(model.getParams().getTipoRicerca().equals("selezionaPotenziali")){%>checked<%}%> onclick="onClickPotenziali()">Clienti Potenziali</td></tr>
				        </table>
			     </td>
		  	 <% } %>
		  	 <td>
				<table align="center" class="text">
					<tr>
						<td>
							<% if( model.getParams().getTipoElementi().equals("personegiuridiche") ){ %>
								<%=template.getProperty("PopupClienti.params_ragioneSociale")%>
								<%=template.hidden("params_nome")%>
							<% } else { %>
								<%=template.getProperty("PopupClienti.params_cognome")%>
							<% } %>
						</td>
						<% if( !model.getParams().getTipoElementi().equals("personegiuridiche") ){ %>
							<td><%=template.getProperty("PopupClienti.params_nome")%></td>
						<% } %>
						<td id="tdLabelCodMediolanum"><%=template.getProperty("PopupClienti.params_codMediolanum")%></td>
						<td><%=template.getProperty("PopupClienti.params_codAgente")%></td>
					</tr>
					<tr>
						<td><%=template.field("params_cognome","abelwidth='50%' size='35' maxlength='40'")%></td>
						<% if( !model.getParams().getTipoElementi().equals("personegiuridiche") ){ %>
							<td><%=template.field("params_nome","abelwidth='50%' size='35' maxlength='40'")%></td>
						<% } %>
						<td id="tdCodMediolanum"><%=template.field("params_codMediolanum","maxlength='16' labelwidth='50%' size='25'")%></td>
						<td><%=template.field("params_codAgente","type='num' maxlength='10' labelwidth='50%' size='10'")%></td>
					</tr>
				</table>
		  	 </td>
		  </tr>
		  <tr>
		     <td colspan="2"><%=template.action("eseguiRicercaAction")%></td>  
		  </tr>
		</table>
	</fieldset>
  </td>
 </tr>
</table>
</center>

<hr><br>

<%if(!model.getIsPrimaVolta().booleanValue() ||
     (!model.getParams().getCodAgente().isNull() || !model.getParams().getCognome().isNull() || !model.getParams().getCodMediolanum().isNull())){%>
	<center>
	<fieldset id='elenco' name='elenco' style='border: none;'>
	   <%   String colonne = "cols='";
			colonne += "codInforete,codMediolanum,cognome,";
			colonne += model.getParams().getTipoElementi().equals("personegiuridiche")?"#nome,":"nome,";
			colonne += "dataNascita,codAgente,agente_cognomeAgente,naturaGiuridica,"+
			   	       "#codPotenziale,#numVariazioni,#datiApplicativi_nomeTabella,#isCancellabile,#sesso,#codCluster,"+
			           "#stato,#statoProposta,#statoConfermato,#codFiscale,#partitaIva,"+
			           "#isProspect,#isBozza,#isPotenziale,#isAcquisito,#isEffettivoPersonale,#isEffettivoRiassegnato,"+
			           "#isCointestatarioNonAssegnato,#isAssegnatoAdAltroAgente,#isTopBusiness,#descrCluster,#secondaIntestazione,"+
			           "#agente_codAgente,#agente_nomeAgente,#agente_areaAgente,#agente_serverReplica,#agente_codAgenzia,#agente_descrAgenzia,"+
			           "#agente_codProvincia,#agente_codiceContrattoAgente,#agente_cicloVitaAgente,#isDitta' ";
			String dimensioni = "colswidths='13%,10%,*,";
			dimensioni += model.getParams().getTipoElementi().equals("personegiuridiche")?"":"*,";
			dimensioni += "10%,10%,*,9%' ";
	    %>
		<%=template.grid("elenco",colonne+dimensioni+
				                   "pageformname='ricercaClienti' "+
	                 			   "selection='single' "+
				                   "height='250' "+
				                   "width='810' "+
				                   "onnewcell='newCell(this);' "+
				                	(model.getParams().getTipoElementi().equals("personegiuridiche")?"onnewheader='newHeader(this);' ":"")+
				                   "decorator='prgm.ita.anagraficaclienti.popup.model.PopupClientiModel' "+
				                   "onclick='selezionaCliente(this);'")%>
	</fieldset>
	</center>
<%}%>
</form>

<script>
document.ricercaClienti.params_cognome.focus();
</script>

<%=template.getFooter()%>
</body>
</html>
