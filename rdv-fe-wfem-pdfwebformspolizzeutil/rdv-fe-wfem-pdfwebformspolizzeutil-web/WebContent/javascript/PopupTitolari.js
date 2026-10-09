/***********************************************************************************************/
/* Gestione Popup Ricerca Titolari */																	    																	 
/***********************************************************************************************/
var ass = null;
var idxBeneficiario = null;
var prefBeneficiario = null;
var idxTitolare = null;



PdfPageDriver.prototype.openPopupRicercaTitolari = function(assicurando, prefissoBeneficiario, indiceBeneficiario, indiceTitolare, codiceAgente){
	
	ass = assicurando; 
	prefBeneficiario = prefissoBeneficiario; 
	idxBeneficiario = indiceBeneficiario; 
	idxTitolare=indiceTitolare
	
	openModalPopup("prgm.pdfwebformspolizzeutil.popup.RicercaTitolari",
			"codiceAgente="+jsCodAgeFiltro,
			null,
			openPopupRicercaTitolariCallBack,"Ricerca titolari",600,800,false);
	

}
/***********************************************************************************************/
/***********************************************************************************************/
function openPopupRicercaTitolariCallBack(obj){
	
	
	if (obj === null){
		ass = null; 
		prefBeneficiario = null; 
		idxBeneficiario = null; 
		idxTitolare = null;
		return;
	}
	
	if (idxBeneficiario === null || prefBeneficiario === null || ass === null || idxTitolare === null){
		ass = null; 
		prefBeneficiario = null; 
		idxBeneficiario = null; 
		idxTitolare = null;
		return;
	}
	
	var suffissoTitolare = "Titolare" + idxTitolare + prefBeneficiario + idxBeneficiario;

	pdf.setFieldValue("codiceCliente"+ suffissoTitolare,obj.codiceCliente);
	var titolareFieldTargets = getFieldNamesBeneficiarioPF(getBaseFieldNamesTitolare(), suffissoTitolare);
	clearErrors(titolareFieldTargets);
	var eventObj = {eventName:"onChangeCodiceClienteSezioneTitolareBeneficiario", eventArgs:prefBeneficiario+","+idxBeneficiario+","+idxTitolare+"", fields:["codiceCliente"+ suffissoTitolare], targets: titolareFieldTargets};
	pdf.callHeavyEvent(eventObj);

	
	ass = null; 
	prefBeneficiario = null; 
	idxBeneficiario = null; 
	idxTitolare = null;
}
