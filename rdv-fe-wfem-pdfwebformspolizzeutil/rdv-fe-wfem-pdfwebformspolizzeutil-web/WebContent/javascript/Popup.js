/***********************************************************************************************/
/* Gestione Popup Ricerca Beneficiari */																	    																	 
/***********************************************************************************************/
var ass = null;
var idxBeneficiario = null;
var prefBeneficiario = null;
var tipSoggetto = null;


PdfPageDriver.prototype.openPopupRicercaBeneficiari = function(assicurando, prefissoBeneficiario, indiceBeneficiario, tipologiaSoggetto, codiceAgente){
	
	ass = assicurando; 
	prefBeneficiario = prefissoBeneficiario; 
	idxBeneficiario = indiceBeneficiario; 
	tipSoggetto = tipologiaSoggetto;
	
	if (tipologiaSoggetto === "PF"){
		openModalPopup("prgm.pdfwebformspolizzeutil.popup.RicercaBeneficiariPF",
				"tipoRicerca=" + tipologiaSoggetto + "&codiceAgente="+jsCodAgeFiltro,
				null,
				openPopupRicercaBeneficiariPFCallBack,"Ricerca Beneficiari",600,800,false);
	} else if (tipologiaSoggetto === "PG"){
		openModalPopup("prgm.pdfwebformspolizzeutil.popup.RicercaBeneficiariPG",
				"tipoRicerca=" + tipologiaSoggetto + "&codiceAgente="+jsCodAgeFiltro,
				null,
				openPopupRicercaBeneficiariPGCallBack,"Ricerca Beneficiari",600,800,false);
	} 

}
/***********************************************************************************************/
/***********************************************************************************************/
function openPopupRicercaBeneficiariPFCallBack(obj){
	
	
	if (obj === null){
		ass = null; 
		prefBeneficiario = null; 
		idxBeneficiario = null; 
		tipSoggetto = null;
		return;
	}
	
	if (idxBeneficiario === null || prefBeneficiario === null || ass === null || tipSoggetto === null){
		ass = null; 
		prefBeneficiario = null; 
		idxBeneficiario = null; 
		tipSoggetto = null;
		return;
	}
	
	var suffissoBeneficiario = prefBeneficiario + idxBeneficiario;

	pdf.setFieldValue("codiceCliente"+ suffissoBeneficiario,obj.codiceCliente);
	var beneficiarioFieldTargets = getFieldNamesBeneficiarioPF(getBaseFieldNamesBeneficiarioPF(), suffissoBeneficiario);
	clearErrors(beneficiarioFieldTargets);
	var fields = ["codiceCliente"+ suffissoBeneficiario];
	fields.push("isPep"+suffissoBeneficiario);
	fields.push("indiceMotivazionePep"+suffissoBeneficiario)
	var eventObj = {eventName:"onChangeCodiceClienteSezioneBeneficiario", eventArgs:prefBeneficiario+","+idxBeneficiario+","+tipSoggetto, fields:fields, targets: beneficiarioFieldTargets};
	pdf.callHeavyEvent(eventObj);

	
	ass = null; 
	prefBeneficiario = null; 
	idxBeneficiario = null; 
	tipSoggetto = null;
}

/***********************************************************************************************/
/***********************************************************************************************/
function openPopupRicercaBeneficiariPGCallBack(obj){
	
	
	if (obj === null){
		ass = null; 
		prefBeneficiario = null; 
		idxBeneficiario = null; 
		tipSoggetto = null;
		return;
	}
	
	if (idxBeneficiario === null || prefBeneficiario === null || ass === null || tipSoggetto === null){
		ass = null; 
		prefBeneficiario = null; 
		idxBeneficiario = null; 
		tipSoggetto = null;
		return;
	}
	
	var suffissoBeneficiario = prefBeneficiario + idxBeneficiario;

	pdf.setFieldValue("codiceCliente"+ suffissoBeneficiario,obj.codiceCliente);
	
	var beneficiarioFieldTargets = getFieldNamesBeneficiarioPG(getBaseFieldNamesBeneficiarioPG(), getBaseFieldNamesTitolare(), suffissoBeneficiario, 2);
	clearErrors(beneficiarioFieldTargets);
	var eventObj = {eventName:"onChangeCodiceClienteSezioneBeneficiario", eventArgs:prefBeneficiario+","+idxBeneficiario+","+tipSoggetto, fields:["codiceCliente"+ suffissoBeneficiario], targets: beneficiarioFieldTargets};
	pdf.callHeavyEvent(eventObj);
	
		
	ass = null; 
	prefBeneficiario = null; 
	idxBeneficiario = null; 
	tipSoggetto = null;
}