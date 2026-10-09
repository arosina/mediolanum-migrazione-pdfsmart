<!doctype html>
<%@page import="com.atosorigin.wfem.types.*"%>
<%@page import="com.atosorigin.wfem.command.CommandError"%>
<%@page import="prgm.pdfwebforms.model.*"%>
<%@page import="prgm.pdfwebforms.core.PdfPredefinedFields"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlDrawer"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	PdfPersonModel personaCorrente = model.getPersonaCorrente();
	PdfPersonSignDataModel signData = personaCorrente.getSignData();
	boolean mostraOtp = model.getPersonaCorrente().getSignData().isStepFlagSignSuperato();
%>
<html lang="it">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<%template.getVariables().put("jQueryVersion","1.9.1");%>
		<%=template.getHeader()%>
		
		
		<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/bootstrap/bootstrap.min.css'/>
		<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/font-awesome/css/all.min.css'/>
		<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/Styles.css'/>
		
		<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
		<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>
		
		<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
		<script src="<%=template.getWebApp()%>/display/PdfPageFields.js"></script>
		<script src="<%=template.getWebApp()%>/display/PdfPageDataentryUtil.js"></script>
		<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/PdfCopernicoPersonSign.js"></script>
		<% if(model.isInAccettazioneCopernicoMobile()){ %>
			<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/PdfEventsMobile.js"></script>
		<% }else{ %>
			<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/PdfEvents.js"></script>
		<% } %>
    	<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/resources/bootstrap/bootstrap.bundle.min.js"></script>
    	<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/KeyboardEvents.js"></script>
    	<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/CustomModal.js"></script>
		
		
		<script>
		var jsIsInBasket = <%=model.isInBasket()%>;
		var jsIsInAccettazioneCopernicoMobile = <%=model.isInAccettazioneCopernicoMobile()%>;
		</script>
	</head>
	
	<body onload="onPageLoad();" class="tabArea">
		<form name="dati" id="dati" method="post" action="call.wfem">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="modelHashCode" value="<%=model.hashCode()%>">
		<input type="hidden" name="dataHashCode" value="<%=model.getPdfData().getPdfIndex()%>">
		<input type="hidden" name="checkCodDescFields" value="false">
		<input type="hidden" name="manageChangedFields" value="false">
		<input type="hidden" name="scrollYValue" value="">
		<input type="hidden" name="scrollXValue" value="">
		
		<% if(!mostraOtp){ %>
			<script>setTitolo("<%=model.getPdfData().getPdfTitle().isNull()?model.mainPdfAnag().getPdfDescr().toString():model.getPdfData().getPdfTitle().toString()%>");</script>
			<%@ include file="./include/CheckSign.html"%>
			<script>
				if (window.screen.width <= 767) {
					$(".pdf-sign-main-container").addClass("pdf-sign-for-mobile");
					var viewportMeta = document.querySelector('meta[name="viewport"]');
					if (viewportMeta) {
					    viewportMeta.setAttribute('content', 'user-scalable=no');
					} else {
					    viewportMeta = document.createElement('meta');
					    viewportMeta.setAttribute('name', 'viewport');
					    viewportMeta.setAttribute('content', 'user-scalable=no');
					    document.head.appendChild(viewportMeta);
					}
				}
				loadSign();
			</script>
		<% }else{ %>
			<script>setTitolo("Riepilogo e firma");</script>
			<%@ include file="./include/PinOtp.html"%>
		<% } %>
		</form>
		
		<% if(model.hasCommandErrors() || signData.getDigit1DaChiedere().hasTypeErrors() || signData.getOtpDigitato().hasTypeErrors()){
			if(signData.getDigit1DaChiedere().hasTypeErrors()){
				model.addCommandError(signData.getDigit1DaChiedere().getTypeErrors().get(0).toString());
			}else if(signData.getOtpDigitato().hasTypeErrors()){
				model.addCommandError(signData.getOtpDigitato().getTypeErrors().get(0).toString());
			}
		%>
			<%@ include file="./include/ErroriInFirma.html"%>
			<%
				model.resetCommandErrors();
				signData.getDigit1DaChiedere().resetTypeErrors();
				signData.getOtpDigitato().resetTypeErrors();
			%>
			<% if(!mostraOtp){ %>
		    <script type="text/javascript">
		    	if (window.screen.width <= 767) {
					$("#errorReportMessage").addClass("modal-mobile");
				}
		    </script>
		    <% } %>
			<script type="text/javascript">
		        $('#errorReportMessage').on('open-custom-modal', function () {
		        	$('.main-content-readable').attr('aria-hidden', 'true');
		            $('#errorReportMessage').focus();
		            trapPageFocus('#errorReportMessage button');
		        });
		        
		        $('#errorReportMessage').on('close-custom-modal', function (event) {
		        	$('.main-content-readable').attr('aria-hidden', 'false');
		        	$('#errorReportMessage button').attr("tabindex", "-1");
	    	  		trapPageFocus();
	    	  		focusOnFirstInvalidField();
	    		});
		        
		        $('#errorReportMessage').openCustomModal();
		    </script>
		    
		<% } %>
		
		<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
		<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;" role="status" aria-live="polite" aria-hidden="true">
			<div class="waitAnchorContainer">
				<img id="waitAnchor" alt="Pagina in caricamento" src="<%=template.getWebApp()%>/jsWait/loading_0.gif">
			</div>
		</div>		
		<%=template.getFooter()%>
	</body>
</html>