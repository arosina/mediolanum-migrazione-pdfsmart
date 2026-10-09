var defActionName = "apriAction";
function doEnterAction(){
	doAction(defActionName);
	return;	
}

function newCell(cell){
	cell.align = 'center';
	if(cell.propertyName == 'pdfAnag_pdfDescr')
		cell.align = 'left';
	
	if(cell.propertyName == 'comandi')
		cell.title = "";
	
	if(cell.propertyName == 'pdfAnag_pdfDescr'){
		cell.title = cell.row.pdfAnag_pdfCode + " - "+ cell.row.pdfAnag_pdfDescr;
	}
}

function apri(pdfInstanceId){
	wait();
	document.apriForm.pdfInstanceId.value = pdfInstanceId;
	document.apriForm.submit();
	return false;
}

function cancella(pdfInstanceId){
	if(!window.confirm("Confermi la cancellazione della bozza ?"))
		return;
	wait();
	document.cancellaForm.idToDelete.value = pdfInstanceId;
	document.cancellaForm.submit();
	return false;
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

