var defActionName = "ricercaAction";
function doEnterAction(){
	doAction(defActionName);
	return;	
}

function newCell(cell){
	cell.align = 'center';
	if(cell.propertyName == 'pdfDescr'){
		cell.align = 'left';
		if(cell.row.pdfDescr.length > 120)
			cell.innerHTML = cell.row.pdfDescr.substring(0,120)+"...";
	}
}

function pulisci(){
	setPropertyValue("ricercaPdfParam_pdfCode","");
	setPropertyValue("ricercaPdfParam_pdfMomCode","");
	setPropertyValue("ricercaPdfParam_pdfDescr","");
	setPropertyValue("ricercaPdfParam_callSrvDispositivaBMED","");
	setPropertyValue("ricercaPdfParam_pdfDriverName","");
}

function doRicercaAction(){
	document.searchForm.eseguiRicerca.value = "true";
	wfemHiddenSubmit(document.searchForm,"searchToolbarCont,searchResultCont");
	return true;
}

function doNuovoAction(){
	if(document.nuovoForm.pdfArea.value === ""){
		alert("Selezionare l'area");
		return false;
	}
	document.nuovoForm.submit();
	return true;
}

var righeSelezionate = new Array();
function selezionaRiga(row){
	righeSelezionate = row.selectedRows;
	abilitaPulsantiera(row);
}

function abilitaPulsantiera(row){
	enableAction('apriAction',false);
	enableAction('cancellaAction',false);
	if(righeSelezionate.length <= 0)
		return;

	enableAction('apriAction',true);
	if(row.isFromCatalogoModuli != "true" && row.pdfNumPubblicazioni == 0 && row.pdfNumArchiviazioni == 0)
		enableAction('cancellaAction',true);
}

function doApriAction(){
	if(righeSelezionate.length == 0)
		return;
	document.apriForm.pdfDaGestire_pdfId.value = righeSelezionate[0].pdfId;
	document.apriForm.submit();
	return true;
}

function doCancellaAction(){
	if(righeSelezionate.length == 0)
		return;
	if(!window.confirm("Confermi la cancellazione del modulo ?"))
		return;
	document.cancellaForm.pdfDaGestire_pdfId.value = righeSelezionate[0].pdfId;
	document.cancellaForm.submit();
	return true;
}
