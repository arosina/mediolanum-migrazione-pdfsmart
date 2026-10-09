<%@page import="prgm.pdfwebforms.publisher.nasutil.moveblob.MoveBlobModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("MoveBlob");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	MoveBlobModel model = (MoveBlobModel)template.getPageDataModel();	 
%>

<html>

<head>
<%=template.getHeader()%> 
<script>
function doEseguiAttivitaAction(){
	if(!window.confirm("Confermi lo spostamento dei pdf su NAS?"))
		return false;
	elaboraAction();
	return false;
}

function elaboraAction(){

	$("#elabResultCont").hide();
	$("#workingMessageRowTot").html("Avvio spostamento...");
	
	$("#workingMessage").dialog({
		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
		autoOpen: true, 
		modal: true,
		width: 350,
		height: 150,
		closeOnEscape: false,
		title: "Spostamento in corso..."
	   });

	setInterval( function() { 
					try{
						var tm=new Date().getTime(); 
						document.getElementById('utilIFrame').src='call.wfem?wfemCmd=prgm.pdfwebforms.publisher.nasutil.moveblob.MoveBlobSlider.execute&BrowserInstance='+__getBrowserInstance()+'&timenow='+tm;}catch(e){} 
				 }, 1000);

	document.dati.wfemCmd.value = "prgm.pdfwebforms.publisher.nasutil.moveblob.DoMoveBlob.execute";
	document.dati.submit();
	return false;	
}

function updateSlider(obj){
	if(!isActionEnabled("interrompiAction"))
		return;
	if(obj.pdfElaborati > 0)
		$("#workingMessageRowTot").html(""+obj.pdfElaborati+" pdf gestiti");
}

function doInterrompiAction(){
	try{
		enableAction("interrompiAction",false);
		$("#workingMessageRowTot").html("Interruzione in corso...");
		var tm=new Date().getTime(); 
		document.getElementById('utilIFrame').src='call.wfem?wfemCmd=prgm.pdfwebforms.publisher.nasutil.moveblob.DoInterrompi.execute&BrowserInstance='+__getBrowserInstance()+'&timenow='+tm;
	}catch(e){} 
}
</script>
</head>

<body>
<form name="dati" id="dati" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.nasutil.moveblob.DoMoveBlob.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="moveType" value="BlobToNas">

<table width="100%">
  
  <tr>
    <td style="padding: 10;">
		<fieldset class="fieldsGroup">
			<legend class="text" style="font-weight: bold;">
			    <img src="<%=template.getWebApp()%>/publisher/images/section.gif">
				&nbsp;Spostamento BLOB su NAS&nbsp;
			</legend>
			<table width="100%">
				<tr>
					<td align="center">
						<table>
						  <tr>
						  	<td><%=template.field("dataInizio")%></td>
						  	<td><%=template.field("dataFine")%></td>
						  </tr>
						</table>
					</td>
				</tr>

				<tr>
				  	<td align="center">
				  		<table>
				  			<tr><td><%=template.action("eseguiAttivitaAction")%></td></tr>
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
						<td  align="center"><%=template.action("interrompiAction")%></td>
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
