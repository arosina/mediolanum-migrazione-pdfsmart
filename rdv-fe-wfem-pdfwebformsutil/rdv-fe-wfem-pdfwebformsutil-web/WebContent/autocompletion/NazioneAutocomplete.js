function bindNazioneAutocomplete(nazioneFieldName, fieldsToBlank){
	$("#" + nazioneFieldName).unbind("change").autocomplete({
		source: function(request, response){			
			$.ajax({
					url: "call.wfem?wfemCmd=prgm.pdfwebformsutil.drivers.autocompletion.nazione.NazioneAutocomplete.execute&readRequest=false" +
					"&nazione="+ $("#" + nazioneFieldName).val(),
					dataType: "json",
					async: true,
					success: function(data){ response(data); }
				});
			},
		minLength: 3,
		select: function(event, ui){ 
			if(ui.item.value === ""){
				clearNazioneData();
				return false;
			}
			setNazioneData(ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) {
			if($(this).val() !== this.savValue)
				clearNazioneData();
		},
		search: function(event, ui){
			if($(this).val() !== '' && $(this).val().length < 3){
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
		if($(this).val() !== '')
			return;
	    $(this).autocomplete("search", $(this).val());
	});
	
	function setNazioneData(item){
		pdf.setFieldValue(nazioneFieldName, item.label);
		clearFields();
	}

	function clearNazioneData(){	
		pdf.setFieldValue(nazioneFieldName, "");	
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