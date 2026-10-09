document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    try {
		    document.all("vediDac").click();
		} catch (e) { }
	}
}

function focusOnBarcode() {
	document.frmDocSparato.barcode.select();
}

function doVediDac() {
	var barcode = document.frmDocSparato.barcode.value;

	focusOnBarcode();
	if (trim(barcode)=="")
		return;

	document.frmDocSparato.submit();
	return true;
}

function selezionaDac(row) {
	enableAction ("ricevi", true);
	
	barcodeSelezionato = document.frmDocSparato.barcode.value;
	idDacSelezionata = row.idDac;
	descrUffDestinatario = row.descrUffDestinatario;
	
	document.frmRiceviDocForzato.idDacSelezionata.value = row.idDac;
}

function doRicevi() {
	if (!confirm("Il documento con barcode " + barcodeSelezionato + " verra' ricevuto nella DAC con codice " + idDacSelezionata + " destinata all'ufficio " + descrUffDestinatario + ". Continuare?")) {
		return false;
	}

	document.frmRiceviDocForzato.submit();
	return true;	
}