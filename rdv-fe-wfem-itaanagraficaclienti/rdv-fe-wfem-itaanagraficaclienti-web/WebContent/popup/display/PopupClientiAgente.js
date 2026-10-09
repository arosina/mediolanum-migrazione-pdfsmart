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
	ret.codInforete                     = cliHtml.codInforete;
	ret.codMediolanum 					= cliHtml.codMediolanum;
	ret.cognome 						= cliHtml.cognome;
	ret.nome 							= cliHtml.nome;
	ret.dataNascita 					= cliHtml.dataNascita;
	ret.codPotenziale 					= cliHtml.codPotenziale;
	ret.datiApplicativi_nomeTabella 	= cliHtml.datiApplicativi_nomeTabella;
	ret.isCancellabile 					= cliHtml.isCancellabile;
	ret.sesso 							= cliHtml.sesso;
	ret.codCluster 						= cliHtml.codCluster;
	ret.numVariazioni 					= cliHtml.numVariazioni;
	ret.codAgente 						= cliHtml.codAgente;
	ret.stato 							= cliHtml.stato;
	ret.statoProposta 					= cliHtml.statoProposta;
	ret.statoConfermato 				= cliHtml.statoConfermato;
	ret.codFiscale 						= cliHtml.codFiscale;
	ret.partitaIva 						= cliHtml.partitaIva;
	ret.isProspect 						= cliHtml.isProspect;
	ret.isBozza 						= cliHtml.isBozza;
	ret.isPotenziale 					= cliHtml.isPotenziale;
	ret.isAcquisito 					= cliHtml.isAcquisito;
	ret.isEffettivoPersonale 			= cliHtml.isEffettivoPersonale;
	ret.isEffettivoRiassegnato 			= cliHtml.isEffettivoRiassegnato;
	ret.isCointestatarioNonAssegnato 	= cliHtml.isCointestatarioNonAssegnato;
	ret.isAssegnatoAdAltroAgente 		= cliHtml.isAssegnatoAdAltroAgente;
	ret.isTopBusiness 					= cliHtml.isTopBusiness;
	ret.descrCluster 					= cliHtml.descrCluster;
	ret.secondaIntestazione 			= cliHtml.secondaIntestazione;
	ret.naturaGiuridica		 			= cliHtml.naturaGiuridica;
	ret.isDitta				 			= cliHtml.isDitta;

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
	try{
		document.ricercaClienti.params_nome.value = "";
	}catch(e){}
}

function setCodiceAgente(agenteDiretto){
	document.ricercaClienti.params_codAgente.value = agenteDiretto.value;
}
