var varSelezionate = new Array();

function selVar(row){
	varSelezionate = row.selectedRows;
	abilitaPulsantiera(row);
}

function abilitaPulsantiera(varHtml){
	enableAction('visualizzaVariazioneSelezionataAction',false);
	enableAction('stampaVariazioneSelezionataAction',false);
	enableAction('cancellaVariazioneSelezionataAction',false);
	if(varSelezionate.length != 1)
		return;

	enableAction('visualizzaVariazioneSelezionataAction',true);
	enableAction('stampaVariazioneSelezionataAction',true);
	enableAction('cancellaVariazioneSelezionataAction',true);
}

function doVisualizzaVariazioneSelezionataAction(){
	if(varSelezionate.length == 0){
		alert("Selezionare una variazione");
		return false;
	}

	var variazioneSelezionata = varSelezionate[0];	
	
	var cmdParams = "codAgente="+variazioneSelezionata.codAgente+
	    			"&codPotenziale="+variazioneSelezionata.codPotenziale+
	    			"&codMediolanum="+variazioneSelezionata.codMediolanum+
	    			"&codFiscale="+variazioneSelezionata.codFiscale+
	    			"&partitaIva="+variazioneSelezionata.partitaIva+
	    			"&progressivo="+variazioneSelezionata.progressivo+
	    			"&popupMode=true"+
	    			"&readRequest=false";
	openModalPopup("prgm.ita.anagraficaclienti.business.VisualizzaVariazioneAnagraficaCliente",
	  				 cmdParams,null,null,"Variazione",680,940);
	return false;
}

function doStampaVariazioneSelezionataAction(){
	if(varSelezionate[0].tipoCarta == 'D'){
		alert("Variazione in firma digitale. La stampa non e' disponibile");
		return false;
	}
	doStampaVariazioneAction(varSelezionate[0].progressivo);
	return false;
}

function doCancellaVariazioneSelezionataAction(){

	if(varSelezionate.length == 0){
		alert("Selezionare una variazione");
		return false;
	}
	
	var variazioneSelezionata = varSelezionate[0];	
	
	var msg = "Sei sicuro di voler cancellare la variazione di\n"+variazioneSelezionata.cognome+" "+variazioneSelezionata.nome+"\ndel "+variazioneSelezionata.dataVariazione+" ?";
	if(!window.confirm(msg))
		return false;

	startRequest();
	cancellaVariazioneForm.codPotenziale.value = variazioneSelezionata.codPotenziale;
	cancellaVariazioneForm.codAgente.value = variazioneSelezionata.codAgente;
	cancellaVariazioneForm.partitaIva.value = variazioneSelezionata.partitaIva;
	cancellaVariazioneForm.codFiscale.value = variazioneSelezionata.codFiscale;		
	cancellaVariazioneForm.progressivo.value = variazioneSelezionata.progressivo;		

	cancellaVariazioneForm.wfemCmd.value='prgm.ita.anagraficaclienti.business.CancellaVariazioneCliente.execute';	
	cancellaVariazioneForm.submit();
	return true;
}
