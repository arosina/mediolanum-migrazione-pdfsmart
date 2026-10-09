function doGoLastPdf(){
	wait();
	document.goLastPdfForm.submit();
	return false;
}

function doShowdata(){
	var url = "call.wfem?wfemCmd=prgm.pdfwebforms.display.PdfResultDataOnNewWindow.execute&BrowserInstance="+document.goLastPdfForm.BrowserInstance.value;
	openNewWindow(url,"","status=no,toolbar=no,resizable=yes,scrollbars=yes");
	return false;
}

function openStampa(){
	if(isPdfIsInCartaChimica){
		$("#pdfObj").css("visibility","hidden");
		var input = new Object();
		input.msg = "E' stato inserito il numero carta chimica.<br>La stampa non e' permessa.";
		input.onlyCancel = true;
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		input,alertCallback,"Avviso",250,400);
		return false;
	}
	openPdfObject("call.wfem?wfemCmd=prgm.pdfwebforms.stream.PdfPrintPreview.execute&BrowserInstance="+document.goLastPdfForm.BrowserInstance.value,"Modulo");
	return false;
}

function openAmlPopup(callback, obj){
	var dial = document.getElementById("amlMessageDialog");
	if(dial == null)
		return false;
	
	$("#pdfObj").css("visibility","hidden");
	dial.callback = callback;
	dial.obj = obj;
	$("#amlMessageDialog").dialog({
		open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
		autoOpen: true, 
		modal: true,
		width: 750,
		height: 400,
		closeOnEscape: false,
		title: "Avviso",
		beforeclose: function(event, ui) {
			setTimeout(function() {
				closeAmlPopup();
			}, 5);				
		}
	   });
	return true;
}

function closeAmlPopup(){
	var dial = document.getElementById("amlMessageDialog");
	var callback = dial.callback;
	var obj = dial.obj;
	$("#amlMessageDialog").dialog('destroy');
	if(obj == null)
		callback(true);
	else
		callback(obj, true);
}

function doInviaInSede(fromAmlPopup){
	$("#pdfObj").css("visibility","hidden");
	openModalPopup("prgm.pdfwebforms.display.PdfSendProcessAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		null,doInviaInSedeCallback,"Avviso",300,450,true);
	return false;
}
function doInviaInSedeCallback(obj, fromAmlPopup){
	$("#pdfObj").css("visibility","visible");
	if(obj == null)
		return;
	if(fromAmlPopup || !openAmlPopup(doInviaInSedeCallback, obj)){
		wait();
		document.goOnForm.codiciOperazionePritPerMultioperazione.value = obj.codiciOperazionePritPerMultioperazione;
		if(jsIsInBasket)
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.sendprocess.business.StartSendBasketProcess.execute";
		else
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.sendprocess.business.StartSendProcess.execute";
		document.goOnForm.submit();
	}
	return;
}

function doCopernico(fromAmlPopup){
	if(isPdfIsInCartaChimica){
		$("#pdfObj").css("visibility","hidden");
		var input = new Object();
		input.msg = "E' stato compilato il numero di carta chimica.<br>Non e' possibile procedere in modalità copernico.";
		input.onlyCancel = true;
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		input,alertCallback,"Avviso",250,400);
		return;
	}

	$("#pdfObj").css("visibility","hidden");
	document.goOnForm.isFirmaADistanza.value = "";	
	if(jsHasLayerCollocamentoADistanza){
		var copModalityInput = new Object();
		copModalityInput.mod = "C";
		copModalityInput.tipoDistanzaCollocamento = jsTipoDistanzaCollocamento;
		openModalPopup("prgm.pdfwebforms.signprocess.display.PdfSignModalityAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		copModalityInput,doCopernicoCallback,"Attenzione",300,480);
	}else{
		if(fromAmlPopup || !openAmlPopup(doCopernico, null)){
			wait();
			if(jsIsInBasket)
				document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.business.StartCopernicoBasketProcess.execute";
			else
				document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.business.StartCopernicoProcess.execute";
			document.goOnForm.submit();
		}
	}
	return false;
}
function doCopernicoCallback(obj, fromAmlPopup){
	$("#pdfObj").css("visibility","visible");
	if(obj == null)
		return;
	
	if(fromAmlPopup || !openAmlPopup(doCopernicoCallback, obj)){
		wait();
		if(jsIsInBasket)
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.business.StartCopernicoBasketProcess.execute";
		else
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.business.StartCopernicoProcess.execute";
		if(obj.isFirmaADistanza)
			document.goOnForm.isFirmaADistanza.value = obj.isFirmaADistanza;
		document.goOnForm.submit();
	}
	return;
}

function doFirma(fromAmlPopup){
	if(isPdfIsInCartaChimica){
		$("#pdfObj").css("visibility","hidden");
		var input = new Object();
		input.msg = "E' stato compilato il numero di carta chimica.<br>Non e' possibile procedere in firma digitale.";
		input.onlyCancel = true;
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		input,alertCallback,"Avviso",250,400);
		return;
	}
	
	var showAlertCatalogo = false;
	var signModalityInput = new Object();
	var h = 300;
	if(jsPdfEnvironment == jsENVIRONMENT_CATALOGO_MODULI || jsPdfEnvironment == jsENVIRONMENT_CATALOGO_OPERAZIONI){
		var msg = "La firma digitale del modulo consente di azzerare i tempi di spedizione ed evitano la compilazione della riga di Prit.<br>";
		h = 350;
		if(!jsHasProcessoControlliCompleti){
			h = 420;
			msg += "Non sono comunque previsti i controlli di completezza e correttezza dei dati inseriti.<br>"+ 
				   "Per evitare sospesi o respinti presta la massima attenzione alla compilazione.<br>";
		}
		if(!jsHasLayerCollocamentoADistanza)
			msg += "Confermi di voler procedere con la firma?";
		signModalityInput.msg = msg+"<br>";
		showAlertCatalogo = true;
	}	
	
	$("#pdfObj").css("visibility","hidden");
	document.goOnForm.isFirmaADistanza.value = "";	
	if(jsHasLayerCollocamentoADistanza){
		signModalityInput.mod = "F";
		signModalityInput.tipoDistanzaCollocamento = jsTipoDistanzaCollocamento;
		openModalPopup("prgm.pdfwebforms.signprocess.display.PdfSignModalityAlert",
		  		"BrowserInstance="+__getBrowserInstance(),
		  		signModalityInput,doFirmaCallback,"Attenzione",h,480);
	}else if(showAlertCatalogo){
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
		  		"BrowserInstance="+__getBrowserInstance(),
		  		signModalityInput,doFirmaCallback,"Avviso",h,480);
	}else{
		if(fromAmlPopup || !openAmlPopup(doFirma, null)){
			wait();
			if(jsIsInBasket)
				document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.StartSignBasketProcess.execute";
			else
				document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.StartSignProcess.execute";
			document.goOnForm.submit();
		}
	}
	return false;
}
function doFirmaCallback(obj, fromAmlPopup){
	$("#pdfObj").css("visibility","visible");
	if(obj == null)
		return;
	
	if(fromAmlPopup || !openAmlPopup(doFirmaCallback, obj)){
		wait();
		if(jsIsInBasket)
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.StartSignBasketProcess.execute";
		else
			document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.StartSignProcess.execute";
		if(obj.isFirmaADistanza)
			document.goOnForm.isFirmaADistanza.value = obj.isFirmaADistanza;
		document.goOnForm.submit();
	}
	return;
}

function doCartaDigitale(){
	if(isPdfIsInCartaChimica){
		$("#pdfObj").css("visibility","hidden");
		var input = new Object();
		input.msg = "E' stato compilato il numero di carta chimica.<br>Non e' possibile procedere in modalità digitale.";
		input.onlyCancel = true;
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		input,alertCallback,"Avviso",250,400);
		return;
	}
	
	$("#pdfObj").css("visibility","hidden");
	
	var input = new Object();
	input.msg = "Il modulo verra' inviato in sede e non sara' piu' modificabile.<br>Confermi di voler procedere?";
	input.onlyCancel = false;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,doCartaDigitaleCallback,"Avviso",250,400);
	return false;
}
function doCartaDigitaleCallback(obj){
	$("#pdfObj").css("visibility","visible");
	if(obj == null)
		return;
	wait();
	document.goOnForm.wfemCmd.value = "prgm.pdfwebforms.sendprocess.business.StartSendDigitalProcess.execute";
	document.goOnForm.submit();
	return;
}

//**************************************************
// Alert tasti FD e COpernico
function alertNoCopernicoCausaContratti(){
	$("#pdfObj").css("visibility","hidden");
	var input = new Object();
	input.msg = "<b>ATTENZIONE</b>: Sulla base delle informazioni valorizzate all'interno di almeno un modulo della proposta, non è possibile procedere con <b>Copernico</b>.";
	input.onlyCancel = true;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,alertCallback,"Avviso",350,500);
	return;
}
function alertNoFirmaDigitaleCausaContratti(){
	$("#pdfObj").css("visibility","hidden");
	var input = new Object();
	input.msg = "<b>ATTENZIONE</b>: Sulla base delle informazioni valorizzate all'interno di almeno un modulo della proposta, non è possibile procedere con la <b>Firma Digitale</b>.";
	input.onlyCancel = true;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,alertCallback,"Avviso",350,500);
	return;
}
var msgNoPerFirmaOlografaInCaller = 
	"<b>ATTENZIONE</b>: Non è possibile procedere con <b>Firma Digitale/Copernico</b> in quanto hai avviato il processo di Stampa e Firma; se desideri cambiare la modalità di firma, "+
	"chiudi questa pagina e riprendi la compilazione dalla sezione Operazioni selezionando la <b>Firma Digitale</b> o <b>Copernico</b>, altrimenti clicca su <b>OK</b> e procedi con l'<b>invio in Sede</b>."
var msgNoPerCopernicoInCaller = 
	"<b>ATTENZIONE</b>: Non è possibile procedere con <b>Firma Digitale</b> in quanto hai avviato il processo di <b>Copernico</b>; se desideri cambiare la modalità di firma, "+
	"chiudi questa pagina e riprendi la compilazione dalla sezione Operazioni selezionando la <b>Firma Digitale</b>, altrimenti clicca su <b>OK</b> e procedi con <b>Copernico</b> o con l'<b>invio in Sede</b>.";
function alertNoCausaCaller(callerSelectedCompilationMode){
	$("#pdfObj").css("visibility","hidden");
	var input = new Object();
	input.msg = callerSelectedCompilationMode == "CARTA_LIBERA" ? msgNoPerFirmaOlografaInCaller : msgNoPerCopernicoInCaller;
	input.onlyCancel = true;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,alertCallback,"Avviso",350,500);
	return;
}

// **************************************************

function alertCallback(){
	$("#pdfObj").css("visibility","visible");
}
