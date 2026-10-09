function bindRicercaBeneficiarioAutocomplete(input){
	
	var beneficiarioFieldName = input.beneficiarioFieldName;
	var fieldName = input.fieldName;
	var tipoRicerca = input.tipoRicerca;
	var prefissoBeneficiario = input.prefissoBeneficiario;
	var indiceBeneficiario = input.indiceBeneficiario;
	var indiceTitolare = input.indiceTitolare;
	var driverCmd = input.driverCmd;
	var minLenght = input.minLenght;
	var assicurando = input.assicurando;
	var posizioneAssicurando = input.posizioneAssicurando;
	var codAgente = input.codAgente;
	if(!driverCmd){
		driverCmd = "prgm.pdfwebformspolizzeutil.autocomplete.RicercaBeneficiarioAutocomplete";
	}
	
	
	

	$("#"+fieldName).unbind("change").autocomplete({
		source: function(request, response){
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute"+
					"&codiceAgente="+codAgente+
					"&autocompleteFieldName="+beneficiarioFieldName+
					"&tipoRicerca="+tipoRicerca+
					"&readRequest=false",
					dataType: "json",
					async: true,
					data: beneficiarioFieldName+"="+request.term,
					success: function(data){ response(data); }
				});
			},
		minLength: minLenght,
		select: function(event, ui){ 
			if(ui.item.value === ""){
				clearDatiBeneficiario(assicurando, posizioneAssicurando,tipoRicerca, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
				return false;
			}
			setDatiBeneficiario(ui.item, tipoRicerca, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui) {
			if (tipoRicerca === "PF" || tipoRicerca === "PG" ){
				var isGiaCliente = pdf.getFieldValue("isGiaCliente" + prefissoBeneficiario + indiceBeneficiario);
				if($(this).val() != this.savValue ){
					if (isGiaCliente == "N"){
						changeAnagraficaPep(prefissoBeneficiario, indiceBeneficiario);
						changeAnagraficaDichiarazioni(prefissoBeneficiario, indiceBeneficiario);
					} else if (isGiaCliente == "S"){
						clearDatiBeneficiario(assicurando, posizioneAssicurando,tipoRicerca, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
					}
				}
			} else if (tipoRicerca === "TIT"){
				var isGiaClienteTitolare = pdf.getFieldValue("isGiaCliente" + "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario);
				if($(this).val() != this.savValue) {
					if (isGiaClienteTitolare == "N"){
						changeAnagraficaTitolarePep(prefissoBeneficiario, indiceBeneficiario, indiceTitolare)
					} else if (isGiaClienteTitolare == "S"){
						clearDatiBeneficiario(assicurando, posizioneAssicurando, tipoRicerca, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
					}
				}
			}

			
		},
		search: function(event, ui){
			if (tipoRicerca === "PF" || tipoRicerca === "PG" ){
				var isGiaCliente = pdf.getFieldValue("isGiaCliente" + prefissoBeneficiario
						+ indiceBeneficiario);
				if(isGiaCliente !== "S" || ($(this).val() != '' && $(this).val().length < minLenght)){
					event.preventDefault();
					return false;
				}
				return true;
			} else if (tipoRicerca === "TIT"){
				var isGiaClienteTitolare = pdf.getFieldValue("isGiaCliente" + "Titolare" + indiceTitolare + prefissoBeneficiario
						+ indiceBeneficiario);
				if(isGiaClienteTitolare !== "S" || ($(this).val() != '' && $(this).val().length < minLenght)){
					event.preventDefault();
					return false;
				}
				return true;
			}
			
		},
		open: function( event, ui ) {
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null) || $(this).attr('readonly')) {
				$(this).autocomplete("close");
			}
		}
	}).focusin(function() {
		this.savValue = $(this).val();
		if($(this).val() != '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function() {
		//Nothing
	});
}



function setDatiBeneficiario(obj,  tipoRicerca, prefissoBeneficiario, indiceBeneficiario, indiceTitolare){

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	
	
	
	if (tipoRicerca === "PF"){
		pdf.setFieldValue("codiceCliente"+ suffissoBeneficiario,obj.value);
		var beneficiarioPFFieldTargets = getFieldNamesBeneficiarioPF(getBaseFieldNamesBeneficiarioPF(), suffissoBeneficiario);
		clearErrors(beneficiarioPFFieldTargets);
		var fields = ["codiceCliente"+ suffissoBeneficiario];
		fields.push("isPep"+suffissoBeneficiario);
		fields.push("indiceMotivazionePep"+suffissoBeneficiario)
		fields.push("cognomeLegaleRapprProcuratore"+suffissoBeneficiario)
		fields.push("nomeLegaleRapprProcuratore"+suffissoBeneficiario)
		fields.push("codiceFiscaleLegaleRapprProcuratore"+suffissoBeneficiario)
		var eventObjPF = {eventName:"onChangeCodiceClienteSezioneBeneficiario", eventArgs:prefissoBeneficiario+","+indiceBeneficiario+","+tipoRicerca, fields:fields, targets: beneficiarioPFFieldTargets};
		pdf.callHeavyEvent(eventObjPF);
	} else if (tipoRicerca === "PG"){
		pdf.setFieldValue("codiceCliente"+ suffissoBeneficiario,obj.value);
		var beneficiarioPGFieldTargets = getFieldNamesBeneficiarioPG(getBaseFieldNamesBeneficiarioPG(), getBaseFieldNamesTitolare(), suffissoBeneficiario, 2);
		beneficiarioPGFieldTargets.push("cognomeLegaleRapprProcuratore"+suffissoBeneficiario)
		beneficiarioPGFieldTargets.push("nomeLegaleRapprProcuratore"+suffissoBeneficiario)
		beneficiarioPGFieldTargets.push("codiceFiscaleLegaleRapprProcuratore"+suffissoBeneficiario)
		clearErrors(beneficiarioPGFieldTargets);
		var eventObjPG = {eventName:"onChangeCodiceClienteSezioneBeneficiario", eventArgs:prefissoBeneficiario+","+indiceBeneficiario+","+tipoRicerca, fields:["codiceCliente"+ suffissoBeneficiario], targets: beneficiarioPGFieldTargets};
		pdf.callHeavyEvent(eventObjPG);
	} else if (tipoRicerca === "TIT"){
		var suffissoTitolare = "Titolare" + indiceTitolare + suffissoBeneficiario;
		pdf.setFieldValue("codiceCliente"+ suffissoTitolare,obj.value);
		var titolareFieldTargets = getFieldNamesBeneficiarioPF(getBaseFieldNamesTitolare(), suffissoTitolare);
		clearErrors(titolareFieldTargets);
		var eventObjTIT = {eventName:"onChangeCodiceClienteSezioneTitolareBeneficiario", eventArgs:prefissoBeneficiario+","+indiceBeneficiario+","+indiceTitolare+"", fields:["codiceCliente"+ suffissoTitolare], targets: titolareFieldTargets};
		pdf.callHeavyEvent(eventObjTIT);
	}



	
}

function clearDatiBeneficiario(assicurando, posizioneAssicurando, tipoRicerca, prefissoBeneficiario, indiceBeneficiario, indiceTitolare){
	if (tipoRicerca === "PF" || tipoRicerca === "PG"){
		
		clearBeneficiarioDesignatoFormaNominativa(assicurando, posizioneAssicurando,
				prefissoBeneficiario, indiceBeneficiario) ;
		
	}else if (tipoRicerca === "TIT"){
		
		clearTitolare(assicurando,posizioneAssicurando,
				prefissoBeneficiario, indiceBeneficiario, indiceTitolare);		
	}

}