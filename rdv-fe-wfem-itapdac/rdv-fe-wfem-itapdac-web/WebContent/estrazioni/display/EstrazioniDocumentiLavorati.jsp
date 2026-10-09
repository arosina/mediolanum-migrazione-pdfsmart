
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.ita.p.dac.estrazioni.model.EstrazioniModel"%>
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%
	template.setWlt(true);

	template.setApplCode("ITAPDAC");		// Nome del file .properties
	template.setPageName("EstrazioniDocumentiLavorati"); 	// Nome della JSP
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setLabelWidth("70");
	template.setJSCombo(false);
	
	EstrazioniModel model=(EstrazioniModel)template.getPageDataModel();
%>
<html>
<head>
<script src="<%=template.getWebApp()%>/estrazioni/display/EstrazioniDocumentiLavorati.js"></script>
<%=template.getHeader()%>	
</head>
<body>
<iframe id='ExportPort' src='call.wfem?wfemCmd=getBlankPage' scrolling='no' frameborder='0' style='position:absolute; top:0px; left:0px; display:none;'></iframe>

<form id="dati" name="dati" method="post" action="cmd.wfem" >
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	<input type="hidden" name="wfemCmd" value="">
	<input type="hidden" id="utenteCollegato" name="utenteCollegato"  value="">

<table width="100%" >
	<tr>
		<td >
		  <fieldset >
		  <legend class="text"><%=template.getProperty(template.getPageName()+"parametri") %></legend>
			<table width="100%">
				<tr>
					<td>
						<%=template.field("dataInizio")%>
					</td>
					<td>
						<%=template.field("dataFine")%>
					</td>
					
				</tr>	
				<tr>
					<td>
						<%=template.field("codOperazione")%>
					</td>
					<td>
						<%=template.field("codProdotto","onChange='aggiornaOperazioni()'")%>
					</td>
				</tr>
				<tr>
					<td colspan="2"><%=template.action("esegui") %></td>
				</tr>	  
		  	</table>
		  </fieldset>
		</td>
	</tr>
</table>
</form>
<%=template.getFooter()%>
</body>
</html>