<%@page import="prgm.ita.p.dac.facade.Costanti"%>
<%
	erroreInLettura = !doc.getErroreInLettura().getCodErrore().isNull() && !doc.isWarningInLettura();
	warningInLettura = !doc.getErroreInLettura().getCodErrore().isNull() && doc.isWarningInLettura();
	String descrErrore = "";

	if (erroreInLettura || warningInLettura) {
		descrErrore = doc.getErroreInLettura().getDescValue("codErrore");

		if (!doc.getErroreInLettura().getParametroUno().isNull())
			descrErrore += 	" [ " + doc.getErroreInLettura().getParametroUno().toString() + " ]";
	}

	boolean docConTableAccess = false;
	if (doc.getDescrProdotto().isNull())
		docConTableAccess = true;
%>

<table width='100%' cellspacing='0px' cellpadding='0px' style='table-layout:fixed;'>
<tr class='gridDSRigaDocumento'>
	<td class='gridDSCellaDocumento' width='10%' height='18px' title='<%=doc.getBarcode()%>' nowrap><%=doc.getBarcode()%></td>

	<% 
		if (erroreInLettura) {
			//Riga di errore
	%>
			<td class='gridDSCellaAttesa' width='61%'>&nbsp;</td>
			<td class='gridDSCellaStrumenti' width='29%' id='wait<%=doc.getBarcode()%>' align='center' style='background-color:#FF3333; color: white;'><%=descrErrore%></td>
	<% 
		} else { 
			//Riga documento
	%>
			<% if (doc.getCodProdotto().equals(Costanti.DOC_ASSEGNO)) { %>
				<td class='gridDSCellaDocumento' width='13%' title='Assegno' nowrap>&nbsp;Assegno n. <%=doc.getNumeroContratto()%></td>
			<% }else if (docConTableAccess) { %>
				<td class='gridDSCellaDocumento' width='13%' title='<%=doc.getDescValue("codProdotto")%>' nowrap>&nbsp;<%=doc.getDescValue("codProdotto")%></td>
			<% } else { %>
				<td class='gridDSCellaDocumento' width='13%' title='<%=doc.getDescrProdotto()%>' nowrap>&nbsp;<%=doc.getDescrProdotto()%></td>
			<% } %>			
		
			<% if (doc.getCodProdotto().equals(Costanti.DOC_ASSEGNO)) { %>
				<td class='gridDSCellaDocumento' width='10%' align='center' nowrap>&nbsp;</td>
			<% }else{ %>
				<td class='gridDSCellaDocumento' width='10%' align='center' title='<%=doc.getNumeroContratto()%>' nowrap>&nbsp;<%=doc.getNumeroContratto()%></td>
			<% } %>
			
			<% if (doc.getCodProdotto().equals(Costanti.DOC_ASSEGNO)) { %>
				<td class='gridDSCellaDocumento' width='15%' align='center' nowrap>&nbsp;</td>
			<% }else if (docConTableAccess) { %>
				<td class='gridDSCellaDocumento' width='15%' title='<%=doc.getDescValue("codOperazione")%>' nowrap>&nbsp;<%=doc.getDescValue("codOperazione")%></td>
			<% } else { %>
				<td class='gridDSCellaDocumento' width='15%' title='<%=doc.getDescrOperazione()%>' nowrap>&nbsp;<%=doc.getDescrOperazione()%></td>
			<% } %>			
		
			<td class='gridDSCellaDocumento' width='8%' align='center' title='<%=doc.getAgente().getCodAgente()%>' nowrap>&nbsp;<%=doc.getAgente().getCodAgente()%></td>
			<td class='gridDSCellaDocumento' width='15%' title='<%=doc.getCliente().getNominativo()%>' nowrap>&nbsp;<%=doc.getCliente().getNominativo()%></td>	

			<td class='gridDSCellaStrumenti' width='29%' id='wait<%=doc.getBarcode()%>' align='center' valign="middle" style='background-color:yellowgreen;color: #1A458F;'>
				Documento aggiunto&nbsp;
				<% if (warningInLettura) { %>
					&nbsp;<img style='cursor:hand;' align="absmiddle" onmouseover="tooltip.show('<%=descrErrore%>');" onmouseout="tooltip.hide();" src='<%=template.getWebApp()%>/images/warning.gif'>
				<% } %>
	
				<% if (!dacReadonly) {%>
					<img style='cursor:hand;' align="absmiddle" title='Rimuovi documento' src='<%=template.getWebApp()%>/images/deleteDocDaDac.gif'
						onclick="rimuoviDocumento('<%=doc.getIdDocumento()%>', '<%=doc.getBarcode()%>');">
				<%}else{%>
					&nbsp;
				<%}%>
				&nbsp;<img style='cursor:hand;' align="absmiddle" title='Visualizza documento' src='<%=template.getWebApp()%>/images/viewDocInDac.gif' onclick="apriDocumento('<%=doc.getIdDocumento()%>');">
			</td>

	<% } %>
</tr>
</table>