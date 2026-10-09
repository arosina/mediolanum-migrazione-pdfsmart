var isDacSpuntabile=false;
var rowAssegniHeight = '20px';

function doNuovaDacDopoInvia(){
	document.nuovaDacDopoInviaForm.submit();
	return true;
}

function doSalvaDac(){
	document.datiRead.refreshable.value = 'true';
	document.dati.wfemCmd.value = 'prgm.ita.p.dac.business.SalvaDac.execute';
	wfemHiddenSubmit(document.dati,'none');
	return true;
}

function doInviaInSedeDac(){
	testChangeDoc('yesno','Il documento &egrave; stato variato<br>Le modifiche non verranno considerate.<br>Desideri comunque procedere con l\'invio ?',doInviaInSedeDacEnd1);
	return;	
}
function doInviaInSedeDacEnd1(ret){
	if(ret == 'continue' || ret == 'yes')
		showPopupMsg('yesno','Confermi l\'invio in sede ?',doInviaInSedeDacEnd2);
	return;	
}
function doInviaInSedeDacEnd2(ret){
	if(ret == 'no' || ret == 'cancel')
		return;
	startRequest();
	document.datiRead.refreshable.value = 'true';
	document.documentoForm.documento_idDocModificato.value = '';
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.InviaDac.execute';
	document.documentoForm.submit();
	return;	
}

function doSpuntaDac(){
	var ret = testChangeDoc('yesno','Il documento &egrave; stato variato<br>Le modifiche non verranno considerate.<br>Desideri comunque procedere con la spunta ?');
	if(ret == 'continue' || ret == 'yes'){
		startRequest();
		document.datiRead.refreshable.value = 'true';
		document.documentoForm.documento_idDocModificato.value = '';
		document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.SpuntaDac.execute';
		document.documentoForm.submit();
	}
	return;	
}

function doStampaDac(){
	if(document.datiRead.tipoDac.value == '5')
		stampaDac(document.datiRead.idDac.value,true);
	else
		stampaDac(document.datiRead.idDac.value,false);
}

function doGoback(){
	if(document.documentoForm.documento_idDocModificato.value != ''){
		var ret = showPopupMsg('yesno','Le modifiche apportate al documento verranno perse.<br>Confermi ?');
		if(ret != 'yes')
			return;
	}
	if (document.datiRead.refreshable.value == "true"){
		document.gobackForm.doSearch.value = 'true';
	}else{
		document.gobackForm.doSearch.value = 'false';
	}
	document.gobackForm.submit();
	return true;
}

function showHelpStato(){
	document.getElementById("helpStato").style.display='inline';
}

function hideHelpStato(){
	document.getElementById("helpStato").style.display='none';
}

function onNewHeaderDac(cell){
	if(cell.propertyName == 'imgFase'){
		cell.innerHTML = "(<span style='text-decoration:underline;cursor:pointer;' onclick='showHelpStato();'>?</span>)";
		cell.title = "Clicca per un aiuto sugli stati dei documenti";
	}
}

function onNewRowDac(doc){
	innerOnNewRowDac(doc,false);
}

function onNewRowQuickDac(doc){
	innerOnNewRowDac(doc,true);	
}

function innerOnNewRowDac(doc,isQuick){
	if(isQuick || document.datiRead.isDacSede.value == "true")
		doc.statoRiga = "";
	else
		doc.statoRiga = getStatoRiga(doc);
	if(doc.codProdotto == '0'){
		doc.style.fontStyle = 'italic';
		doc.style.fontSize = '10';
		doc.style.height = rowAssegniHeight;
		try{
			var counterTable = document.getElementById("documentiGridTabFixColumn");
			var counter = counterTable.rows[doc.getAttribute("absIndex")].cells[0];
			counter.style.height = rowAssegniHeight;
		}catch(e){}
	}
}

function getStatoRiga(row){
	if(row.esito != ''){
		if(row.esito == '7')
			return "docEsitato";
		
		if(row.hasControlloFirmeClienteAttivo == 'true' || row.hasControlloFirmeAgenteAttivo == 'true'){
			if((row.hasControlloFirmeClienteAttivo == 'true' && row.esitoFirmaCliente == '') || 
			   (row.hasControlloFirmeAgenteAttivo == 'true' && row.esitoFirmaAgente == '')){
				return "firmaDaEsitare";
			}else{
				return "docFirmaEsitati";
			}	
		}else if(row.esitoFirmaCliente != '' || row.esitoFirmaAgente != ''){
				return "docFirmaEsitati";
		}else{
			return "docEsitato";
		}
	}else{
		return "docDaEsitare";
	}
	return "";
}

function onNewCellDac(cell){
	innerOnNewCellDac(cell,false);
}
function onNewCellQuickDac(cell){
	innerOnNewCellDac(cell,true);	
}

function innerOnNewCellDac(cell,isQuick){
	if(cell.propertyName == 'idPlico' ||
	   cell.propertyName == 'numeroContratto' ||
	   cell.propertyName == 'codInforeteEsterno' ||
	   cell.propertyName == 'agente_codAgente' ||
	   cell.propertyName == 'barcode' ||
	   cell.propertyName == 'descrEsito')
	   cell.align='center';
	   
	if(cell.propertyName == 'numeroContratto' &&
	   cell.row.numeroContratto == cell.row.codInforeteEsterno){
		cell.title = '';
		cell.innerHTML = '&nbsp;';
	}
	
	if(cell.propertyName == 'imgFase'){
		var imgName = "";
		var imgTitle = "";
		if(cell.row.statoRiga == 'firmaDaEsitare'){
			imgTitle = 'Firma da esitare';
			imgName = __retrieveResourceUrl()+'/images/docFirmaDaEsitare.gif';
		}else if(cell.row.statoRiga == 'docFirmaEsitati'){
			imgTitle = 'Documento e Firma esitati';
			imgName = __retrieveResourceUrl()+'/images/docEsitatoConFirma.png';
		}else if(cell.row.statoRiga == 'docEsitato'){
			imgTitle = 'Documento esitato';
			imgName = __retrieveResourceUrl()+'/images/docEsitato.png';
		}else if(cell.row.statoRiga == 'docDaEsitare'){
			imgTitle = 'Documento da esitare';
			imgName = __retrieveResourceUrl()+'/images/docDaEsitare.png';
		}
		cell.onmouseover = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
		cell.onmouseout = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
		cell.onclick = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
		cell.style.cursor = 'default';
		cell.style.padding = '0px';
		var inhtml =
			"<table class='text' width='100%' height='100%' cellpadding='0' cellspacing='0' style='background-color:white;'>" +
			"  <tr>"+
			"   <td align='center'>";
		if(imgName != null)
			inhtml += "      <img src='"+imgName+"' title='"+imgTitle+"'>";
		inhtml +=
			"   </td>" +
			"  </tr>" +
			"</table>";
		cell.innerHTML = inhtml;
	}
	
	// Gestione assegni
	if(cell.row.codProdotto == '0'){
		
		cell.style.height = rowAssegniHeight;

		var cellaAssegno = false;
		if(cell.propertyName == 'numeroContratto' || cell.propertyName == 'codInforeteEsterno' ||
		   cell.propertyName == 'descrOperazione' || cell.propertyName == 'descrEsito' ||
		   cell.propertyName == 'agente_codAgente' || cell.propertyName == 'cliente_nominativo')
		   cellaAssegno = true;
		
		if(cellaAssegno)
			cell.style.backgroundColor = '#ececff';
		
		if(cellaAssegno && cell.propertyName != 'cliente_nominativo')
			cell.style.borderRight = 'none';
		
		if(cell.propertyName == 'descrOperazione')
			cell.innerHTML = "Assegno n. "+cell.row.numeroContratto;
	
		if(cell.propertyName == 'descrProdotto' ||
		   cell.propertyName == 'numeroContratto' || cell.propertyName == 'codInforeteEsterno' || 
		   cell.propertyName == 'agente_codAgente' || cell.propertyName == 'cliente_nominativo')
			cell.innerHTML = '&nbsp;';
			
		if(cell.propertyName == 'descrEsito'){
			if(cell.row.esito == '5')
				cell.innerHTML = 'Doc. Man. Segn.';
			if(cell.row.esito == '7')
				cell.innerHTML = 'Doppia ann.';
		}			
	}
	if (document.datiRead.isDacSede.value == "false" && document.datiRead.tipoDac.value != '5') {
		if(document.datiRead.isPromotore.value != 'true' && !isQuick){
			if(cell.row.statoRiga == 'firmaDaEsitare'){
				cell.style.backgroundColor = 'khaki';
			}else if(cell.row.statoRiga == 'docFirmaEsitati'){
				cell.style.color = 'darkgoldenrod';
				cell.style.backgroundColor = '#CF9';
			}else if(cell.row.statoRiga == 'docEsitato'){
				cell.style.backgroundColor = '#CF9';
			}
		}
	} else {
		//DAC di sede
		var isDacInLavorazione = (document.datiRead.isDacInLavorazione.value=="true");
		var isDacLavorata = (document.datiRead.isDacLavorata.value=="true");
		var isAggiuntoInRicezione = (cell.row.aggiuntoInRicezione=="true");
		var isNonPervenuto = (cell.row.nonPervenuto=="true");
		var idDacCorrente = cell.row.idDac;
		var isDocRicevuto = (idDacCorrente == document.datiRead.constIdDacDocFuoriDac.value);
	
		if (isDacLavorata) {
			if (!isAggiuntoInRicezione && !isNonPervenuto) {
				//Ricevuto correttamente in ricezione
				cell.style.backgroundColor = '#CF9';
			} else if (isAggiuntoInRicezione) {
				//Aggiunto in ricezione
				cell.style.backgroundColor = '#FFFA00';
			} else if (isNonPervenuto) {
				//Documento non pervenuto
				cell.style.color = 'white';
				cell.style.backgroundColor = '#A62D05';			
			}
		} else if (isDacInLavorazione) {
			//Il non pervenuto non è identificabile fino alla chiusura della DAC
			
			if (isDocRicevuto && !isAggiuntoInRicezione) {
				//Ricevuto correttamente in ricezione
				cell.style.backgroundColor = '#CF9';
			} else if (isAggiuntoInRicezione) {
				//Aggiunto in ricezione
				cell.style.backgroundColor = '#FFFA00';
			}
		}
	}
	
	if(cell.propertyName == 'barcode'){
		if(document.datiRead.isGestoreBarcode.value == 'true' &&
		   cell.row.barcode == '' &&
		   document.datiRead.readonly.value != 'true' &&
		   (cell.row.esito != '4' && cell.row.esito != '5' && cell.row.esito != '7')){
			cell.title = "Per il documento va inserito il barcode";
			cell.style.color='red';
			cell.style.fontStyle = 'italic';
			cell.innerHTML = "Da inserire";
		}
	} 

	gestioneCellePlichi(cell);
}

var docVisibile=false;
function doInserisciDocumenti(){
	testChangeDoc(null,null,doInserisciDocumentiEnd);
}
function doInserisciDocumentiEnd(ret){
	if(ret == 'cancel')
		return;
	if(ret == 'no')
		document.documentoForm.documento_idDocModificato.value = '';
		
	startRequest();
	docVisibile=true;
	try{
		document.documentiGrid.gridListSelection.unselectRows();
	}catch(e){}
	document.documentoForm.documento_idDocumento.value = '';
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.InserisciDocumenti.execute';
	enableAction("clonaDocumento",false);
	if(ret == 'yes')
		refreshAll();
	else	
		refreshDoc();
}

var openingDoc;
function apriDocumento(doc){
	if(curDoc != null && curDoc == doc.idDocumento)
		return;
	openingDoc = doc;
	testChangeDoc(null,null,apriDocumentoEnd);
}
function apriDocumentoEnd(ret){
	if(ret == 'cancel'){
		selezionaDocumento(curDoc);
		return;
	}
	if(ret == 'no')
		document.documentoForm.documento_idDocModificato.value = '';
		
	curDoc = openingDoc.idDocumento;
	startRequest();
	docVisibile=true;
	if(openingDoc.codProdotto == '0')
		enableAction("clonaDocumento",false);
	else
		enableAction("clonaDocumento",true);
	document.documentoForm.documento_idDocumento.value = openingDoc.idDocumento;
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.ApriDocumento.execute';
	if(ret == 'yes')
		refreshAll();
	else	
		refreshDoc();
}

function doClonaDocumento(){
	if(curDoc == null){
		alert("Selezionare il documento da duplicare");
		return;
	}
	testChangeDoc(null,null,doClonaDocumentoEnd);
}
function doClonaDocumentoEnd(ret){
	if(ret == 'cancel')
		return;
	if(ret == 'no')
		document.documentoForm.documento_idDocModificato.value = '';
		
	startRequest();
	docVisibile=true;
	try{
		document.documentiGrid.gridListSelection.unselectRows();
	}catch(e){}
	document.documentoForm.documento_idDocumento.value = curDoc;
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.ClonaDocumento.execute';
	enableAction("clonaDocumento",false);
	if(ret == 'yes')
		refreshAll();
	else	
		refreshDoc();
}

function chiudiDocumento(){
	testChangeDoc(null,null,doChiudiDocumentoEnd);
}
function doChiudiDocumentoEnd(ret){
	if(ret == 'cancel')
		return;
	if(ret == 'no')
		document.documentoForm.documento_idDocModificato.value = '';
		
	docVisibile=false;
	try{
		document.documentiGrid.gridListSelection.unselectRows();
	}catch(e){}
	curDoc = null;
	enableAction("clonaDocumento",false);
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.ChiudiDocumento.execute';
	startRequest();
	if(ret == 'yes')
		refreshAll();
	else	
		refreshDoc();
}

function onWfemHiddenSubmitEnd(){
	hideHelper();
}

function hideHelper(){
	__fieldIntf.curOpenedHelper = null;
	$("#divErrHelper").hide();
	$("#divWarHelper").hide();
}
