<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDAC");
	template.setPageName("CreazioneDacSede");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);

	String enabCampoDoc=null;
	DacModel model = (DacModel)template.getPageDataModel();	
	boolean dacReadonly = template.getModality() == Template.READ_MODALITY ? true : false;

	// Var x include testata
	String enabOraSpedizione="";
	String onChangeUffDest="";
	String onChangeBox="";	
	String onChangeTipoSpedizione="";
	String onChangeOraSpedizione="";
	String onChangeNoteAutore="";

	AgenteModel agenteRiferimento = model.getAgenteRiferimento();
	DocumentoModel documento = model.getDocumento();
	boolean inErroreCtrlCassette = model.isCtrlCassette() && !model.getUffDestinatario().isNull() && model.getCassettaBox().isNull();
%>

<%@ include file="../../documento/DefinizioneVariabili.html"%>

<%
	idDacCorrente = model.getIdDac().toString();
%>
<html>

<head>
	<%=template.getHeader()%>
	
	<script src="<%=template.getWebApp()%>/resources/javascript/stampa.js"></script>
	<script src="<%=template.getWebApp()%>/resources/javascript/ErroriDocumenti.js"></script>
	<script src="<%=template.getWebApp()%>/sede/creazionedac/CreazioneDacSede.js"></script>
	
	<!-- File x include Gestione documenti -->
	<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Labels.css" type="text/css">
	<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/GridDacSede.css" type="text/css">
	<script src="<%=template.getWebApp()%>/resources/javascript/stampa.js"></script>
	<script src="<%=template.getWebApp()%>/sede/creazionedac/include/DocumentiInDac.js"></script>
	<script src="<%=template.getWebApp()%>/sede/creazionedac/include/DocumentiPlichiMancanti.js"></script>
	
	<!-- File x include Documento -->
	<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tab.css" type="text/css">
	<script src="<%=template.getWebApp()%>/documento/Documento.js"></script>
	<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tooltip.css" type="text/css">
	<script src="<%=template.getWebApp()%>/resources/javascript/Tooltip.js"></script>
	<script>
		var docInElenco = <%=model.getDocumenti().size()%>;
		var inErroreCtrlCassette = <%=inErroreCtrlCassette%>;
	</script>
</head>

<body style="margin:0;" onload="focusOnBarcode();">

<bgsound name="BGSOUND_ID" id="BGSOUND_ID" LOOP="1" SRC="">

<form name="gobackForm" method="post" action="call.wfem">
	<input type="hidden" name="wfemCmd" value="executeLastDisplay">
	<input type="hidden" name="readRequest" value="true">
	<input type="hidden" name="doSearch" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="datiRead" style="display:none;">
  <input type="hidden" name="idDac" value="<%=model.getIdDac()%>"></input>
  <input type="hidden" name="isSmistatore" value="<%=model.isSmistatore()%>"></input>
  <input type="hidden" name="numDocInElenco" value="<%=model.getDocumenti().size()%>"></input>
  
  <input type="hidden" name="constMezzoCorriere" value="<%=Costanti.MEZZO_SPEDIZIONE_CORRIERE%>"></input>
  <input type="hidden" name="codReteAgenteRiferimento" value="<%=model.getAgenteRiferimento().getCodRete()%>"></input>
  <input type="hidden" name="codAgenteRiferimento" value="<%=model.getAgenteRiferimento().getCodAgente()%>"></input>
  <input type="hidden" name="nominativoAgenteRiferimento" value="<%=model.getAgenteRiferimento().getNominativo()%>"></input>
  <input type="hidden" name="flagReplicaDac" value="<%=model.getFlagReplica()%>"></input>
  <input type="hidden" name="imgPath" value="<%=template.getWebApp()%>/images/"></input>
  <input type="hidden" name="refreshable" value="<%=model.isRefreshable()%>"></input>
  <input type="hidden" name="reso" value="<%=model.getReso()%>"></input>
</form>

<table width="100%" height="98%">

<!--	#########################		BARRA SUPERIORE		#########################	-->
<tr>
	<td valign="top">
		<table width="100%" class="text" style="background-color:#e1e1e1;">
		<tr>
    		<td width="100%" id="barraSuperiore"><b>&nbsp;Logistica documenti Rete<%if(!model.getIdDac().isNull()){%> - Nuova DAC n. <%=model.getIdDac()%><%}%></b></td>
    	    <td align="right">
	    	   	<table>
	    		<tr>
	    	    <% if(model.isShowBack()){ %>
					<td><%=template.action("goback","style='width:120;'")%></td>
	    	    <% } %>
		    	<% if(!dacReadonly) {
						if (!model.getIdDac().isNull()){
							enabCampoDoc = "false";
							if (model.getDocumenti().size()>0)
								enabCampoDoc = "true";
				%>
							<td><%=template.action("spedisciDac","style='width:80;' enabled='"+enabCampoDoc+"'")%></td>
				<% 		}
					}else{ %>
	    	    		<td><%=template.action("stampaDac")%></td>
				<% } %>
				
				<% if(model.isNuovaDacDopoInvia()){
					model.setNuovaDacDopoInvia(false);
				%>
    	          	<td><%=template.action("nuovaDacDopoInvia","style='width:80;'")%></td>
				<% } %>
				
	    	    </tr>
				</table>
			</td>
		</tr>
    	<tr>
    		<td align="center" colspan="2">
    			<table width="100%" cellpadding="0" cellspacing="0">
    			<tr>
    				<td width="25%" class="text" align="center"><b>Ufficio corrente: <%=model.getDescValue("ufficio")%></b></td>
    				<td width="75%"></td>
    			</tr>
    			</table>    			
    		</td>
		</tr>
		</table>
	</td>
</tr>
<!--	#########################		END BARRA SUPERIORE		#########################	-->

<!--	#########################			TESTATA DAC			#########################	-->
<tr>
	<td valign="top" align="center">
		<form name="frmTestataDac" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	
		<%if (model.isSmistatore()) {%>
			<%@ include file="include/TestataDacSmistatore.jsp"%>
		<%}else{ %>
			<%@ include file="include/TestataDac.jsp"%>
		<%} %>

    	</form>
	</td>
</tr>
<!--	#########################			END TESTATA DAC			#########################	-->

<%@ include file="include/ErroriDocumenti.jsp"%>
<%@ include file="include/DocumentiInDac.jsp"%>

<% if (dacReadonly || model.hasCommandErrors() || model.hasCommandMessages()) { %>
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td style="background-color:#e1e1e1;">&nbsp;</td>
		</tr>
		<tr>
			<td height="18px"><%=template.getMessagesAndErrors()%></td>
		</tr>
		</table>
	</td>	
</tr>
<% } %>
</table>

<div id="scriptCode" style='display: none;'>
</div>

<div  style="display:none;" id="finalsDacScriptsCont">
<table><tr><td>
	<script>
	</script>
</td></tr></table>
</div>

<div  style="display:none;" id="finalsDocScriptsCont">
<table><tr><td>
    <script>
    	selectDocJSTab("tabDati");
    </script>
</td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>