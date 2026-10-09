function doRicercaAction(){
	if(document.getElementById("help"))
		document.getElementById("help").style.display = "none";
	
	
	wfemHiddenSubmit(document.ricercaClienti,'body');
	return true;
}

var idxClienteSelezionato = -1;
function resetIdxClienteSelezionato(){
	idxClienteSelezionato = -1;
}
function selezionaCliente(cliente){
	if(cliente.absIndex == idxClienteSelezionato)
		return;
	if(document.getElementById("help"))
		document.getElementById("help").style.display = "none";
	idxClienteSelezionato = cliente.absIndex;
		
	settaChiave(cliente);
	includePdfObject("moduloPrintObject",document.moduloForm);
}

function doEnterAction(){
	doAction("ricercaAction");
	return;	
}

function pulisci(){
	document.ricercaClienti.popupClientiModel_params_codMediolanum.value = "";
	document.ricercaClienti.popupClientiModel_params_cognome.value = "";
	document.ricercaClienti.popupClientiModel_params_nome.value = "";
}

function settaChiave(clienteSelezionato){
	moduloForm.codMediolanum.value = clienteSelezionato.codMediolanum;
	moduloForm.codPotenziale.value = clienteSelezionato.codPotenziale;
	moduloForm.codAgente.value = clienteSelezionato.codAgente;
	moduloForm.partitaIva.value = clienteSelezionato.partitaIva;
	moduloForm.codFiscale.value = clienteSelezionato.codFiscale;		
}
function newCell(cell){
}
