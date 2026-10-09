<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");  
	template.setPageName("PopupClienti"); 
	template.setLabelPosition(template.UP_LABEL);
	template.setJSCombo(false);
	
	PopupClientiModel model = (PopupClientiModel)template.getPageDataModel();
	AgenteModel agente = model.getParams().getAgente();
	model.getElenco().setRowsInPage(15);
%>

<html>

<head>
<%=template.getHeader()%>
<script>
var statoComfermatoJS = '<%=Costanti.VALORE_FLAG_STATO_CONFERMATO%>';
var agente = new Object();
agente.codAgente = "<%=agente.getCodAgente()%>";
agente.cognomeAgente = "<%=agente.getCognomeAgente()%>";
agente.nomeAgente = "<%=agente.getNomeAgente()%>";
agente.areaAgente = "<%=agente.getAreaAgente()%>";
agente.serverReplica = "<%=agente.getServerReplica()%>";
agente.codAgenzia = "<%=agente.getCodAgenzia()%>";
agente.descrAgenzia = "<%=agente.getDescrAgenzia()%>";
agente.codProvincia = "<%=agente.getCodProvincia()%>";
agente.codiceContrattoAgente = "<%=agente.getCodiceContrattoAgente()%>";
agente.cicloVitaAgente = "<%=agente.getCicloVitaAgente()%>";
</script>
<script src="/ItaAnagraficaClienti/popup/display/PopupClientiAgente.js"></script>
</head>

<body scroll="no">

<%if(agente.getCodAgente().isNull()){ %>
	<table width="100%">
		<tr><td height="20px;"></td></tr>
		<tr>
			<td align="center" class="text" style="font-size: 12pt;">
			   <b>Attenzione! L'agente <%=model.getParams().getCodAgente()%> non esiste</b>
			</td>
		</tr>
		<tr><td height="20px;"></td></tr>
		<tr>
			<td align="center" class="text" style="font-size: 12pt;">
			   <%=template.action("chiudiAction")%>
			</td>
		</tr>
	</table>
<%}else{%>
	<form name="ricercaClienti" method="post" action="call.wfem" style="margin: 0;">
	<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.popup.display.PopupClientiAgentePrestitiPreapprovati.execute">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	<input type="hidden" name="params_codAgente" value="<%=agente.getCodAgente()%>">
	<input type="hidden" name="params_codAgenteRoot" value="<%=model.getParams().getCodAgenteRoot().toString()%>">
	<input type="hidden" name="isPrimaVolta" value="false">
	<input type='hidden' name="index" id="index">
	<input type='hidden' name="listPropertyName" id="listPropertyName">
	
	<center>
	<table width="98%">
	 <tr>
	  <td>
		<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			    <img src="<%=template.getWebApp()%>/images/section.gif">
			    <% if(model.getParams().getTipoElementi().equalsIgnoreCase("ditte")){ %>
				&nbsp;Ricerca Ditte/Liberi professionisti&nbsp;
			    <% }else{ %>
				&nbsp;Ricerca Clienti&nbsp;
				<% } %>
				<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" 
				     onclick="pulisci();" style="cursor:pointer;">
			</legend>
			<center>
			<table align="center" cellpadding="5" cellspacing="5">
			  <% if (!model.getParams().getCodAgenteRoot().isNull() || !model.getParams().getCodAgenteRoot().equals("")) { %>
			  <tr>
			  	<td colspan="2" align="center" width="100%">
			  		<table class="text" cellpadding="0" cellspacing="0" align="center">
			  			<tr>
			  				<td align="center">
			  					<%=template.field("params_agenteDiretto","onchange='setCodiceAgente(this);'") %>
			  				</td>
			  			</tr>
			  		</table>
			  	</td>  
			  </tr>
			  <% } %>
			  <tr>
			     <td>
				  	 <% if(com.atosorigin.wfem.controller.Configuration.getInstance().isOnlineEnvironment() &&
				  			 model.getParams().getTipoInclusioneAgenti().equals("spv") && !model.getParams().getCodAgenteSpv().isNull()){ %>
				        <table width="100%" cellpadding="0" cellspacing="0" class="text">
				        	<% 
				        		int age 	= Integer.parseInt(model.getParams().getCodAgente().toString());
				        		int ageInp 	= Integer.parseInt(model.getParams().getCodAgenteInp().toString());
				        		int ageSpv 	= Integer.parseInt(model.getParams().getCodAgenteSpv().toString());
				        	%>
				          <tr><td nowrap="nowrap"><input id="radio1" name="radio1" type="radio" <%if(age == ageInp){%>checked<%}%> onclick="document.ricercaClienti.params_codAgente.value='<%=model.getParams().getCodAgenteInp()%>';">Cerca clienti propri</td></tr>
				          <tr><td nowrap="nowrap"><input id="radio1" name="radio1" type="radio" <%if(age == ageSpv){%>checked<%}%> onclick="document.ricercaClienti.params_codAgente.value='<%=model.getParams().getCodAgenteSpv()%>';">Cerca clienti del supervisore</td></tr>
				        </table>
				  	 <% } %>
			     </td>
			     <td>
			        <table>
			          <tr>
					    <% if(model.getParams().getTipoElementi().equalsIgnoreCase("ditte")){ %>
				             <td><%=template.field("params_cognome","labelcode='Cognome del titolare/lib. prof.' labelwidth='50%' size='30' maxlength='40'")%></td>
				             <td><%=template.field("params_nome","labelcode='Nome' labelwidth='50%' size='30' maxlength='40'")%></td>
				        <% }else if(model.getParams().getTipoElementi().equalsIgnoreCase("personefisiche")){ %>
				             <td><%=template.field("params_cognome","labelcode='Cognome' labelwidth='50%' size='30' maxlength='40'")%></td>
				             <td><%=template.field("params_nome","labelcode='Nome' labelwidth='50%' size='30' maxlength='40'")%></td>
				        <% }else if(model.getParams().getTipoElementi().equalsIgnoreCase("personegiuridiche")){ %>
				             <td><%=template.field("params_cognome","labelcode='Ragione sociale' labelwidth='50%' size='60' maxlength='40'")%></td>
				        <% }else if(model.getParams().getTipoElementi().equalsIgnoreCase("datoridilavoro")){ %>
				             <td><%=template.field("params_cognome","labelcode='Ragione sociale' labelwidth='50%' size='60' maxlength='40'")%></td>
					    <% }else{ %>
				             <td><%=template.field("params_cognome","labelwidth='50%' size='30' maxlength='40'")%></td>
				             <td><%=template.field("params_nome","labelcode='Nome' labelwidth='50%' size='30' maxlength='40'")%></td>
						<% } %>
						
					    <% if(model.getParams().getTipoElementi().equalsIgnoreCase("ditte")){ %>
				             <td><%=template.field("params_codMediolanum","labelcode='Cod. Cliente della ditta/lib. prof.' maxlength='16' labelwidth='50%' size='15'")%></td>
					    <% }else{ %>
				             <td><%=template.field("params_codMediolanum","maxlength='16' labelwidth='50%' size='15'")%></td>
						<% } %>
					  </tr>
			        </table>
			     </td>
			  </tr>
			  <tr>
			     <td colspan="2"><%=template.action("eseguiRicercaAction")%></td>  
			  </tr>
			</table>
			</center>
		</fieldset>
	  </td>
	 </tr>
	</table>
	</center>
	
	<hr><br>
	
	<%if(!model.getIsPrimaVolta().booleanValue() || !model.getParams().getCognome().isNull() || !model.getParams().getCodMediolanum().isNull()){%>
		<center>
		   <%   String colonne =  "cols='";
				colonne += "codInforete,codMediolanum,cognome,nome,dataNascita,naturaGiuridica,"+
				   	       "#codPotenziale,#datiApplicativi_nomeTabella,#isCancellabile,#sesso,#codCluster,#numVariazioni,"+
				           "#codAgente,#stato,#statoProposta,#statoConfermato,#codFiscale,#partitaIva,"+
				           "#isProspect,#isBozza,#isPotenziale,#isAcquisito,#isEffettivoPersonale,#isEffettivoRiassegnato,"+
				           "#isCointestatarioNonAssegnato,#isAssegnatoAdAltroAgente,#isTopBusiness,#descrCluster,"+
				           "#secondaIntestazione,#isDitta' ";
				String dimensioni = "colswidths='16%,12%,*,*,11%,11%' ";
		    %>
			<%=template.grid("elenco",colonne+dimensioni+
					                  "pageformname='ricercaClienti' "+
		                 			  "selection='single' "+
					                  "height='270' "+
					                  "width='680' "+
					                  "decorator='prgm.ita.anagraficaclienti.popup.model.PopupClientiModel' "+
					                  "onclick='selezionaCliente(this);'")%>
		    <%if(model.getElenco().size() > 0){%>
	           <%@ include file="../../include/Legenda.html"%>
	        <%}%>
		</center>
	<%}%>
	</form>
<%}%>

<script>
try{
document.ricercaClienti.params_cognome.focus();
}catch(e){}
</script>

<%=template.getFooter()%>
</body>
</html>
