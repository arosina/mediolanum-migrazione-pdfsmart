function doEsegui(){
	
		if(document.dati.dataDal.value==''||document.dati.statoPrit.value=='' || document.dati.tipologiaPrit.value==''){
			alert("I campi: \n\n - Inviati in sede dal \n\n - Stato Prit \n\n - Tipologia Prit \n\n sono obbligatori!");
			return false;
		}
	
		if(document.dati.tipoEstrazione.value == 'sintesi') {
			var url='call.wfem?wfemCmd=prgm.ita.p.dac.estrazioni.print.EstrazioniPritInviatiInSedeSintesi.execute'+
			'&BrowserInstance='+document.dati.BrowserInstance.value+
			'&dataDal='+document.dati.dataDal.value+
			'&dataAl='+document.dati.dataAl.value+
			'&statoPrit='+document.dati.statoPrit.value+
			'&tipologiaPrit='+document.dati.tipologiaPrit.value;
			includePdfObject ("pdf", url);
		} 
}

function esportaXls(){
	
	if(document.dati.dataDal.value==''||document.dati.statoPrit.value=='' || document.dati.tipologiaPrit.value==''){
		alert("I campi: \n\n - Inviati in sede dal \n\n - Stato Prit \n\n - Tipologia Prit \n\n sono obbligatori!");
		return false;
	}
	
	if(document.dati.tipoEstrazione.value == 'sintesi') {
		var url='call.wfem?wfemCmd=prgm.ita.p.dac.estrazioni.business.EstrazioniPritInviatiInSedeEsportaSintesi.executeOnPopup'+
		'&BrowserInstance='+document.dati.BrowserInstance.value+
		'&dataDal='+document.dati.dataDal.value+
		'&dataAl='+document.dati.dataAl.value+
		'&statoPrit='+document.dati.statoPrit.value+
		'&tipologiaPrit='+document.dati.tipologiaPrit.value;
		document.getElementById("ExportPort").src = url;
	} else if(document.dati.tipoEstrazione.value == 'dettaglio') {
		var url='call.wfem?wfemCmd=prgm.ita.p.dac.estrazioni.business.EstrazioniPritInviatiInSedeEsportaDettaglio.executeOnPopup'+
		'&BrowserInstance='+document.dati.BrowserInstance.value+
		'&dataDal='+document.dati.dataDal.value+
		'&dataAl='+document.dati.dataAl.value+
		'&statoPrit='+document.dati.statoPrit.value+
		'&tipologiaPrit='+document.dati.tipologiaPrit.value;
		document.getElementById("ExportPort").src = url;
	}
}

function pulisciCampiRicerca() {	
	document.dati.wfemCmd.value = "prgm.ita.p.dac.estrazioni.business.EstrazioniPritInviatiInSedeResetForm.execute";
	document.dati.submit();
}

function abilita() {
	if(document.dati.tipoEstrazione.value == 'sintesi') {
		enableAction('esegui',false);
		
	} else if(document.dati.tipoEstrazione.value == 'dettaglio') {
		enableAction('esegui',true);
	}
	return true;
}

