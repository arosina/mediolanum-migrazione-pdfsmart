<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.sede.model.SbloccaDacModel"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDAC");
	template.setPageName("SbloccaDac");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);

	SbloccaDacModel model = (SbloccaDacModel)template.getPageDataModel();	
%>
<html>

<head>
	<%=template.getHeader()%>

	<script>
		var barcodeSelezionato = "";
		var idDacSelezionata = "";
		var descrUffDestinatario = "";
	</script>
	<script src="<%=template.getWebApp()%>/sede/display/SbloccaDac.js"></script>
</head>

<body style="margin-top:10px;" onload="focusOnBarcode();">

<form name="frmRiceviDocForzato" method="post" action="call.wfem" style="margin:0;">
	<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.business.RiceviDocForzato.execute">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	
	<input type="hidden" name="idDacSelezionata" value="">
</form>

<table width="100%" height="98%">

<!--	#########################		BARRA SUPERIORE		#########################	-->
<tr>
	<td valign="top" height="10%">
		<table width="100%" style="background-color:#e1e1e1;">
		<tr>
    		<td width="100%">&nbsp;</td>
		</tr>
		</table>
	</td>
</tr>
<!--	#########################		END BARRA SUPERIORE		#########################	-->

<!--	#########################			BARCODE DOCUMENTO		#########################	-->
<tr>
	<td valign="top" align="center" height="10%">
		<form name="frmDocSparato" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.business.LoadDacAttive.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		
		<table width="96%">
		<tr>
			<td width="1%"><%=template.field("barcode", "maxlength='50'")%></td>
	  		<td width="1%"><%=template.action("vediDac", "style='width:120px;' onclick='doVediDac();'")%></td>
			<td width="98%">&nbsp;</td>
		</tr>
		</table>
		
    	</form>
	</td>
</tr>
<!--	#########################			END BARCODE DOCUMENTO	#########################	-->

<!--	#########################			DAC		#########################	-->
<tr>
	<td width="100%" height="80%" align="center" valign="top">
		<table width="95%" height="100%">
		<tr><td colspan=2>&nbsp;</td></tr>
		<% if (!model.isPrimaVolta()) { %>
		
			<% if (model.getElencoDac().size() > 0) { %>
				<tr>
					<td height="30px"><%=template.action("ricevi", "style='width:120px;' onclick='doRisolvi();' enabled='false'")%></td>
					<td width="100%" class="text" style="font-weight: bold; color: green">&#9668; <%=template.getProperty(template.getPageName() + "helpRicezione")%></td>
				</tr>
				<tr><td colspan=2>&nbsp;</td></tr>
			<% } %>
			<tr>
				<td width="100%" height="100%" align="center" valign="middle" colspan=2>
					<%
						String cols="idDac,descrStato,descrUffMittente,descrUffDestinatario,dataOraEmissione,codUtenteLavorazione,dataOraLavorazione";
						String colswidths="17%,14%,14%,14%,14%,14%,*";
					%>
					<%=template.grid("elencoDac", "cols='"+cols+"'"+
						" colswidths='"+colswidths+"'"+
						" width='98%' height='100%'"+
						" title='Elenco delle DAC che contengono attualmente il documento'"+
						" noRowsMsg='"+template.getProperty(template.getPageName() + "textDocNonTrovato")+"'"+
						" onclick='selezionaDac(this);'")%>
				</td>
			</tr>
			<tr>
				<td colspan=2 height="30px"><%=template.getMessagesAndErrors()%></td>
			</tr>
		<% } %>
		</table>
	</td>
</tr>
</table>
<!--	#########################			END DAC		#########################	-->

<%=template.getFooter()%>
</body>
</html>

<% model.setPrimaVolta(false); %>