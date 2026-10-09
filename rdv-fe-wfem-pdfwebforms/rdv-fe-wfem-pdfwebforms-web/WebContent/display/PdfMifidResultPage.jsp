<%@page import="java.util.ArrayList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlFieldsDrawer"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.mifid.MifidCaller"%>
<%@page import="prgm.pdfwebforms.mifid.ControlliConcentrazioneFia"%>
<%@page import="prgm.pdfwebforms.sostituzioni.SostituzioniCaller"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	
	PdfModel model = (PdfModel)template.getPageDataModel();
	String[] erroriAdeguatezza = model.getErroriAdeguatezza();

	// MIFID
	boolean erroreControlliConcentrazioneFia = false;
	String erroriAdeguatezzaMifid = erroriAdeguatezza[0];
	if(erroriAdeguatezzaMifid != null){
		if(erroriAdeguatezzaMifid.startsWith(ControlliConcentrazioneFia.ERRORE_CONTROLLI_CONCENTRAZIONE_FIA_INDICATOR)){
			erroreControlliConcentrazioneFia = true;
			erroriAdeguatezzaMifid = erroriAdeguatezzaMifid.substring(ControlliConcentrazioneFia.ERRORE_CONTROLLI_CONCENTRAZIONE_FIA_INDICATOR.length());
		}else if(erroriAdeguatezzaMifid.indexOf("Manleva presente su idAdeguatezzaPadre")>=0){
			// Con la rfc #260880, quando non da 5D, si è dovuto gestire il caso in cui il motore di ageguatezza torna un errore causato dal fatto
			// che a fronte di una chiamata fatta dopo la manleva con RDA a "S" non si può più proseguire se non rifacendo daccapo anche la preview
			// Testare parte della descrizione è cosa poco pulita ma il servzio non torna il caso in modo specifico.
			erroriAdeguatezzaMifid = "L'azione appena eseguita ha invalidato il processo di verifica di adeguatezza; "+
									 "per poter completare l'operazione è necessario aprire nuovamente la bozza della proposta presente nella sezione Operazioni dell'area cliente.<br>"+
									 "Ti consigliamo di verificare i dati già inseriti nella bozza e assicurarti di avere tutte le informazioni necessarie prima di procedere.";
		}
	}
	
	
	boolean isWarningMifid = erroriAdeguatezzaMifid == null || (erroriAdeguatezzaMifid != null && erroriAdeguatezzaMifid.startsWith(MifidCaller.WARNING_INDICATOR));
	ArrayList<String> elencoMsgAdgMifid = MifidCaller.formattaErroriAdeguatezzaMifid(erroriAdeguatezzaMifid);

	// IDD
	String erroriAdeguatezzaIdd = erroriAdeguatezza[1];
	boolean isWarningIdd = erroriAdeguatezzaIdd == null;
	
	// SOSTITUZIONI
	String erroriAdeguatezzaSostituzioni = erroriAdeguatezza[2];
	boolean isWarningSostituzioni = erroriAdeguatezzaSostituzioni == null || (erroriAdeguatezzaSostituzioni != null && erroriAdeguatezzaSostituzioni.startsWith(MifidCaller.WARNING_INDICATOR));
	erroriAdeguatezzaSostituzioni = SostituzioniCaller.formattaErroriSostituzioni(erroriAdeguatezzaSostituzioni);
	
	// SALDO BASKET
	String erroriSaldo = erroriAdeguatezza[3];
	boolean isWarningSaldo = erroriSaldo == null || (erroriSaldo != null && erroriSaldo.startsWith(MifidCaller.WARNING_INDICATOR));

	boolean isWarning = isWarningMifid && isWarningIdd && isWarningSostituzioni && isWarningSaldo;

	if(model.getIsSede().booleanValue() && model.isAdeguatezzaOnPreview())
		isWarning = true;

%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/SignProcess.css'/>
<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script>
function doBack(){
	wait();
	document.goBackForm.submit();
	return false;
}
function doProsegui(){
	wait();
	document.proseguiForm.submit();
	return false;
}
</script>
<style>
body{
	margin: 0;
	padding: 0;
	border: 0;
	height: 100%;
	overflow: hidden;
}
</style>
<%@ include file="./PdfReady.html"%>
</head>

<body>

<form name="goBackForm" method="post" action="call.wfem" style="display:none;">
<% if(model.isAdeguatezzaOnPreview()){ %>
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GotoLastPdf.execute">
<% }else{ %>
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.GoBackOnError.execute">
<% } %>
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="proseguiForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.SkipMifidWarnings.execute">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="isMifidSkipped" value="true">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<table class="htmlContainer">
<tr>
	<td style="height:100%;">	
	<div style="height:100%;overflow-y:auto;">
	<table>
	<% if(erroriAdeguatezzaMifid != null){ %>
	<tr>
		<td valign="top" style="padding: 30;">
			<table class='titolo'>
				<tr>
					<td>
						<% if(erroreControlliConcentrazioneFia){ %>
							La proposta non soddisfa i controlli sulla concentrazione fondi FIA per:
						<% }else if(isWarning){ %>
							Attenzione:
						<% }else{ %>
							Attenzione: la proposta risulta NON adeguata
						<% } %>						
					</td>
				</tr>
				<tr>
					<td>
						<table class='testo'>
							<% for(String msg : elencoMsgAdgMifid){ %>
								<tr><td style="padding-bottom:5px;"><%=msg%></td></tr>
							<% } %>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% } %>
	<% if(erroriAdeguatezzaIdd != null){ %>
	<tr>
		<td valign="top" style="padding: 30;">
			<table class='titolo'>
				<tr>
					<td>
						<b>Attenzione: IDD - Non è possibile completare la richiesta</b>
					</td>
				</tr>
				<tr>
					<td>
						<table class='testo'>
							<tr><td><%=erroriAdeguatezzaIdd%></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% } %>
	<% if(erroriAdeguatezzaSostituzioni != null){ %>
	<tr>
		<td valign="top" style="padding: 30;">
			<table class='titolo'>
				<tr>
					<td>
						<b>Attenzione: Sostituzioni - la proposta risulta NON adeguata</b>
					</td>
				</tr>
				<tr>
					<td>
						<table class='testo'>
							<tr><td><%=erroriAdeguatezzaSostituzioni%></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% } %>
	<% if(erroriSaldo != null){ %>
	<tr>
		<td valign="top" style="padding: 30;">
			<table class='titolo'>
				<tr>
					<td>
						<b>Esito Controllo del Saldo</b>
					</td>
				</tr>
				<tr>
					<td>
						<table class='testo'>
							<tr><td><%=(erroriSaldo.startsWith(MifidCaller.WARNING_INDICATOR)?erroriSaldo.substring(MifidCaller.WARNING_INDICATOR.length()):erroriSaldo)%></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% } %>
	</table>
	</div>
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
							<td><div class="normalButton" onclick="doBack();">Indietro</div></td>
						</tr>
					</table>
				</td>				
				<td align="right" style="padding:10;padding-top:3;">
					<% if(isWarning){ %>
					<table>
						<tr>
							<td><div class="normalButton" onclick="doProsegui();">Prosegui</div></td>
						</tr>
					</table>
					<% } %>						
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
<% model.setErroriAdeguatezza(null);%>
