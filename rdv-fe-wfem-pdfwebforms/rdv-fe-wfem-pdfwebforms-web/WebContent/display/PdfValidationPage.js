try{
	if(window.addEventListener){
		addEventListener("message", receiveValidationEvent, false);
	}else{
		attachEvent("onmessage", receiveValidationEvent);	
	}
}catch(e){}

function receiveValidationEvent(event){
	try{
		var chdata = JSON.parse(event.data);
		var evdata = chdata.channelMsgData;
		if(chdata.channelMsgName === "PdfWebForms.CallPdfValidationEvent"){
			var objForm = document.dati;
			objForm.pdfValidationEventData_actionName.value = evdata["actionName"];
			objForm.pdfValidationEventData_doValidation.value = evdata["doValidation"];
			objForm.pdfValidationEventData_doAdeguatezza.value = evdata["doAdeguatezza"];
			doCallPdfValidationEvent();
		}
	}catch(e){alert("error-> "+e.message);}
}

function doCallPdfValidationEvent(){
	wait();
	submitPdfData("prgm.pdfwebforms.validation.CallPdfValidationEvent",doCallPdfValidationEvent);
	return false;
}

function callValidationCallbackEvent(dataObj){
	try{
		var chdata = {
			    "msgXchanger": true,
		        "channelMsgName": "PdfWebForms.CallPdfValidationEventCallback",
		        "channelMsgData":  dataObj
		};
		var schdata = JSON.stringify(chdata);
		parent.postMessage(schdata,"*");
	}catch(e){
		alert("Error sending CallPdfValidationEventCallback: "+e.message);
	}
}
