function doEseguiRicercaAction(){
	wfemHiddenSubmit(document.ricercaComuni,'body');
	return true;
}

function selezionaComune(comuneHtml){
	if(isRequestPending())
		return;
		
	var ret = new Object();
	ret.codNazione 	= jsCostanti_COD_NAZIONE_ITALIA;
	ret.cap 		= comuneHtml.cap;
	ret.comune 		= comuneHtml.comune;
	ret.provincia 	= comuneHtml.provincia;
	ret.codComune 	= comuneHtml.codComune;

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
	document.ricercaComuni.cap.value = "";
	document.ricercaComuni.comune.value = "";
	document.ricercaComuni.provincia.value = "";
}

function newCell(cell){
	if(cell.propertyName == "cap" || cell.propertyName == "provincia")
		cell.align="center";
}
