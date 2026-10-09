function doCalcolaCodiceFiscaleAction(){
	var errMsg = "";
   	if(document.dati.cognome.value == "")
		errMsg += "- Cognome\n";
	if(document.dati.nome.value == "")
		errMsg += "- Nome\n";
	if(!document.dati.sesso[0].checked && !document.dati.sesso[1].checked)
		errMsg += "- Sesso\n";
	if(document.dati.dataNascita.value == "")
		errMsg += "- Data di nascita\n";
	if(document.dati.comuneNascita_comune.value == "")
		errMsg += "- Luogo di nascita\n";
	if(errMsg != ""){	    
		alert("Inserire:\n"+errMsg);
		return false;
	}
	document.dati.wfemCmd.value = 'prgm.ita.anagraficaclienti.business.CalcolaCodiceFiscale.execute';
	submitForm();
	return true;
	
}

function resetComuneNascita(){
	document.dati.comuneNascita_provincia.value="";
	document.dati.comuneNascita_codComune.value="";
	document.dati.comuneNascita_cap.value="";
}

function loadComuneNascita(){
	var comune = document.dati.comuneNascita_comune.value;
	
	var cmdParams = "isRicercaIscrittiAlCatasto=true";
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupComuni",
	  				 cmdParams,null,loadComuneNascitaEnd,"Ricerca Comuni / Località",500,700);
}

function loadComuneNascitaEnd(comuneSelezionato){
	if(comuneSelezionato == null)
		return;
		
	document.dati.comuneNascita_comune.value = comuneSelezionato.comune;
	document.dati.comuneNascita_provincia.value = comuneSelezionato.provincia;
	document.dati.comuneNascita_codComune.value = comuneSelezionato.codComune;		
	document.dati.comuneNascita_cap.value = comuneSelezionato.cap;
	document.dati.comuneNascita_codNazione.value = comuneSelezionato.codNazione;
}

function gestioneNazioneNascita(onchange){
	if(onchange){
		document.dati.comuneNascita_cap.value = '';
		document.dati.comuneNascita_comune.value = '';
		document.dati.comuneNascita_comuneEstero.value = '';
		document.dati.comuneNascita_provincia.value = '';
		document.dati.comuneNascita_codComune.value = '';
	}
	
	var combo = document.dati.comuneNascita_codNazione;
	if(combo.value == Costanti_COD_NAZIONE_ITALIA){
		document.getElementById('tabComuneEstero').style.display = 'none';
		document.getElementById('tabComuneItalia').style.display = '';		
	}else{
		document.getElementById('tabComuneItalia').style.display = 'none';
		document.getElementById('tabComuneEstero').style.display = '';
	}
}

function settaTipoPersona(tipoPersona){
	if(document.dati.sesso[0].checked || document.dati.sesso[1].checked || tipoPersona == 'F'){ // Fisica

		document.getElementById('tabSesso').style.display = '';
		document.getElementById('tabCodFiscale').style.display = '';		
		document.getElementById('tabPartitaIva').style.display = 'none';	
		
		setFieldLabelText("dataNascita",template_getProperty_AnagraficaCliente_dataNascita);
		setFieldLabelText("comuneNascita_comune",template_getProperty_AnagraficaCliente_comuneNascita_comune);
		if(document.datiread.isDitta.value == 'true'){
			document.getElementById('tabPartitaIva').style.display = '';	
			setFieldLabelText("partitaIva",template_getProperty_AnagraficaCliente_partitaIva+" <span class='mtory'>(*)</span>");
		}

	}else if(tipoPersona == 'G'){ // Giuridica

		document.getElementById('tabSesso').style.display = 'none';
		document.getElementById('tabCodFiscale').style.display = 'none';
		document.getElementById('tabPartitaIva').style.display = '';			
		
		setFieldLabelText("dataNascita",template_getProperty_AnagraficaCliente_dataCostituzione);
		setFieldLabelText("comuneNascita_comune",template_getProperty_AnagraficaCliente_comuneNascita_comuneCostituzione);
		setFieldLabelText("partitaIva",template_getProperty_AnagraficaCliente_partitaIva+" <span class='mtory'>(*)</span>");

	}else{ // Non valorizzata
		
		document.getElementById('tabSesso').style.display = 'none';
		
		setFieldLabelText("dataNascita",template_getProperty_AnagraficaCliente_dataNascita);
		setFieldLabelText("comuneNascita_comune",template_getProperty_AnagraficaCliente_comuneNascita_comune);
		if(document.datiread.isDitta == false)		
			setFieldLabelText("partitaIva",template_getProperty_AnagraficaCliente_partitaIva);
		else
			setFieldLabelText("partitaIva",template_getProperty_AnagraficaCliente_partitaIva+" <span class='mtory'>(*)</span>");
		
		if(document.datiread.isDitta == false)		
			document.getElementById('tabSesso').style.display = 'none';
		document.getElementById('tabCodFiscale').style.display = 'none';
		document.getElementById('tabPartitaIva').style.display = 'none';
	}
}

function settaRadio(nome,valore,click){
	var o = setPropertyValue(nome,valore);
	if(click)
		o.click();
}
