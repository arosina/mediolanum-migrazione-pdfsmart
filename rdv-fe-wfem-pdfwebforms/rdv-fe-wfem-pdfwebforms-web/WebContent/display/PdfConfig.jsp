<%@page import="prgm.pdfwebforms.model.PdfConfigModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	template.getVariables().put("jQueryVersion","1.9.1");
	
	PdfConfigModel model = (PdfConfigModel)template.getPageDataModel();
%>
<html>
<head>
<%=template.getHeader()%>
<style>
.text{
    font-family: Arial;
    font-size: 10pt;
    color: #1A458F;
    font-weight: normal;
}
.inputField{
    font-family: Arial;
    font-size: 10pt;
    color: #1A458F;
    font-weight: normal;
}
</style>
<script>
function doApplica(){
	var input = new Object();
	input.msg = "Confermi l'applicazione della configurazione?";
	input.onlyCancel = false;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,doApplicaEnd,"Avviso",250,400);
	$("#wltPopupContainer").dialog({ position: { my: "top", at: "top", of: $("#bodyTab") } });
	return false;
}
function doApplicaEnd(ret){	
	if(ret == null)
		return false;
	startRequest();
	document.dati.submit();
	return false;	
}
function doLanciaFunzioneCodificheMessaggiMOM(){
	startRequest();
	document.lanciaFunzioneCodificheMessaggiMOM.submit();
	return false;	
}
</script>
</head>

<body>
<form name="lanciaFunzioneCodificheMessaggiMOM" id="lanciaFunzioneCodificheMessaggiMOM" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.messagecodesupdater.PdfMessageCodesUpdater.executeProcessOnNewStack">
</form>

<form name="dati" id="dati" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.ApplyPdfConfig.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<table width="100%" id="bodyTab">
	<tr>
		<td valign="top">
		<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			Configurazione globale Pdf
			</legend>
			<table class="text" width="100%">
				<tr>
					<td>Escludi integrazione con il servizio ProcessaDispositiva per i processi sede orizzontali</td>
					<td><%=template.field("praticheDigitaliDisabilitaCallSrvDispositiva_valore","showempty='false'")%></td>
				</tr>
				<tr>
					<td>ProcessaDispositiva - Escludi verifica semaforo per invio in fabbrica</td>
					<td><%=template.field("praticheDigitaliDisabilitaSemaforoMom_valore","showempty='false'")%></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<table><tr><td><%=template.action("applica","text='Applica'")%></td></tr></table>
					</td>
				</tr>
			</table>
		</fieldset>
		</td>
	</tr>
	<tr>
		<td valign="top">
		<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			Configurazione codifiche messaggi MOM
			</legend>
			<table class="text" width="100%">
				<tr>
					<td align="center">
						<table><tr><td><%=template.action("lanciaFunzioneCodificheMessaggiMOM","text='Lancia la funzione di configurazione codifiche messaggi MOM'")%></td></tr></table>
					</td>
				</tr>
			</table>
		</fieldset>
		</td>
	</tr>
</table>
</form>

<%=template.getFooter()%>

<% if(model.getApplyedMsg() != null){ %>
	<script>
	var input = new Object();
	input.msg = "<%=model.getApplyedMsg()%>";
	input.onlyCancel = true;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,null,"Avviso",250,400);
	$("#wltPopupContainer").dialog({ position: { my: "top", at: "top", of: $("#bodyTab") } });
	</script>
<% } %>
<% model.setApplyedMsg(null); %>

</body>
</html>
