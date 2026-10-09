<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="prgm.pdfwebforms.model.PdfInstanceListModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfCompletedList");
	template.setLabelPosition(template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PdfInstanceListModel model = (PdfInstanceListModel)template.getPageDataModel();	 
	boolean isSede = model.getParams().getIsSede().booleanValue(); 
%>

<html>

<head>
<%=template.getHeader()%> 

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/jquery-placeholder-plugin/jquery.placeholder.css'/>
<script src="<%=template.getWebApp()%>/jquery-placeholder-plugin/jquery.placeholder.js"></script>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfCompletedList.js"></script>
<script>
var jsMODALITA_SOTTOSCRIZIONE_CARTA_LIBERA = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA%>";
var jsMODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA%>";
var jsMODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE%>";
var jsMODALITA_SOTTOSCRIZIONE_COPERNICO = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO%>";
var jsMODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE = "<%=PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE%>";

$( document ).ready(function() {
	try{
		$(".normalButton").bind({
			click: function(event){
				$(this).addClass("normalButtonSel");
			}
		}).mouseout(
			function(){ $(this).removeClass("normalButtonSel"); }
		).mouseenter(
			function(){ $(this).addClass("normalButtonSel"); }
		);
	}catch(e){}
	try{
		$(":input[placeholder]").placeholder();
	}catch(e){}
});
</script>
</head>

<body>

<form name="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="readRequest" value="false">
<input type="hidden" name="pdfInstanceId" value="">
</form>

<table height="100%" width="100%">

  <tr>
    <td align="center">
		<table width="100%" cellpadding="0" cellspacing="0">
		  <tr>
		    <td>
				<form name="ricerca" method="post" action="cmd.wfem" style="margin:0;">
				<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.display.PdfCompletedList.execute">
				<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
				<fieldset class="fieldsGroup">
				<legend class="text" style="font-weight: bold;">
				    <img src="<%=template.getWebApp()%>/images/section.gif">
					&nbsp;Ricerca moduli completati&nbsp;
					<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" 
					     onclick="pulisci();" style="cursor: pointer;">
				</legend>
				<table width="100%"> 
				 <tr>
				 	<td align="center">
				 		<table>
				 			<% if(isSede){ %>
					 			<tr>
								    <td><%=template.field("params_codAgente","labelwidth='120'")%></td>
								    <td><%=template.field("params_nominativoAgente","labelwidth='60'")%></td>
					 			</tr>
				 			<% } %>
				 			<tr>
							    <td><%=template.field("params_cli1_nome","labelwidth='120'")%></td>
							    <td><%=template.field("params_cli1_cognome","labelwidth='60'")%></td>
				 			</tr>
				 			<tr>
							    <td colspan="2"><%=template.field("params_pdfAnag_pdfDescr","labelwidth='120' size='57'")%></td>
				 			</tr>
				 			<tr>
							    <td><%=template.field("params_dataInizio","labelwidth='120'")%></td>
							    <td><%=template.field("params_dataFine")%></td>
				 			</tr>
				 		</table>
				 	</td>
				 </tr>
				 <tr>
	  				<td align="center" height="60"><table><tr><td><div class="normalButton" onclick="doRicercaAction();">Cerca</div></td></tr></table></td>
				 </tr>
				</table>
				</fieldset>
				</form>
		    </td>
		  </tr>
		</table>
    </td>
  </tr>

  <tr><td height="5"></td></tr>

  <tr>
    <td align="center" height="100%" id="resultCont">
    <% if(!model.isNewProcess()){ %>
			<%
			  	String cols = "cols='#pdfStatus,#pdfAnag_pdfCode,#pdfCompilationMode,pdfInstanceId,pdfAnag_pdfDescr,clienti,pdfCompilationModeDescr,pdfCompletionTime,pdf' ";
			  	String dim = "colswidths='15%,*,*,15%,14%,5%' ";
				if(isSede){
				  	cols = "cols='#pdfStatus,#pdfAnag_pdfCode,#pdfCompilationMode,pdfInstanceId,pdfAnag_pdfDescr,agente,clienti,pdfCompilationModeDescr,pdfCompletionTime,pdf' ";
				  	dim = "colswidths='15%,*,*,*,15%,14%,5%' ";
			  	}
			%>
	        <%=template.grid("pdfList",cols+dim+"height='100%' width='100%' "+
	        									   "selection='none' "+
	        									   "onnewcell='newCell(this);' "+
	        									   "showbottomarea='true' "+
	        									   "showtoparea='false' "+
	        									   "showcounter='false' "+
	        									   "cellheight='60' "+
	        									   "cellsnowrap='false' "+
	     	  									   "decorator='prgm.pdfwebforms.display.PdfCompletedList'")%>
	<% } %>
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
<% model.setNewProcess(false); %>
