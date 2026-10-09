function doEsegui(){	
	if(document.dati.dataInizio.value==''||document.dati.dataFine.value==''||document.dati.ufficio.value==''){
		alert("I campi Data Inizio, DataFine e Ufficio sono obbligatori!");
		return false;
	}
	
	var url='call.wfem?wfemCmd=prgm.ita.p.dac.estrazioni.business.EstrazioniDocumentiRicevutiDAC.executeOnPopup'+
	'&BrowserInstance='+document.dati.BrowserInstance.value+
	'&ufficio='+document.dati.ufficio.value+
	'&dataInizio='+document.dati.dataInizio.value+
	'&dataFine='+document.dati.dataFine.value+
	'&codOperazione='+document.dati.codOperazione.value+
	'&codProdotto='+document.dati.codProdotto.value;
	document.getElementById("ExportPort").src = url;
}

function aggiornaOperazioni(){
	startRequest();	
	document.dati.wfemCmd.value= "prgm.ita.p.dac.estrazioni.display.EstrazioniDocumentiRicevutiDAC.execute";
	document.dati.submit();
	return true;
}