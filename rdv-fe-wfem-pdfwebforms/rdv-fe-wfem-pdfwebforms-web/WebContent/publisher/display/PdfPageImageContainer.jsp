<%@page import="prgm.pdfwebforms.publisher.model.PdfPageAnagModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	PdfPageAnagModel pdfPage = (PdfPageAnagModel)template.getPageDataModel();
%>

<html>
<head>
<title>Pdf <%=pdfPage.getPdfCode()%> - pag. <%=pdfPage.getPdfPageNum()%></title>
</head>
<body>
<table width="100%" height="100%">
 <tr>
   <td>
   	<img width="800px" src="call.wfem?wfemCmd=prgm.pdfwebforms.publisher.business.VisualizzaImmaginePagina.execute&pdfId=<%=pdfPage.getPdfId()%>&pdfPublicationId=<%=pdfPage.getPdfPublicationId()%>&pdfPageNum=<%=pdfPage.getPdfPageNum()%>">
    </td>
  </tr>
</table>
</body>
</html>
