<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	//template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);
	template.setWlt(true);
	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");  
	template.setPageName("PopupAgenti"); 
	template.setLabelPosition(template.UP_LABEL);
	//template.setJSCombo(false);
	
	PopupAgentiModel model = (PopupAgentiModel)template.getPageDataModel();
	model.getElenco().setRowsInPage(15);
%>

<html>

<head>
<%=template.getHeader()%>
<script src="/ItaAnagraficaClienti/popup/display/PopupAgenti.js"></script>
</head>

<body scroll="no" onload="document.ricercaAgenti.params_codAgente.focus();">

<form name="ricercaAgenti" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.popup.display.PopupAgenti.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="isPrimaVolta" value="false">
<input type='hidden' name="index" id="index">
<input type='hidden' name="listPropertyName" id="listPropertyName">

<center>
<table width="40%">
 <tr>
  <td>
	<fieldset class="fieldsgroup">
		<legend class="text" style="font-weight: bold;">
		    <img src="<%=template.getWebApp()%>/images/section.gif">
				&nbsp;Agenti&nbsp;
			<img title="Pulisci campi" src="<%=template.getWebApp()%>/images/clear.gif" 
			     onclick="pulisci();" style="cursor:pointer;">
		</legend>
		<table align="center" cellpadding="5" cellspacing="5">
		  <tr>
		     <td>
		        <table width="100%" cellpadding="0" cellspacing="0">
			       <tr><td><%=template.field("params_codAgente","tabindex='1' type='num' maxlength='10' size='15'")%></td></tr>
		        </table>
		     </td>
		     <td>
		        <table width="100%" cellpadding="0" cellspacing="0">
		     	   <tr><td><%=template.field("params_cognomeAgente","tabindex='2' size='30' maxlength='40'")%></td></tr>
		        </table>
		     </td>
		     <td style='width: 20;'></td>
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
      (!model.getParams().getCodAgente().isNull() || !model.getParams().getCognomeAgente().isNull())){%>
		<center>
		<fieldset id='elenco' name='elenco' style='border: none;'>
		<%  String colonne = "cols='";
			colonne += "codAgente,nominativoAgente,"+
					   "#cognomeAgente,#nomeAgente,#areaAgente,#serverReplica,#codAgenzia,#descrAgenzia,"+
					   "#codProvincia,#codiceContrattoAgente,#cicloVitaAgente' ";
			String dimensioni = "colswidths='20%,*' ";
		%>
		<%=template.grid("elenco",colonne+dimensioni+
						          "pageformname='ricercaAgenti' "+
		                 		  "selection='single' "+
		                 		  "height='200' "+
		                 		  "width='500' "+
		                 		  "onnewcell='newCell(this);' "+
		                 		  "onclick='selezionaAgente(this);'")%>
		</fieldset>
		</center>
<%}%>
</form>

<script>
document.ricercaAgenti.params_codAgente.focus();
</script>

<%=template.getFooter()%>
</body>
</html>
