function openHelpCodProfessione(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpProfessione.execute'+bi);
}

function openHelpCodSettoreEconomico(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpSettoreEconomico.execute'+bi);
}

function openHelpMotivazionePep(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpMotivazionePep.execute'+bi);
}

function impostaCombinazioneProvenienzaPatrimonio(){
	var comb = "";
	if(document.dati.infoPersonali_combinazione1ProvenienzaPatrimonio.value === "true")
		comb += "1";
	if(document.dati.infoPersonali_combinazione2ProvenienzaPatrimonio.value === "true")
		comb += "2";
	if(document.dati.infoPersonali_combinazione3ProvenienzaPatrimonio.value === "true")
		comb += "3";
	if(document.dati.infoPersonali_combinazione4ProvenienzaPatrimonio.value === "true")
		comb += "4";
	if(document.dati.infoPersonali_combinazione5ProvenienzaPatrimonio.value === "true")
		comb += "5";
	if(document.dati.infoPersonali_combinazione6ProvenienzaPatrimonio.value === "true")
		comb += "6";
	if(document.dati.infoPersonali_combinazione7ProvenienzaPatrimonio.value === "true")
		comb += "7";
	document.dati.infoPersonali_combinazioneProvenienzaPatrimonio.value = comb;
	hideHelperAnchor("infoPersonali_combinazioneProvenienzaPatrimonio");
}

function ricaricaTendinaSettoreEconomico(){
	if(isRequestPending())
		return;
	startRequest();
	document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.business.AggiornaElencoSettoriEconomici.execute";
	wfemHiddenSubmit(document.dati,'tabContainer');
}

function gestioneProfessione(){
	var professione = document.getElementById("infoPersonali_codProfessione").value;
	if(professione === Costanti_CODICE_PROFESSIONE_STUDENTE)
		document.getElementById("universitaDiv").style.display='';
	else
		document.getElementById("universitaDiv").style.display='none';

	if( professione === Costanti_CODICE_PROFESSIONE_DIPENDENTE_DIRIGENTE ||
		professione === Costanti_CODICE_PROFESSIONE_AUTONOMO_IMPRENDITORE ||
		professione === Costanti_CODICE_PROFESSIONE_SOGGETTO_APICALE){
		document.getElementById("divFormaGiuridicaSocietaAppartenenza").style.display='';
	}else{
		document.getElementById("adempimentiNormativi_formaGiuridicaSocietaAppartenenza").value="";
		document.getElementById("divFormaGiuridicaSocietaAppartenenza").style.display='none';
	}
}

function gestioneNazioneProfessione(){
	var combo = document.dati.infoPersonali_nazioneSvolgimentoProfessione;
	if(combo.value === Costanti_COD_UIC_NAZIONE_ITALIA){
		document.getElementById('tabProvinciaSvolgimentoProfessione').style.display = '';
	}else{
		document.dati.infoPersonali_provinciaSvolgimentoProfessione.value = ''
		document.getElementById('tabProvinciaSvolgimentoProfessione').style.display = 'none';
	}
}

function gestioneMotivazionePep(si){
	try{
		hideHelperAnchor("residenza_flagPep");
	}catch(e){}
	if(si){
		enableField("residenza_motivazionePep",true);
	}else{
		document.getElementById('residenza_motivazionePep').value = '';
		enableField("residenza_motivazionePep",false);
	}
}

function loadUniversita(){
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupUniversita",
	  				null,null,loadUniversitaEnd,"Universit�",580,780);
}

function loadUniversitaEnd(uni){
	if(uni == null)
		return;
	document.getElementById("infoPersonali_universita_codUniversita").value = uni.codUniversita;
	document.getElementById("infoPersonali_universita_facolta").value = uni.facolta;
	document.getElementById("infoPersonali_universita_ateneo").value = uni.ateneo;
	document.getElementById("infoPersonali_universita_provincia").value = uni.provincia;
}

function clearUniversita(){
	document.getElementById("infoPersonali_universita_codUniversita").value = '';
	document.getElementById("infoPersonali_universita_facolta").value = '';
	document.getElementById("infoPersonali_universita_ateneo").value = '';
	document.getElementById("infoPersonali_universita_provincia").value = '';
}

function gestioneHaCarichePubbliche(){
	var val = getPropertyValue("adempimentiNormativi_haCarichePubbliche");
	if(val == null)
		val = "";
	if(val === "S"){
		document.getElementById("divCaricaPubblicaSi").style.display='';
	}else{
		document.getElementById("divCaricaPubblicaSi").style.display='none';
		document.dati.adempimentiNormativi_caricaPubblicaRicoperta.value = "";
	}
	gestioneCaricaPubblicaRicoperta();
}

function gestioneCaricaPubblicaRicoperta(){
	var combo = document.dati.adempimentiNormativi_caricaPubblicaRicoperta;
	if(combo.value === Costanti_CODICE_CARICA_PUBBLICA_RICOPERTA_POLITICO_ISTITUZIONALE){
		document.getElementById("divDettaglioCaricaPubblicaRicoperta").style.display='';
	}else{
		document.getElementById("divDettaglioCaricaPubblicaRicoperta").style.display='none';
		document.dati.adempimentiNormativi_dettaglioCaricaPubblicaRicoperta.value="";
	}
}

function gestioneLegamiAffariDiversiDaAttivitaPrincipale(){
	var val = getPropertyValue("adempimentiNormativi_haLegamiAffariDiversiDaAttivitaPrincipale");
	if(val == null)
		val = "";
	if(val === "S"){
		document.getElementById("divTipologiaLegameAffariDiversoDaAttivitaPrincipaleSi").style.display='';
		document.getElementById("divPaesiLegamiAffariDiversiDaAttivitaPrincipaleSi").style.display='';
	}else{
		document.getElementById("divTipologiaLegameAffariDiversoDaAttivitaPrincipaleSi").style.display='none';
		document.getElementById("divPaesiLegamiAffariDiversiDaAttivitaPrincipaleSi").style.display='none';
		document.dati.adempimentiNormativi_tipologiaLegameAffariDiversoDaAttivitaPrincipale.value="";
		document.dati.adempimentiNormativi_paeseLegameAffariDiversoDaAttivitaPrincipale1.value="";
		document.dati.adempimentiNormativi_paeseLegameAffariDiversoDaAttivitaPrincipale2.value="";
		document.dati.adempimentiNormativi_paeseLegameAffariDiversoDaAttivitaPrincipale3.value="";
	}
}

function gestioneLegamiParentelaConPep(onclick){
	var val = getPropertyValue("adempimentiNormativi_haLegamiParentelaConPep");
	if(val == null)
		val = "";
	if(val !== "" && val !== "N"){
		document.getElementById("divLegamiParentelaConPepSi").style.display='';
		if(onclick){
			enableField("adempimentiNormativi_tipologiaLegameParentelaConPep",true);
			enableField("adempimentiNormativi_tipologiaFunzionePubblicaLegameParentelaConPep",true);
		}
	}else{
		document.getElementById("divLegamiParentelaConPepSi").style.display='none';
		document.dati.adempimentiNormativi_tipologiaLegameParentelaConPep.value="";
		document.dati.adempimentiNormativi_tipologiaFunzionePubblicaLegameParentelaConPep.value="";
	}
}

function gestioneLegamiAffariConPep(onclick){
	var val = getPropertyValue("adempimentiNormativi_haLegamiAffariConPep");
	if(val == null)
		val = "";
	if(val !== "" && val !== "N"){
		document.getElementById("divLegamiAffariConPepSi").style.display='';
		if(onclick){
			enableField("adempimentiNormativi_tipologiaLegameAffariConPep",true);
			enableField("adempimentiNormativi_tipologiaFunzionePubblicaLegameAffariConPep",true);
		}
	}else{
		document.getElementById("divLegamiAffariConPepSi").style.display='none';
		document.dati.adempimentiNormativi_tipologiaLegameAffariConPep.value="";
		document.dati.adempimentiNormativi_tipologiaFunzionePubblicaLegameAffariConPep.value="";
	}
}
