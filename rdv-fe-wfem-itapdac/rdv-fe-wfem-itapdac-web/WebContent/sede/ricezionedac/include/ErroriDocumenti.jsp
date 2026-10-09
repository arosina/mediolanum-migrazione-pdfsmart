<!--	#########################			ERRORI DOCUMENTI			#########################	-->
<tr>
	<td width="100%" align="center" id="erroriDoc">
	<%	if (model.getErroriDocumento().size()>0) {
			String colsErrori = "cols='#progressivo,barcode,erroreDescr,dataIns,daAutorizzare,dataAutorizzazione' ";
			String dimErrori = "colswidths='15%,*,15%,15%,15%' "; 	%>
			
					<%=template.grid("erroriDocumento", colsErrori + dimErrori + 
															"width='90%' height='80' " +
															"title='Errori presenti nella DAC' " +
															"onnewcell='newCellErrDoc(this);' " +
															"onnewrow='newRowErrDoc(this);' " + 
															"selection='none'") %>
	<%	}%>
	</td>
</tr>
<tr><td height="5px"></td></tr>

<!--	#########################			END ERRORI DOCUMENTI		#########################	-->
