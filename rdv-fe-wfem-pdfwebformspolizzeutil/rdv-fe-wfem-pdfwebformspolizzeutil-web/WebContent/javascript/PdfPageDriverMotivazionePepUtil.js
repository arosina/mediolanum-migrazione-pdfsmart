function manageMotivazionePep(prefissoBeneficiario) {

	/*
	 * La sezione pep contiene lo stesso numero di posizioni (e stessa logica PF/PG) della sezione beneficiari; 
	 * per facilitare la scrittura del codice utilizziamo
	 * i campi della sezione beneficiario per scorrere i campi della pep
	 */
	for (var i = 1;; i++) {
		
		var suffissoBeneficiario = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null){
			break;			
		}

		manageMotivazionePepBeneficiarioPersonaFisicaGiuridica(prefissoBeneficiario,
				i);
	}
	
	/*
	 * Se esiste il flag devo ciclare sulle motivazioni, verificare se ho un beneficiario associato 
	 * altrimenti devo disabilitare
	 */
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);
	var isPep = document.getElementById("isPep"+ prefissoBeneficiario +"1");
	if (isPep === null || tipoBeneficiario !== "031")
		return;
	
	for (var indMotivazione = 1;; indMotivazione++){
		var suffissoMotivazione = prefissoBeneficiario + indMotivazione;
		if (document.getElementById("motivazionePep" + suffissoMotivazione) === null)
			break;
		
		var trovato = false;
		for (var indBeneficiario = 1;; indBeneficiario++) {
			
			var suffissoBeneficiario = prefissoBeneficiario + indBeneficiario;
			if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null)
				break;			
			
			var indiceMotivazionePep = pdf.getFieldValue("indiceMotivazionePep"+ suffissoBeneficiario);
			if (parseInt(indiceMotivazionePep) === indMotivazione)
				trovato = true;
		}
		if (!trovato)
			disableMotivazionePepBeneficiario(prefissoBeneficiario, indMotivazione, false, null);

	}
}

function manageMotivazionePepBeneficiarioPersonaFisicaGiuridica(prefissoBeneficiario,
		indiceBeneficiario) {
	

	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	
	var isPersonaFisica = pdf.getFieldValue("isPersonaFisica"
			+ suffissoBeneficiario) === "true" ? true : false;
	
	
	if (isPersonaFisica){

		manageMotivazionePepBeneficiario(prefissoBeneficiario,
				indiceBeneficiario);
	} else {

		// tratto i titolari, per le persone giuridiche la pep è relativa ai
		// titolari effettivi
		for (var j = 1;; j++) {
			var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
					+ indiceBeneficiario;
			if (document.getElementById("isGiaCliente" + suffissoTitolare) === null){
				break;					
			}

			manageMotivazionePepBeneficiarioTitolare(prefissoBeneficiario,
					indiceBeneficiario, j);
		}
	}

	
}

function manageMotivazionePepBeneficiarioTitolare(prefissoBeneficiario,	indiceBeneficiario, indiceTitolare) {
	
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);
	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;
	var isGiaCliente = pdf.getFieldValue("isGiaCliente" + suffissoTitolare);

	if (tipoBeneficiario === "031") {
		var isPep = document.getElementById("isPep"+ suffissoTitolare) === null?null:pdf.getFieldValue("isPep"+ suffissoTitolare);

		if ((isGiaCliente === "N" || isGiaCliente === "S") && (isPep === null || isPep ==="S")) {
			enableMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario, true, indiceTitolare, false, true);
		} else {// non valorizzato oppure isPep = "S"
			disableMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario, true, indiceTitolare);
		}
	} else if (tipoBeneficiario === null || tipoBeneficiario === "") {
		disableMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario, true, indiceTitolare);
	
		// Le motivazioni pep per beneficiari generici non possono essere di persone giuridiche
	//} else {
	//	enableMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario, true, indiceTitolare, false, true);
	}
}

function manageMotivazionePepBeneficiario(prefissoBeneficiario,
		indiceBeneficiario) {
	
	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	var isGiaCliente = pdf.getFieldValue("isGiaCliente" + suffissoBeneficiario);
	var tipoBeneficiario = pdf.getFieldValue("tipo" + prefissoBeneficiario);

	if (tipoBeneficiario === "031") {
		var gestioneIsPep = document.getElementById("isPep" + suffissoBeneficiario) !== null;
		if (gestioneIsPep){
			//Se ho il campo indiceMotivazionePep non abilito disabilito in base all'indice del beneficiario
			//ma in base all'indice motivazione pep memoriazzata solo se isPep = "S"
			var indMotivazione = pdf.getFieldValue("indiceMotivazionePep" + suffissoBeneficiario);
			if (indMotivazione !== ""){
				if (pdf.getFieldValue("isPep" + suffissoBeneficiario) === "S") {
					enableMotivazionePepBeneficiario(prefissoBeneficiario, indMotivazione, false, null, false, null);
				} else {// non valorizzato
					disableMotivazionePepBeneficiario(prefissoBeneficiario, indMotivazione, false, null);
				}
			}
		} else {
			if (isGiaCliente === "N" || isGiaCliente === "S") {
				enableMotivazionePepBeneficiario(prefissoBeneficiario,
						indiceBeneficiario, false, null, false, null);
			} else {// non valorizzato
				disableMotivazionePepBeneficiario(prefissoBeneficiario,
						indiceBeneficiario, false, null);
			}
		}
	} else if (tipoBeneficiario === null || tipoBeneficiario === "") {
		disableMotivazionePepBeneficiario(prefissoBeneficiario,
				indiceBeneficiario, false, null);
	} else {
		/*
		 * Parametri di input : prefissoBeneficiario,indiceBeneficiario, 
		 * titolare, indiceTitolare, enableDatiAnagrafici,	enableMotivazionePepTitolare
		 */
		enableMotivazionePepBeneficiario(prefissoBeneficiario,
				indiceBeneficiario, false, null, true, false);
	}
}

function enableMotivazionePepBeneficiario(prefissoBeneficiario,
		indiceBeneficiario, titolare, indiceTitolare, enableDatiAnagrafici,
		enableMotivazionePepTitolare) {
	if (titolare) {
		var suffissoTitolare = "Titolare" + indiceTitolare
				+ prefissoBeneficiario + indiceBeneficiario;
		if (enableMotivazionePepTitolare) {
			pdf.enableField("motivazionePep" + suffissoTitolare);
		} else {
			pdf.disableField("motivazionePep" + suffissoTitolare);
			if (document.getElementById("cognomePep" + suffissoTitolare) !== null) {
				pdf.disableField("cognomePep" + suffissoTitolare);
				pdf.disableField("nomePep" + suffissoTitolare);
			}
			if (document.getElementById("dataNascitaPep" + suffissoTitolare) !== null) {
				pdf.disableField("dataNascitaPep" + suffissoTitolare);
				pdf.disableField("comuneNascitaPep" + suffissoTitolare);
			}
		}
	} else {
		var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		pdf.enableField("motivazionePep" + suffissoBeneficiario);
		/*
		 * per il caso vita, esiste soltanto la tipologia beneficiario 031 e
		 * quindi soltanto il campo motivazione pep
		 */
		if (document.getElementById("cognomePep" + suffissoBeneficiario) !== null) {
			if (enableDatiAnagrafici) {
				pdf.enableField("cognomePep" + suffissoBeneficiario);
				pdf.enableField("nomePep" + suffissoBeneficiario);
				pdf.enableField("dataNascitaPep" + suffissoBeneficiario);
				pdf.enableField("comuneNascitaPep" + suffissoBeneficiario);
			} else {
				pdf.disableField("cognomePep" + suffissoBeneficiario);
				pdf.disableField("nomePep" + suffissoBeneficiario);
				pdf.disableField("dataNascitaPep" + suffissoBeneficiario);
				pdf.disableField("comuneNascitaPep" + suffissoBeneficiario);
			}
		}

	}
}

function disableMotivazionePepBeneficiario(prefissoBeneficiario,
		indiceBeneficiario, titolare, indiceTitolare) {
	if (titolare) {
		var suffissoTitolare = "Titolare" + indiceTitolare
				+ prefissoBeneficiario + indiceBeneficiario;
		pdf.disableField("motivazionePep" + suffissoTitolare);
		if (document.getElementById("cognomePep" + suffissoTitolare) !== null) {
			pdf.disableField("cognomePep" + suffissoTitolare);
			pdf.disableField("nomePep" + suffissoTitolare);
		}
		if (document.getElementById("dataNascitaPep" + suffissoTitolare) !== null) {
			pdf.disableField("dataNascitaPep" + suffissoTitolare);
			pdf.disableField("comuneNascitaPep" + suffissoTitolare);
		}
	} else {
		var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
		pdf.disableField("motivazionePep" + suffissoBeneficiario);

		if (document.getElementById("cognomePep" + suffissoBeneficiario) !== null) {
			pdf.disableField("cognomePep" + suffissoBeneficiario);
			pdf.disableField("nomePep" + suffissoBeneficiario);
			pdf.disableField("dataNascitaPep" + suffissoBeneficiario);
			pdf.disableField("comuneNascitaPep" + suffissoBeneficiario);
		}
	}
}

function clearMotivazionePepBeneficiario(prefissoBeneficiario,
		indiceBeneficiario, titolare, indiceTitolare) {
	if (titolare) {
		var suffissoTitolare = "Titolare" + indiceTitolare
				+ prefissoBeneficiario + indiceBeneficiario;
		clearField("motivazionePep" + suffissoTitolare);
		if (document.getElementById("cognomePep" + suffissoTitolare) !== null) {
			clearField("cognomePep" + suffissoTitolare);
			clearField("nomePep" + suffissoTitolare);
		}
		if (document.getElementById("dataNascitaPep" + suffissoTitolare) !== null) {
			clearField("dataNascitaPep" + suffissoTitolare);
			clearField("comuneNascitaPep" + suffissoTitolare);
		}
	} else {
		var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
		clearField("motivazionePep" + suffissoBeneficiario);

		if (document.getElementById("cognomePep" + suffissoBeneficiario) !== null) {
			clearField("cognomePep" + suffissoBeneficiario);
			clearField("nomePep" + suffissoBeneficiario);
			clearField("dataNascitaPep" + suffissoBeneficiario);
			clearField("comuneNascitaPep" + suffissoBeneficiario);
		}
	}
}
function clearMotivazionePep(prefissoBeneficiario) {
	for (var i = 1;; i++) {
		var suffissoBeneficiario = prefissoBeneficiario + i;
		if (document.getElementById("isPersonaFisica" + suffissoBeneficiario) === null){
			break;			
		}

		var isPersonaFisica = pdf.getFieldValue("isPersonaFisica"
				+ suffissoBeneficiario) === "true" ? true : false;

		if (isPersonaFisica) {
			clearMotivazionePepBeneficiario(prefissoBeneficiario, i, false,
					null);
		} else {
			// tratto i titolari
			for (var j = 1;; j++) {
				var suffissoTitolare = "Titolare" + j + prefissoBeneficiario
						+ i;
				if (document.getElementById("isGiaCliente" + suffissoTitolare) === null){
					break;					
				}

				clearMotivazionePepBeneficiario(prefissoBeneficiario, i, true,
						j);

			}
		}
	}
}
//************************************************//
function manageIsPep (prefissoBeneficiario, indiceBeneficiario){
	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	
	var isPep = pdf.getFieldValue("isPep" + suffissoBeneficiario);
	
	if (isPep === "S") {
		//Cerco indice movitazione pep vuoto e lo memorizzo tra i dati del beneficiario
		var indMotivazione = cercaIndiceMotivazionePepVuoto(prefissoBeneficiario);
		pdf.setFieldValue("indiceMotivazionePep"+ suffissoBeneficiario, indMotivazione);
		
		changeAnagraficaPep(prefissoBeneficiario, indiceBeneficiario);
		manageMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario);
		
	}else{
		//cerco indice motivazione pep del beneficiario
		manageMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario);
		clearMotivazionePepDaFlag(prefissoBeneficiario, indiceBeneficiario);
	}	
}

function cercaIndiceMotivazionePepVuoto(prefissoBeneficiario){
	for (var ind = 1;; ind++) {
		var suffissoBeneficiario = prefissoBeneficiario + ind;
		var cognomePep = pdf.getFieldValue("cognomePep" + suffissoBeneficiario);
		var nomePep = pdf.getFieldValue("nomePep" + suffissoBeneficiario);
		var dataNascitaPep = pdf.getFieldValue("dataNascitaPep" + suffissoBeneficiario);
		var comuneNascitaPep = pdf.getFieldValue("comuneNascitaPep" + suffissoBeneficiario);

		if (cognomePep === null)
			break;
		
		if (cognomePep === "" && nomePep === "" && comuneNascitaPep === "")
			return ind;
	}
	
	return -1;
}

//************************************************//
//Esiste 1 solo beneficiario giuridico morte e / o 1 solo beneficiario giuridico vita
//Quindi la motivazione pep corrisponde all'indice del beneficiario
//************************************************//
function manageIsPepTitolare(prefissoBeneficiario, indiceBeneficiario, indiceTitolare){

	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario; 
	var isPep = pdf.getFieldValue("isPep" + suffissoTitolare);
	
	if (isPep === "S") {
		//Imposto cognome / nome del titolare del beneficiario
		pdf.setFieldValue("cognomePep"+ suffissoTitolare, pdf.getFieldValue("cognome" + suffissoTitolare));
		pdf.setFieldValue("nomePep"+ suffissoTitolare, pdf.getFieldValue("nome" + suffissoTitolare));
	}else{
		//pulisco i campi motivazione
		clearField("motivazionePep" + suffissoTitolare);
		clearField("cognomePep" + suffissoTitolare);
		clearField("nomePep" + suffissoTitolare);
	}
	
	manageMotivazionePepBeneficiarioTitolare(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
}
//************************************************//
function changeAnagraficaPep (prefissoBeneficiario, indiceBeneficiario){
	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	
	var isPep = pdf.getFieldValue("isPep" + suffissoBeneficiario);
	if (document.getElementById("isPep" + suffissoBeneficiario) === null || isPep != "S")
		return;
	
	var indMotivazione = pdf.getFieldValue("indiceMotivazionePep" + suffissoBeneficiario);
	var suffissoPep = prefissoBeneficiario + indMotivazione;
	
	pdf.setFieldValue("cognomePep"+ suffissoPep, pdf.getFieldValue("cognome" + suffissoBeneficiario));
	pdf.setFieldValue("nomePep"+ suffissoPep, pdf.getFieldValue("nome" + suffissoBeneficiario));
	if (document.getElementById("dataNascitaPep" + suffissoPep) !== null ){
		if (document.getElementById("dataNascita" + suffissoBeneficiario).value !== "")
			pdf.setFieldValue("dataNascitaPep"+ suffissoPep, pdf.getFieldValue("dataNascita" + suffissoBeneficiario));
		else
			clearField("dataNascitaPep" + suffissoPep);		
	}
	if (document.getElementById("comuneNascitaPep" + suffissoPep) !== null){
		pdf.setFieldValue("comuneNascitaPep"+ suffissoPep, pdf.getFieldValue("comuneNascita" + suffissoBeneficiario));
	}	
}

//************************************************//
function changeAnagraficaTitolarePep (prefissoBeneficiario, indiceBeneficiario, indiceTitolare){
	var suffissoTitolare = "Titolare" + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;
	
	var isPep = pdf.getFieldValue("isPep" + suffissoTitolare);
	if (document.getElementById("isPep" + suffissoTitolare) === null || isPep != "S")
		return;
	
	pdf.setFieldValue("cognomePep"+ suffissoTitolare, pdf.getFieldValue("cognome" + suffissoTitolare));
	pdf.setFieldValue("nomePep"+ suffissoTitolare, pdf.getFieldValue("nome" + suffissoTitolare));
}

//************************************************//
function clearMotivazionePepDaFlag(prefissoBeneficiario, indiceBeneficiario){
	var suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
	if (document.getElementById("isPep" + suffissoBeneficiario) === null)
		return;
	var indiceMotivazione = pdf.getFieldValue("indiceMotivazionePep" + suffissoBeneficiario)
	clearMotivazionePepBeneficiario(prefissoBeneficiario, indiceMotivazione, false, null);	
	manageMotivazionePepBeneficiario(prefissoBeneficiario, indiceBeneficiario);
	clearField("indiceMotivazionePep"+ suffissoBeneficiario);
}