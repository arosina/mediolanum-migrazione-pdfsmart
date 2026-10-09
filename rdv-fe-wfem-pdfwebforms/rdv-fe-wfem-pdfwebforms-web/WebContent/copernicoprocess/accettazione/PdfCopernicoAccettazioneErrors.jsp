<!doctype html>
<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="prgm.pdfwebforms.model.PdfPersonModel"%>
<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="com.atosorigin.wfem.types.DoubleType"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	String closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");
	String endErrorTraceMsg = "";
	if(model.getEndErrorTraceMsg() != null){
		endErrorTraceMsg = "&endErrorTraceMsg="+model.getEndErrorTraceMsg();
	}
%>
<html lang="it">
	<head>
		<meta charset="utf-8" />
	    <meta name="format-detection" content="telephone=no" />
    	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    	
	    <link rel="stylesheet" href='<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/bootstrap/bootstrap.min.css' />
	    <link rel="stylesheet" href='<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/Styles.css' />
		
		<%template.getVariables().put("jQueryVersion","1.9.1");%>
		<%=template.getHeader()%>

		<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
		<% if(model.isInAccettazioneCopernicoMobile()){ %>
			<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/PdfEventsMobile.js"></script>
		<% }else{ %>
			<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/PdfEvents.js"></script>
		<% } %>
    	<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/KeyboardEvents.js"></script>	
    	<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/CustomModal.js"></script>
		
	</head>
	
	<body onload="onLoadPopupErrore();" class="tabArea">
	
		<%@ include file="./include/Errori.html"%>
	
		<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/bootstrap/bootstrap.bundle.min.js"></script>
	    <script type="text/javascript">
	      $(window).on('load', function () {
	    	  $('#errorModal').openCustomModal();
		      $('#errorModal').focus();
	      });
	    </script>

		<iframe src='call.wfem?wfemCmd=prgm.pdfwebforms.copernicoprocess.accettazione.ErrorsAccettazioneCopernicoProcess.execute&readRequest=false<%=closeBrowserInstances%>&pdfInstanceId=<%=model.mainPdfData().getPdfInstanceId()%><%=endErrorTraceMsg%>' style='display:none;'></iframe>
		<%=template.getFooter()%>		
	</body>
</html>
<% model.setErrorOnSign(false); %>
