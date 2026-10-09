<%@page import="prgm.pdfwebforms.core.PdfXmlUtils"%>
<%@page import="prgm.pdfwebforms.core.PdfInfos"%>
<%@page import="prgm.pdfwebforms.core.PdfFieldInfos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.atosorigin.wfem.types.AbstractType"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	PdfDataModel pdfData = model.getPdfData();
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

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function doBack(){
	wait();
	document.gobackForm.submit();
	return false;
}
</script>
<%@ include file="./PdfReady.html"%>
</head>

<body>

<form name="gobackForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="executeLastDisplay">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<table class="htmlContainer">
	<tr>
		<td height="100%">
			<table height="100%" width="100%">
				<tr>
					<td>
						<table class="text">
							<tr>
								<td style="font-size: 20px;">Dati</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%" valign="top">
						<div style="position:relative;height:100%;width:100%;margin:0;overflow:auto;">
						<div style="position:absolute;left:0;top:0;margin:0;width:100%;">
						<table width="100%" class="text" border=1>
							<tr>
								<td style="background-color: #f0f0f0;"><b>Campo</b></td>
								<td style="background-color: #f0f0f0;"><b>Campo Pdf</b></td>
								<td style="background-color: #f0f0f0;"><b>Tipo</b></td>
								<td style="background-color: #f0f0f0;"><b>Valore</b></td>
							</tr>
							<%	ArrayList fieldNames = Tools.getAbstractTypeFieldNames(pdfData); %>
							<%	for(int i=0;i<fieldNames.size();i++){
									String htmlFieldName = (String)fieldNames.get(i);
									String pdfFieldName = "";
									PdfFieldInfos fi = model.getPdfData().getPdfInfos().findFieldInfoByHtmlName(htmlFieldName);
									if(fi == null)
										continue;
									pdfFieldName = fi.pdfFieldName;
									AbstractType field = (AbstractType)Tools.getPropertyValue(pdfData,htmlFieldName);
									String classname = field.getClass().getSimpleName();
							%>
								<tr>
									<td><%=htmlFieldName%></td>
									<td><%=pdfFieldName%></td>
									<td><%=classname%></td>
									<td><%=field%></td>
								</tr>
							<% } %>
						</table>
						</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td>
			<table width="100%">
				<tr>
					<td>
						<table class="text">
							<tr>
								<td style="font-size: 20px;">Xml</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<textarea class="text" rows="30" cols="50" style="width:100%;"><%=PdfXmlUtils.xmlFromModel(pdfData,model,true)%></textarea>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr><td style="padding-top: 10;"><div class="line">&nbsp;</div></td></tr>
	
	<tr>
		<td align="right" style="padding:10;padding-top:3;">
			<table>
				<tr>
	    			<td><div class="normalButton" onclick="doBack();">Indietro</div></td>			    		
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
