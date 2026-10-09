<%@page import="prgm.pdfwebforms.catalog.PdfCatalogModel"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfPublisherModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfPublisher");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PdfPublisherModel model = (PdfPublisherModel)template.getPageDataModel();	 
	String areaDescr = model.getDescValue("area");
	if(areaDescr == null || areaDescr.length() == 0)
		areaDescr = model.getArea().toString();
%>

<html>

<head>
<script src="<%=template.getWebApp()%>/publisher/display/PdfPublisher.js"></script>
<%=template.getHeader()%> 
<script>
function onChangeArea(areaObj){
	document.changeAreaForm.area.value = areaObj.value;
	document.changeAreaForm.submit();
	startRequest();
}
</script>
</head>

<body>

<form name="changeAreaForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.display.PdfPublisher.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="area" value="">
<input type="hidden" name="eseguiRicerca" value="false">
</form>

<form name="nuovoForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.NuovoPdf.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfArea" value="<%=model.getArea()%>">
<input type="hidden" name="profiloUtente" value="<%=model.getProfiloUtente()%>">
</form>

<form name="apriForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.ApriConf.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfDaGestire_pdfId" value="">
<input type="hidden" name="profiloUtente" value="<%=model.getProfiloUtente()%>">
</form>

<form name="cancellaForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.business.CancellaPdf.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfDaGestire_pdfId" value="">
</form>

<table height="100%" width="100%">
  <tr>
    <td align="center">
		<table width="100%" cellpadding="0" cellspacing="0">
		  <tr>
		    <td>
				<form name="searchForm" method="post" action="call.wfem" style="margin:0;">
				<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.publisher.display.PdfPublisher.execute">
				<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
				<input type="hidden" name="eseguiRicerca" value="">
				<fieldset class="fieldsGroup">
				<legend class="text" style="font-weight: bold;">
					<table cellpadding="0" cellspacing="0" class="text" style="font-weight: bold;">
						<tr>
							<td>
							    <img src="<%=template.getWebApp()%>/publisher/images/section.gif">
							</td>
							<td>
								&nbsp;Pubblicazione Moduli Compilabili&nbsp;
							</td>
							<td>
								<% if(model.isProfiloUtenteAmministratore()){ %>
									<%=template.field("area","onchange='onChangeArea(this);' labelstyle='font-weight: bold;' emptylabel='Tutte le aree'")%>
								<% }else{ %>
									area <%=areaDescr%>
								<% } %>
							</td>
							<td>
								&nbsp;<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/publisher/images/clear.gif" 
							    	 onclick="pulisci();" style="cursor: pointer;">
							</td>
					     </tr>
					</table>
				</legend>
				<table width="100%"> 
				  <tr>
				    <td align="center">
				      <table>
					      <tr>
					      	<td>
					      		<table><tr>
							      	 <% if(model.isProfiloUtenteAmministratore()){ %>
							      	 	<input type="hidden" name="area" value="<%=model.getArea()%>">
							      	 <% } %>
								     <td><%=template.field("ricercaPdfParam_pdfCode","size='30' labelwidth='100'")%></td>  
								     <td><%=template.field("ricercaPdfParam_pdfMomCode","size='5'")%></td>
								     <td><%=template.field("ricercaPdfParam_pdfDescr","size='40'")%></td>
								     <td><%=template.field("soloAttivi")%></td>    
							     </tr></table>
					      	</td>
					      </tr>
						  <% if(model.isProfiloUtenteAmministratore()){ %>
					      <tr>
					      	<td>
					      		<table><tr>
								     <td><%=template.field("ricercaPdfParam_callSrvDispositivaBMED","labelwidth='100'")%></td> 
								     <% if(model.isProfiloUtenteSviluppo()){ %>
								     	<td><%=template.field("ricercaPdfParam_pdfDriverName","labelwidth='100' uppercase='false' type='all'")%></td>  
								     <% } %>								      	 				      	 
							    </tr></table>
					      	</td>
					      </tr>
						  <% } %>
				      </table>
				     </td>
				  </tr>
				  <tr>
				    <td align="center">
				      <table><tr>
					     <td><%=template.action("ricercaAction","style='width:80px;'")%></td>
					     <% if(!model.getArea().isNull() && !model.getArea().equals(PdfCatalogModel.AREA_CATALOGO_MODULI)){ %>  
						     <td style="width:10px;"></td>
						   	 <td><%=template.action("nuovoAction","style='width:80px;'")%></td>
					   	 <% } %>  
				      </tr></table>
				    </td>
				  </tr>
				  <tr>
				     <td colspan="2" style="height: 5"></td>
				  </tr>   
				</table>
				</fieldset>
				</form>
		    </td>
		  </tr>
		</table>
    </td>
  </tr>
  
  <tr>
    <td id="searchToolbarCont">
	    <% if(model.getPdfList().size() > 0){ %>
		      <table><tr>
		         <td><%=template.action("apriAction","enabled='false' style='width:80;'")%></td>
		         <td><%=template.action("cancellaAction","enabled='false' style='width:80;'")%></td>
		      </tr></table>
	     <% } %>
    </td>
  </tr>
  
  <tr>
    <td height="100%" id="searchResultCont">
		<% if(!model.isPrimaAttivazione() && model.getEseguiRicerca().booleanValue()){ %>
			<%
			  String cols = "cols='#pdfId,pdfCode,pdfDescr,pdfStartDate,pdfEndDate,#isFromCatalogoModuli,pdfPubIdCorrente,pdfNumPubblArch,#pdfNumPubblicazioni,#pdfNumArchiviazioni,#isOnWork' ";
			  String dim = "colswidths='25%,*,10%,10%,8%,13%' ";
			  if(model.getArea().isNull()){
				  cols =  "cols='#pdfId,pdfCode,pdfDescr,pdfArea,pdfStartDate,pdfEndDate,#isFromCatalogoModuli,pdfPubIdCorrente,pdfNumPubblArch,#pdfNumPubblicazioni,#pdfNumArchiviazioni,#isOnWork' ";
				  dim = "colswidths='20%,*,15%,10%,10%,8%,13%' ";
			  }
			  if(model.isProfiloUtenteAmministratore()){
				  cols = "cols='#pdfId,pdfCode,pdfDescr,pdfStartDate,pdfEndDate,#isFromCatalogoModuli,pdfPubIdCorrente,pdfNumPubblArch,pdfMomCode,pdfCodProdottoPrit,pdfCodOperazionePrit,callSrvDispositivaBMED,#pdfNumPubblicazioni,#pdfNumArchiviazioni,#isOnWork' ";
				  dim = "colswidths='15%,*,10%,10%,8%,13%,5%,5%,5%,5%' ";
				  if(model.getArea().isNull()){
					  cols =  "cols='#pdfId,pdfCode,pdfDescr,pdfArea,pdfStartDate,pdfEndDate,#isFromCatalogoModuli,pdfPubIdCorrente,pdfNumPubblArch,pdfMomCode,pdfCodProdottoPrit,pdfCodOperazionePrit,callSrvDispositivaBMED,#pdfNumPubblicazioni,#pdfNumArchiviazioni,#isOnWork' ";
					  dim = "colswidths='10%,*,8%,10%,10%,8%,13%,5%,5%,5%,5%' ";
				  }
			  }
			%>
	        <%=template.grid("pdfList",cols+dim+"height='100%' width='100%' "+
													(model.isProfiloUtenteAmministratore()?"gridwidth='1200' ":"")+
													(model.isProfiloUtenteAmministratore()?"excel='true' ":"")+
	        									   "title='Elenco moduli' "+
	        									   "onclick='selezionaRiga(this);' "+
	        									   "cellheight='30' "+
	        									   "cellsnowrap='false' "+
	        									   "selection='single' "+
	        									   "headernowrap='false' "+
	        									   "onnewcell='newCell(this);' "+
	        									   "decorator='prgm.pdfwebforms.publisher.display.PdfPublisher' "+
	        									   "ondblclick='selezionaRiga(this);startRequest();doApriAction();'")%>
		<% } %>
    </td>
  </tr>
  
  <tr>
    <td>
		<%=template.getMessagesAndErrors()%>
    </td>
  </tr>
</table>

<%=template.getFooter()%>
<%
	model.setPrimaAttivazione(false);
%>
<script>
document.searchForm.ricercaPdfParam_pdfCode.focus();
</script>
</body>
</html>
