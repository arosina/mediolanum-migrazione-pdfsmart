<!--	#########################			ERRORI DOCUMENTI			#########################	-->
<%	if (model.getErroriDocumento().size()>0) {
		String colsErrori = "cols='#progressivo,barcode,erroreDescr,dataIns,daAutorizzare,dataAutorizzazione' ";
		String dimErrori = "colswidths='15%,*,15%,15%,15%' "; 	%>
		
		<tr>
			<td width="100%" align="center">
				<%=template.grid("erroriDocumento", colsErrori + dimErrori + 
														"width='90%' height='75px' " +
														"title='Errori riscontrati nella compilazione della DAC' " +
														"onnewcell='newCellErrDoc(this);' " +
														"onnewrow='newRowErrDoc(this);' " + 
														"selection='none'") %>
			</td>
		</tr>
		<tr><td height="5px"></td></tr>
<%	}%>
<!--	#########################			END ERRORI DOCUMENTI		#########################	-->
