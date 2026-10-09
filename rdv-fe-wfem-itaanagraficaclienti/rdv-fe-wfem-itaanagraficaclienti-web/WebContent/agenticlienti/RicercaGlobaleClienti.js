var idxClienteSelezionato = -1;

function doEseguiRicerca(){
	idxClienteSelezionato=-1;
	if(document.getElementById("help"))
		document.getElementById("help").style.display = "none";
	wfemHiddenSubmit(document.ricercaClienti,'body');
	return true;
}

function selezionaCliente(cliHtml){
	if(cliHtml.absIndex == idxClienteSelezionato)
		return;
	if(isRequestPending())
		return;
	if(document.getElementById("help"))
		document.getElementById("help").style.display = "none";
	startRequest();
	idxClienteSelezionato = cliHtml.absIndex;
	var url = "call.wfem?wfemCmd=prgm.ita.anagraficaclienti.agenticlienti.ElencoAgentiCliente.execute";
	url += "&idxClienteSelezionato="+idxClienteSelezionato;
	url += "&BrowserInstance="+document.ricercaClienti.BrowserInstance.value;
	document.getElementById("elencoAgentiCliente").src = url;
}

function doEnterAction(){
	doAction("eseguiRicerca");
	return;	
}

function pulisci(){
	document.ricercaClienti.params_codMediolanum.value = "";
	document.ricercaClienti.params_cognome.value = "";
	document.ricercaClienti.params_nome.value = "";
}
