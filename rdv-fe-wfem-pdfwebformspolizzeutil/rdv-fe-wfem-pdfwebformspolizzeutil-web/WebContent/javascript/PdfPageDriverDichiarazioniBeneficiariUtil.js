function manageDichiarazioniBeneficiari(prefissoBeneficiario) {

	//Verifico se ci sono le dichiarazioni sul modulo se no esco
	var cognomeDichiarazione = document.getElementById("cognomeDichiarazione"+ prefissoBeneficiario +"1");
	if (cognomeDichiarazione === null )
		return;
	
	//Ciclo sulle dichiarazioni per disabilitarle
	for (var i = 1;; i++) {
		
		var suffissoBeneficiario = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null){
			break;			
		}

		manageDichiarazioniBeneficiario(prefissoBeneficiario, i);
	}	
}

function manageDichiarazioniBeneficiario(prefissoBeneficiario, indBeneficiario) {

	var suffissoBeneficiario = prefissoBeneficiario + indBeneficiario;
	var isPersonaFisica = pdf.getFieldValue("isPersonaFisica" + suffissoBeneficiario) === "true" ? true : false;
	
	if (isPersonaFisica){
		disableDichiarazioneBeneficiarioPF(suffissoBeneficiario);
	} else {
		disableDichiarazioneBeneficiarioPG(suffissoBeneficiario);
	}
		
	var isGiaCliente = pdf.getFieldValue("isGiaCliente" + suffissoBeneficiario);
	
	if (isGiaCliente === "N"){
		enableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario)
	} else {
		disableLegaleRapprProcuratoreBeneficiario(suffissoBeneficiario)
	}
}

function disableDichiarazioneBeneficiarioPF(suffisso) {
	// nel suffisso deve essere  compreso l'indice del beneficiario,
	if (document.getElementById("cognomeDichiarazione" + suffisso) !== null){
		pdf.disableField("cognomeDichiarazione" + suffisso);
	}
	if (document.getElementById("nomeDichiarazione" + suffisso) !== null){
		pdf.disableField("nomeDichiarazione" + suffisso);
	}
	if (document.getElementById("codiceFiscaleDichiarazione" + suffisso) !== null){
		pdf.disableField("codiceFiscaleDichiarazione" + suffisso);
	}
}

function clearDichiarazioneBeneficiarioPF(suffisso) {
	if (document.getElementById("cognomeDichiarazione" + suffisso) !== null){
		pdf.setFieldValue("cognomeDichiarazione" + suffisso, "");
	}
	if (document.getElementById("nomeDichiarazione" + suffisso) !== null){
		pdf.setFieldValue("nomeDichiarazione" + suffisso, "");
	}
	if (document.getElementById("codiceFiscaleDichiarazione" + suffisso) !== null){
		pdf.setFieldValue("codiceFiscaleDichiarazione" + suffisso, "");
	}
	clearLegaleRapprProcuratore(suffisso);
}

function clearLegaleRapprProcuratore(suffisso){
	if (document.getElementById("cognomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.setFieldValue("cognomeLegaleRapprProcuratore" + suffisso, "");
	}
	if (document.getElementById("nomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.setFieldValue("nomeLegaleRapprProcuratore" + suffisso, "");
	}
	if (document.getElementById("codiceFiscaleLegaleRapprProcuratore" + suffisso) !== null){
		pdf.setFieldValue("codiceFiscaleLegaleRapprProcuratore" + suffisso, "");
	}	
}

function disableDichiarazioneBeneficiarioPG(suffisso) {
	// nel suffisso deve essere  compreso l'indice del beneficiario,
	if (document.getElementById("ragioneSocialeDichiarazione" + suffisso) !== null){
		pdf.disableField("ragioneSocialeDichiarazione" + suffisso);
	}
	if (document.getElementById("codiceFiscalePartitaIvaDichiarazione" + suffisso) !== null){
		pdf.disableField("codiceFiscalePartitaIvaDichiarazione" + suffisso);
	}
}

function clearDichiarazioneBeneficiarioPG(suffisso) {
	if (document.getElementById("ragioneSocialeDichiarazione" + suffisso) !== null){
		pdf.setFieldValue("ragioneSocialeDichiarazione" + suffisso, "");
	}
	if (document.getElementById("codiceFiscalePartitaIvaDichiarazione" + suffisso) !== null){
		pdf.setFieldValue("codiceFiscalePartitaIvaDichiarazione" + suffisso, "");
	}
	clearLegaleRapprProcuratore(suffisso);
}

function disableLegaleRapprProcuratoreBeneficiario(suffisso) {
	// nel suffisso deve essere  compreso l'indice del beneficiario,
	if (document.getElementById("cognomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.disableField("cognomeLegaleRapprProcuratore" + suffisso);
	}
	if (document.getElementById("nomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.disableField("nomeLegaleRapprProcuratore" + suffisso);
	}
	if (document.getElementById("codiceFiscaleLegaleRapprProcuratore" + suffisso) !== null){
		pdf.disableField("codiceFiscaleLegaleRapprProcuratore" + suffisso);
	}
}

function enableLegaleRapprProcuratoreBeneficiario(suffisso) {
	// nel suffisso deve essere  compreso l'indice del beneficiario,
	if (document.getElementById("cognomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.enableField("cognomeLegaleRapprProcuratore" + suffisso);
	}
	if (document.getElementById("nomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.enableField("nomeLegaleRapprProcuratore" + suffisso);
	}
	if (document.getElementById("codiceFiscaleLegaleRapprProcuratore" + suffisso) !== null){
		pdf.enableField("codiceFiscaleLegaleRapprProcuratore" + suffisso);
	}
}

function clearLegaleRapprProcuratoreBeneficiario(suffisso) {
	if (document.getElementById("cognomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.setFieldValue("cognomeLegaleRapprProcuratore" + suffisso, "");
	}
	if (document.getElementById("nomeLegaleRapprProcuratore" + suffisso) !== null){
		pdf.setFieldValue("nomeLegaleRapprProcuratore" + suffisso, "");
	}
	if (document.getElementById("codiceFiscaleLegaleRapprProcuratore" + suffisso) !== null){
		pdf.setFieldValue("codiceFiscaleLegaleRapprProcuratore" + suffisso, "");
	}
}

function changeAnagraficaDichiarazioni(prefissoBeneficiario, indiceBeneficiario){ 

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	
	var cognomeDichiarazione = document.getElementById("cognomeDichiarazione"+ prefissoBeneficiario +"1");
	//Sul modulo non ci sono le dichiarazioni
	if (cognomeDichiarazione === null )
		return;

	var isPersonaFisica = pdf.getFieldValue("isPersonaFisica" + suffissoBeneficiario) === "true" ? true : false;
	
	if (isPersonaFisica){
		pdf.setFieldValue("cognomeDichiarazione"+ suffissoBeneficiario, pdf.getFieldValue("cognome" + suffissoBeneficiario));
		pdf.setFieldValue("nomeDichiarazione"+ suffissoBeneficiario, pdf.getFieldValue("nome" + suffissoBeneficiario));
		pdf.setFieldValue("codiceFiscaleDichiarazione"+ suffissoBeneficiario, pdf.getFieldValue("codiceFiscale" + suffissoBeneficiario));
	} else {
		pdf.setFieldValue("ragioneSocialeDichiarazione"+ suffissoBeneficiario, pdf.getFieldValue("ragioneSociale" + suffissoBeneficiario));
		pdf.setFieldValue("codiceFiscalePartitaIvaDichiarazione"+ suffissoBeneficiario, pdf.getFieldValue("codiceFiscalePartitaIva" + suffissoBeneficiario));
	}	
}

function clearDichiarazioniBeneficiari(prefissoBeneficiario) {
	for (var i = 1;; i++) {
		var suffissoBeneficiario = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null){
			break;			
		}

		var isPersonaFisica = pdf.getFieldValue("isPersonaFisica"+ suffissoBeneficiario) === "true" ? true : false;

		if (isPersonaFisica) {
			clearDichiarazioneBeneficiarioPF(suffissoBeneficiario);
		} else {
			clearDichiarazioneBeneficiarioPG(suffissoBeneficiario);
		}
	}
}
