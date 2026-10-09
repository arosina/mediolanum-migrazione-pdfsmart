<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.aml.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	CoraModel coraModel = model.getCoraModel();
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/aml/Aml.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function doBack(){
	wait();
	document.goBackForm.submit();
	return false;
}
function doProsegui(){
	wait();
	document.proseguiForm.submit();
	return false;
}
$( document ).ready(function() {
	$(".normalButton").attr({"tabindex":"0", "role":"button"});
	try{
		$("[onclick]").bind({
			keypress: function(event){
				if(event.which == 13 || event.which == 32){
					this.click();
				}
			}			
		});
	}catch(e){}
});
</script>
<style>
body{
	margin: 0;
	padding: 0;
	border: 0;
	height: 100%;
	overflow: hidden;
}
</style>
</head>

<body>

<% 
	String gotoBackCmd = "prgm.pdfwebforms.business.GotoLastPdf"; 
	if(model.getAmlModel() != null)
		gotoBackCmd = "prgm.pdfwebforms.aml.AmlPage"; 
%>
<form name="goBackForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="<%=gotoBackCmd%>.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<table class="htmlContainer">
	<tr>
		<td valign="top" class="titolo" style="padding: 10;">
			Valutazione dell'operazione complessiva
		</td>
	</tr>
	<tr>
		<td height="100%" align="center">
			<form name="proseguiForm" method="post" action="call.wfem">
			<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.aml.ConfirmCora.execute">
			<input type="hidden" name="modelHashCode" value="<%=model.hashCode()%>">
			<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
			<table>
				<tr><td class="sottotitolo">Indicare la tipologia relativa alla proposta</td></tr>
				<tr><td style="height:30;"></td></tr>
				<tr>
					<td align="center">
						<table class="inputLabel"><tr>
							<td>
								<input tabindex="0" type="radio" name="coraModel_codRating" 
									   value="1"
									   <%=coraModel.getCodRating().equals("1")?"checked":"" %>>
							</td>
							<td>TIPOLOGIA A</td>
							<td style="width:50;"></td>
							<td>
								<input tabindex="0" type="radio" name="coraModel_codRating" 
									   value="2"
									   <%=coraModel.getCodRating().equals("2")?"checked":"" %>>
							</td>
							<td>TIPOLOGIA B</td>
						</tr></table>
					</td>
				</tr>
			</table>
			</form>
		</td>
	</tr>
	<tr><td style="padding-top: 10;"><div class="line">&nbsp;</div></td></tr>
	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td style="padding:10;padding-top:3;">
						<table>
							<tr>
								<td><div class="normalButton whiteButton" onclick="doBack();">Indietro</div></td>
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
						<table>
							<tr>
								<td><div class="normalButton" onclick="doProsegui();">Avanti</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</center>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>
