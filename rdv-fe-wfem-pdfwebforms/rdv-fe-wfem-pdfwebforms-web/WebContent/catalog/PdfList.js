PdfList = function(){
}

PdfList.prototype.onChangeSegmento = function(){
	var fr = document.ricercaListForm;
	fr.wfemCmd.value = 'prgm.pdfwebforms.catalog.OnChangeSegmento.execute';
	var combo = document.getElementById('pdfListParams_codiceProdotto');
	combo.disabled=true;
	combo.value='';
	combo.options[0].text='Caricamento in corso...';
	wfemHiddenSubmit(fr,'pdfListParams_codiceProdottoField');
}

PdfList.prototype.onNewHeader = function(cell){
	cell.title = "";
	var img = "";
	if(cell.propertyName == "pdfDescr" || cell.propertyName == "pdfModulo_tipoModulo"){
		
		if(document.pdfListSortForm.pdfListParams_orderField.value == cell.propertyName){
			if(document.pdfListSortForm.pdfListParams_orderType.value == "asc"){
				img = "<img style='cursor:pointer;' orderField='"+cell.propertyName+"' orderType='desc' onclick='pdfList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/ordinamentoCrescente.png'>";
			}else{
				img = "<img style='cursor:pointer;' orderField='"+cell.propertyName+"' orderType='asc' onclick='pdfList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/ordinamentoDecrescente.png'>";
			}
		}else{
			img = "<img style='cursor:pointer;'  orderField='"+cell.propertyName+"' orderType='asc' onclick='pdfList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/noOrdinamento.png'>";
		}
		
	}
	cell.innerHTML = "<table width='100%'><tr>"+
						"<td style='border:none;text-align:left;padding-left:5;'>"+cell.innerHTML+"</td>"+
						"<td style='border:none;text-align:right;padding-right:5;'>"+img+"</td>"+
					 "</tr></table>";
}

PdfList.prototype.orderCol = function(colInfo){
	var orderField = colInfo.getAttribute("orderField");
	var orderType = colInfo.getAttribute("orderType");
	
	document.pdfListSortForm.pdfListParams_orderField.value = orderField; 
	document.pdfListSortForm.pdfListParams_orderType.value = orderType; 
	wfemHiddenSubmit(document.pdfListSortForm,"pdfListSortFormCont,pdfListGridCont");
}

PdfList.prototype.onNewCell = function(cell){
	cell.align = "center";
	cell.title = "";
	
	if(cell.propertyName == 'pdfDescr'){
		cell.align = "left";
		cell.style.paddingLeft = 10;
	}

	if(cell.propertyName == 'pdfDescr')
		cell.title = cell.row.pdfCode + " - "+ cell.row.pdfDescr;
	
	if(cell.propertyName == 'elencoProdotti')
		cell.align = "left";
}

PdfList.prototype.dettaglioModulo = function(pdfId,pdfCode,hasNoteOperative){
	openPopup("prgm.pdfwebforms.catalog.PdfDettaglioModulo",
			  "BrowserInstance="+__getBrowserInstance()+"&dettaglioModulo_pdfId="+pdfId,
			  null,null,"Modulo "+pdfCode,700,700,true);
}

PdfList.prototype.downloadPdf = function(pdfId){
	if(navigator.userAgent.indexOf('iPad') != -1){
		var cmd = "prgm.pdfwebforms.stream.PdfOpen.execute";
		var url = "modulo.wfem?wfemCmd="+cmd+"&pdfId="+pdfId+"&BrowserInstance="+__getBrowserInstance();
		window.open(url);
	}else{
		var cmd = "prgm.pdfwebforms.stream.PdfDownload.execute";
		var url = "modulo.wfem?wfemCmd="+cmd+"&pdfId="+pdfId+"&BrowserInstance="+__getBrowserInstance();
		document.getElementById("utilIFrame").src = url;
	}
}

PdfList.prototype.openCompilationExample = function(obj,pdfId){
	var cmd = "prgm.pdfwebforms.catalog.OpenCompilationExample.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&pdfId="+pdfId+"&BrowserInstance="+__getBrowserInstance();
	if(obj.getAttribute("contentType").indexOf("application/pdf") >= 0){
		if(navigator.userAgent.indexOf('iPad') != -1){
			window.open(url);
		}else{
			openPdfObject(url,"Esempio di compilazione");
		}
	}else{
		document.getElementById("utilIFrame").src = url;
	}
}

PdfList.prototype.compila = function(pdfId){
	wait();
	document.datiPdfList.pdfId.value = pdfId;
	document.datiPdfList.submit();
}

PdfList.prototype.doFiltra = function(){
	var resetPlace = false;
	if($("#pdfListParams_descrizione").val() == $("#pdfListParams_descrizione").attr("placeholder")){
		$("#pdfListParams_descrizione").val("");
		resetPlace = true;
	}
	$("#pdfListParams_descrizione").autocomplete("close");
	wait();
	document.ricercaListForm.wfemCmd.value = "prgm.pdfwebforms.catalog.FilterPdfList.execute";
	wfemHiddenSubmit(document.ricercaListForm,"pdfListSortFormCont,pdfListGridCont");
	if(resetPlace)
		$("#pdfListParams_descrizione").focus().blur();
	return false;
}

PdfList.prototype.doAnnulla = function(){
	setPropertyValue("pdfListParams_descrizione","");
	setPropertyValue("pdfListParams_codiceTipoModulo","");
	setPropertyValue("pdfListParams_codiceSegmento","");
	setPropertyValue("pdfListParams_codiceProdotto","");
	setPropertyValue("pdfListParams_mostraSoloInFD",true);
	wait();
	document.ricercaListForm.wfemCmd.value = "prgm.pdfwebforms.catalog.FilterPdfList.execute";
	wfemHiddenSubmit(document.ricercaListForm,"pdfListCont");
}

PdfList.prototype.doFiltraFilteredList = function(){
	document.ricercaListForm.wfemCmd.value = "prgm.pdfwebforms.catalog.FilterPdfList.execute";
	wfemHiddenSubmit(document.ricercaListForm,"pdfListSortFormCont,pdfListGridCont");
	return false;
}

PdfList.prototype.bindModuloAutocomplete = function(){
	$("#pdfListParams_descrizione").autocomplete({
		source: function( request, response){
					$.ajax({
						url: "call.wfem?wfemCmd=prgm.pdfwebforms.catalog.dataentryutil.DescrizioneAutocomplete.execute&readRequest=false",
						dataType: "json",
						data: { pdfListParams_descrizione: request.term },
						success: function(data) { response(data); }
					});
		},
		minLength: 1,
		focus: function(event, ui){ 
			$("#pdfListParams_descrizione").val(ui.item.descrizione); 
			return false; 
		},
		select: function(event, ui){ 
			$("#pdfListParams_descrizione").val(ui.item.descrizione); 
			return false; 
		}
	});
}

var pdfList = new PdfList();
