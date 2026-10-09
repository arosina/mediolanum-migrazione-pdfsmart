function wait(){
	try{$(".waitDiv").show();jsWait.start();}catch(e){}
}

function endWait(){
	try{$(".waitDiv").hide();jsWait.stop();}catch(e){}
}

function onWfemHiddenSubmitEnd(){
	endWait();
}

function doEnterAction(){
	if(document.datiTab.selectedTab.value == "pdfListCont")
		pdfList.doFiltra();
	else if(document.datiTab.selectedTab.value == "pdfCompletedListCont")
		pdfCompletedList.doRicercaAction();
	return;	
}

