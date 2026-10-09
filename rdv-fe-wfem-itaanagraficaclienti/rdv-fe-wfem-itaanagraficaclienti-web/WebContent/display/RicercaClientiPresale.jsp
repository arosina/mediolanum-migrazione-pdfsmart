<%@ page import="com.atosorigin.wfem.types.*"%>
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
    ListType elenco = model.getElenco();
	elenco.setRowsInPage(30);		
%>

<html>

<head>
<%=template.getHeader()%>
<script src="/ItaAnagraficaClienti/display/Costanti.jsp"></script>
<script src="/ItaAnagraficaClienti/display/StatiPropostaAnagrafica.jsp"></script>
<script>var curStatoElementi = '<%=model.getParams().getStatoElementi()%>';</script>
<script src="/ItaAnagraficaClienti/display/RicercaClientiPresale.js"></script>
</head>

<body>

<form name="dati" method="post" action="call.wfem" target="_self">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="runtimeModality" value="<%=model.getRuntimeModality()%>">
<input type="hidden" name="codMediolanum" value="">
<input type="hidden" name="codPotenziale" value="">
<input type="hidden" name="partitaIva" value="">
<input type="hidden" name="codFiscale" value="">
<input type="hidden" name="codAgente" value="">
<input type="hidden" name="showBack" value="true">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="callingAppl" value="">
</form>

<form name="ricercaClienti" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.display.RicercaClientiPresale.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="params_codMediolanumIsCodPotenziale" value="true">
<input type="hidden" name="isPrimaVolta" value="false">
<input type="hidden" name="params_maxRows" value="50">
<input type='hidden' name='index' id='index'>
<input type='hidden' name='listPropertyName' id='listPropertyName'>

<table height="100%" width="100%">

<tr>
  <td>
	<fieldset class="fieldsGroup">
	<legend class="text" style="font-weight: bold;">
	    <img src="<%=template.getWebApp()%>/images/section.gif">
		&nbsp;Ricerca Clienti&nbsp;
		<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" 
		     onclick="pulisci();" style="cursor: pointer;">
	</legend>
	<table width="95%"> 
	  <tr>
       	 <td><%=template.field("params_cognome","labelwidth='50%' size='30'  maxlength='40' ")%></td>
       	 <td><%=template.field("params_nome","labelwidth='50%' size='30' maxlength='40' ")%></td>
         <td><%=template.field("params_codMediolanum","labelcode='RicercaClienti.params_codPotenziale' maxlength='16' labelwidth='50%' size='22'")%></td>
	  </tr>
	  <tr>
	     <td colspan="3" style="height: 5"></td>
	  </tr>
	  <tr>
	     <td colspan="3"><%=template.action("ricercaAction")%></td>  
	  </tr>
	  <tr>
	     <td colspan="3" style="height: 5"></td>
	  </tr>
	</table>
	</fieldset>
  </td>
</tr>

<% if(elenco.size() > 0){ %>
<tr>
  <td>
	<table>
	  <tr>
	     <td><%=template.action("apriAction","enabled='false'")%></td>
	     <td><%=template.action("cancellaAction","enabled='false'")%></td>
	  </tr>
	</table>
  </td>
</tr>
<% } %>

<tr>
  <td height="100%">
  <% if(!model.getIsPrimaVolta().booleanValue()){ %>
	<%
		String colonne =  "cols='codInforete,#codMediolanum,cognome,nome,dataNascita,";
		       colonne += "#codPotenziale,#datiApplicativi_nomeTabella,#isCancellabile,";
		       colonne += "#codAgente,#stato,#statoProposta,#statoConfermato,#codFiscale,#partitaIva,";
		       colonne += "#isProspect,#isBozza,#isPotenziale,#isAcquisito,#isEffettivoPersonale,#isEffettivoRiassegnato,";
		       colonne += "#isCointestatarioNonAssegnato,#isAssegnatoAdAltroAgente,#secondaIntestazione' ";
		String dimensioni = "colswidths='15%,*,*,10%' ";
	%>
	<%=template.grid("elenco", 
	   				   colonne+dimensioni+
	                   "selection='single' pageformname='ricercaClienti' height='100%' width='100%' onnewcell='newCell(this);' "+
	                   "onclick='selCli(this);' ondblclick='selCli(this);startRequest();doApriAction();'")%>
  <% } %>
  </td>
</tr>

<% if(elenco.size() > 0){ %>
<tr>
  <td align="center" class="text">
  	<b>Seleziona un cliente e clicca sull'opzione desiderata</b>
  </td>
</tr>
<% } %>

</table>

</form>

<script>
document.ricercaClienti.params_cognome.focus();
</script>

<%=template.getFooter()%>
</body>
</html>
