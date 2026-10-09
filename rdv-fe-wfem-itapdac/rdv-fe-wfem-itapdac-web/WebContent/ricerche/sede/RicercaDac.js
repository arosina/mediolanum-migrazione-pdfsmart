function setTipoRicerca(radio){
	document.ricerca.tipoRicerca.value = radio.value;
	try{
		clearDateField("parametri_dataInizio");
		clearDateField("parametri_dataFine");
	}catch(e){}
}

function pulisciCampiRicercaDac(){
	var f = document.ricerca;
	
	f.parametri_stato.value = '';
	f.parametri_esito.value = '';
	if(document.getElementById("parametri_tipoDac") != null)
		f.parametri_tipoDac.value = '';
	if(document.getElementById("parametri_ubicazione") != null)
		f.parametri_ubicazione.value = '';

	clearDateField('parametri_dataInizio');
	clearDateField('parametri_dataFine');
	clearDateField('parametri_dataRicezioneDocumenti');
	
	f.parametri_uffLavorazione.value = '';
	f.parametri_uffSpunta.value = '';
	if(document.getElementById("parametri_uffMittente") != null)
		f.parametri_uffMittente.value = '';
	
	f.parametri_codUtenteMittente.value = '';
	f.parametri_codUtenteLavorazione.value = '';
	f.parametri_codUtenteSpunta.value = '';
}

document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    document.all("ricercaAction").focus();
	    document.all("ricercaAction").click();
	}
}

function doRicercaAction(){
	if(currentJSTabId == null)
		return;
		
	document.ricerca.doSearch.value = 'true';
	
	if(currentJSTabId == 'JSTab0')
		return doRicercaUno();
	else	
		return doRicercaAvanzata();
}

function doRicercaUno(){
	if(document.ricerca.parametri_idDac.value == ''){
		document.getElementById("parametri_idDac").focus();
		document.getElementById("parametri_idDac").select();
		return false;
	}
	
	document.ricerca.wfemCmd.value = 'prgm.ita.p.dac.ricerche.sede.EseguiRicercaDac.execute';
   	document.getElementById("parametri_idDac").select();
   	startRequest();
	document.ricerca.submit();
	return true;
}

function doRicercaAvanzata(){
	if(document.ricerca.daoAccessName.value == 'ricercaStoricoDacSede')
		document.ricerca.wfemCmd.value = 'prgm.ita.p.dac.ricerche.sede.RicercaStoricoDac.execute';
	else	
		document.ricerca.wfemCmd.value = 'prgm.ita.p.dac.ricerche.sede.RicercaDac.execute';
	document.ricerca.parametri_idDac.value = '';
   	startRequest();
	wfemHiddenSubmit(document.ricerca,'toolbarCont,elencoCont,legenda');
	return true;
}

function onNewCell(cell){
	if(cell.propertyName == 'idDac' ||
	   cell.propertyName == 'dataOraCambioStato' ||
	   cell.propertyName == 'codUtenteIns' ||
	   cell.propertyName == 'codUtenteUpd')
	   cell.align = 'center';
	
	if(cell.propertyName == 'tipoDac'){
		cell.onmouseover = function(){event.cancelBubble = true;};
		cell.onmouseout = function(){event.cancelBubble = true;};
		cell.title = '';
		cell.innerHTML = '<table height="100%" width="100%"><tr><td bgcolor="'+document.getElementById("dacColor"+cell.row.tipoDac).value+'"></td></tr></table>';
	}
	
	if(cell.propertyName == 'descrUffLavorazione'){
		if(cell.row.descrUffSpunta != ''){
			cell.innerHTML = cell.row.descrUffSpunta;
			cell.title = cell.row.descrUffSpunta; 
		}
	}
}

var curDac=null;
function selectDac(dac){
	enableAction("apriAction",true);
	enableAction("stampaAction",false);
	if(dac.stato != '1')
		enableAction("stampaAction",true);
	curDac = dac;
}

function doApriAction(){
	document.dati.idDac.value = curDac.idDac;
	document.dati.submit();
	return true;
}

function doStampaAction(){
	if(curDac.tipoDac == '5')
		stampaDac(curDac.idDac,true);
	else	
		stampaDac(curDac.idDac,false);
}

function loadOperazioni(){
	startRequest();
	document.ricerca.wfemCmd.value = 'prgm.ita.p.dac.ricerche.sede.LoadOperazioni.execute';
	var combo = document.getElementById("parametri_codOperazione");
	combo.disabled=true;
	combo.value='';
	combo.options(0).text='Caricamento in corso...';
	wfemHiddenSubmit(document.ricerca,'codOperazioneCont');
}


// Gestione tabbettini
var currentJSTabId = null;
function selectJSTab(tabContainerName,tabNum){
	
	currentJSTabId = tabContainerName+tabNum;

	document.dati.tabNum.value = tabNum;
	document.ricerca.tabNum.value = tabNum;

	for(var i=0;i<2;i++){
		var tmpTab = document.all(tabContainerName).all(tabContainerName+i);
		tmpTab.className = "htab";
		document.all(tabContainerName).all(tabContainerName+i+"El").style.display = "none";
	}
	
	document.all(tabContainerName).all(currentJSTabId).className = "htabSelected";
	document.all(tabContainerName).all(currentJSTabId+"El").style.display = "inline";
	
	if(currentJSTabId == 'JSTab0'){
		document.getElementById("pulisciCampiRicercaDacCont").style.visibility='hidden';
		try{
			document.getElementById("parametri_idDac").focus();
			document.getElementById("parametri_idDac").select();
		}catch(e){}
	}else{
		document.getElementById("pulisciCampiRicercaDacCont").style.visibility='visible';
	}
}

