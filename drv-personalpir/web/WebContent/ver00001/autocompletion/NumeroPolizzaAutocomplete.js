function bindNumeroPolizzaAutocomplete(){
	$("#numeroPolizza").unbind("change").autocomplete({
		source: function(request, response){
			var codiceAgente = jsCodAgeFiltro;
			var ndgCliente = $("#ndgCliente1").val();
			
			// rfc #257299 - Se non esiste la forma contrattuale dobbiamo escludere le polizze PIC
			var findOnlyPAC="";
			if(!pdf.fieldExist("formaContrattuale"))				
				findOnlyPAC = "&findOnlyPAC=true";
			
			$.ajax({
					url: "call.wfem?wfemCmd=prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutocomplete.execute&codiceAgente="+codiceAgente+"&ndgCliente="+ndgCliente+"&readRequest=false"+findOnlyPAC,
					dataType: "json",
					async: true,
					success: function(data){ response(data); }
				});
		},
		minLength: 0,
		delay: 700,
		select: function(event, ui){ 
			setPolizzaData(ui.item);
			if(this.savValue != $(this).val()) {
				pdfPageFields.dispatchChangeEvent(document.getElementById('numeroPolizza'));
			}
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui ) {
			if($(this).val() != this.savValue)
				clearPolizzaData();
		},
		search: function(event, ui){
			if(!pdfPageFields.isFieldEnabled("numeroPolizza") || $(this).val() != ''){
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
		if(jsCodAgeFiltro == '' || $("#ndgCliente1").val() == '' || $(this).val() != '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function() {
		if($(this).val() == "")
			clearPolizzaData();
	});
}

function setPolizzaData(item){
	pdf.setFieldValue("numeroPolizza", item.value);
	pdf.setFieldValue("numeroContratto", item.numeroContratto);
	pdf.setFieldValue("codProdottoPolizza", item.codProdottoPolizza); 
	pdf.setFieldValue("flagTrasformatoPic", item.flagTrasformatoPic);
	pdf.setFieldValue("formaContrattuale", item.formaContrattuale);
	
	managePolizza(false);
}

function clearPolizzaData(){
	pdf.setFieldValue("numeroPolizza", "");
	pdf.setFieldValue("numeroContratto", "");
	pdf.setFieldValue("codProdottoPolizza", "");
	pdf.setFieldValue("flagTrasformatoPic", "");
	pdf.setFieldValue("importoPiano", "");
	pdf.setFieldValue("frequenzaPiano", "");
	pdf.setFieldValue("formaContrattuale", "");

	managePolizza(true);
}
