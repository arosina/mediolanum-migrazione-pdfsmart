function startWait() {
	wait();
}

function stopWait() {
	endWait();
}

function setTitolo(titolo) {
}

function onLoadPopupErrore() {
	sendToNmol('errore');
}

function onClosePopupErrore() {
	sendToNmol('indietro');
}

function sendIndietro() {
	sendToNmol('indietro');
}

function sendFine(dispoOkArray, dispoKoArray) {
	sendToNmol('fine',dispoOk,dispoKo);
}

function sendToNmol(nomeEvento,dispoOk,dispoKo){
	if(!dispoOk)
		dispoOk = new Array();
	if(!dispoKo)
		dispoKo = new Array();
	try{
		var chdata = {
			    "msgXchanger": true,
		        "channelMsgName": "PdfWebForms.Nmol",
		        "channelMsgData":  {
		        	"event" : ""+nomeEvento,
		        	"dispoOk" : dispoOk,
		        	"dispoKo" : dispoKo
		        }
		};
		var schdata = JSON.stringify(chdata);
		parent.postMessage(schdata,"*");
	}catch(e){}
}

try{
	if(window.addEventListener){
		addEventListener("message", receiveNmolEvent, false);
	}else{
		attachEvent("onmessage", receiveNmolEvent);	
	}
}catch(e){}

function receiveNmolEvent(event){
}

