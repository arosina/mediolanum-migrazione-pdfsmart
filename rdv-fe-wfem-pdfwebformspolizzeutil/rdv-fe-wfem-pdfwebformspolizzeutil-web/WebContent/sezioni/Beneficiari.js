function Beneficiari() {
	// This is intentional
}

Beneficiari.prototype.manageOnLoadCasoVita = function(assicurando, posizioneAssicurando) {
	manageOnLoad(assicurando, posizioneAssicurando, "BeneficiarioVita");
}

Beneficiari.prototype.manageOnLoadCasoDecesso = function(assicurando, posizioneAssicurando) {
	manageOnLoad(assicurando, posizioneAssicurando, "Beneficiario");
}

Beneficiari.prototype.manageOnLoad = function(assicurando, posizioneAssicurando, prefissoBeneficiario) {
	manageOnLoad(assicurando,posizioneAssicurando, prefissoBeneficiario);
}

Beneficiari.prototype.manageRelazione = function(fieldName) {
	//vengono gestite le possibili tipologie di relazione : ContraenteBeneficiario, ContraenteTitolare, AssicurandoBeneficiario, AssicurandoTitolare
	var suffisso = "";
	var prefissoBeneficiario = "";
	var indiceBeneficiario = "";
	var posizioneBeneficiario = "";
	var indiceTitolare = "";
	var fieldNameBeneficiario= "";
	if(fieldName.indexOf("tipoRelazioneContraenteBeneficiario") >= 0){
		//ContraenteBeneficiario
		prefissoBeneficiario = "Beneficiario";
		if (fieldName.indexOf("BeneficiarioVita") >= 0){
			prefissoBeneficiario = "BeneficiarioVita";
		}
		fieldNameBeneficiario = "tipoRelazioneContraente" + prefissoBeneficiario;
		indiceBeneficiario = fieldName.substring(fieldNameBeneficiario.length,fieldName.length);
		suffisso = "Contraente" + prefissoBeneficiario + indiceBeneficiario;
	} else if(fieldName.indexOf("tipoRelazioneContraenteTitolare") >= 0){
		//ContraenteTitolare
		prefissoBeneficiario = "Beneficiario";
		if (fieldName.indexOf("BeneficiarioVita") >= 0){
			prefissoBeneficiario = "BeneficiarioVita";
		}
		
		posizioneBeneficiario = fieldName.indexOf(prefissoBeneficiario);
		indiceTitolare = fieldName.substring("tipoRelazioneContraenteTitolare".length,posizioneBeneficiario);
		fieldNameBeneficiario = "tipoRelazioneContraenteTitolare" + indiceTitolare + prefissoBeneficiario;
		indiceBeneficiario = fieldName.substring(fieldNameBeneficiario.length,fieldName.length);
		suffisso = "ContraenteTitolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;
	}else if(fieldName.indexOf("tipoRelazioneAssicurandoBeneficiario") >= 0){
		//AssicurandoBeneficiario
		prefissoBeneficiario = "Beneficiario";
		if (fieldName.indexOf("BeneficiarioVita") >= 0){
			prefissoBeneficiario = "BeneficiarioVita";
		}
		fieldNameBeneficiario = "tipoRelazioneAssicurando" + prefissoBeneficiario;
		indiceBeneficiario = fieldName.substring(fieldNameBeneficiario.length,fieldName.length);
		suffisso = "Assicurando" + prefissoBeneficiario + indiceBeneficiario;
	} else if(fieldName.indexOf("tipoRelazioneAssicurandoTitolare") >= 0){
		//AssicurandoTitolare
		prefissoBeneficiario = "Beneficiario";
		if (fieldName.indexOf("BeneficiarioVita") >= 0){
			prefissoBeneficiario = "BeneficiarioVita";
		}
		
		posizioneBeneficiario = fieldName.indexOf(prefissoBeneficiario);
		indiceTitolare = fieldName.substring("tipoRelazioneAssicurandoTitolare".length,posizioneBeneficiario);
		fieldNameBeneficiario = "tipoRelazioneAssicurandoTitolare" + indiceTitolare + prefissoBeneficiario;
		indiceBeneficiario = fieldName.substring(fieldNameBeneficiario.length,fieldName.length);
		suffisso = "AssicurandoTitolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;
	}
	
	
	manageRelazioneSoggettoAltro(suffisso);
	manageRelazioneSoggetto3o4(suffisso);
}

Beneficiari.prototype.manageCheckIsGiaClientePersonaFisicaGiuridica = function(assicurando, posizioneAssicurando, fieldName) {
	var prefissoBeneficiario = "Beneficiario";
	
	if (fieldName.indexOf("isGiaClienteBeneficiarioVita") >= 0){
		prefissoBeneficiario = "BeneficiarioVita";
	}
	
	var temp = "isGiaCliente" + prefissoBeneficiario;
	var indiceBeneficiario = fieldName.substring(temp.length,fieldName.length);

	manageCheckIsGiaClientePersonaFisicaGiuridica(assicurando, posizioneAssicurando, prefissoBeneficiario,
			indiceBeneficiario);
}

Beneficiari.prototype.manageCheckIsGiaClienteTitolare = function(assicurando, posizioneAssicurando, fieldName) {
	var prefissoBeneficiario = "Beneficiario";

	if (fieldName.indexOf("BeneficiarioVita") >= 0){
		prefissoBeneficiario = "BeneficiarioVita";
	}

	var posBeneficiario = fieldName.indexOf(prefissoBeneficiario);
	var indiceTitolare = fieldName.substring("isGiaClienteTitolare".length,posBeneficiario);
	var fieldNameBeneficiario = "isGiaClienteTitolare" + indiceTitolare + prefissoBeneficiario;
	var indiceBeneficiario = fieldName.substring(fieldNameBeneficiario.length,fieldName.length);

	manageCheckIsGiaClienteTitolare(assicurando, posizioneAssicurando, prefissoBeneficiario, 
			indiceBeneficiario, indiceTitolare);
}

Beneficiari.prototype.manageCheckTipoBeneficiario = function(assicurando, posizioneAssicurando, prefissoBeneficiario) {
	manageCheckTipoBeneficiario(assicurando, posizioneAssicurando, prefissoBeneficiario);
}



Beneficiari.prototype.manageTipoRelazioneBeneficiariAssicurando = function(posizioneAssicurando,prefissoBeneficiario){
	var codiceFiscaleAssicurandoFieldName = "codiceFiscalePartitaIvaCliente" + posizioneAssicurando;
	if (pdf.getFieldValue(codiceFiscaleAssicurandoFieldName) !== null && pdf.getFieldValue(codiceFiscaleAssicurandoFieldName) !== ""){
		manageTipoRelazioneBeneficiariAssicurando(true, prefissoBeneficiario);
	} else {
		manageTipoRelazioneBeneficiariAssicurando(false, prefissoBeneficiario);
	}
}

Beneficiari.prototype.disabilitaTuttiBeneficiari = function(assicurando, prefissoBeneficiario){
	disableBeneficiariDesignatiFormaNominativa(assicurando, prefissoBeneficiario);	
}

Beneficiari.prototype.manageBeneficiariVitaOnChangeContraente = function(indContraente, assicurando, indAssicurando, indBeneficiarioGiuridico){
	//Questo metodo viene richiamato nei vari driver erroneamente sia in caso di onChange che in caso di onLoad
	//Per non modificare tutti i driver modifico questo metodo
	if (pdfPageFields.onPageLoad == true) {
		manageBeneficiariOnLoad(indContraente, assicurando, indAssicurando, "BeneficiarioVita", indBeneficiarioGiuridico);
	} else {
		manageBeneficiariOnChangeContraente(indContraente, assicurando, indAssicurando, "BeneficiarioVita", indBeneficiarioGiuridico);
	}
}

Beneficiari.prototype.manageBeneficiariDecessoOnChangeContraente = function(indContraente, assicurando, indAssicurando, indBeneficiarioGiuridico){
	//Questo metodo viene richiamato nei vari driver erroneamente sia in caso di onChange che in caso di onLoad
	//Per non modificare tutti i driver modifico questo metodo
	if (pdfPageFields.onPageLoad == true) {
		manageBeneficiariOnLoad(indContraente, assicurando, indAssicurando, "Beneficiario", indBeneficiarioGiuridico);
	} else {
		manageBeneficiariOnChangeContraente(indContraente, assicurando, indAssicurando, "Beneficiario", indBeneficiarioGiuridico);
	}
}

//Per la forzatura dei beneficiari giuridici
Beneficiari.prototype.manageTipoBeneficiarioVitaConForzatura = function(assicurando, indAssicurando){
	manageTipoBeneficiarioConForzatura(assicurando, indAssicurando, "BeneficiarioVita");
}

//Per la forzatura dei beneficiari giuridici
Beneficiari.prototype.manageTipoBeneficiarioDecessoConForzatura = function(assicurando, indAssicurando){
	manageTipoBeneficiarioConForzatura(assicurando, indAssicurando, "Beneficiario");
}

//gestione lunghezza campi e tipologia di inserimento prefisso e telefono beneficiari
Beneficiari.prototype.manageTelefono = function(){
	for (var i = 1; ; i++) {
		if(document.getElementById("prefissoTelefonoBeneficiario"+i) == null)
			break;
		$("#prefissoInternazionaleTelefonoBeneficiario"+i).attr("maxlength",6);
		$("#prefissoTelefonoBeneficiario"+i).attr("maxlength",6);
		$("#telefonoBeneficiario"+i).attr("maxlength",12);
		$("#prefissoInternazionaleTelefonoBeneficiario"+i).attr("onlynum","true");
		$("#prefissoTelefonoBeneficiario"+i).attr("onlynum","true");
		$("#telefonoBeneficiario"+i).attr("onlynum","true");
	}
}

var beneficiari = new Beneficiari();
