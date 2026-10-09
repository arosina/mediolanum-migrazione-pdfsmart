function doGoOn(){
	if(requestPending)
		return false;
	wait();
	document.goForm.wfemCmd.value = "prgm.pdfwebforms.business.ConfirmPdfAttachments.execute";
	document.goForm.submit();
	return false;
}

function doGoPrevPdf(){
	if(requestPending)
		return false;
	var showAlert = false;
	for(var i=0;i<numAllegati;i++){
		if(document.getElementById("fileName"+i) != null){
			showAlert = true;
			break;
		}
	}
	if(showAlert){
		var input = new Object();
		input.msg = "Gli allegati verranno persi. Confermi?";
		input.onlyCancel = false;
		openModalPopup("prgm.pdfwebforms.display.PdfAlert",
				  		"BrowserInstance="+__getBrowserInstance(),
				  		input,doGoPrevPdfOk,"Avviso",250,400);
	}else{
		doGoPrevPdfOk(new Object());
	}
	return false;
}

function doGoPrevPdfOk(ret){
	if(ret == null)
		return;
	wait();
	document.goForm.wfemCmd.value = "prgm.pdfwebforms.display.PdfPage.execute";
	document.goForm.submit();
}

function onSelectFile(attachIdx){
	if(requestPending)
		return false;
	$("#pdfAttachments"+attachIdx+"_file").click();
}

function onLoadAttch(attachIdx){
	if($("#pdfAttachments"+attachIdx+"_file").val() != "") {
		$("#removeAttachButtonDiv"+attachIdx).css("display","none");
		if($("#pdfAttachments"+attachIdx+"_file").val().lastIndexOf(".pdf") >= 0)
			$("#fileNameCont"+attachIdx).html("Verifica file PDF in corso...");
		else
			$("#fileNameCont"+attachIdx).html("Conversione in formato PDF in corso...");
		document.convertAttachForm.attachIdx.value = attachIdx;
		wfemHiddenSubmit(document.convertAttachForm,"attach"+attachIdx);
	}else{
		requestPending = false;
	}
}

function openAttach(attachIdx){
	window.open("call.wfem?wfemCmd=prgm.pdfwebforms.business.OpenAttach.execute&BrowserInstance="+__getBrowserInstance()+"&attachIdx="+attachIdx);
}

function removeAttch(attachIdx){
	document.removeAttachForm.attachIdx.value = attachIdx;
	wfemHiddenSubmit(document.removeAttachForm,"attach"+attachIdx);
}