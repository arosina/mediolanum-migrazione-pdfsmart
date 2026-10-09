function bindCabFilialeAutocomplete(abiFieldName, cabFieldName, descFieldName, fieldsToBlank){
	$("#" + descFieldName).unbind("change").autocomplete({
		source: function(request, response){			
			$.ajax({
					url: "call.wfem?wfemCmd=prgm.pdfwebformsutil.drivers.autocompletion.abicab.CabFilialeAutocomplete.execute&readRequest=false" +
					"&inputAbi="+ $("#" + abiFieldName).val() +
					"&inputDenominazione="+ $("#" + descFieldName).val(),
					dataType: "json",
					async: true,
					success: function(data){ response(data); }
				});
			},
		minLength: 3,
		select: function(event, ui){ 
			if(ui.item.value === ""){
				clearFilialeData();
				return false;
			}
			setFilialeData(ui.item);
			this.savValue = $(this).val().replace(/\u00a0/g, " ");
			return false;
		},
		change: function( event, ui ) {
			if($(this).val() !== this.savValue)
				clearFilialeData();
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
		this.savValue = $(this).val().replace(/\u00a0/g, " ");
		if($(this).val() !== '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function() {
		if($(this).val() === "")
			clearFilialeData();
	});
	
	function setFilialeData(item){
		pdf.setFieldValue(cabFieldName, item.value);	
		pdf.setFieldValue(descFieldName, item.descrizioneFiliale);
		clearFields();
	}

	function clearFilialeData(){	
		pdf.setFieldValue(cabFieldName, "");
		pdf.setFieldValue(descFieldName, "");	
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