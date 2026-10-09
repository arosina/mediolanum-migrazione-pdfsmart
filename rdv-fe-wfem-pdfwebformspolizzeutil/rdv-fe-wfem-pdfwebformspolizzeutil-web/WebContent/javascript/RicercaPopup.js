/***********************************************************************************************/
/* Gestione Popup Ricerca  */																	    																	 
/***********************************************************************************************/

var indice = null;
var fieldCliente = null;
var eventObj = null;


PdfPageDriver.prototype.openRicercaPopup = function(codiceAgente, idx, prefissoElem, eventObjTrigger, title, escludiXxx){

	indice = idx; 
	prefisso = prefissoElem;
	eventObj = eventObjTrigger;
	
	openModalPopup("prgm.pdfwebformspolizzeutil.popup.RicercaPopup",
			"codiceAgente="+ codiceAgente + "&codAgeImpersonato="+ jsCodAgeFiltro +"&codRuoloImpersonato="+ jsCodRuoloImpersonato +"&indice="+ indice+"&escludiXxx="+escludiXxx,
			null,
			openRicercaPopupCallBack, title, 600, 800, false);
	
}
/***********************************************************************************************/
/***********************************************************************************************/
function openRicercaPopupCallBack(obj){
	
	
	if (obj === null){		
		prefisso = null; 
		indice = null; 		
		return;
	}
	
	if (indice === null || prefisso === null){
		
		prefisso = null; 
		indice = null; 		
		return;
	}
	
	var suffissoCodiceCliente = "codiceCliente" + prefisso;
	var suffissoRagioneSociale = "ragioneSociale" + prefisso;
	var suffissoCodiceFiscalePartitaIva = "codiceFiscalePartitaIva" + prefisso;
	
	pdf.setFieldValue(suffissoCodiceCliente, obj.codiceCliente);
	pdf.setFieldValue(suffissoRagioneSociale, obj.ragioneSociale);
	pdf.setFieldValue(suffissoCodiceFiscalePartitaIva, obj.codiceFiscale);
			
	if(typeof eventObj !=="undefined" && typeof eventObj.eventName !=="undefined" && eventObj.eventName!==null && eventObj.eventName!==""){
		var callEventObj = {eventName:eventObj.eventName, eventArgs:indice, fields:eventObj.fields, targets: eventObj.targets};
		pdf.callHeavyEvent(callEventObj);
	}
	
}

