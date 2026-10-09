function setScrollPos(){
	try{
		$("#elencoSinistraCont").bind("scroll",function(){ 
			document.dati.amlModel_origine_elencoSinistraContScrollPos.value = this.scrollTop | 0; 
		}).scrollTop(document.dati.amlModel_origine_elencoSinistraContScrollPos.value);
		$("#elencoDestraCont").bind("scroll",function(){ 
			document.dati.amlModel_origine_elencoDestraContScrollPos.value = this.scrollTop | 0; 
		}).scrollTop(document.dati.amlModel_origine_elencoDestraContScrollPos.value);
	}catch(e){}
}

function aggiungiTipoImporto(sezioneImporti){
	document.dati.amlModel_origine_sezioneImporti.value=sezioneImporti;
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.OrigineAggiungiTipoImporto.execute";
	callHiddenSubmit(document.dati,"tabLabels,elencoImporti"+sezioneImporti+"Cont");
	return false;
}

function eliminaTipoImporto(sezioneImporti,idx){
	setPropertyValue("amlModel_origine_elencoImporti"+sezioneImporti+idx+"_codTipoImporto","");
	setPropertyValue("amlModel_origine_elencoImporti"+sezioneImporti+idx+"_importo","");
	var totaleDigitato = calcolaTotaleImportoDigitato(sezioneImporti);
	setPropertyValue("amlModel_origine_importoTotaleDigitato"+sezioneImporti,totaleDigitato);

	document.dati.amlModel_origine_sezioneImporti.value=sezioneImporti;
	document.dati.amlModel_origine_idxImportoSelezionato.value=idx;
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.OrigineEliminaTipoImporto.execute";
	callHiddenSubmit(document.dati,"tabLabels,elencoImporti"+sezioneImporti+"Cont,importoTotaleDigitatoContestualiCont,importoTotaleDigitatoFuturiCont");
	return false;
}

function onChangeTipoImporto(field){
	var sezioneImporti = field.getAttribute("sezioneImporti");
	document.dati.amlModel_origine_sezioneImporti.value=sezioneImporti;
	document.dati.amlModel_origine_idxImportoSelezionato.value=field.getAttribute("idx");
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.OrigineOnChangeTipoImporto.execute";
	callHiddenSubmit(document.dati,"tabLabels,elencoImporti"+sezioneImporti+"Cont");
	return false;
}

function onChangeImporto(field){
	removeError(field);
	var sezioneImporti = field.getAttribute("sezioneImporti");
	var totaleDigitato = calcolaTotaleImportoDigitato(sezioneImporti);
	setPropertyValue("amlModel_origine_importoTotaleDigitato"+sezioneImporti,totaleDigitato);
	document.dati.amlModel_origine_sezioneImporti.value=sezioneImporti;
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	changeRunning = true;
	callHiddenSubmit(document.dati,"tabLabels,importoTotaleDigitatoContestualiCont,importoTotaleDigitatoFuturiCont");
	return false;	
}

function onChangeDescrAltro(field){
	removeError(field);
	document.dati.amlModel_origine_sezioneImporti.value=field.getAttribute("sezioneImporti");
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	callHiddenSubmit(document.dati,"tabLabels");
	return false;	
}

function calcolaTotaleImportoDigitato(sezioneImporti){
	var tot = 0;
	for(var i=0;;i++){
		var fieldName = "amlModel_origine_elencoImporti"+sezioneImporti+i+"_importo";
		if(document.getElementById(fieldName) == null)
			break;
		var imp = getPropertyValue(fieldName);
		if(!isNaN(imp) && imp > 0)
			tot += imp;
	}
	return tot;
}
