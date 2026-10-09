function doEnterAction(){
	doAction("eseguiRicercaAction");
	return;	
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
	ret.codMediolanum		       	    = cliHtml.codMediolanum;
	ret.cognome                         = cliHtml.cognome;               
	ret.nome                            = cliHtml.nome;                     
	ret.dataNascita                     = cliHtml.dataNascita;
	ret.codAgente                       = cliHtml.codAgente;
	ret.agente_cognomeAgente            = cliHtml.agente_cognomeAgente;
	ret.codPotenziale                   = cliHtml.codPotenziale;
	ret.numVariazioni                   = cliHtml.numVariazioni;
	ret.datiApplicativi_nomeTabella     = cliHtml.datiApplicativi_nomeTabella;
	ret.isCancellabile                  = cliHtml.isCancellabile;
	ret.sesso                           = cliHtml.sesso;
	ret.codCluster                      = cliHtml.codCluster;
	ret.stato                           = cliHtml.stato;
	ret.statoProposta                   = cliHtml.statoProposta;
	ret.statoConfermato                 = cliHtml.statoConfermato;
	ret.codFiscale                      = cliHtml.codFiscale;
	ret.partitaIva                      = cliHtml.partitaIva;
	ret.isProspect                      = cliHtml.isProspect;
	ret.isBozza                         = cliHtml.isBozza;
	ret.isPotenziale                    = cliHtml.isPotenziale;
	ret.isAcquisito                     = cliHtml.isAcquisito;
	ret.isEffettivoPersonale            = cliHtml.isEffettivoPersonale;
	ret.isEffettivoRiassegnato          = cliHtml.isEffettivoRiassegnato;
	ret.isCointestatarioNonAssegnato    = cliHtml.isCointestatarioNonAssegnato;
	ret.isAssegnatoAdAltroAgente        = cliHtml.isAssegnatoAdAltroAgente;
	ret.isTopBusiness                   = cliHtml.isTopBusiness;
	ret.descrCluster 					= cliHtml.descrCluster;
	ret.secondaIntestazione 			= cliHtml.secondaIntestazione;
	ret.naturaGiuridica		 			= cliHtml.naturaGiuridica;
	ret.isDitta				 			= cliHtml.isDitta;
	
	ret.agente_codAgente                = cliHtml.agente_codAgente;
	ret.agente_nomeAgente               = cliHtml.agente_nomeAgente;
	ret.agente_areaAgente               = cliHtml.agente_areaAgente;
	ret.agente_serverReplica            = cliHtml.agente_serverReplica;
	ret.agente_codAgenzia               = cliHtml.agente_codAgenzia;
	ret.agente_descrAgenzia             = cliHtml.agente_descrAgenzia;
	ret.agente_codProvincia             = cliHtml.agente_codProvincia;
	ret.agente_codiceContrattoAgente    = cliHtml.agente_codiceContrattoAgente;
	ret.agente_cicloVitaAgente          = cliHtml.agente_cicloVitaAgente;

	// Dati agente		
	var agente = new Object();
	agente.codAgente 				= cliHtml.agente_codAgente;
	agente.cognomeAgente 			= cliHtml.agente_cognomeAgente;
	agente.nomeAgente 				= cliHtml.agente_nomeAgente;
	agente.areaAgente 				= cliHtml.agente_areaAgente;
	agente.serverReplica 			= cliHtml.agente_serverReplica;
	agente.codAgenzia 				= cliHtml.agente_codAgenzia;
	agente.descrAgenzia 			= cliHtml.agente_descrAgenzia;
	agente.codProvincia 			= cliHtml.agente_codProvincia;
	agente.codiceContrattoAgente 	= cliHtml.agente_codiceContrattoAgente;
	agente.cicloVitaAgente 			= cliHtml.agente_cicloVitaAgente;
	ret.agente = agente;
	
	if(parent.__isWlt){
		closeModalPopup(ret);	
	}else{
		returnValue = ret;
		window.close();
	}
}

document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    document.all("eseguiRicercaAction").focus();
	    document.all("eseguiRicercaAction").click();
	}
}		

function pulisci(){
	document.ricercaClienti.params_codAgente.value = "";
	document.ricercaClienti.params_nome.value = "";
	document.ricercaClienti.params_cognome.value = "";
	document.ricercaClienti.params_codMediolanum.value = "";
}

function newHeader(headerCell){
	if(headerCell.propertyName === 'cognome'){
		headerCell.innerHTML = 'Ragione sociale';
		headerCell.title = 'Ragione sociale';
	}
}
function newCell(cell){
	cell.style.backgroundColor = 'white';
	return;
	
	if(cell.propertyName == 'codMediolanum' ||
	   cell.propertyName == 'codAgente' ||
	   cell.propertyName == 'dataNascita'){
		cell.align = 'center';
	}
	
	if(cell.propertyName == 'naturaGiuridica'){
		if(cell.row.naturaGiuridica == 'SMM' || cell.row.naturaGiuridica == 'SFF')
			cell.innerHTML = "Ditta";
		else if(cell.row.sesso == 'S')
			cell.innerHTML = "Soc.";
		cell.align = 'center';
	}
	
	if(cell.row.isDitta == 'true'){
		if(cell.row.secondaIntestazione != ''){
			if(cell.propertyName == 'cognome')
				cell.innerHTML = cell.row.secondaIntestazione;
			else if(cell.propertyName == 'nome')
				cell.innerHTML = "di "+cell.row.cognome+" "+cell.row.nome;
			
		}
	}
}
