function bindAbiBancaAutocomplete(abiFieldName, descFieldName, fieldsToBlank){
	$("#" + descFieldName).unbind("change").autocomplete({
		source: function(request, response){			
			$.ajax({
					url: "call.wfem?wfemCmd=prgm.pdfwebformsutil.drivers.autocompletion.abicab.AbiBancaAutocomplete.execute&readRequest=false" +
					"&inputDenominazione="+ $("#" + descFieldName).val(),
					dataType: "json",
					async: true,
					success: function(data){ response(data); }
				});
			},
		minLength: 3,
		select: function(event, ui){ 
			if(ui.item.value === ""){
				clearBancaData();
				return false;
			}
			setBancaData(ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) {
			if($(this).val() !== this.savValue)
				clearBancaData();
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
	}).focusout(function() {
		if($(this).val() === "")
			clearBancaData();
	});
	
	function setBancaData(item){
		pdf.setFieldValue(abiFieldName, item.value);	
		pdf.setFieldValue(descFieldName, item.descrizioneBanca);
		clearFields();
	}

	function clearBancaData(){	
		pdf.setFieldValue(abiFieldName, "");
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