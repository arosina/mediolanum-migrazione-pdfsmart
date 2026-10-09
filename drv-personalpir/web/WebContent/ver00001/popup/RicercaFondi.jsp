<%@page import="prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup.RicercaFondiModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	template.setApplCode("PDFWEBFORMSDRIVERS-PERSONALPIRVARIAZIONEPIANOPAC");
	template.setPageName("RicercaFondi");
	template.setLabelAlign("left");
	template.setLabelPosition(com.atosorigin.wfem.layout.Template.UP_LABEL);
	RicercaFondiModel model = (RicercaFondiModel)template.getPageDataModel();
%>
<html>
<head>
<%=template.getHeader()%>
<script>
var numeroFondiSelezionatiInGriglia = <%=(model.getFondiSelezionati().isNull()?0:model.getFondiSelezionati().toString().split(",").length)%>;

function doEseguiRicerca(){
	document.getElementById("societa").value = document.getElementById("societaAppoggio").value; 
	document.getElementById("tipologia").value = document.getElementById("tipologiaAppoggio").value; 
	document.getElementById("denominazione").value = document.getElementById("denominazioneAppoggio").value;
	document.getElementById("inPortafoglio").value = document.getElementById("inPortafoglioAppoggio").value;
	startRequest();
	wfemHiddenSubmit(document.ricercaFondiForm,"elenco,buttonArea");
}

function newCell(cell){
	if(cell.propertyName == 'flagConsolidaAz') {
		if (cell.innerHTML == 'S') {
			cell.innerHTML = 'Si';
		} else if (cell.innerHTML == 'N') {
			cell.innerHTML = 'No';
		}
	}
	if(cell.propertyName == 'selected'){
		cell.align="center";
	}
}

function pulisci(){
	document.getElementById("societaAppoggio").value = "";
	document.getElementById("tipologiaAppoggio").value = "";
	document.getElementById("denominazioneAppoggio").value = "";
	document.getElementById("inPortafoglioAppoggio").value = "";
	
}
function doConferma() {
	var rows = document.getElementById("elencoFondiGridTab").rows;
	var returnObj = new Array();
	var numRigheSelezionate = 0;
	for (i=0;i<rows.length;i++){
		if (document.getElementById("elencoFondi"+rows[i].absIndex+"_selectedCheck").checked){
			numRigheSelezionate++;
			returnObj.push({"lineaFondo":rows[i].lineaFondo, "codiceFondo":rows[i].codiceFondo, "importoMinimo":rows[i].importoMinimo, "societaFondo":rows[i].sicav, "isinFondo":rows[i].isin, "descrizioneFondo":rows[i].descFondo, "controvaloreFondo":rows[i].controvaloreComparto });
		}
	}

	var numeroFondiSelezionabili = 35 - numeroFondiSelezionatiInGriglia;
	
	if (returnObj.length == 0){
		alert("Selezionare almeno un fondo.");
	} else if (numeroFondiSelezionabili <= 0){
		alert("Non è possibile selezionare ulteriori fondi.");		
	} else if (numRigheSelezionate > numeroFondiSelezionabili){
		alert("Non è possibile selezionare più di "+numeroFondiSelezionabili+" "+(numeroFondiSelezionabili==1?"fondo":"fondi")+".");
	} else {
		closeModalPopup(returnObj);	
	}

}
</script>
</head>

<body>

<table width="100%" height="98%">
	<tr>
		<td>
			<fieldset class="fieldsGroup">
				<legend class="text" style="font-weight: bold;">
				   &nbsp;<%=template.getProperty(template.getPageName()+"clear")%>
				   &nbsp;<img title="Pulisci campi" src="<%=template.getWebApp()%>/images/clear.gif" onclick="pulisci();" style="cursor: pointer;">
	 			</legend>
	
				<table width="100%" cellspacing="3">
				    	<tr>
				    		<td width="60%"><%=template.field("societaAppoggio", "labelwidth='18%' style='width: 100%;'")%></td>
		    				<td width="40%"><%=template.field("tipologiaAppoggio", "labelwidth='20%' style='width: 100%;'")%></td>
		    			</tr>
				    	<tr>
				       		<td width="60%"><%=template.field("denominazioneAppoggio","style='width: 100%;'")%></td>
				       		<td width="40%"><%=template.field("inPortafoglioAppoggio","style='width: 100%;'")%></td>	
				    	</tr>
						<tr><td colspan="2"><hr></td></tr>
				    	<tr><td colspan="2" valign="middle"><%=template.action("eseguiRicerca", "style='width: 100px;'")%></td></tr>
	   				</table>
			</fieldset>
		</td>
	</tr>
		<td id='elenco' height="100%">
		<% if (model.isPrimaAttivazione()) { %>
	        &nbsp;
		<% } else { %>
	        <%
				String hiddenCols = "#codiceFondo,#importoMinimo,#controvalore";
				String colonne = null;
				String dimensioniColonne = null;
				
				colonne = "cols='"+hiddenCols+",lineaFondo,isin,sicav,descFondo,classe,flagConsolidaAz,tipologia,controvaloreComparto,selected' ";
				dimensioniColonne = "colswidths='6%,12%,16%,*,6%,7%,12%,12%,5%' ";		
            %>
	        <%=template.grid("elencoFondi",
					colonne
					+ dimensioniColonne
					+ "selection='none' "
					+ "width='100%' "
					+ "height='100%' "
					+ "headerheight='40px' "
					+ "headernowrap='false'"
					+ "modality='insert' " 
	 				+ "onnewcell='newCell(this);' " 				
	 				+ "helper='yes' "
	 				+ "showcounter='true' "
	 				+ "showtoparea='false' "
	 				+ "decorator='prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup.RicercaFondi' "
	 				+ "noRowsMsg='Nessun fondo trovato.'") %>
	 	<% } %>
		</td>
	</tr>
	<tr>
		<td align="center" id="buttonArea">
		<% if (model.getElencoFondi().size() > 0){	%>
			<%=template.action("conferma", "style='width: 100px;'")%>
		<% } %>
		</td>
	</tr>
	
</table>

<form name="ricercaFondiForm" method="post" action="call.wfem">
	<input type="hidden" name="wfemCmd" value="prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup.RicercaFondi.execute">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	<input type="hidden" id="societa" name="societa" value="">
	<input type="hidden" id="tipologia" name="tipologia" value="">
	<input type="hidden" id="denominazione" name="denominazione" value="">
	<input type="hidden" id="inPortafoglio" name="inPortafoglio" value="">
	
</form>

<%=template.getFooter()%>
<% model.setPrimaAttivazione(false); %>
</body>
</html>

