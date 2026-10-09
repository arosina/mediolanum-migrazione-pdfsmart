
function setScrollPos(){
	try{
		$("#elencoSinistraCont").bind("scroll",function(){ 
			document.dati.amlModel_relazioni_elencoSinistraContScrollPos.value = this.scrollTop | 0; 
		}).scrollTop(document.dati.amlModel_relazioni_elencoSinistraContScrollPos.value);
		$("#elencoDestraCont").bind("scroll",function(){ 
			document.dati.amlModel_relazioni_elencoDestraContScrollPos.value = this.scrollTop | 0; 
		}).scrollTop(document.dati.amlModel_relazioni_elencoDestraContScrollPos.value);
	}catch(e){}
}

String.prototype.replaceAt = function(index, replacement) {
    return this.substring(0, index) + replacement + this.substring(index + replacement.length);
}

function openCloseAccordion(idx){
	var newStatus = "1";
	var isClose = $("#accordionSoggetti"+idx).hasClass("giublu");
	if(isClose){
		$("#accordionSoggetti"+idx).removeClass("giublu");
		$("#accordionSoggettiCont"+idx).show();
	}else{
		newStatus = "0";
		$("#accordionSoggetti"+idx).addClass("giublu");
		$("#accordionSoggettiCont"+idx).hide();
	}
	var globalSoggAccordionStatus = document.dati.amlModel_relazioni_globalSoggAccordionStatus.value;
	globalSoggAccordionStatus = globalSoggAccordionStatus.replaceAt(idx,newStatus);
	document.dati.amlModel_relazioni_globalSoggAccordionStatus.value = globalSoggAccordionStatus;
	return false;
}

function onChangeTipoRelazioneContraente(obj){
	document.dati.amlModel_relazioni_idxSoggettoSelezionato.value = obj.getAttribute("idx");
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RelazioneOnChangeTipo.execute";
	callHiddenSubmit(document.dati,"tabLabels,elencoDestraCont");
	return false;
}

function onChangeDescrTipoRelazioneContraente(field,soggIndex){
	removeError(field);	
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	callHiddenSubmit(document.dati,"tabLabels,accordionTitle"+soggIndex);
	return false;
}

function onChangeInformazioniRelazioneContraente(field,soggIndex){
	removeError(field);	
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	callHiddenSubmit(document.dati,"tabLabels,accordionTitle"+soggIndex);
	return false;
}

function onChangeTipoMotivazioneTerzoPagatore(radio){
	var field = document.getElementById(radio.name);
	setPropertyValue(field.name, radio.value);
	removeError(field);	
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	callHiddenSubmit(document.dati,"tabLabels,elencoDestraCont");
	return false;
}

function onChangeMotivazioneAssicuratoDiversoDaContraente(field,soggIndex){
	removeError(field);	
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	callHiddenSubmit(document.dati,"tabLabels,accordionTitle"+soggIndex);
	return false;	
}

function onChangeInformazioniRelazioneAssicurando(field,soggIndex){
	removeError(field);	
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	callHiddenSubmit(document.dati,"tabLabels,accordionTitle"+soggIndex);
	return false;	
}
