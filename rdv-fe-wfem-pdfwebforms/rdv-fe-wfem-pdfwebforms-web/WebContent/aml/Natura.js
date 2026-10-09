function setScrollPos(){
	try{
		$("#elencoSinistraCont").bind("scroll",function(){ 
			document.dati.amlModel_natura_elencoSinistraContScrollPos.value = this.scrollTop | 0; 
		}).scrollTop(document.dati.amlModel_natura_elencoSinistraContScrollPos.value);
	}catch(e){}
}

function selezionaDispo(obj){
	if($(obj).hasClass("dispoSel"))
		return;
	wait();
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	document.dati.amlModel_natura_dispositivaSelezionata.value = obj.getAttribute("idx");
	callHiddenSubmit(document.dati,"tabContent");
	return false;
}

function selezionaScopoRapporto(radio){
	var field = document.getElementById(radio.name);
	setPropertyValue(field.name, radio.value);
	removeError(field);	
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	callHiddenSubmit(document.dati,"tabLabels,tabContent");
	return false;
}

function doApplicaATutteLeOperazioni(){
	wait();
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.NaturaApplicaATutteLeOperazioni.execute";
	callHiddenSubmit(document.dati,"tabLabels,tabContent");
	return false;
}