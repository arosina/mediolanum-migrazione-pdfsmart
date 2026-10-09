/*
 * Guideline for PageDriver methods overloading
 */
PdfPageDriver = function(){
}

/*
 * Richiamato solo una volta al caricamento della pagina
 */
PdfPageDriver.prototype.onInit = function(){
}

/*
 * Richiamato al caricamento della pagina
 */
PdfPageDriver.prototype.onLoad = function(){
}

/*
 * Richiamato al cambiamento di un dato dell'agente
 */
PdfPageDriver.prototype.onChangeAge = function(fieldName){
	return true;
}

/*
 * Richiamato per pulire i dati dell'agente oltre a quelli standard
 */
PdfPageDriver.prototype.clearAgeData = function(){
}

/*
 * Richiamato al cambiamento di un dato dei clienti. 
 * Tornare true se si vuole lasciare il comportamento stardard del motore
 */
PdfPageDriver.prototype.onChangePerson = function(fieldName, personIdx){
	return true;
}

/*
 * Richiamato per pulire i dati del cliente  oltre a quelli standard
 */
PdfPageDriver.prototype.clearPersonData = function(personIdx){
}

/*
 * Richiamato al cambiamento di un campo
 */
PdfPageDriver.prototype.onChange = function(fieldName){
}

/*
 * Callback richiamata in response ad un evento
 * In input il parametro "eventNameOrObj" cosi' come passato dalla relativa "callEvent"
 */
PdfPageDriver.prototype.onEventCallback = function(eventNameOrObj){
}

/*
 * Default binding ai luoghi
 * 
  	input.fieldName
	input.noElementsIndicator (opt)
	input.driverCmd (opt)
 */
PdfPageDriver.prototype.bindLuogoAutocomplete = function(input){
	pdfPageDataentryUtil.bindLuogoAutocomplete(input);
}

/*
 * Default binding ai toponimi
 * 
  	input.fieldName
	input.noElementsIndicator (opt)
	input.driverCmd (opt)
 */
PdfPageDriver.prototype.bindDescrToponimoAutocomplete = function(input){
	pdfPageDataentryUtil.bindDescrToponimoAutocomplete(input);
}
PdfPageDriver.prototype.clearToponimoData = function(fieldName){
	pdfPageDataentryUtil.clearToponimoData(fieldName)
}

/*
 * Servizio di bind autocomplete sui comuni
 * 
 	input.comuneFieldName
	input.fieldName
	input.noElementsIndicator (opt)
	input.driverCmd (opt)
 */
PdfPageDriver.prototype.bindComuneAutocomplete = function(input){
	pdfPageDataentryUtil.bindComuneAutocomplete(input);
}
PdfPageDriver.prototype.clearComuneData = function(fieldName){
	pdfPageDataentryUtil.clearComuneData(fieldName);
}

/*
 * Default binding all'agente
 * 
 	input.personFieldName
	input.noElementsIndicator (opt)
	input.driverCmd (opt)
 */
PdfPageDriver.prototype.bindAgePersonAutocomplete = function(input){
	pdfPageDataentryUtil.bindAgePersonAutocomplete(input);
}

/*
 * Default binding ai clienti
 * 
 	input.personFieldName
	input.fieldName
	input.noElementsIndicator (opt)
	input.driverCmd (opt)
	input.escludiProspect (opt)
 */
PdfPageDriver.prototype.bindPersonAutocomplete = function(input){
	pdfPageDataentryUtil.bindPersonAutocomplete(input);
}

/*
 * Binding ai clienti con nomi campo specifici
 * 
	input.personName: nome logico del cliente. Da usare nelle callback setMyPersonData e clearMyPersonData (da sovrascrivere)
	input.fieldName: Nome campo in pagina cui bindare l'autocomplete
 	input.personFieldName: Nome campo in query
	input.noElementsIndicator (opt)
	input.driverCmd (opt)
	input.escludiProspect (opt)
 */
PdfPageDriver.prototype.bindMyPersonAutocomplete = function(input){
	pdfPageDataentryUtil.bindMyPersonAutocomplete(input);
}
PdfPageDriver.prototype.setMyPersonData = function(personName, item){	
}
PdfPageDriver.prototype.clearMyPersonData = function(personName){
}

/*
 * Servizio di bind autocomplete sui conti correnti di un cliente
 * 
	input.useCodAgente
	input.ndgFieldName
	input.tipoConto: ''->tutti oppure una label configurata nella tabella PDF_CONFIG, sezione "WHERE_CONDITION_TIPO_CONTO" (ad es. 'CONTO_CORRENTE' per i conti correnti)
	input.ruoliAmmessi: i ruoli ammessi. Se tipoConto != '' e non specificato->'P', 'I', 'C', 'D'
	input.divisaConto (opt): le eventuali divise da considerare (valori tra apici separati da virgola)
	input.contoFieldName
	input.fieldName
	input.noElementsIndicator (opt) 
	input.driverCmd (opt)
 */
PdfPageDriver.prototype.bindContoAutocomplete = function(input){
	pdfPageDataentryUtil.bindContoAutocomplete(input);
}
PdfPageDriver.prototype.clearContoData = function(fieldName){
	pdfPageDataentryUtil.clearContoData(fieldName);
}

/*
 * Sovrascrivere questo metodo per "agganciare" la gestione centralizzata dell'autocomlpete delle agevolazioni
 * In output deve tornare oggetto javascript con le seguenti proprietà:
 * 
	modalitaVersamentoAgevolazione (opt): modalità di versamento (PIC/PAC)
	codProdottoDispositiva (opt): il codice prodotto dispositiva. Se non valorizzato per filtrare le righe delle agevolazioni verrà utilizzato il MomCode
	numeroContratto (opt): il numero contratto che, se valorizzato, viene usato per filtrare le righe delle agevolazioni
	codProdottoDispositivaPartenza (opt): campo PRODC_PROD_PART in like
 * 
 * Se si desidera che l'autocomplete non appaia ritornare "null" 
 */
PdfPageDriver.prototype.provideAgevolazioneData = function(){
	return null;
}

/*
 * Servizio di bind autocomplete sulle agevolazioni
 * 
	input.driverCmd (opt)
 */
PdfPageDriver.prototype.bindCodiceAgevolazioneAutocomplete = function(input){
	pdfPageDataentryUtil.bindCodiceAgevolazioneAutocomplete(input);
}

