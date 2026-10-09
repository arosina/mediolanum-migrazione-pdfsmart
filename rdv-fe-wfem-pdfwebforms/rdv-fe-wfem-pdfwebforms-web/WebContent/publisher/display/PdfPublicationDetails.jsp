<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfValidationWarningMessage");
	
	PdfAnagModel model = (PdfAnagModel)template.getPageDataModel();
%>

<html>

<head>
<%=template.getHeader()%> 
</head>

<body style="margin:0;">

<table width="100%" height="100%" class="text">
  <tr>
  	<td>
  		<table width="100%" class="text">
  			<tr><td>Pubblicato da:</td><td><%=model.getPdfPublishUser()%></td></tr>
  			<tr><td>Il:</td><td><%=model.getPdfPublishTime()%></td></tr>
  		</table>
  	</td>
  </tr>
  <tr>
    <td height="100%">
 		<div style="height:100%;width:100%;position:relative;overflow:auto;">
			<div style="position:absolute;left:0;top:0;">
				<%=model.getPdfValidationWarningMessage()%>
			</div>
		</div>
   </td>
  </tr>
</table>

<%=template.getFooter()%>
</body>
</html>
<% model.setPdfValidationWarningMessage(null); %>
