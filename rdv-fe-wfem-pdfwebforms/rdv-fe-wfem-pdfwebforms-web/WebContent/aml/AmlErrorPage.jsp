<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.aml.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	AmlModel amlModel = model.getAmlModel();
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function doBack(){
	wait();
	document.goBackForm.submit();
	return false;
}
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

<form name="goBackForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GotoLastPdf.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<table width="100%">
<tr>
	<td style="height:100;">	
		<table>
			<tr>
				<td valign="top" style="padding: 30;">
					<table class='title'>
						<tr>
							<td style="font-size: 16;">
									<b>Attenzione:</b> <%=amlModel.getErrorMsg()%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
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
							<td><div class="normalButton" onclick="doBack();">Indietro</div></td>
						</tr>
					</table>
				</td>				
			</tr>
		</table>
	</td>
</tr>
</table>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>
