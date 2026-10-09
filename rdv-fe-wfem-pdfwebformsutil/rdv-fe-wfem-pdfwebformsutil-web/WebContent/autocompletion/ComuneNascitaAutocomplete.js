function bindComuneNascitaAutocomplete(input){

	var comuneFieldName = input.comuneFieldName;
	var fieldName = input.fieldName;
	var driverCmd = input.driverCmd;
	var noElementsIndicator = input.noElementsIndicator ? "&noElementsIndicator="+input.noElementsIndicator : "";

	var comuneName = fieldName.substring(comuneFieldName.length);
	var fieldsToBlank = input.fieldsToBlank;

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
				clearComuneNascitaData(comuneName);
				return false;
			}
			setComuneNascitaData(ui.item, comuneName);
			this.savValue = $(this).val();
			return false;
		},
		change: function( event, ui ) {
			if($(this).val() !== this.savValue && $("#"+comuneName).attr("forceSelection") === "true"){
				clearComuneNascitaData(comuneName);
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

	function setComuneNascitaData(item, comuneName){
		pdf.setFieldValue("nazioneComune"+comuneName, item.nazioneComune);
		pdf.setFieldValue("provinciaComune"+comuneName, item.provinciaComune);
		pdf.setFieldValue("comune"+comuneName, item.comune);
		clearFields();
	}

	function clearComuneNascitaData(comuneName){
		pdf.setFieldValue("nazioneComune"+comuneName, "");
		pdf.setFieldValue("provinciaComune"+comuneName, "");
		pdf.setFieldValue("comune"+comuneName, "");
		clearFields();
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
