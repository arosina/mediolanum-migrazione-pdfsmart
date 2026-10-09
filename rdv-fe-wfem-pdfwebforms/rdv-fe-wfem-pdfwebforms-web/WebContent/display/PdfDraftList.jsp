<%@page import="prgm.pdfwebforms.model.PdfInstanceListModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfDraftList");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PdfInstanceListModel model = (PdfInstanceListModel)template.getPageDataModel();	 
	boolean isSede = model.getParams().getIsSede().booleanValue(); 
%>

<html>

<head>
<%=template.getHeader()%> 
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfDraftList.js"></script>
</head>

<body>

<form name="apriForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.OpenPdf.executeOnNewThread">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfInstanceId" value="">
<input type="hidden" name="codRuoloImpersonato" value="<%=model.getCodRuoloImpersonato()%>">
<input type="hidden" name="gobackLabel" value="Torna ai moduli in bozza">
<input type="hidden" name="gobackUrl" value="call.wfem?wfemCmd=executeCurrentDisplay&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="gobackEndLabel" value="">
<input type="hidden" name="gobackEndUrl" value="">
</form>

<form name="cancellaForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.DeletePdf.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="idToDelete" value="">
</form>

<table height="100%" width="100%">
  <tr>
    <td height="100%" align="center" id="resultCont">
			<%
			  	String cols = "cols='#pdfStatus,#pdfAnag_pdfCode,pdfInstanceId,pdfAnag_pdfDescr,clienti,pdfCreationTime,comandi' ";
			  	String dim = "colswidths='15%,*,*,14%,10%' ";
				if(isSede){
					  cols = "cols='#pdfStatus,#pdfAnag_pdfCode,pdfInstanceId,pdfAnag_pdfDescr,agente,clienti,pdfCreationTime,comandi' ";
					  dim = "colswidths='15%,*,*,*,14%,10%' ";
				} 
			%>
	        <%=template.grid("pdfList",cols+dim+"height='100%' width='100%' "+
	        									   "selection='none' "+
	        									   "onnewcell='newCell(this);' "+
	        									   "sortable='false' "+
	        									   "showbottomarea='false' "+
	        									   "showcounter='false' "+
	        									   "cellheight='60' "+
	        									   "cellsnowrap='false' "+
	        									   "title='Elenco moduli in bozza' "+
	        									   "noRowsMsg='Nessun modulo in bozza' "+
	     	  									   "decorator='prgm.pdfwebforms.display.PdfDraftList'")%>
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
