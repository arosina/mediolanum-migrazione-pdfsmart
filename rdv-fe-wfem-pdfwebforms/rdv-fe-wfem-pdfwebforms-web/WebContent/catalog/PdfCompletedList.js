PdfCompletedList = function(){
}

PdfCompletedList.prototype.doRicercaAction = function(){
	wait();
	wfemHiddenSubmit(document.ricercaCompletatiForm,"pdfCompletedListSortFormCont,pdfCompletedListGridCont");
}

PdfCompletedList.prototype.onNewHeader = function(cell){
	cell.title = "";
	var img = "";
	if(cell.propertyName != "pdf"){
		
		if(document.pdfCompletedListSortForm.pdfCompletedListParams_orderField.value == cell.propertyName){
			if(document.pdfCompletedListSortForm.pdfCompletedListParams_orderType.value == "asc"){
				img = "<img style='cursor:pointer;' orderField='"+cell.propertyName+"' orderType='desc' onclick='pdfCompletedList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/ordinamentoCrescente.png'>";
			}else{
				img = "<img style='cursor:pointer;' orderField='"+cell.propertyName+"' orderType='asc' onclick='pdfCompletedList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/ordinamentoDecrescente.png'>";
			}
		}else{
			img = "<img style='cursor:pointer;'  orderField='"+cell.propertyName+"' orderType='asc' onclick='pdfCompletedList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/noOrdinamento.png'>";
		}
		
	}
	cell.innerHTML = "<table width='100%'><tr>"+
						"<td style='border:none;text-align:left;padding-left:5;'>"+cell.innerHTML+"</td>"+
						"<td style='border:none;text-align:right;padding-right:5;'>"+img+"</td>"+
					 "</tr></table>";
}

PdfCompletedList.prototype.orderCol = function(colInfo){
	var orderField = colInfo.getAttribute("orderField");
	var orderType = colInfo.getAttribute("orderType");
	
	document.pdfCompletedListSortForm.pdfCompletedListParams_orderField.value = orderField; 
	document.pdfCompletedListSortForm.pdfCompletedListParams_orderType.value = orderType; 
	wfemHiddenSubmit(document.pdfCompletedListSortForm,"pdfCompletedListSortFormCont,pdfCompletedListGridCont");
}

PdfCompletedList.prototype.onNewCell = function (cell){
	
	cell.align = 'center';
	if(cell.propertyName == 'pdfAnag_pdfDescr' || cell.propertyName == 'clienti')
		cell.align = 'left';
	
	if(cell.propertyName == "pdf"){
		cell.title = "Modulo compilato";
		cell.onmouseover = function(){event.cancelBubble = true;};
		cell.onmouseout = function(){event.cancelBubble = true;};
		cell.onclick = function(){event.cancelBubble = true;};
		cell.style.cursor = 'default';
		
		var stampaAbilitata = (cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE || 
							   cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE ||
							   cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_COPERNICO);
		if(cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA)
			stampaAbilitata = false;
		if(cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_CARTA_LIBERA)
			stampaAbilitata = true;
		
		var imgName = "cartaChimica.jpg";
		if(stampaAbilitata)
			imgName = "downloadPdf.jpg";
		var inhtml =
			"<table class='text' width='100%' height='100%' cellpadding='0' cellspacing='0' style='background-color:white;'>" +
				"<tr>"+
					"<td align='center'>"+
						"<img src='"+__retrieveResourceUrl()+"/catalog/images/"+imgName+"' style='cursor:pointer;' onclick='pdfCompletedList.openPdfModuloCompilato(\""+cell.row.pdfInstanceId+"\");'>"+
					"</td>" +
				"</tr>" +
			"</table>";
		cell.innerHTML = inhtml;
	}
	
	if(cell.propertyName == 'pdfAnag_pdfDescr'){
		cell.title = cell.row.pdfAnag_pdfCode + " - "+ cell.row.pdfAnag_pdfDescr;
	}
	
}

PdfCompletedList.prototype.openPdfModuloCompilato = function(pdfInstanceId){
	document.datiPdfCompletedList.pdfInstanceId.value = pdfInstanceId;
	openPdfObject(document.datiPdfCompletedList,"Modulo");
}

var pdfCompletedList = new PdfCompletedList();
