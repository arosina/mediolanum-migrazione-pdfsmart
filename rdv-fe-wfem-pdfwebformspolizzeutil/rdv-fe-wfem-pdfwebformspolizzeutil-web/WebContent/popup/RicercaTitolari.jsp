<%@page import="prgm.pdfwebformspolizzeutil.popup.RicercaBeneficiariModel"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	template.setApplCode("PDFWEBFORMSDRIVERSPOLIZZEUTIL");
	template.setPageName("RicercaTitolari");
	template.setLabelAlign("left");
	template.setLabelPosition(com.atosorigin.wfem.layout.Template.UP_LABEL);
	RicercaBeneficiariModel model = (RicercaBeneficiariModel)template.getPageDataModel();
%>
<html>
<head>
<%=template.getHeader()%>
<script>

function doEseguiRicerca(){
	if (document.getElementById("codiceClienteAppoggio").value === ""
			&& document.getElementById("codiceFiscaleAppoggio").value === ""
			&& document.getElementById("cognomeAppoggio").value === ""){
		alert("Indicare almeno un campo tra Codice Cliente, Codice Fiscale e Cognome");
		return;
	}
	document.getElementById("codiceCliente").value = document.getElementById("codiceClienteAppoggio").value; 
	document.getElementById("codiceFiscale").value = document.getElementById("codiceFiscaleAppoggio").value; 
	document.getElementById("nome").value = document.getElementById("nomeAppoggio").value; 
	document.getElementById("cognome").value = document.getElementById("cognomeAppoggio").value; 
	startRequest();
	wfemHiddenSubmit(document.ricercaBeneficiariForm,"elenco,buttonArea");
}

function newCell(cell){
	
}

function pulisci(){
	document.getElementById("codiceClienteAppoggio").value = "";
	document.getElementById("codiceFiscaleAppoggio").value = ""; 
	document.getElementById("nomeAppoggio").value = ""; 
	document.getElementById("cognomeAppoggio").value = ""; 
}

function selezionaTitolare(titolareIn) {
	
	var returnObj = new Array();
	
	returnObj.codiceCliente = titolareIn.codiceCliente;
	
	
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
				    		<td width="50%"><%=template.field("nomeAppoggio", "labelwidth='18%' style='width: 100%;'")%></td>
		    				<td width="50%"><%=template.field("cognomeAppoggio", "labelwidth='20%' style='width: 100%;'")%></td>
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
				
				colonne = "cols='"+hiddenCols+",codiceCliente,codiceFiscale,nome,cognome' ";
				dimensioniColonne = "colswidths='20%,20%,20%,40%' ";		
			%>
			<%=template.grid("elencoBeneficiari",
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
	 				+  "onclick='selezionaTitolare(this);' "
	 				+ "decorator='prgm.pdfwebformspolizzeutil.popup.RicercaTitolari' "
	 				+ "noRowsMsg='Nessun titolare trovato.'") %>
	 	<% } %>
		</td>
	</tr>
	<tr>
		<td align="center" id="buttonArea">
		
		</td>
	</tr>

</table>

<form name="ricercaBeneficiariForm" method="post" action="call.wfem">
	<input type="hidden" name="wfemCmd" value="prgm.pdfwebformspolizzeutil.popup.RicercaTitolari.execute">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	<input type="hidden" id="codiceCliente" name="codiceCliente" value="">
	<input type="hidden" id="codiceFiscale" name="codiceFiscale" value="">
	<input type="hidden" id="nome" name="nome" value="">
	<input type="hidden" id="cognome" name="cognome" value="">
</form>

<%=template.getFooter()%>
<% model.setPrimaAttivazione(false); %>
</body>
</html>

