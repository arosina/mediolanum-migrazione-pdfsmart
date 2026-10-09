<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.anagraficaclienti.popup.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.Costanti"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL);
	
	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AllegaFotoClienti");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
    template.setModality(Template.READ_MODALITY);
	template.setPrefix("clienteSelezionato");
	
	PopupClientiModel model = (PopupClientiModel)template.getPageDataModel();	 
	ClienteModel cliente = model.getClienteSelezionato();
%>

<html>

<head>
<%=template.getHeader()%>
<script>
function doSalvaFotoAction(){
	if(document.getElementById("clienteSelezionato_fotografia_immagine").value == ""){
		alert("Seleziona il file della fotografia del cliente");
		return false;
	}
	parent.startRequest();
	document.dati.submit();
	return false;	
}
function onNewCell(cell){
	if(cell.propertyName == 'dataUpload'){
		cell.align = 'center';
	}
	if(cell.propertyName == 'statoRepository'){
		cell.align = 'center';
		if(cell.row.dataInvioRepository == '')
			cell.innerHTML = 'Inviata in sede';
		else if(cell.row.isInviataRepository == 'false')
			cell.innerHTML = 'In elaborazione in sede';
	}
}
</script>
</head>

<body>

<form name="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.allegafoto.SalvaFotoCliente.execute">
<input type="hidden" name="clienteSelezionato_codAgente" value="<%=cliente.getCodAgente()%>">
<input type="hidden" name="clienteSelezionato_codFiscale" value="<%=cliente.getCodFiscale()%>">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<table width="100%" height="100%">
 <tr>
    <td>
      	<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			    <img src="<%=template.getWebApp()%>/images/section.gif">
				&nbsp;Sezione per l'invio della fotografia&nbsp;
			</legend>
		    <table class="text">
		        <tr><td colspan="3" style="height:10px;"></td></tr>
		        <tr>
		          <td align="center"><b>1</b>) Selziona l'immagine con l'apposito pulsante (estensioni JPG,JPEG,PNG)</td>
		          <td style="width:40;"></td>
		          <td align="center"><b>2</b>) Utilizza il pulsante 'Invia in sede' per inviare in sede l'immagine selezionata</td>
		        </tr>
		        <tr><td colspan="3" style="height:10px;"></td></tr>
		        <tr>
		          <td><%=template.field("fotografia_immagine","modality='insert' labelalign='left' fieldalign='left' labelwidth='37%' labelcode='Fotografia (max. 2 MB)'")%></td>
		          <td style="width:40;"></td>
			      <td><%=template.action("salvaFotoAction","style='font-weight:bold;'")%></td>
		        </tr>
		        <tr><td colspan="2" style="height:10px;"></td></tr>
		    </table>
        </fieldset>
    </td>
 </tr>
 <tr><td style="height:20;"></td></tr>
 <% if(cliente.getElencoFotografie().size() > 0){ %>
 <tr height="100%">
    <td align="center">
	    <%
			String colonne =  "cols='nomeFile,dataUpload,statoRepository,#dataInvioRepository,#isInviataRepository'";
			String dimensioni = "colswidths='*,20%,30%' ";
	    %>
		<%=template.grid("elencoFotografie", 
				   				   colonne+dimensioni+
				   				   "title='Elenco delle fotografie gi&agrave; inviate in sede per il cliente selezionato' "+
				   				   "norowsmsg='Nessuna fotografia inviata in sede' "+
				   				   "sortable='fale' "+
				   				   "onnewcell='onNewCell(this);' "+
				                   "selection='none' height='100%' width='100%'")%>
    </td>
 </tr>
 <% } %>
</table>

<script>
parent.stopRequest();
<% if(model.hasCommandMessages()){ 
	String msg =  template.getProperty((com.atosorigin.wfem.command.CommandMessage)model.getCommandMessages().get(0));
	model.resetCommandMessages();
%>
	alert("<%=msg%>");
<% } %>
<% if(model.hasCommandErrors()){ 
	String msgErr =  template.getProperty((com.atosorigin.wfem.command.CommandError)model.getCommandErrors().get(0));
	model.resetCommandErrors();
%>
	alert("<%=msgErr%>");
<% } %>
</script>

<%=template.getFooter()%>
</body>
</html>
