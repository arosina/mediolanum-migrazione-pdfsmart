function _log(s){
	try{document.getElementById("log").value = document.getElementById("log").value+s+"\n";}catch(e){}
}

/* ************************************************************************************ */
/* Public callback methods called from framework */
/* ************************************************************************************ */
function openApplicationManual(manualId,manualTd){
	var url= __retrieveGatewayUrl()+"call.wfem?wfemCmd=loadApplicationManual&applicationManualId="+manualId;
	var openWindow = window.open("about:blank","","titlebar=yes,scrollbars=yes,resizable=yes");
	openWindow.document.write("<html><head>");
	if(manualTd)
		openWindow.document.write("<title>"+manualTd.innerHTML+"</title>");
	else
		openWindow.document.write("<title>Manuale operativo</title>");
	openWindow.document.write("</head>");
	openWindow.document.write("<body style='margin:0; padding:0;'>");
	openWindow.document.write("<iframe width='100%;' height='100%' src='"+url+"'/>");
	openWindow.document.write("</body>");
	openWindow.document.write("</html>");
	openWindow.document.close();	
}

/* ************************************************************************************ */
/* Page interface object */
/* ************************************************************************************ */
PageIntf = function(langCode){
	this.waitString = "";
	this.requestPending = false;
	this.langCode = langCode;
}

PageIntf.prototype.footerInitWait = function (waitString){
	this.waitString = waitString;
	document.getElementById('divwait').innerHTML = this.waitHtmlString();
}

PageIntf.prototype.waitHtmlString = function(waitString){
	if(!waitString || waitString == null)
		waitString = this.waitString;
	return "<div id='__wfemDivWaitText' style='position:absolute;top:0;left:0;z-index:1001;'>"+
				"<div style='z-index:2001;position:relative;width:300px;height:80px;'>"+
				"<table width='100%' height='100%' bgcolor='#F0F0F0' class='ui-corner-all' style='border:solid 1px #1A458F;'>"+
					"<tr>"+
						"<td align='center' valign='middle' style='font-family:Arial;font-size:11pt;color: #1A458F;'>"+
							"<img src='"+__retrieveWfemLayoutResourceUrl()+"/images/wait.gif'/>&nbsp;&nbsp;"+waitString+
						"</td>"+
					"</tr>"+
				"</table>"+
				"</div>"+
				"<div class='ui-corner-all divwaitOpacityStyle' style='z-index:2000;position:absolute;left:10;top:10;width:300;height:80;'>"+
				"</div>"+
			"</div>";
}

PageIntf.prototype.footerInitFields = function (errTitle,skippableFields,warTitle,warModify,warIgnore,msgTitle){
	document.getElementById('divErrHelper').innerHTML = 
					"<table cellspacing='0' cellpadding='0' width='250' class='helperPanel'>"+
					"<tr><td style='padding:5pt;'>"+
						"<table id='tabErrHelper' name='tabErrHelper' cellspacing='0' cellpadding='0' width='100%' style='background:white;'>"+
					      "<tr>"+
					        "<td>"+
						        "<table width='100%' cellspacing='0' cellpadding='0' class='helperErrorHeader'>"+
						          "<tr>"+
							        "<td onClick='__fieldIntf.closeHelper(\"Err\")'>"+errTitle+"</td>"+
							        "<td onClick='__fieldIntf.closeHelper(\"Err\")' align='right'>X</td>"+
						          "</tr>"+
						        "</table>"+
					        "</td>"+
					      "</tr>"+
					    "</table>"+
				    "</td></tr>"+
				    "</table>";
				
	document.getElementById('divWarHelper').innerHTML = 
					"<input type='hidden' name='skippableFields' id='skippableFields' value='"+skippableFields+"'>"+
					"<table cellspacing='0' cellpadding='0' width='290' class='helperPanel'>"+
					"<tr><td style='padding: 5pt;'> "+
						"<table id='tabWarHelper' name='tabWarHelper' cellspacing='0' cellpadding='0' width='100%' style='background:white;'>"+
					      "<tr>"+
					        "<td>"+
					          "<table width='100%' cellspacing='0' cellpadding='0' class='helperWarningHeader'>"+
							    "<tr>"+
							        "<td nowrap onClick='__fieldIntf.closeHelper(\"War\")'>"+warTitle+"</td>"+
							        "<td style='padding: 1pt;'>"+
							           "<input type='button' class='action' style='font-weight: normal; font-size: 8pt; width: 50pt; height: 15pt;'"+
							           		  "onclick='__fieldIntf.warningModify();' "+
							           		  "onmouseover='this.style.textDecoration=\"underline\";' onmouseout='this.style.textDecoration=\"\";'"+
							           		  "value='"+warModify+"'>"+
							        "</td>"+
							        "<td style='padding: 1pt;'>"+
							           "<input type='button' class='action' style='font-weight: normal; font-size: 8pt; width: 50pt; height: 15pt;'"+ 
							           		  "onclick='__fieldIntf.warningIgnore();' "+
							           		  "onmouseover='this.style.textDecoration=\"underline\";' onmouseout='this.style.textDecoration=\"\";'"+
							           		  "value='"+warIgnore+"'>"+
							        "</td>"+
							        "<td onClick='__fieldIntf.closeHelper(\"War\")' align='right'>X</td>"+
							    "</tr>"+
						      "</table>"+
						    "</td>"+
					      "</tr>"+
					    "</table>"+
				    "</td></tr>"+
				    "</table>";

	document.getElementById('divMsgHelper').innerHTML = 
					"<table cellspacing='0' cellpadding='0' width='250' class='helperPanel'>"+
					"<tr><td style='padding: 5pt;'>"+
						"<table id='tabMsgHelper' name='tabMsgHelper' cellspacing='0' cellpadding='0' width='100%' style='background:white;'>"+
					      "<tr>"+
					        "<td>"+
					          "<table width='100%' cellspacing='0' cellpadding='0' class='helperMessageHeader'>"+
					            "<tr>"+
							        "<td onClick='__fieldIntf.closeHelper(\"Msg\")'>"+msgTitle+"</td>"+
							        "<td onClick='__fieldIntf.closeHelper(\"Msg\")'align='right'>X</td>"+
							    "</tr>"+
							  "</table>"+
							"</td>"+
					      "</tr>"+
					    "</table>"+
				    "</td></tr>"+
				    "</table>";
				    
	document.onkeypress = function(e){__pageIntf.onkeypress(e)};
	document.onkeydown = function(e){__pageIntf.onkeydown(e)};
	
}

PageIntf.prototype.onkeydown = function (e){
	try{
		var intKey = __getKeyEvent(e);
		if(intKey == 8){ // Back
			if(isIE()){
				if(event.srcElement.tagName.toUpperCase() == 'INPUT'){
					if((event.srcElement.type.toUpperCase() != 'TEXT' && event.srcElement.type.toUpperCase() != 'PASSWORD') || !isFieldEnabled(event.srcElement.id))
						preventDefault(e); 
				}else if(event.srcElement.tagName.toUpperCase() == 'TEXTAREA'){
					if(!isFieldEnabled(event.srcElement.id))
						preventDefault(e); 
				}else{
					preventDefault(e); 
				}
			}else{
				if(e.target.nodeName.toUpperCase() == 'INPUT'){
					if((e.target.type.toUpperCase() != 'TEXT' && event.srcElement.type.toUpperCase() != 'PASSWORD') || !isFieldEnabled(e.target.id))
						preventDefault(e); 
				}else if(e.target.nodeName.toUpperCase() == 'TEXTAREA'){
					if(!isFieldEnabled(e.target.id))
						preventDefault(e); 
				}else{
					preventDefault(e); 
				}
			}
		}
	}catch(e){}
}

PageIntf.prototype.onkeypress = function (e){
	var intKey = __getKeyEvent(e);
	if(intKey == 13){
		try{ 
			doEnterAction();
			preventDefault(e); // From JavaScriptUtil.js
		}catch(e){}
	}
}

PageIntf.prototype.openPopup = function(cmd,cmdParams,jsInputParams,callback,title,height,width,modal){
	if(cmdParams == null)
		cmdParams = "";
	if(cmdParams != "")
		cmdParams = "&"+cmdParams;
	if(cmdParams.indexOf("BrowserInstance=") < 0)
        cmdParams += "&BrowserInstance="+__getBrowserInstance();
	
	document.getElementById("wltPopupContainer").style.height=height;
	document.getElementById("wltPopupContainer").style.width=width;
	
	var popupIframe = document.getElementById("wltPopupContainerIFrame");
	popupIframe.style.visibility='hidden';
	popupIframe.cmd=cmd;
	popupIframe.cmdParams=cmdParams;
	popupIframe.jsInputParams = jsInputParams;
	popupIframe.callback = callback;
	
	if(isIE()){
		popupIframe.height=height;
		popupIframe.width=width;	
	}
	
	popupIframe.src= __retrieveWfemLayoutResourceUrl()+"/private/waitPopup.html";
	$("#wltPopupContainer").dialog({
										autoOpen: true, 
										modal: modal,
										closeOnEscape: false,
										title: title,
										height: isIE()?'auto':height,
										width: isIE()?(width+20):width,
										beforeclose: function(event, ui) {
																			__pageIntf.closeModalPopup(null);
										 								 }
								   });
}								   

PageIntf.prototype.openModalPopupEnd = function(){
	var popupIframe = document.getElementById("wltPopupContainerIFrame");
	popupIframe.style.visibility='visible';
	popupIframe.src= __retrieveGatewayUrl()+"call.wfem?wfemCmd="+popupIframe.cmd+popupIframe.cmdParams;
}

PageIntf.prototype.getModalPopupInputParams = function (){
	return document.getElementById("wltPopupContainerIFrame").jsInputParams;	
}

PageIntf.prototype.closeModalPopup = function (popupResult){
	if(!popupResult)
		popupResult = null;
	var pCont = $("#wltPopupContainer"); 
	var popupIframe = document.getElementById("wltPopupContainerIFrame");
	popupIframe.src = "/wfemlayout/wlt/blankPage.html";
	setTimeout(function() {
								pCont.dialog('destroy');
								if(popupIframe.callback != null){
									if(popupIframe.callback.callbackObj)
										popupIframe.callback.callbackFnc.apply(popupIframe.callback.callbackObj,[popupResult]);
									else
										popupIframe.callback.apply(this,[popupResult]);
								}
							}, 5);	
}

PageIntf.prototype.isRequestPending = function(){
	return this.requestPending;
}

PageIntf.prototype.startRequest = function(){	
	if(this.isRequestPending())
		return false;

	this.requestPending = true;
	var waitObj=document.getElementById("waitObject");
	if(waitObj != null){
		waitObj.style.visibility = 'visible';
		return true;
	}

	var divWaitObj=document.getElementById("divwait");
	if(divWaitObj == null)
		return true;
		
	divWaitObj.style.display='';
	freeze();
	
	var docH = document.body.clientHeight;
	var docW = document.body.clientWidth;
	var docScrollLeft = 0; try{docScrollLeft = document.body.scrollLeft;}catch(e){}
	var docScrollTop = 0; try{docScrollTop = document.body.scrollTop;}catch(e){}
	
	var divWaitText=document.getElementById("__wfemDivWaitText");
	var divH = divWaitText.clientHeight;
	var divW = divWaitText.clientWidth;
	
	var x = ((docW - divW) / 2) + docScrollLeft;
	var y = ((docH - divH) / 2) + docScrollTop;
	
	divWaitText.style.left=parseInt(x,10)+"px";
	divWaitText.style.top=parseInt(y - 10,10)+"px";
	return true;
}

// Hide wait object
PageIntf.prototype.stopRequest = function(){
	this.requestPending = false;
	
	var waitObj=document.getElementById("waitObject");
	if(waitObj != null){
		waitObj.style.visibility = 'hidden';
		return;
	}

	var divWaitObj=document.getElementById("divwait");
	if(divWaitObj != null)
		divWaitObj.style.display='none';
	unfreeze();
}

