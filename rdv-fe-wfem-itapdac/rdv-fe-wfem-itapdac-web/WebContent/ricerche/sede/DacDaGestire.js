document.onkeypress = function(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    document.all("ricercaAction").focus();
	    document.all("ricercaAction").click();
	}
}

function doRicercaAction(){
	if(document.ricerca.parametri_idDac.value == ''){
		document.getElementById("parametri_idDac").focus();
		document.getElementById("parametri_idDac").select();
		return;
	}
	document.ricerca.doSearch.value = 'true';
	document.ricerca.submit();
	return true;
}

function onNewCell(cell){
	if(cell.propertyName == 'dataOraEmissione')
	   cell.align = 'center';
}

var curDac=null;
function selectDac(dac){
	enableAction("apriAction",true);
	curDac = dac;
}

function doApriAction(){
	document.dati.idDac.value = curDac.idDac;
	document.dati.stato.value = curDac.stato;
	document.dati.submit();
	return true;
}