document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    try {
			doAggiungiDocumento();
		} catch (e) { }
	}
}

function doNuovaDacDopoInvia(){
	document.frmTestataDac.wfemCmd.value = 'prgm.ita.p.dac.sede.creazionedac.NuovaDacDopoSpedizione.execute';
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


function showOraSpedizione(codTipoSpedizione, salva) {
	if (codTipoSpedizione.value != document.datiRead.constMezzoCorriere.value) {
		document.frmTestataDac.oraSpedizione.value = "";
		if (salva)
			doSalvaDac();

		enableField("oraSpedizione", false);
	} else {
		enableField("oraSpedizione", true);

		if (salva)
			doSalvaDac();
	}
}

function verificaEsistenzaDac(){
	document.frmTestataDac.wfemCmd.value = 'prgm.ita.p.dac.sede.creazionedac.VerificaSeEsisteDacAttiva.execute';
	startRequest();
	enableField("oraSpedizione", true);
	document.frmTestataDac.submit();
}

function loadBox(){
	startRequest();
	document.frmTestataDac.wfemCmd.value = 'prgm.ita.p.dac.sede.creazionedac.LoadBox.execute';
	var combo = document.getElementById("box");
	combo.disabled=true;
	combo.value='';
	combo.options(0).text='Attendere...';
	wfemHiddenSubmit(document.frmTestataDac,'boxCont');
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
	enableField("oraSpedizione", true);
	document.frmTestataDac.submit();
	return true;
}

function doStampaDac(){
	stampaDac(document.datiRead.idDac.value,true);
}

function doInserisciDocumento(){
	document.frmTestataDac.wfemCmd.value = 'prgm.ita.p.dac.business.SalvaDac.execute';
	enableField("oraSpedizione", true);
	document.frmTestataDac.submit();
	return true;
}