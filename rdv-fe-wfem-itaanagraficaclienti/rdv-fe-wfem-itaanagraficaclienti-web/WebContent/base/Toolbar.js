function doSalvaBozzaAction(){
	if(jsAvvisoCogestione !== ""){
		if(!window.confirm(jsAvvisoCogestione+"\n\nConfermi l'operazione ?"))
			return false;
	}
	document.dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.SalvaBozzaAnagraficaCliente.execute';
	submitForm();
	return true;
}

function doConfermaAction(){
	var msg = "L'anagrafica verra' controllata e, se compilata correttamente, non sara' piu' modificabile.\n";
	if(jsAvvisoCogestione !== "")
		msg += "\n"+jsAvvisoCogestione+"\n\n";
	msg += "Confermi l'operazione ?";
	if(!window.confirm(msg))
		return false;
		
	document.dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.ConfermaAnagraficaCliente.execute';
	submitForm();
	return true;
}

function doInviaInSedeAction(){
	var msg = "Se compilata correttamente la proposta anagrafica verra' inviata in sede e non sara' piu' modificabile.\n";
	if(jsAvvisoCogestione !== "")
		msg += "\n"+jsAvvisoCogestione+"\n\n";
	msg += "Confermi l'invio in sede ?";
	if(!window.confirm(msg))
		return false;
		
	document.dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.InviaInSedeAnagraficaCliente.execute';
	submitForm();
	return true;
}

function doInviaInSedeVariazioneAction(){
	var msg = "Se compilata correttamente la variazione anagrafica verra' inviata in sede e non sara' piu' modificabile.\n";
	msg += "Confermi l'invio in sede ?";
	if(!window.confirm(msg))
		return false;
		
	document.dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.InviaInSedeAnagraficaCliente.execute';
	submitForm();
	return true;
}

function inviaInSedeDopoFlussoFatca(moduloFatca){
	startRequest();
	document.dati.moduloFatca.value = moduloFatca;
	document.dati.isFlussoFatcaTerminato.value = 'true';
	document.dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.InviaInSedeAnagraficaCliente.execute';
	submitForm();
	return true;
}

function doStampaCensimentoAction(){
	if(document.datiread.statoProposta.value == "1"){
		document.dati.wfemCmd.value='prgm.ita.anagraficaclienti.business.StampaBozzaPrepare.execute';
		submitForm();
		return true;
	}
	var url = "call.wfem?wfemCmd=prgm.ita.anagraficaclienti.business.StampaCensimento.executeOnPopup";
	url += "&codAgente="+document.datiread.codAgente.value;
	url += "&codPotenziale="+document.datiread.codPotenziale.value;
	
	openStampa(url);
	
	return false;
}

function doStampaCensimentoFacsimileAction(){

	var url = "call.wfem?wfemCmd=prgm.ita.anagraficaclienti.business.StampaCensimentoFacsimile.executeOnPopup";
	url += "&codAgente="+document.datiread.codAgente.value;
	url += "&codPotenziale="+document.datiread.codPotenziale.value;
	if(document.datiread.codMediolanum.value != "")
		url += "&codMediolanum="+document.datiread.codMediolanum.value;
	openStampa(url);
	
	return false;
}

function doStampaVariazioneAction(progressivo){
	
	if(typeof(progressivo) == 'undefined') // Richiamato dalla toolbar
		progressivo = document.datiread.progressivo.value;
		
	var url = "call.wfem?wfemCmd=prgm.ita.anagraficaclienti.business.StampaVariazione.executeOnPopup";
	url += "&codAgente="+document.datiread.codAgente.value;
	url += "&progressivo="+progressivo;
	url += "&codPotenziale="+document.datiread.codPotenziale.value;
	
	openStampa(url);
	
	return false;
}

function doStampaVariazioniAction(){
  var cmdParams = "readRequest=false";
  cmdParams += "&codAgente="+document.datiread.codAgente.value;
  cmdParams += "&codPotenziale="+document.datiread.codPotenziale.value;
  openModalPopup("prgm.ita.anagraficaclienti.popup.business.LoadPopupStampaVariazioni",
  				 cmdParams,null,null,"Stampa variazioni",600,880);
}

function openStampa(url){
	var title = "";
	if(document.datiread.isDitta.value == 'true')
		title = document.dati.secondaIntestazione.value+" - "+document.dati.cognome.value+" "+document.dati.nome.value;
	else
		title = document.dati.cognome.value+" "+document.dati.nome.value;
	openPdfObject(url,title);
}