<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="com.atosorigin.wfem.types.ListType"%>
<%@page import="prgm.pdfwebforms.model.PdfListModel"%>
<%@page import="com.atosorigin.wfem.types.AbstractTypePropertyDescriptor"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfList");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PdfListModel model = (PdfListModel)template.getPageDataModel();	 
%>

<html>

<head>
<%=template.getHeader()%> 

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/jquery-placeholder-plugin/jquery.placeholder.css'/>
<script src="<%=template.getWebApp()%>/jquery-placeholder-plugin/jquery.placeholder.js"></script>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfList.js"></script>
<script>
var jsAreas = "<%=model.getParams().getAreas()%>";
$( document ).ready(function() {
	try{
		$(":input[placeholder]").placeholder();
	}catch(e){}
});
</script>
</head>

<body>

<form name="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.NewPdf.executeOnNewThread">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="loadMappedPropertiesFields" value="false">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="pdfId" value="">
<input type="hidden" name="pdfInstanceId" value="">
<input type="hidden" name="compilationModes" value="">
<input type="hidden" name="pdfEnvironment" value="<%=model.getPdfEnvironment()%>">
<input type="hidden" name="gobackLabel" value="Torna all'elenco moduli">
<input type="hidden" name="gobackUrl" value="call.wfem?wfemCmd=executeCurrentDisplay&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="gobackEndLabel" value="Torna all'elenco moduli">
<input type="hidden" name="gobackEndUrl" value="call.wfem?wfemCmd=executeCurrentDisplay&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>">
</form>

<table height="100%" width="100%">
  <% if(!model.isPrimaVolta() || model.getPdfList().size() > 0){ %>
  <tr>
  	<td align="center">
		<form name="filtraForm" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.display.PdfList.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
  		<table cellpadding="0" cellspacing="0">
  			<tr>
  				<td>
  					<table>
  						<tr>
			  				<td><%=template.field("params_pdfDescr","uppercase='false' placeholder='Nome o codice del modulo...' size='79'")%></td>
  						</tr>
  					</table>
  				</td>
  			</tr>
  		</table>
  		</form>
  	</td>
  	<script>bindModuloAutocomplete();</script>
  </tr>
  <% } %>
  <tr>
    <td height="100%" id="resultCont" align="center">
			<%
			  String cols = "cols='#pdfId,#pdfCode,pdfDescr,comandi' ";
			  String dim = "colswidths='*,30%' ";
			%>
	        <%=template.grid("pdfList",cols+dim+"height='100%' width='100%' "+
	        									   "selection='none' "+
	        									   "onnewcell='newCell(this);' "+
	        									   "sortable='false' "+
	        									   "showtoparea='false' "+
	        									   "showbottomarea='false' "+
	        									   "showcounter='false' "+
	        									   "cellheight='60' "+
	        									   "cellsnowrap='false' "+
	        									   "decorator='prgm.pdfwebforms.display.PdfList' "+
	        									   (model.isPrimaVolta()?"noRowsMsg='Nessun modulo compilabile'":""))%>
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
<%
	model.setPdfList(new ListType(PdfAnagModel.class));
	model.setPrimaVolta(false);
%>
