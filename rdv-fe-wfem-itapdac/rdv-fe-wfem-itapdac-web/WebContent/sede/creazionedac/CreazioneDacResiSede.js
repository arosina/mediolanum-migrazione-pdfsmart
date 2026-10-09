document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    try {
			doAggiungiDocumento();
		} catch (e) { }
	}
}

function doNuovaDacDopoInvia(){
	document.frmTestataDac.wfemCmd.value = "prgm.ita.p.dac.sede.creazionedac.NuovaDacResiDopoSpedizione.execute";
	document.frmTestataDac.submit();
	return true;
}

function doGoback(){
	if (document.datiRead.refreshable.value == "true"){
		document.gobackForm.doSearch.value = 'true';
	}else{
		document.gobackForm.doSearch.value = 'false';
	}
	document.gobackForm.submit();
	return true;
}

function verificaEsistenzaDac(){
	document.frmTestataDac.wfemCmd.value = 'prgm.ita.p.dac.sede.creazionedac.VerificaSeEsisteDacAttiva.execute';
	startRequest();
	document.frmTestataDac.submit();
}

function doSalvaDac(){
	document.frmTestataDac.wfemCmd.value = "prgm.ita.p.dac.business.SalvaDac.execute";
	wfemHiddenSubmit(document.frmTestataDac, "none");
}

function doSpedisciDac(){
	if(docVisibile){
		alert("Chiudere il dettaglio del documento");
		return;
	}
	if (parseInt(richiestaPendente,10)>0) {
		alert("Sono in corso operazioni con il Server. Attendere che terminino e riprovare");
		return;
	}

	if(parseInt(docInElencoConErrore, 10)>0){
		alert("Spedizione DAC: si sono presentati errori durante l'aggiunta dei documenti.\nLa spedizione viene sospesa in attesa di autorizzazione");
		return;
	}else{
		if (!confirm("Spedizione DAC: continuare?"))
			return;
	}

	document.frmTestataDac.wfemCmd.value = 'prgm.ita.p.dac.sede.creazionedac.SpedisciDac.execute';
	document.frmTestataDac.submit();
	return true;
}

function doStampaDac(){
	stampaDac(document.datiRead.idDac.value,true);
}

function doInserisciDocumento(){
	enableField("uffDestinatario",true);
	document.frmTestataDac.wfemCmd.value = 'prgm.ita.p.dac.business.SalvaDac.execute';
	document.frmTestataDac.submit();
	return true;
}