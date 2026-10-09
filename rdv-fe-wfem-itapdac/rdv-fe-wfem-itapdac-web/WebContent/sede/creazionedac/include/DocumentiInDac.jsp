<%	String prefixBeforeIncludeDocumentiInDac = template.getLabelCodePrefix();
	template.setLabelCodePrefix("DocumentiInDac."); %>
	
<% if(!dacReadonly && !model.getIdDac().isNull()){ %>
	<form name="frmDocDaInserire" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.creazionedac.InserisciDocInDac.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="readRequest" value="false">
		<input type="hidden" name="barcode" value="">
		<input type="hidden" name="idDac" value="<%=model.getIdDac()%>">
		<input type="hidden" name="uffDestinatario" value="<%=model.getUffDestinatario()%>">
		<input type="hidden" name="codCassetta" value="<%=model.getCassettaBox()%>">
		<input type="hidden" name="reso" value="true">
		<%=model.htmlParams("","")%>
	</form>

	<form name="frmDocDaRimuovere" method="post" action="call.wfem" style="display:none">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.creazionedac.RimuoviDocDaDac.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="idDac" value="<%=model.getIdDac()%>">
		<input type="hidden" name="idDocumento" value="">
		<input type="hidden" name="barcode" value="">
	</form>

	<form name="frmDocDaSpinzare" method="post" action="call.wfem" style="display:none">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.sede.creazionedac.SpinzaDocDaDac.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="idDac" value="<%=model.getIdDac()%>">
		<input type="hidden" name="idDocumento" value="">
	</form>

	<!--	#########################			BARCODE DOCUMENTO		#########################	-->
	<tr>
		<td>
			<form name="frmDocSparato" style="margin:0;">
			  	<table width="96%" align="center">
				<tr>
					<td width="1%"><%=template.field("barcode", "maxlength='50'")%></td>
			  		<td width="1%">
			  			<input type="button" id="aggiungiDocumento" name="aggiungiDocumento" class="action" style='width:80;' value="Aggiungi" onclick="doAggiungiDocumento();">
			  		</td>
					<td width="98%">&nbsp;</td>
				</tr>
			  	</table>
			</form>
		</td>
	</tr>
	<!--	#########################			END BARCODE DOCUMENTO	#########################	-->
<%} %>

<!--	#########################			ELENCO DOCUMENTI		#########################	-->
<tr>
	<td valign="top" align="center">
	  <!-- Titolo Grid -->
	  <table id="titoloElencoDoc" style="display:<%if(model.getDocumenti().size()==0){%>none<%}%>;" width='100%' cellpadding="0" cellspacing="0">
		<tr><td height="10px" colspan="6"></td></tr>
		<tr bgcolor="#fffacd">
			<td colspan="2" class="text" style="font-weight: bold;" height="15px">Distinta documenti</td>
			<td colspan="4"></td>
			<td>
				<table width="100%" cellpadding="1px" cellspacing="0">
				<tr>
					<td class="text" valign="middle" align="center" style="font-weight: bold;" width="40%" id="tdNumDocErr" style="visibility:hidden;color:red">Errori : <span id="nDocErr"></span></td>
					<td class="text" valign="middle" align="center" style="font-weight: bold;" width="60%">Documenti inseriti: <span id="nDocIns"><%=model.getDocumenti().size()%></span></td>
				</tr>
				</table>							
			</td>
		</tr>
		<!-- Titolo Elenco -->
		<tr class='gridDSRigaTitolo'>
			<td width='10%' class='gridDSCellaTitolo' height="20px">Barcode</td>
			<td width='13%' class='gridDSCellaTitolo'>Prodotto</td>
			<td width='10%' class='gridDSCellaTitolo'>Contratto/Polizza</td>
			<td width='15%' class='gridDSCellaTitolo'>Operazione</td>
			<td width='8%' class='gridDSCellaTitolo'>Family Banker</td>
			<td width='15%' class='gridDSCellaTitolo'>Cliente</td>
			<td width='29%' class='gridDSCellaTitolo'>&nbsp;</td>
		</tr>
	  </table>
   </td>
</tr>
<tr>
	<td valign="top" align="left" height="100%">
		<div id="elencoDoc" style="overflow:auto;height:100%;width:100%;border:0;margin:0;">

			<!-- Body -->
			<% for (int i=0; i<model.getDocumenti().size(); i++) {
					DocumentoModel doc = (DocumentoModel)model.getDocumenti().get(i); 					
					
					boolean erroreInLettura = false;
					boolean warningInLettura = false;
			%>
					<div id='<%=doc.getBarcode()%>Cont'>
						<div id='<%=doc.getBarcode()%>' style='border:0;margin:0;width:100%;'>
							<%@ include file="DocumentoDaAggiungere.jsp"%>
						</div>
					</div>
			<% } %>

		</div>
	</td>
</tr>	  
<tr>
	<td align="center" valign="middle" id="docCont">
		<%@ include file="../../../documento/Documento.html"%>
	</td>
</tr>
<!--	#########################		END ELENCO DOCUMENTI		#########################	-->

<!--	#########################	ELENCO PLICHI CON DOCUMENTI MANCANTI	#######################	-->
<tr>
	<td align="center" valign="middle">
		<%@ include file="DocumentiPlichiMancanti.jsp"%>
	</td>
</tr>
<!--	#########################	END  PLICHI CON DOCUMENTI MANCANTI	#######################	-->
<%	
	template.restorePrefix();
	template.setLabelCodePrefix(prefixBeforeIncludeDocumentiInDac); 
%>