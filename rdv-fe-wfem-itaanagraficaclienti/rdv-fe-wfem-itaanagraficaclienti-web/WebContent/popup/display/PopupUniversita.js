function doEseguiRicercaAction(){
	wfemHiddenSubmit(document.ricercaUniversita,'body');
	return true;
}

function selezionaUniversita(un){
	if(isRequestPending())
		return;
		
	var ret = new Object();
	ret.codUniversita 	= un.codUniversita;
	ret.facolta 		= un.facolta;
	ret.ateneo 			= un.ateneo;
	ret.provincia 		= un.provincia;

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
	document.ricercaUniversita.facolta.value = "";
	document.ricercaUniversita.ateneo.value = "";
	document.ricercaUniversita.provincia.value = "";
}

