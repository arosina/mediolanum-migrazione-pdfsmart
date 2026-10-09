function manageCaratteriDescrizioneAltro(event) {
	var myRegEx = /[^a-zA-Z0-9',.; \u00E0\u00E8\u00E9\u00EC\u00F2\u00F9]/;
	if (myRegEx.test(String.fromCharCode(event.keyCode))) {
		event.preventDefault();
		return;
	}
}


function clearField(fieldName) {
	try {
		document.getElementById(fieldName).value = "";
		$("div[name='" + fieldName + "ContBorder']").removeClass(
				"pdfFieldHasError");
	} catch (e) {
	}
}

function clearErrors(fieldsName) {
	try {
		
		for (var i = 0; i < fieldsName.length; i++) {
		
			pdf.clearFieldErrors(fieldsName[i]);
		}

	} catch (e) {
	}
}

function getBaseFieldNamesBeneficiarioPF() {
	var retval = [ "codiceCliente", "nome", "cognome", "codiceFiscale",
			"sesso", "dataNascita", "comuneNascita", "provinciaComuneNascita",
			"nazioneComuneNascita", "codToponimoIndirizzo", "toponimoIndirizzo", "indirizzo",
			"numeroCivicoIndirizzo", "capComune", "comune", "provinciaComune",
			"nazioneComune", "email", "prefissoInternazionaleTelefono" , "prefissoTelefono", "telefono", "tipoTelefono", 
			"isPep", "indiceMotivazionePep"];
	return retval;
}

function getBaseFieldNamesBeneficiarioPG() {
	var retval = [ "codiceCliente", "codiceFiscalePartitaIva", "ragioneSociale",
			"numeroIscrizioneCCIAA", "dataIscrizioneCCIAA",
			"provinciaIscrizioneCCIAA", "codToponimoIndirizzo", "toponimoIndirizzo", "indirizzo",
			"numeroCivicoIndirizzo", "capComune", "comune", "provinciaComune",
			"nazioneComune", "email", "prefissoInternazionaleTelefono" , "prefissoTelefono", "telefono", "tipoTelefono" ];
	return retval;
}

function getBaseFieldNamesTitolare() {
	var retval = [ "codiceCliente", "nome", "cognome", "codiceFiscale",
			"sesso", "dataNascita", "comuneNascita", "provinciaComuneNascita",
			"nazioneComuneNascita", "isGiaCliente", "codToponimoIndirizzo", "toponimoIndirizzo", "indirizzo",
			"numeroCivicoIndirizzo", "capComune", "comune", "provinciaComune",
			"nazioneComune", "email", "prefissoInternazionaleTelefono" , "prefissoTelefono", "telefono", "tipoTelefono", 
			"isPep", "motivazionePep", "cognomePep", "nomePep"];
	return retval;
}

function getFieldNamesBeneficiarioPF(baseFieldNamesBeneficiario, suffisso) {
	var retval = baseFieldNamesBeneficiario;
	var indiceMotivazionePep = null;
	for (var i = 0; i < baseFieldNamesBeneficiario.length; i++) {
		retval[i] = retval[i] + suffisso;
		//Per la gestione del flag pep le motivazioni pep da refreshare non sono in base a indice beneficiario
		//ma in base a indiceMotivazionePep
		if (retval[i].indexOf("indiceMotivazionePep") >= 0 && document.getElementById(retval[i]) !== null){
			indiceMotivazionePep = document.getElementById(retval[i]).value;
		}		
	}
	if (indiceMotivazionePep !== null && indiceMotivazionePep !== ""){
		var prefissoBeneficiario = suffisso.indexOf("BeneficiarioVita") >= 0?"BeneficiarioVita":"Beneficiario";
		retval.push("motivazionePep" + prefissoBeneficiario + indiceMotivazionePep);
		retval.push("cognomePep" + prefissoBeneficiario + indiceMotivazionePep);
		retval.push("nomePep" + prefissoBeneficiario + indiceMotivazionePep);
		retval.push("dataNascitaPep" + prefissoBeneficiario + indiceMotivazionePep);
		retval.push("comuneNascitaPep" + prefissoBeneficiario + indiceMotivazionePep);
	}
	
	if (document.getElementById("cognomeLegaleRapprProcuratore"+suffisso) !== null){
		//Per eventuale presenza procuratore
		retval.push("cognomeDichiarazione"+suffisso);
		retval.push("nomeDichiarazione"+suffisso);
		retval.push("codiceFiscaleDichiarazione"+suffisso);
		
		retval.push("cognomeLegaleRapprProcuratore"+suffisso);
		retval.push("nomeLegaleRapprProcuratore"+suffisso);
		retval.push("codiceFiscaleLegaleRapprProcuratore"+suffisso);
	}
	return retval;
}

function getFieldNamesBeneficiarioPG(baseFieldNamesBeneficiario, baseFieldNamesTitolare, suffissoBeneficiario, numeroTitolari) {
	var retval = baseFieldNamesBeneficiario;
	for (var i = 0; i < baseFieldNamesBeneficiario.length; i++) {
		retval[i] = retval[i] + suffissoBeneficiario;
	}
	for (var k = 1; k <= numeroTitolari; k++) {
		var suffissoTitolare = "Titolare" + k + suffissoBeneficiario;
		for (var j = 0; j < baseFieldNamesTitolare.length; j++) {
			retval[retval.length] = baseFieldNamesTitolare[j] + suffissoTitolare;
		}
	}
	
	if (document.getElementById("cognomeLegaleRapprProcuratore"+suffissoBeneficiario) !== null){
		//Per eventuale presenza procuratore
		retval.push("ragioneSocialeDichiarazione"+suffissoBeneficiario);
		retval.push("codiceFiscalePartitaIvaDichiarazione"+suffissoBeneficiario);
		
		retval.push("cognomeLegaleRapprProcuratore"+suffissoBeneficiario);
		retval.push("nomeLegaleRapprProcuratore"+suffissoBeneficiario);
		retval.push("codiceFiscaleLegaleRapprProcuratore"+suffissoBeneficiario);
	}
	
	return retval;
}
function isHiddenField(fieldName){
	return ($("div[name='"+fieldName+"Cont']").css("visibility") === "hidden");
}
