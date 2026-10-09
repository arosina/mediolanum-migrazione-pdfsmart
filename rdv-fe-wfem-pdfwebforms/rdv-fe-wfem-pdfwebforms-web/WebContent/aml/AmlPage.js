var submitDelay = 100;
var changeRunning = false;
var afterChangeFnc = null;

$( document ).ready(function() {
	$(".normalButton").attr({"tabindex":"0", "role":"button"});
	try{
		$("[onclick]").bind({
			keypress: function(event){
				if(event.which == 13 || event.which == 32){
					this.click();
				}
			}			
		});
	}catch(e){}
});

function submitAmlData(cmd, form, afterChangeFuncion){
	setTimeout(function() {
		if(changeRunning && afterChangeFuncion){
			afterChangeFnc = afterChangeFuncion;
			return;
		}
		form.wfemCmd.value = cmd+".execute";
		form.submit();
	}, submitDelay);		
}

function callHiddenSubmit(form, targets){
	changeRunning = true;
	wfemHiddenSubmit(form, targets);
}

function onWfemHiddenSubmitEnd(obj){
	changeRunning = false;
	endWait();
	if(afterChangeFnc != null){
		afterChangeFnc();
		afterChangeFnc = null;
	}
}

function doBack(){
	var input = new Object();
	input.msg = "Attenzione, tutte le informazioni eventualmente inserite andranno perse. Confermi?";
	input.onlyCancel = false;
	openModalPopup("prgm.pdfwebforms.display.PdfAlert",
			  		"BrowserInstance="+__getBrowserInstance(),
			  		input,doBackEnd,"Avviso",250,400);
	
}

function doBackEnd(obj){
	if(obj==null)
		return;
	wait();
	document.goBackForm.submit();
	return false;	
}

function selezionaTab(nomeTab, isSelezionato){
	if(isSelezionato)
		return;
	__fieldIntf.closeHelper("Err");
	wait();
	document.dati.wfemCmd.value="prgm.pdfwebforms.aml.RefreshPage.execute";
	document.dati.amlModel_tabSelezionato.value = nomeTab;
	callHiddenSubmit(document.dati,"tabContent");
	return false;
}

function doProsegui(){
	wait();
	submitAmlData("prgm.pdfwebforms.aml.ConfirmAml",document.dati,doProsegui);
	return false;
}

function removeError(field){
	hideHelperAnchor(field.id);
	$(field).removeClass('fieldHasError');
	$(field).addClass('inputField');
}

$( document ).ready(function() {
	try{
		$(".normalButton").bind({
			click: function(event){
				$(this).addClass("normalButtonSel");
			}
		}).mouseout(
			function(){ $(this).removeClass("normalButtonSel"); }
		).mouseenter(
			function(){ $(this).addClass("normalButtonSel"); }
		);
	}catch(e){}
});
