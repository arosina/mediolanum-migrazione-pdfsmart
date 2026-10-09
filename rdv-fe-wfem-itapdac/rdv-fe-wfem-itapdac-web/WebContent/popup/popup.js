var itaPDacParSeparator = "%09";
var itaPDacEqIndicator = "%3d";
var itaPDacUrlPrefix = __retrieveGatewayUrl()+"call.wfem?wfemCmd=showPage&page=ita/p/dac/popup/display/PopupIFrame.jsp";

function showPopupContratti(numeroContratto){
	freeze();
	openModalPopup("prgm.ita.p.dac.popup.display.PopupContratti","copyFields=false&ufficio="+document.datiReadDocumento.documento_ufficio.value,null,showPopupContrattiEnd,"Cerca contratto",500,850);
	unfreeze();
}

function showPopupClienti(){
	freeze();
	openModalPopup("prgm.ita.p.dac.popup.display.PopupClienti","copyFields=false&ufficio="+document.datiReadDocumento.documento_ufficio.value,null,showPopupClientiEnd,"Cerca cliente",600,850);
	unfreeze();
}

function showPopupAgenti(){
	freeze();
	openModalPopup("prgm.ita.p.dac.popup.display.PopupAgenti","copyFields=false&ufficio="+document.datiReadDocumento.documento_ufficio.value,null,showPopupAgentiEnd,"Cerca agente",500,600);
	unfreeze();
}

function showPopupMsg(type,msg,callback){
	var input = new Object();
	input.type = type;
	input.msg = msg;

	if(callback){
		openModalPopup("prgm.ita.p.dac.popup.display.Message",null,input,callback,"Avviso",225,300);
	}else{
		freeze();
		var ret = showModalDialog(__retrieveResourceUrl()+"/popup/Message.html",input,"scroll:no;status:no;dialogHeight:200px;dialogWidth:300px;");
		if(ret == null || typeof(ret) == 'undefined')
			ret = 'cancel';
		unfreeze();
		return ret;
	}
}
