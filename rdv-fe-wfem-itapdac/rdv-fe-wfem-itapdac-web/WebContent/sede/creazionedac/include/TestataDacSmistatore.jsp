<table cellspacing="10px" width="70%">
<tr>
<%
	if (!model.getCodTipoSpedizione().equals(Costanti.MEZZO_SPEDIZIONE_CORRIERE))
		enabOraSpedizione="modality='read' ";
		
	onChangeBox = "verificaEsistenzaDac();";
	onChangeTipoSpedizione = "showOraSpedizione(this, false);";
	onChangeUffDest = "loadBox()";
	enab = "update";
	if (!model.getIdDac().isNull()){
		if(model.getDocumenti().size() > 0) enab = "read";
		
//		onChangeBox += "doSalvaDac();";
		onChangeTipoSpedizione = "showOraSpedizione(this, true);";
		onChangeOraSpedizione = "onchange='doSalvaDac();' ";
		onChangeNoteAutore = "onchange='doSalvaDac();' ";
	}
%>
	<td width="20%"><%=template.field("uffDestinatario", "modality='"+enab+"' onchange='"+onChangeUffDest+"'")%></td>
	<td width="20%" id="boxCont"><%=template.field("box", "modality='"+enab+"' onchange='"+onChangeBox+"'")%></td>
   	<td width="30%"><%=template.field("codTipoSpedizione", "onchange='"+onChangeTipoSpedizione+"' labelalign='left' showEmpty='false'")%></td>
	<td width="30%" class="text"><%=template.field("oraSpedizione", onChangeOraSpedizione+enabOraSpedizione+"style='width: 80px;'")%></td>
  	</tr>
<tr>
	<td id="idMsgEsistenzaDacAttiva" colspan="2" class="text" style="color:red;">&nbsp;<%=model.getMsg()%><%model.setMsg("");%></td>
   	<td colspan="2"><%=template.field("noteAutore", onChangeNoteAutore+"style='width:365;' maxlength='50' labelalign='left' labelwidth='10%' fieldalign='left'")%></td>
</tr>
<tr><td colspan="4"><hr></td></tr>
<% 
	if (model.getIdDac().isNull() && !inErroreCtrlCassette){ %>
	<tr><td colspan="4"><%=template.action("inserisciDocumento")%></td></tr>
	
<% } %>
</table>