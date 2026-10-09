Pdf = function(){
	this.forceSelection= false;
}

/* Ritorna se il pdf e' stato instanziato con una agevolazione in input */
Pdf.prototype.isAgevolazionePrecaricata = function(){
	return jsNumAgevolazione != "" || (jsIdCarrello != "" && jsCodAgevolazione != "");
}

/* Ritorna se siamo nel carrello */
Pdf.prototype.isPdfInCarrello = function(){
	return jsIsOperatoreMom ? false : jsIdCarrello !== ""; //MOP
}

/* Ritorna il codice del modulo corrente */
Pdf.prototype.getPdfCode = function(){
	return jsPdfCode;
}

/* Ritorna il codice MOM del modulo corrente */
Pdf.prototype.getPdfMomCode = function(){
	return jsPdfMomCode;
}

/* Ritorna la descrizione del modulo corrente */
Pdf.prototype.getPdfDescr = function(){
	return jsPdfDescr;
}

/* Imposta il valore di un campo e di tutte le sue eventuali copie */
Pdf.prototype.setFieldValue = function(fieldName, value){
	pdfPageFields.setFieldValue(fieldName, value);
}

/* Ritorna il valore di un campo */
Pdf.prototype.getFieldValue = function(fieldName){
	return pdfPageFields.getFieldValue(fieldName);
}

/* Ritorna il valore di un campo in formato stringa */
Pdf.prototype.getFieldValueAsString = function(fieldName){
	return pdfPageFields.getFieldValueAsString(fieldName);
}

/* Ritorna l'insieme degli oggetti jquery che rappresentano un campo e tutte le sue eventuali copie */
Pdf.prototype.getJqFieldObjects = function(fieldName){
	return pdfPageFields.getJqFieldObjects(fieldName);
}

/* Ritorna true se il campo esiste, false alrtimenti */
Pdf.prototype.fieldExist = function(fieldName){
	return pdfPageFields.getJqFieldObjects(fieldName).length > 0;
}

/* Abilita un campo */
Pdf.prototype.enableField = function(fieldName){
	pdfPageFields.enableField(fieldName, true);
}

/* Disabilita un campo */
Pdf.prototype.disableField = function(fieldName){
	pdfPageFields.enableField(fieldName, false);
}

/* Ritorna se un campo e' abilitato */
Pdf.prototype.isFieldEnabled = function(field){
	return pdfPageFields.isFieldEnabled(field);
}

/* Nasconde un campo */
Pdf.prototype.hideField = function(fieldName){
	pdfPageFields.hideField(fieldName);
}

/* Mostra un campo */
Pdf.prototype.showField = function(fieldName){
	pdfPageFields.showField(fieldName);
}

/* Ritorna se un campo e' nascosto */
Pdf.prototype.isFieldHided = function(fieldName){
	return pdfPageFields.isFieldHided(fieldName);
}

/* Imposta un campo come obbligatorio (solo come stile di front-end) */
Pdf.prototype.setMandatoryField = function(fieldName, mandatory){
	pdfPageFields.setMandatoryField(fieldName, mandatory);
}

/* Ritorna se un campo e' obbligatorio o meno (solo come stile di front-end) */
Pdf.prototype.isFieldMandatory = function(field){
	return pdfPageFields.isFieldMandatory(field);
}

/* riconoscimento operatore/fasi lavorazione MOM */
Pdf.prototype.isOperatoreMOM = function(){
	return jsIsOperatoreMom;
}
Pdf.prototype.isInValidazioneMOM = function(){
	return jsIsInValidazioneMOM;
}
Pdf.prototype.isInInserimentoMOM = function(){
	return jsIsInInserimentoMOM;
}

/* Richiama un evento (tramite hidden submit)
 * I parametri possono essere:
 * 			eventName: 	"someEventName". Stringa. Obbligatorio. Nome dell'evento
 * 	 		targets:	"field1,field2,...". Stringa o Array. Opzionale. Campi o gruppi da rinfrescare. Se non passato -> refresh dell'intero pdf
 * es: pdf.callEvent("onChangeDataSottoscrizione", "dataSottoscrizione");
 * 
 * oppure un oggetto con:
 * 			eventName: 	"someEventName". Stringa. Obbligatorio. Nome dell'evento
 * 			eventArgs: 	"someEventArgs". Stringa. Opzionale. Argomenti in input all'evento
 * 			fields:	   	"field1,field2,...". Stringa o Array. Opzionale. Campi in submit. Se non passato -> submit di tutti i campi
 * 			targets:	"field1,field2,...". Stringa o Array. Opzionale. Campi o gruppi da rinfrescare. Se non passato -> refresh dell'intero pdf
 * es: pdf.callEvent({eventName: "onChangeNdgCliente", eventArgs: "1", fields: ["ndgCLiente1","nomeCliente1"], targets: "ndgCliente1,nomeCliente1,cognomeCliente1"});
 */
Pdf.prototype.callEvent = function(eventNameOrObj, targets){
	pdfPageFields.callEvent(eventNameOrObj, targets);
}

/* Richiama un evento (hidden submit) mostrando il pannello di attesa
 * Stessi parametri di "callEvent"
 */
Pdf.prototype.callHeavyEvent = function(eventNameOrObj, targets){
	pdfPageFields.callHeavyEvent(eventNameOrObj, targets);
}

/* Richiama un evento tramite normale submit */
Pdf.prototype.submitEvent = function(eventName){
	pdfPageFields.submitEvent(eventName);
}

/* Rimuove gli errori da un campo */
Pdf.prototype.clearFieldErrors = function(fieldName){
	pdfPageFields.clearFieldErrors(fieldName);
}

/* Verifica la presenza di errori su un campo */
Pdf.prototype.hasFieldErrors = function(fieldName){
	return pdfPageFields.hasFieldErrors(fieldName);
}

/* Posizione il pdf sul campo */
Pdf.prototype.scrollToField = function(fieldName){
	pdfPageFields.scrollToField(fieldName);
}

/* Imposta gli autocompletion su clienti e conti fatti dall'engine a "selezione obbligatoria" */
Pdf.prototype.forceAllSelection = function(){
	this.forceSelection = true;
}

/* Imposta l'autocompletion fatto dall'engine sul campo luogo specificato a "selezione obbligatoria" */
Pdf.prototype.forceLuogoSelection = function(fieldName){
	$("#"+fieldName).attr("forceSelection","true");
}

/* Imposta l'autocompletion fatto dall'engine sui campi del cliente specificato a "selezione obbligatoria" */
Pdf.prototype.forcePersonSelection = function(personIdx){
	$("#ndgCliente"+personIdx).attr("forceSelection","true");
	$("#idCensimentoCliente"+personIdx).attr("forceSelection","true");
	$("#nomeCliente"+personIdx).attr("forceSelection","true");
	$("#cognomeCliente"+personIdx).attr("forceSelection","true");
	$("#cognomeNomeCliente"+personIdx).attr("forceSelection","true");
	$("#nomeCognomeCliente"+personIdx).attr("forceSelection","true");
	$("#codiceFiscaleCliente"+personIdx).attr("forceSelection","true");
	$("#partitaIvaCliente"+personIdx).attr("forceSelection","true");
	$("#codiceFiscalePartitaIvaCliente"+personIdx).attr("forceSelection","true");
}

/* Imposta l'autocompletion fatto dall'engine sui campi del conto corrente specificato a "selezione obbligatoria" */
Pdf.prototype.forceContoCorrenteSelection = function(contoName, personIdx){
	$("#numeroContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#ibanContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#paeseIbanContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#cinEuropeoIbanContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#cinControlloIbanContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#abiIbanContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#cabIbanContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#numeroIbanContoCorrente"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
}

/* Imposta l'autocompletion fatto dall'engine sui campi del conto specificato a "selezione obbligatoria" */
Pdf.prototype.forceContoSelection = function(contoName, personIdx){
	$("#numeroConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#ibanConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#paeseIbanConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#cinEuropeoIbanConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#cinControlloIbanConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#abiIbanConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#cabIbanConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
	$("#numeroIbanConto"+contoName+"Cliente"+personIdx).attr("forceSelection","true");
}

/* Imposta l'autocompletion NON AUTOMATICO sui campi del comune specificato a "selezione obbligatoria" */
Pdf.prototype.forceComuneSelection = function(comuneName){
	$("#nazioneComune"+comuneName).attr("forceSelection","true");
	$("#descrNazioneComune"+comuneName).attr("forceSelection","true");
	$("#capComune"+comuneName).attr("forceSelection","true");
	$("#provinciaComune"+comuneName).attr("forceSelection","true");
	$("#comune"+comuneName).attr("forceSelection","true");
}

/* Ritorna se la dispo è uno switch */
Pdf.prototype.isSwitch = function(){
	return jsIsSwitch;
}

var pdf = new Pdf();