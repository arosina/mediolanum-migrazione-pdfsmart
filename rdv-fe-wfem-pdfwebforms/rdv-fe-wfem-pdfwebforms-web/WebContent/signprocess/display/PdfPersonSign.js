function onPageLoad(){

	$("select[htmltype='combo']").bind({ // Disable all combobox
		mousedown: function(event){
			event.preventDefault();
		}
	});		

	var speakOnLoad = null;
	var speakOnLoadObj = document.getElementById("speakOnLoad");
	if(speakOnLoadObj != null)
		speakOnLoad = speakOnLoadObj.innerHTML;

	var pin1Digit = document.getElementById("personaCorrente_signData_digit1Digitato");
	if(pin1Digit != null){
		var errorMsg = "";
		var errorCont = document.getElementById("pinErrorMsg");
		if(errorCont != null)
			errorMsg = errorCont.innerHTML+". ";
		if(speakOnLoad == null){
			$(pin1Digit).attr("aria-label", errorMsg+$(pin1Digit).attr("aria-label"));
		}else{
			$(pin1Digit).attr("aria-label", speakOnLoad+errorMsg+$(pin1Digit).attr("aria-label"));
		}
		trapPageFocus();
		manageFocus();
		return;
	}
	
	var otp = document.getElementById("personaCorrente_signData_otpDigitato");
	if(otp != null){
		if(jsIsInBasket){ // E' aperta la popup di richiesta otp
			var errorMsg = "";
			var errorCont = document.getElementById("otpErrorMsg");
			if(errorCont != null){
				errorMsg = errorCont.innerHTML+". ";
				$(otp).removeAttr("aria-describedby").attr("aria-label",errorMsg+$("#otpDigitato-aria-label").html());
				document.getElementById("personaCorrente_signData_otpDigitato").focus();
			}
		}else{ // Siamo sulla pagina dei check firma (no basket)
			try{
				$('#presaVisioneMaterialePrecontrattualeCheck').bind({
					keypress: function(event){
						if(event.which == 13 || event.which == 32)
							this.click();
						event.preventDefault();					
					}			
				});
			}catch(e){}			
			var errorMsg = "";
			var errorCont = document.getElementById("otpErrorMsg");
			if(errorCont != null){
				errorMsg = errorCont.innerHTML+". ";
				$(otp).attr("aria-label", errorMsg+$(otp).attr("aria-label"));	
			}
		}
	}
	
	if(speakOnLoad != null){
		if(speakOnLoadObj.getAttribute("type") == "text")
			speakOnLoadObj.value=" Premi tab per iniziare la navigazione. ";
		 window.setTimeout(function () {
			 speakOnLoadObj.setAttribute("aria-hidden","false");
			 speakOnLoadObj.focus();
		 }, 100);
		speakOnLoadObj.setAttribute("tabindex","-1");
		trapPageFocus();
	}else{
		trapPageFocus();
		manageFocus();
	}
	
	$('[tabindex="-1"]').attr("aria-hidden","true");
}

function manageFocus(putOn){
	
	if(isSomeDialogOpen())
		return;
	
    window.setTimeout(function () {
    	
		var pinDigit = document.getElementById("personaCorrente_signData_digit1Digitato");
		if(pinDigit != null){
			pinDigit.select();
			pinDigit.focus();
			return;
		}	
		
		if(jsIsInBasket){
			var otpDigit = document.getElementById("personaCorrente_signData_otpDigitato");
			if(otpDigit == null){ // E' aperta la pagina dei check firma
				if(document.dati.personaCorrente_signData_leggiIlContrattoClicked.value != "true"){
					var linkLeggiIlContratto = document.getElementById("linkLeggiIlContratto");
					if(linkLeggiIlContratto != null){
						$(linkLeggiIlContratto).focus();
						return;
					}
				}
				
				$(".signFieldValue").each(function(index){
					if(this.value != "true"){
						$("#"+this.id+"Check").focus();
						return false;
					}
				});
			}else{ // E' aperta la poopup di richiesta otp
				var errorCont = document.getElementById("otpErrorMsg");
				if(errorCont != null)
					document.getElementById("personaCorrente_signData_otpDigitato").focus();
			}			
		}else{
			var otpDigit = document.getElementById("personaCorrente_signData_otpDigitato");
			if(otpDigit != null){
				
				var linkMaterialePrecontrattuale = document.getElementById("linkMaterialePrecontrattuale");
				var materialePrecontrattualeClicked = document.getElementById("materialePrecontrattualeClicked");
				if(linkMaterialePrecontrattuale != null && materialePrecontrattualeClicked != null && materialePrecontrattualeClicked.value != "true"){
					$(linkMaterialePrecontrattuale).focus();
					return;
				}
	
				if(putOn == "linkMaterialePrecontrattualeAccessorio"){
					var linkMaterialePrecontrattualeAccessorio = document.getElementById("linkMaterialePrecontrattualeAccessorio");
					var materialePrecontrattualeAccessorioClicked = document.getElementById("materialePrecontrattualeAccessorioClicked");
					if(linkMaterialePrecontrattualeAccessorio != null && materialePrecontrattualeAccessorioClicked != null && materialePrecontrattualeAccessorioClicked.value != "true"){
						$(linkMaterialePrecontrattualeAccessorio).focus();
						return;
					}
				}

				var linkReportAdeguatezza = document.getElementById("linkReportAdeguatezza");
				if(linkReportAdeguatezza != null && linkReportAdeguatezza.getAttribute("isReportAdeguatezzaClicked") != "true"){
					$(linkReportAdeguatezza).focus();
					return;
				}
	
				var linkRaccomandazioneIdd = document.getElementById("linkRaccomandazioneIdd");
				if(linkRaccomandazioneIdd != null && linkRaccomandazioneIdd.getAttribute("isRaccomandazioneIddClicked") != "true"){
					$(linkRaccomandazioneIdd).focus();
					return;
				}
				
				if(document.dati.personaCorrente_signData_leggiIlContrattoClicked.value != "true"){
					var linkLeggiIlContratto = document.getElementById("linkLeggiIlContratto");
					if(linkLeggiIlContratto != null){
						$(linkLeggiIlContratto).focus();
						return;
					}
				}
				
				var allSigned = true;
				$(".signFieldValue").each(function(index){
					if(this.value != "true"){
						$("#"+this.id+"Check").focus();
						allSigned = false;
						return false;
					}
				});
				if(!allSigned)
					return;
				
				var presaVisioneMaterialePrecontrattuale = document.getElementById("presaVisioneMaterialePrecontrattuale");
				if(presaVisioneMaterialePrecontrattuale != null && presaVisioneMaterialePrecontrattuale.value != "true"){
					$("#presaVisioneMaterialePrecontrattualeCheck").focus();
					return;
				}
				
				$(otpDigit).focus();
			}
		}
		
    }, 50);	
}

function doEnterAction(){
	return;	
}

function onKeyupPin1(event,field){
	var ch = event.which || event.keyCode;
	if(ch < '0'.charCodeAt(0) || ch > 'z'.charCodeAt(0))
		return;
	if(field.value.length != 1)
		return;
	try{
		document.getElementById('personaCorrente_signData_digit2Digitato').focus();
		document.getElementById('personaCorrente_signData_digit2Digitato').select();
	}catch(e){}
}

function onKeyupPin2(event,field){
	// Se prospect il fuoco rimane sul pin2
	if(document.getElementById("richiediNuovoPinProvvisorioLink") != null)
		return;
	
	var ch = event.which || event.keyCode;
	if(ch < '0'.charCodeAt(0) || ch > 'z'.charCodeAt(0))
		return;
	if(field.value.length != 1)
		return;
	try{
		document.getElementById('avantiButton').focus();
	}catch(e){}
}

function richiediNuovoPinProvvisorio(){
	wait();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.RichiediNuovoPinProvvisorio.execute";
	document.dati.submit();
}

function doVerificaPin(){
	wait();
	if(jsIsInBasket)
		document.dati.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.GoOnBasketSignProcess.execute";
	else
		document.dati.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.GoOnSignProcess.execute";
	document.dati.submit();
	return;
}

function doVerificaOtp(){
	
	var materialePrecontrattualeClicked = document.getElementById("materialePrecontrattualeClicked");
	if((materialePrecontrattualeClicked != null && materialePrecontrattualeClicked.value !== "true")){
		showSignAlert("Per proseguire è necessario cliccare sul link del materiale precontrattuale e poi prenderne visione");
		return;
	}

	var linkReportAdeguatezza = document.getElementById("linkReportAdeguatezza");
	if(linkReportAdeguatezza != null){
		if(linkReportAdeguatezza.getAttribute("statoReportAdeguatezza") == "WAIT"){
			showSignAlert("Attendere il completamento dell'elaborazione del Report di Adeguatezza");
			return;
		}else if(linkReportAdeguatezza.getAttribute("statoReportAdeguatezza") == "ERROR"){
			showSignAlert($("#linkReportAdeguatezza").html());
			return;
		}
		if(linkReportAdeguatezza.getAttribute("isReportAdeguatezzaClicked") != "true"){
			showSignAlert("Per proseguire è necessario prima stampare il Report di Adeguatezza");
			return;
		}
	}

	var linkRaccomandazioneIdd = document.getElementById("linkRaccomandazioneIdd");
	if(linkRaccomandazioneIdd != null){
		if(linkRaccomandazioneIdd.getAttribute("isRaccomandazioneIddClicked") != "true"){
			showSignAlert("Per proseguire è necessario prima prendere visione della Raccomandazione Personalizzata");
			return;
		}
	}

	if(document.dati.personaCorrente_signData_leggiIlContrattoClicked.value != "true"){
		showSignAlert("Per proseguire è necessario prima leggere il contratto");
		return;
	}

	var allSigned = true;
	$(".signFieldValue").each(function(index){
		if(this.value != "true"){
			allSigned = false;
			return false;
		}
	});
	
	var presaVisioneMaterialePrecontrattuale = document.getElementById("presaVisioneMaterialePrecontrattuale");
	if(presaVisioneMaterialePrecontrattuale != null && presaVisioneMaterialePrecontrattuale.value !== "true" && allSigned){
		showSignAlert("Non è possibile proseguire senza aver preso visione del materiale precontrattuale");
		return;
	}
	
	if(!allSigned){
		showSignAlert("Selezionare tutte le firme");
		return;
	}
	
	wait();
	var formObj = document.dati;
	if(jsIsInBasket){
		if(document.getElementById("otpForm") != null){
			formObj = document.otpForm;
			formObj.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.GoOnBasketSignProcess.execute";
		}else{
			formObj.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.GoOnBasketSignProcess.execute";
		}
	}else{
		formObj.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.GoOnSignProcess.execute";
	}
	formObj.submit();
	return;
}

function doGotoPreview(){
	wait();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.GotoPreview.execute";
	document.dati.submit();
	return;
}

function doZoomIn(){
	wait();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.ZoomIn.execute";
	document.dati.submit();
	return false;
}

function doZoomNormal(){
	wait();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.ZoomNormal.execute";
	document.dati.submit();
	return false;
}

function doZoomOut(){
	wait();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.ZoomOut.execute";
	document.dati.submit();
	return false;
}

function openLinkUrl(url){
	window.open(url);
}

function richiediNuovoOtp(){
	wait();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.RichiediNuovoOtp.execute";
	wfemHiddenSubmit(document.dati,"otpCont");
}

function onWfemHiddenSubmitEnd(objId){
	endWait();
	if(objId == "pinCont")
		$("#personaCorrente_signData_digit1Digitato").focus();
	else if(objId == "otpCont")
		$("#personaCorrente_signData_otpDigitato").focus();
}

function openRaccomandazioneIddOnSign(link){
	if(jsTipoPriips === isTIPO_PRIIPS_PREVIDENZA){
		openMyDialog("pdfPriipsPrevidenzaAlert",false,"pdfPriipsPrevidenzaAlertOkButton");
	}else{
		manageFocusRaccomandazioneIdd();
		return openRaccomandazioneIdd(link);
	}
}
function confirmOpenRaccomandazioneIddOnSign(){
	closeMyDialog('pdfPriipsPrevidenzaAlert');
	manageFocusRaccomandazioneIdd();
	openRaccomandazioneIdd(document.getElementById("linkRaccomandazioneIdd"));
}
function manageFocusRaccomandazioneIdd(){
	var focusOnCurrent = true;
	var linkRaccomandazioneIdd = document.getElementById("linkRaccomandazioneIdd");
	if(linkRaccomandazioneIdd != null){
		if(linkRaccomandazioneIdd.getAttribute("isRaccomandazioneIddClicked") != "true")
			focusOnCurrent = false;
	}
	try{
		if(focusOnCurrent)
			linkRaccomandazioneIdd.focus();
		else
			manageFocus();
	}catch(e){}
}

function openCatalogPdf(img){
	var codProd = img.getAttribute("codProd");
	window.open("call.wfem?wfemCmd=prgm.pdfwebforms.business.PrintPdf.executeProcessOnNewStack"+
				"&pdfTitle=Modulo&doMultipleCopiesOnPrintPdf=false&facSimileLabelOnPrintPdf=fac simile&"+codProd);
}

function showLeggiIlContrattoAlert(){
	openMyDialog("leggiContrattoAlertMessage",true,"leggiIlContrattoOkButton");
}
function confirmLeggiIlContrattoAlert(){
	var curValue = document.dati.personaCorrente_signData_leggiIlContrattoClicked.value;
	document.dati.personaCorrente_signData_leggiIlContrattoClicked.value = "true";
	closeMyDialog('leggiContrattoAlertMessage');
	manageFocusLeggiIlContratto(curValue);
	window.open("call.wfem?wfemCmd=prgm.pdfwebforms.stream.LeggiIlContrattoInFirma.execute&BrowserInstance="+document.dati.BrowserInstance.value,"Contratto");
}
function manageFocusLeggiIlContratto(curValue){
	var focusOnCurrent = true;
	var linkLeggiIlContratto = document.getElementById("linkLeggiIlContratto");
	if(linkLeggiIlContratto != null){
		if(curValue != "true")
			focusOnCurrent = false;
	}
	try{
		if(focusOnCurrent)
			linkLeggiIlContratto.focus();
		else
			manageFocus();
	}catch(e){}
}

function showSignAlert(msg){
	$("#signAlertMessageText").html(msg);
	openMyDialog("signAlertMessage",true,"signAlertMessageOkButton");
}
function closeSignAlert(){
	closeMyDialog('signAlertMessage');
	manageFocus();
}
