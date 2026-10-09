PdfPageDataentryUtil = function(){
}

//*************************************************************************************
// Luogo (automatico)
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindLuogoAutocomplete = function(input){
	
	var fieldName = input.fieldName;
	var driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";

	if(!driverCmd)
		driverCmd = "prgm.pdfwebforms.dataentryutil.LuogoAutocomplete";
	
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	jqObj.unbind("change").autocomplete({
		source: function( request, response){
					$.ajax({
						url: "call.wfem?wfemCmd="+driverCmd+".execute&readRequest=false"+noElementsIndicator,
						dataType: "json",
						async: true,
						data: { autocompleteTerm: request.term },
						success: function(data) { response(data); }
					});
		},
		minLength: 1,
		delay: 700,
		select: function(event, ui){
			if(ui.item.value == "")
				return false; 
			pdfPageFields.setFieldValue(fieldName, ui.item.value);
			pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName), ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) { 
			if($(this).val() != this.savValue && $("#"+fieldName).attr("forceSelection") == "true"){
				pdfPageFields.setFieldValue(fieldName, "");
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
			if($(this).val() != this.savValue)
				pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName));
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName)){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
	});
}

//*************************************************************************************
//Toponimo (automatico)
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindDescrToponimoAutocomplete = function(input){
	
	var fieldName = input.fieldName;
	var driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";

	var toponimoName = fieldName.substring("descrToponimo".length);
	
	if(!driverCmd)
		driverCmd = "prgm.pdfwebforms.dataentryutil.ToponimoAutocomplete";
	
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	jqObj.unbind("change").autocomplete({
		source: function( request, response){
					$.ajax({
						url: "call.wfem?wfemCmd="+driverCmd+".execute&readRequest=false"+noElementsIndicator,
						dataType: "json",
						async: true,
						data: { autocompleteTerm: request.term },
						success: function(data) { response(data); }
					});
		},
		minLength: 1,
		delay: 700,
		select: function(event, ui){ 
			if(ui.item.value === ""){
				pdfPageDataentryUtil.clearToponimoData(toponimoName);
				return false; 
			}
			pdfPageDataentryUtil.setToponimoData(ui.item, toponimoName);
			pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName), ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) { 
			if($(this).val() !== this.savValue){
				pdfPageFields.setFieldValue(fieldName, "");
				pdfPageDataentryUtil.clearToponimoData(toponimoName);
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
			if($(this).val() !== this.savValue)
				pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName));
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName)){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
	});
}

PdfPageDataentryUtil.prototype.setToponimoData = function(item, toponimoName){
	pdfPageFields.setFieldValue("descrToponimo"+toponimoName, item.descrToponimo);
	pdfPageFields.setFieldValue("codToponimo"+toponimoName, item.codToponimo);
}

PdfPageDataentryUtil.prototype.clearToponimoData = function(toponimoName){
	pdfPageFields.setFieldValue("descrToponimo"+toponimoName, "");
	pdfPageFields.setFieldValue("codToponimo"+toponimoName, "");
}

//*************************************************************************************
// Comune (non automatico)
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindComuneAutocomplete = function(input){
	
	var comuneFieldName = input.comuneFieldName;
	var fieldName = input.fieldName;
	var driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";
	
	var comuneName = fieldName.substring(comuneFieldName.length);
	
	if(!driverCmd)
		driverCmd = "prgm.pdfwebforms.dataentryutil.ComuneAutocomplete";

	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	jqObj.unbind("change").autocomplete({
		source: function( request, response){
					$.ajax({
						url: "call.wfem?wfemCmd="+driverCmd+".execute&autocompleteFieldName="+comuneFieldName+"&readRequest=false"+noElementsIndicator,
						dataType: "json",
						async: true,
						data: { autocompleteTerm: request.term },
						success: function(data) { response(data); }
					});
		},
		minLength: 1,
		delay: 700,
		select: function(event, ui){ 
			if(ui.item.value == ""){
				pdfPageDataentryUtil.clearComuneData(comuneName);
				return false; 
			}
			pdfPageDataentryUtil.setComuneData(ui.item, comuneName);
			pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName), ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) { 
			if($(this).val() != this.savValue && $("#"+fieldName).attr("forceSelection") == "true"){
				pdfPageFields.setFieldValue(fieldName, "");
				pdfPageDataentryUtil.clearComuneData(comuneName);
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
			if($(this).val() != this.savValue)
				pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName));
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName)){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
	});
}

PdfPageDataentryUtil.prototype.setComuneData = function(item, comuneName){
	pdfPageFields.setFieldValue("nazioneComune"+comuneName, item.nazioneComune);
	pdfPageFields.setFieldValue("descrNazioneComune"+comuneName, item.descrNazioneComune);
	pdfPageFields.setFieldValue("provinciaComune"+comuneName, item.provinciaComune);
	pdfPageFields.setFieldValue("capComune"+comuneName, item.capComune);
	pdfPageFields.setFieldValue("comune"+comuneName, item.comune);
}

PdfPageDataentryUtil.prototype.clearComuneData = function(comuneName){
	pdfPageFields.setFieldValue("nazioneComune"+comuneName, "");
	pdfPageFields.setFieldValue("descrNazioneComune"+comuneName, "");
	pdfPageFields.setFieldValue("provinciaComune"+comuneName, "");
	pdfPageFields.setFieldValue("capComune"+comuneName, "");
	pdfPageFields.setFieldValue("comune"+comuneName, "");
}

//*************************************************************************************
// Agente (automatico)
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindAgePersonAutocomplete = function(input){
	
	var personFieldName = input.personFieldName;
	var driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";
	
	if(!driverCmd)
		driverCmd = "prgm.pdfwebforms.dataentryutil.AgePersonAutocomplete";
	
	var fieldName = personFieldName+"Agente"; 
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+personFieldName+"Agente']");
	jqObj.unbind("change").autocomplete({
		source: function(request, response){
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute&autocompleteFieldName="+personFieldName+"&readRequest=false"+noElementsIndicator,
					dataType: "json",
					async: true,
					data: personFieldName+"="+request.term,
				    success: function(data){ response(data); }
				});
		},
		minLength:  1,
		delay: 700,
		select: function(event, ui){ 
			if(ui.item.value == ""){
				pdfPageDataentryUtil.clearPersonData("", "Agente");
				pdfPageDriver.clearAgeData();
				if(jsIsOperatoreMom)
					callPopolamentoAgenteMomEvent("");
				return false; 
			}
			pdfPageDataentryUtil.setPersonData(ui.item, "", "Agente");
			if(jsIsOperatoreMom)
				callPopolamentoAgenteMomEvent(ui.item.codice);
			var fieldObj = document.getElementById(fieldName);
			if(pdfPageFields.dispatchChangeAgeEvent(fieldObj, ui.item))
				pdfPageFields.dispatchChangeEvent(fieldObj, ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) { 
			var popolaCalled=false;
			if($(this).val() != this.savValue){
				pdfPageFields.setFieldValue(fieldName,"");
				pdfPageDataentryUtil.clearPersonData("", "Agente");
				pdfPageDriver.clearAgeData();
				if(jsIsOperatoreMom){
					callPopolamentoAgenteMomEvent("");
					popolaCalled=true;
				}
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
			if($(this).val() != this.savValue){
				var fieldObj = document.getElementById(fieldName);
				if(pdfPageFields.dispatchChangeAgeEvent(fieldObj))
					pdfPageFields.dispatchChangeEvent(fieldObj);
				if(jsIsOperatoreMom && !popolaCalled && fieldName == "codiceAgente")
					callPopolamentoAgenteMomEvent(fieldObj.value);
			}
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName)){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
	});
}

//*************************************************************************************
//Split (automatico)
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindSplitPersonAutocomplete = function(input){
	
	var personFieldName = input.personFieldName;
	var driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";
	
	if(!driverCmd)
		driverCmd = "prgm.pdfwebforms.dataentryutil.AgePersonAutocomplete";
	
	var fieldName = personFieldName+"Split"; 
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+personFieldName+"Split']");
	jqObj.unbind("change").autocomplete({
		source: function(request, response){
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute&autocompleteFieldName="+personFieldName+"&readRequest=false"+noElementsIndicator,
					dataType: "json",
					async: true,
					data: personFieldName+"="+request.term,
				    success: function(data){ response(data); }
				});
		},
		minLength:  1,
		delay: 700,
		select: function(event, ui){ 
			if(ui.item.value == ""){
				pdfPageDataentryUtil.clearPersonData("", "Split");
				return false; 
			}
			pdfPageDataentryUtil.setPersonData(ui.item, "", "Split");
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) { 
			if($(this).val() != this.savValue){
				pdfPageFields.setFieldValue(fieldName,"");
				pdfPageDataentryUtil.clearPersonData("", "Split");
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName)){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
	});
}


//*************************************************************************************
// Cliente (automatico)
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindPersonAutocomplete = function(input){
	
	var personFieldName = input.personFieldName;
	var fieldName = input.fieldName;
	var driverCmd = input.driverCmd;
	var personIdx = input.personIdx;
	var escludiProspect = input.escludiProspect;
	if(escludiProspect === undefined)
		escludiProspect = false;
	if(jsIsOperatoreMom)
		escludiProspect = true;
	if(!personIdx)
		personIdx = -1;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";
	var escludiVariazioni = "&escludiVariazioni=" + (jsEscludiVariazioniCliente?"true":"false");

	var personName = fieldName.substring(personFieldName.length);
	
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	jqObj.unbind("change").autocomplete({
		source: function(request, response){
			var codAgente = $("#"+jsCodiceAgenteFieldName).val();
			if(jsCodAgeImpersonato !== "")
				codAgente = jsCodAgeImpersonato;
			var codRuoloImpersonato = "";
			if(jsCodRuoloImpersonato !== "" && !jsIsOperatoreMom)
				codRuoloImpersonato = jsCodRuoloImpersonato;
			var includiNdgSpecificiPar = "";
			if(isIsGestioneLegaleRappresentanteAttiva && (personIdx == 2 || personIdx == 3)){ 	// Quando attiva la gestione il legale rappresentante, sempre secondo o terzo cliente, può essere di altri FB
				var isClientePG = pdf.getFieldValue("isPersonaGiuridicaCliente1");
				if(isClientePG == "true"){
					if(personIdx == 2 || (	pdf.fieldExist("isCliente3LegaleRappresentante") ||
											pdf.fieldExist("tipoCliente3") ||
											pdf.fieldExist("legaleRappresentante3")))
						includiNdgSpecificiPar = "&includiNdgSpecifici=true";
				}
			}
			if(!driverCmd)
				driverCmd = "prgm.pdfwebforms.dataentryutil.PersonAutocomplete";
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute&personIdx="+personIdx+"&codAgente="+codAgente+"&codRuoloImpersonato="+codRuoloImpersonato+"&autocompleteFieldName="+personFieldName+"&readRequest=false&escludiProspect="+escludiProspect+noElementsIndicator+escludiVariazioni+includiNdgSpecificiPar,
					dataType: "json",
					async: true,
					data: personFieldName+"="+request.term,
				    success: function(data){ response(data); }
				});
		},
		minLength:  1,
		delay: 700,
		select: function(event, ui){
			if(ui.item.value == ""){
				pdfPageDataentryUtil.clearPersonData(personName);
				pdfPageDriver.clearPersonData(personIdx);
				if(jsIsOperatoreMom)
					callPopolamentoClienteMomEvent(personIdx, "");
				return false;
			}
			pdfPageDataentryUtil.setPersonData(ui.item, personName);
			if(jsIsOperatoreMom)
				callPopolamentoClienteMomEvent(personIdx, ui.item.ndg);
			var fieldObj = document.getElementById(fieldName);
			if(pdfPageFields.dispatchChangePersonEvent(fieldObj, personIdx, ui.item))
				pdfPageFields.dispatchChangeEvent(fieldObj, ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) { 
			var popolaCalled=false;
			if($(this).val() != this.savValue && (pdf.forceSelection || $("#"+fieldName).attr("forceSelection") == "true")){
				pdfPageFields.setFieldValue(fieldName,"");
				pdfPageDataentryUtil.clearPersonData(personName);
				pdfPageDriver.clearPersonData(personIdx);
				if(jsIsOperatoreMom){
					callPopolamentoClienteMomEvent(personIdx, "");
					popolaCalled=true;
				}
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
			if($(this).val() != this.savValue){
				var fieldObj = document.getElementById(fieldName);
				if(pdfPageFields.dispatchChangePersonEvent(fieldObj, personIdx))
					pdfPageFields.dispatchChangeEvent(fieldObj);
				if(jsIsOperatoreMom && !popolaCalled && fieldName == "ndgCliente"+personIdx)
					callPopolamentoClienteMomEvent(personIdx, fieldObj.value);	
			}
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName)){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
	});
}

PdfPageDataentryUtil.prototype.setPersonData = function(item, personIdx, suffix){
	if(suffix === undefined)
		suffix = "";
	if(suffix !== ""){
		personIdx = "";
		pdfPageFields.setFieldValue("codice"+suffix, item.codice);		
		pdfPageFields.setFieldValue("numeroCellulare"+suffix, item.numeroCellulare);
		pdfPageFields.setFieldValue("numeroTelefono"+suffix, item.numeroTelefono);
		pdfPageFields.setFieldValue("codiceArea"+suffix, item.codiceArea);
		pdfPageFields.setFieldValue("codiceAgenzia"+suffix, item.codiceAgenzia);
		pdfPageFields.setFieldValue("comuneAgenzia"+suffix, item.comuneAgenzia);
		pdfPageFields.setFieldValue("provinciaAgenzia"+suffix, item.provinciaAgenzia);
		pdfPageFields.setFieldValue("dataIscrizioneRui"+suffix, item.dataIscrizioneRui);
		pdfPageFields.setFieldValue("numeroIscrizioneRui"+suffix, item.numeroIscrizioneRui);
	}
	pdfPageFields.setFieldValue("ndg"+suffix+personIdx, item.ndg);
	pdfPageFields.setFieldValue("idCensimento"+suffix+personIdx, item.idCensimento);
	
	pdfPageFields.setFieldValue("cognome"+suffix+personIdx, item.cognome);
	pdfPageFields.setFieldValue("nome"+suffix+personIdx, item.nome);
	pdfPageFields.setFieldValue("secondaIntestazione"+suffix+personIdx, item.secondaIntestazione);
	pdfPageFields.setFieldValue("codiceFiscale"+suffix+personIdx, item.codiceFiscale);
	pdfPageFields.setFieldValue("partitaIva"+suffix+personIdx, item.partitaIva);
	pdfPageFields.setFieldValue("codiceFiscalePartitaIva"+suffix+personIdx, item.codiceFiscalePartitaIva);
	pdfPageFields.setFieldValue("sesso"+suffix+personIdx, item.sesso);
	pdfPageFields.setFieldValue("cognomeNome"+suffix+personIdx, item.cognomeNome);
	pdfPageFields.setFieldValue("nomeCognome"+suffix+personIdx, item.nomeCognome);
	pdfPageFields.setFieldValue("isPrimafila"+suffix+personIdx, item.isPrimafila);
	pdfPageFields.setFieldValue("isGiaCliente"+suffix+personIdx, item.isGiaCliente);
	pdfPageFields.setFieldValue("isCointestatario"+suffix+personIdx, item.isCointestatario);
	
	pdfPageFields.setFieldValue("dataNascita"+suffix+personIdx, item.dataNascita);
	pdfPageFields.setFieldValue("luogoNascita"+suffix+personIdx, item.luogoNascita);	
	pdfPageFields.setFieldValue("codiceNazioneNascita"+suffix+personIdx, item.codiceNazioneNascita);
	pdfPageFields.setFieldValue("nazioneNascita"+suffix+personIdx, item.nazioneNascita);
	pdfPageFields.setFieldValue("provinciaNascita"+suffix+personIdx, item.provinciaNascita);

	
	pdfPageFields.setFieldValue("isPersonaFisica"+suffix+personIdx, item.isPersonaFisica);
	pdfPageFields.setFieldValue("isPersonaGiuridica"+suffix+personIdx, item.isPersonaGiuridica);
	pdfPageFields.setFieldValue("isDittaIndividuale"+suffix+personIdx, item.isDittaIndividuale);
	pdfPageFields.setFieldValue("isLiberoProfessionista"+suffix+personIdx, item.isLiberoProfessionista);
	pdfPageFields.setFieldValue("naturaGiuridica"+suffix+personIdx, item.naturaGiuridica);
		
	pdfPageFields.setFieldValue("codiceStatoCivile"+suffix+personIdx, item.codiceStatoCivile);
	pdfPageFields.setFieldValue("statoCivile"+suffix+personIdx, item.statoCivile);
	
	pdfPageFields.setFieldValue("codiceNazioneCittadinanza"+suffix+personIdx, item.codiceNazioneCittadinanza);
	pdfPageFields.setFieldValue("nazioneCittadinanza"+suffix+personIdx, item.nazioneCittadinanza);
	
	pdfPageFields.setFieldValue("email"+suffix+personIdx, item.email);
	
	pdfPageFields.setFieldValue("codiceTipoDocumento"+suffix+personIdx, item.codiceTipoDocumento);
	var tipoDocFound = true;
	if($("#codiceTipoDocumento"+suffix+personIdx) === "radiobutton"){
		tipoDocFound = false;
		pdfPageFields.getJqFieldObjects("codiceTipoDocumento"+suffix+personIdx).each(function(index){
			if(this.getAttribute("value") == item.codiceTipoDocumento)
				tipoDocFound = true;
		});
	}
	if(tipoDocFound){
		pdfPageFields.setFieldValue("tipoDocumento"+suffix+personIdx, item.tipoDocumento);
		pdfPageFields.setFieldValue("numeroDocumento"+suffix+personIdx, item.numeroDocumento);
		pdfPageFields.setFieldValue("codiceEnteRilascianteDocumento"+suffix+personIdx, item.codiceEnteRilascianteDocumento);
		pdfPageFields.setFieldValue("enteRilascianteDocumento"+suffix+personIdx, item.enteRilascianteDocumento);
		pdfPageFields.setFieldValue("dataEmissioneDocumento"+suffix+personIdx, item.dataEmissioneDocumento);
		pdfPageFields.setFieldValue("luogoEmissioneDocumento"+suffix+personIdx, item.luogoEmissioneDocumento);
		pdfPageFields.setFieldValue("provinciaEmissioneDocumento"+suffix+personIdx, item.provinciaEmissioneDocumento);
		pdfPageFields.setFieldValue("dataScadenzaDocumento"+suffix+personIdx, item.dataScadenzaDocumento);
	}else{
		pdfPageFields.setFieldValue("tipoDocumento"+suffix+personIdx, "");
		pdfPageFields.setFieldValue("numeroDocumento"+suffix+personIdx, "");
		pdfPageFields.setFieldValue("codiceEnteRilascianteDocumento"+suffix+personIdx, "");
		pdfPageFields.setFieldValue("enteRilascianteDocumento"+suffix+personIdx, "");
		pdfPageFields.setFieldValue("dataEmissioneDocumento"+suffix+personIdx, "");
		pdfPageFields.setFieldValue("luogoEmissioneDocumento"+suffix+personIdx, "");
		pdfPageFields.setFieldValue("provinciaEmissioneDocumento"+suffix+personIdx, "");
		pdfPageFields.setFieldValue("dataScadenzaDocumento"+suffix+personIdx, "");
	}
	
	pdfPageFields.setFieldValue("codiceToponimoResidenza"+suffix+personIdx, item.codiceToponimoResidenza);
	pdfPageFields.setFieldValue("toponimoResidenza"+suffix+personIdx, item.toponimoResidenza);
	pdfPageFields.setFieldValue("indirizzoResidenza"+suffix+personIdx, item.indirizzoResidenza);
	pdfPageFields.setFieldValue("toponimoIndirizzoResidenza"+suffix+personIdx, item.toponimoIndirizzoResidenza);
	pdfPageFields.setFieldValue("numeroCivicoResidenza"+suffix+personIdx, item.numeroCivicoResidenza);
	pdfPageFields.setFieldValue("toponimoIndirizzoNumeroResidenza"+suffix+personIdx, item.toponimoIndirizzoNumeroResidenza);
	pdfPageFields.setFieldValue("capResidenza"+suffix+personIdx, item.capResidenza);
	pdfPageFields.setFieldValue("luogoResidenza"+suffix+personIdx, item.luogoResidenza);
	pdfPageFields.setFieldValue("comuneResidenza"+suffix+personIdx, item.comuneResidenza);
	pdfPageFields.setFieldValue("provinciaResidenza"+suffix+personIdx, item.provinciaResidenza);
	pdfPageFields.setFieldValue("codiceNazioneResidenza"+suffix+personIdx, item.codiceNazioneResidenza);
	pdfPageFields.setFieldValue("nazioneResidenza"+suffix+personIdx, item.nazioneResidenza);
	
	pdfPageFields.setFieldValue("prefissoTelefonoAbitazione"+suffix+personIdx, item.prefissoTelefonoAbitazione);
	pdfPageFields.setFieldValue("numeroTelefonoAbitazione"+suffix+personIdx, item.numeroTelefonoAbitazione);
	pdfPageFields.setFieldValue("telefonoAbitazione"+suffix+personIdx, item.telefonoAbitazione);

	pdfPageFields.setFieldValue("prefissoTelefonoCellulare"+suffix+personIdx, item.prefissoTelefonoCellulare);
	pdfPageFields.setFieldValue("numeroTelefonoCellulare"+suffix+personIdx, item.numeroTelefonoCellulare);
	pdfPageFields.setFieldValue("telefonoCellulare"+suffix+personIdx, item.telefonoCellulare);

	pdfPageFields.setFieldValue("motivazionePep"+suffix+personIdx, item.motivazionePep);
}

PdfPageDataentryUtil.prototype.clearPersonData = function(personIdx, suffix){
	if(suffix === undefined)
		suffix = "";
	if(suffix !== ""){
		personIdx = "";
		pdfPageFields.setFieldValue("codice"+suffix, "");		
		pdfPageFields.setFieldValue("numeroCellulare"+suffix, "");
		pdfPageFields.setFieldValue("numeroTelefono"+suffix, "");
		pdfPageFields.setFieldValue("codiceArea"+suffix, "");
		pdfPageFields.setFieldValue("codiceAgenzia"+suffix, "");
		pdfPageFields.setFieldValue("comuneAgenzia"+suffix, "");
		pdfPageFields.setFieldValue("provinciaAgenzia"+suffix, "");
		pdfPageFields.setFieldValue("dataIscrizioneRui"+suffix, "");
		pdfPageFields.setFieldValue("numeroIscrizioneRui"+suffix, "");
	}
	pdfPageFields.setFieldValue("ndg"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("idCensimento"+suffix+personIdx, "");
	
	pdfPageFields.setFieldValue("cognome"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("nome"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("secondaIntestazione"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("codiceFiscale"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("partitaIva"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("codiceFiscalePartitaIva"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("sesso"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("cognomeNome"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("nomeCognome"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("isPrimafila"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("isGiaCliente"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("isCointestatario"+suffix+personIdx, "");
	
	pdfPageFields.setFieldValue("dataNascita"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("luogoNascita"+suffix+personIdx, "");	
	pdfPageFields.setFieldValue("codiceNazioneNascita"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("nazioneNascita"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("provinciaNascita"+suffix+personIdx, "");
	
	
	pdfPageFields.setFieldValue("isPersonaFisica"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("isPersonaGiuridica"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("isDittaIndividuale"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("isLiberoProfessionista"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("naturaGiuridica"+suffix+personIdx, "");
		
	pdfPageFields.setFieldValue("codiceStatoCivile"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("statoCivile"+suffix+personIdx, "");
	
	pdfPageFields.setFieldValue("codiceNazioneCittadinanza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("nazioneCittadinanza"+suffix+personIdx, "");
	
	pdfPageFields.setFieldValue("email"+suffix+personIdx, "");
	
	pdfPageFields.setFieldValue("codiceTipoDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("tipoDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("numeroDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("codiceEnteRilascianteDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("enteRilascianteDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("dataEmissioneDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("luogoEmissioneDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("provinciaEmissioneDocumento"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("dataScadenzaDocumento"+suffix+personIdx, "");
	
	pdfPageFields.setFieldValue("codiceToponimoResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("toponimoResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("indirizzoResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("toponimoIndirizzoResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("numeroCivicoResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("toponimoIndirizzoNumeroResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("capResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("luogoResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("comuneResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("provinciaResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("codiceNazioneResidenza"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("nazioneResidenza"+suffix+personIdx, "");
	
	pdfPageFields.setFieldValue("prefissoTelefonoAbitazione"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("numeroTelefonoAbitazione"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("telefonoAbitazione"+suffix+personIdx, "");

	pdfPageFields.setFieldValue("prefissoTelefonoCellulare"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("numeroTelefonoCellulare"+suffix+personIdx, "");
	pdfPageFields.setFieldValue("telefonoCellulare"+suffix+personIdx, "");

	pdfPageFields.setFieldValue("motivazionePep"+suffix+personIdx, "");
}

//*************************************************************************************
//Cliente (da driver)
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindMyPersonAutocomplete = function(input){
	
	var personFieldName = input.personFieldName; // Nome campo in query
	var fieldName = input.fieldName; // Nome campo in pagina
	var personName = input.personName; // Nome logico cliente
	var driverCmd = input.driverCmd;
	var escludiProspect = input.escludiProspect;
	if(escludiProspect === undefined)
		escludiProspect = false;
	if(jsIsOperatoreMom)
		escludiProspect = true;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";
	var escludiVariazioni = "&escludiVariazioni=" + (jsEscludiVariazioniCliente?"true":"false");
	
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	jqObj.unbind("change").autocomplete({
		source: function(request, response){
			var codAgente = $("#"+jsCodiceAgenteFieldName).val();
			if(jsCodAgeImpersonato !== "")
				codAgente = jsCodAgeImpersonato;
			var codRuoloImpersonato = "";
			if(jsCodRuoloImpersonato !== "" && !jsIsOperatoreMom)
				codRuoloImpersonato = jsCodRuoloImpersonato;
			var includiNdgSpecificiPar = "";
			var isClientePG = pdf.getFieldValue("isPersonaGiuridicaCliente1");
			if(isClientePG=="true" && personIdx == 2)
				includiNdgSpecificiPar = "&includiNdgSpecifici=true";
			if(!driverCmd)
				driverCmd = "prgm.pdfwebforms.dataentryutil.PersonAutocomplete";
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute&codAgente="+codAgente+"&codRuoloImpersonato="+codRuoloImpersonato+"&autocompleteFieldName="+personFieldName+"&readRequest=false&escludiProspect="+escludiProspect+noElementsIndicator+escludiVariazioni+includiNdgSpecificiPar,
					dataType: "json",
					async: true,
					data: personFieldName+"="+request.term,
				    success: function(data){ response(data); }
				});
		},
		minLength:  1,
		delay: 700,
		select: function(event, ui){
			if(ui.item.value == ""){
				pdfPageDriver.clearMyPersonData(personName);
				return false;
			}
			pdfPageDriver.setMyPersonData(personName, ui.item);
			var fieldObj = document.getElementById(fieldName);
			pdfPageFields.dispatchChangeEvent(fieldObj, ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) { 
			if($(this).val() != this.savValue && (pdf.forceSelection || $("#"+fieldName).attr("forceSelection") == "true")){
				pdfPageFields.setFieldValue(fieldName,"");
				pdfPageDriver.clearMyPersonData(personName);
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
			if($(this).val() != this.savValue){
				var fieldObj = document.getElementById(fieldName);
				pdfPageFields.dispatchChangeEvent(fieldObj);
			}
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName)){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
	});
}

//*************************************************************************************
//Conto corrente cliente
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindContoAutocomplete = function(input){

	var useCodAgente = input.useCodAgente === undefined ? true : input.useCodAgente;
	if(jsIsOperatoreMom)
		useCodAgente = false;
	var ndgFieldNames = input.ndgFieldName;
	var tipoConto = input.tipoConto ? input.tipoConto : "";
	var ruoliAmmessi = input.ruoliAmmessi ? input.ruoliAmmessi : "";
	var divisaConto = input.divisaConto ? input.divisaConto : "";
	var	contoFieldName = input.contoFieldName;
	var fieldName = input.fieldName;
	var	driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";
	
	var contoName = fieldName.substring(contoFieldName.length);
	
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	jqObj.unbind("change").autocomplete({
		source: function(request, response){
			var ndgCliente = pdfPageDataentryUtil.getElencoNdg(ndgFieldNames);
			var codAgente = useCodAgente ? $("#"+jsCodiceAgenteFieldName).val() : "";
			if(useCodAgente && jsCodAgeImpersonato !== "")
				codAgente = jsCodAgeImpersonato;
			if(!driverCmd)
				driverCmd = "prgm.pdfwebforms.dataentryutil.ContoAutocomplete";
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute&codAgente="+codAgente+"&ndgCliente="+ndgCliente+"&tipoConto="+tipoConto+"&ruoliAmmessi="+ruoliAmmessi+"&divisaConto="+divisaConto+"&autocompleteFieldName="+contoFieldName+"&readRequest=false"+noElementsIndicator,
					dataType: "json",
					async: true,
				    success: function(data){ response(data); }
				});
		},
		minLength: 0,
		select: function(event, ui){ 
			if(ui.item.value == ""){
				pdfPageDataentryUtil.clearContoData(contoName);
				return false;		
			}
			pdfPageDataentryUtil.setContoData(ui.item, contoName);
			pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName), ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) {
			if($(this).val() != this.savValue && (pdf.forceSelection || $("#"+fieldName).attr("forceSelection") == "true")){
				pdfPageDataentryUtil.clearContoData(contoName);
			}else{
				$("input[fieldName='"+fieldName+"']").not(document.getElementById(this.id)).val($(this).val()); 
			}
			if($(this).val() != this.savValue)
				pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName));
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName) || $(this).val() != ''){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
		if((useCodAgente && $("#"+jsCodiceAgenteFieldName).val() == '') || pdfPageDataentryUtil.getElencoNdg(ndgFieldNames) == '' || $(this).val() != '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function() {
		if(pdf.forceSelection || $("#"+fieldName).attr("forceSelection") == "true"){
			if($(this).val() == "")
				pdfPageDataentryUtil.clearContoData(contoName);
		}
	});
}

PdfPageDataentryUtil.prototype.getElencoNdg = function(ndgFieldNames){
	var ndgFieldNamesAsArray = ndgFieldNames.split(",");
	if(ndgFieldNamesAsArray.length == 1)
		return $("#"+ndgFieldNames).val();
	
	var elencoNdg = "";
	for(var i=0; i<ndgFieldNamesAsArray.length; i++){
		var ndg = $("#"+ndgFieldNamesAsArray[i]).val();
		elencoNdg += "'"+ndg.padStart(11,"0")+"',";
	}
	if(elencoNdg.length > 0)
		elencoNdg = elencoNdg.substring(0, elencoNdg.length-1);
	return elencoNdg;
}

PdfPageDataentryUtil.prototype.setContoData = function(item, suffix){
	pdfPageFields.setFieldValue("numeroConto"+suffix, item.numeroConto);
	pdfPageFields.setFieldValue("numeroEstesoConto"+suffix, item.numeroEstesoConto);
	pdfPageFields.setFieldValue("ibanConto"+suffix, item.ibanConto);
	pdfPageFields.setFieldValue("dataAperturaConto"+suffix, item.dataAperturaConto);
	pdfPageFields.setFieldValue("annoAperturaConto"+suffix, item.annoAperturaConto);
	pdfPageFields.setFieldValue("paeseIbanConto"+suffix, item.paeseIbanConto);
	pdfPageFields.setFieldValue("cinEuropeoIbanConto"+suffix, item.cinEuropeoIbanConto);
	pdfPageFields.setFieldValue("cinControlloIbanConto"+suffix, item.cinControlloIbanConto);
	pdfPageFields.setFieldValue("abiIbanConto"+suffix, item.abiIbanConto);
	pdfPageFields.setFieldValue("cabIbanConto"+suffix, item.cabIbanConto);
	pdfPageFields.setFieldValue("numeroIbanConto"+suffix, item.numeroIbanConto);
	pdfPageFields.setFieldValue("codiceRuoloConto"+suffix, item.codiceRuoloConto);
	pdfPageFields.setFieldValue("ruoloConto"+suffix, item.ruoloConto);
}

PdfPageDataentryUtil.prototype.clearContoData = function(suffix){
	pdfPageFields.setFieldValue("numeroConto"+suffix, "");
	pdfPageFields.setFieldValue("numeroEstesoConto"+suffix, "");
	pdfPageFields.setFieldValue("ibanConto"+suffix, "");
	pdfPageFields.setFieldValue("dataAperturaConto"+suffix, "");
	pdfPageFields.setFieldValue("annoAperturaConto"+suffix, "");
	pdfPageFields.setFieldValue("paeseIbanConto"+suffix, "");
	pdfPageFields.setFieldValue("cinEuropeoIbanConto"+suffix, "");
	pdfPageFields.setFieldValue("cinControlloIbanConto"+suffix, "");
	pdfPageFields.setFieldValue("abiIbanConto"+suffix, "");
	pdfPageFields.setFieldValue("cabIbanConto"+suffix, "");
	pdfPageFields.setFieldValue("numeroIbanConto"+suffix, "");
	pdfPageFields.setFieldValue("codiceRuoloConto"+suffix, "");
	pdfPageFields.setFieldValue("ruoloConto"+suffix, "");
}

//*************************************************************************************
//Agevolazioni cliente
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindCodiceAgevolazioneAutocomplete = function(input){
	
	$("#codiceAgevolazione").unbind("change").autocomplete({
		source: function(request, response){
			var agev = pdfPageDriver.provideAgevolazioneData(); 
			if(agev == null)
				return;
			
			var	driverCmd = "prgm.pdfwebforms.dataentryutil.CodiceAgevolazioneAutocomplete";
			if(input){
				if(input.driverCmd)
					driverCmd = input.driverCmd;
			}
			
			var pdfMomCode = pdf.getPdfMomCode();
			var codAgente = $("#"+jsCodiceAgenteFieldName).val();
			if(jsCodAgeImpersonato !== "")
				codAgente = jsCodAgeImpersonato;
			var codCliente = $("#ndgCliente1").val();
			
			var numeroContratto = agev.numeroContratto ? agev.numeroContratto : "";
			var modalitaVersamentoAgevolazione = agev.modalitaVersamentoAgevolazione ? agev.modalitaVersamentoAgevolazione : "";
			var codProdottoDispositiva = agev.codProdottoDispositiva ? agev.codProdottoDispositiva : "";
			var codProdottoDispositivaPartenza = agev.codProdottoDispositivaPartenza ? agev.codProdottoDispositivaPartenza : "";
			
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute"
																+"&codAgente="+codAgente
																+"&codCliente="+codCliente
																+"&pdfMomCode="+pdfMomCode
																+"&isOperatoreMom="+jsIsOperatoreMom
																+"&numeroContrattoDestinazione="+numeroContratto
																+"&modalitaVersamentoAgevolazione="+modalitaVersamentoAgevolazione
																+"&codProdottoDispositiva="+codProdottoDispositiva
																+"&codDescrAgevolazioneDipendenti="+jscodDescrAgevolazioneDipendenti
																+"&codProdottoDispositivaPartenza="+codProdottoDispositivaPartenza
																+"&readRequest=false",
					dataType: "json",
					async: true,
				    success: function(data){ response(data); }
				});
			},
		minLength: 0,
		select: function(event, ui){ 
			if(!ui.item.codiceAgevolazione || ui.item.codiceAgevolazione == ""){
				pdfPageDataentryUtil.clearAgevolazioneData();
				return false; 
			}
			pdfPageDataentryUtil.setAgevolazioneData(ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) {
			if($(this).val() != this.savValue)
				pdfPageDataentryUtil.clearAgevolazioneData();
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled("codiceAgevolazione") || $(this).val() != ''){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		if(pdfPageDriver.provideAgevolazioneData() == null)
			return;
		this.savValue = $(this).val();
		if($("#ndgCliente1").val() == '' || $(this).val() != '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function() {
		if($(this).val() == "")
			pdfPageDataentryUtil.clearAgevolazioneData();
	});
}

PdfPageDataentryUtil.prototype.setAgevolazioneData = function(item){
	pdf.setFieldValue("idAgevolazione", item.idAgevolazione);
	pdf.setFieldValue("codiceAgevolazione", item.codiceAgevolazione);
	pdf.setFieldValue("descrizioneAgevolazione", item.descrizioneAgevolazione.replace("<br>","\n"));
	pdf.setFieldValue("tipologiaAgevolazione", item.tipologiaAgevolazione);
	pdf.setFieldValue("modalitaVersamentoAgevolazione", item.modalitaVersamentoAgevolazione);
	pdf.setFieldValue("percentualeAgevolazione", item.percentualeAgevolazione);
	pdf.setFieldValue("importoAgevolazione", item.importoAgevolazione);
}

PdfPageDataentryUtil.prototype.clearAgevolazioneData = function(){
	pdf.setFieldValue("idAgevolazione", "");
	pdf.setFieldValue("codiceAgevolazione", "");
	pdf.setFieldValue("descrizioneAgevolazione", "");
	pdf.setFieldValue("tipologiaAgevolazione", "");
	pdf.setFieldValue("modalitaVersamentoAgevolazione", "");
	pdf.setFieldValue("percentualeAgevolazione", "");
	pdf.setFieldValue("importoAgevolazione", "");
}

//*************************************************************************************
//Prestito cliente
//*************************************************************************************
PdfPageDataentryUtil.prototype.bindPrestitoAutocomplete = function(input){

	var ndgFieldName = input.ndgFieldName;
	var	prestitoFieldName = input.prestitoFieldName;
	var fieldName = input.fieldName;
	var	driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";
	
	var prestitoName = fieldName.substring(prestitoFieldName.length);
	
	var jqObj = $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	jqObj.unbind("change").autocomplete({
		source: function(request, response){
			var ndgCliente = $("#"+ndgFieldName).val();
			if(!driverCmd)
				driverCmd = "prgm.pdfwebforms.dataentryutil.PrestitoAutocomplete";
			$.ajax({
					url: "call.wfem?wfemCmd="+driverCmd+".execute&ndgCliente="+ndgCliente+"&autocompleteFieldName="+prestitoFieldName+"&readRequest=false"+noElementsIndicator,
					dataType: "json",
					async: true,
				    success: function(data){ response(data); }
				});
		},
		minLength: 0,
		select: function(event, ui){ 
			if(ui.item.value == ""){
				pdfPageDataentryUtil.clearPrestitoData(prestitoName);
				return false;		
			}
			pdfPageDataentryUtil.setPrestitoData(ui.item, prestitoName);
			pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName), ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) {
			if($(this).val() != this.savValue)
				pdfPageDataentryUtil.clearPrestitoData(prestitoName);
			if($(this).val() != this.savValue)
				pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName));
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled(fieldName) || $(this).val() != ''){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui){
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null)){
				$(this).autocomplete("close");
			}
		}		
	}).focusin(function() {
		this.savValue = $(this).val();
		if($("#"+ndgFieldName).val() == '' || $(this).val() != '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function() {
		if($(this).val() == "")
			pdfPageDataentryUtil.clearPrestitoData(prestitoName);
	});
}

PdfPageDataentryUtil.prototype.setPrestitoData = function(item, suffix){
	pdfPageFields.setFieldValue("numero"+suffix, item.numero);
	pdfPageFields.setFieldValue("importoErogato"+suffix, item.importoErogato);
	pdfPageFields.setFieldValue("debitoResiduo"+suffix, item.debitoResiduo);
	pdfPageFields.setFieldValue("totaleRate"+suffix, item.totaleRate);
	pdfPageFields.setFieldValue("ratePagate"+suffix, item.ratePagate);
	pdfPageFields.setFieldValue("dataScadenza"+suffix, item.dataScadenza);
	pdfPageFields.setFieldValue("scadenzaProssimaRata"+suffix, item.scadenzaProssimaRata);
	pdfPageFields.setFieldValue("importoProssimaRata"+suffix, item.importoProssimaRata);
	pdfPageFields.setFieldValue("progressivoPiano"+suffix, item.progressivoPiano);
	pdfPageFields.setFieldValue("codStato"+suffix, item.codStato);
	pdfPageFields.setFieldValue("tassoIniziale"+suffix, item.tassoIniziale);
	pdfPageFields.setFieldValue("tassoCorrente"+suffix, item.tassoCorrente);
	pdfPageFields.setFieldValue("taeg"+suffix, item.taeg);
	pdfPageFields.setFieldValue("spread"+suffix, item.spread);
	pdfPageFields.setFieldValue("codCategoria"+suffix, item.codCategoria);
	pdfPageFields.setFieldValue("codSottocategoria"+suffix, item.codSottocategoria);
	pdfPageFields.setFieldValue("codConvenzione"+suffix, item.codConvenzione);
	pdfPageFields.setFieldValue("contoCorrenteRegolamento"+suffix, item.contoCorrenteRegolamento);
}

PdfPageDataentryUtil.prototype.clearPrestitoData = function(suffix){
	pdfPageFields.setFieldValue("numero"+suffix, "");
	pdfPageFields.setFieldValue("importoErogato"+suffix, "");
	pdfPageFields.setFieldValue("debitoResiduo"+suffix, "");
	pdfPageFields.setFieldValue("totaleRate"+suffix, "");
	pdfPageFields.setFieldValue("ratePagate"+suffix, "");
	pdfPageFields.setFieldValue("dataScadenza"+suffix, "");
	pdfPageFields.setFieldValue("scadenzaProssimaRata"+suffix, "");
	pdfPageFields.setFieldValue("importoProssimaRata"+suffix, "");
	pdfPageFields.setFieldValue("progressivoPiano"+suffix, "");
	pdfPageFields.setFieldValue("codStato"+suffix, "");
	pdfPageFields.setFieldValue("tassoIniziale"+suffix, "");
	pdfPageFields.setFieldValue("tassoCorrente"+suffix, "");
	pdfPageFields.setFieldValue("taeg"+suffix, "");
	pdfPageFields.setFieldValue("spread"+suffix, "");
	pdfPageFields.setFieldValue("codCategoria"+suffix, "");
	pdfPageFields.setFieldValue("codSottocategoria"+suffix, "");
	pdfPageFields.setFieldValue("codConvenzione"+suffix, "");
	pdfPageFields.setFieldValue("contoCorrenteRegolamento"+suffix, "");
}

var pdfPageDataentryUtil = new PdfPageDataentryUtil();