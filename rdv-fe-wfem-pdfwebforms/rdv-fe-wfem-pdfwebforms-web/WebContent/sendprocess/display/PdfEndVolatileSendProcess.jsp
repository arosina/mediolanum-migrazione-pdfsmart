<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="com.atosorigin.wfem.types.IntegerType"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
<%@page import="com.atosorigin.wfem.types.AbstractType"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	PdfModel model = (PdfModel)template.getPageDataModel();
	
	PdfPersonModel firstFilledPerson =  model.firstFilledPerson();
	String codCliente = "";
	if(firstFilledPerson != null && !firstFilledPerson.getNdg().isNull())
		codCliente = "&codCliente="+firstFilledPerson.getNdg();
	
	String closeBrowserInstances = "";
	if(!model.getPdfData().getIsOnSameStack().booleanValue())
		closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");
%>
<html>

<head>
<%=template.getHeader()%>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function callackToMainProc(){
	<% if(!model.getPdfData().getGobackEndUrl().isNull()){ %>
		wait();
		location.href="<%=model.getPdfData().getGobackEndUrl()%>";
	<% } %>
	return;
}
</script>
</head>

<body onload="callackToMainProc();">

<%=prgm.pdfwebforms.core.PdfEndProcessLogDrawer.draw(model,"prgm.pdfwebforms.sendprocess.business.EndSendProcess","&isVolatile=true")%>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>
