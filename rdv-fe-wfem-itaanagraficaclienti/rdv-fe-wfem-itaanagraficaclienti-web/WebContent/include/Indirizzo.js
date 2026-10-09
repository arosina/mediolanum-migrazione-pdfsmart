function resetComune(prefix){
	document.getElementById(prefix+"_provincia").value="";
	document.getElementById(prefix+'_codComune').value = "";
}

function loadComune(prefix){
	document.curPrefix=prefix;
	var cmdParams = "isRicercaIscrittiAlCatasto=false";
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupComuni",
	  				 cmdParams,null,loadComuneEnd,"Ricerca Comuni / Località",500,700);
}

function loadComuneEnd(comuneSelezionato){
	if(comuneSelezionato == null)
		return;
	var prefix = document.curPrefix;
	document.getElementById(prefix+'_comune').value = comuneSelezionato.comune;
	document.getElementById(prefix+'_provincia').value = comuneSelezionato.provincia;
	document.getElementById(prefix+'_codComune').value = comuneSelezionato.codComune;
	document.getElementById(prefix+'_cap').value = comuneSelezionato.cap;
	document.getElementById(prefix+'_codNazione').value = comuneSelezionato.codNazione;
}

function gestioneNazioneComune(onchange,prefix){
	if(onchange){
		document.getElementById(prefix+"_cap").value = '';
		document.getElementById(prefix+"_comune").value = '';
		document.getElementById(prefix+"_comuneEstero").value = '';
		document.getElementById(prefix+"_provincia").value = '';
		document.getElementById(prefix+"_codComune").value = '';
		document.getElementById(prefix+"_toponimoIndirizzo").value = '';
		clearIndirizzoErrors(prefix);
	}
	
	var combo = document.getElementById(prefix+"_codNazione");
	if(combo.value === Costanti_COD_NAZIONE_ITALIA){
		document.getElementById('tabComuneEstero'+prefix).style.display = 'none';
		document.getElementById('tabComuneItalia0'+prefix).style.display = '';
		document.getElementById('tabComuneItalia1'+prefix).style.display = '';
		document.getElementById('tabComuneItalia2'+prefix).style.display = '';
		document.getElementById('tabComuneItalia3'+prefix).style.display = '';
		document.getElementById('tabComuneItalia4'+prefix).style.display = '';
	}else{
		document.getElementById('tabComuneItalia0'+prefix).style.display = 'none';
		document.getElementById('tabComuneItalia1'+prefix).style.display = 'none';
		document.getElementById('tabComuneItalia2'+prefix).style.display = 'none';
		document.getElementById('tabComuneItalia3'+prefix).style.display = 'none';
		document.getElementById('tabComuneItalia4'+prefix).style.display = 'none';
		document.getElementById('tabComuneEstero'+prefix).style.display = '';
	}
}

function clearIndirizzoErrors(prefix){
	try{
		__fieldIntf.closeHelper("Err");
		__fieldIntf.closeHelper("War");
		clearIndirizzoFieldErrors(prefix, "toponimoIndirizzo");
		clearIndirizzoFieldErrors(prefix, "descrizioneIndirizzo");
		clearIndirizzoFieldErrors(prefix, "numeroCivico");
		clearIndirizzoFieldErrors(prefix, "provincia");
		clearIndirizzoFieldErrors(prefix, "comune");
		clearIndirizzoFieldErrors(prefix, "cap");
	}catch(e){}
}

function clearIndirizzoFieldErrors(prefix, field){
	hideHelperAnchor(prefix+"_"+field); 
	$("#"+prefix+"_"+field).removeClass('fieldHasError').addClass('inputField');
}