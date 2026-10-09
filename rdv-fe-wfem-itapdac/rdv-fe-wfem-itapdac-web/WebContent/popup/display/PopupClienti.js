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
			if(document.elencoContrattiClienteGrid.tableModel.rows.length == 1 &&
			   document.elencoClientiGrid.tableModel.rows.length == 1)
				selContratto(document.elencoContrattiClienteGrid.tableModel.rows[0]);
			else
				doRic = true;	    	
	    }catch(e){
	    	try{
				if(document.elencoClientiGrid.tableModel.rows.length == 1)
					selCliente(document.elencoClientiGrid.tableModel.rows[0]);
				else
					doRic = true;	    	
	    	}catch(e){
				doRic = true;	    	
	    	}
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
	document.dati.codAgente.value = '';
	document.dati.tipoRicerca.value = '1';
	isFieldChanged = false;
	wfemHiddenSubmit(document.dati,'elencoCont,legendaCont,elencoContrattiCont');
	return true;
}

function doRicercaNonMieiAction(){
	document.dati.tipoRicerca.value = '2';
	isFieldChanged = false;
	wfemHiddenSubmit(document.dati,'elencoCont,legendaCont,elencoContrattiCont');
	return true;
}

function doRicercaTuttiAction(){
	document.dati.tipoRicerca.value = '3';
	isFieldChanged = false;
	wfemHiddenSubmit(document.dati,'elencoCont,legendaCont,elencoContrattiCont');
	return true;
}

function onNewCellClienti(cell){
	if(cell.propertyName != 'cognome' &&
	   cell.propertyName != 'nome')
	   cell.align='center';
	   
	if(cell.propertyName == 'visContr'){
		cell.onmouseover = function(){event.cancelBubble = true;};
		cell.onmouseout = function(){event.cancelBubble = true;};
		cell.style.padding = '0px';
		if(cell.row.codMediolanum == ''){
			cell.title = 'Cliente potenziale'
			cell.innerHTML = '&nbsp;';
		}else{
			cell.style.cursor = 'default';
			cell.onclick = function(event){ onClickCellaContratto(event, cell); };
			cell.innerHTML =
			"<table width='100%' height='100%' cellpadding='0px' cellspacing='0px' style='cursor:pointer;background-color:white;'>" +
			"  <tr>"+
			"   <td align='center' title='Visualizza i contratti di "+cell.row.cognome+" "+cell.row.nome+"'>"+
			"      <img src='"+__retrieveResourceUrl()+"/images/contratti.png'>"+
			"   </td>" +
			"  </tr>" +
			"</table>";
		}
	}
}

function onClickCellaContratto(event, cell) {
	if(!isIE()) 
		event.stopPropagation();
	else
		window.event.cancelBubble = true;
		
	loadContrattiCliente(cell.row);
}

function loadContrattiCliente(cli){
	startRequest();
	document.contrattiClienteForm.clienteSelezionato_codMediolanum.value = cli.codMediolanum;
	wfemHiddenSubmit(document.contrattiClienteForm,'elencoContrattiCont,legendaContrCont');
}

function selCliente(cli){

	var ret = new Object();		
	ret.type = 'cliente';
	ret.tipoRicerca = document.dati.tipoRicerca.value;
	ret.codMediolanum = cli.codMediolanum;
	ret.cognome = cli.cognome;
	ret.nome = cli.nome;
	if(cli.agente_codRete)
		ret.agente_codRete = cli.agente_codRete;
	if(cli.agente_codAgente)
		ret.agente_codAgente = cli.agente_codAgente;
	if(cli.agente_nominativo)
		ret.agente_nominativo = cli.agente_nominativo;
	if(cli.agente_codMediolanum)
		ret.agente_codMediolanum = cli.agente_codMediolanum;
	
	if(parent.__isWlt){
		closeModalPopup(ret);	
	}else{
		returnValue = ret;
		window.close();
	}
}
