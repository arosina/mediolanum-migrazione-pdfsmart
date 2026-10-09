function doStampaAction(){
	if(document.getElementById("codicePromo").value == '')
		alert("Inserire il Codice Promozionale");
	else
		wfemHiddenSubmit(document.ricercaCodicePromozionaleForm,'body');
}

function doEnterAction(){
	doAction("stampaAction");
	return;	
}

function pulisciCodicePromozionale(){
	document.all.ricercaCodicePromozionaleForm.codicePromo.value = "";
}
