function doEsegui(){	
	if(document.dati.dataInizio.value==''||document.dati.dataFine.value==''){
		alert("I campi Data Inizio e DataFine sono obbligatori!");
		return false;
	}
	
	var url='call.wfem?wfemCmd=prgm.ita.p.dac.estrazioni.business.EstrazioneDocumentiLavorati.executeOnPopup'+
	'&BrowserInstance='+document.dati.BrowserInstance.value+
	'&dataInizio='+document.dati.dataInizio.value+
	'&dataFine='+document.dati.dataFine.value+
	'&operazione='+document.dati.codOperazione.value+
	'&prodotto='+document.dati.codProdotto.value;
	document.getElementById("ExportPort").src = url;
	
}

function aggiornaOperazioni(){
	startRequest();	
	document.dati.wfemCmd.value= "prgm.ita.p.dac.estrazioni.display.EstrazioniDocumentiLavorati.execute";
	document.dati.submit();
	return true;
}