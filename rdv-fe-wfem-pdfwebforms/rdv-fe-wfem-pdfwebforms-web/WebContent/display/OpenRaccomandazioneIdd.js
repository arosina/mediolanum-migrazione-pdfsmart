function openRaccomandazioneIdd(link){
	$.ajax({
		url: "call.wfem?wfemCmd=prgm.pdfwebforms.idd.GetIdEcmRaccomandazioneIdd.execute&BrowserInstance="+__getBrowserInstance()+"&checkCodDescFields=false&manageChangedFields=false",
		dataType: "json",
		async: true,
		success: function(data) {
			if(data.errMsg != ""){
				alert(data.errMsg);
				return;
			}
			if(link.getAttribute("isRaccomandazioneIddClicked") != "true")			
				link.setAttribute("isRaccomandazioneIddClicked", data.isRaccomandazioneIddClicked);
			window.open("call.wfem?wfemCmd=prgm.pdfwebforms.idd.GetPdfRaccomandazioneIdd.executeOnPopup&idEcmRaccomandazioneIdd="+data.idEcmRaccomandazioneIdd);
		}
	});	
}
