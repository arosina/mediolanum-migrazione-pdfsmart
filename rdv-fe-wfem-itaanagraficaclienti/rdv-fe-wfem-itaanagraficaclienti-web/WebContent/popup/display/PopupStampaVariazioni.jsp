<%@ page import="prgm.ita.anagraficaclienti.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");  
	template.setPageName("PopupStampaVariazioni"); 
	template.setLabelPosition(template.UP_LABEL);
	template.setJSCombo(false);
	
	ClienteModel model = (ClienteModel)template.getPageDataModel();
%>

<html>

<head>
<%=template.getHeader()%>
<script src="/ItaAnagraficaClienti/popup/display/PopupStampaVariazioni.js"></script>
</head>

<body>

<form name="stampa" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.business.StampaVariazione.execute">
<input type="hidden" name="codAgente" value="<%=model.getCodAgente()%>">
<input type="hidden" name="codPotenziale" value="<%=model.getCodPotenziale()%>">
<input type="hidden" name="progressivo" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<%
	String cols = "cols='progressivo,dataVariazione,#nome,#cognome,#codAgente,#codPotenziale,#codMediolanum,#codFiscale,#partitaIva,#tipoCarta' ";
	String dims = "colswidths='12%,*' ";
%>
<table height="100%" width="100%">
 <tr style="height:30%;">
 	<td align="center" class="text">
 		<% if(model.getDatiApplicativi().getMessaggioCentrale() != null && model.getDatiApplicativi().getMessaggioCentrale().length() > 0){ %>
 			<b><%=model.getDatiApplicativi().getMessaggioCentrale()%></b>
 		<% }else{ %>
	 		<%=template.grid("variazioni_elencoVariazioni",cols+dims+"selection='single' "+
											 			"title='Elenco variazioni in corso. <span style=\"color:green;\">Seleziona la variazione desiderata per produrre la scheda di stampa</span>' "+
	 													"onclick='selVar(this);' height='100%' width='100%'")%>
		<% } %>
	</td>
 </tr>
 <tr style="height:70%;">
 	<td>
 		<div id="printObject" style="height: 100%;width: 100%;"></div>
 	</td>
 </tr>
</table>

<%=template.getFooter()%>
</body>
</html>
