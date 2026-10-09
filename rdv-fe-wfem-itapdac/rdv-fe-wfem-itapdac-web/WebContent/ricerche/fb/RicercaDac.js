function doEnterAction(){
	document.getElementById("ricercaAction").focus();
	return doRicercaAction();
}

function pulisciParametri(){
	document.ricerca.parametri_stato.value = '';
	document.ricerca.parametri_esito.value = '';
	clearDateField('parametri_dataInizio');
	clearDateField('parametri_dataFine');
	document.ricerca.parametri_cognomeCliente.value = '';
	document.ricerca.parametri_nomeCliente.value = '';
	document.ricerca.parametri_numeroContratto.value = '';
}

function doRicercaAction(){
	startRequest();
	document.ricerca.doSearch.value = 'true';
	wfemHiddenSubmit(document.ricerca,'body');
	return false;
}

function onNewCell(cell){
	if(cell.propertyName == 'idDac' ||
	   cell.propertyName == 'dataOraEmissione' ||
	   cell.propertyName == 'dataOraSpunta')
	   cell.align = 'center';
	   
	if(cell.propertyName == 'descrStato'){
		if(cell.row.flagReplica == 'D')
			cell.innerHTML = '<span class="text" style="color:red;">Da replicare</span>'
	}
	
}

var curDac=null;
function selectDac(dac){
	enableAction("apriAction",true);
	enableAction("stampaAction",true);
	curDac = dac;
}

function doApriAction(){
	document.dati.idDac.value = curDac.idDac;
	document.dati.submit();
	return true;
}

function doStampaAction(){
	stampaDac(curDac.idDac,false);
}

