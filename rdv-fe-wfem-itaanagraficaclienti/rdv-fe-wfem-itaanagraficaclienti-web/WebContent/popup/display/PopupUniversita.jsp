<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");  
	template.setPageName("PopupUniversita"); 
	template.setLabelPosition(template.LEFT_LABEL);
	template.setJSCombo(false);
	
	PopupUniversitaModel model = (PopupUniversitaModel)template.getPageDataModel();
	model.getElenco().setRowsInPage(12);
%>

<html>

<%=template.getHeader()%>

<script src="/ItaAnagraficaClienti/popup/display/PopupUniversita.js"></script>

<body srcoll="no">

<form name="ricercaUniversita" method="post" action="call.wfem">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.popup.display.PopupUniversita.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="isPrimaVolta" value="false">
<input type='hidden' name="index" id="index">
<input type='hidden' name="listPropertyName" id="listPropertyName">

<center>
<table width="80%">
<tr>
<td align="center">
	<fieldset class="fieldsGroup">
		<legend class="text">
			&nbsp;<b>Ricerca universit&agrave;</b>&nbsp;
			<img title="Pulisci campi" src="<%=template.getWebApp()%>/images/clear.gif" 
			onclick="pulisci();" style="cursor:pointer;">
		</legend>
		<table width="100%">
		  <tr>
		    <td align="center">
		      <table>
			    <tr><td><%=template.field("ateneo","size='44'")%></td><td><%=template.field("provincia")%></td></tr>
		        <tr><td colspan="2"><%=template.field("facolta")%></td></tr>
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

<%if(!model.getIsPrimaVolta().booleanValue()){%>
<fieldset id='elenco' name='elenco' style='border: none;'>
<center>
    <%
    	String colonne    = "cols='#codUniversita,ateneo,facolta,provincia' ";
		String dimensioni = "colswidths='*,25%,7%' ";
    %>
	<%=template.grid("elenco",colonne+dimensioni+"pageformname='ricercaUniversita' height='260' width='720' onclick='selezionaUniversita(this);'")%>
</center>
</fieldset>
<%}%>

</form>

<script>
try{
document.ricercaUniversita.ateneo.focus();
}catch(e){}
</script>

<%=template.getFooter()%>
</body>
</html>
