<%@page import="com.atosorigin.wfem.types.StringType"%>
<%@page import="com.atosorigin.wfem.command.MapCommandDataModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	MapCommandDataModel cliData = (MapCommandDataModel)template.getPageDataModel();
	
	StringType codMediolanum = (StringType)cliData.readProperty("codMediolanum");
	
	StringType layout = (StringType)cliData.readProperty("layout");
	StringType modalita = (StringType)cliData.readProperty("modalita");
	StringType srvErrors = (StringType)cliData.readProperty("srvErrors");
	boolean showFdButton = true; 
	if(modalita.equals("OLOGRAFA"))
		showFdButton = false;
	String fdButtonText = "Firma digitale e Copernico"; 
	String fdJsMethod = "launchFD();";
	if(modalita.equals("OLOGRAFA&COPERNICO")){
		fdButtonText = "Copernico";
		fdJsMethod = "launchCopernico();";
	}
	if(modalita.equals("OLOGRAFA&DIGITALE")){
		fdButtonText = "Firma digitale";
		fdJsMethod = "launchOnlyFD();";
	}
	
%>
<html>
<head>
<%=template.getHeader()%>
<style>
.title {
	color : #5b9bc1;
	font-family: Segoe UI; 
	font-size: 18px; 
	font-style: normal; 
	font-weight: normal; 
	text-decoration: none;
}
.msg{
	font-family: Segoe UI;
	font-size: 14px;
	color: #666666;
}
.normalButton{
	background-color: #a3cbe5;
	color: #fff;
	font-family: Segoe UI;
	font-size: 14px;
	text-align: center;
	vertical-align: middle;	
	position: relative;
	cursor: pointer;
	padding-left: 12px;
	padding-right: 12px;
	padding-top: 6px;
	padding-bottom: 6px;
}

.normalButtonSel{
	background-color: #2068a5;
}

.normalButtonDisab{
	cursor: default;
}
</style>
<script>
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
});

function launchFD(){
	startRequest();
	document.varazioneForm.wfemCmd.value="prgm.ita.p.cew.variazionianagrafiche.conilcliente.business.NuovaVariazioneAnagraficaCliente.executeProcessOnNewStack";
	document.varazioneForm.submit();
}
function launchOnlyFD(){
	startRequest();
	document.varazioneForm.modalitaDiSottoscrizione.value="DIGITALE";
	document.varazioneForm.wfemCmd.value="prgm.ita.p.cew.variazionianagrafiche.conilcliente.business.NuovaVariazioneAnagraficaCliente.executeProcessOnNewStack";
	document.varazioneForm.submit();
}
function launchCopernico(){
	startRequest();
	document.varazioneForm.modalitaDiSottoscrizione.value="COPERNICO";
	document.varazioneForm.wfemCmd.value="prgm.ita.p.cew.variazionianagrafiche.conilcliente.business.NuovaVariazioneAnagraficaCliente.executeProcessOnNewStack";
	document.varazioneForm.submit();
}
function launchFO(){
	startRequest();
	document.varazioneForm.wfemCmd.value="prgm.ita.anagraficaclienti.business.ApriAnagraficaCliente.executeProcessOnNewStack";
	document.varazioneForm.submit();
}
function openPdfProfit(){
	startRequest();
	document.pdfForm.pdfCode.value="SAMA VPG";
	document.pdfForm.submit();
}
function openPdfNoProfit(){
	startRequest();
	document.pdfForm.pdfCode.value="SAMA VPG NO PROFIT";
	document.pdfForm.submit();
}
</script>
</head>

<body>

<form name="varazioneForm" method="post" action="call.wfem" style="display:none;">  
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="codMediolanum" value="<%=codMediolanum%>">
<input type="hidden" name="modalitaDiSottoscrizione" value="">
</form>

<form name="pdfForm" method="post" action="call.wfem" style="display:none;">  
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.stream.PdfOpen.executeProcessOnNewStack">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="pdfCode" value="">
</form>

<table width="100%" style="background-color:white;"><tr><td align="center">
	<% if(layout.equals("FISICA")){ %>
		<table width="80%" style="background-color:white;">
			<tr><td class="title" style="padding:20px;">Seleziona la modalità di firma per la variazione anagrafica</td></tr>
			<tr><td style="background-color: #ffffff; border-bottom-color: silver; border-bottom-width: 1pt; border-bottom-style: solid; height: 1pt; font-size: 5px;">&nbsp;</td></tr> 	   
			<% if(!srvErrors.isNull()){ %>
				<tr><td class="msg">Per problemi tecnici è possibile procedere solo con stampa e firma<br><span style="font-size:8px;">(err: <%=srvErrors%>)</span></td></tr>
			<% } %>
			<tr>
				<td align="center" style="padding:30px;">
					<table><tr>
						<% if(showFdButton){ %>
							<td><div class="normalButton" style="width:180px;" onclick="<%=fdJsMethod%>"><%=fdButtonText%></div></td>
							<td style="width:20px;"></td>
						<% } %>
						<td><div class="normalButton" style="width:180px;" onclick="launchFO();">Firma Olografa</div></td>
					</tr></table>
				</td>
			</tr>
		</table>
	<% }else if(layout.equals("GIURIDICA")){ %>
		<table width="80%" style="background-color:white;">
			<tr><td class="title" style="padding:20px;">Variazione anagrafica Persona Giuridica</td></tr>
			<tr><td style="background-color: #ffffff; border-bottom-color: silver; border-bottom-width: 1pt; border-bottom-style: solid; height: 1pt; font-size: 5px;">&nbsp;</td></tr>
			<tr><td class="msg">Per variare l'anagrafica di una persona giuridica è necessario utilizzare il modulo dedicato di Scheda anagrafica e adeguata verifica:</td></tr> 	   
			<tr>
				<td align="center" style="padding:30px;">
					<table><tr>
						<td><div class="normalButton" style="width:230px;" onclick="openPdfProfit();">Modulo Persona Giuridica Profit</div></td>
						<td style="width:20px;"></td>
						<td><div class="normalButton" style="width:230px;" onclick="openPdfNoProfit();">Modulo Persona Giuridica No Profit</div></td>
					</tr></table>
				</td>
			</tr>
		</table>
	<% } %>
</td></tr></table>
<%=template.getFooter()%>
</body>
</html>