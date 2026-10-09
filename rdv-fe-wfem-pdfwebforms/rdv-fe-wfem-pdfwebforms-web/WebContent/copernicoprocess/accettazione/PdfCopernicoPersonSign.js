function onPageLoad(){
	stopWait();
	var speakOnLoadObj = document.getElementById("speakOnLoad");
	if(speakOnLoadObj != null){
		// Scommentare per abilitare il workaround
//		speakOnLoadObj.value = " Premi tab per iniziare la navigazione.";
//	    window.setTimeout(function () {
//			speakOnLoadObj.setAttribute("aria-hidden","false");
//			speakOnLoadObj.focus();
//	    }, 100);
		// ****************************
		speakOnLoadObj.setAttribute("tabindex","-1");
	}
	$('[tabindex="-1"]').attr("aria-hidden","true");
	if(!isSomeDialogOpen)
		trapPageFocus();
	
	if(!jsIsInAccettazioneCopernicoMobile)
		setPositionToNextSign();
}

function doEnterAction(){
	return;	
}

function setPositionToNextSign(){
	var sigField = [];
	$("div[isFieldToSign='true']").each(function(index){
		var inputField = document.getElementById(this.getAttribute("fid"));
		if(inputField != null && inputField.value === 'false')
			sigField.push(this);
	});
	if(sigField.length > 0){
		sigField.sort(function(a, b){return a.getAttribute("top") - b.getAttribute("top")});
		pdfPageFields.scrollToField(sigField[0].getAttribute("fid"), 100);
	}
}

var curSignIndex=-1;
var signFields = [];
function loadSign(){
	$("div[isFieldToSign='true']").each(function(index){
		signFields.push(this);
		$(this).bind({
			click: function(event){
				document.getElementById(this.getAttribute("fid")+"Check").click();
			}
		});
	});
	signFields.sort(function(a, b){return a.getAttribute("top") - b.getAttribute("top")});
}

function scrollToNextSign(){
	if(signFields.length === 0)
		return;

	if(curSignIndex >= signFields.length)
		curSignIndex = 0;

	curSignIndex++;
	if(curSignIndex >= signFields.length)
		curSignIndex = 0;
	
	document.getElementById(signFields[curSignIndex].getAttribute("fid")+"Check").focus();
	//scrollToField(signFields[curSignIndex].getAttribute("fid"), 100);
}

function scrollToPriorSign(){
	if(signFields.length === 0)
		return;

	if(curSignIndex < 0)
		curSignIndex = signFields.length;
	
	curSignIndex--;
	if(curSignIndex < 0)
		curSignIndex = signFields.length-1;
	
	document.getElementById(signFields[curSignIndex].getAttribute("fid")+"Check").focus();
	//scrollToField(signFields[curSignIndex].getAttribute("fid"), 100);
}

function scrollToField(fieldName, relativePos){
	try{
		var fobj = $("div[fid='"+fieldName+"']");
		var pdfFieldTop = fobj.attr("top");
		if(typeof relativePos == "undefined")
			relativePos = 50;		
		var h = $("#arrow").outerHeight();
		var pos = parseInt(pdfFieldTop,10)-h-relativePos;
		$("#pagesCont").animate({scrollTop:pos, scrollLeft: fobj.position().left-20},'50','swing');
	}catch(e){}
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
	var ch = event.which || event.keyCode;
	if(ch < '0'.charCodeAt(0) || ch > 'z'.charCodeAt(0))
		return;
	if(field.value.length != 1)
		return;
	try{
		document.getElementById('personaCorrente_signData_otpDigitato').focus();
		document.getElementById('personaCorrente_signData_otpDigitato').select();
	}catch(e){}
}

function goOnCopernicoSign(){
	startWait();
	if(jsIsInBasket)
		document.dati.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.accettazione.GoOnCopernicoSignBasketProcess.execute";
	else
		document.dati.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.accettazione.GoOnCopernicoSignProcess.execute";
	document.dati.submit();
	return;
}

function doVerificaPin2Otp(){
	startWait();
	if(jsIsInBasket)
		document.dati.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.accettazione.GoOnCopernicoSignBasketProcess.execute";
	else
		document.dati.wfemCmd.value = "prgm.pdfwebforms.copernicoprocess.accettazione.GoOnCopernicoSignProcess.execute";
	document.dati.submit();
	return;
}

function doZoomIn(){
	startWait();
	document.dati.scrollYValue.value = 0;
	document.dati.scrollXValue.value = 0;
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.ZoomIn.execute";
	document.dati.submit();
	return false;
}

function doZoomNormal(){
	startWait();
	document.dati.scrollYValue.value = 0;
	document.dati.scrollXValue.value = 0;
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.ZoomNormal.execute";
	document.dati.submit();
	return false;
}

function doZoomOut(){
	startWait();
	document.dati.scrollYValue.value = 0;
	document.dati.scrollXValue.value = 0;
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.ZoomOut.execute";
	document.dati.submit();
	return false;
}

function richiediNuovoOtp(){
	wait();
	document.dati.wfemCmd.value = "prgm.pdfwebforms.signprocess.business.RichiediNuovoOtp.execute";
	wfemHiddenSubmit(document.dati,"otpCont");
}

function onWfemHiddenSubmitEnd(objId){
	endWait();
	if(objId == "otpCont")
		$("#personaCorrente_signData_otpDigitato").focus();
}

