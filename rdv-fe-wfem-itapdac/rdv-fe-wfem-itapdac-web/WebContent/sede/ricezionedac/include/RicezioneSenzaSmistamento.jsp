<tr>
	<td id="documentiCont" height="100%" valign="top">
		<table width="100%" cellpadding="0" cellspacing="0" height="100%">
		<tr>
			<td valign="top" align="center">
				<%
					int docRicevuti=0;
					int docNonPervenuti=0;
					for (int i=0; i<model.getDocumenti().size(); i++){
						DocumentoModel doc = (DocumentoModel) model.getDocumenti().get(i);
						
						boolean docRicevuto = doc.getIdDac().equals(Costanti.ID_DAC_X_DOC_FUORI_DAC) && 
												!doc.getUbicazione().equals(Costanti.UBICAZIONE_IN_VIAGGIO) &&
												!doc.getIsErroreSmistamento().booleanValue();
						if (docRicevuto)
							docRicevuti++;
						
						boolean docNonPervenuto = doc.getNonPervenuto().booleanValue() && !doc.getIsErroreSmistamento().booleanValue();
						if (docNonPervenuto)
							docNonPervenuti++;
						
					}
				%>
				<script>
					<% if (model.hasCommandErrors()) { %>
							document.all("BGSOUND_ID").src = "<%=template.getWebApp()%>/resources/sounds/glass.wav";
					<% } %>

					docRicevuti = <%=docRicevuti%>;
					docNonPervenuti = <%=docNonPervenuti%>;
				</script>
				<table width='100%' cellpadding="0" cellspacing="0">
				<tr>
					<td class="text" align="right" style="font-weight: bold;">
						Documenti<%if(model.isMgmPlichi()){%>/Plichi<%}%>: <font color="green">Ricevuti <span id="nDocRicevuti"><%=docRicevuti%></span></font>,
									<font color="red">Non pervenuti <span id="nDocNonPervenuti"><%=docNonPervenuti%></span></font>
					</td>
					<td width="2%"></td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td valign="top" align="center" height="100%">
				<form name="documentiForm" method="post" action="call.wfem" style="margin:0; height: 100%;">
				<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
				<input type="hidden" name="wfemCmd" value="">
				<input type="hidden" name="checkCodDescFields" value="false">
				<%=template.hidden("noteOperatore")%>

				<%
					String colsModSenzaSmistamento = "cols='#idDocumento,#idDac,#codAggregatore,#isFirstInPlico,#isLastInPlico,#ubicazione,#aggiuntoInRicezione,#codProdotto,#isInBusta,"+
															"idPlico,barcode,descrProdotto,numeroContratto,descrOperazione,cliente_nominativo,nonPervenuto,isErroreSmistamento' ";
					String dimModSenzaSmistamento = "colswidths='5%,10%,15%,10%,20%,*,7%,3%' "; 
					
					GestoreEditabilita.setEditabilitaDocumenti(model.getDocumenti());
				%>
			
				<%=template.grid("documenti", colsModSenzaSmistamento + dimModSenzaSmistamento +
			    								 "title='"+model.htmlTitoloGrigliaDocumenti()+"' "+
												"onclick='doVisualizzaDoc(this);' "+
												"onnewcell='onNewCell(this);' "+
												"headerheight='35px' "+
												"headernowrap='false' "+
												"cellheight='23px' "+
												"modality='update' " +
												"helper='yes' " +
												"sortable='false' "+
												"selection='single' "+
												"width='100%' height='100%' ")%>
				</form>
			</td>
		</tr>
		<tr>
			<td height="30px" valign="bottom">
				<%=template.getMessagesAndErrors()%>
			</td>
		</tr>
		</table>
		
		<script>
			document.documentiGrid.showRow((document.documentiGrid.tableModel.rows.length - 1));	//Mi posiziono sull'ultima riga
		</script>
	</td>
</tr>