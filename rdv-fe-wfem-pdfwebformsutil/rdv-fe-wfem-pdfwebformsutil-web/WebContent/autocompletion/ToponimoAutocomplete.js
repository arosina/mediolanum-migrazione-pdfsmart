function bindToponimoAutocomplete(toponimoFieldName, fieldsToBlank){
	$("#" + toponimoFieldName).unbind("change").autocomplete({
		source: function(request, response){			
			$.ajax({
					url: "call.wfem?wfemCmd=prgm.pdfwebformsutil.drivers.autocompletion.toponimo.ToponimoAutocomplete.execute&readRequest=false" +
					"&autocompleteTerm="+ $("#" + toponimoFieldName).val(),
					dataType: "json",
					async: true,
					success: function(data){ response(data); }
				});
			},
		minLength: 1,
		select: function(event, ui){ 
			if(ui.item.value === ""){
				clearToponimoData();
				return false;
			}
			setToponimoData(ui.item);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) {
			if($(this).val() !== this.savValue)
				clearToponimoData();
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
		if($(this).val() !== '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function(event) {
		if($(this).val() === "" || 
				pdf.getFieldValue(event.target.id) === "")
			clearToponimoData();
	});
	
	function setToponimoData(item){
		pdf.setFieldValue("cod" + capitalizeFirstLetter(toponimoFieldName), item.value);
		pdf.setFieldValue(toponimoFieldName, item.label);
		clearFields();
	}

	function clearToponimoData(){
		pdf.setFieldValue("cod" + capitalizeFirstLetter(toponimoFieldName), "");
		pdf.setFieldValue(toponimoFieldName, "");	
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
	
	function capitalizeFirstLetter(string) {
	    return string.charAt(0).toUpperCase() + string.slice(1);
	}
}