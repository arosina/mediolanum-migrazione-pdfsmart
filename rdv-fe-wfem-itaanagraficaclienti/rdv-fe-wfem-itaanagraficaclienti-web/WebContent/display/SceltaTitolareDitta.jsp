<%@ page import="prgm.ita.anagraficaclienti.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AnagraficaCliente");
	template.setLabelPosition(template.DOWN_LABEL);
	template.setJSCombo(false);
	
	String tipoInclusioneCogestiti = "";
	String ruoloCogestione = "";
	String tipoOrdinamentoCogestiti = "";
	ClienteModel model = (ClienteModel)template.getPageDataModel();
	if(model.getCogestioneData().getIsUtenteCogestore().booleanValue()) {
		tipoInclusioneCogestiti ="T";
		ruoloCogestione ="BC";
		tipoOrdinamentoCogestiti = "C";
	}else if(model.getCogestioneData().getIsUtenteTitolare().booleanValue()) {
		tipoInclusioneCogestiti ="T";
		ruoloCogestione ="BC";
		tipoOrdinamentoCogestiti = "P";
	}
	
%>

<html>
<head>
<%=template.getHeader()%>

<script src="/ItaAnagraficaClienti/base/header.js"></script>
<script src="/ItaAnagraficaClienti/display/SceltaTitolareDitta.js"></script>
<script>
var jsTipoInclusioneCogestiti = "<%=tipoInclusioneCogestiti%>";
var jsRuoloCogestione = "<%=ruoloCogestione%>";
var jsTipoOrdinamentoCogestiti = "<%=tipoOrdinamentoCogestiti%>";
</script>
</head>

<body topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0" 
	  bgcolor="#fbfbfb" scroll="no" style="background-repeat: no-repeat;">

<form name="goback" method="post" action="call.wfem" style="display: none;">
	<input type="hidden" name="wfemCmd" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="dati" method="post" action="call.wfem" style="margin: 5px;">  
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="resetErrors" value="false">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<%=template.hidden("agente_codAgente")%>
<%=template.hidden("codPotenzialeTitolare")%>
<%=template.hidden("codMediolanumTitolare")%>
<%=template.hidden("codFiscaleTitolare")%>

<center>
<table style="width:100%;">
 <tr><td style="height:130px;"></td></tr>
 <tr>
  <td valign="middle" align="center">
	<table id="tabScelta" border="1" style="background-color: white;">
	  <tr><td>
		<table class="text" style="font-size: 14pt;">	
			 <tr>
 			 	<td style="background-color:#1A458F; color: white;">Selezione del Titolare ditta / Libero professionista</td> 
			 </tr>
			 <tr><td style="height:15px;">&nbsp;</td></tr>
			 <tr>
			   <td align="center">
			     <table class="text" style="font-size: 12pt;">
 			     	<tr><td><input name="scelta" id="scelta" type="radio" value="1">Titolare ditta / Libero professionista gi&agrave; cliente</td></tr>
			     	<tr><td><input name="scelta" id="scelta" type="radio" value="2">Titolare ditta / Libero professionista non cliente</td></tr> 
			     </table>
			   </td>
			 </tr>
			 <tr><td style="height:15px;">&nbsp;</td></tr>
			 <tr>
			   <td align="center">
			     <table><tr>
				 	<% if(!model.getCallingAppl().isNull()){ %>
				 		<td><%=template.action("selezioneTitolareDittaBackAction","style='font-size:10pt;font-weight:bold;'")%></td>
				 	<% } %>
				 	<td><%=template.action("selezioneTitolareDittaProseguiAction","style='font-size:10pt;font-weight:bold;'")%></td>
			     </tr></table>
			   </td>
			 </tr>
		</table>
	  </td></tr>
	</table>
  </td>
 </tr>
</table>
</center>

</form>
<%=template.getFooter()%>

<%  // Documento del titolare scaduto
	if(model.getClienteTitolare() != null && model.getCodFiscaleTitolare().hasTypeErrors()){ 
		model.getCodFiscaleTitolare().resetTypeErrors();
		String errorMsg = "Attenzione: il documento del cliente selezionato:\\n"+
					     model.getClienteTitolare().getCognome()+" "+model.getClienteTitolare().getNome()+"\\n"+
					    "risulta scaduto.\\n"+
					    "E' necessario effettuare la variazione anagrafica prima di censire la ditta/lib. prof.";
%>
	<script>
	alert("<%=errorMsg%>");
	</script>
<% } %>

<iframe id="closeThreadIF" name="closeThreadIF" src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" style="display:none;"></iframe>

</body>
</html>
