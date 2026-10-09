function openReportsAdeguatezza(linksReportAdeguatezza){
	if(linksReportAdeguatezza.length == 0){
		alert("Il servizio di generazione del Report di Ageduatezza non ha prodotto alcun elemento");
		return false;
	}
	for(var i=0;i<linksReportAdeguatezza.length;i++){
		if(linksReportAdeguatezza[i].idECM == null || linksReportAdeguatezza[i].idECM == ""){
			alert("Il servizio di generazione del Report di Ageduatezza non ha prodotto un link valido per almeno un elemento");
			return false;
		}
	}
	
	var error = false;
	var openCmd = "call.wfem?wfemCmd=prgm.pdfwebforms.reportadeguatezza.GetPdfReportAdeguatezza.executeOnPopup&idEcmReportAdeguatezza=";
	var rdaMsgObj = document.getElementById("avvisoReportAdeguatezzaMessage");
	if(rdaMsgObj != null){ // Siamo nell'invio in sede: si mostra la popup per ricordare la stampa
		$(rdaMsgObj).dialog({
			autoOpen: true, 
			modal: true,
			width: 350,
			height: 200,
			closeOnEscape: false,
			title: "Avviso",
			close: function( event, ui ) {
				for(var i=0;i<linksReportAdeguatezza.length;i++){
					try{
						window.open(openCmd+linksReportAdeguatezza[i].idECM);
					}catch(e){
						error = true;
						alert("Errore nell'apertura del Report di Adeguatezza con id: "+linksReportAdeguatezza[i].idECM+": "+e.message);
					}
				}
			}
	    });
	}else{
		for(var i=0;i<linksReportAdeguatezza.length;i++){
			try{
				window.open(openCmd+linksReportAdeguatezza[i].idECM);
			}catch(e){
				error = true;
				alert("Errore nell'apertura del Report di Adeguatezza con id: "+linksReportAdeguatezza[i].idECM+": "+e.message);
			}
		}
	}
	return !error;
}

function showAlertErroreReportAdeguatezza(msg){
	$("#errorReportAdeguatezzaMessageText").html(msg);
	$("#errorReportAdeguatezzaMessage").dialog({
		autoOpen: true, 
		modal: true,
		width: 350,
		height: 200,
		closeOnEscape: false,
		title: "Avviso"
    });
}
