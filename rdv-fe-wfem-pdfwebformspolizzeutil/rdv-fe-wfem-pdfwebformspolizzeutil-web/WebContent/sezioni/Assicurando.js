function Assicurando(){
	// This is intentional
}

Assicurando.prototype.manage = function(posizioneAssicurando){
	var codiceFiscaleAssicurandoFieldName = "codiceFiscalePartitaIvaCliente" + posizioneAssicurando;
	if (pdf.getFieldValue(codiceFiscaleAssicurandoFieldName) !== null && pdf.getFieldValue(codiceFiscaleAssicurandoFieldName) !== ""){
		pdf.enableField("tipoRelazioneContraenteAssicurando");
	} else {
		pdf.disableField("descrizioneTipoRelazioneContraenteAssicurando");
		pdf.setFieldValue("tipoRelazioneContraenteAssicurando","");
		pdf.disableField("tipoRelazioneContraenteAssicurando");
	}
}

Assicurando.prototype.manageRelazioni = function(){
	manageRelazioneSoggettoAltro("ContraenteAssicurando");
}

Assicurando.prototype.manageDescrizioneRelazioneSoggettoAltro = function(){
	$("#descrizioneTipoRelazioneContraenteAssicurando").bind({
		keypress: function(event){manageCaratteriDescrizioneAltro(event);},
		focusout: function(event){manageCaratteriDescrizioneAltro(event);}
	});
}




var assicurando = new Assicurando();
