var clientiSelezionati = new Array();

function selCli(row){
	clientiSelezionati = row.selectedRows;
	abilitaPulsantiera(row);
}

function doRicercaAction(){
	wfemHiddenSubmit(document.ricercaClienti,'body');
	return true;
}

function abilitaPulsantiera(clienteHtml){
	enableAction('apriAction',false);
	enableAction('cancellaAction',false);
	if(clientiSelezionati.length != 1)
		return;

	enableAction('apriAction',true);
	
	if(clienteHtml.isCancellabile == 'true')
		enableAction('cancellaAction',true);
}

function doApriAction(){
	if(clientiSelezionati.length == 0){
		alert("Selezionare un cliente");
		return false;
	}
	
	var clienteSelezionato = clientiSelezionati[0];	
	settaChiave(clienteSelezionato);

	dati.callingAppl.value = "";		

	dati.target = '_self';
	dati.action = 'call.wfem';
	dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.ApriAnagraficaCliente.execute';	
	dati.submit();
	return true;
}


function doCancellaAction(){
	if(clientiSelezionati.length == 0){
		alert("Selezionare un cliente");
		return false;
	}
	
	var clienteSelezionato = clientiSelezionati[0];	
	
	var msg = "Sei sicuro di voler cancellare il cliente "+clienteSelezionato.cognome+" ?";
	if(!window.confirm(msg))
		return false;

	settaChiave(clienteSelezionato);

	dati.callingAppl.value = "";		

	dati.target = '_self';
	dati.action = 'call.wfem';
	dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.CancellaAnagraficaCliente.execute';	
	dati.submit();
	return true;
}

function pulisci(){
	document.ricercaClienti.params_cognome.value = "";
	document.ricercaClienti.params_codMediolanum.value = "";
	document.ricercaClienti.params_statoElementi.value = "";
}

function settaChiave(clienteSelezionato){
	dati.codMediolanum.value = clienteSelezionato.codMediolanum;
	dati.codPotenziale.value = clienteSelezionato.codPotenziale;
	dati.codAgente.value = clienteSelezionato.codAgente;
	dati.partitaIva.value = clienteSelezionato.partitaIva;
	dati.codFiscale.value = clienteSelezionato.codFiscale;		
}

function doEnterAction(){
	doAction("ricercaAction");
	return;	
}
