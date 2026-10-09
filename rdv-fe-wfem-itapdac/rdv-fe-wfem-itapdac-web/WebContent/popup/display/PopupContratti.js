var isFieldChanged = false;

function fieldChanged(){
	isFieldChanged = true;
}

function doEnterAction(){
    var but = document.getElementById("ricercaMieiAction");
    if(but == null)
    	but = document.getElementById("ricercaNonMieiAction");
    if(but == null)
    	but = document.getElementById("ricercaTuttiAction");
    but.focus();
    
    var doRic = false;
    if(isFieldChanged){
		doRic = true;	    	
    }else{
	    try{
			if(document.elencoContrattiGrid.tableModel.rows.length == 1)
				selContratto(document.elencoContrattiGrid.tableModel.rows[0]);
			else
				doRic = true;	    	
	    }catch(e){
			doRic = true;	    	
	    }
    }
    
    if(doRic){
	    try{
		    but.focus();
		    but.click();
	    }catch(e){}
    }
}

function doRicercaMieiAction(){
	if(document.dati.numeroContratto.value == ''){
		alert('Specificare il numero contratto');
		document.dati.numeroContratto.focus();
		return;
	}
	document.dati.tipoRicerca.value = '1';
	isFieldChanged = false;
	wfemHiddenSubmit(document.dati,'elencoCont,legendaCont');
	return true;
}

function doRicercaNonMieiAction(){
	if(document.dati.numeroContratto.value == ''){
		alert('Specificare il numero contratto');
		document.dati.numeroContratto.focus();
		return;
	}
	document.dati.tipoRicerca.value = '2';
	isFieldChanged = false;
	wfemHiddenSubmit(document.dati,'elencoCont');
	return true;
}

function doRicercaTuttiAction(){
	if(document.dati.numeroContratto.value == ''){
		alert('Specificare il numero contratto');
		document.dati.numeroContratto.focus();
		return;
	}
	document.dati.tipoRicerca.value = '3';
	isFieldChanged = false;
	wfemHiddenSubmit(document.dati,'elencoCont');
	return true;
}
