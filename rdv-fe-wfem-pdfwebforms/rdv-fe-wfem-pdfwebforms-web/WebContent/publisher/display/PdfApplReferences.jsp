<%@page import="prgm.pdfwebforms.publisher.common.CostantiPublisher"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfModuloModel"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfConfigurationModel"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfPublisherModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfApplReferences");
	
	PdfConfigurationModel confModel = (PdfConfigurationModel)template.getPageDataModel();
	PdfAnagModel pdfAnag = confModel.getPdfAnag();
%>

<html>

<head>
<%=template.getHeader()%> 
<script>
function newCell(cell){
	cell.align = 'center';
	if(cell.propertyName == 'applDescr')
		cell.align = 'left';
}
</script>
</head>

<body>

<table width="100%" height="100%">
	<tr>
	    <td>
			<%
			  String cols = "cols='applReference,pdfEnvironment,pdfCode,acroformVersion,applDescr' ";
			  String dim = "colswidths='28%,15%,15%,10%,*' ";
			%>
	        <%=template.grid("pdfAnag_pdfApplReferences",cols+dim+"height='100%' width='100%' "+
		        									   "title='Elenco riferimenti applicativi' "+
		        									   "cellheight='30' "+
		        									   "headerheight='40' "+
		        									   "cellsnowrap='false' "+
		        									   "sortable='false' "+
		        									   "showcounter='false' "+
		        									   "showbottomarea='false' "+
		        									   "showtoparea='false' "+
		        									   "selection='none' "+
    	        									   "onnewcell='newCell(this);' "+
    	        									   "decorator='prgm.pdfwebforms.publisher.display.PdfApplReferences' "+
		        									   "headernowrap='false'")%>
	    
	    </td>
	</tr>
</table>

<%=template.getFooter()%>
</body>
</html>
