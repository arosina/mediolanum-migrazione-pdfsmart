var isFieldChanged = false;

function fieldChanged(){
	isFieldChanged = true;
}

function doEnterAction(){
    var but = document.getElementById("ricercaAction");
    but.focus();

    var doRic = false;
    if(isFieldChanged){
		doRic = true;	    	
    }else{
	    try{
			if(document.elencoAgentiGrid.tableModel.rows.length == 1)
				selAgente(document.elencoAgentiGrid.tableModel.rows[0]);
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

function doRicercaAction(){
	isFieldChanged = false;
	wfemHiddenSubmit(document.dati,'elencoCont,legendaCont');
	return true;
}

function selAgente(age){
	
	var ret = new Object();		
	ret.codRete = age.codRete;
	ret.codAgente = age.codAgente;
	ret.nominativo = age.nominativo;
	ret.codMediolanum = age.codMediolanum;

	if(parent.__isWlt){
		closeModalPopup(ret);	
	}else{
		returnValue = ret;
		window.close();
	}
}

function onNewCellAgenti(cell){
	if(cell.propertyName == 'codAgente')
		cell.align = 'center';
}
