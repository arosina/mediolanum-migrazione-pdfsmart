function startWait() {
	sendToMobileApp('startLoading');
}

function stopWait() {
	sendToMobileApp('stopLoading');
}

function setTitolo(titolo) {
	sendToMobileApp('setTitle', { title: titolo });
}

function onLoadPopupErrore() {
	stopWait();
}

function onClosePopupErrore() {
	sendToMobileApp('error');
}

function sendIndietro() {
	sendToMobileApp('back');
}

function sendFine(dispoOkArray, dispoKoArray) {
	sendToMobileApp('end', { dispoOk: dispoOkArray, dispoKo: dispoKoArray });  
}

function sendToMobileApp(nomeEvento, datiEvento) {
  if(!datiEvento)
	  datiEvento = new Object();
  
  var data = {
    eventName: nomeEvento,
    eventData: datiEvento
  };

  try{
	  window.postMessage(JSON.stringify(data), "*");	  
  }catch (err) {
	  console.log("postMessage ["+ nomeEvento +"] fallito: " + err);
  }

}
