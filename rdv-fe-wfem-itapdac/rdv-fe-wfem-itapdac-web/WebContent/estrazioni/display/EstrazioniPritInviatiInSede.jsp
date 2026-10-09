<%@ page import="com.atosorigin.wfem.types.*"%>
<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel"%>
<!-- Multi browser -->
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setJSCombo(false);	
    template.setApplCode("ITAPDAC");
    template.setPageName("EstrazioniPritInviatiInSede");
	template.setLabelPosition(Template.LEFT_LABEL);
	
	PritInviatiInSedeModel model = (PritInviatiInSedeModel)template.getPageDataModel();
	//template.setDoubleScale(2);
	
	DateType dataAl = new DateType();
	
	if(!model.getDataDal().isNull()) {
		dataAl = new DateType(model.getDataDal().dateValue());
		dataAl.addDays(6);
	}
	
%>
<html>
<head>
<%=template.getHeader()%>

<script src="<%=template.getWebApp()%>/estrazioni/display/EstrazioniPritInviatiInSede.js"></script>
</head>

<body>
<!-- execute command -->
<form name="dati" id="dati" method="post" action="call.wfem" style="margin:0;">
<input type="hidden" name="wfemCmd">
<%=template.hidden("tipoEstrazione")%>
<%=template.hidden("dataAl")%>
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<fieldset name="richiestaDati" id="richiestaDati">
<legend class="text" style="font-weight: bold;">
<img src="<%=template.getWebApp()%>/images/section.gif">&nbsp;<%=template.getProperty(template.getPageName()+"datiInput")%>
<img title="Pulisci campi di ricerca" src="<%=template.getWebApp()%>/images/clear.gif" onclick="pulisciCampiRicerca();" style="cursor: pointer;">
</legend>

<table align = "center" cellpadding="0" cellspacing="0">
	<tr>
		<td><%=template.field("dataDal", "labelalign='right' labelwidth='150px' fieldalign='left'")%></td>
		<td>&nbsp;</td>
		<td class="text">
			<input name="tipoEstrazione2" type="radio" value="sintesi"  onclick="abilita();document.dati.tipoEstrazione.value='sintesi';" <%if(model.getTipoEstrazione().equals("sintesi")){%>checked<%}%>>Sintesi&nbsp;&nbsp;
			<input name="tipoEstrazione2" type="radio" value ="dettaglio"  onclick="abilita();document.dati.tipoEstrazione.value='dettaglio';" <%if(model.getTipoEstrazione().equals("dettaglio")){%>checked<%}%>>Dettaglio&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td><%=template.field("statoPrit","maxleght='12' labelalign='right' labelwidth='150px' fieldalign='left' style=width:195px")%></td>
		<td>&nbsp;</td>
		<td><%=template.field("tipologiaPrit","labelalign='right' labelwidth='150px' fieldalign='left' style=width:120px")%></td>
	</tr>		
</table>
</fieldset>
<table width="100%">
	<tr>
		<td width="33%">&nbsp</td>
		<td width="33%"><%=template.action("esegui","style='width:120px;'")%></td>
		<td width="33%" align="right" style="cursor: pointer;"><a href="#" onclick="esportaXls();" class="text" ><b>Estrai Griglia</b></td>
	</tr>
</table>
</form>
<iframe id='ExportPort' frameborder='0' style='position:relative; top:0px; left:0px; width:100%; height:100%; display: none;'></iframe>

<div id="pdf" style='position:relative; top:0px; left:0px; width:100%; height:100%'></div>

<%=template.getMessagesAndErrors() %>
<%=template.getFooter()%>
</body>
</html>
  