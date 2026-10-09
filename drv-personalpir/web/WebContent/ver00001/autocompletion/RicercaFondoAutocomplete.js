function bindRicercaFondoAutocomplete(nomeCampo, fondoIdx, suffisso, minLength){
	$("#"+nomeCampo).unbind("change").autocomplete({
		source: function(request, response){
			var tipoSottoscrizione = pdf.getFieldValueAsString("tipoSottoscrizione");
			var tipologiaDoubleChance = "";
			if (pdf.getFieldValueAsString("tipoSottoscrizione") == "DC"){
				tipologiaDoubleChance = pdf.getFieldValueAsString("tipoPicProgrammatoDoubleChance"); 
			}
			var numeroContratto = "";
			numeroContratto = pdf.getFieldValueAsString("numeroContratto");
			var codProdottoPolizza = "";
			codProdottoPolizza = pdf.getFieldValueAsString("codProdottoPolizza");
			var flagTrasformatoPic = "";
			flagTrasformatoPic = pdf.getFieldValueAsString("flagTrasformatoPic");
			
			$.ajax({
					url: "call.wfem?wfemCmd=prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.RicercaFondoAutocomplete.execute"+
					"&denominazione="+$("#"+nomeCampo).val()+
					"&tipoSottoscrizione="+tipoSottoscrizione+
					"&fondiSelezionati="+getFondiSelezionati(suffisso, fondoIdx)+
					"&numeroContratto="+numeroContratto+"&codProdottoPolizza="+codProdottoPolizza+"&flagTrasformatoPic="+flagTrasformatoPic+
					"&readRequest=false",
					dataType: "json",
					async: true,
					success: function(data){ response(data); }
				});
			},
		minLength: minLength,
		select: function(event, ui){ 
			setDatiFondo(ui.item, fondoIdx, suffisso);
			this.savValue = $(this).val();
			return false; 
		},
		change: function( event, ui) {
			if($(this).val().replace(/\s+/g, ' ') != this.savValue.replace(/\s+/g, ' '))
				clearDatiFondo(fondoIdx, suffisso);
		},
		search: function(event, ui){
			if($(this).val() != '' && $(this).val().length < minLength){
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
		if($(this).val() != '')
			return;
	    $(this).autocomplete("search", $(this).val());
	}).focusout(function() {
		if($(this).val() == "")
			clearDatiFondo(fondoIdx, suffisso);
	});
}

function getFondiSelezionati(suffisso, currentIdx){
	var elencoFondi = "";
	
	if (suffisso == null)
		return "";
	
	for(var i=0;;i++){
		if(document.getElementById("codiceFondo"+suffisso+i) == null)
			break;

		if (i == currentIdx)
			continue;

		var codfondo = pdf.getFieldValue("codiceFondo"+suffisso+i);
		if(codfondo.length > 0){
			if (elencoFondi.length > 0)
				elencoFondi+=",";
			elencoFondi+= "'"+pdf.getFieldValue("codiceFondo"+suffisso+i)+"'";
		}	
	}

	if(elencoFondi.length == 0)
		return "";
	return elencoFondi;
}

function setDatiFondo(obj, idx, suffisso){
	pdf.setFieldValue("lineaFondo"+suffisso+idx,obj.lineaFondo);
	pdf.setFieldValue("codiceFondo"+suffisso+idx,obj.codiceFondo);
	pdf.setFieldValue("importoMinimoFondo"+suffisso+idx,obj.importoMinimo);
	pdf.setFieldValue("societaFondo"+suffisso+idx,obj.societaFondo);
	pdf.setFieldValue("isinFondo"+suffisso+idx,obj.isinFondo);
	pdf.setFieldValue("descrizioneFondo"+suffisso+idx,obj.descrizioneFondo);
	pdf.setFieldValue("controvaloreFondo"+suffisso+idx,obj.controvaloreFondo);	
	pdfPageDriver.onChange("isinFondo"+suffisso+idx);
}

function clearDatiFondo(idx, suffisso){
	pdf.setFieldValue("lineaFondo"+suffisso+idx,"");
	pdf.setFieldValue("codiceFondo"+suffisso+idx,"");
	pdf.setFieldValue("importoMinimoFondo"+suffisso+idx,"");
	pdf.setFieldValue("societaFondo"+suffisso+idx,"");
	pdf.setFieldValue("isinFondo"+suffisso+idx,"");
	pdf.setFieldValue("descrizioneFondo"+suffisso+idx,"");
	pdf.setFieldValue("percentualeFondo"+suffisso+idx,"");
	pdf.setFieldValue("importoFondo"+suffisso+idx,"");
	pdf.setFieldValue("controvaloreFondo"+suffisso+idx,"");
}