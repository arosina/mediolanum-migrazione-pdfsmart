var anagParSeparator = "%09";
var anagEqIndicator = "%3d";
var anagUrlPrefix = "call.wfem?wfemCmd=showPage&page=ita/anagraficaclienti/popup/display/PopupIFrame.jsp";

//**************************************************************************
//VARIAZIONI
//**************************************************************************
function showPopupStampaVariazioni(codAgente, codPotenziale){
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.business.LoadPopupStampaVariazioni";
  url += "&popupParams=";
  url += "readRequest"+anagEqIndicator+"false"+anagParSeparator;
  url += "codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "codPotenziale"+anagEqIndicator+codPotenziale+anagParSeparator;
  return doShowDialog(url,680,880);
}

//**************************************************************************
//AGENTI
//**************************************************************************
function showPopupQuestinario(codAgente, codPotenziale, codMediolanum){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";
  	
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.questionari.business.NuovoQuestionario";
  url += "&popupParams=";
  url += "readRequest"+anagEqIndicator+"false"+anagParSeparator;
  url += "showBack"+anagEqIndicator+"false"+anagParSeparator;
  url += "codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "codPotenziale"+anagEqIndicator+codPotenziale+anagParSeparator;
  url += "codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  return doShowDialog(url,680,880);
}

//**************************************************************************
//AGENTI
//**************************************************************************
function showPopupAgenti(codRete, codAgente, cognomeAgente){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";
  if(typeof(cognomeAgente) == "undefined" || cognomeAgente == null)
  	cognomeAgente = "";
  	
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupAgenti";
  url += "&popupParams=";
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_cognomeAgente"+anagEqIndicator+escape(cognomeAgente)+anagParSeparator;
  return doShowDialog(url,500,600);
}

//**************************************************************************
//AGENTI GLOBAL SPECIALIST
//**************************************************************************
function showPopupGlobalSpecialist(codRete, codAgente, cognomeAgente){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";
  if(typeof(cognomeAgente) == "undefined" || cognomeAgente == null)
  	cognomeAgente = "";
  	
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupGlobalSpecialist";
  url += "&popupParams=";
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_cognomeAgente"+anagEqIndicator+escape(cognomeAgente)+anagParSeparator;
  return doShowDialog(url,500,600);
}

//**************************************************************************
//CLIENTI MEDIOLANUM (Letti da IQ)
//**************************************************************************
function showPopupClienti(codRete, codAgente, codMediolanum, cognome){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";
  if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
  	codMediolanum = "";
  if(typeof(cognome) == "undefined" || cognome == null)
  	cognome = "";

  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClienti";
  url += "&popupParams=";
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+escape(cognome)+anagParSeparator;
  return doShowDialog(url,580,900);
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

  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente";
  url += "&popupParams=";
  url += "params_tipoElementi"+anagEqIndicator+tipoElementi+anagParSeparator;
  url += "params_statoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoRicerca"+anagEqIndicator+"primari"+anagParSeparator;
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+escape(cognome)+anagParSeparator;
  url += "params_maxRows"+anagEqIndicator+"50"; 
  return doShowDialog(url,620,780);
}

//**************************************************************************
// CLIENTI SECONDARI DI UN AGENTE CON PRESALE (Con i cointestatari assaltro)
//**************************************************************************
function showPopupClientiSecondari(codRete, codAgente, codMediolanum, cognome, tipoElementi){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";
  if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
  	codMediolanum = "";
  if(typeof(cognome) == "undefined" || cognome == null)
  	cognome = "";
  if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
  	tipoElementi = "";
	
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente";
  url += "&popupParams=";
  url += "params_tipoElementi"+anagEqIndicator+tipoElementi+anagParSeparator;
  url += "params_statoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoRicerca"+anagEqIndicator+"secondari"+anagParSeparator;
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+escape(cognome)+anagParSeparator;
  url += "params_maxRows"+anagEqIndicator+"50";
  return doShowDialog(url,620,780);
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

  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente";
  url += "&popupParams=";
  url += "params_tipoElementi"+anagEqIndicator+tipoElementi+anagParSeparator;
  url += "params_statoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoRicerca"+anagEqIndicator+"primaricensiti"+anagParSeparator;
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+escape(cognome)+anagParSeparator;
  url += "params_maxRows"+anagEqIndicator+"50"; 
  return doShowDialog(url,620,780);
}

//**********************************************************************************************
// CLIENTI SECONDARI (EFFETTIVI+INV IN SEDE) DI UN AGENTE (Con i cointestatari assaltro)
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
	
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente";
  url += "&popupParams=";
  url += "params_tipoElementi"+anagEqIndicator+tipoElementi+anagParSeparator;
  url += "params_statoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoRicerca"+anagEqIndicator+"secondaricensiti"+anagParSeparator;
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+escape(cognome)+anagParSeparator;
  url += "params_maxRows"+anagEqIndicator+"50";
  return doShowDialog(url,620,780);
}

//****************************************************************************************
//CLIENTI PRIMARI EFFETTIVI DI UN AGENTE (Senza i cointestatari assaltro)
//****************************************************************************************
function showPopupClientiPrimariEffettivi(codRete, codAgente, codMediolanum, cognome, tipoElementi){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";
  if(typeof(codMediolanum) == "undefined" || codMediolanum == null)
  	codMediolanum = "";
  if(typeof(cognome) == "undefined" || cognome == null)
  	cognome = "";
  if(typeof(tipoElementi) == "undefined" || tipoElementi == null)
  	tipoElementi = "";

  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente";
  url += "&popupParams=";
  url += "params_tipoElementi"+anagEqIndicator+tipoElementi+anagParSeparator;
  url += "params_statoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoRicerca"+anagEqIndicator+"primarieffettivi"+anagParSeparator;
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+escape(cognome)+anagParSeparator;
  url += "params_maxRows"+anagEqIndicator+"50"; 
  return doShowDialog(url,620,780);
}

//**********************************************************************************************
// CLIENTI SECONDARI EFFETTIVI DI UN AGENTE (Con i cointestatari)
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
	
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente";
  url += "&popupParams=";
  url += "params_tipoElementi"+anagEqIndicator+tipoElementi+anagParSeparator;
  url += "params_statoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoRicerca"+anagEqIndicator+"secondarieffettivi"+anagParSeparator;
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+escape(cognome)+anagParSeparator;
  url += "params_maxRows"+anagEqIndicator+"50";
  return doShowDialog(url,620,780);
}

//**************************************************************************************************
//CLIENTI MGM (EFFETTIVI+INV IN SEDE) DI UN AGENTE+SUPERVISORE (Senza i cointestatari assaltro)
//**************************************************************************************************
function showPopupClientiMGM(codAgente){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";

  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupClientiAgente";
  url += "&popupParams=";
  url += "params_tipoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_statoElementi"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoRicerca"+anagEqIndicator+"primaricensiti"+anagParSeparator;
  url += "params_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  url += "params_codMediolanum"+anagEqIndicator+""+anagParSeparator;
  url += "params_cognome"+anagEqIndicator+""+anagParSeparator;
  url += "params_tipoInclusioneAgenti"+anagEqIndicator+"spv"+anagParSeparator;
  url += "params_maxRows"+anagEqIndicator+"50"; 
  return doShowDialog(url,620,780);
}


//**************************************************************************
//COMUNI
//**************************************************************************
function showPopupComuni(cap, comune, provincia){
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupComuni";
  url += "&popupParams=";

  url += "isRicercaIscrittiAlCatasto"+anagEqIndicator+"false"+anagParSeparator;
  url += "cap"+anagEqIndicator+cap+anagParSeparator;
  url += "provincia"+anagEqIndicator+provincia+anagParSeparator;
  url += "comune"+anagEqIndicator+escape(comune);
  return doShowDialog(url,500,700);
}

//**************************************************************************
//COMUNI INSCRITTI AL CATASTO
//**************************************************************************
function showPopupComuniCatasto(cap, comune, provincia){
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupComuni";
  url += "&popupParams=";

  url += "isRicercaIscrittiAlCatasto"+anagEqIndicator+"true"+anagParSeparator;
  url += "cap"+anagEqIndicator+cap+anagParSeparator;
  url += "provincia"+anagEqIndicator+provincia+anagParSeparator;
  url += "comune"+anagEqIndicator+escape(comune);
  return doShowDialog(url,500,700);
}

//**************************************************************************
//UNIVERSITA
//**************************************************************************
function showPopupUniversita(){
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupUniversita";
  url += "&popupParams=";
  return doShowDialog(url,500,800);
}

//**************************************************************************
//MANDATI M4U
//**************************************************************************
function showPopupMandatiM4U(codAgente){
  if(typeof(codAgente) == "undefined" || codAgente == null)
  	codAgente = "";
  	
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupMandatiM4U";
  url += "&popupParams=";
  url += "params_codAgente"+anagEqIndicator+codAgente;
  return doShowDialog(url,620,780);
}

//**************************************************************************
//VARIAZIONE CLIENTE
//**************************************************************************
function showPopupVariazioneAnagraficaCliente(codAgente,codPotenziale,codMediolanum,
											  codFiscale,partitaIva,progressivo){
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.business.VisualizzaVariazioneAnagraficaCliente";
  url += "&popupParams=";

  url += "codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  if(codPotenziale)
	  url += "codPotenziale"+anagEqIndicator+codPotenziale+anagParSeparator;
  if(codMediolanum)
	  url += "codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  if(codFiscale)
	  url += "codFiscale"+anagEqIndicator+codFiscale+anagParSeparator;
  if(partitaIva)
	  url += "partitaIva"+anagEqIndicator+partitaIva+anagParSeparator;
  if(progressivo)
	  url += "progressivo"+anagEqIndicator+progressivo+anagParSeparator;
  url += "popupMode"+anagEqIndicator+"true"+anagParSeparator;
  url += "readRequest"+anagEqIndicator+"false";
  return doShowDialog(url,680,950);
}

//**************************************************************************
//DATI CLIENTE
//**************************************************************************
function showPopupAnagraficaCliente(codAgente,codPotenziale,codMediolanum,
								    codFiscale,partitaIva){
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.business.VisualizzaAnagraficaCliente";
  url += "&popupParams=";

  url += "codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  if(codPotenziale)
	  url += "codPotenziale"+anagEqIndicator+codPotenziale+anagParSeparator;
  if(codMediolanum)
	  url += "codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  if(codFiscale)
	  url += "codFiscale"+anagEqIndicator+codFiscale+anagParSeparator;
  if(partitaIva)
	  url += "partitaIva"+anagEqIndicator+partitaIva+anagParSeparator;
  url += "popupMode"+anagEqIndicator+"true";
  return doShowDialog(url,680,950);
}

//**************************************************************************
//INDIRIZZI CLIENTE
//**************************************************************************
function showPopupIndirizziCliente(codAgente,codPotenziale,codMediolanum,
								    codFiscale,partitaIva){
  var url = anagUrlPrefix;
  url += "&popupName=prgm.ita.anagraficaclienti.popup.display.PopupIndirizziCliente";
  url += "&popupParams=";

  url += "cliente_codAgente"+anagEqIndicator+codAgente+anagParSeparator;
  if(codPotenziale)
	  url += "cliente_codPotenziale"+anagEqIndicator+codPotenziale+anagParSeparator;
  if(codMediolanum)
	  url += "cliente_codMediolanum"+anagEqIndicator+codMediolanum+anagParSeparator;
  if(codFiscale)
	  url += "cliente_codFiscale"+anagEqIndicator+codFiscale+anagParSeparator;
  if(partitaIva)
	  url += "cliente_partitaIva"+anagEqIndicator+partitaIva+anagParSeparator;
  return doShowDialog(url,300,900);
}

//**************************************************************************
//**************************************************************************
function doShowDialog(url,h,w){
  return showModalDialog(url,"","scroll:no;status:no;dialogHeight:"+h+"px;dialogWidth:"+w+"px");
}
//**************************************************************************
//**************************************************************************
function doShowDialogWindow(url,h,w){
  return window.open(url,"","");
}
