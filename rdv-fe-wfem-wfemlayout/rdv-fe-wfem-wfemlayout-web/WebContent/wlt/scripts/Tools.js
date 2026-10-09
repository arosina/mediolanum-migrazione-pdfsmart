// IE versus others
function isIE(){
    if(!window.ActiveXObject)
        return false;
    return true;
}

// Return if we are in PUM environment
function isPUM(){
	try{
		var gatewayUrl = document.getElementById("__gatewayUrlGeneratorObject").href;
		if(gatewayUrl.indexOf("portal/server.pt/gateway") < 0 && gatewayUrl.indexOf("lr/rdv-fe-lr-proxy-portlet") < 0)
			return false;
		return true;
	}catch(e){
		return false;
	}
}

// Sleep for millisceonds 
function pause(millis){
	var date = new Date();
    var curDate = null;
    do{ 
    	curDate = new Date(); 
    }while(curDate-date < millis)
}

// Open a new Window whith isPUM information 
function openNewWindow(url,title,specs){
	var woPAr1 = 'about:blank';
	if(!title || title == null){ 
		woPAr1 = ''; 
		title= ''; 
	}
	if(!specs)
		specs = 'titlebar=yes,scrollbars=yes,resizable=yes';

	var newWindow = null;
	if (navigator.userAgent.indexOf('iPad') != -1) {
		newWindow = window.open(url, title, specs);
	} else {
		newWindow = window.open(woPAr1,'',specs);
		newWindow.document.write("<html><head><title>"+title+"&nbsp;</title></head>");
		newWindow.document.write("<body style='height:100%; margin:0; padding:0;'>");
		newWindow.document.write("<iframe style='width=100%; height:100%' width='100%' height='100%' frameborder='0' src='"+url+"'></iframe>");
		newWindow.document.write("</body></html>");
		newWindow.document.close();
	}
	
	return newWindow;	
}

// Open a non modal dialog 
function openPopup(cmd,cmdParams,jsInputParams,callback,title,height,width,sameStack){
	if(sameStack)
		__pageIntf.openPopup(cmd+".execute",cmdParams,jsInputParams,callback,title,height,width,false);
	else
		__pageIntf.openPopup(cmd+".executeOnPopup",cmdParams,jsInputParams,callback,title,height,width,false);
}

// Open a modal dialog 
function openModalPopup(cmd,cmdParams,jsInputParams,callback,title,height,width,sameStack){
	if(sameStack)
		__pageIntf.openPopup(cmd+".execute",cmdParams,jsInputParams,callback,title,height,width,true);
	else
		__pageIntf.openPopup(cmd+".executeOnPopup",cmdParams,jsInputParams,callback,title,height,width,true);
}

// Retrieve the javascript dialog input object 
function getModalPopupInputParams(){
	return parent.__pageIntf.getModalPopupInputParams();
}

// Close the modal dialog
function closeModalPopup(popupResult){
	parent.__pageIntf.closeModalPopup(popupResult);
}

// Return parameters of a form into a string representation. Suitable for use of a form in an URL query string
function formToString(form){
	return new _WHSClass(form).loadFormData();
}  

// Normal submit
function wfemSubmit(objForm){
	if(typeof(objForm.skippableFields) == 'undefined'){
		var skipFields = document.getElementById('skippableFields');
		if(skipFields != null){
		    var sk = document.createElement("input");
		    sk.setAttribute("type", "hidden");
		    sk.setAttribute("value", skipFields.value);
		    sk.setAttribute("name", "skippableFields");
		    objForm.appendChild(sk);
		}
	}
	objForm.submit();
}

// Hidden submit
function wfemHiddenSubmit(objForm,objId,callback){
	var whs = new _WHSClass(objForm,objId,callback);
	whs.submitForm();
}

// Retrieve the gateway URL
function __retrieveGatewayUrl(){
	try{
		var gatewayUrl = document.getElementById("__gatewayUrlGeneratorObject").href;
		if(gatewayUrl.indexOf("portal/server.pt/gateway") < 0 && gatewayUrl.indexOf("lr/rdv-fe-lr-proxy-portlet") < 0)
			return "";
		var url = gatewayUrl.substring(0,gatewayUrl.indexOf("/prgm/mokeCssToHaveGatewayUrl.css"));
		if(url.length > 0 && url.indexOf("/") != (url.length-1))
			url = url+"/";
		return url;
	}catch(e){
		return "";
	}
}

// Retrieve the webapp URL
function __retrieveResourceUrl(webApp){
	if(typeof(webApp) == 'undefined'){
		if(__jsWebApp.indexOf("/") == 0)
			webApp = __jsWebApp;
		else
			webApp = "/"+__jsWebApp;
	}
	if(__jsResourceServerUrl != ''){
		if(webApp.indexOf("/") == 0)
			return __jsResourceServerUrl+webApp;
		else
			return __jsResourceServerUrl+"/"+webApp;
	}
	if(webApp.indexOf("/") == 0)
		return __retrieveGatewayUrl()+webApp; 
	else
		return __retrieveGatewayUrl()+"/"+webApp; 
}

// Retrieve the WfemLayotu webapp URL
function __retrieveWfemLayoutResourceUrl(){
	if(__jsWfemLayoutResourceServerUrl != '')
		return __jsWfemLayoutResourceServerUrl+"/wfemlayout/wlt";
	return __retrieveGatewayUrl()+"/wfemlayout/wlt"; 
}

// Return the keycode
function __getKeyEvent(e){
	return (window.event) ? window.event.keyCode : e.which;
}

// Return if request is running
function isRequestPending(){
	return __pageIntf.isRequestPending();
}

// Show wait object
function startRequest(){
	return __pageIntf.startRequest();
}

// Hide wait object
function stopRequest(){
	__pageIntf.stopRequest();
}

// Show opacity over document
function freeze(){
	if(document.getElementById("divwaitOpacityCover")){
		$("#divwaitOpacityCover").height("100%");
		document.getElementById("divwaitOpacityCover").style.display='';
	}
}

// Remove opacity to document
function unfreeze(){
	if(document.getElementById("divwaitOpacityCover"))
		document.getElementById("divwaitOpacityCover").style.display='none';	
}

// Setting property value. You can pass a relative object type (int, date etc.) or a formatted string
function setPropertyValue(fieldName,fieldValue){
	if(typeof(fieldValue) == 'undefined' || fieldValue == null)
		fieldValue = "";
	
	var typeofFieldValue = ""+typeof(fieldValue); 
	typeofFieldValue = typeofFieldValue.toLowerCase();
	
	var fs = document.getElementsByName(fieldName);
	if(fs == null || fs.length == 0)
		return null;
	
	var f = fs[0];

	// If is a radio button	
	if(f.type.toLowerCase() == 'radio'){
		for(var i=0;i<fs.length;i++){
			if(fs[i].value == fieldValue.toString()){
				fs[i].checked = true;
				return fs[i];
			}
		}
		return null;
	}
	
	// If is a checkbox	
	var checkBoolObj = document.getElementById(fieldName+'Check');
	if(checkBoolObj != null){
		if(typeofFieldValue == 'string')
			f.value = fieldValue.toLowerCase();
		else
			f.value = ""+fieldValue;
		if(f.value == 'true')
			checkBoolObj.checked = true;
		else
			checkBoolObj.checked = false;
		return f;
	}
	
	if(typeofFieldValue == 'string'){
		f.value = fieldValue;
		return f;
	}
	
	var ftype = f.getAttribute("ftype");
	
	if(ftype == 'IntegerType'){
		f.value = parseInt(fieldValue);
		return f;
	}
		
	if(ftype == 'DoubleType'){
		if(typeofFieldValue == 'string'){
			f.value = fieldValue;
			return;
		}
		var separator = __fieldIntf.separator;
		var decimalChar = __fieldIntf.decimalChar;
		var scale = f.getAttribute("scale");
		var numdig = f.getAttribute("numDig");
		var negative = false;
		if(fieldValue < 0){
			negative = true;
			fieldValue = fieldValue*-1;
		}
		var dv = ""+fieldValue;
		var idx = dv.lastIndexOf(".");
		var si = dv;
		var di = "";
		if(idx >= 0){
			si = dv.substring(0,idx);
			di = dv.substring(idx+1,idx+1+parseInt(scale,10));
		}
		
		var ssi = addSeparator(si,separator);
		for(var i=di.length;i<scale;i++)
			di += "0"
				
		if(f.getAttribute('keepzerodec') != 'true'){
			if(parseInt(di,10) == 0)
				di = "";
		}
		
		if(di != "")
			dv = ssi+decimalChar+di;
		else
			dv = ssi;
		
		if(negative)
			f.value = "-"+dv;
		else
			f.value = dv;
		return f;
	}
		
	if(ftype == 'DateType' || ftype == 'TimestampType'){
		var gg = fieldValue.getDate(); 		if(gg < 10) gg = "0"+gg;
		var mm = fieldValue.getMonth()+1;	if(mm < 10) mm = "0"+mm;
		var aa = fieldValue.getFullYear();
		f.value = gg+"-"+mm+"-"+aa;
		
		if(ftype == 'TimestampType'){
			var hh = fieldValue.getHours();		if(hh < 10) hh = "0"+hh;
			var mi = fieldValue.getMinutes();	if(mi < 10) mi = "0"+mi;
			var ss = fieldValue.getSeconds();	if(ss < 10) ss = "0"+ss;
			f.value = gg+"-"+mm+"-"+aa+" "+hh+":"+mi+":"+ss;
		}
		return f;
	}
	return null;
}

//Add number separator
function addSeparator(nStr,separator)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + separator + '$2');
	}
	return x1 + x2;
}

// Getting property value
function getPropertyValue(fieldName){
	var result = null;

	var fs = document.getElementsByName(fieldName);
	if(fs == null || fs.length == 0)
		return false;
	
	var f = fs[0];

	// If is a radio button	
	if(f.type.toLowerCase() == 'radio'){
		for(var i=0;i<fs.length;i++){
			if(fs[i].checked)
				return fs[i].value;
		}
		return null;
	}
		
	var ftype = f.getAttribute("ftype");
	
	if(ftype == 'StringType'){
		result = f.value.toString();
	}

	if(ftype == 'IntegerType'){
		result = parseInt(f.value,10);
	}
		
	if(ftype == 'DoubleType'){
		result = toDouble(f.value,f.getAttribute("scale"));
	}
		
	if(ftype == 'DateType' || ftype == 'TimestampType'){

		var date = f.value;
		
		var gg = parseInt(date.substr(0,2),10);
		var mm = parseInt(date.substr(3,5),10)-1;
		var aa = parseInt(date.substr(6,10),10);
		
		result = new Date(aa,mm,gg);
		
		if(ftype == 'TimestampType'){
			
			var hh = parseInt(date.substr(11,13),10);
			var mi = parseInt(date.substr(14,16),10);
			var ss = parseInt(date.substr(17,19),10);
			
			result = new Date(aa,mm,gg,hh,mi,ss);
			
		}
	}
	return result;
}

// Check if date is valid. You can pass a propName or a date string in "dd-mm-yyyy" format
function checkDate(datePropName){
	try{
		if(datePropName.indexOf("-") == 2 && datePropName.lastIndexOf("-") == 5)
			date = datePropName;
		else{
			var dObj = document.getElementById(datePropName);
			if(dObj == null)
				return false;
			else
				date = dObj.value;
		}
		
		var flen = 10;
			
		if(date.length == 0)
			return true;
			
		if(date.length != flen)
			return checkTime(datePropName);
			
		var gg = parseInt(date.substr(0,2),10);
		var mm = parseInt(date.substr(3,5),10)-1;
		var aa = parseInt(date.substr(6,10),10);
		if(aa < 1753)
			return false;
			
		var newDataObj = new Date(aa,mm,gg);
		if(newDataObj.getFullYear() != aa || newDataObj.getMonth() != mm || newDataObj.getDate() != gg)
			return false;
			
		return true;
	}catch(e){
	   return false;
	}
}

// Check if datetime is valid. You can pass a propName or a datetime string in "dd-mm-yyyy hh:mi:ss" format
function checkTime(timePropName){
	try{
		if(timePropName.indexOf("-") == 2 && timePropName.lastIndexOf("-") == 5)
			date = timePropName;
		else{
			var tObj = document.getElementById(timePropName);
			if(tObj == null)
				return false;
			else
				date = tObj.value;
		}

		var flen = 19; 
			
		if(date.length == 0)
			return true;
			
		if(date.length != flen)
			return false;
			
		var gg = parseInt(date.substr(0,2),10);
		var mm = parseInt(date.substr(3,5),10)-1;
		var aa = parseInt(date.substr(6,10),10);
			
		var hh = parseInt(date.substr(11,13),10);
		var mi = parseInt(date.substr(14,16),10);
		var ss = parseInt(date.substr(17,19),10);
		
		var	newDataObj = new Date(aa,mm,gg,hh,mi,ss);
		if(newDataObj.getHours() != hh || newDataObj.getMinutes() != mi || newDataObj.getSeconds() != ss)
			return false;
		
		return true;
	}catch(e){
	   return false;
	}
}

// Test enabled/disabled action
function isActionEnabled(actionName){
	var actions = document.getElementsByName(actionName);
	if(actions == null || actions.length == 0)
		return false;
	
	var action = actions[0];
	if(action == null)
		return false;

	var enabled = false;
	if(action.className == 'action')
		enabled = true;

	return enabled;
}

// Enable/Disable action
function enableAction(actionName,enabled){
	var actions = document.getElementsByName(actionName);
	if(actions == null || actions.length == 0)
		return;
	
	for(var i=0;i<actions.length;i++){

		var action = actions[i];
		action.disabled = !enabled;
		if(enabled)
			action.className = 'action';
		else
			action.className = 'disabledAction';
			
	}
	return;
}

// Execute an action
function doAction(actionName){
	var actions = document.getElementsByName(actionName);
	if(actions == null || actions.length == 0)
		return;
	
    actions[0].focus();
    actions[0].click();
	return;
}

// Test enabled/disabled field
function isFieldEnabled(fieldName){
	var field = document.getElementById(fieldName);
	if(field == null)
		return false;

	var enabled = true;
	if(field.getAttribute("isreadonly") != null && field.getAttribute("isreadonly") == "true")
		enabled = false;

	return enabled;
}

// Enable/Disable field
function enableField(fieldName,enabled){
	var fields = document.getElementsByName(fieldName);
	if(fields == null)
		return;

	var ronly = false;
	if(!enabled)
		ronly = true;
	
	for(var i=0;i<fields.length;i++){

		var fieldObj = fields[i];
		
		if(ronly)
			fieldObj.setAttribute("isreadonly","true");
		else
			fieldObj.setAttribute("isreadonly","false");
		
		if(fieldObj.tagName.toLowerCase() == 'select'){
			
			fieldObj = document.getElementById(fieldObj.id);
			fieldObj.disabled = !enabled;
			
		}else if(fieldObj.type.toLowerCase() == 'radio'){
			
			fieldObj.disabled = !enabled;
		
		}else{
			
			fieldObj.readOnly = ronly;
			if(enabled){
				$(fieldObj).removeClass("outputField");
				$(fieldObj).addClass("inputField");
			}else{
				$(fieldObj).removeClass("inputField");
				$(fieldObj).addClass("outputField");
			}
				
			var ftype = fieldObj.getAttribute("ftype");
			
			if(ftype == 'BooleanType'){
				var cfieldObj = document.getElementById(fieldName+"Check");
				if(cfieldObj != null){ // Is a checkbox
					cfieldObj.disabled = !enabled;
				}
			}
			
			if(ftype == 'DateType' || ftype == 'TimestampType'){
				var calvis = 'visible';
				if(!enabled)
					calvis = 'hidden';
				var calImg = document.getElementById(fieldName+"ImgCal");
				if(calImg != null)
					calImg.style.visibility = calvis;
				var mask = document.getElementById(fieldName+"Mask");
				if(mask != null){
					if(enabled){
						$(mask).removeClass("outputField");
						$(mask).addClass("inputField");
					}else{
						$(mask).removeClass("inputField");
						$(mask).addClass("outputField");
					}
				}
			}
			
		}
	}
			
}

// Cahange field label
function setFieldLabelText(fieldName,labelText){
	var ll = document.getElementById(fieldName+"Label");
	if(ll != null)
		ll.innerHTML = labelText;
}

// Hide error/warning anchor image
function hideHelperAnchor(propName){
	var divObj = document.getElementById(propName+"HelperAnchor");
	if(divObj != null)
		divObj.style.display='none';
}


// Fill field to left with pad char
function padLeft(obj,pad) {
	if(!obj.maxLength)
		return;
	if((trim(obj.value)).length == 0) 
		return;
	while(obj.value.length<obj.maxLength){
		obj.value="" + pad + obj.value;
	}		
}

// Remove right blank from field
function trim(s){
	while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r')){
		s = s.substring(1,s.length);}
	
	while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r')){
		s = s.substring(0,s.length-1);}
	return s;
}

// Return a double from double string (width separator and decimal char management)
function toDouble(value,scale){
	var re1 = new RegExp('\\'+__fieldIntf.separator,'g');
	var snum = value.replace(re1,'');
	var re2 = new RegExp('\\'+__fieldIntf.decimalChar,'g');
	snum = snum.replace(re2,'.');
	if(scale)
		return parseFloat(parseFloat(snum).toFixed(scale));
	else
		return parseFloat(snum);
}	

// Include a PDF object into anchor with specified id. formOrUrl can be a from object or an URL string
function includePdfObject(anchorId,formOrUrl){
	document.getElementById(anchorId).innerHTML = 
		"<div id='"+anchorId+"PdfObjDiv' style='background: transparent url("+__retrieveWfemLayoutResourceUrl()+"/images/wait.gif) no-repeat center center;width:100%;height:100%;overflow: auto;'></div>";
	var data = "";
	if(typeof(formOrUrl) == 'string')
		data = formOrUrl;
	else
		data = __retrieveGatewayUrl()+"call.wfem?" + new _WHSClass(formOrUrl).loadFormData();
	var objectTag = "";
	if(isIE()){
		objectTag = "<iframe src=\""+data+"\" width='100%' height='100%' frameborder='0'></iframe>"
	}else{
		objectTag =	"<object height='100%' width='100%' style='height:100%;width:100%;' data=\""+data+"\">"+
								"<param value=\""+data+"\" name='src'/>"+
								"<param value='transparent' name='wmode'/>"+
					"</object>";
	}
	setTimeout(function(){
							document.getElementById(anchorId).loaded=true;
							document.getElementById(anchorId+"PdfObjDiv").innerHTML = objectTag;
						  },1000); // So image is showed onto all browsers
						  
}

// Open a PDF object into a new window. formOrUrl can be a from object or an URL string
function openPdfObject(formOrUrl,title,height,width){
	var woPAr1 = 'about:blank';
	if(!title || title == null){ 
		woPAr1 = ''; 
		title= ''; 
	}

	var data = "";
	if(typeof(formOrUrl) == 'string')
		data = formOrUrl;
	else
		data = __retrieveGatewayUrl()+"call.wfem?" + new _WHSClass(formOrUrl).loadFormData();
	
	var sh = ''; if(height) sh=",height="+height;
	var sw = ''; if(width)  sw=",width="+width;

	var openWindow = null;
	var extraParams = 'titlebar=yes,scrollbars=yes,resizable=yes,top=10,left=10'+sh+sw;
	if (navigator.userAgent.indexOf('iPad') != -1) {
		openWindow = window.open(data, title, extraParams);
	} else {
		openWindow = window.open(woPAr1, '', extraParams);
		openWindow.document.write("<html><head><title>"+title+"&nbsp;</title></head>");
		openWindow.document.write("<body style='height:100%; margin:0; padding:0;'>");
		openWindow.document.write("<iframe style='width=100%;height:100%;' width='100%' height='100%' frameborder='0' src=\""+data+"\"/>");
		openWindow.document.write("</body></html>");
		openWindow.document.close();
	}
	return openWindow;
}

// Download a CALC object. formOrUrl can be a from object or an URL string
function downloadCalcObject(formOrUrl,fileName){
	var data = "";
	if(typeof(formOrUrl) == 'string')
		data = formOrUrl;
	else
		data = __retrieveGatewayUrl()+"call.wfem?" + new _WHSClass(formOrUrl).loadFormData();
	var fn = "";
	if(fileName != null && fileName != "")
		fn = "&fileName="+fileName+".xls";
	document.getElementById("utilIFrame").src=data+fn;	
}

// Show the helper box with the input message
function showMessageHelper(anchorName,message){
	__fieldIntf.showMessageHelper(anchorName,message);	
}

function removeSkippable(propName){
	__fieldIntf.removeSkippable(propName);	
}

function addWindowOnLoadEvent(func) {
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	} else {
		window.onload = function() {
	    	if (oldonload)
	        	oldonload();
	      	func();
		}
	}
}
