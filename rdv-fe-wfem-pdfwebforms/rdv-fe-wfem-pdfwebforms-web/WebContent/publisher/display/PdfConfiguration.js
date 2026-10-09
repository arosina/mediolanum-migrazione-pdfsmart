function onChangeProdottoPrit(){
	var fr = document.datiAnag;
	if(fr == undefined)
		fr = document.datiPublication;
	fr.wfemCmd.value = 'prgm.pdfwebforms.publisher.events.OnChangeProdottoPrit.execute';
	var combo = document.getElementById('pdfAnag_pdfCodOperazionePrit');
	combo.disabled=true;
	combo.value='';
	combo.options[0].text='Caricamento in corso...';
	wfemHiddenSubmit(fr,'pdfAnag_pdfCodOperazionePritField');
}

function openApplReferencesPopup(){
	openModalPopup("prgm.pdfwebforms.publisher.display.PdfApplReferences",
	  				"BrowserInstance="+__getBrowserInstance(),
	  				null,null,"Riferimenti applicativi",400,850,true);
}

function doAggiornaAnagAction(){
	if(!window.confirm("Confermi l'aggiornamento dei dati anagrafici del modulo ?"))
		return false;
	document.datiAnag.wfemCmd.value = "prgm.pdfwebforms.publisher.business.AggiornaAnag.execute";
	wfemHiddenSubmit(document.datiAnag,"body");
	return true;
}

function clearFilePdf(){
	document.datiPublication.pdfAnag_fileIsChanged.value = "true";
	document.datiPublication.wfemCmd.value = "prgm.pdfwebforms.publisher.business.ClearFilePdf.execute";
	wfemHiddenSubmit(document.datiPublication,"body");
	return true;
}

function restoreFilePdf(){
	document.datiPublication.pdfAnag_fileIsChanged.value = "false";
	document.datiPublication.wfemCmd.value = "prgm.pdfwebforms.publisher.business.RestoreFilePdf.execute";
	wfemHiddenSubmit(document.datiPublication,"body");
	return true;
}

function doGobackAction(){
	document.gobackForm.submit();
	return true;
}

function doSalvaAction(){
	document.datiPublication.wfemCmd.value = "prgm.pdfwebforms.publisher.business.SalvaConf.execute";
	wfemHiddenSubmit(document.datiPublication,"body");
	return true;
}

function doChiudiAreaDiLavoro(){
	document.annullaModificheForm.submit();
	return true;
}

function doAnnullaModificheAction(){
	if(!window.confirm("Confermi la chiusura dell'area di lavoro ?\nLe modifiche effettuate andranno perse."))
		return false;
	document.annullaModificheForm.submit();
	return true;
}

function doSalvaPubblicazioneAction(){
	if(!window.confirm("Confermi l'aggiornamento della pubblicazione ?"))
		return false;
	document.datiPublication.pubblicaComeNuovaPubblicazione.value = "false";
	document.datiPublication.wfemCmd.value = "prgm.pdfwebforms.publisher.business.PubblicaConf.execute";
	wfemHiddenSubmit(document.datiPublication,"body");
	return true;
}

function doPubblicaComeNuovaPubblicazione(){
	if(!window.confirm("Confermi la pubblicazione del modulo ?"))
		return false;
	document.datiPublication.pubblicaComeNuovaPubblicazione.value = "true";
	document.datiPublication.wfemCmd.value = "prgm.pdfwebforms.publisher.business.PubblicaConf.execute";
	wfemHiddenSubmit(document.datiPublication,"body");
	return true;
}

function visualizaImmaginePagina(pdfId,pdfPublicationId,pdfPageNum){
    var d = new Date();
    var now = d.getTime();
	var cmd = "prgm.pdfwebforms.publisher.display.PdfPageImageContainer.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&pdfId="+pdfId+"&pdfPublicationId="+pdfPublicationId+"&pdfPageNum="+pdfPageNum+"&timenow="+now;
	window.open(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
}

function onNewCell(cell){
	cell.title = '';
	if(cell.propertyName != "comandi")
		cell.align = 'center';
	
	if(cell.propertyName == "pdfPublicationId"){
		cell.innerHTML = "<span style='cursor:hand;' onclick='showPdfPublicationDetails(\""+cell.row.pdfPublicationId+"\");'>"+cell.innerHTML+"</span>";
	}
	
	if(cell.propertyName == "pdfAcroformVersion"){
		if(cell.row.pdfHasValidationWarningMessage == 'true'){
			cell.innerHTML = cell.innerHTML+"&nbsp;<span title='In pubblicazione sono stati segnalati punti di attenzione'>(!)</span>";
		}
	}
	
	if(cell.propertyName == "pdfHasPublicationNotes"){
		if(cell.row.pdfHasPublicationNotes == 'false'){
			cell.innerHTML = "&nbsp;";
		}else{
			cell.innerHTML = "<table width='100%' height='100%' cellspacing='0' cellpagging='0'><tr><td align='center'>"+
            					"<img src='/PdfWebForms/publisher/images/notes.gif' style='cursor:hand;vertical-align:top;' "+
            					 		"onclick='showPdfPublicationNotes(\""+cell.row.pdfPublicationId+"\");'>"+
            				 "</td></tr></table>";			
		}
	}

}

function showPdfPublicationDetails(pdfPublicationId){
	openModalPopup("prgm.pdfwebforms.publisher.display.PdfPublicationDetails","readRequest=false&pdfId="+document.datireadForm.pdfId.value+"&pdfPublicationId="+pdfPublicationId+"&BrowserInstance="+__getBrowserInstance(),null,null,"Dettagli pubblicazione - Id "+pdfPublicationId,400,520);
}

function showPdfPublicationNotes(pdfPublicationId){
	openPopup("prgm.pdfwebforms.publisher.display.PdfPublicationNotes","readRequest=false&pdfId="+document.datireadForm.pdfId.value+"&pdfPublicationId="+pdfPublicationId+"&BrowserInstance="+__getBrowserInstance(),null,null,"Note di pubblicazione - Id "+pdfPublicationId,300,700);
}

function selezionaPubblicazione(pdfPublicationId){
	if(document.datireadForm.pdfPublishTime.value == "" && document.datireadForm.isWorkingAreaHidden.value == "false"){ // Ho salvato qualcosa e l'area di lavoro è visibile
		if(!window.confirm("Le modifiche effettuate nell'area di pubblicazione verranno perse. Confermi ?"))
			return false;
	}
	startRequest();
	document.selezionaPubblicazioneForm.pdfAnag_pdfPublicationId.value = pdfPublicationId;
	document.selezionaPubblicazioneForm.submit();
}

function eliminaPubblicazione(pdfPublicationId, pdfMomVersion){
	if(document.datireadForm.pdfPublishTime.value == "" && document.datireadForm.isWorkingAreaHidden.value == "false"){ // Ho salvato qualcosa e l'area di lavoro è visibile
		if(!window.confirm("Confermi la definitiva eliminazione della pubblicazione ? (Sull'area di lavoro hai inoltre effettuato modifiche e verranno perse)"))
			return false;
	}else{
		if(!window.confirm("Confermi la definitiva eliminazione della pubblicazione ?"))
			return false;
	}
	startRequest();
	document.eliminaPubblicazioneForm.pdfAnag_pdfPublicationId.value = pdfPublicationId;
	document.eliminaPubblicazioneForm.pdfAnag_pdfMomVersion.value = pdfMomVersion;
	document.eliminaPubblicazioneForm.submit();
}

function archiviaPubblicazione(pdfPublicationId){
	if(document.datireadForm.pdfPublishTime.value == "" && document.datireadForm.isWorkingAreaHidden.value == "false"){ // Ho salvato qualcosa e l'area di lavoro è visibile
		if(!window.confirm("Confermi l'archiviazione della pubblicazione ? (Sull'area di lavoro hai inoltre effettuato modifiche e verranno perse)"))
			return false;
	}else{
		if(!window.confirm("Confermi l'archiviazione della pubblicazione ?"))
			return false;
	}
	startRequest();
	document.archiviaPubblicazioneForm.pdfAnag_pdfPublicationIdForArch.value = pdfPublicationId;
	document.archiviaPubblicazioneForm.submit();
}

function visualizzaFilePdfPubblicato(pdfPublicationId){
	var cmd = "prgm.pdfwebforms.publisher.business.VisualizzaFilePdfPubblicato.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&pdfId="+document.datireadForm.pdfId.value+"&pdfPublicationId="+pdfPublicationId;
	document.getElementById("utilIFrame").src = url;
}

function visualizzaAcroformPdfPubblicato(pdfPublicationId){
	var cmd = "prgm.pdfwebforms.publisher.business.VisualizzaAcroformPdfPubblicato.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&pdfId="+document.datireadForm.pdfId.value+"&pdfPublicationId="+pdfPublicationId;
	openNewWindow(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
}

function visualizzaFilePdf(){
	var cmd = "prgm.pdfwebforms.publisher.business.VisualizzaFilePdf.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&BrowserInstance="+__getBrowserInstance();
	document.getElementById("utilIFrame").src = url;
}

function visualizzaAcroformPdf(){
	var cmd = "prgm.pdfwebforms.publisher.business.VisualizzaAcroformPdf.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&BrowserInstance="+__getBrowserInstance();
	openNewWindow(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
}

function verificaPdfPubblicato(pdfPublicationId){
	askCodAgente(pdfPublicationId);
}

function verificaPdf(){
	askCodAgente();
}

function askCodAgente(pdfPublicationId){
	var cmd;
	var url;
    var d = new Date();
    var now = d.getTime();
	if(pdfPublicationId != undefined){
		cmd = "prgm.pdfwebforms.publisher.business.VerificaPdfPubblicato.executeProcessOnNewStack";
		url = "call.wfem?wfemCmd="+cmd+"&pdfId="+document.datireadForm.pdfId.value+"&pdfPublicationId="+pdfPublicationId+"&profiloUtente="+jsProfiloUtente+"&timenow="+now;
	}else{
		cmd = "prgm.pdfwebforms.publisher.business.VerificaPdf.executeProcessOnNewStack";
		url = "call.wfem?wfemCmd="+cmd+"&pdfId="+document.datireadForm.pdfId.value+"&profiloUtente="+jsProfiloUtente+"&timenow="+now;
	}
	$("#codAgenteVerificaPdf").attr("url",url).dialog({
														autoOpen: true, 
														modal: true,
														width: 350,
														height: 150,
														closeOnEscape: false,
														title: ""
													   });
}

function gotVerificaPdf(){
	var codAgente = $("#codAgenteVerificaPdfField").val();
	url = $("#codAgenteVerificaPdf").attr("url")+"&codiceAgente="+codAgente+"&pdfEnvironment=CATALOGO_MODULI";
	$("#codAgenteVerificaPdf").dialog('destroy');
	openNewWindow(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
}

function openPdfArchivedList(){
	openPopup("prgm.pdfwebforms.publisher.display.PdfArchivedList","BrowserInstance="+__getBrowserInstance(),null,doPdfArchivedListAction,"Archivio",300,700,true);
}
function doPdfArchivedListAction(action){
	if(action == null)
		return;
	startRequest();
	document.ripristinaPubblicazioneForm.pdfAnag_pdfPublicationIdForArch.value = action.pdfPublicationId;
	document.ripristinaPubblicazioneForm.submit();
}

function doSalvaImmaginePagina(){
	if(document.getElementById("pdfAnag_pageImageContent").value == ''){
		alert("Selezionare l'immagine");
		return false;
	}
	if(document.getElementById("pdfAnag_pageImageNum").value == ''){
		alert("Selezionare il numero di pagina (1, 2, 3,....)");
		return false;
	}
	var n1 = parseInt(document.getElementById("pdfAnag_pageImageNum").value,10);
	var n2 = parseInt(document.datireadForm.pdfNumPages.value,10);
	if(n1 <= 0 || n1 > n2){
		alert("Numero di pagina non corretto");
		return false;
	}
	startRequest();
	wfemHiddenSubmit(document.datiImmaginePagina,"body");
	return false;
}

function createPdfDataHelperPdfPubblicato(pdfPublicationId){
	var cmd = "prgm.pdfwebforms.publisher.business.CreatePdfDataHelperPdfPubblicato.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&pdfId="+document.datireadForm.pdfId.value+"&pdfPublicationId="+pdfPublicationId;
	window.open(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
}

function createPdfDataHelper(){
	var cmd = "prgm.pdfwebforms.publisher.business.CreatePdfDataHelper.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&BrowserInstance="+__getBrowserInstance();
	window.open(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
}

function showValidationWarningMessage(){
	$("#validationWarningReportMessage").dialog({
		autoOpen: true, 
		modal: true,
		height: 410,
		width: 550,
		closeOnEscape: false,
		title: "Punti di attenzione"
	   });
}

function showImageLoaderCont(){
	var cont = document.getElementById('imageLoaderCont');
	if(cont.style.visibility != 'visible')
		cont.style.visibility = 'visible';
	else
		cont.style.visibility = 'hidden';
}

function doPopolaCrafterAction(){
	if(!window.confirm("Confermi la rigenerazione dei dati Crafter ?"))
		return false;
	document.datiAnag.wfemCmd.value = "prgm.pdfwebforms.publisher.business.PopolaCrafter.execute";
	wfemHiddenSubmit(document.datiAnag,"body");
	return true;
}
