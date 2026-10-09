<%@page import="prgm.pdfwebforms.publisher.crafter.util.PopolaCrafterUtilModel"%>
<%@page import="prgm.pdfwebforms.catalog.PdfCatalogModel"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfPublisherModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PopolaCrafterUtil");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PopolaCrafterUtilModel model = (PopolaCrafterUtilModel)template.getPageDataModel();	 
%>

<html>

<head>
<%=template.getHeader()%> 
<script>
function doAggiornaConfigurazioneAction(){
	if(!window.confirm("Confermi l'aggiornamento della configurazione?"))
		return false;
	
	document.dati.wfemCmd.value = "prgm.pdfwebforms.publisher.crafter.util.DoAggiornaConfigurazione.execute";
	document.dati.submit();
	return true;	
}

function doPopolaAction(){
	if(!window.confirm("Confermi il popolamento?"))
		return false;
	elaboraAction();
	return false;
}

function elaboraAction(){

	$("#elabResultCont").hide();
	$("#workingMessageRowTot").html("Avvio popolamento...");
	
	$("#workingMessage").dialog({
		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
		autoOpen: true, 
		modal: true,
		width: 350,
		height: 150,
		closeOnEscape: false,
		title: "Popolamento in corso..."
	   });

	setInterval( function() { 
					try{
						var tm=new Date().getTime(); 
						document.getElementById('utilIFrame').src='call.wfem?wfemCmd=prgm.pdfwebforms.publisher.crafter.util.PopolaCrafterSlider.execute&BrowserInstance='+__getBrowserInstance()+'&timenow='+tm;}catch(e){} 
				 }, 1000);
	
	document.dati.wfemCmd.value = "prgm.pdfwebforms.publisher.crafter.util.DoPopolaCrafter.execute";
	document.dati.submit();
	return false;	
}

function updateSlider(obj){
	if(!isActionEnabled("interrompiPopolamentoAction"))
		return;
	if(obj.pdfElaborati > 0)
		$("#workingMessageRowTot").html(""+obj.pdfElaborati+" pdf gestiti");
}

function doInterrompiPopolamentoAction(){
	try{
		enableAction("interrompiPopolamentoAction",false);
		$("#workingMessageRowTot").html("Interruzione in corso...");
		var tm=new Date().getTime(); 
		document.getElementById('utilIFrame').src='call.wfem?wfemCmd=prgm.pdfwebforms.publisher.crafter.util.DoInterrompiPopolamentoCrafter.execute&BrowserInstance='+__getBrowserInstance()+'&timenow='+tm;
	}catch(e){} 
}
</script>
</head>

<body>
<form name="dati" id="dati" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.crafter.util.DoPopolaCrafter.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">

<table width="100%">
  <tr>
    <td style="padding: 10;">
		<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			    <img src="<%=template.getWebApp()%>/publisher/images/section.gif">
				&nbsp;Configurazione aree popolate in pubblicazione&nbsp;
			</legend>
			<table width="100%">
				<tr>
					<td>
						<table width="100%">
						  <tr><td>&nbsp;</td></tr>
						  <tr><td class="text">Indicare in questo campo le <b>aree</b> che si desidera vengano <b>considerate per il popolamento in pubblicazione</b>, con valori separati da virgola.</td></tr>
						  <tr><td><%=template.field("areeConfigurate","labelposition='nolabel' labelalign='left' style='width:100%;'")%></td></tr>
						  <tr><td>&nbsp;</td></tr>
						</table>
					</td>
				</tr>
				
				<tr>
				  	<td align="center">
				  		<table>
				  			<tr><td><%=template.action("aggiornaConfigurazioneAction")%></td></tr>
				  		</table>
				  	</td>
			  	</tr>
			</table>

		</fieldset>
	</td>
  </tr>
  
  <tr>
    <td style="padding: 10;">
		<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			    <img src="<%=template.getWebApp()%>/publisher/images/section.gif">
				&nbsp;Popolamento moduli crafter&nbsp;
			</legend>
			<table width="100%">
				<tr>
					<td>
						<table width="100%">
						  <tr><td>&nbsp;</td></tr>
						  <tr><td class="text">Indicare in questo campo le <b>aree</b> dei pdf da considerare, con valori separati da virgola.</td></tr>
						  <tr><td><%=template.field("aree","labelposition='nolabel' labelalign='left' style='width:100%;'")%></td></tr>
						  <tr><td>&nbsp;</td></tr>
						  <tr><td class="text">oppure nel campo sottostante i <b>singoli indicatori</b> di pdf (id, codice o codice mom), con valori separati da virgola e/o su più righe.</td></tr>
						  <tr><td><%=template.field("indicatori","uppercase='false' labelposition='nolabel' labelalign='left' rows='5' style='width:100%;'")%></td></tr>
						  <tr><td>&nbsp;</td></tr>
						</table>
					</td>
				</tr>

				<tr>
				  	<td align="center">
				  		<table>
				  			<tr><td><%=template.action("popolaAction")%></td></tr>
				  		</table>
				  	</td>
			  	</tr>

				<tr>
				  <td style="padding: 10;" id="elabResultCont">
						<% if(model.getResultMessage() != null){ %>
						<fieldset class="fieldsGroup">
							<legend class="text" style="font-weight: bold;">
							    <img src="<%=template.getWebApp()%>/publisher/images/section.gif">
								&nbsp;Risultato&nbsp;
							</legend>
							<table width="100%">
							  <tr><td class="text"><%=model.getResultMessage()%></td></tr>
							</table>
						</fieldset>
					 	<% } %>    
				  </td>
				</tr>
			</table>

		</fieldset>
    </td>
  </tr>
    
  <tr>
    <td>
		<%=template.getMessagesAndErrors()%>
    </td>
  </tr>
</table>

<div id="workingMessageCont">
<div id="workingMessage" style="display:none;">
	<table width="100%">
		<tr>
			<td align="center">
				<table class="text" width="200" style="font-weight: bold; font-size: 14;">
					<tr>
						<td align="center" id="workingMessageRowTot" style="height: 40;"></td>
					</tr>
					<tr>
						<td  align="center"><%=template.action("interrompiPopolamentoAction")%></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
</div>

<%=template.getFooter()%>
</form>
</body>
</html>
