var docVisibile=false;
var docPlichiVisibile=false;
var docInElencoConErrore = 0;
var richiestaPendente = 0;

function focusOnBarcode(){
	try{
		document.frmDocSparato.barcode.select();
	}catch(e){}	
}

// ##################### OPERAZIONI SUL DOCUMENTO
function doAggiungiDocumento(barcodeInput){
	//Test valorizzazione Box/Ufficio destinatario
	var boxDest=null;
	var textAlertBoxDest="";
	if (document.datiRead.isSmistatore.value=="true") {
		boxDest = document.frmTestataDac.box;
		textAlertBoxDest = "Selezionare il box prima di aggiungere documenti";

		if (inErroreCtrlCassette) {
			alert("Nessuna cassetta definita per il box selezionato. Impossibile inserire documenti");
			return;
		}		

	} else {
		boxDest = document.frmTestataDac.uffDestinatario;
		textAlertBoxDest = "Selezionare l'ufficio destinatario prima di aggiungere documenti";
	}
	if (boxDest.value=="") {
		alert(textAlertBoxDest);
		return;
	}
	//End - Test valorizzazione Box/Ufficio destinatario

	var barcode = document.frmDocSparato.barcode.value;
	if (barcodeInput!=undefined)
		barcode = barcodeInput;

	focusOnBarcode();
	if (trim(barcode)=="")
		return;

	if(docVisibile){
		alert("Chiudere il dettaglio del documento");
		return;
	}

	if (document.getElementById(barcode) != null)
		return;
			
	//Visualizzo il titolo dell'elenco documenti
	document.getElementById("titoloElencoDoc").style.display='';

	//Aggiungo la riga all'elenco dei documenti
	var esisteContDoc = document.getElementById(barcode+"Cont")!=null;
	var contDoc = null;
	var result = "";	//HTML da aggiungere alla div elenco generale
	
	if (esisteContDoc){
		//Il documento era già stato aggiunto alla DAC (ma poi cancellato). La div contenitore è già presente
		contDoc = document.getElementById(barcode+"Cont")
		contDoc.style.display = '';
	} else {
		//Il documento non è mai stato aggiunto alla DAC. La div contenitore è l'elenco generale dei documenti
		contDoc = document.getElementById("elencoDoc");
		result = "<div id='"+barcode+"Cont' style='border:0;margin:0;width:100%;'>";
	}
	
	//Div del documento da aggiungere
	result +=	"<div id='"+barcode+"' style='border:0;margin:0;width:100%;'>"+
					"<table width='100%' cellspacing='0' cellpadding='0'>"+
					"<tr class='gridDSRigaAttesa'>"+
						"<td class='gridDSCellaDocumento' width='10%' height='18px'>"+barcode+"</td>"+
						"<td class='gridDSCellaAttesa' width='90%' id='wait"+barcode+"' align='right'><img src='"+document.datiRead.imgPath.value+"wait.gif'>&nbsp;</td>"+
					"</tr>"+
					"</table>"+
				"</div>";

	if (!esisteContDoc)
		result += "</div>";
		
	contDoc.innerHTML += result;
	document.frmDocDaInserire.barcode.value = barcode;

	richiestaPendente++;
	startRequest();
	wfemHiddenSubmit(document.frmDocDaInserire, barcode);
	document.frmDocSparato.barcode.readOnly = true;
}

function onAggiungiDocumentoEnd(errore){
	stopRequest();
	document.frmDocSparato.barcode.readOnly = false;
	focusOnBarcode();
	richiestaPendente--;

	if (errore){
		document.all("BGSOUND_ID").src = __retrieveResourceUrl()+"/resources/sounds/glass.wav";
		
		docInElencoConErrore++;
		document.getElementById("tdNumDocErr").style.visibility = "";
		document.getElementById("nDocErr").innerHTML = docInElencoConErrore;
	}else {
		document.all("BGSOUND_ID").src = __retrieveResourceUrl()+"/resources/sounds/chimes.wav";
		
		docInElenco++;
		document.getElementById("nDocIns").innerHTML = docInElenco;
	}

			
	if (parseInt(docInElenco,10)>0) {
		if (document.datiRead.reso.value=="false") {
			//Gestione abilitazione campo destinatario/box

			var field = null;
			if (document.datiRead.isSmistatore.value=="true") {
				field = document.frmTestataDac.box;

				field.disabled = true;
				field.className = 'outputField';
				field.options.style.backgroundColor = '#f7f7f7';
			}

			field = document.frmTestataDac.uffDestinatario;
			
			field.disabled = true;
			field.className = 'outputField';
			field.options.style.backgroundColor = '#f7f7f7';
		}


		enableAction("spedisciDac", true);
	} else {
		if (document.datiRead.reso.value=="false") {
			//Gestione abilitazione campo destinatario/box

			var field = null;
			if (document.datiRead.isSmistatore.value=="true") {
				field = document.frmTestataDac.box;

				field.disabled = false;
				field.className = 'inputField';
				field.options.style.backgroundColor = 'white';
			}

			field = document.frmTestataDac.uffDestinatario;
			
			field.disabled = false;
			field.className = 'inputField';
			field.options.style.backgroundColor = 'white';
		}	

		enableAction("spedisciDac", false);
	}
}
function rimuoviDocumento(idDoc, barcode){
	event.cancelBubble = true;

	if (document.getElementById(barcode)==null)
		return;

	if(docVisibile){
		alert("Chiudere il dettaglio del documento");
		return;
	}

	if (!confirm("Rimuovere dalla DAC il documento con barcode " + barcode + "?"))
		return;	
	
	document.frmDocDaRimuovere.idDocumento.value = idDoc;
	document.frmDocDaRimuovere.barcode.value = barcode;

	startRequest();
	richiestaPendente++;
	wfemHiddenSubmit(document.frmDocDaRimuovere,'scriptCode');
}
function onRimuoviDocumentoEnd(barcode) {
	document.getElementById(barcode+'Cont').innerHTML = "";
	document.getElementById(barcode+'Cont').style.display = "none";

	docInElenco--;
	richiestaPendente--;
	
	document.getElementById("nDocIns").innerHTML = docInElenco;

	//Gestione abilitazione campo destinatario/box
	if (parseInt(docInElenco,10)==0) {
		if (document.datiRead.reso.value=="false") {
			//Gestione abilitazione campo destinatario/box

			var field = null;
			if (document.datiRead.isSmistatore.value=="true") {
				field = document.frmTestataDac.box;

				field.disabled = false;
				field.className = 'inputField';
				field.options.style.backgroundColor = 'white';
			}

			field = document.frmTestataDac.uffDestinatario;
			field.disabled = false;
			field.className = 'inputField';
			field.options.style.backgroundColor = 'white';
		}

		enableAction("spedisciDac", false);
		if (parseInt(docInElencoConErrore,10)==0)
			document.getElementById('titoloElencoDoc').style.display = "none";
	}
}

function doSpinzaDocumento(idDoc) {
	document.frmDocDaSpinzare.idDocumento.value = idDoc;

	startRequest();
	richiestaPendente++;	
	wfemHiddenSubmit(document.frmDocDaSpinzare,'scriptCode');
}
function onSpinzaDocumentoEnd() {
	richiestaPendente--;
}

function apriDocumento(idDoc){
	startRequest();
	docVisibile=true;
	document.documentoForm.documento_idDocumento.value = idDoc;
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.ApriDocumento.execute';
	wfemHiddenSubmit(document.documentoForm,'docCont,finalsDocScriptsCont');
}

function chiudiDocumento(){
	docVisibile=false;

	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.ChiudiDocumento.execute';
	startRequest();
	wfemHiddenSubmit(document.documentoForm,'docCont,finalsDocScriptsCont');
}

// ##################### END OPERAZIONI SUL DOCUMENTO

// ##################### OPERAZIONI SUL PLICO
function aggiungiPlico(idPlico) {
	if (parseInt(document.getElementById("numeroPlichi").value, 10)==1)
		chiudiDocPlichi();	
	else {
		document.getElementById("tr"+idPlico).style.display = "none";
		document.getElementById("numeroPlichi").value = parseInt(document.getElementById("numeroPlichi").value, 10)-1;
	}
		
	var elencoDocInfo = new Array();
	var elencoDocInfoStr = document.getElementById(idPlico).value;
	elencoDocInfo = elencoDocInfoStr.split(";");

	for (var i=0; i<elencoDocInfo.length; i++){
		var docInfo = new Array();
		var docInfo = elencoDocInfo[i].split(",");
		doAggiungiDocumento(docInfo[1]);
	}
}
function rimuoviPlico(idPlico) {
	if (parseInt(document.getElementById("numeroPlichi").value, 10)==1)
		chiudiDocPlichi();	
	else {
		document.getElementById("tr"+idPlico).style.display = "none";
		document.getElementById("numeroPlichi").value = parseInt(document.getElementById("numeroPlichi").value, 10)-1;
	}
		
	var elencoDocInfo = new Array();
	var elencoDocInfoStr = document.getElementById(idPlico).value;
	elencoDocInfo = elencoDocInfoStr.split(";");

	for (var i=0; i<elencoDocInfo.length; i++){
		var docInfo = new Array();
		var docInfo = elencoDocInfo[i].split(",");
		rimuoviDocumento(docInfo[0],docInfo[1]);
	}
}
function spinzaDaPlico(idPlico) {
	if (parseInt(document.getElementById("numeroPlichi").value, 10)==1)
		chiudiDocPlichi();	
	else {
		document.getElementById("tr"+idPlico).style.display = "none";
		document.getElementById("numeroPlichi").value = parseInt(document.getElementById("numeroPlichi").value, 10)-1;
	}

	var elencoDocInfo = new Array();
	var elencoDocInfoStr = document.getElementById(idPlico).value;
	elencoDocInfo = elencoDocInfoStr.split(";");

	//Ciclo che spinza i documenti presenti nella DAC dal plico
	var spinzati = 0;
	var countDaSpinzare = 0;
	var daSpinzare = new Array();
	for (var i=0; i<elencoDocInfo.length; i++){
		var docInfo = new Array();
		var docInfo = elencoDocInfo[i].split(",");
		
		var idDac = docInfo[2];
		if (idDac!="") {
			doSpinzaDocumento(docInfo[0]);
		} else {
			daSpinzare[countDaSpinzare] = docInfo[0];
			countDaSpinzare++;
		}
	}
	
	//Se rimane un solo documento nel plico lo "spinzo"
	if (daSpinzare.length == 1)
		doSpinzaDocumento(daSpinzare[0]);
	

}
// ##################### END OPERAZIONI SUL PLICO

// ##################### POPUP DOCUMENTI MANCANTI PLICHI
function chiudiDocPlichi(){
	docPlichiVisibile=false;
	document.getElementById("docPlichiCont").style.display = "none";
	focusOnBarcode();
}
// ##################### END POPUP DOCUMENTI MANCANTI PLICHI