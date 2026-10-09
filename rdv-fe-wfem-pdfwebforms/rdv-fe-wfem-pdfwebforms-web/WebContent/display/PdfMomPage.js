try{
	if(window.addEventListener){
		addEventListener("message", receiveMomEvent, false);
	}else{
		attachEvent("onmessage", receiveMomEvent);	
	}
}catch(e){}

function tryConsoleLog(msg){
	try{
		if(typeof console !== "undefined" && typeof console.log === "function")
		    console.log(msg);
	}catch(e){}
}

function receiveMomEvent(event){
	try{
		var chdata = JSON.parse(event.data);
		var evdata = chdata.channelMsgData;
		if(chdata.channelMsgName == "PdfWebForms.SalvaEValidaDocumentoMomEvent"){
			
			var objForm = document.dati;
			objForm.momEventData_azioneMom.value = evdata["azione-mom"];
			objForm.momEventData_readonly.value = evdata["read-only"];
			objForm.momEventData_valida.value = evdata.valida;
			objForm.momEventData_salva.value = evdata.salva;
			objForm.momEventData_aggiornaDispositiva.value = evdata["aggiorna-dispositiva"];
			objForm.momEventData_gotoPdf.value = evdata["goto-pdf"]?evdata["goto-pdf"]:"";
			objForm.momEventData_idPratica.value = evdata["id-pratica"]?evdata["id-pratica"]:"";
			objForm.momEventData_confrontaDoppiaSpunta.value = evdata["confronta-doppia-spunta"]?evdata["confronta-doppia-spunta"]:"";
			var parametri = evdata.parametri;
			if(parametri && parametri.length > 0){
				for(var i=0; i< parametri.length; i++){
					var parametro = evdata.parametri[i];
					var nome = parametro.parametro;
					var valore = parametro.valore;
					if(valore == null)
						valore = "";

					var sk = document.createElement("input");
				    sk.setAttribute("type", "hidden");
				    sk.setAttribute("name", "momEventData_parametri"+i+"_nomeValore");
				    sk.setAttribute("value", nome+"|"+valore);
				    objForm.appendChild(sk);
				}				
			}
			doCallMomEvent();
			
		}else if(chdata.channelMsgName == "PdfWebForms.RicercaClienteMomCallbackEvent"){
			
			var ndg = "ndgCliente"+evdata.indiceCliente;
			var indg = parseInt(evdata.ndgCliente, 10);
			pdf.setFieldValue(ndg, ""+indg);
			try{
				if(typeof(evdata.indiceCliente) === "string")
					evdata.indiceCliente = parseInt(evdata.indiceCliente, 10);
			}catch(e){}
			doRicercaClienteMomCallbackEvent(evdata.ndgCliente, evdata.indiceCliente);
			
		}
		
	}catch(e){tryConsoleLog("error in receive MOM event-> "+e.message);}
}

function doCallMomEvent(){
	wait();
	submitPdfData("prgm.pdfwebforms.mom.CallMomEvent",doCallMomEvent);
	return false;
}

function doRicercaClienteMomCallbackEvent(ndgCliente,indiceCliente){
	wait();	
	$.ajax({
		url: "call.wfem?wfemCmd=prgm.pdfwebforms.mom.RicercaClienteMomCallbackEvent.executeOnPopup&readRequest=false&ndg="+ndgCliente,
		dataType: "json",
		async: true,
		success: function(data) {
									endWait();
									if(typeof(data.ndg) == "undefined"){
										pdfPageDataentryUtil.clearPersonData("Cliente"+indiceCliente);
									}else{
										pdfPageDataentryUtil.setPersonData(data, "Cliente"+indiceCliente);
									}
									var fieldObj = document.getElementById("ndgCliente"+indiceCliente);
									if(pdfPageFields.dispatchChangePersonEvent(fieldObj, indiceCliente, data))
										pdfPageFields.dispatchChangeEvent(fieldObj, data);									
								}
	});	
	return false;
}

function callMomCallbackEvent(dataObj){
	try{
		var chdata = {
			    "msgXchanger": true,
		        "channelMsgName": "PdfWebForms.SalvaEValidaDocumentoMomCallbackEvent",
		        "channelMsgData":  dataObj
		};
		var schdata = JSON.stringify(chdata);
		top.postMessage(schdata,"*");
	}catch(e){
		alert("Error sending SalvaEValidaDocumentoMomCallbackEvent: "+e.message);
	}
}

function callRicercaClienteMomEvent(cliIdx){
	try{
		var chdata = {
			    "msgXchanger": true,
		        "channelMsgName": "PdfWebForms.RicercaClienteMomEvent",
		        "channelMsgData":  {
		        	"indiceCliente" : ""+cliIdx
		        }
		};
		var schdata = JSON.stringify(chdata);
		top.postMessage(schdata,"*");
	}catch(e){
		alert("Error sending RicercaClienteMomEvent: "+e.message);
	}
}

function callPopolamentoAgenteMomEvent(codiceAgente){
	try{
		var chdata = {
			    "msgXchanger": true,
		        "channelMsgName": "PdfWebForms.PopolamentoAgenteMomEvent",
		        "channelMsgData":  {
		        	"codiceAgente" : codiceAgente
		        }
		};
		var schdata = JSON.stringify(chdata);
		top.postMessage(schdata,"*");
	}catch(e){}
}

function callPopolamentoClienteMomEvent(indiceCliente, ndgCliente){
	if(indiceCliente <= 0)
		return;
	try{
		var chdata = {
			    "msgXchanger": true,
		        "channelMsgName": "PdfWebForms.PopolamentoClienteMomEvent",
		        "channelMsgData":  {
		        	"indiceCliente" : ""+indiceCliente,
		        	"ndgCliente" : ndgCliente
		        }
		};
		var schdata = JSON.stringify(chdata);
		top.postMessage(schdata,"*");
	}catch(e){}
}
