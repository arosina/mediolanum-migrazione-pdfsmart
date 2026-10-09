PdfDraftList = function(){
}

PdfDraftList.prototype.onNewHeader = function(cell){
	cell.title = "";
	var img = "";
	if(cell.propertyName != "comandi"){
		
		if(document.pdfDraftListSortForm.pdfDraftListParams_orderField.value == cell.propertyName){
			if(document.pdfDraftListSortForm.pdfDraftListParams_orderType.value == "asc"){
				img = "<img style='cursor:pointer;' orderField='"+cell.propertyName+"' orderType='desc' onclick='pdfDraftList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/ordinamentoCrescente.png'>";
			}else{
				img = "<img style='cursor:pointer;' orderField='"+cell.propertyName+"' orderType='asc' onclick='pdfDraftList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/ordinamentoDecrescente.png'>";
			}
		}else{
			img = "<img style='cursor:pointer;'  orderField='"+cell.propertyName+"' orderType='asc' onclick='pdfDraftList.orderCol(this);' src='"+__jsWebApp+"/catalog/images/noOrdinamento.png'>";
		}
		
	}
	cell.innerHTML = "<table width='100%'><tr>"+
						"<td style='border:none;text-align:left;padding-left:5;'>"+cell.innerHTML+"</td>"+
						"<td style='border:none;text-align:right;padding-right:5;'>"+img+"</td>"+
					 "</tr></table>";
}

PdfDraftList.prototype.orderCol = function(colInfo){
	var orderField = colInfo.getAttribute("orderField");
	var orderType = colInfo.getAttribute("orderType");
	
	document.pdfDraftListSortForm.pdfDraftListParams_orderField.value = orderField; 
	document.pdfDraftListSortForm.pdfDraftListParams_orderType.value = orderType; 
	wfemHiddenSubmit(document.pdfDraftListSortForm,"pdfDraftListSortFormCont,pdfDraftListGridCont");
}

PdfDraftList.prototype.onNewCell = function(cell){
	cell.align = 'center';
	if(cell.propertyName == 'pdfAnag_pdfDescr')
		cell.align = 'left';
	
	if(cell.propertyName == 'comandi')
		cell.title = "";
	
	if(cell.propertyName == 'pdfAnag_pdfDescr'){
		cell.title = cell.row.pdfAnag_pdfCode + " - "+ cell.row.pdfAnag_pdfDescr;
	}
}

PdfDraftList.prototype.apri = function(pdfInstanceId){
	wait();
	document.draftApriForm.pdfInstanceId.value = pdfInstanceId;
	document.draftApriForm.submit();
	return false;
}

PdfDraftList.prototype.cancella = function(pdfInstanceId){
	if(!window.confirm("Confermi la cancellazione della bozza ?"))
		return;
	wait();
	document.draftCancellaForm.idToDelete.value = pdfInstanceId;
	document.draftCancellaForm.submit();
	return false;
}

var pdfDraftList = new PdfDraftList();
