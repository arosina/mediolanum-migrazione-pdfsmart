var clientiSelezionati = new Array();
function selCli(row){
	clientiSelezionati = row.selectedRows;
	abilitaPulsantiera(row);
	return true;
}

function doRicercaAction(){
	wfemHiddenSubmit(document.ricercaClienti,'body');
	return true;
}

function abilitaPulsantiera(clienteHtml){
	enableAction('nuovoQuestionarioAction',false);
	enableAction('nuovoPatrimonioAction',false);
	if(clientiSelezionati.length != 1)
		return;

	enableAction('nuovoQuestionarioAction',true);
	enableAction('nuovoPatrimonioAction',true);
}

function doNuovoQuestionarioAction(){
	if(clientiSelezionati.length == 0){
		alert("Selezionare un cliente");
		return false;
	}
	
	var clienteSelezionato = clientiSelezionati[0];	

	if(jsUrlNuovoPcp !== ''){
		var codcli = clienteSelezionato.codMediolanum;
		if(codcli === "")
			codcli = clienteSelezionato.codPotenziale;
		var encodedParams = window.btoa('{"ndg":"'+codcli+'"}');
		if(jsUrlNuovoPcp.indexOf("http:") == 0 || jsUrlNuovoPcp.indexOf("https:") == 0)
			window.open(jsUrlNuovoPcp+encodedParams);
		else
			window.open(getSlash()+jsUrlNuovoPcp+encodedParams);
		return false;
	}
	
	if(clienteSelezionato.sesso == 'S' || clienteSelezionato.sesso == 's'){
		alert("Il profilo dell'investitore può essere creato/modificato elettronicamente solo per persone fisiche e per ditte/liberi professionisti");
		return false;
	}

	startRequest();
	settaChiave(clienteSelezionato);
	dati.target = '_self';
	dati.action = 'call.wfem';
	dati.wfemCmd.value='prgm.ita.anagraficaclienti.questionari.business.NuovoQuestionario.execute';	
	dati.submit();
	return true;
}

function doNuovoPatrimonioAction(){
	if(clientiSelezionati.length == 0){
		alert("Selezionare un cliente");
		return false;
	}
	
	var clienteSelezionato = clientiSelezionati[0];
	
	if(clienteSelezionato.sesso == 'S' || clienteSelezionato.sesso == 's'){
		alert("Il profilo dell'investitore può essere creato/modificato elettronicamente solo per persone fisiche e per ditte/liberi professionisti");
		return false;
	}
	
	startRequest();
	settaChiave(clienteSelezionato);
	dati.target = '_self';
	dati.action = 'call.wfem';
	dati.wfemCmd.value='prgm.ita.anagraficaclienti.questionari.business.NuovoPatrimonio.execute';	
	dati.submit();
	return true;
}

function pulisci(){
	document.ricercaClienti.params_cognome.value = "";
	document.ricercaClienti.params_nome.value = "";
	document.ricercaClienti.params_codMediolanum.value = "";
}

function settaChiave(clienteSelezionato){
	dati.codMediolanum.value = clienteSelezionato.codMediolanum;
	dati.codPotenziale.value = clienteSelezionato.codPotenziale;
}

function doEnterAction(){
	doAction("ricercaAction");
	return;	
}

//Per fare in modo che la proxy non intervenga sulle stringhe che iniziano con "/"
function getSlash(){
	return "x/".substring(1);
}
