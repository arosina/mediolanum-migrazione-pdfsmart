function doEnterAction(){
	doRicercaAction();;
	return;	
}

function pulisci(){
	setPropertyValue("params_cli1_nome","");
	setPropertyValue("params_cli1_cognome","");
	setPropertyValue("params_pdfAnag_pdfDescr","");
	setPropertyValue("params_dataInizio","");
	setPropertyValue("params_dataFine","");
}

function doRicercaAction(){
	wait();
	wfemHiddenSubmit(document.ricerca,"resultCont");
}

function newCell(cell){
	
	cell.align = 'center';
	if(cell.propertyName == 'pdfAnag_pdfDescr' || cell.propertyName == 'clienti')
		cell.align = 'left';
	
	if(cell.propertyName == "pdf"){
		cell.title = "Modulo compilato";
		cell.onmouseover = function(){event.cancelBubble = true;};
		cell.onmouseout = function(){event.cancelBubble = true;};
		cell.onclick = function(){event.cancelBubble = true;};
		cell.style.cursor = 'default';
		cell.style.padding = '0px';
		
		var stampaAbilitata = ( cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE || 
								cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE || 
								cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_COPERNICO);
		if(cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA)
			stampaAbilitata = false;
		if(cell.row.pdfCompilationMode == jsMODALITA_SOTTOSCRIZIONE_CARTA_LIBERA)
			stampaAbilitata = true;
		
		var imgName = "document.png";
		if(stampaAbilitata)
			imgName = "pdf.gif";
		var inhtml =
			"<table class='text' width='100%' height='100%' cellpadding='0' cellspacing='0' style='background-color:white;'>" +
				"<tr>"+
					"<td align='center'>"+
						"<img src='"+__retrieveResourceUrl()+"/images/"+imgName+"' style='cursor:pointer;' onclick='openPdfModuloCompilato(\""+cell.row.pdfInstanceId+"\");'>"+
					"</td>" +
				"</tr>" +
			"</table>";
		cell.innerHTML = inhtml;
	}
	
	if(cell.propertyName == 'pdfAnag_pdfDescr'){
		cell.title = cell.row.pdfAnag_pdfCode + " - "+ cell.row.pdfAnag_pdfDescr;
	}
	
}

function openPdfModuloCompilato(pdfInstanceId){
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.OpenCompletedPdf.executeOnPopup";
	document.dati.pdfInstanceId.value = pdfInstanceId;
	openPdfObject(document.dati,"Modulo");
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

