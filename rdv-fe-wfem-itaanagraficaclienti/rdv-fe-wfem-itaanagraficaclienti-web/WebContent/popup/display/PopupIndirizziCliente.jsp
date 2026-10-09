<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setApplCode("ITAANAGRAFICACLIENTIPOPUP");
	template.setPageName("PopupIndirizziCliente");
   
	PopupIndirizziClienteModel model = (PopupIndirizziClienteModel)template.getPageDataModel();
%>

<html>
<body>

<%=template.getHeader()%>

<script src="/ItaAnagraficaClienti/popup/display/PopupIndirizziCliente.js"></script>

<center>
<fieldset id='elenco' name='elenco' style='border: none;'>

<%
	String colonne = 	"cols='indirizzoCompleto,comune,cap,provincia,codNazione,presso,#codComune,#tipoIndirizzo,#toponimoIndirizzo,#descrizioneIndirizzo,#numeroCivico' ";
	String dimensioni = "colswidths='*,*,8%,8%,10%,*' ";
%>
	<%=template.grid("indirizzi_elencoAttributi", colonne+dimensioni+
	                 			"selection='single' "+
	                 			"height='160' width='850' "+
	                 			"onnewrow='newRow(this);' "+
	                 			"onnewcell='newCell(this);' "+
	                 			"onclick='selezionaIndirizzo(this);' "+
	                 			"noRowsMsg='"+template.getProperty("PopupIndirizziCliente.nessunRisultato")+"'")%>
</fieldset>
<br>
<table>
  <tr>
  	<td class="text">L'indirizzo di residenza è evidenziato con lo sfondo <span style="border: solid 1px gray; background-color: lavender; width: 10pt;">&nbsp;</span></td>
  </tr>
</table>
</center>

<%=template.getFooter()%>
</body>
</html>
