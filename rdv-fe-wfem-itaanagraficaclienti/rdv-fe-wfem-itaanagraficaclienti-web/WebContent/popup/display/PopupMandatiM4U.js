function doChiudiAction(){
	if(parent.__isWlt){
		closeModalPopup(null);	
	}else{
		returnValue = null;
		window.close();
	}
}

function doEseguiRicercaAction(){
	wfemHiddenSubmit(document.ricercaClienti,'body');
	return true;
}

function selezionaCliente(cliHtml){
	if(isRequestPending())
		return;
		
	var ret = new Object();
	ret.codInforete						= cliHtml.codInforete;
	ret.codPotenziale 					= cliHtml.codPotenziale;
	ret.codMediolanum 					= cliHtml.codMediolanum;
	ret.cognome 						= cliHtml.cognome;
	ret.nome 							= cliHtml.nome;
	ret.dataNascita 					= cliHtml.dataNascita;

	if(parent.__isWlt){
		closeModalPopup(ret);	
	}else{
		returnValue = ret;
		window.close();
	}
}

function doEnterAction(){
	doAction("eseguiRicercaAction");
	return;	
}

function pulisci(){
	document.ricercaClienti.params_cognome.value = "";
	document.ricercaClienti.params_codMediolanum.value = "";
}

