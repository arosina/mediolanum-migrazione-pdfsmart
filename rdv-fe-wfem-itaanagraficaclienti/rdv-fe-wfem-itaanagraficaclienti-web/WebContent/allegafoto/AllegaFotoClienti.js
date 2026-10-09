function selCli(clienteSelezionato){
	document.dati.clienteSelezionato_codAgente.value = clienteSelezionato.codAgente;
	document.dati.clienteSelezionato_codFiscale.value = clienteSelezionato.codFiscale;		
	document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.allegafoto.DettaglioClientePerFoto.execute";
	startRequest();
	document.dati.submit();
}

function doRicercaAction(){
	document.ricercaClienti.target='_self';
	wfemHiddenSubmit(document.ricercaClienti,'body');
	return true;
}

function pulisci(){
	document.ricercaClienti.params_cognome.value = "";
	document.ricercaClienti.params_nome.value = "";
	document.ricercaClienti.params_codMediolanum.value = "";
}

function doEnterAction(){
	doAction("ricercaAction");
	return;	
}
