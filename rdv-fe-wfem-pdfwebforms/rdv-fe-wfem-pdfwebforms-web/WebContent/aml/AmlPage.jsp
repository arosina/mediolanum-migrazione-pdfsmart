<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.model.*"%>
<%@page import="prgm.pdfwebforms.aml.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	PdfPersonModel sottoscrittore = model.mainPdfData().getPerson(1);
	AmlModel amlModel = model.getAmlModel();
	NaturaModel natura = amlModel.getNatura();
	OrigineModel origine = amlModel.getOrigine();
	RelazioniModel relazioni = amlModel.getRelazioni();
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/wfemStyle.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/aml/Aml.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>

<script src="<%=template.getWebApp()%>/aml/AmlPage.js"></script>
<script src="<%=template.getWebApp()%>/aml/Origine.js"></script>
<script src="<%=template.getWebApp()%>/aml/Natura.js"></script>
<script src="<%=template.getWebApp()%>/aml/Relazioni.js"></script>

<style>
body{
	margin: 0;
	padding: 0;
	border: 0;
	height: 100%;
	overflow: hidden;
}
.text{
	font-family: Segoe UI;
	font-style: normal; 
	font-weight: normal; 
	text-decoration: none;		
	font-size: 12px;
	color: #666666;
}
</style>
</head>

<body>

<form name="goBackForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GotoLastPdf.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="amlModel_tabSelezionato" value="<%=amlModel.getTabSelezionato()%>">
</form>

<center>
<table class="htmlContainer">
	<tr>
		<td valign="top" class="titolo" style="padding:10;">
			Questionario AML
		</td>
	</tr>
	<tr>
		<td height="100%" style="padding:5;" id="tabContent">
			<form name="dati" method="post" action="call.wfem" style="height:100%;">
			<input type="hidden" name="wfemCmd" value="">
			<input type="hidden" name="modelHashCode" value="<%=model.hashCode()%>">
			<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
			<input type="hidden" name="amlModel_tabSelezionato" value="<%=amlModel.getTabSelezionato()%>">
			<input type="hidden" name="amlModel_natura_dispositivaSelezionata" value="<%=natura.getDispositivaSelezionata()%>">
			<input type="hidden" name="amlModel_origine_idxImportoSelezionato" value="">
			<input type="hidden" name="amlModel_origine_sezioneImporti" value="">
			<input type="hidden" name="amlModel_relazioni_idxSoggettoSelezionato" value="">
			<input type="hidden" name="amlModel_relazioni_globalSoggAccordionStatus" value="<%=relazioni.getGlobalSoggAccordionStatus()%>">		
			<input type="hidden" name="amlModel_natura_elencoSinistraContScrollPos" value="<%=natura.getElencoSinistraContScrollPos()%>">
			<input type="hidden" name="amlModel_origine_elencoSinistraContScrollPos" value="<%=origine.getElencoSinistraContScrollPos()%>">
			<input type="hidden" name="amlModel_origine_elencoDestraContScrollPos" value="<%=origine.getElencoDestraContScrollPos()%>">
			<input type="hidden" name="amlModel_relazioni_elencoSinistraContScrollPos" value="<%=relazioni.getElencoSinistraContScrollPos()%>">
			<input type="hidden" name="amlModel_relazioni_elencoDestraContScrollPos" value="<%=relazioni.getElencoDestraContScrollPos()%>">
			<table width="100%" height="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td id="tabLabels">
						<table width="100%" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
							<tr>
							<% if(amlModel.hasNatura()){ %>
								<td onclick="selezionaTab('natura',<%=amlModel.getTabSelezionato().equals("natura")?"true":""%>);">
									<table class="tab<%=amlModel.getTabSelezionato().equals("natura")?" tabSel":""%>"><tr>
										<td style="width:20px;"></td>
										<td style="width:100%;" align="center">Natura e scopo del rapporto</td>
										<td style="width:3px;"></td>
										<td style="width:20;padding-right:10;"><%=natura.drawIcons()%></td>
									</tr></table>
								</td>
							<% } %>
							<% if(amlModel.hasOrigine()){ %>
								<td onclick="selezionaTab('origine',<%=amlModel.getTabSelezionato().equals("origine")?"true":""%>);">
									<table class="tab <%=amlModel.getTabSelezionato().equals("origine")?" tabSel":""%>"><tr>
										<td style="width:20px;"></td>
										<td style="width:100%;" align="center">Origine della provvista</td>
										<td style="width:3px;"></td>
										<td style="width:20;padding-right:10;"><%=origine.drawIcons()%></td>
									</tr></table>
								</td>
							<% } %>
							<% if(amlModel.hasRelazioni()){ %>
								<td onclick="selezionaTab('relazioni',<%=amlModel.getTabSelezionato().equals("relazioni")?"true":""%>);">
									<table class="tab<%=amlModel.getTabSelezionato().equals("relazioni")?" tabSel":""%>"><tr>
										<td style="width:20px;"></td>
										<td style="width:100%;" align="center">Relazioni</td>
										<td style="width:3px;"></td>
										<td style="width:20;padding-right:10;"><%=relazioni.drawIcons()%></td>
									</tr></table>
								</td>
							<% } %>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%" valign="top" style="border: 1px solid #666666;">
						<% if(amlModel.getTabSelezionato().equals("natura")){ %>
							<%@ include file="./Natura.html"%>
						<% }else if(amlModel.getTabSelezionato().equals("origine")){ %>
							<%@ include file="./Origine.html"%>
						<% }else if(amlModel.getTabSelezionato().equals("relazioni")){ %>
							<%@ include file="./Relazioni.html"%>
						<% } %>
					</td>
				</tr>
			</table>
			</form>
		</td>
	</tr>
	<tr><td style="padding-top: 10;"><div class="line">&nbsp;</div></td></tr>
	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td style="padding:10;padding-top:3;">
						<table>
							<tr>
								<td><div class="normalButton whiteButton" onclick="doBack();">Indietro</div></td>
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
						<table>
							<tr>
								<td><div class="normalButton" onclick="doProsegui();">Avanti</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</center>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>
