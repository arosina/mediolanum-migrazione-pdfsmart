function newCell(cell){
	cell.align = "center";
	if(cell.propertyName == 'pdfDescr'){
		cell.align = "left";
		cell.style.paddingLeft = 10;
		cell.title = cell.row.pdfCode + " - "+ cell.row.pdfDescr;
	}else{
		cell.title = "";
	}
	
}

function downloadPdf(pdfId){
	if(navigator.userAgent.indexOf('iPad') != -1){
		var cmd = "prgm.pdfwebforms.stream.PdfOpen.execute";
		var url = "modulo.wfem?wfemCmd="+cmd+"&pdfId="+pdfId;
		window.open(url);
	}else{
		var cmd = "prgm.pdfwebforms.stream.PdfDownload.execute";
		var url = "modulo.wfem?wfemCmd="+cmd+"&pdfId="+pdfId;
		document.getElementById("utilIFrame").src = url;
	}
}

function compila(pdfId){
	wait();
	document.dati.pdfId.value = pdfId;
	document.dati.submit();
}

function wait(){
	try{$(".waitDiv").show();jsWait.start();}catch(e){}
}

function endWait(){
	try{$(".waitDiv").hide();jsWait.stop();}catch(e){}
}

function onWfemHiddenSubmitEnd(){
	endWait();
}

function doEnterAction(){
	doFiltra();
	return;	
}

function doFiltra(){
	var resetPlace = false;
	if($("#params_pdfDescr").val() == $("#params_pdfDescr").attr("placeholder")){
		$("#params_pdfDescr").val("");
		resetPlace = true;
	}
	$("#params_pdfDescr").autocomplete("close");
	wait();
	wfemHiddenSubmit(document.filtraForm,"resultCont");
	if(resetPlace)
		$("#params_pdfDescr").focus().blur();
	return false;
}

function bindModuloAutocomplete(){
	$("#params_pdfDescr").autocomplete({
		source: function( request, response){
					$.ajax({
						url: "call.wfem?wfemCmd=prgm.pdfwebforms.dataentryutil.PdfDescrAutocomplete.execute&readRequest=false",
						dataType: "json",
						data: { pdfDescr: request.term, areas: jsAreas },
						success: function(data) { response(data); }
					});
		},
		minLength: 1,
		focus: function(event, ui){ 
			$("#params_pdfDescr").val(ui.item.pdfDescr); 
			return false; 
		},
		select: function(event, ui){ 
			$("#params_pdfDescr").val(ui.item.pdfDescr); 
			doFiltra();
			return false; 
		}
	});
}

