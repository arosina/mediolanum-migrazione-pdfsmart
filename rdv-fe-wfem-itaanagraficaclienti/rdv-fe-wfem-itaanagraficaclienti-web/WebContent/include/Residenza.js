function manageDomicilioDiversoDaResidenza(event,check){
	var domicilioDiversoDaResidenzaOriginalValue = document.getElementById("domicilioDiversoDaResidenzaOriginalValue").value;
	if(check.value === domicilioDiversoDaResidenzaOriginalValue)
		return;
	if(check.value === "S"){
		document.getElementById("indirizzoDomicilioTitleCont").style.display = '';
		document.getElementById("indirizzoDomicilioCont").style.display = '';
		document.getElementById("domicilioDiversoDaResidenzaOriginalValue").value = check.value;
	}else if(check.value === "N"){
		if(domicilioDiversoDaResidenzaOriginalValue === "S"){
			if(!window.confirm("Confermi la cancellazione dell'indirizzo di domicilio?")){
				preventDefault(event);
				return;
			}
		}
		clearIndirizzoErrors("domicilio_indirizzo");
		document.dati.wfemCmd.value = 'prgm.ita.anagraficaclienti.business.ClearIndirizzoDomicilio.execute';
		startRequest();
		submitForm("tabContainer");
	}
}
