<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfConfigurationModel"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfPublisherModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfArchivedList");
	template.setLabelAlign("right");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setJSCombo(false);
	
	PdfConfigurationModel confModel = (PdfConfigurationModel)template.getPageDataModel();
	PdfAnagModel pdfAnag = confModel.getPdfAnag();
%>

<html>

<head>
<style>
form{
	margin: 0;
}
</style>
<%=template.getHeader()%> 
<script src="<%=template.getWebApp()%>/publisher/display/PdfArchivedList.js"></script>
</head>

<body>

<table width="100%" height="100%">

  <tr>
    <td height="100%">
    	<%
    		String archiv = "";
    		if(confModel.getPdfNumArchiviazioni().intValue() > 0)
    			archiv = " (<span style=\"cursor:pointer;text-decoration:underline;font-weight:normal;\">"+confModel.getPdfNumArchiviazioni()+" archiviati</span>)";

       		String cols = "pdfPublicationId,pdfAcroformVersion,pdfDriverVersion,pdfEdition,pdfMomVersion,pdfPubStartDate,comandi";
       		String colswidths = "15%,10%,14%,*,12%,12%,20%";
    		
    	%>
	   	<%=template.grid("pdfArchivedList",""+
	   									  "cols='"+cols+"' "+
	   									  "colswidths='"+colswidths+"' "+
	   									  "cellheight='25' "+
	  									  "sortable='false' "+
	  									  "headernowrap='false' "+
	  									  "selection='none' "+
	  									  "showcounter='false' "+
	  									  "showbottomarea='false' "+
	  									  "onnewcell='onNewCell(this);' "+
	  									  "decorator='prgm.pdfwebforms.publisher.display.PdfArchivedList' "+
	  									  "height='100%' width='100%'")%>
    </td>
  </tr>
</table>

<%=template.getFooter()%>
</body>
</html>
<% confModel.setPdfArchivedList(null); %>
