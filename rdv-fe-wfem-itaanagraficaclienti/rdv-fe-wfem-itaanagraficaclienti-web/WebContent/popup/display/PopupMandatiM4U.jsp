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
<script src="/ItaAnagraficaClienti/popup/display/PopupMandatiM4U.js"></script>
</head>

<body scroll="no">

<form name="ricercaClienti" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.popup.display.PopupMandatiM4U.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
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
			&nbsp;Ricerca presentatori M4U&nbsp;
			<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" 
			     onclick="pulisci();" style="cursor:pointer;">
		</legend>
		<center>
		<table align="center" cellpadding="5" cellspacing="5">
		  <tr>
		     <td>
		        <table width="100%" cellpadding="0" cellspacing="0">
		          <tr><td><%=template.field("params_cognome","labelcode='Cognome' labelwidth='50%' size='50' maxlength='40'")%></td></tr>
		        </table>
		     </td>
		     <td>
		        <table width="100%" cellpadding="0" cellspacing="0">
		          <tr><td><%=template.field("params_codMediolanum","type='num'  maxlength='16' labelwidth='50%' size='30'")%></td></tr>
		        </table>
		     </td>
		     <td style='width: 20;'></td>
		  </tr>
		  <tr>
		     <td colspan="3"><%=template.action("eseguiRicercaAction")%></td>  
		  </tr>
		</table>
		</center>
	</fieldset>
  </td>
 </tr>
</table>
</center>

<hr><br>

<%if(!model.getIsPrimaVolta().booleanValue()){%>
	<center>
	<fieldset id='elenco' name='elenco' style='border: none;'>
	   <%   String  colonne =  "cols='";
					colonne += "codMediolanum,cognome,nome,dataNascita,#codInforete,#codPotenziale' ";
			String dimensioni = "colswidths='13%,*,*,12%' ";
	    %>
		<%=template.grid("elenco",colonne+dimensioni+
				                  "pageformname='ricercaClienti' "+
	                 			  "selection='single' "+
				                  "height='270' "+
				                  "width='680' "+
				                  "onclick='selezionaCliente(this);'")%>
	</fieldset>
	</center>
<%}%>
</form>

<script>
try{
document.ricercaClienti.params_cognome.focus();
}catch(e){}
</script>

<%=template.getFooter()%>
</body>
</html>
