<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfPublicationNotes");
	
	PdfAnagModel model = (PdfAnagModel)template.getPageDataModel();
%>

<html>

<head>
<%=template.getHeader()%> 
</head>

<body>

<table width="100%" height="100%">

  <tr>
    <td height="100%">
    	<textarea class="text" style="width:100%;height:100%;" readonly><%=model.getPdfPublicationNotes()%></textarea>
    </td>
  </tr>
</table>

<%=template.getFooter()%>
</body>
</html>
<% model.setPdfPublicationNotes(null); %>
