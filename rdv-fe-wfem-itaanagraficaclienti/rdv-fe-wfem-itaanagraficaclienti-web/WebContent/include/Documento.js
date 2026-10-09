function openHelpTipoDocumento(){
	var bi = "&BrowserInstance="+document.dati.BrowserInstance.value;
	openNewWindow('call.wfem?wfemCmd=prgm.ita.anagraficaclienti.display.HelpTipoDocumento.execute'+bi);
}

function loadComuneDocumento(){
	var cmdParams = "isRicercaIscrittiAlCatasto=false";
	openModalPopup("prgm.ita.anagraficaclienti.popup.display.PopupComuni",
	  				 cmdParams,null,loadComuneDocumentoEnd,"Ricerca Comuni / Località",500,700);

}

function loadComuneDocumentoEnd(comuneSelezionato){
	if(comuneSelezionato == null)
		return;
	document.getElementById('documento_luogoRilascio_comune').value = comuneSelezionato.comune;
}

function gestioneTipoDocumento(onchange){
	var value = document.getElementById('documento_tipoDocumento').value;
	document.getElementById('tabLoadLuogo').style.visibility = 'visible';
	if(value === Costanti_TIPO_DOCUMENTO_CARTA_IDENTITA_ESTERA ||
	   value === Costanti_TIPO_DOCUMENTO_PASSAPORTO_ESTERO)
		document.getElementById('tabLoadLuogo').style.visibility = 'hidden';

	if(onchange){
		if(value === 'P'){
			showAlerMessage(jsMessaggioPatenteUCO);
			$("#messaggioPatenteUCOCont").html(jsMessaggioPatenteUCO);
		}else{
			$("#messaggioPatenteUCOCont").html("");
		}
		startRequest();
		document.getElementById('documento_luogoRilascio_comune').value = "";
		document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.display.AnagraficaCliente.execute";
		wfemHiddenSubmit(document.dati,'documentoLuogoRilascioCont');
	}
}
