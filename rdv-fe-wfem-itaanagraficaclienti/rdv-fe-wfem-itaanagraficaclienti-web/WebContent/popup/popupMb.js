//**************************************************************************
//CLIENTI MEDIOLANUM (Letti da IQ)
//**************************************************************************
function showPopupClienti(codAgente, codMediolanum, cognome){

	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
		codMediolanum = "";
	if(typeof(cognome) == "undefined" || cognome == null)
		cognome = "";

	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClienti","copyFields=false&params_codAgente="+codAgente+"&params_codMediolanum="+codMediolanum+"&params_cognome="+escape(cognome),null,showPopupClientiEnd,"Cerca cliente",550,900);
}

//**************************************************************************
//CLIENTI SECONDARI DI UN AGENTE CON PRESALE (Con i cointestatari assaltro)
//**************************************************************************
function showPopupClientiSecondari(codAgente, codMediolanum, cognome, tipoElementi){

	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
		codMediolanum = "";
	if(typeof(cognome) == "undefined" || cognome == null)
		cognome = "";
	if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
		tipoElementi = "";

	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente","copyFields=false&params_tipoElementi="+tipoElementi+"&params_statoElementi="+""+"&params_tipoRicerca="+"secondari"+"&params_codAgente="+codAgente+"&params_codMediolanum="+codMediolanum+"&params_cognome="+escape(cognome)+"&params_maxRows=50",null,showPopupClientiSecondariEnd,"Cerca cliente",650,750);
}

//****************************************************************************************
//CLIENTI PRIMARI EFFETTIVI DI UN AGENTE (Senza i cointestatari assaltro)
//****************************************************************************************
function showPopupClientiPrimariEffettivi(codRete, codAgente, codMediolanum, cognome, tipoElementi, codAgenteRoot){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
		codMediolanum = "";
	if(typeof(cognome) == "undefined" || cognome == null)
		cognome = "";
	if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
		tipoElementi = "";
	if(typeof(codAgenteRoot) == "undefined" || codAgenteRoot == null)
		codAgenteRoot = "";

	var params = "copyFields=false";
	params += 	 "&params_tipoElementi="+tipoElementi;
	params +=	 "&params_statoElementi="+"";
	params +=	 "&params_tipoRicerca="+"primarieffettivi";
	params +=	 "&params_codAgente="+codAgente;
	params +=	 "&params_codMediolanum="+codMediolanum;
	params +=	 "&params_cognome="+escape(cognome);
	params +=	 "&params_codAgenteRoot="+escape(codAgenteRoot);	
	params +=	 "&params_maxRows=50";
	
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",params,null,showPopupClientiPrimariEffettiviEnd,"Cerca cliente",620,780);
}

//**************************************************************************
//VARIAZIONI
//**************************************************************************
function showPopupStampaVariazioni(codAgente, codPotenziale){
	var params = "readRequest="+"false";
	params += "&codAgente="+codAgente;
	params += "&codPotenziale="+codPotenziale;
	openModalPopup("prgm.ita.anagraficaclienti.popup.business.LoadPopupStampaVariazioni",params,null,null,"Stampa Variazioni",680,880);
}

//**************************************************************************
//AGENTI
//**************************************************************************
function showPopupQuestinario(codAgente, codPotenziale, codMediolanum){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	var params = "readRequest="+"false";
	params += "&showBack="+"false";
	params += "&codAgente="+codAgente;
	params += "&codPotenziale="+codPotenziale;
	params += "&codMediolanum="+codMediolanum;
	openModalPopup("prgm.ita.anagraficaclienti.questionari.business.NuovoQuestionario",params,null,null,"Questionario",680,880);
}


//**************************************************************************
//AGENTI
//**************************************************************************
function showPopupAgenti(codRete, codAgente, cognomeAgente){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(cognomeAgente) == "undefined" || cognomeAgente == null)
		cognomeAgente = "";
	var params = "params_codAgente="+codAgente;
	params += "&params_cognomeAgente="+escape(cognomeAgente);
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupAgenti",params,null,showPopupAgentiEnd,"Agenti",500,600);
}

//**************************************************************************
//AGENTI GLOBAL SPECIALIST
//**************************************************************************
function showPopupGlobalSpecialist(codRete, codAgente, cognomeAgente){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(cognomeAgente) == "undefined" || cognomeAgente == null)
		cognomeAgente = "";
	var params = "params_codAgente="+codAgente;
	params += "&params_cognomeAgente="+escape(cognomeAgente);
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupGlobalSpecialist",params,null,null,"Global Specialist",500,600);
}


//**************************************************************************
//CLIENTI PRIMARI DI UN AGENTE CON PRESALE (Senza i cointestatari assaltro)
//**************************************************************************
function showPopupClientiPrimari(codRete, codAgente, codMediolanum, cognome, tipoElementi){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
		codMediolanum = "";
	if(typeof(cognome) == "undefined" || cognome == null)
		cognome = "";
	if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
		tipoElementi = "";
	var params = "params_tipoElementi="+tipoElementi;
	params += "&params_statoElementi="+"";
	params += "&params_tipoRicerca="+"primari";
	params += "&params_codAgente="+codAgente;
	params += "&params_codMediolanum="+codMediolanum;
	params += "&params_cognome="+escape(cognome);
	params += "&params_maxRows="+"50"; 
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",params,null,null,"Anagrafica Clienti",620,780);
}

//**************************************************************************************************
//CLIENTI PRIMARI (EFFETTIVI+INV IN SEDE) DI UN AGENTE (Senza i cointestatari assaltro)
//**************************************************************************************************
function showPopupClientiPrimariCensiti(codRete, codAgente, codMediolanum, cognome, tipoElementi){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
		codMediolanum = "";
	if(typeof(cognome) == "undefined" || cognome == null)
		cognome = "";
	if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
		tipoElementi = "";
	var params = "params_tipoElementi="+tipoElementi;
	params += "&params_statoElementi="+"";
	params += "&params_tipoRicerca="+"primaricensiti";
	params += "&params_codAgente="+codAgente;
	params += "&params_codMediolanum="+codMediolanum;
	params += "&params_cognome="+escape(cognome);
	params += "&params_maxRows="+"50"; 
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",params,null,null,"Anagrafica Clienti",620,780);
}

//**********************************************************************************************
//CLIENTI SECONDARI (EFFETTIVI+INV IN SEDE) DI UN AGENTE (Con i cointestatari assaltro)
//**********************************************************************************************
function showPopupClientiSecondariCensiti(codRete, codAgente, codMediolanum, cognome, tipoElementi){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
		codMediolanum = "";
	if(typeof(cognome) == "undefined" || cognome == null)
		cognome = "";
	if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
		tipoElementi = "";
	var params = "params_tipoElementi="+tipoElementi;
	params += "&params_statoElementi="+"";
	params += "&params_tipoRicerca="+"secondaricensiti";
	params += "&params_codAgente="+codAgente;
	params += "&params_codMediolanum="+codMediolanum;
	params += "&params_cognome="+escape(cognome);
	params += "&params_maxRows="+"50";
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",params,null,null,"Anagrafica Clienti",620,780);
}

//**********************************************************************************************
//CLIENTI SECONDARI EFFETTIVI DI UN AGENTE (Con i cointestatari)
//**********************************************************************************************
function showPopupClientiSecondariEffettivi(codRete, codAgente, codMediolanum, cognome, tipoElementi){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
		codMediolanum = "";
	if(typeof(cognome) == "undefined" || cognome == null)
		cognome = "";
	if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
		tipoElementi = "";
	var params = "params_tipoElementi="+tipoElementi;
	params += "&params_statoElementi="+"";
	params += "&params_tipoRicerca="+"secondarieffettivi";
	params += "&params_codAgente="+codAgente;
	params += "&params_codMediolanum="+codMediolanum;
	params += "&params_cognome="+escape(cognome);
	params += "&params_maxRows="+"50";
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",params,null,null,"Anagrafica Clienti",620,780);
}

//**************************************************************************************************
//CLIENTI MGM (EFFETTIVI+INV IN SEDE) DI UN AGENTE+SUPERVISORE (Senza i cointestatari assaltro)
//**************************************************************************************************
function showPopupClientiMGM(codAgente){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	var params = "params_tipoElementi="+"";
	params += "&params_statoElementi="+"";
	params += "&params_tipoRicerca="+"primaricensiti";
	params += "&params_codAgente="+codAgente;
	params += "&params_codMediolanum="+"";
	params += "&params_cognome="+"";
	params += "&params_tipoInclusioneAgenti="+"spv";
	params += "&params_maxRows="+"50"; 
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente",params,null,null,"Clienti",620,780);
}

//**************************************************************************
//COMUNI
//**************************************************************************
function showPopupComuni(cap, comune, provincia){
	var params = "isRicercaIscrittiAlCatasto="+"false";
	params += "&cap="+cap;
	params += "&provincia="+provincia;
	params += "&comune="+escape(comune);
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupComuni",params,null,null,"Comuni",500,700);
}

//**************************************************************************
//COMUNI INSCRITTI AL CATASTO
//**************************************************************************
function showPopupComuniCatasto(cap, comune, provincia){
	var params = "isRicercaIscrittiAlCatasto"+"true";
	params += "&cap="+cap;
	params += "&provincia="+provincia;
	params += "&comune="+escape(comune);
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupComuni",params,null,null,"Comuni",500,700);
}

//**************************************************************************
//UNIVERSITA
//**************************************************************************
function showPopupUniversita(){
	var params = "";
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupUniversita",params,null,null,"Università",500,800);
}

//**************************************************************************
//MANDATI M4U
//**************************************************************************
function showPopupMandatiM4U(codAgente){
	if(typeof(codAgente) == "undefined" || codAgente == null)
		codAgente = "";
	var params = "params_codAgente="+codAgente;
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupMandatiM4U",params,null,null,"Università",620,780);
}


//**************************************************************************
//VARIAZIONE CLIENTE
//**************************************************************************
function showPopupVariazioneAnagraficaCliente(codAgente,codPotenziale,codMediolanum, codFiscale,partitaIva,progressivo){
	var params = "codAgente="+codAgente;
	if(codPotenziale)
		params += "&codPotenziale="+codPotenziale;
	if(codMediolanum)
		params += "&codMediolanum="+codMediolanum;
	if(codFiscale)
		params += "&codFiscale="+codFiscale;
	if(partitaIva)
		params += "&partitaIva="+partitaIva;
	if(progressivo)
		params += "&progressivo="+progressivo;
	params += "&popupMode="+"true";
	params += "&readRequest="+"false";
	openModalPopup("prgm.ita.anagraficaclienti.business.VisualizzaVariazioneAnagraficaCliente",params,null,null,"Variazione Anagrafica",680,950);
}


//**************************************************************************
//DATI CLIENTE
//**************************************************************************
function showPopupAnagraficaCliente(codAgente,codPotenziale,codMediolanum, codFiscale,partitaIva){
	var params = "codAgente="+codAgente;
	if(codPotenziale)
		params += "&codPotenziale="+codPotenziale;
	if(codMediolanum)
		params += "&codMediolanum="+codMediolanum;
	if(codFiscale)
		params += "&codFiscale="+codFiscale;
	if(partitaIva)
		params += "&partitaIva="+partitaIva;
	params += "&popupMode="+"true";
	openModalPopup("prgm.ita.anagraficaclienti.business.VisualizzaAnagraficaCliente",params,null,null,"Anagrafica Clienti",680,950);
}

//**************************************************************************
//INDIRIZZI CLIENTE
//**************************************************************************
function showPopupIndirizziCliente(codAgente,codPotenziale,codMediolanum, codFiscale,partitaIva){
	var params = "cliente_codAgente="+codAgente;
	if(codPotenziale)
		params += "&cliente_codPotenziale="+codPotenziale;
	if(codMediolanum)
		params += "&cliente_codMediolanum="+codMediolanum;
	if(codFiscale)
		params += "&cliente_codFiscale="+codFiscale;
	if(partitaIva)
		params += "&cliente_partitaIva="+partitaIva;
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupIndirizziCliente",params,null,null,"Indirizzi",300,900);
}

