<%@page import="prgm.pdfwebformspolizzeutil.popup.RicercaPopupModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	template.setApplCode("PDFWEBFORMSDRIVERSPOLIZZEUTIL");
	template.setPageName("RicercaPopup");
	template.setLabelAlign("left");
	template.setLabelPosition(com.atosorigin.wfem.layout.Template.UP_LABEL);
	RicercaPopupModel model = (RicercaPopupModel)template.getPageDataModel();
%>
<html>
<head>
<%=template.getHeader()%>
<script>

function doEseguiRicerca(){
	if (document.getElementById("codiceClienteAppoggio").value === ""
			&& document.getElementById("codiceFiscaleAppoggio").value === ""
			&& document.getElementById("ragioneSocialeAppoggio").value === ""){
		alert("Indicare almeno un campo tra Codice Cliente, Codice Fiscale/Partiva IVA e Ragione Sociale");
		return;
	}
	document.getElementById("codiceCliente").value = document.getElementById("codiceClienteAppoggio").value; 
	document.getElementById("codiceFiscale").value = document.getElementById("codiceFiscaleAppoggio").value; 
	document.getElementById("ragioneSociale").value = document.getElementById("ragioneSocialeAppoggio").value; 
	startRequest();
	wfemHiddenSubmit(document.ricercaPopupForm,"elenco,buttonArea");
}

function newCell(cell){
	
}

function pulisci(){
	document.getElementById("codiceClienteAppoggio").value = "";
	document.getElementById("codiceFiscaleAppoggio").value = ""; 
	document.getElementById("ragioneSocialeAppoggio").value = ""; 
}

function selezionaElemento(elemento) {
	
	var returnObj = new Array();
	returnObj.codiceCliente = elemento.codiceCliente;
	returnObj.codiceFiscale = elemento.codiceFiscale;
	returnObj.ragioneSociale = elemento.ragioneSociale;	
	closeModalPopup(returnObj);	
		
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
				    		<td width="50%"><%=template.field("codiceClienteAppoggio", "labelwidth='18%' style='width: 100%;'")%></td>
		    				<td width="50%"><%=template.field("codiceFiscaleAppoggio", "labelwidth='20%' style='width: 100%;'")%></td>
		    			</tr>
				    	<tr>
		    				<td colspan="2"><%=template.field("ragioneSocialeAppoggio", "labelwidth='20%' style='width: 100%;'")%></td>
		    			</tr>
						<tr><td colspan="2"><hr></td></tr>
				    	<tr><td colspan="2" valign="middle"><%=template.action("eseguiRicerca", "style='width: 100px;'")%></td></tr>
	   				</table>
			</fieldset>
		</td>
	</tr>
	<tr>
		<td id='elenco' height="100%">
		<% if (model.isPrimaAttivazione()) { %>
			&nbsp;
		<% } else { %>
			<% 
				String hiddenCols = "";
				String colonne = null;
				String dimensioniColonne = null;
				
				colonne = "cols='"+hiddenCols+",codiceCliente,codiceFiscale,ragioneSociale' ";
				dimensioniColonne = "colswidths='20%,20%,60%' ";		
			%>
			<%=template.grid("elencoRicerca",
					colonne
					+ dimensioniColonne
					+ "width='100%' "
					+ "height='100%' "
					+ "headerheight='40px' "
					+ "headernowrap='false'"
	 				+ "onnewcell='newCell(this);' " 
	 				+ "selection='single' " 
	 				+ "helper='yes' "
	 				+ "showcounter='true' "
	 				+ "showtoparea='false' "
	 				+  "onclick='selezionaElemento(this);' "
	 				+ "decorator='prgm.pdfwebformspolizzeutil.popup.RicercaPopup' "
	 				+ "noRowsMsg='Nessun elemento trovato.'") %>
	 	<% } %>
		</td>
	</tr>
	<tr>
		<td align="center" id="buttonArea">
		
		</td>
	</tr>

</table>

<form name="ricercaPopupForm" method="post" action="call.wfem">
	<input type="hidden" name="wfemCmd" value="prgm.pdfwebformspolizzeutil.popup.RicercaPopup.execute">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	<input type="hidden" id="codiceCliente" name="codiceCliente" value="">
	<input type="hidden" id="codiceFiscale" name="codiceFiscale" value="">
	<input type="hidden" id="ragioneSociale" name="ragioneSociale" value="">
</form>

<%=template.getFooter()%>
<% model.setPrimaAttivazione(false); %>
</body>
</html>

