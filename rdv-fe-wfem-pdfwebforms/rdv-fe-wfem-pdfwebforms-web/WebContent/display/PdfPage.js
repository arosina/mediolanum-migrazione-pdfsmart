var submitDelay = 100;
var changeRunning = false;
var afterChangeFnc = null;

function submitPdfData(cmd, afterChangeFuncion){
	setTimeout(function() {
		if(changeRunning && afterChangeFuncion){
			afterChangeFnc = afterChangeFuncion;
			return;
		}
		document.dati.reloadPageOnEvent.value = "true";
		document.dati.eventName.value = "";
		document.dati.wfemCmd.value = cmd+".execute";
		document.dati.submit();
	}, submitDelay);		
}

function stopEventPropagation(event){
	if(event != null){
		if(isIE()) window.event.cancelBubble = true; else event.stopPropagation();
	}
}

function doVisualizzaCompilationExample(obj,pdfId){
	var cmd = "prgm.pdfwebforms.catalog.OpenCompilationExample.execute";
	var url = "call.wfem?wfemCmd="+cmd+"&pdfId="+pdfId+"&BrowserInstance="+__getBrowserInstance();
	if(obj.getAttribute("contentType").indexOf("application/pdf") >= 0){
		if(navigator.userAgent.indexOf('iPad') != -1){
			window.open(url);
		}else{
			openPdfObject(url,"Esempio di compilazione");
		}
	}else{
		document.getElementById("utilIFrame").src = url;
	}
}

function doShowdata(){
	wait();
	submitPdfData("prgm.pdfwebforms.display.PdfResultData",doShowdata);
	return false;
}

function doSalva(){
	if(jsErrorOnSomePerson){
		alert("Non è possibile procedere in quanto uno o più clienti non risultano anagrafati");
		return false;
	}
	wait();
	submitPdfData("prgm.pdfwebforms.business.SavePdf",doSalva);
	return false;
}

function doGobackToPriips(){
	wait();
	submitPdfData("prgm.pdfwebforms.business.GotoPriips",doGobackToPriips);
	return false;
}

function doPrevPdf(){
	wait();
	submitPdfData("prgm.pdfwebforms.business.GotoPrevPdf",doPrevPdf);
	return false;
}

function doConferma(){
	if(jsErrorOnSomePerson){
		alert("Non è possibile procedere in quanto uno o più clienti non risultano anagrafati");
		return false;
	}
	wait();
	submitPdfData("prgm.pdfwebforms.business.ConfirmPdf",doConferma);
	return false;
}

function doProsegui(){
	$('#errorReportMessage').dialog('destroy');
	document.dati.skipCommandWarnings.value = "true";
	doConferma();
}

function doZoomIn(){
	wait();
	submitPdfData("prgm.pdfwebforms.business.ZoomIn",doZoomIn);
	return false;
}

function doZoomNormal(){
	wait();
	submitPdfData("prgm.pdfwebforms.business.ZoomNormal",doZoomNormal);
	return false;
}

function doZoomOut(){
	wait();
	submitPdfData("prgm.pdfwebforms.business.ZoomOut",doZoomOut);
	return false;
}

function doPositionHelper(event, obj){
	var parentOffset = $(obj).offset();
	var pageNum = $(obj).attr("pageNum");
	var y = event.pageY - parentOffset.top;
	var x = event.pageX - parentOffset.left;
	$("#positionHelper").html("position: [ "+pageNum+", "+x+", "+y+" ]");
}

var curMarker = null;
function showErrMarkers(){
	curMarker = null;
	var errhtml="";
	var markerHeight = $("#errorsMarkers").height();
	var pdfHeight = $("#pdfPagesCont").height();
	var numErrs = 0;
	$(".pdfFieldHasError").each(function(index){
		var fieldName = this.getAttribute("name");
		fieldName = fieldName.substring(0,fieldName.length-"ContBorder".length);
		var fpage = parseInt(this.getAttribute("page"),10);
		var fvisible = jsVisiblePagesArray.length == 0 || jsVisiblePagesArray.includes(fpage);
		if($("div[name='"+fieldName+"Cont']").css("display") !== "none" && fvisible){
			$("#errorsMarkersTitle").show();
			var pdfFieldTop = this.getAttribute("top");
			var markerFieldTop = ((pdfFieldTop / pdfHeight) * markerHeight) | 0;
			var fid = this.getAttribute("fid");
			errhtml += "<div name='"+fieldName+"ErrMarker' fid='"+fid+"' "+
							"pdfFieldTop="+pdfFieldTop+" class='errMarker totErrMarker' style='top:"+markerFieldTop+"px;' "+
							"onclick='gotoErrMarker(this);'>"+
							"&#9658;"+
						"</div>";
			numErrs++;
		}
	});
	$("#errorsMarkers").html(errhtml);
	if(numErrs === 0){
		$("#numErrorsMarkers").hide();
		$("#navigateErrorsMarkers").hide();
	}else{		
		$("#numErrorsMarkers").html("Correzione errori ("+numErrs+"/"+numErrs+" errori)").show();
		$("#navigateErrorsMarkers").show();
	}
}

function gotoErrMarker(marker){
	var fid = marker.getAttribute("fid");
	var fobj = document.getElementById(fid);
	if(fobj == null)
		fobj = document.getElementById(fid+"Combo");
	if(fobj == null)
		fobj = document.getElementById(fid+"Check");
	if(fobj == null)
		fobj = document.getElementById(fid+"Radio");
	if(fobj == null)
		return;
	curMarker = marker;
	var pos = parseInt(marker.getAttribute("pdfFieldTop"),10)-120;
	$("#pagesCont").animate({scrollTop:pos},'50','swing', function(){ fobj.focus(); });
}

function gotoPriorErrMarker(){
	var errs = $(".errMarker").toArray();
	if(curMarker == null){
		curMarker = errs[errs.length-1];
	}else{
		var i=0;
		for(;i<errs.length;i++){
			if(curMarker.getAttribute("fid") === errs[i].getAttribute("fid"))
				break;
		}
		if(i > 0)
			curMarker = errs[i-1];
		else
			curMarker = errs[errs.length-1];
	}
	gotoErrMarker(curMarker);
}

function gotoNextErrMarker(){
	var errs = $(".errMarker").toArray();
	if(curMarker == null){
		curMarker = errs[0];
	}else{
		var i=0;
		for(;i<errs.length;i++){
			if(curMarker.getAttribute("fid") === errs[i].getAttribute("fid"))
				break;
		}
		if(i < errs.length-1)
			curMarker = errs[i+1];
		else
			curMarker = errs[0];
	}
	gotoErrMarker(curMarker);
}

function sendPdfCompletedProcessEvent(pdfData){
	var chdata = {
		"msgXchanger": true,
		"channelMsgName": "PdfWebForms.PdfCompletedProcessEvent",
		"channelMsgData": pdfData
	};
	var schdata = JSON.stringify(chdata);
	try{
		if(window.opener && window.opener != null){
			window.opener.postMessage(schdata,"*");
		}
	}catch(e){}
	try{
		if(parent && parent != null){
			parent.postMessage(schdata,"*");
		}
	}catch(e){}
}
