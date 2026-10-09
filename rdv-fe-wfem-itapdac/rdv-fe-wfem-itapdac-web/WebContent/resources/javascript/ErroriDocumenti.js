function newCellErrDoc(cell) {
	if(cell.propertyName == "dataIns" ||
	   cell.propertyName == "daAutorizzare" ||
	   cell.propertyName == "daAutorizzareAction" ||
	   cell.propertyName == "inSpedizione" ||
	   cell.propertyName == "dataAutorizzazione") {
		cell.align="center";
	}
	
	if(cell.propertyName == "inSpedizione"){
		if(cell.row.inSpedizione == 'true')
			cell.innerHTML = "Spedizione";
		else
			cell.innerHTML = "Ricezione";
	}
		
	if(cell.propertyName == "daAutorizzareAction"){
		if(cell.row.daAutorizzare != 'true'){
			cell.innerHTML = 'No';
		}else{
			if(cell.row.dataAutorizzazione != ''){
				cell.innerHTML = 'Autorizzato'; 
			}else{
				cell.innerHTML = 'Si&nbsp;&nbsp;<span style="text-decoration:underline;cursor:pointer;" '+
								               'onclick="autorizzaErroreDocumento(\''+cell.row.progressivo+'\');">Autorizza</span>'; 
			}
		}
		
	}
}

function newRowErrDoc(row) {
	if (row.daAutorizzare == "false" || (row.daAutorizzare == "true" && row.dataAutorizzazione != "")) {
		row.style.color = "#1A458F";
		row.style.backgroundColor = "yellowgreen";
	} else {
		row.style.color = "white";
		row.style.backgroundColor = "#FF3333";
	}
}

function autorizzaErroreDocumento(progressivo){
	if(!confirm("Confermi l'autorizzazione ?"))
		return;
	startRequest();
	document.erroriForm.wfemCmd.value = 'prgm.ita.p.dac.business.AutorizzaErroreDocumento.execute'; 
	document.erroriForm.erroreDocumento_progressivo.value = progressivo; 
	wfemHiddenSubmit(document.erroriForm,'tdErroriDocumento');
}