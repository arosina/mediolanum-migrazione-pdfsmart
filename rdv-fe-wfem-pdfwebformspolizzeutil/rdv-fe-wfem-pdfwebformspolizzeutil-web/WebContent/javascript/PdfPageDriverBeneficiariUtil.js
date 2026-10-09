function manageRelazioneSoggettoAltro(suffisso) {
	if (pdf.getFieldValue("tipoRelazione" + suffisso) === "004") {
		pdf.enableField("descrizioneTipoRelazione" + suffisso);
	} else {
		pdf.disableField("descrizioneTipoRelazione" + suffisso);
		clearField("descrizioneTipoRelazione" + suffisso);
	}
}

function manageRelazioneSoggetto3o4(suffisso) {
	if($("#informazioniRelazione" + suffisso).length){
		if (pdf.getFieldValue("tipoRelazione" + suffisso) === "004" ||
				pdf.getFieldValue("tipoRelazione" + suffisso) === "003"	) {
			pdf.enableField("informazioniRelazione" + suffisso);
		} else {
			pdf.disableField("informazioniRelazione" + suffisso);
			clearField("informazioniRelazione" + suffisso);
		}
	}
}


function enableDatiAnagraficiPersonaFisica(suffisso, enable) {// nel suffisso
	// deve essere
	// compreso
	// l'indice del
	// beneficiario,
	// per i
	// titolari
	// effettivi
	// anche
	// l'indice del
	// titolare

	pdf.enableField("cognome" + suffisso);
	pdf.enableField("codiceFiscale" + suffisso);

	if (enable){
		pdf.enableField("nome" + suffisso);
		pdf.enableField("sesso" + suffisso);
		pdf.enableField("dataNascita" + suffisso);
		pdf.enableField("comuneNascita" + suffisso);
		pdf.enableField("provinciaComuneNascita" + suffisso);
		pdf.enableField("nazioneComuneNascita" + suffisso);
	}else {
		pdf.disableField("nome" + suffisso);
		pdf.disableField("sesso" + suffisso);
		pdf.disableField("dataNascita" + suffisso);
		pdf.disableField("comuneNascita" + suffisso);
		pdf.disableField("provinciaComuneNascita" + suffisso);
		pdf.disableField("nazioneComuneNascita" + suffisso);
	}
}

function disableDatiAnagraficiPersonaFisica(suffisso) {// nel
	// suffisso
	// deve
	// essere
	// compreso
	// l'indice
	// del
	// beneficiario,
	// per i
	// titolari
	// effettivi
	// anche
	// l'indice
	// del
	// titolare

	pdf.disableField("cognome" + suffisso);
	pdf.disableField("nome" + suffisso);
	pdf.disableField("sesso" + suffisso);
	pdf.disableField("codiceFiscale" + suffisso);
	pdf.disableField("dataNascita" + suffisso);
	pdf.disableField("comuneNascita" + suffisso);
	pdf.disableField("provinciaComuneNascita" + suffisso);
	pdf.disableField("nazioneComuneNascita" + suffisso);


}

function clearDatiAnagraficiPersonaFisica(suffisso) {// nel suffisso
	// deve essere
	// compreso
	// l'indice del
	// beneficiario,
	// per i
	// titolari
	// effettivi
	// anche
	// l'indice del
	// titolare

	clearField("cognome" + suffisso);
	clearField("nome" + suffisso);
	pdf.setFieldValue("sesso" + suffisso, "");
	clearField("codiceFiscale" + suffisso);
	clearField("dataNascita" + suffisso);
	clearField("comuneNascita" + suffisso);
	clearField("provinciaComuneNascita" + suffisso);
	clearField("nazioneComuneNascita" + suffisso);
}

function enablePercentuale(suffisso) {
	var fieldName = "percentuale" + suffisso;
	if (isHiddenField(fieldName)){
		return;
	} else {
		pdf.enableField(fieldName);
	}
	
}

function disablePercentuale(suffisso) {
	var fieldName = "percentuale" + suffisso;
	if (isHiddenField(fieldName)){
		return;
	} else {
		pdf.disableField(fieldName);
	}
}

function clearPercentuale(suffisso) {
	var fieldName = "percentuale" + suffisso;
	if (isHiddenField(fieldName)){
		return;
	} else {
		clearField(fieldName);
	}
}

function enableCodiceCliente(suffisso) {
	pdf.enableField("codiceCliente" + suffisso);
}

function disableCodiceCliente(suffisso) {
	pdf.disableField("codiceCliente" + suffisso);
}

function clearCodiceCliente(suffisso) {
	clearField("codiceCliente" + suffisso);
}

function enableInvioComunicazione(suffisso) {
	if (document.getElementById("invioComunicazione" + suffisso) !== null){
		pdf.enableField("invioComunicazione" + suffisso);
	}
}

function disableInvioComunicazione(suffisso) {
	if (document.getElementById("invioComunicazione" + suffisso) !== null){
		pdf.disableField("invioComunicazione" + suffisso);
	}
}

function clearInvioComunicazione(suffisso) {
	if (document.getElementById("invioComunicazione" + suffisso) !== null){
		pdf.setFieldValue("invioComunicazione" + suffisso, "");
	}
}

function enableIsPep(suffisso) {
	if (document.getElementById("isPep" + suffisso) !== null){
		pdf.enableField("isPep" + suffisso);
	}
}

function disableIsPep(suffisso) {
	if (document.getElementById("isPep" + suffisso) !== null){
		pdf.disableField("isPep" + suffisso);
	}
}

function clearIsPep(suffisso) {
	if (document.getElementById("isPep" + suffisso) !== null){
		pdf.setFieldValue("isPep" + suffisso, "");
	}
}

function enableDatiAnagraficiPersonaGiuridica(suffisso, enable) {// nel suffisso deve
	// essere compreso
	// l'indice del
	// beneficiario

	pdf.enableField("ragioneSociale" + suffisso);
	pdf.enableField("codiceFiscalePartitaIva" + suffisso);
	
	if (enable){
		pdf.enableField("numeroIscrizioneCCIAA" + suffisso);
		pdf.enableField("dataIscrizioneCCIAA" + suffisso);
		pdf.enableField("provinciaIscrizioneCCIAA" + suffisso);
	}else {
		pdf.disableField("numeroIscrizioneCCIAA" + suffisso);
		pdf.disableField("dataIscrizioneCCIAA" + suffisso);
		pdf.disableField("provinciaIscrizioneCCIAA" + suffisso);
	}

}

function disableDatiAnagraficiPersonaGiuridica(suffisso) {// nel suffisso deve
	// essere compreso
	// l'indice del
	// beneficiario

	pdf.disableField("ragioneSociale" + suffisso);
	pdf.disableField("codiceFiscalePartitaIva" + suffisso);
	pdf.disableField("numeroIscrizioneCCIAA" + suffisso);
	pdf.disableField("dataIscrizioneCCIAA" + suffisso);
	pdf.disableField("provinciaIscrizioneCCIAA" + suffisso);

}

function clearDatiAnagraficiPersonaGiuridica(suffisso) {// nel suffisso deve
	// essere compreso
	// l'indice del
	// beneficiario

	clearField("ragioneSociale" + suffisso);
	clearField("codiceFiscalePartitaIva" + suffisso);
	clearField("numeroIscrizioneCCIAA" + suffisso);
	clearField("dataIscrizioneCCIAA" + suffisso);
	clearField("provinciaIscrizioneCCIAA" + suffisso);

}

function enableIndirizzoBeneficiario(suffisso) {// nel suffisso deve essere
	// compreso l'indice del
	// beneficiario
	pdf.enableField("toponimoIndirizzo" + suffisso);
	pdf.enableField("indirizzo" + suffisso);
	pdf.enableField("numeroCivicoIndirizzo" + suffisso);
	pdf.enableField("capComune" + suffisso);
	pdf.enableField("comune" + suffisso);
	pdf.enableField("provinciaComune" + suffisso);
	pdf.enableField("nazioneComune" + suffisso);
}

function disableIndirizzoBeneficiario(suffisso) {// nel suffisso deve essere
	// compreso l'indice del
	// beneficiario
	pdf.disableField("toponimoIndirizzo" + suffisso);
	pdf.disableField("indirizzo" + suffisso);
	pdf.disableField("numeroCivicoIndirizzo" + suffisso);
	pdf.disableField("capComune" + suffisso);
	pdf.disableField("comune" + suffisso);
	pdf.disableField("provinciaComune" + suffisso);
	pdf.disableField("nazioneComune" + suffisso);
}

function clearIndirizzoBeneficiario(suffisso) {// nel suffisso deve essere
	// compreso l'indice del
	// beneficiario
	clearField("codToponimoIndirizzo" + suffisso);
	clearField("toponimoIndirizzo" + suffisso);
	clearField("indirizzo" + suffisso);
	clearField("numeroCivicoIndirizzo" + suffisso);
	clearField("capComune" + suffisso);
	clearField("comune" + suffisso);
	clearField("provinciaComune" + suffisso);
	clearField("nazioneComune" + suffisso);
}

function enableTelefonoEmailBeneficiario(suffisso) {// nel suffisso deve essere
	// compreso l'indice del
	// beneficiario
	pdf.enableField("tipoTelefono" + suffisso);
	pdf.enableField("prefissoInternazionaleTelefono" + suffisso);
	pdf.enableField("prefissoTelefono" + suffisso);
	pdf.enableField("telefono" + suffisso);
	pdf.enableField("email" + suffisso);
}

function disableTelefonoEmailBeneficiario(suffisso) {// nel suffisso deve
	// essere compreso
	// l'indice del
	// beneficiario
	pdf.disableField("tipoTelefono" + suffisso);
	pdf.disableField("prefissoInternazionaleTelefono" + suffisso);
	pdf.disableField("prefissoTelefono" + suffisso);
	pdf.disableField("telefono" + suffisso);
	pdf.disableField("email" + suffisso);
}

function clearTelefonoEmailBeneficiario(suffisso) {// nel suffisso deve essere
	// compreso l'indice del
	// beneficiario
	pdf.setFieldValue("tipoTelefono" + suffisso, "");
	clearField("prefissoInternazionaleTelefono" + suffisso);
	clearField("prefissoTelefono" + suffisso);
	clearField("telefono" + suffisso);
	clearField("email" + suffisso);
}

function enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffisso) {// nel suffisso
	// deve essere
	// compreso
	// l'indice del
	// beneficiario,
	// per i
	// titolari
	// effettivi
	// anche
	// l'indice del
	// titolare

	pdf.enableField("tipoRelazioneContraente" + suffisso);
	manageRelazioneSoggettoAltro("Contraente" + suffisso);

	if (assicurando) {
		var codiceFiscaleAssicurandoFieldName = "codiceFiscalePartitaIvaCliente" + posizioneAssicurando;
		if (pdf.getFieldValue(codiceFiscaleAssicurandoFieldName) !== null && pdf.getFieldValue(codiceFiscaleAssicurandoFieldName) !== ""){
			pdf.enableField("tipoRelazioneAssicurando" + suffisso);
		} else {
			pdf.disableField("tipoRelazioneAssicurando" + suffisso);
		}
		manageRelazioneSoggettoAltro("Assicurando" + suffisso);
	}
}

function disableRelazioniBeneficiario(assicurando, suffisso) {// nel suffisso
	// deve essere
	// compreso
	// l'indice del
	// beneficiario,
	// per i
	// titolari
	// effettivi
	// anche
	// l'indice del
	// titolare

	pdf.disableField("tipoRelazioneContraente" + suffisso);
	pdf.disableField("descrizioneTipoRelazioneContraente" + suffisso);
	pdf.disableField("informazioniRelazioneContraente" + suffisso);
	if (assicurando) {
		pdf.disableField("tipoRelazioneAssicurando" + suffisso);
		pdf.disableField("descrizioneTipoRelazioneAssicurando" + suffisso);
		
	}
}

function clearRelazioniBeneficiario(assicurando, suffisso) {// nel suffisso deve
	// essere compreso
	// l'indice del
	// beneficiario, per
	// i titolari
	// effettivi anche
	// l'indice del
	// titolare

	pdf.setFieldValue("tipoRelazioneContraente" + suffisso, "");
	clearField("descrizioneTipoRelazioneContraente" + suffisso);
	clearField("informazioniRelazioneContraente" + suffisso);
	if (assicurando) {
		pdf.setFieldValue("tipoRelazioneAssicurando" + suffisso, "");
		clearField("descrizioneTipoRelazioneAssicurando" + suffisso);
	}
}

function enableLenteRicercaBeneficiario(prefissoBeneficiario,
		indiceBeneficiario, enable) {
	if (enable) {
		try {
			document.getElementById("apriPopupRicercaAction"
					+ prefissoBeneficiario + indiceBeneficiario).style.display = "";
		} catch (e) {
		}
	} else {
		try {
			document.getElementById("apriPopupRicercaAction"
					+ prefissoBeneficiario + indiceBeneficiario).style.display = "none";
		} catch (e) {
		}
	}
}

function enableLenteRicercaTitolare(prefissoBeneficiario, indiceBeneficiario,
		indiceTitolare, enable) {
	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario
			+ indiceBeneficiario;
	if (enable) {
		try {
			document
					.getElementById("apriPopupRicercaAction" + suffissoTitolare).style.display = "";
		} catch (e) {
		}
	} else {
		try {
			document
					.getElementById("apriPopupRicercaAction" + suffissoTitolare).style.display = "none";
		} catch (e) {
		}
	}
}

function enableCheckIsGiaClienteTitolare(prefissoBeneficiario,
		indiceBeneficiario) {
	for (var j = 1;; j++) {
		var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
				+ indiceBeneficiario;
		if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
			break;
		}

		pdf.enableField("isGiaCliente" + suffissoTitolare);
	}
}

function disableCheckIsGiaClienteTitolare(prefissoBeneficiario,
		indiceBeneficiario) {
	for (var j = 1;; j++) {
		var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
				+ indiceBeneficiario;
		if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
			break;
		}

		pdf.disableField("isGiaCliente" + suffissoTitolare);
	}
}

function clearCheckIsGiaClienteTitolare(prefissoBeneficiario,
		indiceBeneficiario) {
	for (var j = 1;; j++) {
		var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
				+ indiceBeneficiario;
		if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
			break;
		}

		pdf.setFieldValue("isGiaCliente" + suffissoTitolare, "");
	}
}

function clearBeneficiariDesignatiFormaNominativa(assicurando,  posizioneAssicurando,
		prefissoBeneficiario) {

	for (var i = 1;; i++) {
		var suffissoBeneficiario = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null) {
			break;
		}
		
		pdf.disableField("isGiaCliente" + suffissoBeneficiario);
		pdf.setFieldValue("isGiaCliente" + suffissoBeneficiario, "");

		clearBeneficiarioDesignatoFormaNominativa(assicurando,  posizioneAssicurando,
				prefissoBeneficiario, i);
	}
}

function clearBeneficiarioDesignatoFormaNominativa(assicurando, posizioneAssicurando,
		prefissoBeneficiario, indiceBeneficiario) {

		var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		clearCodiceCliente(suffissoBeneficiario);
		enableLenteRicercaBeneficiario(prefissoBeneficiario, indiceBeneficiario, false);
		clearTelefonoEmailBeneficiario(suffissoBeneficiario);
		clearIndirizzoBeneficiario(suffissoBeneficiario);
		clearPercentuale(suffissoBeneficiario);
		clearInvioComunicazione(suffissoBeneficiario);



		var isPersonaFisica = pdf.getFieldValue("isPersonaFisica"+ suffissoBeneficiario) === "true" ? true : false;

		if (isPersonaFisica) {
			clearDatiAnagraficiPersonaFisica(suffissoBeneficiario);
			clearRelazioniBeneficiario(assicurando, suffissoBeneficiario);
			clearIsPep(suffissoBeneficiario);
			clearMotivazionePepDaFlag(prefissoBeneficiario, indiceBeneficiario);
			manageIsGiaClientePF(assicurando, posizioneAssicurando, prefissoBeneficiario, indiceBeneficiario);
			clearDichiarazioneBeneficiarioPF(suffissoBeneficiario);
		} else {
			clearDatiAnagraficiPersonaGiuridica(suffissoBeneficiario);
			manageIsGiaClientePG(prefissoBeneficiario, indiceBeneficiario);
			for (var j = 1;; j++) {
				var suffissoTitolare = "Titolare" + j + suffissoBeneficiario;
				if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
					break;
				}

				pdf.disableField("isGiaCliente" + suffissoTitolare);
				pdf.setFieldValue("isGiaCliente" + suffissoTitolare, "");
				clearTitolare(assicurando, posizioneAssicurando,
						prefissoBeneficiario, indiceBeneficiario, j);
			}
			clearDichiarazioneBeneficiarioPG(suffissoBeneficiario);
		}
}

function disableTitolare(assicurando, prefissoBeneficiario, indiceBeneficiario, indiceTitolare) {
	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;	

	pdf.disableField("isGiaCliente" + suffissoTitolare);
	disableCodiceCliente(suffissoTitolare);
	disableDatiAnagraficiPersonaFisica(suffissoTitolare);
	disableIndirizzoBeneficiario(suffissoTitolare);
	disableTelefonoEmailBeneficiario(suffissoTitolare);
	disableRelazioniBeneficiario(assicurando, suffissoTitolare);
	enableLenteRicercaTitolare(prefissoBeneficiario, indiceBeneficiario,indiceTitolare, false);
}

function clearTitolare(assicurando, posizioneAssicurando, 
		prefissoBeneficiario, indiceBeneficiario, indiceTitolare) {


		var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;
		
		clearCodiceCliente(suffissoTitolare);
		enableLenteRicercaTitolare(prefissoBeneficiario, indiceBeneficiario, indiceTitolare, false);
		clearDatiAnagraficiPersonaFisica(suffissoTitolare);
		clearTelefonoEmailBeneficiario(suffissoTitolare);
		clearIndirizzoBeneficiario(suffissoTitolare);
		clearRelazioniBeneficiario(assicurando, suffissoTitolare);
		clearIsPep(suffissoTitolare);
		manageIsGiaClienteTitolare(assicurando, manageIsGiaClienteTitolare, prefissoBeneficiario,
				indiceBeneficiario, indiceTitolare);
}




function enableBeneficiariDesignatiFormaNominativa(assicurando, posizioneAssicurando,
		prefissoBeneficiario) {
	for (var i = 1;; i++) {
		var suffisso = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffisso) === null) {
			break;
		}

		pdf.enableField("isGiaCliente" + prefissoBeneficiario + i);

		var isPersonaFisica = pdf.getFieldValue("isPersonaFisica" + prefissoBeneficiario + i) === "true" ? true : false;


		if (isPersonaFisica) {
			manageIsGiaClientePF(assicurando, posizioneAssicurando, prefissoBeneficiario, i);
		} else {
			manageIsGiaClientePG(prefissoBeneficiario, i);

			for (var j = 1;; j++) {
				var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
						+ i;
				if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
					break;
				}

				manageIsGiaClienteTitolare(assicurando, posizioneAssicurando, prefissoBeneficiario, i, j);

			}
		}

	}
}

function manageIsGiaClientePF(assicurando, posizioneAssicurando, prefissoBeneficiario, indiceBeneficiario) {

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

	var isGiaCliente = pdf.getFieldValue("isGiaCliente" + suffissoBeneficiario);

	if (isGiaCliente === "N") {
		
		disableCodiceCliente(suffissoBeneficiario);

		enableLenteRicercaBeneficiario(prefissoBeneficiario, indiceBeneficiario, false);
		enableDatiAnagraficiPersonaFisica(suffissoBeneficiario, true);
		enablePercentuale(suffissoBeneficiario);
		enableIndirizzoBeneficiario(suffissoBeneficiario);
		enableTelefonoEmailBeneficiario(suffissoBeneficiario);
		enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffissoBeneficiario);
		enableIsPep(suffissoBeneficiario);
		enableInvioComunicazione(suffissoBeneficiario);
		enableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);

	} else if (isGiaCliente === "S") {
		
		enableCodiceCliente(suffissoBeneficiario);

		enableLenteRicercaBeneficiario(prefissoBeneficiario, indiceBeneficiario, true);
		enableDatiAnagraficiPersonaFisica(suffissoBeneficiario, false);
		disableIndirizzoBeneficiario(suffissoBeneficiario);
		disableTelefonoEmailBeneficiario(suffissoBeneficiario);
		enableInvioComunicazione(suffissoBeneficiario);
		disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);
		
		var codiceCliente = pdf.getFieldValue("codiceCliente" + suffissoBeneficiario);
		if (codiceCliente == ""){
			disablePercentuale(suffissoBeneficiario);
			disableRelazioniBeneficiario(assicurando, suffissoBeneficiario);
			disableIsPep(suffissoBeneficiario);
		} else {
			enablePercentuale(suffissoBeneficiario);
			enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffissoBeneficiario);
			enableIsPep(suffissoBeneficiario);
		}

	} else {// non valorizzato
		
		disableCodiceCliente(suffissoBeneficiario);

		enableLenteRicercaBeneficiario(prefissoBeneficiario, indiceBeneficiario, false);
		disableDatiAnagraficiPersonaFisica(suffissoBeneficiario);
		disableIndirizzoBeneficiario(suffissoBeneficiario);
		disableTelefonoEmailBeneficiario(suffissoBeneficiario);
		disableRelazioniBeneficiario(assicurando, suffissoBeneficiario);
		disableIsPep(suffissoBeneficiario);
		disablePercentuale(suffissoBeneficiario);
		disableInvioComunicazione(suffissoBeneficiario);
		disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);


	}

}

function manageIsGiaClientePG(prefissoBeneficiario, indiceBeneficiario) {

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

	var isGiaCliente = pdf.getFieldValue("isGiaCliente" + prefissoBeneficiario + indiceBeneficiario);

	if (isGiaCliente === "N") {
		
		disableCodiceCliente(suffissoBeneficiario);
		enableLenteRicercaBeneficiario(prefissoBeneficiario, indiceBeneficiario, false);
		enableDatiAnagraficiPersonaGiuridica(suffissoBeneficiario, true);
		enablePercentuale(suffissoBeneficiario);
		enableIndirizzoBeneficiario(suffissoBeneficiario);
		enableTelefonoEmailBeneficiario(suffissoBeneficiario);
		enableInvioComunicazione(suffissoBeneficiario);
		enableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);
		
		enableCheckIsGiaClienteTitolare(prefissoBeneficiario, indiceBeneficiario);

	} else if (isGiaCliente === "S") {
		
		enableCodiceCliente(suffissoBeneficiario);
		enableLenteRicercaBeneficiario(prefissoBeneficiario, indiceBeneficiario, true);
		enableDatiAnagraficiPersonaGiuridica(suffissoBeneficiario, false);
		disableTelefonoEmailBeneficiario(suffissoBeneficiario);
		disableIndirizzoBeneficiario(suffissoBeneficiario);
		disableCheckIsGiaClienteTitolare(prefissoBeneficiario, indiceBeneficiario);
		enableInvioComunicazione(suffissoBeneficiario);
		disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);
		
		var codiceCliente = pdf.getFieldValue("codiceCliente" + suffissoBeneficiario);
		if (codiceCliente == ""){
			disablePercentuale(suffissoBeneficiario);
		} else {
			enablePercentuale(suffissoBeneficiario);
		}

	} else {// non valorizzato
		
		disableCodiceCliente(suffissoBeneficiario);
		enableLenteRicercaBeneficiario(prefissoBeneficiario, indiceBeneficiario, false);
		disableDatiAnagraficiPersonaGiuridica(suffissoBeneficiario);
		disableIndirizzoBeneficiario(suffissoBeneficiario);
		disableTelefonoEmailBeneficiario(suffissoBeneficiario);
		disableCheckIsGiaClienteTitolare(prefissoBeneficiario, indiceBeneficiario);
		disablePercentuale(suffissoBeneficiario);
		disableInvioComunicazione(suffissoBeneficiario);
		disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);


	}
}

function manageIsGiaClienteTitolare(assicurando, posizioneAssicurando, prefissoBeneficiario,
		indiceBeneficiario, indiceTitolare) {

	var suffissoBeneficiario = prefissoBeneficiario	+ indiceBeneficiario;
	
	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario
	+ indiceBeneficiario;

	var isGiaCliente = pdf.getFieldValue("isGiaCliente" + suffissoBeneficiario);
	var isGiaClienteTitolare = pdf.getFieldValue("isGiaCliente" + suffissoTitolare);

	if (isGiaClienteTitolare === "N") {

		disableCodiceCliente(suffissoTitolare);
		enableDatiAnagraficiPersonaFisica(suffissoTitolare, true);
		enableTelefonoEmailBeneficiario(suffissoTitolare);
		enableIndirizzoBeneficiario(suffissoTitolare);
		enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffissoTitolare);
		enableIsPep(suffissoTitolare);
		enableLenteRicercaTitolare(prefissoBeneficiario, indiceBeneficiario,
				indiceTitolare, false);

	} else if (isGiaClienteTitolare === "S") {

		if (isGiaCliente === "N"){
			enableCodiceCliente(suffissoTitolare);
			enableDatiAnagraficiPersonaFisica(suffissoTitolare, false);
			enableLenteRicercaTitolare(prefissoBeneficiario, indiceBeneficiario,
					indiceTitolare, true);
			disableRelazioniBeneficiario(assicurando, suffissoTitolare);
			disableIsPep(suffissoTitolare);
		} else {
			disableCodiceCliente(suffissoTitolare);
			disableDatiAnagraficiPersonaFisica(suffissoTitolare);
			enableLenteRicercaTitolare(prefissoBeneficiario, indiceBeneficiario,
					indiceTitolare, false);
			enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffissoTitolare);
			enableIsPep(suffissoTitolare);
		}
		
		disableIndirizzoBeneficiario(suffissoTitolare);
		disableTelefonoEmailBeneficiario(suffissoTitolare);

			
		

	} else {// non valorizzato
		
		disableCodiceCliente(suffissoTitolare);
		disableDatiAnagraficiPersonaFisica(suffissoTitolare);
		disableIndirizzoBeneficiario(suffissoTitolare);
		disableTelefonoEmailBeneficiario(suffissoTitolare);
		disableRelazioniBeneficiario(assicurando, suffissoTitolare);
		disableIsPep(suffissoTitolare);
		enableLenteRicercaTitolare(prefissoBeneficiario, indiceBeneficiario,
				indiceTitolare, false);
	}

}

function manageCheckIsGiaClientePersonaFisicaGiuridica(assicurando, posizioneAssicurando,
		prefissoBeneficiario, indiceBeneficiario) {

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	clearCodiceCliente(suffissoBeneficiario);
	clearTelefonoEmailBeneficiario(suffissoBeneficiario);
	clearIndirizzoBeneficiario(suffissoBeneficiario);
	clearPercentuale(suffissoBeneficiario);
	clearInvioComunicazione(suffissoBeneficiario);

	var isPersonaFisica = pdf.getFieldValue("isPersonaFisica" + suffissoBeneficiario) === "true" ? true : false;

	if (isPersonaFisica) {
		clearDatiAnagraficiPersonaFisica(suffissoBeneficiario);
		clearRelazioniBeneficiario(assicurando, suffissoBeneficiario);
		
		var gestioneFlagPep = document.getElementById("isPep" + suffissoBeneficiario) !== null;
		if (gestioneFlagPep){
			clearIsPep(suffissoBeneficiario);
			clearMotivazionePepDaFlag(prefissoBeneficiario, indiceBeneficiario);		
		} else {
			clearMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario, false, null);
		}

		manageIsGiaClientePF(assicurando, posizioneAssicurando, prefissoBeneficiario, indiceBeneficiario);
		manageMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario);
		clearDichiarazioneBeneficiarioPF(suffissoBeneficiario);

	} else {
		clearDatiAnagraficiPersonaGiuridica(suffissoBeneficiario);
		manageIsGiaClientePG(prefissoBeneficiario, indiceBeneficiario);
		for (var j = 1;; j++) {
			var suffissoTitolare = "Titolare" + j + suffissoBeneficiario;
			if (document.getElementById("isGiaCliente" + suffissoTitolare) == null)
				break;

			pdf.setFieldValue("isGiaCliente" + suffissoTitolare, "");
			clearCodiceCliente(suffissoTitolare);
			clearDatiAnagraficiPersonaFisica(suffissoTitolare);
			clearTelefonoEmailBeneficiario(suffissoTitolare);
			clearIndirizzoBeneficiario(suffissoTitolare);
			clearRelazioniBeneficiario(assicurando, suffissoTitolare);
			clearIsPep(suffissoTitolare);

			clearMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario, true, j);
			manageIsGiaClienteTitolare(assicurando, posizioneAssicurando, prefissoBeneficiario, indiceBeneficiario, j);

			manageMotivazionePepBeneficiarioTitolare(prefissoBeneficiario, indiceBeneficiario, j);

		}
		clearDichiarazioneBeneficiarioPG(suffissoBeneficiario);
	}

}

function manageCheckIsGiaClienteTitolare(assicurando, posizioneAssicurando, prefissoBeneficiario, indiceBeneficiario, indiceTitolare) {

	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

	clearCodiceCliente(suffissoTitolare);
	clearTelefonoEmailBeneficiario(suffissoTitolare);
	clearIndirizzoBeneficiario(suffissoTitolare);
	clearDatiAnagraficiPersonaFisica(suffissoTitolare);
	clearRelazioniBeneficiario(assicurando, suffissoTitolare);
	clearIsPep(suffissoTitolare);
	clearMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario, true, indiceTitolare);

	manageIsGiaClienteTitolare(assicurando, posizioneAssicurando, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
	manageMotivazionePepBeneficiarioTitolare(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
}

function addAttributesBeneficiariDesignatiFormaNominativa(assicurando,
		prefissoBeneficiario) {

	for (var i = 1;; i++) {
		var suffissoBeneficiario = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null) {
			break;
		}

		addAttributesBeneficiarioDesignatoFormaNominativa(assicurando, prefissoBeneficiario, i);
	}

}

function addAttributesBeneficiarioDesignatoFormaNominativa(assicurando, prefissoBeneficiario, indiceBeneficiario) {

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

	$("#toponimoIndirizzo"+ suffissoBeneficiario).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	$("#indirizzo"+ suffissoBeneficiario).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	$("#indirizzo" + suffissoBeneficiario).attr("maxLength", 60);
	
	$("#numeroCivicoIndirizzo"+ suffissoBeneficiario).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	$("#numeroCivicoIndirizzo" + suffissoBeneficiario).attr("maxLength", 4);
	
	$("#comune" + suffissoBeneficiario).attr("maxLength", 50);

	
	$("#provinciaComune"+ suffissoBeneficiario).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#nazioneComune"+ suffissoBeneficiario).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	$("#prefissoInternazionaleTelefono" + suffissoBeneficiario).attr("onlynum",	"true");
	$("#prefissoInternazionaleTelefono" + suffissoBeneficiario).attr("maxLength",6);
	
	$("#prefissoTelefono" + suffissoBeneficiario).attr("onlynum", "true");
	$("#prefissoTelefono" + suffissoBeneficiario).attr("maxLength", 5);

	$("#telefono" + suffissoBeneficiario).attr("onlynum", "true");
	$("#telefono" + suffissoBeneficiario).attr("maxLength", 12);

	$("#email" + suffissoBeneficiario).attr("maxLength", 50);

	$("#descrizioneTipoRelazioneContraente" + suffissoBeneficiario).bind({
		keypress : function(event) {
			manageCaratteriDescrizioneAltro(event);
		},
		focusout : function(event) {
			manageCaratteriDescrizioneAltro(event);
		}
	});

	if (assicurando) {

		$("#descrizioneTipoRelazioneAssicurando" + suffissoBeneficiario).bind({
			keypress : function(event) {
				manageCaratteriDescrizioneAltro(event);
			},
			focusout : function(event) {
				manageCaratteriDescrizioneAltro(event);
			}
		});
	}
	
	
	var isPersonaFisica = pdf.getFieldValue("isPersonaFisica"
			+ suffissoBeneficiario) === "true" ? true : false;
	
	if (isPersonaFisica){
		
		$("#nome"+ suffissoBeneficiario).bind({
			keyup : function(event) {
				this.value = this.value.toUpperCase();
			}
		});
		$("#nome" + suffissoBeneficiario).attr("maxLength", 80);
		
		$("#cognome"+ suffissoBeneficiario).bind({
			keyup : function(event) {
				this.value = this.value.toUpperCase();
			}
		});
		$("#cognome" + suffissoBeneficiario).attr("maxLength", 80);
		
		$("#codiceFiscale"+ suffissoBeneficiario).bind({
			keyup : function(event) {
				this.value = this.value.toUpperCase();
			}
		});
		
		$("#provinciaComuneNascita"+ suffissoBeneficiario).bind({
			keyup : function(event) {
				this.value = this.value.toUpperCase();
			}
		});
		
		$("#nazioneComuneNascita"+ suffissoBeneficiario).bind({
			keyup : function(event) {
				this.value = this.value.toUpperCase();
			}
		});
		
		$("#comuneNascita" + suffissoBeneficiario).attr("maxLength", 50);


	} else {
		
		$("#codiceFiscalePartitaIva" + suffissoBeneficiario).attr("onlynum",
		"true");
		
		$("#ragioneSociale"+ suffissoBeneficiario).bind({
			keyup : function(event) {
				this.value = this.value.toUpperCase();
			}
		});
		$("#ragioneSociale" + suffissoBeneficiario).attr("maxLength", 80);
		
		$("#provinciaIscrizioneCCIAA"+ suffissoBeneficiario).bind({
			keyup : function(event) {
				this.value = this.value.toUpperCase();
			}
		});

		
		for (var j = 1;; j++) {

			var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
					+ indiceBeneficiario;

			if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
				break;
			}

			addAttributesTitolareBeneficiarioDesignatoFormaNominativa(assicurando,
					prefissoBeneficiario, indiceBeneficiario, j);
		}
	}

	

}

function addAttributesTitolareBeneficiarioDesignatoFormaNominativa(assicurando,
		prefissoBeneficiario, indiceBeneficiario, indiceTitolare) {

	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario
			+ indiceBeneficiario;
	
	$("#prefissoInternazionaleTelefono" + suffissoTitolare).attr("onlynum",
	"true");
	$("#prefissoInternazionaleTelefono" + suffissoTitolare).attr("maxLength",6);

	$("#prefissoTelefono" + suffissoTitolare).attr("onlynum", "true");
	$("#prefissoTelefono" + suffissoTitolare).attr("maxLength", 5);

	$("#telefono" + suffissoTitolare).attr("onlynum", "true");
	$("#telefono" + suffissoTitolare).attr("maxLength", 12);

	$("#email" + suffissoTitolare).attr("maxLength", 50);


	$("#nome"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	$("#nome" + suffissoTitolare).attr("maxLength", 80);

	
	$("#cognome"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	$("#cognome" + suffissoTitolare).attr("maxLength", 80);

	
	$("#codiceFiscale"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#provinciaComuneNascita"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#nazioneComuneNascita"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#comuneNascita" + suffissoTitolare).attr("maxLength", 50);

	
	$("#toponimoIndirizzo"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#indirizzo"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	$("#indirizzo" + suffissoTitolare).attr("maxLength", 60);

	
	$("#numeroCivicoIndirizzo"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	$("#numeroCivicoIndirizzo" + suffissoTitolare).attr("maxLength", 4);

	
	$("#provinciaComune"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#nazioneComune"+ suffissoTitolare).bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	$("#comune" + suffissoTitolare).attr("maxLength", 50);

	
	$("#descrizioneTipoRelazioneContraente" + suffissoTitolare).bind({
		keypress : function(event) {
			manageCaratteriDescrizioneAltro(event);
		},
		focusout : function(event) {
			manageCaratteriDescrizioneAltro(event);
		}
	});

	if (assicurando) {
		$("#descrizioneTipoRelazioneAssicurando" + suffissoTitolare).bind({
			keypress : function(event) {
				manageCaratteriDescrizioneAltro(event);
			},
			focusout : function(event) {
				manageCaratteriDescrizioneAltro(event);
			}
		});
	}
	

}

function manageRelazioni(assicurando, prefissoBeneficiario) {

	for (var i = 1;; i++) {
		var suffisso = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffisso) === null) {
			break;
		}

		manageRelazioneSoggettoAltro("Contraente" + suffisso);

		if (assicurando) {
			manageRelazioneSoggettoAltro("Assicurando" + suffisso);
		}

		for (var j = 1;; j++) {

			var suffissoTitolare = "Titolare" + j + prefissoBeneficiario + i;
			if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
				break;
			}

			manageRelazioneSoggettoAltro("Contraente" + suffissoTitolare);

			if (assicurando) {
				manageRelazioneSoggettoAltro("Assicurando" + suffissoTitolare);
			}

		}
	}
}



function manageTipoRelazioniAssicurando(prefissoBeneficiario, enabled) {
	if (pdf.getFieldValue("tipo" + prefissoBeneficiario) === "031") {

		for (var i = 1;; i++) {
			var suffissoBeneficiario = prefissoBeneficiario + i;
			if (document.getElementById("isPersonaFisica"
					+ suffissoBeneficiario) === null) {
				break;
			}

			if (enabled) {
				pdf.enableField("tipoRelazioneAssicurando"
						+ suffissoBeneficiario);
			} else {
				pdf.disableField("tipoRelazioneAssicurando"
						+ suffissoBeneficiario);
				pdf.setFieldValue("tipoRelazioneAssicurando"
						+ suffissoBeneficiario, "");
				pdf.disableField("descrizioneTipoRelazioneAssicurando"
						+ suffissoBeneficiario);
				pdf.setFieldValue("descrizioneTipoRelazioneAssicurando"
						+ suffissoBeneficiarioi, "");
			}

			for (var j = 1;; j++) {

				var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
						+ i;
				if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
					break;
				}

				if (enabled) {
					pdf.enableField("tipoRelazioneAssicurando"
							+ suffissoTitolare);
				} else {
					pdf.disableField("tipoRelazioneAssicurando"
							+ suffissoTitolare);
					pdf.setFieldValue("tipoRelazioneAssicurando"
							+ suffissoTitolare, "");
					pdf.disableField("descrizioneTipoRelazioneAssicurando"
							+ suffissoTitolare);
					pdf.setFieldValue("descrizioneTipoRelazioneAssicurando"
							+ suffissoTitolare, "");
				}
			}
		}
	}
}

function manageOnLoad(assicurando, posizioneAssicurando, prefissoBeneficiario) {
	
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);
	if (tipoBeneficiario === "031") {
		enableBeneficiariDesignatiFormaNominativa(assicurando, posizioneAssicurando, prefissoBeneficiario);
	} else {
		clearBeneficiariDesignatiFormaNominativa(assicurando, posizioneAssicurando, prefissoBeneficiario);
	}

	addAttributesBeneficiariDesignatiFormaNominativa(assicurando, prefissoBeneficiario);

	manageRelazioni(assicurando, prefissoBeneficiario);
}

function manageCheckTipoBeneficiario(assicurando, posizioneAssicurando, prefissoBeneficiario) {
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);
	if (tipoBeneficiario === "031") {
		enableBeneficiariDesignatiFormaNominativa(assicurando, posizioneAssicurando, prefissoBeneficiario);
	} else {
		clearBeneficiariDesignatiFormaNominativa(assicurando,posizioneAssicurando, prefissoBeneficiario);
	}
	clearMotivazionePep(prefissoBeneficiario);
	manageMotivazionePep(prefissoBeneficiario);
	clearDichiarazioniBeneficiari(prefissoBeneficiario);
	manageDichiarazioniBeneficiari(prefissoBeneficiario);
}

function manageTipoBeneficiarioOnLoad(assicurando, posizioneAssicurando, prefissoBeneficiario) {
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);
	if (tipoBeneficiario === "031") {
		enableBeneficiariDesignatiFormaNominativa(assicurando, posizioneAssicurando, prefissoBeneficiario);
	}
	manageMotivazionePep(prefissoBeneficiario);
	manageDichiarazioniBeneficiari(prefissoBeneficiario);
}

function manageChangeCodiceClienteSezioneBeneficiarioOnEventCallBack(assicurando, posizioneAssicurando, eventsArgs, targets) {
	
	var fields = eventsArgs.split(",");
	var prefissoBeneficiario = fields[0];
	var indiceBeneficiario = fields[1];
	var tipologiaSoggetto = fields[2];

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	clearPercentuale(suffissoBeneficiario);
	enablePercentuale(suffissoBeneficiario);
	
	disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);
	
	if (tipologiaSoggetto === "PF") {
		//Se esiste flag isPep pulisco anche le motivazioni
		if (document.getElementById("isPep" + suffissoBeneficiario) !== null){
			clearIsPep(suffissoBeneficiario);
			manageMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario);
			clearMotivazionePepDaFlag();
		}
		
		// se sono persona fisica abilito le relazioni della PF e le motivazioni pep
		clearRelazioniBeneficiario(assicurando, suffissoBeneficiario);
		enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffissoBeneficiario);
		enableIsPep(suffissoBeneficiario);

	} else if (tipologiaSoggetto === "PG") {
		// se sono persona giuridica devo abilitare le relazioni dei titolari,
		// se popolati e le motivazioni pep
		for (var j = 1;; j++) {

			var suffissoTitolare = "Titolare" + j + prefissoBeneficiario + indiceBeneficiario;
			if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
				break;
			}

			//Se esiste flag isPep pulisco anche le motivazioni
			if (document.getElementById("isPep" + suffissoTitolare) !== null){
				clearIsPep(suffissoTitolare);
				motivazionePep.manageIsPepTitolare(prefissoBeneficiario, indiceBeneficiario, j);
			}
			
			var codiceCliente = pdf.getFieldValue("codiceCliente" + suffissoTitolare);
			
			clearRelazioniBeneficiario(assicurando, suffissoTitolare);
			if (codiceCliente === null || codiceCliente === "") {
				disableRelazioniBeneficiario(assicurando, suffissoTitolare);
				disableIsPep(suffissoTitolare);
			} else {
				enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffissoTitolare);
				enableIsPep(suffissoTitolare);
			}
		}
	}

	// sezione pep
	manageMotivazionePepBeneficiarioPersonaFisicaGiuridica(prefissoBeneficiario, indiceBeneficiario);

	// sezione telefono e relazioni
	addAttributesBeneficiarioDesignatoFormaNominativa(assicurando, prefissoBeneficiario, indiceBeneficiario);

	// pulisco eventuali errori
	//clearErrors(targets);

}

function manageChangeCodiceClienteSezioneTitolareBeneficiarioOnEventCallBack(
		assicurando, posizioneAssicurando, eventsArgs, targets) {
	var fields = eventsArgs.split(",");
	var prefissoBeneficiario = fields[0];
	var indiceBeneficiario = fields[1];
	var indiceTitolare = fields[2];

	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

	var codiceCliente = pdf.getFieldValue("codiceCliente" + suffissoTitolare);
	if (codiceCliente === null || codiceCliente === "") {
		disableRelazioniBeneficiario(assicurando, suffissoTitolare);
		disableIsPep(suffissoTitolare);
	} else {
		enableRelazioniBeneficiario(assicurando, posizioneAssicurando, suffissoTitolare);
		enableIsPep(suffissoTitolare);
	}

	//Pulisco le relazioni perchè ho cambiato il cliente
	clearRelazioniBeneficiario(assicurando, suffissoTitolare);
	// sezione pep
	manageMotivazionePepBeneficiarioTitolare(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);

	// sezione telefono e relazioni
	addAttributesTitolareBeneficiarioDesignatoFormaNominativa(assicurando, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
	
	// pulisco eventuali errori
	//clearErrors(targets);
}

function manageTipoRelazioneBeneficiariAssicurando(enabled, prefissoBeneficiario){
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);
	if (tipoBeneficiario === "031") {
		for (var i=1; ; i++){
			var suffissoBeneficiario = prefissoBeneficiario + i;
			if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null) {
				break;
			}
			
			var isPersonaFisica = pdf.getFieldValue("isPersonaFisica" + suffissoBeneficiario) === "true" ? true : false;
			
			var isGiaCliente = pdf.getFieldValue("isGiaCliente" + suffissoBeneficiario);

			if (isPersonaFisica){
				if (enabled && isGiaCliente !== null && isGiaCliente!== '') {
					pdf.enableField("tipoRelazioneAssicurando"+suffissoBeneficiario);
				} else {
					pdf.disableField("tipoRelazioneAssicurando"+suffissoBeneficiario);
					pdf.setFieldValue("tipoRelazioneAssicurando"+suffissoBeneficiario,"");
					pdf.disableField("descrizioneTipoRelazioneAssicurando"+suffissoBeneficiario);
					clearField("descrizioneTipoRelazioneAssicurando"+suffissoBeneficiario);
				}
			} else {
				for (var j = 1;; j++) {
					var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
							+ i;
					if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
						break;
					}
					
					var isGiaClienteTitolare = pdf.getFieldValue("isGiaCliente" + suffissoTitolare);


					if (enabled  && isGiaClienteTitolare !== null && isGiaClienteTitolare!== '') {
						pdf.enableField("tipoRelazioneAssicurando"+suffissoTitolare);
					} else {
						pdf.disableField("tipoRelazioneAssicurando"+suffissoTitolare);
						pdf.setFieldValue("tipoRelazioneAssicurando"+suffissoTitolare,"");
						pdf.disableField("descrizioneTipoRelazioneAssicurando"+suffissoTitolare);
						clearField("descrizioneTipoRelazioneAssicurando"+suffissoTitolare);
					}
				}
			}
			
		}
	}
}

function manageBeneficiariOnChangeContraente(indContraente, assicurando, indAssicurando, prefissoBeneficiario, indBeneficiarioGiuridico){

	var suffissoBeneficiarioGiuridico = prefissoBeneficiario+indBeneficiarioGiuridico;
	if (pdf.getFieldValue("isPersonaFisicaCliente"+indContraente) == "false" && pdf.getFieldValue("isSocietaFiduciariaCliente"+indContraente) == false){

		pdf.disableField("tipo"+prefissoBeneficiario);
		disableBeneficiariDesignatiFormaNominativa(assicurando, prefissoBeneficiario);
		
		//Abilito le relazioni tra beneficiari e contraente/assicurato
		if (pdf.getFieldValue("codiceClienteTitolare1"+suffissoBeneficiarioGiuridico) !== ""){
			pdf.enableField("tipoRelazioneContraenteTitolare1"+suffissoBeneficiarioGiuridico);
			manageDescrizioneTipoRelazioneAltro("ContraenteTitolare1"+suffissoBeneficiarioGiuridico);
			pdf.enableField("tipoRelazioneAssicurandoTitolare1"+suffissoBeneficiarioGiuridico);			
			manageDescrizioneTipoRelazioneAltro("AssicurandoTitolare1"+suffissoBeneficiarioGiuridico);
		}
		
		if (pdf.getFieldValue("codiceClienteTitolare2"+suffissoBeneficiarioGiuridico) !== ""){
			pdf.enableField("tipoRelazioneContraenteTitolare2"+suffissoBeneficiarioGiuridico);
			manageDescrizioneTipoRelazioneAltro("ContraenteTitolare2"+suffissoBeneficiarioGiuridico);
			pdf.enableField("tipoRelazioneAssicurandoTitolare2"+suffissoBeneficiarioGiuridico);			
			manageDescrizioneTipoRelazioneAltro("AssicurandoTitolare2"+suffissoBeneficiarioGiuridico);
		}
		
		//Abilito l'invio delle comunicazioni
		enableInvioComunicazione(suffissoBeneficiarioGiuridico);
		
		//Visualizzo icona informativa
		document.getElementById("info"+prefissoBeneficiario).style.display = "";
		
	} else {
		pdf.enableField("tipo"+prefissoBeneficiario);
		//Gestione abilitazione / disabilitazione beneficiari
		manageCheckTipoBeneficiario(assicurando, indAssicurando, prefissoBeneficiario);
		//Nascondo icona informativa
		document.getElementById("info"+prefissoBeneficiario).style.display = "none";
	}	
	
}

function manageBeneficiariOnLoad(indContraente, assicurando, indAssicurando, prefissoBeneficiario, indBeneficiarioGiuridico){

	var suffissoBeneficiarioGiuridico = prefissoBeneficiario+indBeneficiarioGiuridico;
	if (pdf.getFieldValue("isPersonaFisicaCliente"+indContraente) == "false" && pdf.getFieldValue("isSocietaFiduciariaCliente"+indContraente) == false){

		pdf.disableField("tipo"+prefissoBeneficiario);
		disableBeneficiariDesignatiFormaNominativa(assicurando, prefissoBeneficiario);
		
		//Abilito le relazioni tra beneficiari e contraente/assicurato
		if (pdf.getFieldValue("codiceClienteTitolare1"+suffissoBeneficiarioGiuridico) !== ""){
			pdf.enableField("tipoRelazioneContraenteTitolare1"+suffissoBeneficiarioGiuridico);
			manageDescrizioneTipoRelazioneAltro("ContraenteTitolare1"+suffissoBeneficiarioGiuridico);
			pdf.enableField("tipoRelazioneAssicurandoTitolare1"+suffissoBeneficiarioGiuridico);			
			manageDescrizioneTipoRelazioneAltro("AssicurandoTitolare1"+suffissoBeneficiarioGiuridico);
		}
		
		if (pdf.getFieldValue("codiceClienteTitolare2"+suffissoBeneficiarioGiuridico) !== ""){
			pdf.enableField("tipoRelazioneContraenteTitolare2"+suffissoBeneficiarioGiuridico);
			manageDescrizioneTipoRelazioneAltro("ContraenteTitolare2"+suffissoBeneficiarioGiuridico);
			pdf.enableField("tipoRelazioneAssicurandoTitolare2"+suffissoBeneficiarioGiuridico);			
			manageDescrizioneTipoRelazioneAltro("AssicurandoTitolare2"+suffissoBeneficiarioGiuridico);
		}
		
		//Abilito l'invio delle comunicazioni
		enableInvioComunicazione(suffissoBeneficiarioGiuridico);
		
		//Visualizzo icona informativa
		document.getElementById("info"+prefissoBeneficiario).style.display = "";
		
	} else {
		pdf.enableField("tipo"+prefissoBeneficiario);
		//Gestione abilitazione / disabilitazione beneficiari
		manageTipoBeneficiarioOnLoad(assicurando, indAssicurando, prefissoBeneficiario);
		//Nascondo icona informativa
		document.getElementById("info"+prefissoBeneficiario).style.display = "none";
	}	
	
}

//Nuove function
function getIndPrimoBeneficiarioGiuridico(prefissoBeneficiario){
	
	//Cerco indice del primo beneficiario giuridico
	for (var i=1; ; i++){
		var suffissoBeneficiario = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null) {
			break;
		}
		
		var isPersonaFisica = pdf.getFieldValue("isPersonaFisica"+ suffissoBeneficiario) === "true" ? true : false;
		
		if (!isPersonaFisica){
			 return i;
		}
	}
	return -1;
}

function manageTipoBeneficiarioConForzatura(assicurando, posizioneAssicurando, prefissoBeneficiario){
	
	var isPersonaGiuridica = pdf.getFieldValue("isPersonaFisicaCliente1") == "false";
	var isSocietaFiduciaria = pdf.getFieldValue("isSocietaFiduciariaCliente1") == true;
	var isBeneficiarioGiuridicoDaForzare = (document.getElementById("isBeneficiarioGiuridicoDaForzare") != null && pdf.getFieldValue("isBeneficiarioGiuridicoDaForzare") == true);
	
	if (!isPersonaGiuridica || isSocietaFiduciaria || !isBeneficiarioGiuridicoDaForzare){
		manageTipoBeneficiario(assicurando, posizioneAssicurando, prefissoBeneficiario);
		return;
	}
	
	pdf.disableField("tipo"+prefissoBeneficiario);
	disableBeneficiariDesignatiFormaNominativa(assicurando, prefissoBeneficiario);
	
	var indBeneficiarioGiuridico = getIndPrimoBeneficiarioGiuridico(prefissoBeneficiario);
	var suffissoBeneficiarioGiuridico = prefissoBeneficiario+indBeneficiarioGiuridico;
	//Abilito per il solo contraente giuridico indicato
	//- le relazioni tra beneficiari e contraente/assicurato
	//- isPep
	// l'invio delle comunicazioni
	//Per gestione dinamica dell'indice del beneficiario giuridico
	if (pdf.getFieldValue("codiceClienteTitolare1"+suffissoBeneficiarioGiuridico) !== ""){
		pdf.enableField("tipoRelazioneContraenteTitolare1"+suffissoBeneficiarioGiuridico);
		manageDescrizioneTipoRelazioneAltro("ContraenteTitolare1"+suffissoBeneficiarioGiuridico);
		if (assicurando) {
			pdf.enableField("tipoRelazioneAssicurandoTitolare1"+suffissoBeneficiarioGiuridico);
			manageDescrizioneTipoRelazioneAltro("AssicurandoTitolare1"+suffissoBeneficiarioGiuridico);
		}
		enableIsPep("Titolare1" + suffissoBeneficiarioGiuridico);
	}
	
	if (pdf.getFieldValue("codiceClienteTitolare2"+suffissoBeneficiarioGiuridico) !== ""){
		pdf.enableField("tipoRelazioneContraenteTitolare2"+suffissoBeneficiarioGiuridico);
		manageDescrizioneTipoRelazioneAltro("ContraenteTitolare2"+suffissoBeneficiarioGiuridico);
		if (assicurando) {
			pdf.enableField("tipoRelazioneAssicurandoTitolare2"+suffissoBeneficiarioGiuridico);
			manageDescrizioneTipoRelazioneAltro("AssicurandoTitolare2"+suffissoBeneficiarioGiuridico);
		}
		enableIsPep("Titolare2" + suffissoBeneficiarioGiuridico);
	}
	
	//Abilito l'invio delle comunicazioni
	enableInvioComunicazione(suffissoBeneficiarioGiuridico);
	
	motivazionePep.manageCasoDecesso();
	//manageMotivazionePep(prefissoBeneficiario);
	manageDichiarazioniBeneficiari(prefissoBeneficiario);
	
	//Visualizzo icona informativa
	document.getElementById("info"+prefissoBeneficiario).style.display = "";
}

function manageTipoBeneficiario(assicurando, posizioneAssicurando, prefissoBeneficiario){
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);
	if (tipoBeneficiario === "031") {
		enableBeneficiariDesignatiFormaNominativa(assicurando, posizioneAssicurando, prefissoBeneficiario);
	} else {
		disableBeneficiariDesignatiFormaNominativa(assicurando, prefissoBeneficiario);
	}
	manageMotivazionePep(prefissoBeneficiario);
	
	manageDichiarazioniBeneficiari(prefissoBeneficiario);
	
	//Nascondo icona informativa
	document.getElementById("info"+prefissoBeneficiario).style.display = "none";

}

function disableBeneficiariDesignatiFormaNominativa(assicurando, prefissoBeneficiario){
	for (var indiceBeneficiario = 1;; indiceBeneficiario++) {
		
		var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null) {
			break;
		}
		var isPersonaFisica = pdf.getFieldValue("isPersonaFisica" + suffissoBeneficiario) === "true" ? true : false;

		pdf.disableField("isGiaCliente" + suffissoBeneficiario);
		if (isPersonaFisica){
			disableCodiceCliente(suffissoBeneficiario);
			enableLenteRicercaBeneficiario(prefissoBeneficiario,indiceBeneficiario, false);
			disableDatiAnagraficiPersonaFisica(suffissoBeneficiario);
			disableIndirizzoBeneficiario(suffissoBeneficiario);
			disableTelefonoEmailBeneficiario(suffissoBeneficiario);
			disableRelazioniBeneficiario(assicurando, suffissoBeneficiario);
			disablePercentuale(suffissoBeneficiario);
			disableInvioComunicazione(suffissoBeneficiario);
			disableIsPep(suffissoBeneficiario);
			disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);
		}else{
			disableCodiceCliente(suffissoBeneficiario);
			enableLenteRicercaBeneficiario(prefissoBeneficiario,indiceBeneficiario, false);
			disableDatiAnagraficiPersonaGiuridica(suffissoBeneficiario);
			disableIndirizzoBeneficiario(suffissoBeneficiario);
			disableTelefonoEmailBeneficiario(suffissoBeneficiario);
			disableCheckIsGiaClienteTitolare(prefissoBeneficiario,indiceBeneficiario);
			disablePercentuale(suffissoBeneficiario);
			disableInvioComunicazione(suffissoBeneficiario);
			
			for (var indiceTitolare = 1;; indiceTitolare++) {
				var suffissoTitolare = "Titolare" + indiceTitolare + suffissoBeneficiario;
				if (document.getElementById("isGiaCliente" + suffissoTitolare) === null) {
					break;
				}

				pdf.disableField("isGiaCliente" + suffissoTitolare);
				disableTitolare(assicurando, prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
				disableIsPep(suffissoTitolare);
			}
			disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario);
		}
	}	
}

function manageDescrizioneTipoRelazioneAltro(suffisso){
	
	if (pdf.getFieldValue("tipoRelazione" + suffisso) === "004") {
		pdf.enableField("descrizioneTipoRelazione" + suffisso);
	} else {
		pdf.disableField("descrizioneTipoRelazione" + suffisso);
	}
}