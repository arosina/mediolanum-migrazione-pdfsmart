<%@page import="prgm.pdfwebforms.model.PdfInstanceModel"%>
<%@page import="prgm.pdfwebforms.publisher.model.PdfAnagModel"%>
<%@page import="com.atosorigin.wfem.types.ListType"%>
<%@page import="prgm.pdfwebforms.catalog.PdfCatalogModel"%>
<%@page import="com.atosorigin.wfem.types.AbstractTypePropertyDescriptor"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_UPLOAD);

	template.setApplCode("PDFWEBFORMS");
	template.setPageName("PdfCatalog");
	template.setLabelPosition(template.NO_LABEL);  
	template.setJSCombo(false);
	
	PdfCatalogModel model = (PdfCatalogModel)template.getPageDataModel();
	boolean isSede = model.getPdfListParams().getIsSede().booleanValue(); 

	String cols = "";
	String dim = "";
%>

<html>

<head>
<%=template.getHeader()%>
<style>
.filterTitle{
	font-family: Segoe UI;
	font-size: 14px;
	font-weight: normal;
	color: #4b96d1;
	font-weight: bold;
	padding-left: 30px;
}
</style>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/jquery-placeholder-plugin/jquery.placeholder.css'/>
<script src="<%=template.getWebApp()%>/jquery-placeholder-plugin/jquery.placeholder.js"></script>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/catalog/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/catalog/style/Tab.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/catalog/style/wfemStyle.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/catalog/PdfCatalog.js"></script>
<script src="<%=template.getWebApp()%>/catalog/PdfList.js"></script>
<script src="<%=template.getWebApp()%>/catalog/PdfDraftList.js"></script>
<script src="<%=template.getWebApp()%>/catalog/PdfCompletedList.js"></script>
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

function showTab(tab,tabid){
	if(tabid == document.datiTab.selectedTab.value)
		return;
	
	__pageIntf.closeModalPopup();
	document.getElementById(document.datiTab.selectedTab.value).style.display='none';
	
	$(".tab").removeClass("tabSel");
	$(tab).addClass("tabSel");
	document.getElementById(tabid).style.display='';
	
	document.datiTab.selectedTab.value = tabid;
	wfemHiddenSubmit(document.datiTab,"none");
}
</script>
</head>

<body>

<form name="datiTab" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.catalog.SelectTab.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="selectedTab" value="<%=model.getSelectedTab()%>">
</form>

<form name="datiPdfList" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.NewPdf.executeOnNewThread">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="loadMappedPropertiesFields" value="false">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="pdfId" value="">
<input type="hidden" name="pdfInstanceId" value="">
<input type="hidden" name="compilationModes" value="">
<input type="hidden" name="pdfEnvironment" value="<%=PdfCatalogModel.AREA_CATALOGO_MODULI%>">
<input type="hidden" name="gobackLabel" value="Torna al catalogo moduli">
<input type="hidden" name="gobackUrl" value="call.wfem?wfemCmd=executeCurrentDisplay&readRequest=true&refreshDraft=true&selectedTab=pdfListCont&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="gobackEndLabel" value="Torna al catalogo moduli">
<input type="hidden" name="gobackEndUrl" value="call.wfem?wfemCmd=executeCurrentDisplay&readRequest=true&selectedTab=pdfListCont&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="draftApriForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.OpenPdf.executeOnNewThread">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfInstanceId" value="">
<input type="hidden" name="gobackLabel" value="Torna ai moduli in bozza">
<input type="hidden" name="gobackUrl" value="call.wfem?wfemCmd=executeCurrentDisplay&readRequest=true&refreshDraft=true&selectedTab=pdfDraftListCont&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="gobackEndLabel" value="Torna al catalogo moduli">
<input type="hidden" name="gobackEndUrl" value="call.wfem?wfemCmd=executeCurrentDisplay&readRequest=true&refreshDraft=true&selectedTab=pdfListCont&BrowserInstance=<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="draftCancellaForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.catalog.DeletePdf.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="idToDelete" value="">
</form>

<form name="datiPdfCompletedList" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.OpenCompletedPdf.executeOnPopup">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="readRequest" value="false">
<input type="hidden" name="pdfInstanceId" value="">
</form>

<table height="100%" width="100%">
	<tr>
		<td colspan="3" align="center">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table cellpadding="0" cellspacing="0"><tr>
							<td><div onclick="showTab(this,'pdfListCont');" class="tab <%=((model.getSelectedTab().equals("pdfListCont")?"tabSel":""))%>">Catalogo moduli</div></td>
							<td width="5"></td>
							<td><div onclick="showTab(this,'pdfDraftListCont');" class="tab <%=((model.getSelectedTab().equals("pdfDraftListCont")?"tabSel":""))%>">Moduli in bozza</div></td>
							<td width="5"></td>
							<td><div onclick="showTab(this,'pdfCompletedListCont');" class="tab <%=((model.getSelectedTab().equals("pdfCompletedListCont")?"tabSel":""))%>">Moduli completati</div></td>
						</tr></table>
					</td>
				</tr>
				<tr><td style="padding-top:8;"><div class="tabline">&nbsp;</div></td></tr>
			</table>
		</td>
	</tr>
	<tr>
		<td id="pdfListCont" height="100%" <%=(model.getSelectedTab().equals("pdfListCont")?"":"style='display:none;'")%>>
			<%@ include file="./PdfList.html"%>
		</td>
		<td id="pdfDraftListCont" height="100%"  <%=(model.getSelectedTab().equals("pdfDraftListCont")?"":"style='display:none;'")%>>
			<%@ include file="./PdfDraftList.html"%>
		</td>
		<td id="pdfCompletedListCont" height="100%"  <%=(model.getSelectedTab().equals("pdfCompletedListCont")?"":"style='display:none;'")%>>
			<%@ include file="./PdfCompletedList.html"%>
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
<%
	model.setPrimaVolta(false);
%>
