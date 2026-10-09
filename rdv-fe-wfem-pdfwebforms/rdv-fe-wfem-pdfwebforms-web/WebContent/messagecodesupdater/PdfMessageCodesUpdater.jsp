<%@page import="prgm.pdfwebforms.messagecodesupdater.PdfMessageCodeModel"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	template.getVariables().put("jQueryVersion","1.9.1");

	PdfMessageCodeModel model = (PdfMessageCodeModel)template.getPageDataModel();
%>
<html>
<head>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<%=template.getHeader()%>
<script>
$( document ).ready(function() {
	try{
		$(".normalButton").bind({
			click: function(event){
				$(this).addClass("normalButtonSel");
			}
		}).mouseout(
			function(){ $(this).removeClass("normalButtonSel"); }
		).mouseenter(
			function(){ $(this).addClass("normalButtonSel"); }
		);
	}catch(e){}
	
	<% 	if(model.getResultMessage() != null){ %>
			var input = new Object();
			input.msg = "<%=model.getResultMessage()%>";
			input.onlyCancel = true;
			openModalPopup("prgm.pdfwebforms.display.PdfAlert",
					  		"BrowserInstance="+__getBrowserInstance(),
					  		input,null,"Avviso",250,400);
			$("#wltPopupContainer").dialog({ position: { my: "top", at: "top", of: $("#bodyTab") } });
	<% 		model.setResultMessage(null); 
		} %>
});

function selezionaItem(){
	startRequest();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.messagecodesupdater.PdfMessageCodesUpdater.execute";
	document.dati.submit();	
}

function doAggiorna(){
	if(document.dati.confItemName.value == ""){
		var input = new Object();
		input.msg = "Selezionare l'item";
		input.onlyCancel = true;
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		input,null,"Avviso",250,400);
		$("#wltPopupContainer").dialog({ position: { my: "top", at: "top", of: $("#bodyTab") } });
		return false;
	}
	
	if(document.dati.confItemProperties.value == ""){
		var input = new Object();
		input.msg = "Impostare le properites";
		input.onlyCancel = true;
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		input,null,"Avviso",250,400);
		$("#wltPopupContainer").dialog({ position: { my: "top", at: "top", of: $("#bodyTab") } });
		return false;
	}	

	var input = new Object();
	input.msg = "Confermi l'aggiornamento di <b>"+document.dati.confItemName.value+"</b>?";
	input.onlyCancel = false;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,doAggiornaEnd,"Avviso",250,400);
	$("#wltPopupContainer").dialog({ position: { my: "top", at: "top", of: $("#bodyTab") } });
	return false;
}

function doAggiornaEnd(ret){
	if(ret == null)
		return false;	
	startRequest();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.messagecodesupdater.UpdatePdfMessageCodes.execute";
	document.dati.submit();
}
</script>
</head>

<body>

<form name="dati" id="dati" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="wfemCmd">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<table width="100%" height="450" id="bodyTab">
	<tr>
		<td align="center">
			<table><tr><td class="text" style="font-size:16;">Item:</td><td><%=template.field("confItemName","style='font-size:16;' onchange='selezionaItem();'")%></td></tr></table>
		</td>
	</tr>
	<tr>
		<td align="center" style="height:100%;width:100%;">
			<table style="height:100%;width:100%;"><tr>
				<td>
					<textarea id="confItemProperties" name="confItemProperties" class="inputField" 
						style="white-space:nowrap;font-size:14;height:100%;width:100%;"><%=Tools.stringToHTMLString(model.getConfItemProperties().toString())%></textarea>
				</td>
			</tr></table>
		</td>
  	</tr>
	<tr>
		<td align="center">
			<table>
				<tr>
					<td><div class="normalButton" style="width:80px;" onclick="doAggiorna();">Aggiorna</div></td>
				</tr>
			</table>
		</td>
  	</tr>
</table>
</form>

<%=template.getFooter()%>
</body>
</html>
