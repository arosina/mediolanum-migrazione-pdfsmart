function selVar(variazione){
	if(variazione.tipoCarta == 'D'){
		document.getElementById("printObject").innerHTML = "<table class='text' width='100%'><tr><td align='center' style='padding-top:50px;'><b>Variazione in firma digitale. La stampa non e' disponibile</b></td></tr></table>";
		return false;
	}
	document.stampa.progressivo.value = variazione.progressivo;
	includePdfObject("printObject",document.stampa);
}

function doActionOnEndWait(){
	document.stampa.submit();
}
