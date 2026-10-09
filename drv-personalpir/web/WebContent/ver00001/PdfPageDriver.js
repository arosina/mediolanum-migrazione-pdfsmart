/***********************************************************************************************/
// @Override
PdfPageDriver.prototype.onLoad = function(){

	// Le autocompletion predefiniti su clienti e conti devono utilizzare solo gli elementi selezionati e non scritti a mano.
	pdf.forceAllSelection();
	pdf.forceLuogoSelection("luogo");

	manageDatiContraente();

	if (pdf.isPdfInCarrello()){
		this.disabilitaCampiCarrello();
	}
}

PdfPageDriver.prototype.fieldsFromCarrello = [
	"numeroPolizza","isVariazioneDataPremioSDD","isVariazioneFrequenzaSDD","isVariazioneImportoSDD","isVariazioneContoSDD","isVariazioneDisposizioneSDD","isRivalutazionePremio",
	"tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "tipoSOSPENSIONEREVOCARIATTIVAZIONESDD",
	"isVariazioneRipartizione",	"isVariazioneImportoRiattivazioneSDD","isVariazioneFrazionamentoRiattivazioneSDD","isVariazioneDataPremioRiattivazioneSDD","importoVariazioneSDD","frequenzaVariazioneSDD","giornoValutaVariazioneSDD", "revocaVersamentoPremioAggiuntivo"];

PdfPageDriver.prototype.tabellaRipartizione = ["FondoPremio"];

PdfPageDriver.prototype.campiTabellaRipartizione = ["isin","descrizione","percentuale","importo"];

//gestisce i campi da abilitare/disabilitare in base alla scelta del numero polizza
PdfPageDriver.prototype.disabilitaCampiCarrello = function() {
	var obj;
	obj = {items: [ {    fields: this.fieldsFromCarrello
						, enable: false, clear: false}
					,{prefissiFields: this.campiTabellaRipartizione,
						suffissiFields: this.tabellaRipartizione,
						enable: false, clear: false}
					]};

	pdfPageDriver.manageFields(obj);

	var isVariazioneDataPremioSDD = pdf.getFieldValue("isVariazioneDataPremioSDD");
	if (typeof isVariazioneDataPremioSDD === "undefined") {
		return;
	} else {
		if (isVariazioneDataPremioSDD) {
			pdf.enableField("giornoValutaVariazioneSDD");
		}
	}

	var tipoSOSPENSIONEREVOCARIATTIVAZIONESDD = pdf.getFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
	if (typeof tipoSOSPENSIONEREVOCARIATTIVAZIONESDD === "undefined") {
		return;
	} else {
		if (tipoSOSPENSIONEREVOCARIATTIVAZIONESDD) {
			pdf.disableField(tipoSOSPENSIONEREVOCARIATTIVAZIONESDD);
			if (tipoSOSPENSIONEREVOCARIATTIVAZIONESDD==="RIATTIVAZIONE"){
				pdf.enableField("isVariazioneImportoRiattivazioneSDD");
				pdf.enableField("isVariazioneFrazionamentoRiattivazioneSDD");
				pdf.enableField("isVariazioneDataPremioRiattivazioneSDD");
			}

			var isVariazioneImportoRiattivazioneSDD = pdf.getFieldValue("isVariazioneImportoRiattivazioneSDD");
			if (isVariazioneImportoRiattivazioneSDD){
				pdf.disableField("isVariazioneImportoRiattivazioneSDD");
				pdf.enableField("importoRiattivazioneSDD");
				var importoRiattivazioneSDD = pdf.getFieldValue("importoRiattivazioneSDD");

				if (importoRiattivazioneSDD){
					pdf.disableField("importoRiattivazioneSDD");
				}
			}

			var isVariazioneFrazionamentoRiattivazioneSDD = pdf.getFieldValue("isVariazioneFrazionamentoRiattivazioneSDD");
			if (isVariazioneFrazionamentoRiattivazioneSDD){
				pdf.disableField("isVariazioneFrazionamentoRiattivazioneSDD");
				pdf.enableField("frequenzaRiattivazioneSDD");
				var frequenzaRiattivazioneSDD = pdf.getFieldValue("frequenzaRiattivazioneSDD");

				if (frequenzaRiattivazioneSDD){
					pdf.disableField("frequenzaRiattivazioneSDD");
				}
			}

			var isVariazioneDataPremioRiattivazioneSDD = pdf.getFieldValue("isVariazioneDataPremioRiattivazioneSDD");
			if (isVariazioneDataPremioRiattivazioneSDD){
				pdf.disableField("isVariazioneDataPremioRiattivazioneSDD");
				pdf.enableField("giornoValutaRiattivazioneSDD");
				var giornoValutaRiattivazioneSDD = pdf.getFieldValue("giornoValutaRiattivazioneSDD");

				if (giornoValutaRiattivazioneSDD){
					pdf.disableField("giornoValutaRiattivazioneSDD");
				}
			}

			var isVariazioneContoSDD = pdf.getFieldValue("isVariazioneContoSDD");
			if (typeof isVariazioneContoSDD === "undefined") {
				return;
			}
		}
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
//@Override
PdfPageDriver.prototype.onChangePerson = function(fieldName, personIdx){
	if (personIdx === 1){
		clearDatiContraente();
	}
	return true;
}

/***********************************************************************************************/
/***********************************************************************************************/
//@Override
PdfPageDriver.prototype.onChange = function(fieldName){

	if(fieldName === "dataSottoscrizione"){
		var eventObj = {eventName:"onChangeDataSottoscrizione", fields:"dataSottoscrizione", targets: "dataSottoscrizione"};
		pdf.callEvent(eventObj);

	} else if(fieldName === "ndgCliente1"
			|| fieldName === "codiceFiscalePartitaIvaCliente1"
			|| fieldName === "cognomeCliente1"
			|| fieldName === "nomeCliente1"){
		var eventObj = {eventName:"onChangeContraente", fields:["ndgCliente1","codiceFiscalePartitaIvaCliente1","cognomeCliente1","nomeCliente1"], targets: "ndgCliente1,codiceFiscalePartitaIvaCliente1,cognomeCliente1,nomeCliente1"};
		pdf.callEvent(eventObj);

	} else if (fieldName === "isVariazioneDisposizioneSDD"){
		manageVariazioneDisposizioneSDD();

	} else if (fieldName === "isVariazioneContoSDD"){
		manageIsVariazioneContoSDD();
		manageSospensioneSDD();
	} else if (fieldName === "isRivalutazionePremio"){
		manageRivalutazionePremio();

	} else if (fieldName === "isVariazioneRipartizione"){
		manageVariazioneRipartizione(false);

	} else if (fieldName === "isVariazioneImportoSDD"){
		manageVariazioneImportoSDD();

	} else if (fieldName === "isVariazioneFrequenzaSDD"){
		manageVariazioneFrequenzaSDD();

	} else if (fieldName === "isVariazioneDataPremioSDD"){
		manageVariazioneDataPremioSDD();

	} else if (fieldName === "tipoIntestazioneContoSDDBMEDVariazione"){
		manageTipoIntestazioneConto();
		
	} else if (fieldName === "tipoContoALTROCLIENTESDDBMEDVariazione"){
		manageTipoContoAltroCliente();

	} else if (fieldName === "tipoContoINTESTATARIOSDDBMEDVariazione"){
		manageTipoContoIntestatario();

	} else if (fieldName === "tipoSOSPENSIONEREVOCARIATTIVAZIONESDD"){
		manageSospensioneSDD();
		manageIsVariazioneContoSDD();
	} else if (fieldName === "isVariazioneImportoRiattivazioneSDD"){
		manageVariazioneImportoRiattivazioneSDD();

	} else if (fieldName === "isVariazioneFrazionamentoRiattivazioneSDD"){
		manageVariazioneFrazionamentoRiattivazioneSDD();

	} else if (fieldName === "isVariazioneDataPremioRiattivazioneSDD"){
		manageVariazioneDataPremioRiattivazioneSDD();

	} else if(fieldName.indexOf("percentualeFondoPremio")== 0){
		var idx = fieldName.substring("percentualeFondoPremio".length,fieldName.length);
		var eventObj = {eventName:"onChangePercentualeFondoPremio", eventArgs:idx, targets: "totaleImporto,totalePercentuale,importoFondoPremio"+idx+",percentualeFondoPremio"+idx};
		pdf.callEvent(eventObj);

	} else if (fieldName.indexOf("importoFondoPremio")== 0){
		var idx = fieldName.substring("importoFondoPremio".length,fieldName.length);
		var eventObj = {eventName:"onChangeImportoFondoPremio", eventArgs:idx, targets: "totaleImporto,totalePercentuale,importoFondoPremio"+idx+",percentualeFondoPremio"+idx};
		pdf.callEvent(eventObj);

	} else if (fieldName == "importoVariazioneSDD"){
		clearImportoPercentualeGrigliaPremio();
		var eventObj = {eventName:"onChangeImportoVariazioneSDD", targets:fieldName};
		pdf.callEvent(eventObj);

	} else if(fieldName === "tipoRelazioneContraenteTerzoPagatore"){
		terzoPagatore.manageTipoRelazione();
		//	RFC#198937 - Equivalenti Freeze
	} else if (fieldName.indexOf("isinFondoPremio") >= 0){
		var idx = fieldName.substring("isinFondoPremio".length,fieldName.length);
		var eventObj = {eventName:"onChangeFondoPremio", eventArgs:idx, fields:[fieldName,"descrizioneFondoPremio" +idx], targets: fieldName};
		pdf.callEvent(eventObj);
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function clearDatiContraente(){
	clearField("numeroPolizza");
	manageDatiContraente();
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageDatiContraente(){

	var cliente = pdf.getFieldValue("ndgCliente1");
	if (cliente===""){
		pdf.setFieldValue("numeroPolizza", "");
		pdf.disableField("numeroPolizza");
	}else{
		pdf.enableField("numeroPolizza");
	}
	managePolizza();
}

/***********************************************************************************************/
/***********************************************************************************************/
function managePolizza(){

	// rfc #257299 - Se esiste la forma contrattuale è il nuovo modulo con la sezione 2 e la gestione PIC/PAC
	if(pdf.fieldExist("formaContrattuale"))
		managePolizzaPicPac();
	else // E' il veccho modulo con solo PAC
		managePolizzaPac();
	
	manageVariazioneDisposizioneSDD();
	manageSospensioneSDD();
	manageIsVariazioneContoSDD();
	manageRivalutazionePremio();
	manageVariazioneRipartizione(false);
}
/***********************************************************************************************/
/***********************************************************************************************/
function managePolizzaPicPac(){
	var formaContrattuale = pdf.getFieldValue("formaContrattuale");
	if(formaContrattuale == "PAC"){
		pdf.enableField("isVariazioneDisposizioneSDD");
		pdf.enableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
		pdf.enableField("isRivalutazionePremio");
		pdf.enableField("isVariazioneRipartizione");
		pdf.setFieldValue("revocaVersamentoPremioAggiuntivo", "");
		pdf.disableField("revocaVersamentoPremioAggiuntivo");
	}else if(formaContrattuale == "PIC"){
		pdf.setFieldValue("isVariazioneDisposizioneSDD", "");
		pdf.disableField("isVariazioneDisposizioneSDD");
		pdf.setFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "");
		pdf.disableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
		pdf.setFieldValue("isRivalutazionePremio", "");
		pdf.disableField("isRivalutazionePremio");
		pdf.setFieldValue("isVariazioneRipartizione", "");
		pdf.disableField("isVariazioneRipartizione");
		pdf.enableField("revocaVersamentoPremioAggiuntivo");		
	}else{
		pdf.setFieldValue("isVariazioneDisposizioneSDD", "");
		pdf.disableField("isVariazioneDisposizioneSDD");
		pdf.setFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "");
		pdf.disableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
		pdf.setFieldValue("isRivalutazionePremio", "");
		pdf.disableField("isRivalutazionePremio");
		pdf.setFieldValue("isVariazioneRipartizione", "");
		pdf.disableField("isVariazioneRipartizione");
		pdf.setFieldValue("revocaVersamentoPremioAggiuntivo", "");
		pdf.disableField("revocaVersamentoPremioAggiuntivo");
	}
}
/***********************************************************************************************/
/***********************************************************************************************/
function managePolizzaPac(){
	var polizza = pdf.getFieldValue("numeroPolizza");
	if(polizza == ""){
		pdf.setFieldValue("isVariazioneDisposizioneSDD", "");
		pdf.disableField("isVariazioneDisposizioneSDD");
		pdf.setFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "");
		pdf.disableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
		pdf.setFieldValue("isRivalutazionePremio", "");
		pdf.disableField("isRivalutazionePremio");
		pdf.setFieldValue("isVariazioneRipartizione", "");
		pdf.disableField("isVariazioneRipartizione");
	}else{
		pdf.enableField("isVariazioneDisposizioneSDD");
		pdf.enableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
		pdf.enableField("isRivalutazionePremio");
		pdf.enableField("isVariazioneRipartizione");	
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageSospensioneSDD(){
	// rfc #257299 - Se esiste la forma contrattuale è il nuovo modulo con la sezione 2, quindi solo PAC
	if(pdf.fieldExist("formaContrattuale") && pdf.getFieldValue("formaContrattuale") != "PAC"){
		pdf.setFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "");
		pdf.disableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
	}else{
		var polizza = pdf.getFieldValue("numeroPolizza");
		if(polizza == ""){
			pdf.setFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "");
			pdf.disableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
		}else{
			var isVariazioneContoSDD = pdf.getFieldValue("isVariazioneContoSDD");
			if(isVariazioneContoSDD !== "SI"){
				pdf.enableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
			}else{
				pdf.setFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD", "");
				pdf.disableField("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD#SOSPENSIONE");
			}
		}
	}
	manageTipoSospensioneRevocaRiattivazioneSDD();
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageIsVariazioneContoSDD(){
	// rfc #257299 - Se esiste la forma contrattuale è il nuovo modulo con la sezione 2, quindi solo PAC
	if(pdf.fieldExist("formaContrattuale") && pdf.getFieldValue("formaContrattuale") != "PAC"){
		pdf.setFieldValue("isVariazioneContoSDD", "");
		pdf.disableField("isVariazioneContoSDD");
	}else{
		var polizza = pdf.getFieldValue("numeroPolizza");
		var operazione = pdf.getFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
		if (polizza !== "" && operazione !== "SOSPENSIONE"){
			pdf.enableField("isVariazioneContoSDD");
		}else {
			pdf.setFieldValue("isVariazioneContoSDD", "");
			pdf.disableField("isVariazioneContoSDD");
		}
	}
	manageVariazioneContoSDD();
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneDisposizioneSDD(){

	var variazione = pdf.getFieldValue("isVariazioneDisposizioneSDD");
	if (variazione==="SI"){
		pdf.enableField("isVariazioneImportoSDD");
		pdf.enableField("isVariazioneFrequenzaSDD");
		pdf.enableField("isVariazioneDataPremioSDD");
	}else{
		pdf.setFieldValue("isVariazioneImportoSDD", "");
		pdf.disableField("isVariazioneImportoSDD");
		pdf.setFieldValue("isVariazioneFrequenzaSDD", "");
		pdf.disableField("isVariazioneFrequenzaSDD");
		pdf.setFieldValue("isVariazioneDataPremioSDD", "");
		pdf.disableField("isVariazioneDataPremioSDD");
	}
	manageVariazioneImportoSDD();
	manageVariazioneFrequenzaSDD();
	manageVariazioneDataPremioSDD();
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneContoSDD(){

	var variazione = pdf.getFieldValue("isVariazioneContoSDD");
	if (variazione==="SI"){
		//pdf.enableField("tipoVariazioneContoSDD");
		pdf.enableField("tipoIntestazioneContoSDDBMEDVariazione")
		
	}else{
		//pdf.setFieldValue("tipoVariazioneContoSDD", "");
		//pdf.disableField("tipoVariazioneContoSDD");
		pdf.setFieldValue("tipoIntestazioneContoSDDBMEDVariazione", "")
		pdf.disableField("tipoIntestazioneContoSDDBMEDVariazione")
	}
	//manageTipoVariazioneContoSDD();
	manageTipoIntestazioneConto();
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageRivalutazionePremio(){

	var variazione = pdf.getFieldValue("isRivalutazionePremio");
	if (variazione==="SI"){
		pdf.enableField("tipoRIVALUTAZIONEPREMIO");
	}else{
		pdf.setFieldValue("tipoRIVALUTAZIONEPREMIO", "");
		pdf.disableField("tipoRIVALUTAZIONEPREMIO");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneRipartizione(clearFondi){

	var variazione = pdf.getFieldValue("isVariazioneRipartizione");
	if (variazione==="SI"){
		for (var i=0; ; i++){
			if(document.getElementById("isinFondoPremio"+i) == null)
				break;
			if (clearFondi)
				clearFondoRipartizionePremio(i);
			if (pdf.isFieldEnabled("isinFondoPremio"+i))
				continue;
			pdf.enableField("isinFondoPremio"+i);
			pdf.enableField("descrizioneFondoPremio"+i);
			pdf.enableField("percentualeFondoPremio"+i);
			pdf.enableField("importoFondoPremio"+i);
		}
		try {
			document.getElementById("apriPopupRicercaFondiAction").style.display = "";
		} catch (e) { }
	}else{
		for (var i=0; ; i++){
			if(document.getElementById("isinFondoPremio"+i) == null)
				break;
			clearFondoRipartizionePremio(i);
			if (!pdf.isFieldEnabled("isinFondoPremio"+i))
				continue;
			pdf.disableField("isinFondoPremio"+i);
			pdf.disableField("descrizioneFondoPremio"+i);
			pdf.disableField("percentualeFondoPremio"+i);
			pdf.disableField("importoFondoPremio"+i);
		}
		try {
			document.getElementById("apriPopupRicercaFondiAction").style.display = "none";
		} catch (e) { }
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneImportoSDD(){

	var variazione = pdf.getFieldValue("isVariazioneImportoSDD");
	if (variazione==="SI"){
		pdf.enableField("importoVariazioneSDD");
	}else{
		pdf.setFieldValue("importoVariazioneSDD", "");
		pdf.disableField("importoVariazioneSDD");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneFrequenzaSDD(){

	var variazione = pdf.getFieldValue("isVariazioneFrequenzaSDD");
	if (variazione==="SI"){
		pdf.enableField("frequenzaVariazioneSDD");
	}else{
		pdf.setFieldValue("frequenzaVariazioneSDD", "");
		pdf.disableField("frequenzaVariazioneSDD");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneDataPremioSDD(){

	var variazione = pdf.getFieldValue("isVariazioneDataPremioSDD");
	if (variazione==="SI"){
		pdf.enableField("giornoValutaVariazioneSDD");
	}else{
		pdf.setFieldValue("giornoValutaVariazioneSDD", "");
		pdf.disableField("giornoValutaVariazioneSDD");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
/*function manageTipoVariazioneContoSDD(){

	var variazione = pdf.getFieldValue("tipoVariazioneContoSDD");
	if (variazione=="SDDBMED"){
		pdf.enableField("tipoIntestazioneContoSDDBMEDVariazione");
	}else{
		pdf.setFieldValue("tipoIntestazioneContoSDDBMEDVariazione", "");
		pdf.disableField("tipoIntestazioneContoSDDBMEDVariazione");
	}
	manageTipoIntestazioneConto();
}*/

/***********************************************************************************************/
/***********************************************************************************************/
function manageTipoIntestazioneConto(){

	var intestazione = pdf.getFieldValue("tipoIntestazioneContoSDDBMEDVariazione");
	if (intestazione==="INTESTATARIO"){
		pdf.enableField("tipoContoINTESTATARIOSDDBMEDVariazione");
	}else{
		pdf.setFieldValue("tipoContoINTESTATARIOSDDBMEDVariazione", "");
		pdf.disableField("tipoContoINTESTATARIOSDDBMEDVariazione");
	}
	if (intestazione==="ALTROCLIENTE"){
		pdf.enableField("ndgALTROCLIENTESDDBMEDVariazione");
		pdf.enableField("cognomeNomeALTROCLIENTESDDBMEDVariazione");
		//pdf.enableField("ibanContoCorrenteALTROCLIENTESDDBMEDVariazione");
		pdf.enableField("tipoContoALTROCLIENTESDDBMEDVariazione");
	}else{
		pdf.setFieldValue("ndgALTROCLIENTESDDBMEDVariazione", "");
		pdf.disableField("ndgALTROCLIENTESDDBMEDVariazione");
		pdf.setFieldValue("cognomeNomeALTROCLIENTESDDBMEDVariazione", "");
		pdf.disableField("cognomeNomeALTROCLIENTESDDBMEDVariazione");
		//pdf.setFieldValue("ibanContoCorrenteALTROCLIENTESDDBMEDVariazione", "");
		//pdf.disableField("ibanContoCorrenteALTROCLIENTESDDBMEDVariazione");
		pdf.setFieldValue("tipoContoALTROCLIENTESDDBMEDVariazione", "");
		pdf.disableField("tipoContoALTROCLIENTESDDBMEDVariazione");
	}
	manageTipoContoAltroCliente();
	terzoPagatore.manage("tipoIntestazioneContoSDDBMEDVariazione", "ALTROCLIENTE", "ContraenteTerzoPagatore");
	manageTipoContoIntestatario();
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageTipoContoIntestatario(){

	var conto = pdf.getFieldValue("tipoContoINTESTATARIOSDDBMEDVariazione");
	if (conto==="CC"){
		pdf.enableField("ibanContoCorrenteCCINTESTATARIOSDDBMEDVariazione");
	}else{
		pdf.setFieldValue("ibanContoCorrenteCCINTESTATARIOSDDBMEDVariazione", "");
		pdf.disableField("ibanContoCorrenteCCINTESTATARIOSDDBMEDVariazione");
	}
	if (conto==="CCINAPERTURA"){
		if (pdf.isInInserimentoMOM()==false) {
			pdf.enableField("numeroPropostaCCINAPERTURAINTESTATARIOSDDBMEDVariazione");
		}
	}else{
		pdf.setFieldValue("numeroPropostaCCINAPERTURAINTESTATARIOSDDBMEDVariazione", "");
		pdf.disableField("numeroPropostaCCINAPERTURAINTESTATARIOSDDBMEDVariazione");

	}
	if (conto==="CCESTERNA"){
		pdf.enableField("ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione");
	}else{
		pdf.setFieldValue("ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione", "");
		pdf.disableField("ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageTipoSospensioneRevocaRiattivazioneSDD(){

	var operazione = pdf.getFieldValue("tipoSOSPENSIONEREVOCARIATTIVAZIONESDD");
	if (operazione==="SOSPENSIONE"){
		pdf.enableField("meseAnnoSospensioneDa");
		pdf.enableField("meseAnnoSospensioneA");
	}else{
		pdf.setFieldValue("meseAnnoSospensioneDa", "");
		pdf.disableField("meseAnnoSospensioneDa");
		pdf.setFieldValue("meseAnnoSospensioneA", "");
		pdf.disableField("meseAnnoSospensioneA");
	}
	if (operazione==="RIATTIVAZIONE"){
		pdf.enableField("isVariazioneImportoRiattivazioneSDD");
		pdf.enableField("isVariazioneFrazionamentoRiattivazioneSDD");
		pdf.enableField("isVariazioneDataPremioRiattivazioneSDD");
	}else{
		pdf.setFieldValue("isVariazioneImportoRiattivazioneSDD", "");
		pdf.disableField("isVariazioneImportoRiattivazioneSDD");
		pdf.setFieldValue("isVariazioneFrazionamentoRiattivazioneSDD", "");
		pdf.disableField("isVariazioneFrazionamentoRiattivazioneSDD");
		pdf.setFieldValue("isVariazioneDataPremioRiattivazioneSDD", "");
		pdf.disableField("isVariazioneDataPremioRiattivazioneSDD");
	}
	manageVariazioneImportoRiattivazioneSDD();
	manageVariazioneFrazionamentoRiattivazioneSDD();
	manageVariazioneDataPremioRiattivazioneSDD();
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneImportoRiattivazioneSDD(){

	var variazione = pdf.getFieldValue("isVariazioneImportoRiattivazioneSDD");
	if (variazione==="SI"){
		pdf.enableField("importoRiattivazioneSDD");
	}else{
		pdf.setFieldValue("importoRiattivazioneSDD", "");
		pdf.disableField("importoRiattivazioneSDD");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneFrazionamentoRiattivazioneSDD(){

	var variazione = pdf.getFieldValue("isVariazioneFrazionamentoRiattivazioneSDD");
	if (variazione==="SI"){
		pdf.enableField("frequenzaRiattivazioneSDD");
	}else{
		pdf.setFieldValue("frequenzaRiattivazioneSDD", "");
		pdf.disableField("frequenzaRiattivazioneSDD");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageVariazioneDataPremioRiattivazioneSDD(){

	var variazione = pdf.getFieldValue("isVariazioneDataPremioRiattivazioneSDD");
	if (variazione==="SI"){
		pdf.enableField("giornoValutaRiattivazioneSDD");
	}else{
		pdf.setFieldValue("giornoValutaRiattivazioneSDD", "");
		pdf.disableField("giornoValutaRiattivazioneSDD");
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function manageTipoContoAltroCliente(){
	var tipoConto = pdf.getFieldValue("tipoContoALTROCLIENTESDDBMEDVariazione");
	if (tipoConto==="CC"){
		pdf.enableField("ibanContoCorrenteALTROCLIENTESDDBMEDVariazione");
	}else{
		pdf.setFieldValue("ibanContoCorrenteALTROCLIENTESDDBMEDVariazione", "");
		pdf.disableField("ibanContoCorrenteALTROCLIENTESDDBMEDVariazione");
	}
	if (tipoConto==="CCESTERNA"){
		pdf.enableField("ibanContoCorrenteALTROCLIENTESDDEsternaVariazione");
	}else{
		pdf.setFieldValue("ibanContoCorrenteALTROCLIENTESDDEsternaVariazione", "");
		pdf.disableField("ibanContoCorrenteALTROCLIENTESDDEsternaVariazione");
	}
}


/***********************************************************************************************/
/***********************************************************************************************/
function clearImportoPercentualeGrigliaPremio(){
	for (var i=0; ; i++){
		if(document.getElementById("isinFondoPremio"+i) == null)
			break;
		clearField("percentualeFondoPremio"+i);
		clearField("importoFondoPremio"+i);
	}
}

/***********************************************************************************************/
/***********************************************************************************************/
function clearField(fieldName){
	pdf.setFieldValue(fieldName, "");
	try{
		document.getElementById(fieldName).value = "";
		$("div[name='"+fieldName+"ContBorder']").removeClass("pdfFieldHasError");
	} catch (e) {}
}

/***********************************************************************************************/
/***********************************************************************************************/
function clearFondoRipartizionePremio(idx){
	clearField("lineaFondoPremio"+idx);
	clearField("codiceFondoPremio"+idx);
	clearField("societaFondoPremio"+idx);
	clearField("isinFondoPremio"+idx);
	clearField("descrizioneFondoPremio"+idx);
	clearField("percentualeFondoPremio"+idx);
	clearField("importoFondoPremio"+idx);
}


PdfPageDriver.prototype.manageFields = function(obj, idxToStart, idx){
	if (typeof obj !== "undefined" && typeof obj.items !== "undefined") {
		if (typeof idxToStart === "undefined") {
			idxToStart = 0;
		}
		for(var z=0;z<obj.items.length;z++){
			var item = obj.items[z];
			var prefissiFields;
			var suffissiFields;
			var i;
			var el;
			if (typeof item.fields !== "undefined") {
				for(i=0;i<item.fields.length;i++){
					el = document.getElementById(item.fields[i]);
					if(el !== null) {
						this.manageField(item, el, item.fields[i]);
					}
				}
			}

			if (typeof item.suffissiFields !== "undefined" && typeof item.prefissiFields !== "undefined") {
				if (typeof item.suffissiFields === 'string') {
					suffissiFields = new Array(item.suffissiFields);
				}
				else if($.isArray(item.suffissiFields)) {
					suffissiFields = item.suffissiFields;
				}

				if (typeof item.prefissiFields === 'string') {
					prefissiFields = new Array(item.prefissiFields);
				}
				else if($.isArray(item.prefissiFields)) {
					prefissiFields = item.prefissiFields;
				}

				if (typeof suffissiFields !== 'undefined' && typeof prefissiFields !== 'undefined') {
					for(i=0;i<suffissiFields.length;i++){
		                var j;
		                var fieldName;
						if (typeof idx === "undefined") {
							for(var k=idxToStart;;k++){
								var endList = false;
								for(j=0;j<prefissiFields.length;j++){
									fieldName = prefissiFields[j] + suffissiFields[i] + k
									el = document.getElementById(fieldName);
									if(el === null) {
										endList = true;
										break;
									}
									this.manageField(item, el, fieldName);
								}
								if (endList) {
									break;
								}
							}
						}
						else {
							for(j=0;j<prefissiFields.length;j++){
								fieldName = prefissiFields[j] + suffissiFields[i] + idx
								el = document.getElementById(fieldName);
								if(el === null) {
									break;
								}
								this.manageField(item, el, fieldName);
							}
						}
					}
				}
			}
		}
	}
}

PdfPageDriver.prototype.manageField = function(item, el, fieldName) {
	if (typeof item.clear === "boolean" && item.clear) {
		//this.clearField(fieldName, el);
		pdf.setFieldValue(fieldName, "");
	}
	if (typeof item.enable === "boolean") {
		if (item.enable) {
			pdf.enableField(fieldName);
		}
		else {
			pdf.disableField(fieldName);
		}
	}

	PdfPageDriver.prototype.sendAjaxParameters = function(field, rule, notIncludeSelectedIndex){
		var elems = $( rule );
		var concatField = "";
		for(var i=0; i<elems.length;i++){
			if(i!==notIncludeSelectedIndex){
				var idField = $(elems[i]).attr("id");
				concatField +=pdf.getFieldValue(idField)+",";
			}
		}
		return  "&"+field+"="+concatField;
	}
}

PdfPageDriver.prototype.disableRadioButtonField = function(fieldName) {
	pdfPageDriver.innerEnableRadioButtonField(fieldName, false);
}

PdfPageDriver.prototype.enableRadioButtonField = function(fieldName) {
	pdfPageDriver.innerEnableRadioButtonField(fieldName, true);
}

PdfPageDriver.prototype.innerEnableRadioButtonField = function(fieldName, enabled){
	if(jsIsReadonlyModality)
		return;

	var fieldVals = null;
	var fieldNameAndvals = fieldName.split("#");
	if(fieldNameAndvals.length > 1){
		fieldName = fieldNameAndvals[0];
		fieldVals = "|"+fieldNameAndvals[1]+"|";
	}

	var jqobj = $("#"+fieldName);
	var htmltype = jqobj.attr("htmltype");

	if(htmltype !== "radiobutton"){
		return;
	}

	if(enabled){
		$("#"+fieldName).attr({"isreadonly":"false"});
		$("div[name='"+fieldName+"Radio']").each(function(index){
			$(this).css("cursor","pointer").attr({"tabindex": $(this).attr("containerId"), "isreadonly":"false"});
			$("div[fid='"+fieldName+$(this).attr("containerId")+"']").removeClass("pdfReadonlyField");
		});
	}else{
		$("div[name='"+fieldName+"Radio']").each(function(index){
			if(fieldVals == null || fieldVals.indexOf("|"+$(this).attr("value")+"|") >= 0){
				$(this).css("cursor","default").attr({"tabindex": "-1", "isreadonly":"true"});
				var fva = $("#"+fieldName).val();
				if(fva !== "" && fva !== $(this).attr("value"))
					$("div[fid='"+fieldName+$(this).attr("containerId")+"']").addClass("pdfReadonlyField").removeClass("pdfFieldHasError");
				else
					$("div[fid='"+fieldName+$(this).attr("containerId")+"']").addClass("pdfReadonlyField");
			}
		});
	}

	if(document.dati.pdfData_editableFields){
		var idx;
		var ar1;
		var ar2;
		var editableFieldsArray = new Array();
		if(document.dati.pdfData_editableFields.value.length > 0)
			editableFieldsArray = document.dati.pdfData_editableFields.value.split(",");
		var uneditableFieldsArray = new Array();
		if(document.dati.pdfData_uneditableFields.value.length > 0)
			uneditableFieldsArray = document.dati.pdfData_uneditableFields.value.split(",");
		if(enabled){
			if(editableFieldsArray.indexOf(fieldName) < 0)
				editableFieldsArray.push(fieldName);
			idx = uneditableFieldsArray.indexOf(fieldName);
			if(idx >= 0){
				ar1 = uneditableFieldsArray.slice(0,idx);
				ar2 = uneditableFieldsArray.slice(idx+1);
				uneditableFieldsArray = ar1.concat(ar2)
			}
		}else{
			if(uneditableFieldsArray.indexOf(fieldName) < 0)
				uneditableFieldsArray.push(fieldName);
			idx = editableFieldsArray.indexOf(fieldName);
			if(idx >= 0){
				ar1 = editableFieldsArray.slice(0,idx);
				ar2 = editableFieldsArray.slice(idx+1);
				editableFieldsArray = ar1.concat(ar2)
			}
		}
		document.dati.pdfData_editableFields.value = editableFieldsArray.toString();
		document.dati.pdfData_uneditableFields.value = uneditableFieldsArray.toString();
	}
}
