function bindComuneNascitaBeneficiarioAutocomplete(input){

	var comuneFieldName = input.comuneFieldName;
	var fieldName = input.fieldName;
	var driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";

	var comuneName = fieldName.substring(comuneFieldName.length);
	var fieldsToBlank = input.fieldsToBlank;
	
	var prefissoBeneficiario = input.prefissoBeneficiario;
	var indiceBeneficiario = input.indiceBeneficiario;
	var indiceTitolare = input.indiceTitolare;

	if(!driverCmd){
		driverCmd = " prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneNascitaAutocomplete";
	}

	$("#"+fieldName).unbind("change").autocomplete({
		source: function(request, response){
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
			if(ui.item.value === ""){
				clearComuneNascitaBeneficiarioData(comuneName, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
				return false;
			}
			setComuneNascitaBeneficiarioData(ui.item, comuneName, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
			this.savValue = $(this).val();
			return false;
		},
		change: function( event, ui ) {
			if($(this).val() !== this.savValue){
				if ($("#"+comuneName).attr("forceSelection") === "true"){
					clearComuneNascitaBeneficiarioData(comuneName, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
				}
				if (indiceTitolare > -1)
					changeAnagraficaTitolarePep(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
				else
					changeAnagraficaPep(prefissoBeneficiario, indiceBeneficiario);
			}
		},
		search: function(event, ui){
			if($(this).val() !== '' && $(this).val().length < 1){
				event.preventDefault();
				return false;
			}
			return true;
		},
		open: function( event, ui ) {
			if($(this).attr("id") !== (document.activeElement ? document.activeElement.id : null) || $(this).attr('readonly')) {
				$(this).autocomplete("close");
			}
		}
	}).focusin(function() {
		this.savValue = $(this).val();
	});

	function setComuneNascitaBeneficiarioData(item, comuneName, prefissoBeneficiario, indiceBeneficiario, indiceTitolare){
		pdf.setFieldValue("nazioneComune"+comuneName, item.nazioneComune);
		pdf.setFieldValue("provinciaComune"+comuneName, item.provinciaComune);
		pdf.setFieldValue("comune"+comuneName, item.comune);
		clearFields();

		if (indiceTitolare > -1)
			changeAnagraficaTitolarePep(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
		else
			changeAnagraficaPep(prefissoBeneficiario, indiceBeneficiario);		
	}

	function clearComuneNascitaBeneficiarioData(comuneName, prefissoBeneficiario, indiceBeneficiario, indiceTitolare){
		pdf.setFieldValue("nazioneComune"+comuneName, "");
		pdf.setFieldValue("provinciaComune"+comuneName, "");
		pdf.setFieldValue("comune"+comuneName, "");
		clearFields();
		
		if (indiceTitolare > -1)
			changeAnagraficaTitolarePep(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
		else
			changeAnagraficaPep(prefissoBeneficiario, indiceBeneficiario);		
	}

	function clearFields() {
		if (typeof fieldsToBlank != 'undefined') {
			for(var i = 0; i < fieldsToBlank.length; i++) {
				var fieldId = fieldsToBlank[i];
				pdf.setFieldValue(fieldId, "");
			}
		}
	}
}
