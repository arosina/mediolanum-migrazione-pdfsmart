function TerzoPagatore(){
	// This is intentional
}

TerzoPagatore.prototype.manage = function(nomeCampoTipoConto, valoreTerzoPagatore, suffissoCampiDaGestire){

	var valoreCampoTipoConto = pdf.getFieldValue(nomeCampoTipoConto);
	if (valoreCampoTipoConto !== null && valoreCampoTipoConto === valoreTerzoPagatore){
		pdf.enableField("tipoRelazione"+suffissoCampiDaGestire);
	} else {
		pdf.disableField("tipoRelazione"+suffissoCampiDaGestire);
		pdf.setFieldValue("tipoRelazione"+suffissoCampiDaGestire,"");
	}
	this.manageTipoRelazione();
}

TerzoPagatore.prototype.manageTipoRelazione = function(){
	manageRelazioneSoggettoAltro("ContraenteTerzoPagatore");
	this.manageDescrizioneRelazioneSoggettoAltro();
}

TerzoPagatore.prototype.manageDescrizioneRelazioneSoggettoAltro = function(){
	$("#descrizioneTipoRelazioneContraenteTerzoPagatore").bind({
		keypress: function(event){manageCaratteriDescrizioneAltro(event);},
		focusout: function(event){manageCaratteriDescrizioneAltro(event);}
	});
}

var terzoPagatore = new TerzoPagatore();
