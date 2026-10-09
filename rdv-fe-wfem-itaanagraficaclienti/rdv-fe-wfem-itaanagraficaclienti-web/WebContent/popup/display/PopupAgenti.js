function focusOn(){
	document.ricercaAgenti.params_codAgente.activate();
	document.ricercaAgenti.params_codAgente.focus();
}

function doEseguiRicercaAction(){
	wfemHiddenSubmit(document.ricercaAgenti,'body');
	return true;
}

function selezionaAgente(agenteHtml){
	if(isRequestPending())
		return;

	var ret = new Object();		
	ret.codAgente 				= agenteHtml.codAgente;
	ret.cognomeAgente 			= agenteHtml.cognomeAgente;
	ret.nomeAgente 				= agenteHtml.nomeAgente;
	ret.areaAgente 				= agenteHtml.areaAgente;
	ret.serverReplica 			= agenteHtml.serverReplica;
	ret.codAgenzia 				= agenteHtml.codAgenzia;
	ret.descrAgenzia 			= agenteHtml.descrAgenzia;
	ret.codProvincia 			= agenteHtml.codProvincia;
	ret.codiceContrattoAgente 	= agenteHtml.codiceContrattoAgente;
	ret.cicloVitaAgente 		= agenteHtml.cicloVitaAgente;
	//returnValue = ret;

	if(parent.__isWlt){
		closeModalPopup(ret);	
	}else{
		returnValue = ret;
		window.close();
	}
	//window.close();
}

/*document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    document.all("eseguiRicercaAction").focus();
	    document.all("eseguiRicercaAction").click();
	}
}		*/

function doEnterAction(){
	doAction("eseguiRicercaAction");
	return;	
}

function pulisci(){
	document.ricercaAgenti.params_codAgente.value = "";
	document.ricercaAgenti.params_cognomeAgente.value = "";
}

function newCell(cell){
	if(cell.propertyName == "codAgente")
		cell.align="center";
}
