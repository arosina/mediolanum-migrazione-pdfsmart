function manageReferenteTerzo(){
	
	var referenteTerzo = pdf.getFieldValue("sceltaReferenteTerzo");

	if(referenteTerzo === "S"){
		pdf.enableField("cognomeReferenteTerzo");
		pdf.enableField("nomeReferenteTerzo");
		pdf.enableField("codiceFiscaleReferenteTerzo");
		pdf.enableField("toponimoIndirizzoReferenteTerzo");
		pdf.enableField("indirizzoReferenteTerzo");
		pdf.enableField("numeroCivicoIndirizzoReferenteTerzo");
		pdf.enableField("capComuneReferenteTerzo");
		pdf.enableField("comuneReferenteTerzo");
		pdf.enableField("provinciaComuneReferenteTerzo");
		pdf.enableField("nazioneComuneReferenteTerzo");
		pdf.enableField("prefissoInternazionaleTelefonoReferenteTerzo");
		pdf.enableField("prefissoTelefonoReferenteTerzo");
		pdf.enableField("telefonoReferenteTerzo");
		pdf.enableField("emailReferenteTerzo");
	} else {
		pdf.disableField("cognomeReferenteTerzo");
		pdf.disableField("nomeReferenteTerzo");
		pdf.disableField("codiceFiscaleReferenteTerzo");
		pdf.disableField("toponimoIndirizzoReferenteTerzo");
		pdf.disableField("indirizzoReferenteTerzo");
		pdf.disableField("numeroCivicoIndirizzoReferenteTerzo");
		pdf.disableField("capComuneReferenteTerzo");
		pdf.disableField("comuneReferenteTerzo");
		pdf.disableField("provinciaComuneReferenteTerzo");
		pdf.disableField("nazioneComuneReferenteTerzo");
		pdf.disableField("prefissoInternazionaleTelefonoReferenteTerzo");
		pdf.disableField("prefissoTelefonoReferenteTerzo");
		pdf.disableField("telefonoReferenteTerzo");
		pdf.disableField("emailReferenteTerzo");
		
		clearField("cognomeReferenteTerzo");
		clearField("nomeReferenteTerzo");
		clearField("codiceFiscaleReferenteTerzo");
		clearField("codToponimoIndirizzoReferenteTerzo");
		clearField("toponimoIndirizzoReferenteTerzo");
		clearField("indirizzoReferenteTerzo");
		clearField("numeroCivicoIndirizzoReferenteTerzo");
		clearField("capComuneReferenteTerzo");
		clearField("comuneReferenteTerzo");
		clearField("provinciaComuneReferenteTerzo");
		clearField("nazioneComuneReferenteTerzo");
		clearField("prefissoInternazionaleTelefonoReferenteTerzo");
		clearField("prefissoTelefonoReferenteTerzo");
		clearField("telefonoReferenteTerzo");
		clearField("emailReferenteTerzo");
	}

}

function addAttributesReferenteTerzo(){
	$("#prefissoInternazionaleTelefonoReferenteTerzo").attr("onlynum","true");
	$("#prefissoInternazionaleTelefonoReferenteTerzo").attr("maxLength",6);

	$("#prefissoTelefonoReferenteTerzo").attr("onlynum","true");
	$("#prefissoTelefonoReferenteTerzo").attr("maxLength", 5);

	$("#telefonoReferenteTerzo").attr("onlynum","true");
	$("#telefonoReferenteTerzo").attr("maxLength", 12);

	$("#emailReferenteTerzo").attr("maxLength", 50);

	
	$("#nomeReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#nomeReferenteTerzo").attr("maxLength", 80);


	$("#cognomeReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#cognomeReferenteTerzo").attr("maxLength", 80);


	$("#codiceFiscaleReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	$("#toponimoIndirizzoReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	$("#indirizzoReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});
	
	$("#indirizzoReferenteTerzo").attr("maxLength", 60);


	$("#numeroCivicoIndirizzoReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	$("#numeroCivicoIndirizzoReferenteTerzo").attr("maxLength", 4);
	
	$("#comuneReferenteTerzo").attr("maxLength", 50);


	$("#provinciaComuneReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	$("#nazioneComuneReferenteTerzo").bind({
		keyup : function(event) {
			this.value = this.value.toUpperCase();
		}
	});

	
	
}


	