$( document ).ready(function() {
	
	$(".normalButton").attr({"tabindex":"0", "role":"button"});
	
	try{
		$("[onclick]").bind({
			keypress: function(event){
				if(event.which == 13 || event.which == 32){
					this.click();
				}
			}			
		});
	}catch(e){}

});

function doPrevPdf(){
	wait();
	document.dati.wfemCmd.value="prgm.pdfwebforms.business.GotoPrevPdf.execute";
	document.dati.submit();
	return false;
}

function doContinua(){
	
	var msg1 = "";
	if(document.dati.pdfData_priipsTipoSupportoMaterialeContrattuale && document.dati.pdfData_priipsTipoSupportoMaterialeContrattuale.value === "")
		msg1 = "E' necessario indicare la modalità di consegna della documentazione<br><br>";
	var msg2= "";
	if(document.dati.pdfData_priipsTolleranzaVolatilita && document.dati.pdfData_priipsTolleranzaVolatilita.value === "")
		msg2 = "E' necessario indicare la tolleranza alla volatilità e aspettativa di rendimento<br><br>";
	var msg3= "";
	if(document.dati.pdfData_priipsOrizzonteTemporale && document.dati.pdfData_priipsOrizzonteTemporale.value === "")
		msg3 = "E' necessario indicare l'orizzonte temporale indicativo<br><br>";
	
	if(msg1 != "" || msg2 != "" || msg3 != ""){
		$("#errorMsg").html(msg1+msg2+msg3);
		$("#errorReportMessage").dialog({
			autoOpen: true, 
			modal: true,
			width: 450,
			height: 300,
			closeOnEscape: false,
			title: "Avviso"
		   });
		return;
	}
	wait();
	document.dati.submit();
	return false;
}

