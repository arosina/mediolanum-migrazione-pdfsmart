<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.sede.facade.GestoreEditabilita"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDAC");
	template.setPageName("RicezioneDacSede");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	DacModel model = (DacModel)template.getPageDataModel();	
	
	boolean dacReadonly = template.getModality() == Template.READ_MODALITY ? true : false;
	int docLavoratiDopoCaricamento = 0;

	AgenteModel agenteRiferimento = model.getAgenteRiferimento();
	DocumentoModel documento = model.getDocumento();
%>

<%@ include file="../../documento/DefinizioneVariabili.html"%>

<%
	idDacCorrente = model.getIdDac().toString();
%>

<html>

<head>
	<%=template.getHeader()%>
	
	<script src="<%=template.getWebApp()%>/sede/ricezionedac/RicezioneDacSede.js"></script>
	<script src="<%=template.getWebApp()%>/resources/javascript/plichi.js"></script>
	<script src="<%=template.getWebApp()%>/resources/javascript/ErroriDocumenti.js"></script>

	<!-- x documento -->
	<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Labels.css" type="text/css">
	<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tab.css" type="text/css">
	<script src="<%=template.getWebApp()%>/resources/javascript/stampa.js"></script>
	<script src="<%=template.getWebApp()%>/documento/Documento.js"></script>
	<script src="<%=template.getWebApp()%>/popup/popup.js"></script>
</head>

<body style="margin:0" onload="focusOnBarcode();">

<bgsound name="BGSOUND_ID" id="BGSOUND_ID" LOOP="1" SRC="">

<form name="gobackForm" method="post" action="call.wfem">
	<input type="hidden" name="wfemCmd" value="executeLastDisplay">
	<input type="hidden" name="readRequest" value="true">
	<input type="hidden" name="doSearch" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="datiRead" style="display:none">
  <input type="hidden" name="idDac" value="<%=model.getIdDac()%>">
  <input type="hidden" name="ufficio" value="<%=model.getUfficio()%>">
  <input type="hidden" name="fnc" value="<%=model.getFnc()%>"></input>
  <input type="hidden" name="isAutoSpuntata" value="<%=model.getIsAutoSpuntata()%>"></input>
  <input type="hidden" name="imgPath" value="<%=template.getWebApp()%>/images/">
  <input type="hidden" name="refreshable" value="<%=model.isRefreshable()%>">
  <input type="hidden" name="constUbicazioneInViaggio" value="<%=Costanti.UBICAZIONE_IN_VIAGGIO%>">
  <input type="hidden" name="constIdDacXDocFuoriDac" value="<%=Costanti.ID_DAC_X_DOC_FUORI_DAC%>">
  <input type="hidden" name="mgmPlichi" value="<%=model.isMgmPlichi()%>">
  <input type="hidden" name="tipoDac" value="<%=model.getTipoDac()%>"></input>
  <input type="hidden" name="readonly" value="<%=dacReadonly%>">

<!-- x include documento -->
  <input type="hidden" name="codReteAgenteRiferimento" value="<%=agenteRiferimento.getCodRete()%>">
  <input type="hidden" name="codAgenteRiferimento" value="<%=agenteRiferimento.getCodAgente()%>">
  <input type="hidden" name="nominativoAgenteRiferimento" value="<%=agenteRiferimento.getNominativo()%>">
</form>

<% if(!dacReadonly){ %>
	<form name="aggiornaDocInRicezione" method="post" action="call.wfem" style="display:none;">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.ricezionedac.AggiornaDocInRicezione.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="readRequest" value="false">	
		<input type="hidden" name="checkCodDescFields" value="false">
		<%=model.htmlParams("","")%>		
		
		<input type="hidden" name="idDocumento" value="">
		<input type="hidden" name="idDac" value="">
		<input type="hidden" name="nonPervenuto" value="">
		<input type="hidden" name="ubicazione" value="">
		<input type="hidden" name="isInBusta" value="">
	</form>

	<form name="frmDocDaAggiungere" method="post" action="call.wfem" style="display:none">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="checkCodDescFields" value="false">
				
		<input type="hidden" name="documento_barcode" value="">
	</form>
<% } %>

<table width="100%" height="98%">

<!--	#########################		BARRA SUPERIORE		#########################	-->
<tr>
	<td valign="top">
		<table width="100%" class="text" style="background-color:#e1e1e1;">
		<tr>
    		<td width="100%"><b>&nbsp;Logistica documenti Rete - DAC n. <%=model.getIdDac()%></b></td>
    	    <td align="right">
	    	   	<table>
	    		<tr>
	    	    <% if(model.isShowBack()){ %>
					<td><%=template.action("goback","style='width:120;'")%></td>
	    	    <% } %>

	    	    <% if(model.getStato().equals(Costanti.STATO_APERTA)){ %>
					<td><%=template.action("chiudiDac","style='width:80;'")%></td>
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
		<input type="hidden" name="checkCodDescFields" value="false">
	
		<table width="100%" cellpadding="5px" cellspacing="0">
		<tr>
			<td><%=template.field("noteAutore", "modality='read' size='70'")%></td>
			<td><%=template.field("noteOperatore", "onchange='doSalvaDac();' maxlength='50' size='70'")%></td>
		</tr>
		</table>
    	</form>
	</td>
</tr>
<!--	#########################			END TESTATA DAC			#########################	-->

<%@ include file="include/ErroriDocumenti.jsp"%>

<% if(!dacReadonly){ %>
<!--	#########################			BARCODE DOCUMENTO		#########################	-->
<tr>
	<td height="50px" align="center">
		<form name="frmDocSparato" style="margin:0">
		  	<table width="95%">
			<tr>
				<td width="1%"><%=template.field("barcode", "maxlength='50'")%></td>
		  		<td width="1%">
		  			<%=template.action("cercaDocumento", "style='width:80;' onclick='doCercaDocumento();'")%>
		  		</td>
				<td width="98%">&nbsp;</td>
			</tr>
		  	</table>
		</form>
	</td>
</tr>
<!--	#########################			END BARCODE DOCUMENTO	#########################	-->
<%} %>

<!--	#########################			ELENCO DOCUMENTI		#########################	-->
<%@ include file="include/RicezioneSenzaSmistamento.jsp"%>
<tr>
	<td align="center" valign="middle" id="docCont">
		<%@ include file="../../documento/Documento.html"%>
	</td>
</tr>
<!--	#########################		END ELENCO DOCUMENTI		#########################	-->
</table>

<div id="scriptCode" style='display: none'>
</div>

<div  style="display:none" id="finalsDacScriptsCont">
<table>
<tr>
	<td>
		<script></script>
	</td>
</tr>
</table>
</div>

<div  style="display:none" id="finalsDocScriptsCont">
<table>
<tr>
	<td>
	    <script>
    		selectDocJSTab("tabDati");
	    </script>
	</td>
</tr>
</table>
</div>

<%=template.getFooter()%>
</body>
</html>