<%@page import="prgm.pdfwebforms.model.*"%>
<%@page import="prgm.pdfwebforms.basket.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	String closeBrowserInstances = "&closeBrowserInstances="+request.getAttribute("BrowserInstance");
%>
<html>	
<head>
<% if(model.isInAccettazioneCopernicoMobile()){ %>
	<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/PdfEventsMobile.js"></script>
<% }else{ %>
	<script src="<%=template.getWebApp()%>/copernicoprocess/accettazione/PdfEvents.js"></script>
<% } %>

<script>
var dispoOk = new Array();
var dispoKo = new Array();
<% if(model.isInBasket()){ %>
	<% for(BasketElement be : model.getBasket().getBasketElements()){ 
		PdfModel dispoPdf = be.getDispoPdf();
		String title = dispoPdf.getPdfData().getPdfTitle().isNull()?dispoPdf.mainPdfAnag().getPdfDescr().toString():dispoPdf.getPdfData().getPdfTitle().toString();
		title = title.replaceAll("\\\"", "\\\\\"");
	%>
		<% if(be.hasFreezeError()){ %>
			dispoKo.push({"title":"<%=title%>"});
		<% }else{ %>
			dispoOk.push({"title":"<%=title%>"});
		<% } %>	
	<% } %>
<% }else{ %>
	<%	PdfModel dispoPdf = model;
		String title = dispoPdf.getPdfData().getPdfTitle().isNull()?dispoPdf.mainPdfAnag().getPdfDescr().toString():dispoPdf.getPdfData().getPdfTitle().toString();
		title = title.replaceAll("\\\"", "\\\\\"");
	%>
		dispoOk.push({"title":"<%=title%>"});
<% } %>
</script>
</head>

<body onload="sendFine(dispoOk,dispoKo);">
	<%=prgm.pdfwebforms.core.PdfEndProcessLogDrawer.draw(model,"prgm.pdfwebforms.copernicoprocess.accettazione.EndAccettazioneCopernicoProcess",closeBrowserInstances)%>
	<% if(model.getMessaggioFineOperazione() != null){ %>
		<div style="display:none;"><%=model.getMessaggioFineOperazione()%></div>
	<% } %>
</body>
</html>
