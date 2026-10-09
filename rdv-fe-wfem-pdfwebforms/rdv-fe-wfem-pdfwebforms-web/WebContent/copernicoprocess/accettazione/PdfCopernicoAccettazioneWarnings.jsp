<!doctype html>
<%@page import="com.atosorigin.wfem.command.CommandWarning"%>
<%@page import="com.atosorigin.wfem.types.BooleanType"%>
<%@page import="prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean"%>
<%@page import="prgm.pdfwebforms.model.PdfDataModel"%>
<%@page import="prgm.pdfwebforms.core.PdfPredefinedFields"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>
<%@page import="com.atosorigin.wfem.types.AbstractType"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.basket.Basket"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
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
	    		
		<script>
		function doContinua(){
			startWait();
			<% if(model.isInBasket()){ %>
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.accettazione.StartCopernicoSignBasketProcess.execute";
			<% }else{ %>
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.accettazione.StartCopernicoSignProcess.execute";
			<% } %>
			document.goOnForm.submit();
			return false;
		}
		</script>
	</head>
	
	<body onload="stopWait();" class="tabArea">	
		<form name="goOnForm" method="post" action="call.wfem" style="display:none;">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="scrollYValue" value="0">
		<input type="hidden" name="scrollXValue" value="0">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="checkCodDescFields" value="false">
		<input type="hidden" name="manageChangedFields" value="false">
		</form>
		
		<%@ include file="./include/Warnings.html"%>
			    
		<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/bootstrap/bootstrap.bundle.min.js"></script>
	    <script type="text/javascript">
	      $(window).on('load', function () {
	        $('#infoModal').openCustomModal();
	        $('#infoModal').focus();
	      });
	    </script>
	
		<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
		<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;" role="status" aria-live="polite" aria-hidden="true">
			<div class="waitAnchorContainer">
				<img id="waitAnchor" alt="Pagina in caricamento" src="<%=template.getWebApp()%>/jsWait/loading_0.gif">
			</div>
		</div>
		<%=template.getFooter()%>
	</body>
</html>