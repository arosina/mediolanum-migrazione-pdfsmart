var curDoc=null;
var ALERT_RDA_MIFID = "Stai inviando in sede un'operazione in ambito MIFID, ricordati di censire anche la riga di PRIT relativa al Report di Adeguatezza e inviarlo in sede compilato e firmato dal cliente.";
var ALERT_RACCOMANDAZIONE_IDD = "Stai inviando in sede un'operazione in ambito IDD, ricordati di censire anche la riga di PRIT relativa alla Raccomandazione e inviarla in sede compilata e firmata dal cliente.";
var ALERT_RDA_MIFID_E_RACCOMANDAZIONE_IDD = "Stai inviando in sede un'operazione in ambito MIFID e IDD, ricordati di censire anche la riga di PRIT relativa al Report di Adeguatezza e inviarlo in sede compilato e firmato dal cliente.";
var ALERT_BENEFICIARI_NOMINATIVI = "Stai inviando in sede un'operazione che potrebbe prevedere il censimento di beneficiari in forma nominativa, se hai indicato almeno un beneficiario non cliente ricordati di censire anche la riga di PRIT relativa al Censimento Anagrafico Beneficiari.";
var MSG_TRASFERIBILITA_ASSEGNI = "Hai verificato che sul titolo sia presente la clausola di NON TRASFERIBILITA'? L'assenza della clausola comporta la violazione art. 49 D.Lgs. 231/07 e successive modifiche/integrazione, nonche', sanzioni amministrative da parte del MEF verso traente e beneficiario"

function refreshAll(){
	resetIframeFirme();
	try{document.documento_mezziPagamentoGrid.unload();}catch(e){}
	try{document.documentiGrid.unload();}catch(e){}
	wfemHiddenSubmit(document.documentoForm,'dacToolbarCont,dacCont,upDocumentiCont,documentiCont,documentoCont,finalsDocScriptsCont,finalsDacScriptsCont');	
}
function refreshDoc(){
	resetIframeFirme();
	try{document.documento_mezziPagamentoGrid.unload();}catch(e){}
	wfemHiddenSubmit(document.documentoForm,'documentoCont,finalsDocScriptsCont');	
}

function changeDoc(){
	if(document.datiReadDocumento.documento_readonlyMode.value == 'true')
		return;
	if(document.documentoForm.documento_idDocModificato.value != '')
		return;
	if(document.documentoForm.documento_idDocumento.value == '')
		document.documentoForm.documento_idDocModificato.value="NEW";
	else
		document.documentoForm.documento_idDocModificato.value=document.documentoForm.documento_idDocumento.value;
}

function testChangeDoc(type,msg,callback){
	// Il doc non e' cambiato
	if(document.documentoForm.documento_idDocModificato.value == ''){
		if(callback){
			callback('continue');
			return;
		}else
			return 'continue';
	}
		
	if(typeof(type) == 'undefined' || type == null)
		type = 'yesnocancel';
	if(typeof(msg) == 'undefined' || msg == null)
		msg = 'Il documento &egrave; stato variato.<br>Desideri salvare le modifiche ?';
	
	if (existAssegnoTrasferibile()) {
		msg += '<br><br>'+MSG_TRASFERIBILITA_ASSEGNI;
	}
	
	return showPopupMsg(type,msg,callback);
}

function waitOnDoc(){
	startRequest();
}

function loadOperazioni(){
	changeDoc();
	document.getElementById("waitCodOperLoader").style.visibility='visible';
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.LoadOperazioni.execute';
	var combo = document.getElementById("documento_codOperazione");
	combo.disabled=true;
	combo.value='';
	combo.options[0].text='Caricamento in corso...';
	$("#idReportAdeguatezzaCont").css("visibility","hidden");
	wfemHiddenSubmit(document.documentoForm,'codOperazioneCont');
}

function onChangeOperazioni(combo){
	$.ajax({
		url: "call.wfem?wfemCmd=prgm.ita.p.dac.business.OnChangeOperazione.execute&documento_codOperazione="+combo.value+"&BrowserInstance="+__getBrowserInstance(),
		dataType: "json",
		async: true,
		success: function(data) {
									document.datiReadDocumento.documento_tipoAlertAdeguatezza.value=""+data.tipoAlertAdeguatezza;
									document.datiReadDocumento.documento_showAlertBeneficiariNominativi.value=""+data.showAlertBeneficiariNominativi;
									if(data.isDocumentoReportAdeguatezza == "true"){
										$("#idReportAdeguatezzaCont").css("visibility","visible");
										hideHelperAnchor("documento_idReportAdeguatezza");
										$("#documento_idReportAdeguatezza").removeClass('fieldHasError').addClass('inputField');
									}else{
										$("#idReportAdeguatezzaCont").css("visibility","hidden");
									}
								}
	});	
	evidenziaCampiObbligatori(combo.value);
	if(document.getElementById("documento_codCassetta") == null)
		return;
	showImpostaCassettaPredefinita();
}

function setFieldClass(id, clname){
	try{
		document.getElementById(id).className=clname;
	}catch(e){}
}

function evidenziaCampiObbligatori(codOpe){

	if(codOpe == '0'){ // Assegni
		document.getElementById("documento_datiAssegno_importo").className = classNameMandInputField;
		document.getElementById("documento_datiAssegno_numAssegno").className = classNameMandInputField;
		document.getElementById("documento_datiAssegno_codDivisa").className = classNameMandInputField;
		document.getElementById("documento_datiAssegno_banca").className = classNameMandInputField;
		document.getElementById("documento_datiAssegno_luogoEmissione").className = classNameMandInputField;
		document.getElementById("documento_datiAssegno_dataEmissioneMask").className = classNameMandInputField;
		document.getElementById("documento_datiAssegno_flagTrasferibile").className = classNameMandInputField;
		setFieldClass("documento_datiAssegno_beneficiariAssegno","inputField");
		if(document.getElementById("documento_datiAssegno_flagTrasferibile").value === "N")
			setFieldClass("documento_datiAssegno_beneficiariAssegno",classNameMandInputField);
		return;
	}
	
	document.getElementById("documento_codProdotto").className = classNameMandInputField;
	document.getElementById("documento_codOperazione").className = classNameMandInputField;
	document.getElementById("documento_agente_nominativoConCodice").className = classNameMandOutputField;
	
	var obblObj = obblOpeArray[codOpe];
	
	if(documentoForm.documento_fnc.value == 'spunta'){
		document.getElementById("documento_numeroContratto").className = classNameMandInputField;
		try{document.getElementById("documento_cliente_nominativoConCodMediolanum").className= classNameMandOutputField;}catch(e){}
		document.getElementById("documento_cliente_codMediolanum").className = classNameMandOutputField;
		document.getElementById("documento_cliente_cognome").className = classNameMandInputField;
		document.getElementById("documento_cliente_nome").className = classNameMandInputField;
	}else{
		if(obblObj != null){
			// Numero contratto
			if(obblObj.obblDati)
				document.getElementById("documento_numeroContratto").className = classNameMandInputField;
			else
				document.getElementById("documento_numeroContratto").className='inputField';
				
			// Spese
			if(obblObj.obblSpese)
				try{document.getElementById("documento_spese").className=classNameMandInputField;}catch(e){}
			else
				try{document.getElementById("documento_spese").className='inputField';}catch(e){}
			
			// Cliente
			if(obblObj.obblCli){
				try{document.getElementById("documento_cliente_nominativoConCodMediolanum").className = classNameMandOutputField;}catch(e){}
				document.getElementById("documento_cliente_codMediolanum").className = classNameMandOutputField;
				document.getElementById("documento_cliente_cognome").className=classNameMandInputField;
				document.getElementById("documento_cliente_nome").className=classNameMandInputField;
			}else{
				try{document.getElementById("documento_cliente_nominativoConCodMediolanum").className='outputField';}catch(e){}
				document.getElementById("documento_cliente_codMediolanum").className='outputField';
				document.getElementById("documento_cliente_cognome").className='inputField';
				document.getElementById("documento_cliente_nome").className='inputField';
			}
		}
	}
		
	// Almeno un mezzo di pagamento
	if(obblObj != null && obblObj.obblMezzoPg)
		document.getElementById("avvisoAlmenoUnMezzoTd").innerHTML="&nbsp;(L'operazione prevede almeno un mezzo di pagamento)";
	else
		document.getElementById("avvisoAlmenoUnMezzoTd").innerHTML="";
	
}

function showImpostaCassettaPredefinita(){
	
	if(document.getElementById("impostaCassettaPredefinitaTd") == null)
		return;
		
	if(document.datiReadDocumento.documento_isDocumentoAssegno.value == 'true')
		return;		
	
	var impostaCassettaPredefinitaVisible = false;	
	var cassetteOpProdCombo = document.getElementById("documento_cassetteOperazioniProdotto");
	var codOperazione = document.getElementById("documento_codOperazione").value;
	var codCassetta = document.getElementById("documento_codCassetta").value;
	if(codOperazione != ''){
		var trovato=false;
		for(var i=0;i<cassetteOpProdCombo.options.length;i++){
			if(cassetteOpProdCombo.options[i].value == codOperazione){
				if(codCassetta != cassetteOpProdCombo.options[i].text)
					trovato=true;
				break;
			}
		}
		if(trovato)
			impostaCassettaPredefinitaVisible = true;		
	}
	if(impostaCassettaPredefinitaVisible)
		document.getElementById("impostaCassettaPredefinitaTd").style.visibility='visible';
	else
		document.getElementById("impostaCassettaPredefinitaTd").style.visibility='hidden';
}

function impostaCassettaPredefinita(){
	var codOperazione = document.getElementById("documento_codOperazione").value;
	if(codOperazione == '')
		return;
	var cassetteCombo = document.getElementById("documento_codCassetta");
	var cassetteOpProdCombo = document.getElementById("documento_cassetteOperazioniProdotto");
	if(cassetteCombo == null || cassetteOpProdCombo == null)
		return;
	for(var i=0;i<cassetteOpProdCombo.options.length;i++){
		if(cassetteOpProdCombo.options[i].value == codOperazione){
			cassetteCombo.value = cassetteOpProdCombo.options[i].text;
			document.getElementById("impostaCassettaPredefinitaTd").style.visibility='hidden';
			return;
		}
	}
	cassetteCombo.focus();
}

function onNewRowMezzoPg(row){
	if(row.isEditabile == 'false'){
		row.style.display = 'none';		
	}else{
		var tipoPag = row.codTipoPagamento;
		if(document.getElementById("documento_mezziPagamento"+row.absIndex+"_codTipoPagamento"))
			tipoPag = document.getElementById("documento_mezziPagamento"+row.absIndex+"_codTipoPagamento").value;
		evidenziaCampiObbligatoriMezzoPg(tipoPag,row.absIndex);
	}
}

function onNewCellMezzoPg(cell){
	if(cell.row.isEditabile != 'true'){
		cell.row.style.height = '20px';
		cell.style.height = '20px';
	}
	
	if(cell.propertyName == 'remove'){
		cell.align = 'center';
		if(cell.row.isEditabile == 'true'){
			cell.title = 'Rimuovi il mezzo di pagamento';
			cell.innerHTML = '<img src="'+__retrieveResourceUrl()+'/images/remove.png" onclick="removeMezzoPg('+cell.row.absIndex+');" style="cursor:pointer;">';
		}else{
			cell.title = '';
			cell.innerHTML = '&nbsp;';
		}		
	}
	
	if(cell.row.changedProps && cell.row.changedProps.indexOf(cell.propertyName) >= 0)
		cell.style.backgroundColor = 'coral';
}

function onChangeTipoPagamento(combo){
	evidenziaCampiObbligatoriMezzoPg(combo.value,combo.getAttribute("absIndex"));
}

function onChangeFlagTrasferibile(combo){
	var absIndex = combo.getAttribute("absIndex");
	var tipoPag = combo.getAttribute("originalCodTipoPagamento");
	if(document.getElementById("documento_mezziPagamento"+absIndex+"_codTipoPagamento"))
		tipoPag = document.getElementById("documento_mezziPagamento"+absIndex+"_codTipoPagamento").value;
	evidenziaCampiObbligatoriMezzoPg(tipoPag,absIndex);
}

function evidenziaCampiObbligatoriMezzoPg(codTipoPagamento,absIndex){
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_codTipoPagamento").className=classNameMandInputField;}catch(e){}
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_importo").className=classNameMandInputField;}catch(e){}
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_codDivisa").className=classNameMandInputField;}catch(e){}
	if(codTipoPagamento == '1'){
		enableField("documento_mezziPagamento"+absIndex+"_codDivisa",false);
		setPropertyValue("documento_mezziPagamento"+absIndex+"_codDivisa","EUR");
	}else{
		enableField("documento_mezziPagamento"+absIndex+"_codDivisa",true);
	}

	try{document.getElementById("documento_mezziPagamento"+absIndex+"_numAssegno").className='inputField';}catch(e){}
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_banca").className='inputField';}catch(e){}
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_luogoEmissione").className='inputField';}catch(e){}
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_dataEmissioneMask").className='inputField';}catch(e){}
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_dataEmissione").className='inputField';}catch(e){}
	try{document.getElementById("documento_mezziPagamento"+absIndex+"_flagTrasferibile").className='inputField';}catch(e){}
	setFieldClass("documento_mezziPagamento"+absIndex+"_beneficiariAssegno", 'inputField');
	if(codTipoPagamento == '1' || codTipoPagamento == '11'){
		try{document.getElementById("documento_mezziPagamento"+absIndex+"_numAssegno").className=classNameMandInputField;}catch(e){}
		try{document.getElementById("documento_mezziPagamento"+absIndex+"_banca").className=classNameMandInputField;}catch(e){}
		try{document.getElementById("documento_mezziPagamento"+absIndex+"_luogoEmissione").className=classNameMandInputField;}catch(e){}
		try{document.getElementById("documento_mezziPagamento"+absIndex+"_dataEmissioneMask").className=classNameMandInputField;}catch(e){}
		try{document.getElementById("documento_mezziPagamento"+absIndex+"_dataEmissione").className=classNameMandInputField;}catch(e){}
		try{document.getElementById("documento_mezziPagamento"+absIndex+"_flagTrasferibile").className=classNameMandInputField;}catch(e){}
		if(document.getElementById("documento_mezziPagamento"+absIndex+"_flagTrasferibile") &&
		   document.getElementById("documento_mezziPagamento"+absIndex+"_flagTrasferibile").value === "N")
			setFieldClass("documento_mezziPagamento"+absIndex+"_beneficiariAssegno", classNameMandInputField);
	}
}

function doAggMezzoPg(){
	changeDoc();
	waitOnDoc();
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.AddMezzoPagamento.execute';
	wfemHiddenSubmit(document.documentoForm,'messaggiObject,mezziPagamentoCont');
}

function removeMezzoPg(absIndexMezzoPg){
	changeDoc();
	waitOnDoc();
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.RemoveMezzoPagamento.execute';
	document.documentoForm.documento_absIndexMezzoPgSelezionato.value = absIndexMezzoPg;
	wfemHiddenSubmit(document.documentoForm,'messaggiObject,mezziPagamentoCont');
}

function doInserisciDocumentoAction(){
	controllaTrasferibilitaAssegni(doInserisciDocumentoActionEnd);
}

function doInserisciDocumentoActionEnd(){
	var alertMsg = "";
	if(document.datiReadDocumento.documento_tipoAlertAdeguatezza.value === "RDA_MIFID")
		alertMsg = ALERT_RDA_MIFID;
	else if(document.datiReadDocumento.documento_tipoAlertAdeguatezza.value === "RACCOMANDAZIONE_IDD")
		alertMsg = ALERT_RACCOMANDAZIONE_IDD;
	else if(document.datiReadDocumento.documento_tipoAlertAdeguatezza.value === "RDA_MIFID_E_RACCOMANDAZIONE_IDD")
		alertMsg = ALERT_RDA_MIFID_E_RACCOMANDAZIONE_IDD;
	if (document.datiReadDocumento.documento_showAlertBeneficiariNominativi.value === "true"){
		if (alertMsg.length > 0)
			alertMsg += "\n\n";
		alertMsg += ALERT_BENEFICIARI_NOMINATIVI;
	}
	if (alertMsg.length > 0)
		alert(alertMsg);
	waitOnDoc();
	document.documentoForm.documento_nuovoOnInserisci.value = 'true';
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.SalvaDocumento.execute';
	refreshAll();
}

function controllaTrasferibilitaAssegni(callback) {	
	if (existAssegnoTrasferibile()) {
		return showPopupMsg('annullaprosegui',MSG_TRASFERIBILITA_ASSEGNI,callback);
	} else {
		callback();
	}
}

function existAssegnoTrasferibile() {
	var i;
	var existAssegnoTrasferibile = false;
	for (i = 0; ; i++) { 
		var codTipoPagamento = $('select[name="documento_mezziPagamento'+i+'_codTipoPagamento"]').find(":selected").val();
		if (codTipoPagamento) {
			if (codTipoPagamento == 1) {
				var importo = $('#documento_mezziPagamento'+i+'_importo').val().replace('.','').replace(',','.');
				var flagTrasferibile = $('select[name="documento_mezziPagamento'+i+'_flagTrasferibile"]').find(":selected").val();
				if (Number(importo) >= 1000 && flagTrasferibile === "N") {
					existAssegnoTrasferibile = true;					
				}
			}
		} else {
			break;
		}
	}
	return existAssegnoTrasferibile;
}

function doSalvaDocumentoAction(){
	controllaTrasferibilitaAssegni(doSalvaDocumentoActionEnd);
}

function doSalvaDocumentoActionEnd(){
	var alertMsg = "";
	if(document.datiReadDocumento.documento_tipoAlertAdeguatezza.value === "RDA_MIFID")
		alertMsg = ALERT_RDA_MIFID;
	else if(document.datiReadDocumento.documento_tipoAlertAdeguatezza.value === "RACCOMANDAZIONE_IDD")
		alertMsg = ALERT_RACCOMANDAZIONE_IDD;
	else if(document.datiReadDocumento.documento_tipoAlertAdeguatezza.value === "RDA_MIFID_E_RACCOMANDAZIONE_IDD")
		alertMsg = ALERT_RDA_MIFID_E_RACCOMANDAZIONE_IDD;
	if (document.datiReadDocumento.documento_showAlertBeneficiariNominativi.value === "true"){
		if (alertMsg.length > 0)
			alertMsg += "\n\n";
		alertMsg += ALERT_BENEFICIARI_NOMINATIVI;
	}
	if (alertMsg.length > 0)
		alert(alertMsg);
	waitOnDoc();
	document.documentoForm.documento_nuovoOnInserisci.value = 'false';
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.SalvaDocumento.execute';
	refreshAll();
}

function doCancellaDocumentoAction(){
	waitOnDoc();
	docVisibile=false;
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.CancellaDocumento.execute';
	refreshAll();
}

function esitaDocumento(esito){
	if(esito == '4'){ // Doc mancante
		var ret = showPopupMsg('yesno','Sei sicuro di voler esitare documento mancante ?');
		if(ret == 'no' || ret == 'cancel')
			return;
	}
	waitOnDoc();
	docVisibile=false;
	document.documentoForm.documento_esitoNew.value = esito;
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.EsitaDocumento.execute';
	refreshAll();
}

function esitaFirmaCliente(esito){
	if(esito == 'N'){ // Non conforme
		var ret = showPopupMsg('yesno','Sei sicuro di voler esitare firma non conforme ?');
		if(ret == 'no' || ret == 'cancel')
			return;
	}
	waitOnDoc();
	docVisibile=false;
	document.documentoForm.documento_esitoFirmaClienteNew.value = esito.toString();
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.EsitaFirmaCliente.execute';
	refreshAll();
}

function esitaFirmaAgente(esito){
	if(esito == 'N'){ // Non conforme
		var ret = showPopupMsg('yesno','Sei sicuro di voler esitare firma non conforme ?');
		if(ret == 'no' || ret == 'cancel')
			return;
	}
	waitOnDoc();
	docVisibile=false;
	document.documentoForm.documento_esitoFirmaAgenteNew.value = esito.toString();
	document.documentoForm.wfemCmd.value = 'prgm.ita.p.dac.business.EsitaFirmaAgente.execute';
	refreshAll();
}

function selezionaDocumento(idDoc){
	if(idDoc == '')
		return;
	try{
		var rows = document.documentiGrid.tableModel.rows;
		if(idDoc == ''){
			document.documentiGrid.showRow(rows.length-1);
			return;
		}
		for(var i=0;i<rows.length;i++){
			if(rows[i].idDocumento == idDoc){
				document.documentiGrid.selectRow(i);
				break;
			}
		}	
	}catch(e){}
}

function loadContratto(){
	showPopupContratti(document.getElementById("documento_numeroContratto").value);
}
function showPopupContrattiEnd(contr) {
	if(contr == null)
		return;

	changeDoc();
	document.getElementById("documento_numeroContratto").value = contr.numeroContratto;
	document.getElementById("documento_descrContratto").value = contr.descrContratto;
	document.getElementById("docTitleDescrContr").innerHTML = "&nbsp;&nbsp;&nbsp;&#9658;&nbsp;"+contr.descrContratto+"&nbsp;&#9668;";
	document.getElementById("documento_agente_codRete").value = contr.agente_codRete;
	document.getElementById("documento_agente_codAgente").value = contr.agente_codAgente;
	document.getElementById("documento_agente_nominativo").value = contr.agente_nominativo;
	document.getElementById("documento_agente_codMediolanum").value = contr.agente_codMediolanum;
	document.getElementById("documento_agente_nominativoConCodice").value = contr.agente_codAgente+' - '+contr.agente_nominativo;
	document.getElementById("documento_cliente_cognome").value = contr.cliente_cognome;
	document.getElementById("documento_cliente_nome").value = contr.cliente_nome;
	document.getElementById("documento_cliente_codMediolanum").value = contr.cliente_codMediolanum;
	if(contr.cliente_codMediolanum == '')
		document.getElementById("documento_cliente_nominativoConCodMediolanum").value = contr.cliente_cognome+' '+contr.cliente_nome;
	else
		document.getElementById("documento_cliente_nominativoConCodMediolanum").value = contr.cliente_codMediolanum+' - '+contr.cliente_cognome+' '+contr.cliente_nome;

	// Se il cliente non � assegnato pulisco l'agente
	clearAgenteNonAssegnato();
}

function loadCliente(){
	showPopupClienti();
}

function showPopupClientiEnd(res) {
	if(res == null)
		return;	

	changeDoc();
	if(res.type == 'cliente'){	
		
		var cli = res;	
		document.getElementById("documento_cliente_codMediolanum").value = cli.codMediolanum;
		document.getElementById("documento_cliente_cognome").value = cli.cognome;
		document.getElementById("documento_cliente_nome").value = cli.nome;
		if(cli.codMediolanum == '')
			document.getElementById("documento_cliente_nominativoConCodMediolanum").value = cli.cognome+' '+cli.nome;
		else
			document.getElementById("documento_cliente_nominativoConCodMediolanum").value = cli.codMediolanum+' - '+cli.cognome+' '+cli.nome;
		if(cli.tipoRicerca == '1'){ // Miei clienti
			document.getElementById("documento_agente_codRete").value = document.datiRead.codReteAgenteRiferimento.value;
			document.getElementById("documento_agente_codAgente").value = document.datiRead.codAgenteRiferimento.value;
			document.getElementById("documento_agente_nominativo").value = document.datiRead.nominativoAgenteRiferimento.value;
			document.getElementById("documento_agente_codMediolanum").value = document.datiRead.codMediolanumAgenteRiferimento.value;
			document.getElementById("documento_agente_nominativoConCodice").value = document.datiRead.codAgenteRiferimento.value+' - '+document.datiRead.nominativoAgenteRiferimento.value;
		}else{
			document.getElementById("documento_agente_codRete").value = cli.agente_codRete;
			document.getElementById("documento_agente_codAgente").value = cli.agente_codAgente;
			document.getElementById("documento_agente_nominativo").value = cli.agente_nominativo;
			document.getElementById("documento_agente_codMediolanum").value = cli.agente_codMediolanum;
			document.getElementById("documento_agente_nominativoConCodice").value = cli.agente_codAgente+' - '+cli.agente_nominativo;
		}
		
	}else if(res.type == 'contratto'){	
		
		var contr = res;
		document.getElementById("documento_numeroContratto").value = contr.numeroContratto;
		document.getElementById("documento_descrContratto").value = contr.descrContratto;
		document.getElementById("docTitleDescrContr").innerHTML = "&nbsp;&nbsp;&nbsp;&#9658;&nbsp;"+contr.descrContratto+"&nbsp;&#9668;";
		document.getElementById("documento_cliente_codMediolanum").value = contr.cliente_codMediolanum;
		document.getElementById("documento_cliente_cognome").value = contr.cliente_cognome;
		document.getElementById("documento_cliente_nome").value = contr.cliente_nome;
		if(contr.cliente_codMediolanum == '')
			document.getElementById("documento_cliente_nominativoConCodMediolanum").value = contr.cliente_cognome+' '+contr.cliente_nome;
		else
			document.getElementById("documento_cliente_nominativoConCodMediolanum").value = contr.cliente_codMediolanum+' - '+contr.cliente_cognome+' '+contr.cliente_nome;
		if(contr.tipoRicerca == '1'){ // Contratti di miei clienti
			document.getElementById("documento_agente_codRete").value = document.datiRead.codReteAgenteRiferimento.value;
			document.getElementById("documento_agente_codAgente").value = document.datiRead.codAgenteRiferimento.value;
			document.getElementById("documento_agente_nominativo").value = document.datiRead.nominativoAgenteRiferimento.value;
			document.getElementById("documento_agente_codMediolanum").value = document.datiRead.codMediolanumAgenteRiferimento.value;
			document.getElementById("documento_agente_nominativoConCodice").value = document.datiRead.codAgenteRiferimento.value+' - '+document.datiRead.nominativoAgenteRiferimento.value;
		}else{
			document.getElementById("documento_agente_codRete").value = contr.agente_codRete;
			document.getElementById("documento_agente_codAgente").value = contr.agente_codAgente;
			document.getElementById("documento_agente_nominativo").value = contr.agente_nominativo;
			document.getElementById("documento_agente_codMediolanum").value = contr.agente_codMediolanum;
			document.getElementById("documento_agente_nominativoConCodice").value = contr.agente_codAgente+' - '+contr.agente_nominativo;
		}
	}
	
	// Se il cliente non � assegnato pulisco l'agente
	clearAgenteNonAssegnato();
}

function clearAgenteNonAssegnato(){
	if(document.getElementById("documento_agente_codAgente").value == '0000000000'){
		document.getElementById("documento_agente_codRete").value = '';
		document.getElementById("documento_agente_codAgente").value = '';
		document.getElementById("documento_agente_nominativo").value = '';
		document.getElementById("documento_agente_codMediolanum").value = '';
		document.getElementById("documento_agente_nominativoConCodice").value = '';
	}
}

function loadAgente(){
	showPopupAgenti();
}
function showPopupAgentiEnd(age) {
	if(age == null)
		return;
		
	changeDoc();
	document.getElementById("documento_agente_codRete").value = age.codRete;
	document.getElementById("documento_agente_codAgente").value = age.codAgente;
	document.getElementById("documento_agente_nominativo").value = age.nominativo;
	document.getElementById("documento_agente_codMediolanum").value = age.codMediolanum;
	document.getElementById("documento_agente_nominativoConCodice").value = age.codAgente+' - '+age.nominativo;
}
	
function clearContratto(){
	document.getElementById("documento_numeroContratto").value = '';
	document.getElementById("documento_descrContratto").value = '';
	document.getElementById("documento_codProdotto").value = '';
	document.getElementById("documento_codOperazione").value = '';
	enableField("documento_codOperazione",false);
	clearCliente();
}

function clearCliente(){
	document.getElementById("documento_cliente_cognome").value = '';
	document.getElementById("documento_cliente_nome").value = '';
	document.getElementById("documento_cliente_codMediolanum").value = '';
	document.getElementById("documento_cliente_nominativoConCodMediolanum").value = '';
	clearAgente();
}

function clearAgente(){
	if(document.getElementById('agenteModificabileTable') == null) // L'agente non � modificabile (ad es. in offline)
		return;
	changeDoc();
	document.getElementById("documento_agente_codRete").value = '';
	document.getElementById("documento_agente_codAgente").value = '';
	document.getElementById("documento_agente_nominativo").value = '';
	document.getElementById("documento_agente_codMediolanum").value = '';
	document.getElementById("documento_agente_nominativoConCodice").value = '';
}

function onNewCellStoriaDocumento(cell){
	if(cell.propertyName == 'idDac'){
		cell.align = 'center';
		cell.title = 'Clicca per visualizzare il Prit/Dac';
		cell.innerHTML = "<span style='cursor:pointer;text-decoration:underline;' onclick='quickViewDac(\""+cell.row.idDac+"\");'>"+cell.innerHTML+"</span>";
	}
	
	if(cell.propertyName == 'tipoDac'){
		cell.onmouseover = function(){event.cancelBubble = true;};
		cell.onmouseout = function(){event.cancelBubble = true;};
		cell.title = '';
		cell.innerHTML = '<table height="100%" width="100%"><tr><td bgcolor="'+document.getElementById("dacColor"+cell.row.tipoDac).value+'"></td></tr></table>';
	}
	
	if(cell.propertyName == 'uffDestinatario'){
		if(cell.row.tipoDac == '1'){
			cell.innerHTML = 'Coding/Comdata/C-Global';
			cell.title = 'Coding/Comdata/C-Global';
		}
	}
}

function onNewCellPlicoDocumento(cell){
	if(cell.propertyName == 'barcode' ||
	   cell.propertyName == 'numeroContratto')
		cell.align = 'center';
	
	// Gestione assegni
	if(cell.row.codProdotto == '0'){
		if(cell.propertyName == 'numeroContratto'){
			cell.title = '';
			cell.innerHTML = '&nbsp;';
		}
		
		if(cell.propertyName == 'codProdotto'){
			cell.title = 'Clicca per visualizzare l\'assegno';
			cell.innerHTML = "<span style='cursor:pointer;text-decoration:underline;' onclick='quickViewDoc(\""+cell.row.idDocumento+"\");'>Assegno</span>";
		}
		
		if(cell.propertyName == 'codOperazione'){
			cell.title = "n. "+cell.row.numeroContratto;
			cell.innerHTML = "n. "+cell.row.numeroContratto;
		}
	}else{
		if(cell.propertyName == 'codProdotto'){
			cell.title = 'Clicca per visualizzare il documento';
			cell.innerHTML = "<span style='cursor:pointer;text-decoration:underline;' onclick='quickViewDoc(\""+cell.row.idDocumento+"\");'>"+cell.innerHTML+"</span>";
		}
	}
	
}

function quickViewDac(idDac,isDac){
	var ufficio = '1';
	try{ufficio = document.documentoForm.documento_ufficio.value;}catch(e){ufficio = '1'}
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=prgm.ita.p.dac.display.QuickViewDac.executeOnPopup"+
									 "&readRequest=false&idDac="+idDac+"&ufficio="+ufficio+"&BrowserInstance="+document.documentoForm.BrowserInstance.value;
	if(__isWlt){
		var title = "";
		if(isDac)
			title = "Dac N� "+idDac;
		else
			title = "Prit N� "+idDac;
		var specs = "titlebar=yes,scrollbars=yes,resizable=yes,width=800,height=600";	
		openNewWindow(url,title,specs);
	}else{
		var w = screen.availWidth - 50; 
		var openWindow = window.open("","","titlebar=yes, resizable=yes");
		if(isDac)
			openWindow.document.write("<title>Dac N� "+idDac+"</title>");
		else
			openWindow.document.write("<title>Prit N� "+idDac+"</title>");
		openWindow.document.write("<body style='margin:0; padding:0;'>");
		openWindow.document.write("<iframe style='width=100%; height:100%' src='"+url+"'/>");
		openWindow.document.write("</body>");
		openWindow.document.close();	
		return;
	}
									 
}

function quickViewDoc(idDocumento){
	var ufficio = '1';
	try{ufficio = document.documentoForm.documento_ufficio.value;}catch(e){ufficio = '1'}
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=prgm.ita.p.dac.display.QuickViewDoc.executeOnPopup"+
									 "&readRequest=false&idDocumento="+idDocumento+"&ufficio="+ufficio+"&BrowserInstance="+document.documentoForm.BrowserInstance.value;
	var openWindow = window.open("","","titlebar=yes, resizable=yes, width=900, height=400");
	openWindow.document.write("<title>Documento</title>");
	openWindow.document.write("<body style='margin:0; padding:0;'>");
	openWindow.document.write("<iframe style='width=100%; height:100%' src='"+url+"'/>");
	openWindow.document.write("</body>");
	openWindow.document.close();	
}

// ///////////////////////////////
// Gestione tabbettini documento
// ///////////////////////////////
var curIframeCliente = null;
var curIframeAgente = null;
function selectDocJSTab(tabName){
	if(tabName == "")
		tabName = "tabDati";
	var tabContainer = document.getElementById('JSTabDocumento');
	var tab = document.getElementById(tabName);  
	var tabEl = document.getElementById(tabName+'El');  
	if(tabContainer == null || tab == null || tabEl == null)
		return;

	var tabNames = tabContainer.getAttribute("tabNames").split(',');
	for(var i=0;i<tabNames.length;i++){
		if(document.getElementById(tabNames[i]).inerror == 'true')
			document.getElementById(tabNames[i]).className = "htabErrors";
		else
			document.getElementById(tabNames[i]).className = "htab";
		document.getElementById(tabNames[i]+"El").style.display = "none";
	}
	
	// Iframe firme cliente
	if(document.getElementById("firmeClienteIframe") != null && tabName == 'tabFirmeCliente' && document.getElementById("firmeClienteCont") != null){
		resetIframeFirmeCliente();
		curIframeCliente = document.getElementById("firmeClienteIframe");
		document.getElementById("firmeClienteCont").appendChild(document.getElementById("firmeClienteIframeContainer").removeChild(curIframeCliente));
	}
	
	// Iframe firme agente
	if(document.getElementById("firmeAgenteIframe") != null && tabName == 'tabFirmeAgente' && document.getElementById("firmeAgenteCont") != null){
		resetIframeFirmeAgente();
		curIframeAgente = document.getElementById("firmeAgenteIframe");
		document.getElementById("firmeAgenteCont").appendChild(document.getElementById("firmeAgenteIframeContainer").removeChild(curIframeAgente));
	}

	tab.className = "htabSelected";
	tabEl.style.display = "inline";

	if(tabName !== document.documentoForm.documento_tabName.value){
		hideHelper();
	}

	document.documentoForm.documento_tabName.value = tabName;
	
	// In modalita' lettura le toolbar non ci sono
	try{
		if(tabName == 'tabFirmeCliente'){ // Tab delle firme cliente
			if(document.documentoForm.documento_idDocModificato.value == ''){ 
				document.getElementById("toolbarDocCont").style.display='none';
				document.getElementById("toolbarFirmeAgenteCont").style.display='none';
				document.getElementById("toolbarFirmeClienteCont").style.display='';
			}else{ // Se il doc � modificato non visualizzo la toolbar firma
				document.getElementById("toolbarDocCont").style.display='';				
			}
		}else if(tabName == 'tabFirmeAgente'){ // Tab delle firme agente
			if(document.documentoForm.documento_idDocModificato.value == ''){ 
				document.getElementById("toolbarDocCont").style.display='none';
				document.getElementById("toolbarFirmeClienteCont").style.display='none';
				document.getElementById("toolbarFirmeAgenteCont").style.display='';
			}else{ // Se il doc � modificato non visualizzo la toolbar firma
				document.getElementById("toolbarDocCont").style.display='';				
			}
		}else{
			document.getElementById("toolbarDocCont").style.display='';
			document.getElementById("toolbarFirmeClienteCont").style.display='none';
			document.getElementById("toolbarFirmeAgenteCont").style.display='none';
		}
	}catch(e){}
}

function resetIframeFirme(){
	resetIframeFirmeCliente();
	resetIframeFirmeAgente();
}

function resetIframeFirmeCliente(){
	if(curIframeCliente != null){		
		document.getElementById("firmeClienteIframeContainer").appendChild(document.getElementById("firmeClienteCont").removeChild(curIframeCliente));
		curIframeCliente = null;
	}	
}

function resetIframeFirmeAgente(){
	if(curIframeAgente != null){		
		document.getElementById("firmeAgenteIframeContainer").appendChild(document.getElementById("firmeAgenteCont").removeChild(curIframeAgente));
		curIframeAgente = null;
	}	
}

function startLoadFirmaCliente(){
	var firmeCliIframe = document.getElementById("firmeClienteIframe");
	if(firmeCliIframe.codMediolanum != document.getElementById("documento_cliente_codMediolanum").value){
		firmeCliIframe.codMediolanum = document.getElementById("documento_cliente_codMediolanum").value;
		firmeCliIframe.src = __retrieveResourceUrl()+'/documento/waitLoadFirmeCliente.html';
	}
}

function endWaitLoadFirmeCliente(){
	document.firmeClienteForm.codMediolanum.value = document.getElementById("documento_cliente_codMediolanum").value;
	document.firmeClienteForm.cognome.value = document.getElementById("documento_cliente_cognome").value;
	document.firmeClienteForm.nome.value = document.getElementById("documento_cliente_nome").value;
	document.firmeClienteForm.submit();
}

function startLoadFirmaAgente(){
	var firmeAgeIframe = document.getElementById("firmeAgenteIframe");
	if(firmeAgeIframe.codMediolanum != document.getElementById("documento_agente_codMediolanum").value){
		firmeAgeIframe.codMediolanum = document.getElementById("documento_agente_codMediolanum").value;
		firmeAgeIframe.src = __retrieveResourceUrl()+'/documento/waitLoadFirmeAgente.html';
	}
}

function endWaitLoadFirmeAgente(){
	document.firmeAgenteForm.codMediolanum.value = document.getElementById("documento_agente_codMediolanum").value;
	document.firmeAgenteForm.nominativo.value = document.getElementById("documento_agente_nominativo").value;
	document.firmeAgenteForm.submit();
}

function showDocRete(idDoc){
	openModalPopup("prgm.ita.p.dac.display.DocumentoRete","idDocumento="+idDoc,null,null,"Documento originale",480,850);
}
// /////////////////////////////

// /////////////////////////////
// Movimento dettaglio documento
// /////////////////////////////
var movingCookieName = "dacMovingDocumentCookie";
var moving=false;
var startx=0; var starty=0;
function setMoveable(click){
	if(click){
		if(document.getElementById("documentoCont").style.position == 'absolute'){
			lockedWindowLayout();
			deleteCookie(movingCookieName);
		}else{
			unlockedWindowLayout();
			setCookie(movingCookieName,''+document.getElementById("documentoCont").style.top+','+document.getElementById("documentoCont").style.left);
		}
	}else{
		var movingCoord = getCookie(movingCookieName);
		if(movingCoord != null){
			unlockedWindowLayout(movingCoord);
		}else{
			lockedWindowLayout();			
		}
		
	}
}
function lockedWindowLayout(){
	document.getElementById("docAnchor").title='Libera finestra';
	document.getElementById("docAnchor").src=__retrieveResourceUrl()+'/images/windowMove.png';
	document.getElementById("documentoCont").style.position='relative';
	document.getElementById("documentoCont").style.top='0';
	document.getElementById("documentoCont").style.left='0';
	document.getElementById("documentoCont").style.width='99%';
	document.getElementById("docTitle").style.cursor='normal';
		
	if(document.getElementById("documento_cliente_nominativoConCodMediolanum") != null)		
		document.getElementById("documento_cliente_nominativoConCodMediolanum").style.width = '300';
		
	if(document.getElementById("documento_agente_nominativoConCodice") != null)		
		document.getElementById("documento_agente_nominativoConCodice").style.width = '250';
}
function unlockedWindowLayout(movingCoord){
	var topCoord = '200';
	var leftCoord = '20';
	if(typeof(movingCoord) != 'undefined'){
		var coord = movingCoord.split(',');
		topCoord = coord[0];
		leftCoord = coord[1];
	}
	document.getElementById("docAnchor").title='Blocca finestra';
	document.getElementById("docAnchor").src=__retrieveResourceUrl()+'/images/windowLock.png';
	document.getElementById("documentoCont").style.position='absolute';
	document.getElementById("documentoCont").style.top=topCoord;
	document.getElementById("documentoCont").style.left=leftCoord;
	document.getElementById("documentoCont").style.width='920';
	document.getElementById("docTitle").style.cursor='move';

	if(document.getElementById("documento_cliente_nominativoConCodMediolanum") != null)		
		document.getElementById("documento_cliente_nominativoConCodMediolanum").style.width = '300';
		
	if(document.getElementById("documento_agente_nominativoConCodice") != null)		
		document.getElementById("documento_agente_nominativoConCodice").style.width = '250';
}
function startMove(event){
	if(document.getElementById("documentoCont").style.position != 'absolute')
		return;
	moving=true;

	if(!event) 
		event = window.event;
	
	startx = event.clientX;
	starty = event.clientY;
}
function move(event){
	if(!moving)
		return;

	try {
		event.stopPropagation();
	} catch(e) {
		event = window.event;
		event.cancelBubble=true;
	}
		
	var deltax = event.clientX-startx; startx=event.clientX;
	var deltay = event.clientY-starty; starty=event.clientY;
	document.getElementById("documentoCont").style.left = parseInt(document.getElementById("documentoCont").style.left) + deltax;
	document.getElementById("documentoCont").style.top = parseInt(document.getElementById("documentoCont").style.top) + deltay;
}
function endMove(){
	if(moving)
		setCookie(movingCookieName,''+document.getElementById("documentoCont").style.top+','+document.getElementById("documentoCont").style.left);
	moving=false;
}
document.onmousemove=move;
document.onmouseup=endMove;

function setCookie(name, value) {
	document.cookie = name + "=" + escape(value);
}
function getCookie(name){
	if (document.cookie.length <= 0)
   		return null;
   	var search = name + "=";
    var offset = document.cookie.indexOf(search);
    if(offset == -1)
    	return null;
    offset += search.length;
    end = document.cookie.indexOf(";",offset);
    if(end == -1)
    	end = document.cookie.length;
	if(offset == end)
		return null;
    return unescape(document.cookie.substring(offset,end))
}
function deleteCookie (name) {
	var exp = new Date();
	exp.setTime (exp.getTime() - 1); // This cookie is history
	var cval = getCookie (name);
	document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString();
}
// /////////////////////////////

