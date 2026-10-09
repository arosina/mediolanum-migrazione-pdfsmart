<table cellspacing="10px">
<tr>
<%
	enab = "update";
	onChangeUffDest = "verificaEsistenzaDac();";
	if (!model.getIdDac().isNull()){
		if(model.getDocumenti().size() > 0) enab = "read";
		
		//onChangeUffDest += "doSalvaDac();";
		onChangeTipoSpedizione = "onchange='doSalvaDac();' ";
		onChangeNoteAutore = "onchange='doSalvaDac();' ";
	}
%>
	<td><%=template.field("uffDestinatario", "modality='"+ enab + "' onchange='"+ onChangeUffDest +"'")%></td>
   	<td><%=template.field("codTipoSpedizione", onChangeTipoSpedizione + "showEmpty='false'")%></td>
   	<td><%=template.field("noteAutore", onChangeNoteAutore + "style='width:350;' maxlength='50'")%></td>
</tr>
<tr><td id="idMsgEsistenzaDacAttiva" colspan="3" class="text" style="color:red;">&nbsp;<%=model.getMsg()%><%model.setMsg("");%></td></tr>
<% 
	if (model.getIdDac().isNull() && !inErroreCtrlCassette){ %>
	<tr><td colspan="3"><%=template.action("inserisciDocumento")%></td></tr>
<% } %>
</table>