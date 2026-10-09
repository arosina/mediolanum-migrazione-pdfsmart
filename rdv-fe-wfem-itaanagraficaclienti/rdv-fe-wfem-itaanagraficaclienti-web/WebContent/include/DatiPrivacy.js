function gestioneFlagLiberatoria(){
	if(document.dati.datiPrivacy_flagLiberatoria.value === '')
		clearDateField("datiPrivacy_dataLiberatoria");
	else
		setHtmlDateField("datiPrivacy_dataLiberatoria",oggi);
}

function gestioneFlagCarte(){
	if(document.dati.datiPrivacy_flagCarte.value === '')
		clearDateField("datiPrivacy_dataCarte");
	else
		setHtmlDateField("datiPrivacy_dataCarte",oggi);
}

function gestioneFlagExtraUE(){
	if(document.dati.datiPrivacy_flagExtraUE.value === '')
		clearDateField("datiPrivacy_dataExtraUE");
	else
		setHtmlDateField("datiPrivacy_dataExtraUE",oggi);
}

function gestioneFlagProfilazione(){
	if(document.dati.datiPrivacy_flagProfilazione.value === '')
		clearDateField("datiPrivacy_dataProfilazione");
	else
		setHtmlDateField("datiPrivacy_dataProfilazione",oggi);
}

