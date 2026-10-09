function doSelezioneTitolareDittaBackAction(){
	document.getElementById("closeThreadIF").src = "call.wfem?wfemCmd=closeThread&BrowserInstance="+document.dati.BrowserInstance.value;
	document.goback.BrowserInstance.value = '0';
	document.goback.wfemCmd.value = 'executeCurrentDisplay';
	document.goback.submit();
	return true;	
}

function doSelezioneTitolareDittaProseguiAction(){
	if(document.dati.scelta[0].checked)
		return doSelezionaTitolareAction();
	else if(document.dati.scelta[1].checked)
		return doPulisciTitlareAction();
	else
		alert("Seleziona una delle due opzioni");
}

function doPulisciTitlareAction(){
	document.getElementById("tabScelta").style.visibility='hidden';
	document.dati.wfemCmd.value = 'prgm.ita.anagraficaclienti.business.PulisciTitolare.execute';
	document.dati.submit();
	return true;
}

function doSelezionaTitolareAction(){
	if(isRequestPending())
		return;
	var cmdParams = "params_tipoElementi=personefisiche"+
	   			  "&params_tipoRicerca=primaricensiti"+
	   			  "&params_codAgente="+document.dati.agente_codAgente.value+
	   			  "&params_tipoInclusioneCogestiti="+jsTipoInclusioneCogestiti+
	   			  "&params_ruoloCogestione="+jsRuoloCogestione+
	   			  "&params_tipoOrdinamentoCogestiti="+jsTipoOrdinamentoCogestiti+
	   			  "&params_maxRows=50";
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",
				   cmdParams,null,showPopupClientiPrimariCensitiEnd,"Clienti",580,780);
	return false;
}

function showPopupClientiPrimariCensitiEnd(cli){
	if(cli == null)
		return;
	document.getElementById("tabScelta").style.visibility='hidden';
	startRequest();
	document.dati.codPotenzialeTitolare.value = cli.codPotenziale;
	document.dati.codMediolanumTitolare.value = cli.codMediolanum;
	document.dati.codFiscaleTitolare.value = cli.codFiscale;
	document.dati.wfemCmd.value = 'prgm.ita.anagraficaclienti.business.SelezionaTitolare.execute';
	document.dati.submit();
}

