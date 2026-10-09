function openHelpCodSottogruppoAttivita(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpSottogruppoAttivita.execute'+bi);
}

function openHelpCodGruppoAttivita(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpGruppoAttivita.execute'+bi);
}

function openHelpCodAteco(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpAteco.execute'+bi);
}

function openHelpCodStatoCivile(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpStatoCivile.execute'+bi);
}

function openHelpCodTitoloStudio(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpTitoloStudio.execute'+bi);
}

function gestioneOrigine(){
	var origine = document.getElementById("infoPersonali_codOrigine");
	var referralDiv = document.getElementById("referralDiv");
	var segnalatoreDiv = document.getElementById("segnalatoreDiv");

	referralDiv.style.display = 'none';
	segnalatoreDiv.style.display = 'none';
	
	if(origine.value === Costanti_CODICE_ORIGINE_LINEABLU){
		referralDiv.style.display = '';
	}
	
	if(origine.value === Costanti_CODICE_ORIGINE_MEMBER_GET_MEMBER){
		segnalatoreDiv.style.display = '';
	}
	
	if (origine.value === Costanti_CODICE_ORIGINE_IMF){
		segnalatoreDiv.style.display = '';
	}
	
	if(origine.value === Costanti_CODICE_ORIGINE_MFORYOU){
		if(document.datiread.datiApplicativi_flagClienteSegnalato.value != '' || 
		   document.getElementById("infoPersonali_segnalatore_codMediolanum").value != '')
			segnalatoreDiv.style.display = '';
	}
	
	if(origine.value === Costanti_CODICE_ORIGINE_GLOBAL_SPECIALIST){
		segnalatoreDiv.style.display = '';		
		setFieldLabelText('infoPersonali_segnalatore_nominativoConCodice','Global Specialist segnalatore');
	}
}

function loadSegnalatore(codAgente){
	var isDitta = document.getElementById("isDitta").value;
	var origine = document.getElementById("infoPersonali_codOrigine");
	if(origine.value === Costanti_CODICE_ORIGINE_MFORYOU){

		var cmdParams = "params_codAgente="+codAgente;
		openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupMandatiM4U",
		  				 cmdParams,null,loadClienteSegnalatore,"Clienti M4U",620,780);
		
	}else if(isDitta === 'true' && origine.value === Costanti_CODICE_ORIGINE_MEMBER_GET_MEMBER){
		
		var cmdParams = "params_tipoRicerca=primaricensiti"+
		    			"&params_codAgente="+codAgente+
		    			"&params_tipoInclusioneAgenti=spv"+
		    			"&params_maxRows=50";
		openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",
		  				 cmdParams,null,loadClienteSegnalatore,"Clienti",580,780);
		  				 
	}else if(isDitta === 'false' && origine.value === Costanti_CODICE_ORIGINE_MEMBER_GET_MEMBER) {
	
		var cmdParams = "params_tipoRicerca=effettiviSenzaCodiceBloccoK"
			openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClienti",
					   cmdParams,null,loadClienteSegnalatore,"Clienti",550,900);

	}else if(origine.value === Costanti_CODICE_ORIGINE_GLOBAL_SPECIALIST){
		
		openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupGlobalSpecialist",
	  				   null,null,loadGlobalSpecialist,"Global Specialist",500,600);
	  				   
	}else if(origine.value === Costanti_CODICE_ORIGINE_IMF) {

		var cmdParams = "params_tipoRicerca=selezionaEffettivi"
			openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClienti",
					   cmdParams,null,loadClienteSegnalatore,"Clienti",550,900);
	}
	return;
}

function loadClienteSegnalatore(cli){
	if(cli == null)
		return;
	var nominativo = document.getElementById("infoPersonali_segnalatore_nominativoConCodice");
	if(cli.codMediolanum != '')
		nominativo.value = cli.codMediolanum+" - "+cli.cognome+" "+cli.nome;
	else if (cli.codInforete != '')
		nominativo.value = cli.codInforete+" - "+cli.cognome+" "+cli.nome;
	else
		nominativo.value = cli.codPotenziale+" - "+cli.cognome+" "+cli.nome;
	document.getElementById("infoPersonali_segnalatore_codInforete").value = cli.codInforete;
	document.getElementById("infoPersonali_segnalatore_codPotenziale").value = cli.codPotenziale;
	document.getElementById("infoPersonali_segnalatore_codMediolanum").value = cli.codMediolanum;
	document.getElementById("infoPersonali_segnalatore_cognome").value = cli.cognome;
	document.getElementById("infoPersonali_segnalatore_nome").value = cli.nome;	
}

function loadGlobalSpecialist(age){
	if(age == null)
		return;
	var nominativo = document.getElementById("infoPersonali_segnalatore_nominativoConCodice");
	nominativo.value = age.codAgente+" - "+age.cognomeAgente+" "+age.nomeAgente;
	document.getElementById("infoPersonali_segnalatore_codAgente").value = age.codAgente;
	document.getElementById("infoPersonali_segnalatore_codInforete").value = '';
	document.getElementById("infoPersonali_segnalatore_codPotenziale").value = '';
	document.getElementById("infoPersonali_segnalatore_codMediolanum").value = age.codAgente;
	document.getElementById("infoPersonali_segnalatore_cognome").value = age.cognomeAgente;
	document.getElementById("infoPersonali_segnalatore_nome").value = age.nomeAgente;	
}

function clearSegnalatore(){
	document.getElementById("infoPersonali_segnalatore_codAgente").value = '';
	document.getElementById("infoPersonali_segnalatore_nominativoConCodice").value = '';
	document.getElementById("infoPersonali_segnalatore_codInforete").value = '';
	document.getElementById("infoPersonali_segnalatore_codPotenziale").value = '';
	document.getElementById("infoPersonali_segnalatore_codMediolanum").value = '';
	document.getElementById("infoPersonali_segnalatore_cognome").value = '';
	document.getElementById("infoPersonali_segnalatore_nome").value = '';
}

// RFC #284520: metodo controllaAteco deprecato
function controllaAteco(){
	var ateco = document.getElementById("infoPersonali_codAteco").value;
	if (ateco.length == 4 && ateco.indexOf(".") == -1) {
		ateco = ateco.substring(0,2) + '.' + ateco.substring(2,4);
		document.getElementById("infoPersonali_codAteco").value = ateco;
	}
}

