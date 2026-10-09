function selezionaIndirizzo(indirizzoHtml){
	if(isRequestPending())
		return;

	var ret = new Object();
	ret.indirizzo 		  = indirizzoHtml.descrizioneIndirizzo;
	ret.presso 			  = indirizzoHtml.presso;
	ret.comune 			  = indirizzoHtml.comune;
	ret.cap 			  = indirizzoHtml.cap;
	ret.provincia 		  = indirizzoHtml.provincia;
	ret.nazione 		  = indirizzoHtml.codNazione;
	ret.codComune 		  = indirizzoHtml.codComune;
	ret.codTipoIndirizzo  = indirizzoHtml.tipoIndirizzo;
	ret.toponimoIndirizzo = indirizzoHtml.toponimoIndirizzo;
	ret.numeroCivico 	  = indirizzoHtml.numeroCivico;
	
	if(parent.__isWlt){
		closeModalPopup(ret);	
	}else{
		returnValue = ret;
		window.close();
	}
}

function newCell(cell){
	if(cell.propertyName == "cap" || 
	   cell.propertyName == "provincia" || 
	   cell.propertyName == "codNazione")
		cell.align="center";
}

function newRow(row){
	if(row.tipoIndirizzo == '01')
		row.style.background = 'lavender';
}
