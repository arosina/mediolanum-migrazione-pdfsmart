<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("RicercaClienti");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PopupClientiModel model = (PopupClientiModel)template.getPageDataModel();	 
	model.getElenco().setRowsInPage(13);		
%>

<html>
<head>
<%=template.getHeader()%>
<script>
var jsUrlNuovoPcp = '<%=model.getUrlNuovoPcp()%>';
</script>
</head>

<script src="/ItaAnagraficaClienti/display/Costanti.jsp"></script>
<script src="/ItaAnagraficaClienti/display/StatiPropostaAnagrafica.jsp"></script>
<script>var curStatoElementi = '<%=model.getParams().getStatoElementi()%>';</script>
<script src="/ItaAnagraficaClienti/questionari/display/RicercaClienti.js"></script>

<body scroll="no">

<form name="dati" method="post" action="call.wfem" target="_self">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="codMediolanum" value="">
<input type="hidden" name="codPotenziale" value="">
<input type="hidden" name="showBack" value="true">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="ricercaClienti" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.questionari.display.RicercaClienti.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="params_maxRows" value="50">
<input type="hidden" name="isPrimaVolta" value="false">

<center>

<table width="85%"><tr><td>
<fieldset class="fieldsGroup">
<legend class="text" style="font-weight: bold;">
    <img src="<%=template.getWebApp()%>/images/section.gif">
    <% if(model.getParams().getNomeFunzione().isNull()){%>
	&nbsp;Ricerca clienti per Creare o Modificare il <b>"Profilo dell' Investitore"</b>&nbsp;
	<%}else {%>
	&nbsp;Ricerca clienti per Modificare il <b>"Patrimonio vs terzi dell'Investitore'"</b>&nbsp;
	<%}%> 
	<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" 
	     onclick="pulisci();" style="cursor:pointer;">
</legend>
<table width="95%"> 
  <tr>
     <td colspan="3" style="height: 5"></td>
  </tr>
  <tr>
     <td><%=template.field("params_cognome","labelcode='Cognome' labelwidth='40%' size='35'  maxlength='40' ")%></td>
     <td><%=template.field("params_nome","labelcode='Nome' labelwidth='40%' size='35'  maxlength='40' ")%></td>
     <td><%=template.field("params_codMediolanum","maxlength='16' labelwidth='40%' size='25'")%></td>
  </tr>
  <tr>
     <td colspan="3" style="height: 5"></td>
  </tr>
  <tr>
     <td colspan="3"><%=template.action("ricercaAction","style='width:60px;'")%></td>  
  </tr>
  <tr>
     <td colspan="3" style="height: 5"></td>
  </tr>   
</table>
</fieldset>
</td></tr></table>
</center>

<fieldset id='elenco' name='elenco' style='border: none;'>
<%
	String colonne = "cols='";
	if(model.getClienteSelezionato().getCogestioneData().getIsUtenteCogestore().booleanValue())
		colonne += "agenteTitolare,";
	else if(model.getClienteSelezionato().getCogestioneData().getIsUtenteTitolare().booleanValue())
		colonne += "agenteCogestore,";
	colonne += "codInforete,codMediolanum,cognome,nome,dataNascita,naturaGiuridica,";
    colonne += "#sesso,#codPotenziale,#datiApplicativi_nomeTabella,#isCancellabile,";
    colonne += "#codAgente,#stato,#statoProposta,#statoConfermato,#codFiscale,#partitaIva,";
    colonne += "#isProspect,#isBozza,#isPotenziale,#isAcquisito,#isEffettivoPersonale,#isEffettivoRiassegnato,";
    colonne += "#isCointestatarioNonAssegnato,#isAssegnatoAdAltroAgente,#secondaIntestazione' ";
	       
	String dimensioni = "colswidths='";
	if(model.getClienteSelezionato().getCogestioneData().getIsUtenteCogestore().booleanValue())
		dimensioni += "15%,";
	else if(model.getClienteSelezionato().getCogestioneData().getIsUtenteTitolare().booleanValue())
		dimensioni += "15%,";
	dimensioni += "14%,12%,*,*,10%,10%' ";

    if(!model.getIsPrimaVolta().booleanValue()){

	     com.atosorigin.wfem.types.ListType elenco = model.getElenco();
	     if(elenco.size() > 0){%>
			<table>
	    <%}else{%>
			<table style='visibility:hidden;'>
	    <%}%>
			  <tr>
			     <td>
			     	<% 
			     	String azione ="doNuovoQuestionarioAction();";
			     		if(model.getParams().getNomeFunzione().isNull()){%>
			       		<%=template.action("nuovoQuestionarioAction","enabled='false'")%>
			       		
					<%}else {
						azione ="doNuovoPatrimonioAction();";%>
			       		<%=template.action("nuovoPatrimonioAction","enabled='false'")%>
			       	<%}%>	
			     </td>
			  </tr>
			  <tr><td style="height: 3pt;"></td></tr>
			</table>
	
		<center>
		<%=template.grid("elenco", 
		   				   colonne+dimensioni+
		   				   "decorator='prgm.ita.anagraficaclienti.popup.model.PopupClientiModel' "+
		                   "selection='single' pageformname='ricercaClienti' height='240' width='100%' "+
		                   "onclick='selCli(this);' ondblclick='selCli(this);"+azione+"'")%>
		                
		</center>

	    <%if(elenco.size() > 0){%>
			<center>
			<% if(model.getParams().getNomeFunzione().isNull()){%>
				<span class="text"><b>Seleziona un cliente e clicca sul pulsante "Crea / Modifica profilo"</b></span>
			<%}else {%>	
				<span class="text"><b>Seleziona un cliente e clicca sul pulsante "Modifica profilo"</b></span>	
			<%}%>		
	        <%@ include file="Legenda.html"%>
			</center>
        <%}
	
    }
%>

</fieldset>

<input type='hidden' name='index' id='index'>
<input type='hidden' name='listPropertyName' id='listPropertyName'>
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<script>
document.ricercaClienti.params_cognome.focus();
</script>

<%=template.getFooter()%>
</body>
</html>
