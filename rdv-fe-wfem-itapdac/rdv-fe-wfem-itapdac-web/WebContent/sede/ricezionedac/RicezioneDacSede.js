var docVisibile=false;
var docPlichiVisibile=false;
var docRicevuti=0;
var docNonPervenuti=0;
var docInAttesaResponse = 0;

document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    try {
			doCercaDocumento();
		} catch (e) { }
	}
}

function focusOnBarcode(){
	try{
		document.frmDocSparato.barcode.select();
	}catch(e){}	
}

function onWfemHiddenSubmitEnd(){
	document.frmDocSparato.barcode.readOnly = false;
	
	focusOnBarcode();
}

/*
	Init celle documento
*/
function onNewCell(cell){
	var docSparato = isDocumentoSparato (cell.row);
	var rigaDisabilitata = document.datiRead.readonly.value=="true" || cell.row.isErroreSmistamento=="true";
	var docInPlico = cell.row.codAggregatore!="";
	var docFiglioInPlico = cell.row.codAggregatore!="" && cell.row.isFirstInPlico=="false";		
	
	if(cell.propertyName == 'barcode') {
		cell.align = 'center';
	}
	
	if(cell.row.codProdotto == '0') { // Assegno
		if(cell.propertyName == 'descrProdotto')
			cell.innerHTML = "Assegno n. "+cell.row.numeroContratto;
		if(cell.propertyName == 'numeroContratto' ||
		   cell.propertyName == 'descrOperazione')
			cell.innerHTML = "&nbsp;";
	}		
	
	if(cell.propertyName == 'idPlico') {
		gestioneCellePlichi(cell);
	} else if(cell.propertyName != 'nonPervenuto' && cell.propertyName != 'isErroreSmistamento') {
	
		if (docSparato)
			configuraCellaDocumentoSparato(cell.row, cell);
			
	} else {
		//Caratteristiche delle celle strumento
		configuraCellaStrumentoDocSparato (cell);
		
		if (cell.propertyName == 'isErroreSmistamento') {
		
			if (cell.row.isErroreSmistamento == "true" && document.datiRead.readonly.value=="false") {
				var onClickAction = "onclick=\"checkBlocco('"+ cell.row.barcode +"');\"";
				cell.innerHTML = "<table height='100%' width='100%' cellpadding='5px;' cellspacing='0' bgcolor='white'><tr><td id='isErroreSmistamento"+cell.row.idDocumento+"' align='center'><img src='"+__retrieveResourceUrl()+"/images/checkBloccoDocumento.png' style='cursor: hand;' title='Verifica blocco' "+onClickAction+"></td></tr></table>";
			} else {
				cell.innerHTML = "<table height='100%' width='100%' cellpadding='5px;' cellspacing='0' bgcolor='white'><tr><td id='isErroreSmistamento"+cell.row.idDocumento+"'></td></tr></table>";
			}
			
		} else if (cell.propertyName == 'nonPervenuto') {
		
			if (cell.row.aggiuntoInRicezione=="true") {
				cell.innerHTML = "<table height='100%' width='100%' cellpadding='5px;' cellspacing='0' bgcolor='white'><tr><td></td></tr></table>";
			} else {
				cell.style.background = "url("+__retrieveResourceUrl()+"/images/bgWhite.gif)";
				
				var field = document.getElementById("documenti"+cell.row.absIndex+"_nonPervenutoCheck");
				field.onclick = function() { documentoNonPervenuto(cell.row.absIndex, cell.row.idDocumento, false, cell.row.ubicazione, cell.row.idDac, docInPlico, cell.row.isInBusta); };

				if ((cell.row.ubicazione != document.datiRead.constUbicazioneInViaggio.value && 
						cell.row.idDac == document.datiRead.constIdDacXDocFuoriDac.value)
						|| rigaDisabilitata) {
						
					enableField("documenti"+cell.row.absIndex+"_nonPervenuto", false);
				}
			}
			
		}
	}
}
/*
	Imposta lo stile delle celle STRUMENTI di un documento ricevuto
*/
function configuraCellaStrumentoDocSparato (cell) {
	cell.style.padding = '0px';
	cell.style.cursor = "default";
	cell.onclick = function(){ event.cancelBubble = true; }
	cell.onmouseover = function(){ event.cancelBubble = true; }
	cell.onmouseout = function(){ event.cancelBubble = true; }
}
/*
	Imposta lo stile delle celle di un documento ricevuto
*/
function configuraCellaDocumentoSparato (row, cella) {
	if (row.isErroreSmistamento=="true")
		return;

	cella.style.color = "#1A458F";
	cella.style.backgroundColor = "yellowgreen";
	cella.onmouseout = function(){ applicaStileCelleRigaSelezionata(row); }
	cella.onclick= function(){ event.cancelBubble = true; doVisualizzaDoc(row); }
}
/*
	Gestione onmouseout delle celle di un documento ricevuto
*/
function applicaStileCelleRigaSelezionata (row) {
	event.cancelBubble = true;
	
	for(var k=0;k<row.cells.length;k++) {
		var cell = row.cells[k];
		
		if(cell.propertyName == 'idPlico')
			continue;

		if(cell.propertyName != 'nonPervenuto' && cell.propertyName != 'isErroreSmistamento') {
			cell.style.color = "#1A458F";
			cell.style.backgroundColor = "yellowgreen";
		}
	}
}

/*
	Torna alla ricerca da cui si è arrivati
*/
function doGoback(){
	if (document.datiRead.refreshable.value == "true"){
		document.gobackForm.doSearch.value = 'true';
	}else{
		document.gobackForm.doSearch.value = 'false';
	}
	document.gobackForm.submit();
	return true;
}

/*
	Salva la DAC corrente
*/
function doSalvaDac() {
	document.frmTestataDac.wfemCmd.value = "prgm.ita.p.dac.business.SalvaDac.execute";
	wfemHiddenSubmit(document.frmTestataDac, "none");
}

/*
	Controlli e chiusura della DAC corrente
*/
function doChiudiDac(){
	if(docVisibile){
		alert("Chiudere il dettaglio del documento");
		return;
	}
	if (docPlichiVisibile){
		alert("Chiudere l'elenco plichi/documenti");
		return;
	}
	if (parseInt(docInAttesaResponse,10)>0) {
		alert("Attendere che termini l'aggiornamento di tutti i documenti");
		return;
	}

	if (!confirm("Chiusura DAC: continuare?"))
		return;

	document.documentiForm.noteOperatore.value = document.frmTestataDac.noteOperatore.value;
	document.documentiForm.wfemCmd.value='prgm.ita.p.dac.sede.ricezionedac.ChiudiDac.execute';	
	document.documentiForm.submit();
	return true;
}

/*
	Visualizza il documento selezionato
*/
function doVisualizzaDoc(doc){
	docVisibile=true;
	document.documentoForm.documento_idDocumento.value = doc.idDocumento;
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.ApriDocumento.execute';
	startRequest();
	wfemHiddenSubmit(document.documentoForm,'docCont,finalsDocScriptsCont');
}

/*
	Chiude il documento selezionato
*/
function chiudiDocumento(){
	docVisibile=false;

	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.ChiudiDocumento.execute';
	startRequest();
	wfemHiddenSubmit(document.documentoForm,'docCont,finalsDocScriptsCont');
}

/*
	In ricezione un documento è considerato sparato se :
			- Non ha più ubicazione "in viaggio (-1)"
			- Id della DAC corrente uguale alla costante "Id Fuori DAC"
*/
function isDocumentoSparato (documento) {
	return documento.ubicazione != document.datiRead.constUbicazioneInViaggio.value && documento.idDac == document.datiRead.constIdDacXDocFuoriDac.value;
}

/*
	In ricezione un documento è considerato non ricevuto se:
			- Ha il check di "Non pervenuto" spuntato
*/
function isDocumentoNonRicevuto (documento) {
	return document.getElementById("documenti" + documento.absIndex + "_nonPervenutoCheck").checked;
}

/*
	Operazioni dopo aver sparato il barcode del documento
*/
function doCercaDocumento(){
	var barcode = trim(document.frmDocSparato.barcode.value);
	var indiceDocTrovato = -1;
	
	//Controlli preliminari
	if (barcode == "")	
		return;
	if(docVisibile){
		alert("Chiudere il dettaglio del documento");
		return;
	}

	//Ciclo sui documenti per cercare il barcode sparato
	var rows = document.documentiGrid.tableModel.rows;
	var documento = null;
	for(var i=0;i<rows.length;i++){
		var row = rows[i];

		if(row.barcode == barcode) {
			documento = row;
			indiceDocTrovato = i;
			break;
		}
	}
	
	//Se il documento non è presente nella lista viene automaticamente aggiunto alla DAC
	if (documento == null) {
		aggiungiDocumento(barcode);
		return;
	}

	/************ DOCUMENTO PRESENTE IN LISTA ***************/
	
	document.documentiGrid.showRow(indiceDocTrovato);	//Mi posiziono sulla riga del documento
	
	focusOnBarcode();	//Fuoco sul barcode per essere pronto ad un'altra sparata
	if (documento.isErroreSmistamento=="true")
		return;	//Documento aggiunto ma bloccato in quanto presente in una DAC ricevuta da un altro destinatario --> Nessuna operazione

	if (isDocumentoSparato(documento))
		return;	//Se è già stato sparato --> nessuna operazione

	//Controllo che se il documento ha il check di "Non pervenuto" non sia già stato spuntato
	var mgmPlichi = document.datiRead.mgmPlichi.value=="true";
	var docPadreInPlico = documento.codAggregatore!="" && documento.isFirstInPlico=="true";
	var docNonInPlico = documento.codAggregatore=="";
	var docConCheckNonPervenuto = !mgmPlichi || docNonInPlico || (mgmPlichi && docPadreInPlico);
	if (docConCheckNonPervenuto && document.getElementById("documenti"+i+"_nonPervenutoCheck").checked)
		return;	//Documento già segnato come non pervenuto --> nessuna operazione

	//Nascondo eventuali errori sul check di non pervenuto
	if (docConCheckNonPervenuto)
		hideHelperAnchor('documenti' + documento.absIndex + '_nonPervenuto');

	//Aggiorno i dati del documento per segnarlo come "Ricevuto"
	enableField("documenti"+ indiceDocTrovato +"_nonPervenuto", false);
	docInAttesaResponse++;
	document.getElementById("isErroreSmistamento"+ documento.idDocumento).innerHTML = "<img src='" + document.datiRead.imgPath.value + "wait.gif'>";

	document.aggiornaDocInRicezione.idDocumento.value = documento.idDocumento;
	document.aggiornaDocInRicezione.idDac.value = document.datiRead.constIdDacXDocFuoriDac.value;
	document.aggiornaDocInRicezione.nonPervenuto.value = "false";
	document.aggiornaDocInRicezione.isInBusta.value = documento.isInBusta;
	document.aggiornaDocInRicezione.ubicazione.value = document.datiRead.ufficio.value;

	startRequest();
	document.frmDocSparato.barcode.readOnly = true;
	wfemHiddenSubmit(document.aggiornaDocInRicezione, 'scriptCode');
}

/*
	Aggiunge un documento non presente nell'elenco
*/
function aggiungiDocumento(barcode){
	//Controlli preliminari
	if (parseInt(docInAttesaResponse,10) > 0) {
		alert("Attendere che termini l'aggiornamento di tutti i documenti");
		return;
	}

	if (!confirm("Il documento con barcode " + barcode + " non e' presente nell'elenco e verra' aggiunto alla DAC. Continuare?"))
		return;
	
	document.frmDocDaAggiungere.wfemCmd.value = "prgm.ita.p.dac.sede.ricezionedac.AggiungiDocInRicezione.execute";
	document.frmDocDaAggiungere.documento_barcode.value = barcode;

	startRequest();
	document.frmDocSparato.barcode.readOnly = true;
	wfemHiddenSubmit(document.frmDocDaAggiungere, 'erroriDoc,documentiCont');
	return;
}
/*
	Aggiorna il documento come "non pervenuto"
*/
function documentoNonPervenuto (index, idDoc, sparato, ubicazione, idDac, docInPlico, isInBusta) {
	hideHelperAnchor("documenti"+index+"_nonPervenuto");
	
	if (docInPlico) {
		alert("Spinzare il plico prima di segnare come non pervenuto il documento");
		clearBoolField("documenti"+index+"_nonPervenuto");
		return;
	}
		
	docInAttesaResponse++;
	document.getElementById("isErroreSmistamento"+idDoc).innerHTML = "<img src='"+document.datiRead.imgPath.value+"wait.gif'>";

	document.aggiornaDocInRicezione.idDac.value = document.datiRead.idDac.value;
	document.aggiornaDocInRicezione.idDocumento.value = idDoc;
	document.aggiornaDocInRicezione.nonPervenuto.value = document.getElementById("documenti"+index+"_nonPervenutoCheck").checked;
	document.aggiornaDocInRicezione.ubicazione.value = document.datiRead.constUbicazioneInViaggio.value;
	document.aggiornaDocInRicezione.isInBusta.value = isInBusta;

	//enableField("documenti"+index+"_nonPervenuto", false);

	startRequest();	
	wfemHiddenSubmit(document.aggiornaDocInRicezione,'scriptCode');
}
/*
	Funzione chiamata dopo la risposta del server
*/
function onRiceviDocumentoEnd(idDocumento, idDacCorrente, ubicazione){
	stopRequest();
		
	var indiceDocTrovato = -1;
		
	docInAttesaResponse--;
	document.getElementById("isErroreSmistamento" + idDocumento).innerHTML = "<table height='100%' width='100%' cellpadding='5px;' cellspacing='0' bgcolor='white'><tr><td id='isErroreSmistamento"+idDocumento+"'></td></tr></table>";
	
	var rows = document.documentiGrid.tableModel.rows;
	var documento = null;
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		
		if(row.idDocumento == idDocumento){
			documento = row;
			indiceDocTrovato = i;
			break;
		}
	}

	if (documento == null)
		throw ("Alert: documento con id = " + idDocumento + " non e' stato trovato nell'elenco");

	document.all("BGSOUND_ID").src = __retrieveResourceUrl()+"/resources/sounds/chimes.wav";	//Sound OK

	//Aggiorno i dati della riga 
	documento.idDac = idDacCorrente;
	documento.ubicazione = ubicazione;

	if (isDocumentoSparato(documento)) {
		//Il documento era stato segnato come "ricevuto". Aggiorno il contatore
		docRicevuti++;
		document.getElementById("nDocRicevuti").innerHTML = docRicevuti;
		
		//Coloro la riga
		for(var j=0;j<documento.cells.length;j++){
			var cell = documento.cells[j];
			
			if(cell.propertyName == 'idPlico')
				continue;
			
			if (cell.propertyName == 'nonPervenuto'  || cell.propertyName == 'isErroreSmistamento')
				configuraCellaStrumentoDocSparato(cell);
			else
				configuraCellaDocumentoSparato(documento, cell);
		}
	} else {
		//Il documento era stato segnato come "non pervenuto". Aggiorno il contatore

		enableField("documenti" + indiceDocTrovato + "_nonPervenuto", true);
		
		if (document.getElementById("documenti" + indiceDocTrovato + "_nonPervenutoCheck").checked)
			docNonPervenuti++;
		else
			docNonPervenuti--;
		document.getElementById("nDocNonPervenuti").innerHTML = docNonPervenuti;
	}
}

/*
	Controlla se il documento "in errore smistamento" risulta essere sbloccato (segnato come "non pervenuto" e chiusa la DAC che lo contiene)
*/
function checkBlocco(barcode) {
	document.frmDocDaAggiungere.wfemCmd.value = "prgm.ita.p.dac.sede.ricezionedac.VerificaBloccoDocumento.execute";
	document.frmDocDaAggiungere.documento_barcode.value = barcode;

	startRequest();
	wfemHiddenSubmit(document.frmDocDaAggiungere, 'documentiCont');
}