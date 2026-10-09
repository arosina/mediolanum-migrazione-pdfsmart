<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");  
	template.setPageName("PopupComuni"); 
	template.setLabelPosition(template.LEFT_LABEL);
	template.setJSCombo(false);
	
	PopupComuniModel model = (PopupComuniModel)template.getPageDataModel();
	model.getElenco().setRowsInPage(30);
%>

<html>

<%=template.getHeader()%>

<script>
var jsCostanti_COD_NAZIONE_ITALIA = '<%=Costanti.COD_NAZIONE_ITALIA%>';
</script>
<script src="/ItaAnagraficaClienti/popup/display/PopupComuni.js"></script>

<body srcoll='no'>

<form name="ricercaComuni" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.popup.display.PopupComuni.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="isPrimaVolta" value="false">
<input type='hidden' name="index" id="index">
<input type='hidden' name="listPropertyName" id="listPropertyName">

<%
	String capDisplay = "";
	if(model.getIsRicercaIscrittiAlCatasto().booleanValue())
		capDisplay = "display:none;";
%>

<center>
<table width="80%">
<tr>
  <td>
	<fieldset class="fieldsGroup">
		<legend class="text">
			&nbsp;<b>Ricerca comuni / Località</b>&nbsp;
			<img title="Pulisci campi" src="<%=template.getWebApp()%>/images/clear.gif" 
			onclick="pulisci();" style="cursor:pointer;">
		<% if(model.getIsRicercaIscrittiAlCatasto().booleanValue()){ %>
			&nbsp;(vengono presi in considerazione solo i comuni accatastati)&nbsp;
		<% } %>
		</legend>
		<table width="100%">
		  <tr>
		    <td align="center">
		      <table>
		        <tr>
			     <td style="<%=capDisplay%>"><%=template.field("cap","type='num' maxlength='5' size='5' labelwidth='40%' fieldwidth='60%'")%></td>
			     <td><%=template.field("comune","size='50' labelwidth='40%' fieldwidth='60%'")%></td>
			     <td><%=template.field("provincia","maxlength='2' size='5' labelwidth='40%' fieldwidth='60%'")%></td>
		        </tr>
		      </table>
		    </td>
		  </tr>
		  <tr>
		     <td><%=template.action("eseguiRicercaAction")%></td>  
		  </tr>
		</table>
	</fieldset>
  </td>
</tr>
</table>
</center>

<hr><br>

<%if(!model.getIsPrimaVolta().booleanValue() ||
     (!model.getCap().isNull() || !model.getComune().isNull() || !model.getProvincia().isNull())){%>
<fieldset id='elenco' name='elenco' style='border: none;'>
<center>
    <%
    	String colonne    = "cols='cap,comune,provincia,#codComune' ";
		String dimensioni = "colswidths='15%,*,15%' ";
		if(model.getIsRicercaIscrittiAlCatasto().booleanValue()){
	    	colonne    = "cols='#cap,comune,provincia,#codComune' ";
			dimensioni = "colswidths='*,15%' ";
		}
    %>
	<%=template.grid("elenco",colonne+dimensioni+"pageformname='ricercaComuni' height='250' width='600' onnewcell='newCell(this);' onclick='selezionaComune(this);'")%>
</center>
</fieldset>
<%}%>

</form>

<script>
document.ricercaComuni.comune.focus();
</script>

<%=template.getFooter()%>

</body>
</html>
