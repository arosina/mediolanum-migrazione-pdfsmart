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

function doStampaAction(){
	if(clientiSelezionati.length == 0){
		alert("Selezionare un cliente");
		return false;
	}
	
	var clienteSelezionato = clientiSelezionati[0];	

	var numVar = 0;
	try{
		numVar = parseInt(clienteSelezionato.numVariazioni);
	}catch(e){
		numVar = 0;
	}
	if(numVar > 0){
		  var cmdParams = "readRequest=false";
		  cmdParams += "&codAgente="+codAgente;
		  cmdParams += "&codPotenziale="+codPotenziale;
		  openModalPopup("prgm.ita.anagraficaclienti.popup.business.LoadPopupStampaVariazioni",
		  				 cmdParams,null,null,"Stampa variazioni",600,880);
		  return false;
	}
	
	var url = "call.wfem?wfemCmd=";
	if(clienteSelezionato.codMediolanum == '' && 
	   clienteSelezionato.statoConfermato != Costanti_VALORE_FLAG_STATO_CONFERMATO &&
	   clienteSelezionato.statoProposta > StatiPropostaAnagrafica_BOZZA_int){
		url += "prgm.ita.anagraficaclienti.business.StampaCensimento.executeOnPopup";
	}else{
		url += "prgm.ita.anagraficaclienti.business.StampaCensimentoFacsimile.executeOnPopup";
	}
	
	url += "&codAgente="+clienteSelezionato.codAgente;
	url += "&codMediolanum="+clienteSelezionato.codMediolanum;
	url += "&codPotenziale="+clienteSelezionato.codPotenziale;
	url += "&partitaIva="+clienteSelezionato.partitaIva;
	url += "&codFiscale="+clienteSelezionato.codFiscale;
	
	var h = screen.height-100;
	var w = screen.width-60;
	openNewWindow(url,clienteSelezionato.cognome+" "+clienteSelezionato.nome,'titlebar=yes,scrollbars=yes,resizable=yes,top=10,left=10,width='+w+',height='+h);
	return false;	
}

function doCancellaAction(){
	if(clientiSelezionati.length == 0){
		alert("Selezionare un cliente");
		return false;
	}
	
	var clienteSelezionato = clientiSelezionati[0];	
	
	var msg = "Sei sicuro di voler cancellare il cliente "+clienteSelezionato.cognome+" "+clienteSelezionato.nome+" ?";
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
	document.ricercaClienti.params_nome.value = "";
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

function doApriPEMAction(){
	if(clientiSelezionati.length == 0){
		alert("Selezionare un cliente");
		return false;
	}
	
	var clienteSelezionato = clientiSelezionati[0];	
	
	dati.codPotenziale.value = clienteSelezionato.codPotenziale;
	dati.codAgente.value = clienteSelezionato.codAgente;
	dati.partitaIva.value = clienteSelezionato.partitaIva;
	dati.codFiscale.value = clienteSelezionato.codFiscale;		

	dati.callingAppl.value = "MHD";		

	dati.target = '_self';
	dati.action = 'call.wfem';
	dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.ApriAnagraficaCliente.execute';	
	dati.submit();
	return true;
}

function doEnterAction(){
	doAction("ricercaAction");
	return;	
}
