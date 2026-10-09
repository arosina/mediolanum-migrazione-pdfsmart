function setTipoRicerca(radio){
	document.ricerca.tipoRicerca.value = radio.value;
	try{
		clearDateField("parametri_dataInizio");
		clearDateField("parametri_dataFine");
	}catch(e){}
}

function pulisciCampiRicercaDoc(){
	var f = document.ricerca;
	
	f.parametri_statoDoc.value = '';
	f.parametri_ubicazioneDoc.value = '';
	f.parametri_esitoDoc.value = '';
	f.parametri_codProdotto.value = '';
	clearDateField('parametri_dataInizio');
	clearDateField('parametri_dataFine');
	
	f.parametri_codMediolanum.value = '';
	f.parametri_cognomeCliente.value = '';
	f.parametri_nomeCliente.value = '';
	
	if(document.getElementById("parametri_codiceAgente") != null)
		f.parametri_codiceAgente.value = '';
	
	f.parametri_numeroContratto.value = '';
	f.parametri_importo.value = '';
	f.parametri_numeroAssegno.value = '';
	
	loadOperazioni();	
}

function doEnterAction(){
	document.getElementById("ricercaAction").focus();
	return doRicercaAction();
}

function popupRicercaCliente(){
	showPopupClienti();
}

function showPopupClientiEnd(res) {
	if(res == null)
		return;	
	
	if(document.getElementById("parametri_codiceAgente") != null)
		document.getElementById("parametri_codiceAgente").value = res.agente_codAgente;
		
	if(res.type == 'cliente'){	
		var cli = res;	
		document.getElementById("parametri_numeroContratto").value = '';
		document.getElementById("parametri_codMediolanum").value = cli.codMediolanum;
		document.getElementById("parametri_cognomeCliente").value = cli.cognome;
		document.getElementById("parametri_nomeCliente").value = cli.nome;
	}else if(res.type == 'contratto'){	
		var contr = res;
		document.getElementById("parametri_numeroContratto").value = contr.numeroContratto;
		document.getElementById("parametri_codMediolanum").value = contr.cliente_codMediolanum;
		document.getElementById("parametri_cognomeCliente").value = contr.cliente_cognome;
		document.getElementById("parametri_nomeCliente").value = contr.cliente_nome;
	}
}

function popupRicercaAgente(){
	showPopupAgenti();
}

function showPopupAgentiEnd(age){
	if(age == null)
		return;
	document.getElementById("parametri_codiceAgente").value = age.codAgente;
	document.getElementById("parametri_numeroContratto").value = '';
	document.getElementById("parametri_codMediolanum").value = '';
	document.getElementById("parametri_cognomeCliente").value = '';
	document.getElementById("parametri_nomeCliente").value = '';
}

function doRicercaAction(){
	if(currentJSTabId == null)
		return;
			
	if(document.ricerca.daoAccessName.value == 'ricercaStoricoDoc')
		document.ricerca.wfemCmd.value = 'prgm.ita.p.dac.ricerche.sede.RicercaStoricoDoc.execute';
	else
		document.ricerca.wfemCmd.value = 'prgm.ita.p.dac.ricerche.sede.RicercaDoc.execute';
		
	document.ricerca.doSearch.value = 'true';
	
	if(currentJSTabId == 'JSRicercaDocTab0')
		return doRicercaUno();
	else
		return doRicercaAvanzata();
}

function doRicercaUno(){
	if(document.ricerca.parametri_barcode.value == ''){
		document.getElementById("parametri_barcode").focus();
		document.getElementById("parametri_barcode").select();
		return false;
	}
	
   	document.getElementById("parametri_barcode").select();
	startRequest();
	resetIframeFirme();

	wfemHiddenSubmit(document.ricerca,'divFrmDati,elencoCont,docCont');
	return true;
}

function isCampoObbRicercaAvanzataCompilato() {
	var isDataInizioCompilato = document.getElementById("parametri_dataInizio").value;
	var isDataFineCompilato = document.getElementById("parametri_dataFine").value;
	var isCodMediolanumCompilato = trim(document.getElementById("parametri_codMediolanum").value);
	var isNumeroContrattoCompilato = trim(document.getElementById("parametri_numeroContratto").value);
	var isCognomeClienteCompilato = trim(document.getElementById("parametri_cognomeCliente").value);
	var isNomeClienteCompilato = trim(document.getElementById("parametri_nomeCliente").value);		
	var isImportoCompilato = document.getElementById("parametri_importo").value;
	var isNumeroAssegnoCompilato = trim(document.getElementById("parametri_numeroAssegno").value);
	
	var isCodiceAgenteCompilato = false;
	if (isSede)
		isCodiceAgenteCompilato = trim(document.getElementById("parametri_codiceAgente").value);


	if (isDataInizioCompilato || isDataFineCompilato || isCodMediolanumCompilato || isNumeroContrattoCompilato || isCognomeClienteCompilato ||
			isNomeClienteCompilato || isImportoCompilato || isNumeroAssegnoCompilato || isCodiceAgenteCompilato)
			return true;
			
	return false;
}

function doRicercaAvanzata(){
	document.ricerca.parametri_barcode.value = '';
	
	if (!isCampoObbRicercaAvanzataCompilato()) {
		alert("Compilare almeno un campo contrassegnato con (*)");
		return false;
	}
	
   	startRequest();
	resetIframeFirme();
	wfemHiddenSubmit(document.ricerca,'body');
	return true;
}

function onNewCell(cell){
	if(cell.propertyName == 'barcode' ||
	   cell.propertyName == 'numeroContratto' ||
	   cell.propertyName == 'dataOraCambioStato')
	   cell.align = 'center';
}

function selectDoc(doc){
	resetIframeFirme();
	startRequest();
	document.dati.documento_idDocumento.value = doc.idDocumento;
	wfemHiddenSubmit(document.dati,'docCont,finalsDocScriptsCont')	
}

function loadOperazioni(){
	startRequest();
	document.ricerca.wfemCmd.value = 'prgm.ita.p.dac.ricerche.sede.LoadOperazioni.execute';
	var combo = document.getElementById("parametri_codOperazione");
	combo.disabled=true;
	combo.value='';
	combo.options[0].text='Caricamento in corso...';
	wfemHiddenSubmit(document.ricerca,'codOperazioneCont');
}

function chiudiDocumento(){
	document.getElementById("documentoCont").style.display='none';
}

// Gestione tabbettini
var currentJSTabId = null;
function selectRicercaDocJSTab(tabContainerName,tabNum){
	
	currentJSTabId = tabContainerName+tabNum;

	document.dati.tabNum.value = tabNum;
	document.ricerca.tabNum.value = tabNum;

	for(var i=0;i<2;i++){
		//var tmpTab = document.all(tabContainerName).all(tabContainerName+i);
		//tmpTab.className = "htab";
		//document.all(tabContainerName).all(tabContainerName+i+"El").style.display = "none";

		var tmpTab = document.getElementById(tabContainerName+i);
		tmpTab.className = "htab";
		document.getElementById(tabContainerName+i+"El").style.display = "none";
	}
	
	//document.all(tabContainerName).all(currentJSTabId).className = "htabSelected";
	//document.all(tabContainerName).all(currentJSTabId+"El").style.display = "inline";
	document.getElementById(currentJSTabId).className = "htabSelected";
	document.getElementById(currentJSTabId+"El").style.display = "inline";
		
	if(currentJSTabId == 'JSRicercaDocTab0'){
		document.getElementById("pulisciCampiRicercaDocCont").style.visibility='hidden';
		try{document.getElementById("msgCampoObb").style.visibility='hidden';}catch(e){}		
		try{
			document.getElementById("parametri_barcode").focus();
			document.getElementById("parametri_barcode").select();
		}catch(e){}
	}else{
		document.getElementById("pulisciCampiRicercaDocCont").style.visibility='visible';
		try{document.getElementById("msgCampoObb").style.visibility='visible';}catch(e){}	
	}
}

