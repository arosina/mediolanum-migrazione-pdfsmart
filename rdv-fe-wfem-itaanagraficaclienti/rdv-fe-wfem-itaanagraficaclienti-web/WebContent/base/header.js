function doEnterAction(){
	if($("#alertMessage").dialog("isOpen"))
		$('#alertMessage').dialog('destroy');
}

function doGobackAction(){
	if(document.datiread.refreshable.value == 'true')
		document.goback.wfemCmd.value = 'executeLastDisplay';
	else
		document.goback.wfemCmd.value = 'showLastDisplay';
	document.goback.submit();
	return true;
}
function doGoApplAction(){
	if(datiread.callingAppl.value == 'TOOLPREVIDENZA' || 
			datiread.callingAppl.value == 'TOOLPROTEZIONE' || 
			datiread.callingAppl.value == 'TOOLDOSSIERTITOLI' ||
			datiread.callingAppl.value == 'TOOL5D' ||
			datiread.callingAppl.value == 'ANYCALLER'){
		document.clienteKeyForm.wfemCmd.value = 'executeLastDisplay';
		document.clienteKeyForm.submit();
	}else{
		document.getElementById("closeThreadIF").src = "call.wfem?wfemCmd=closeThread&BrowserInstance="+document.dati.BrowserInstance.value;
		document.goback.BrowserInstance.value = document.datiread.parentBrowserInstance.value;
		document.goback.wfemCmd.value = 'executeCurrentDisplay';
		document.goback.submit();
	}
	return true;
}

function submitForm(id){

	if(document.getElementById("messagesAndErrors"))
		document.getElementById("messagesAndErrors").style.visibility = "hidden";

	if(document.jsModificabile){
		var objs = document.getElementsByTagName("select");
		for(var i=0;i<objs.length;i++){
			objs[i].disabled = false;
		}
	}
	if(id)
		wfemHiddenSubmit(document.dati,id);
	else
		wfemHiddenSubmit(document.dati,'body');

}

function showAlerMessage(msg){
	$("#alertMessageText").html(msg);
	$("#alertMessage").dialog({
		autoOpen: true, 
		modal: true,
		width: 350,
		height: 200,
		closeOnEscape: false,
		title: "Avviso"
    });
}

function resetFieldLayout(fname){
	try{
		__fieldIntf.curOpenedHelper = null;
		$("#divErrHelper").hide();
		$("#divWarHelper").hide();
		enableField(fname,true);
		hideHelperAnchor(fname);
		removeSkippable(fname);
		$("#"+fname).removeClass('fieldHasError').addClass('inputField');
	}catch(e){}
}

function changeIsInCogestione(check){
	if(document.datiread.isDitta.value === 'true')
		resetFieldLayout("partitaIva");
	else
		resetFieldLayout("codFiscale");
	document.dati.isClienteInCogestione.value = check.checked ? "true" : "false"; 
	var labelObj = document.getElementById("isClienteInCogestioneLabel");
	var labelValue = check.checked ? "Cliente in cogestione" : "Cliente personale"; 
	labelObj.innerHTML = labelValue;
}