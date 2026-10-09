<%@page import="prgm.pdfwebforms.core.PdfPredefinedFields"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
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
%>
<html>

<head>
<style>
body{
  margin: 0;
  padding: 0;
  border: 0;
  height: 100%;
  overflow: hidden;
}
</style>

<%=template.getHeader()%>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>

<%@ include file="./PdfReady.html"%>
</head>

<body>

<center>
<table class="htmlContainer">
	<tr>
		<td id="pageBody" height="100%" align="center" style="visibility:hidden;">
			<%PdfHtmlDrawer.drawAllPdf((java.io.Writer)out,request.getAttribute("BrowserInstance").toString(),template,model);%>
		</td>
	</tr>
</table>
</center>

<%=template.getFooter()%>
</body>
</html>
