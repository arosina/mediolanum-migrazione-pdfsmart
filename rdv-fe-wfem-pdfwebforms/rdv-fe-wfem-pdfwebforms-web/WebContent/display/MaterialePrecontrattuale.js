// Materiale precontrattuale obbligatorio
function openMaterialePrecontrattualeDialog(onDataentry){
	openMyDialog("materialePrecontrattualeDialog");
	if(!onDataentry){
		try{
			$.ajax({
				url: "call.wfem?wfemCmd=prgm.pdfwebforms.materialeprecontrattuale.TracciaPresaVisioneDiTutti.execute&BrowserInstance="+__getBrowserInstance()+"&timenow="+new Date().getTime(),
				dataType: "json",
				async: true
			});
		}catch(e){}	
	}	
}
function confirmMaterialePrecontrattualeDialog(){
	closeMyDialog("materialePrecontrattualeDialog");
	setLinkClicked("materialePrecontrattualeClicked","linkMaterialePrecontrattualeAccessorio");
}
function openMaterialePrecontrattuale(url, title, ndg, idCarrello, pdfId){
	var originalUrl = url;
	var tagDominio = "###DOMINIO_WFEM###/prgm/";
	if(url.startsWith(tagDominio))
		url = url.replace(tagDominio,"");
	window.open(url);
	if(ndg === "")
		return;
	try{
		$.ajax({
			url: "call.wfem?wfemCmd=prgm.pdfwebforms.materialeprecontrattuale.TracciaPresaVisione.execute&readRequest=false&BrowserInstance="+__getBrowserInstance()+"&timenow="+new Date().getTime(),
			dataType: "json",
			data: { "url": originalUrl, "title": title, "ndg": ndg, "idCarrello": idCarrello, "pdfId": pdfId, "clickType": "link" },
			async: true
		});	
	}catch(e){}	
}

// Materiale precontrattuale accessorio
function openMaterialePrecontrattualeAccessorioDialog(){
	openMyDialog("materialePrecontrattualeAccessorioDialog");
}
function confirmMaterialePrecontrattualeAccessorioDialog(){
	closeMyDialog("materialePrecontrattualeAccessorioDialog");
	setLinkClicked("materialePrecontrattualeAccessorioClicked");
}
function openMaterialePrecontrattualeAccessorio(url){
	var tagDominio = "###DOMINIO_WFEM###/prgm/";
	if(url.startsWith(tagDominio))
		url = url.replace(tagDominio,"");
	window.open(url);
}

function setLinkClicked(elId, putOn){
	var focusOnCurrent = true;
	var clicked = document.getElementById(elId);
	if(clicked != null){
		if(clicked.value != "true")
			focusOnCurrent = false;
		clicked.value = "true";
	}	
	try{
		if(focusOnCurrent)
			curFocus.focus();
		else
			manageFocus(putOn);
	}catch(e){}
}

/*
RFC 223045: Inizialmente è stato chiesto di tracciare anche la presa visione. Sucessivamente è stato chesto di eliminare questo tipo di tracciatura.
            Per farlo ho eliminato l'onClick sulla checkbox lasciando il metodo anche se non più utilizzato così se dovesse essere necessario 
            ripristinarlo basta inserire la riga sotto nei primi doppi apici del template.field del campo "presaVisioneMaterialePrecontrattuale"
            
 			onclick=\"confermaPresaVisioneMaterialePrecontrattuale(this,'"+personaCorrente.getNdg()+"','"+model.getPdfData().getPdfInstanceId()+"');\"
 
*/
function confermaPresaVisioneMaterialePrecontrattuale(check, ndg, pdfId){
	if(!check.checked)
		return;
	try{
		$.ajax({
			url: "call.wfem?wfemCmd=prgm.pdfwebforms.materialeprecontrattuale.TracciaPresaVisione.execute&readRequest=false&BrowserInstance="+__getBrowserInstance()+"&timenow="+new Date().getTime(),
			dataType: "json",
			data: { "ndg": ndg, "pdfId": pdfId, "clickType": "presavisione" },
			async: true
		});	
	}catch(e){}	
}
