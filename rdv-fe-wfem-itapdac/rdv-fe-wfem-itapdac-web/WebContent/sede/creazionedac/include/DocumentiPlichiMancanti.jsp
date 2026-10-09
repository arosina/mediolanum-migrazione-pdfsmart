<%	String prefixBeforeIncludeDocumentiPlichiMancanti = template.getLabelCodePrefix();
	template.setLabelCodePrefix("DocumentiPlichiMancanti."); %>

<div id="docPlichiCont" style="position:relative;z-index:10;width:99%;">

<% if (model.getPlichiNonCompletiInSpedizione().size()>0) { %>
	<script>
		docInElencoConErrore = 0;
	</script>
	
   <table width="90%" class="text" cellpadding="0" cellspacing="0" style="border:solid 2px darkgray; background-color:#d0e4ef;">
	
     <tr>
		<td>
		     <!-- Titolo -->
			<table width="100%" style="background-color:#1A458F;color:lightgrey;" cellpadding="0" cellspacing="0" >
    		<tr>
			    <td style="height:25px;">&nbsp;<b>Elenco plichi/documenti mancanti</b></td>
			    <td align="right">
			    	&nbsp;
			    	<img src="<%=template.getWebApp()%>/images/windowClose.png" title="Chiudi" style="cursor:pointer;" onclick="chiudiDocPlichi();"></img>
			    	&nbsp;
			    </td>
			</tr>
			</table>
		</td>
     </tr>

	<tr><td>&nbsp;</td></tr>

	  
	<tr>
	  <td align="center">
	  
		<div style="overflow:auto;height:220px;width:98%;background-color: white;border:0;margin:0;">
			<form name="frmPlichi" method="post" action="call.wfem" style="margin:0;">
			<input type="hidden" name="wfemCmd" value="">
			<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
			<input type="hidden" name="numeroPlichi" value="<%=model.getPlichiNonCompletiInSpedizione().size()%>">
			
			<table width="98%" cellspacing="0" cellpadding="0">
			<tr><td>&nbsp;</td></tr>
	
			<%
		   		cols = "cols='#idDac,barcode,numeroContratto,descrProdotto,descrOperazione,agente_codAgente,cliente_nominativo' ";
		   		dim = "colswidths='12%,17%,16%,19%,15%,*' ";
				
		   		for (int i=0; i<model.getPlichiNonCompletiInSpedizione().size(); i++){
					PlicoModel plico = (PlicoModel)model.getPlichiNonCompletiInSpedizione().get(i);

					String allDocInfo = "";	//Stringa con struttura: "idDoc,barcode,idDac;idDoc,barcode,idDac;...."
					for (int k=0; k<plico.getDocumenti().size(); k++){
						DocumentoModel doc = (DocumentoModel)plico.getDocumenti().get(k);
						
						if (allDocInfo!="")
							allDocInfo = allDocInfo + ";";

						allDocInfo += doc.getIdDocumento().toString()+","+doc.getBarcode().toString()+","+doc.getIdDac().toString();
					}

					template.setPrefix("plichiNonCompletiInSpedizione"+i);						
			%>
			    <!-- Plico -->
				<tr id="tr<%=plico.getIdPlico()%>">
					<td>
						<table width="100%" cellpadding="0" cellspacing="0">
						<!-- titolo plico -->
						<tr>
							<td class="text" style="border-bottom: solid 1px darkgray;">&nbsp;<i><%=plico.getIdPlico()%></i></td>
							<td align="right" style="border-bottom: solid 1px darkgray;">
							<!-- TOLTA LA POSSIBILITA' DI AGGIUNGERE IN AUTOMATICO I DOCUMENTI MANCANTI DI UN PLICO
								<img src="<%//template.getWebApp()%>/images/addPlico.png" title="Aggiungi alla DAC" style='cursor:hand;' onclick="aggiungiPlico('<%//plico.getIdPlico()%>');">
								&nbsp;&nbsp;&nbsp;
							 -->
								<span onclick="rimuoviPlico('<%=plico.getIdPlico()%>');" class="text" style='cursor:hand;'>[Rimuovi]</span>&nbsp;
								<span onclick="spinzaDaPlico('<%=plico.getIdPlico()%>');" class="text" style='cursor:hand;'>[Spinza]</span>
							</td>
							<td width="5%" style="border-bottom: solid 1px darkgray;">&nbsp;</td>
						</tr>
						<tr><td style="height: 2px;"></td></tr>
						<!-- documenti plico -->						
						<tr>
							<td colspan=3 align="center" height="78px" valign="top">
							    <%=template.grid("documenti",	cols+dim+
							    								  		"helper='yes' "+
							    								  		"width='97%' height='54px' "+
									    								"sortable='false' "+
							    										"selection='none' "+
								    									"showbottomarea='false' showtoparea='false' "+
								    									"onnewrow='onNewDocPlico(this);' "+
																		"counterwidth='0'")%>		
							</td>
							<input type="hidden" id="<%=plico.getIdPlico()%>" value="<%=allDocInfo%>">
						</tr>
						</table>
					</td>
				</tr>

			<%
				}
			%>
	
			</table>	    
			</form>
		</div>
		
	  </td>
	</tr>   
	
	<tr>
		<td align="center">

		    <!-- Footer -->
			<table width="98%" cellspacing="0" cellpadding="0" style="background-color: white;">
		    <tr><td colspan=2>&nbsp;</td></tr>
		    <tr>
		    	<td width="2%"></td>
				<td class="text" align="left">Attenzione: i documenti evidenziati in rosso non sono presenti nella DAC</td>
			</tr>
		    <tr><td colspan=2>&nbsp;</td></tr>
		    </table>
		</td>
	</tr>

	<tr><td>&nbsp;</td></tr>
   </table>

	<script>
		docPlichiVisibile=true;
	</script>
<% } %>	

</div>
<%	
	model.getPlichiNonCompletiInSpedizione().clear();

	template.restorePrefix();
	template.setLabelCodePrefix(prefixBeforeIncludeDocumentiPlichiMancanti); 
%>