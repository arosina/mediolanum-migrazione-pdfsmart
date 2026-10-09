var isNS6=(navigator.userAgent.toLowerCase().indexOf("gecko")>-1 || window.sidebar);
///////////////////////////////////////////////////////////////////////////////

var LOG = new Logger();

var SEMAPHORE = new Semaphore(100);

/////////////////////////////////////////////////////////////////////
/////////////////////   Recupero WebApp         /////////////////////
/////////////////////////////////////////////////////////////////////
function __retrieveGatewayUrl(){
	try{
		var gatewayUrl = document.getElementById("__gatewayUrlGeneratorObject").href;
		var url = gatewayUrl.substring(0,gatewayUrl.indexOf("/prgm/mokeCssToHaveGatewayUrl.css"));
		if(url.length > 0)
			url = url+"/";
		return url;
	}catch(e){alert("__retrieveGatewayUrl: "+e.message);}
}

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

function __retrieveWfemLayoutResourceUrl(){
	if(__jsWfemLayoutResourceServerUrl != '')
		return __jsWfemLayoutResourceServerUrl+"/wfemlayout";
	return __retrieveGatewayUrl()+"/wfemlayout"; 
}

/////////////////////////////////////////////////////////////////////
/////////////////////   ERRORI E WARNING        /////////////////////
/////////////////////////////////////////////////////////////////////
function warningModify(){
	var propName = document.all('divWarHelperField').value;
	var fieldObj = document.all(propName);
	if(!fieldObj.wasReadonly){
		if(fieldObj.isCombo && fieldObj.isCombo == true){
			fieldObj.setReadonly(false);
			fieldObj.setClassName('inputField');
		}else{
			enableField(propName,true);
		}
		if(fieldObj.isDate && fieldObj.isDate == 'true'){
			var dFieldObj = document.all(propName+"GG");
			try{
				dFieldObj.select();
			}catch(e){}	
			try{
				dFieldObj.focus();
			}catch(e){}
			try{
				document.all(propName+'CalendarTable').style.visibility = 'visible';
			}catch(e){}
		}else{			
			try{
				fieldObj.select();
			}catch(e){}	
			try{
				fieldObj.focus();
			}catch(e){}
		}
	}
	var anchorName = document.all('divWarHelperAnchor').value;
	var anchorObj = document.all(anchorName+'HelperAnchor');
	anchorObj.style.display = 'none';
	var	ifrRef = document.all(anchorName+"DivViewPort");
	if(ifrRef != null)
		ifrRef.style.display='none';
	removeSkippable(propName);
	closeHelper("War");
}
function warningIgnore(){
	var propName = document.all('divWarHelperField').value;
	var anchorName = document.all('divWarHelperAnchor').value;
	var anchorObj = document.all(anchorName+'HelperAnchor');
	anchorObj.style.display = 'none';
	var	ifrRef = document.all(anchorName+"DivViewPort");
	if(ifrRef != null)
		ifrRef.style.display='none';
	addSkippable(propName);
	closeHelper("War");
}

////////////////////////////////////////////////////////////////////////////////
/////////////////////      Esclusione di F5 e back         /////////////////////
////////////////////////////////////////////////////////////////////////////////
function manageBack() 
{
	if (window.event) {
	  	// keycode for F5 function 
		if (window.event && window.event.keyCode == 116) {
			window.event.keyCode = 8;
		   window.event.cancelBubble = true; 
		   window.event.returnValue = false; 
		   return false; 
		}
	
	var oSource = window.event.srcElement;
    if (!oSource.isContentEditable) { 
			// keycode for backspace 
			if (window.event && window.event.keyCode == 8) { 
			   window.event.cancelBubble = true; 
			   window.event.returnValue = false; 
			   return false; 
			} 
		}
	}
} 
document.onkeydown = manageBack;

////////////////////////////////////////////////////////////////////////////////
/////////////////////      Gestione Actions         ////////////////////////////
////////////////////////////////////////////////////////////////////////////////
var requestPending = false;

function isRequestPending(){
	return requestPending;
}

function setRequestPending(isRequestPending){
	requestPending = isRequestPending;
}

function startRequest(){
	if(isRequestPending()){
		return false;
	}
	requestPending = true;
	var waitObj=document.all("waitObject");
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
	var divH = divWaitObj.clientHeight;
	var divW = divWaitObj.clientWidth;
	var x = (docW - divW) / 2;
	var y = (docH - divH) / 2;
	divWaitObj.style.left=x;
	divWaitObj.style.top=y - 10;

	var ifrRef = document.getElementById('DivViewPort');	
    ifrRef.style.width = divWaitObj.offsetWidth;
    ifrRef.style.height = divWaitObj.offsetHeight;
    ifrRef.style.top = divWaitObj.style.top;
    ifrRef.style.left = divWaitObj.style.left;
    ifrRef.style.zIndex = divWaitObj.style.zIndex - 1;
    ifrRef.style.display = "block";	
	return true;
}
function stopRequest(){
	
	requestPending = false;
	
	var waitObj=document.all("waitObject");
	if(waitObj != null){
		waitObj.style.visibility = 'hidden';
		return;
	}

	var divWaitObj=document.getElementById("divwait");
	if(divWaitObj != null)
		divWaitObj.style.display='none';
	unfreeze();
	var ifrRef = document.getElementById('DivViewPort');	
	if(ifrRef != null)
		ifrRef.style.display='none';
		
}

function freeze(){
	if(document.getElementById("divwaitOpacityCover"))
		document.getElementById("divwaitOpacityCover").style.display='';	
}

function unfreeze(){
	if(document.getElementById("divwaitOpacityCover"))
		document.getElementById("divwaitOpacityCover").style.display='none';	
}
function isActionEnabled(actionName){
	var action = document.all(actionName);
	if(action == null)
		return;

	var enabled = false;
	if(action.className == 'action')
		enabled = true;

	return enabled;
}
function enableAction(actionName,enabled){
	var action = document.all(actionName);
	if(action == null)
		return;
	if(enabled)
		action.className = 'action';
	else
		action.className = 'disabledAction';
}
function enableField(fieldName,enabled){
	var field = document.all(fieldName);
	if(field == null)
		return;
	if(enabled){
		if(field.isDate && field.isDate == 'true'){
			var dField = document.all(fieldName+"GG");
			dField.className = 'inputField';
			dField.readOnly = false;
			dField = document.all(fieldName+"MM");
			dField.className = 'inputField';
			dField.readOnly = false;
			dField = document.all(fieldName+"AA");
			dField.className = 'inputField';
			dField.readOnly = false;
			var calImg = document.all(fieldName+"CalendarTable");
			if(calImg && calImg != null)
				calImg.style.visibility = 'visible';
		}else if(field.isCheck && field.isCheck == 'true'){
			var cField = document.all(fieldName+"Check");
			cField.disabled = false;
		}else if(field.tagName == 'select' || field.tagName == 'SELECT'){
			field.disabled = false;
			field.className = 'inputField';
			field.options.style.backgroundColor = 'white';
		}else{
			field.className = 'inputField';
			field.readOnly = false;
		}
	}else{
		if(field.isDate && field.isDate == 'true'){
			var dField = document.all(fieldName+"GG");
			dField.className = 'outputField';
			dField.readOnly = true;
			dField = document.all(fieldName+"MM");
			dField.className = 'outputField';
			dField.readOnly = true;
			dField = document.all(fieldName+"AA");
			dField.className = 'outputField';
			dField.readOnly = true;
			var calImg = document.all(fieldName+"CalendarTable");
			if(calImg && calImg != null)
				calImg.style.visibility = 'hidden';
		}else if(field.isCheck && field.isCheck == 'true'){
			var cField = document.all(fieldName+"Check");
			cField.disabled = true;
		}else if(field.tagName == 'select' || field.tagName == 'SELECT'){
			field.disabled = true;
			field.className = 'outputField';
			field.options.style.backgroundColor = '#f7f7f7';
		}else{
			field.className = 'outputField';
			field.readOnly = true;
		}
	}
}
function setFieldLabelText(fieldName,labelText){
	var label = document.all(fieldName+"Label");
	if(label != null)
		label.innerHTML = labelText;
}
////////////////////////////////////////////////////////////////////////////////
/////////////////////       Formattazione campi     ////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function formatUK2IT(obj){
	var temp = (obj.toString().replace(/\,/g,'')).replace('.',',');
	return temp;
}

function formatIT2UK(obj){
	var temp = (obj.toString().replace(/\./g,'')).replace(',','.');
	return temp;
}

function formatNum(str, decimal){
   
   maxDigit = 12;
   maxFix = decimal;
   check="0123456789,";noPointsStr="";virgola="-2";zero="-2";

   if(event.keyCode=="8" && str.value == "0"){
      return str.value;
   }

   tmpStr="";
   for(x=0;x<str.value.length;x++){
      tmpStr+=str.value.substring(x,x+1)=="."?"":str.value.substring(x,x+1);
   }

   for(x=0;x<tmpStr.length;x++){
        c=tmpStr.substring(x,x+1);
        if(check.indexOf(c)>-1){
            if(x==0){
               if(decimal>0 && c==",") {virgola=0;c="0,";}
               else {virgola=-1;}
               cNext=tmpStr.length>1?tmpStr.substring(x+1,x+2):"";
               if(decimal>0 && c=="0"&&cNext!=",") {zero=1;c="0,";}
               else {zero=-1;}
            }else{
               if(c==","){
                  if(virgola>-1){c="";}
                  else{virgola=x;}
               }else{
                  if(virgola>-1){c=x>(virgola+maxFix)?"":c;}
               }
            }
            if(decimal<1 && c==","){c="";}
            if(x>(maxDigit-1)){c="";}
            noPointsStr+=c;
        }
   }

   if(maxFix == 0)
   	return noPointsStr;

   l=virgola>-1?virgola:noPointsStr.length;
   coda=noPointsStr.substring(l,noPointsStr.length);
   out=coda;
   cnt=0;
   for(x=(l-1);x>-1;x--){
      cc=noPointsStr.substring(x,x+1);
      if(cnt==3||cnt==6||cnt==9||cnt==12){
         cc=cc+".";
      }
      out=cc+out;
      cnt++;
   }

   return out;
}
function onlyNum(field){
   if(field.readOnly)
   	return false;

   if(event.keyCode < 48 || event.keyCode > 57){
      if(event.keyCode != 13){
	   event.keyCode='';
  	   return false;
  	  }
   }
   return true;
}
function onlyAlfa(field){
   if(field.readOnly)
   	return false;

   if(event.keyCode >= 48 && event.keyCode <= 57){
      if(event.keyCode != 13){
	   event.keyCode='';
  	   return false;
  	  }
   }
   return true;
}

////////////////////////////////////////////////////////////////////////
/////////////////////  Gestione checkbox    ////////////////////////////
////////////////////////////////////////////////////////////////////////
function setBoolField(boolPropName){
	var boolObj = document.all(boolPropName);
	var checkBoolObj = document.all(boolPropName+'Check');
	boolObj.value = checkBoolObj.checked ? "true" : "false";
}

function clearBoolField(boolPropName){
	var boolObj = document.all(boolPropName);
	var checkBoolObj = document.all(boolPropName+'Check');
	boolObj.value = "false";
	checkBoolObj.checked = false;
}

function checkBoolField(boolPropName){
	var boolObj = document.all(boolPropName);
	var checkBoolObj = document.all(boolPropName+'Check');
	boolObj.value = "true";
	checkBoolObj.checked = true;
}
////////////////////////////////////////////////////////////////////////////////
/////////////////////  Gestione calendario INIZIO   ////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function checkDate(datePropName){
	try{
		var date = document.all(datePropName).value;
		if(date.length == 0)
			return true;
		if(date.length != 10)
			return false;
		var gg = parseInt(date.substr(0,2),10);
		var mm = parseInt(date.substr(3,5),10)-1;
		var aa = parseInt(date.substr(6),10);
		if(aa < 2000)
			aa -= 1900;
		var newDataObj = new Date(aa,mm,gg);
		if(newDataObj.getYear()  != aa ||
		   newDataObj.getMonth() != mm ||
			newDataObj.getDate() != gg)
			return false;
			
		return true;
	}catch(e){
	   return false;
	}
}

function fillDate(anno, mese, giorno){
	var fire = false;
	var obj = document.all(calendar.output+"GG");
	if(obj.value != giorno){
		obj.value = giorno;
		fire = true;
	}
	obj = document.all(calendar.output+"MM");
	if(obj.value != mese){
		obj.value = mese;
		fire = true;
	}
	obj = document.all(calendar.output+"AA");
	if(obj.value != anno){
		obj.value = anno;
		fire = true;
	}
	setDateField(calendar.output);
	if(fire){
		try{obj.onchange();}catch(e){}
	}
	return true;
}

function setHtmlDateField(datePropName,value){
	if(value.length == 10){
		document.all(datePropName).value = value;
		document.all(datePropName+"GG").value = value.substring(0,2);
		document.all(datePropName+"MM").value = value.substring(3,5);
		document.all(datePropName+"AA").value = value.substring(6,10);
	}
}

function setDateField(datePropName){
	var dateObj = document.all(datePropName);

	var ggObj = document.all(datePropName+"GG");
	var mmObj = document.all(datePropName+"MM");
	var aaObj = document.all(datePropName+"AA");

	var gg = ggObj.value;
	if(parseInt(gg,10) == 0) gg = "";
	var mm = mmObj.value;
	if(parseInt(mm,10) == 0) mm = "";
	var aa = aaObj.value;
	if(parseInt(aa,10) == 0) aa = "";
	if(gg != ""){
		if(parseInt(gg,10) < 10)
			gg = "0"+parseInt(gg,10);
	}
	if(mm != "")
		if(parseInt(mm,10) < 10)
			mm = "0"+parseInt(mm,10);
	if(aa != ""){
		if(parseInt(aa,10)<100){
			aa=(parseInt(aa,10)+1900)+"";
		}
		else if((parseInt(aa,10)>=100)&&(parseInt(aa,10)<1000)){
			aa=(parseInt(aa,10)+1900)+"";
		}
	}

	ggObj.value = gg;
	mmObj.value = mm;
	aaObj.value = aa;
	if(gg != "" && mm != "" && aa != "")
		dateObj.value = gg+"-"+mm+"-"+aa;
	else
		dateObj.value = "";
		
	hideHelperAnchor(datePropName);
}

function clearDateField(datePropName){
	var dateObj = document.all(datePropName+"GG");
	dateObj.value = "";
	dateObj = document.all(datePropName+"MM");
	dateObj.value = "";
	dateObj = document.all(datePropName+"AA");
	dateObj.value = "";
	dateObj = document.all(datePropName);
	dateObj.value = "";

	// Only for timestamp fields
	var hhObj = document.all(datePropName+"HH");
	var miObj = document.all(datePropName+"MI");
	var ssObj = document.all(datePropName+"SS");
	if(hhObj != null)
		hhObj.value = "";
	if(miObj != null)
		miObj.value = "";
	if(ssObj != null)
		ssObj.value = "";
	//////////////////////////////////
}

function isDate(day,month,year) {
    var today = new Date();

    if (!day) return false

    if (!month) return false
    else month = month - 1;
    
    if (!year) return false

    var test = new Date(year,month,day);
    if ( (y2k(test.getYear()) == year) &&
         (month == test.getMonth()) &&
         (day == test.getDate()) )
        return true;
    else
        return false
}

function y2k(number) {
	return (number < 1000) ? number + 1900 : number;
}

////////////////////////////////////////////////////////////////////////////////
/////////////////////  Gestione helper sui campi    ////////////////////////////
////////////////////////////////////////////////////////////////////////////////
var curOpenedHelper = null;
function showInlineMsgHelper(anchor){
	var objTab = document.all("tabMsgHelper");
	var objFldName = document.all("divMsgHelperField");
	var objAncName = document.all("divMsgHelperAnchor");
	objFldName.value = anchor.propName;
	objAncName.value = anchor.anchorName;

	var messages = document.getElementById(anchor.propName+"HelperAnchorMsg").messages;
	var totRow=objTab.rows.length;
	for(var x=0;x<totRow-1;x++)
		objTab.deleteRow(1);
	for(var i=0;i<messages.length;i++){
		objRow   = objTab.insertRow();
		objCell  = objRow.insertCell();
		objCell.className = 'helperMessageRow';
		objCell.innerHTML = messages[i];
	}
	var objDiv = document.all("divMsgHelper");
	showInlineHelper(anchor,objDiv,"Msg");
}
function showInlineWarHelper(anchor){
	var objTab = document.all("tabWarHelper");
	var objFldName = document.all("divWarHelperField");
	var objAncName = document.all("divWarHelperAnchor");
	objFldName.value = anchor.propName;
	objAncName.value = anchor.anchorName;

	var messages = document.getElementById(anchor.propName+"HelperAnchor").messages;
	var totRow=objTab.rows.length;
	for(var x=0;x<totRow-1;x++)
		objTab.deleteRow(1);
	for(var i=0;i<messages.length;i++){
		objRow   = objTab.insertRow();
		objCell  = objRow.insertCell();
		objCell.className = 'helperWarningRow';
		objCell.innerHTML = messages[i];
	}
	var objDiv = document.all("divWarHelper");
	showInlineHelper(anchor,objDiv,"");
}
function showInlineErrHelper(anchor){
	var objTab = document.all("tabErrHelper");
	var objFldName = document.all("divErrHelperField");
	var objAncName = document.all("divErrHelperAnchor");
	objFldName.value = anchor.propName;
	objAncName.value = anchor.anchorName;

	var messages = document.getElementById(anchor.propName+"HelperAnchor").messages;
	var totRow=objTab.rows.length;
	for(var x=0;x<totRow-1;x++)
		objTab.deleteRow(1);
	for(var i=0;i<messages.length;i++){
		objRow   = objTab.insertRow();
		objCell  = objRow.insertCell();
		objCell.className = 'helperErrorRow';
		objCell.innerHTML = messages[i];
	}
	var objDiv = document.all("divErrHelper");
	showInlineHelper(anchor,objDiv,"");
}

function showInlineHelper(anchor,objDiv,type){

	if(curOpenedHelper != null)
		curOpenedHelper.style.display = 'none';

	var anchorDiv = document.all(anchor.anchorName+"HelperAnchor"+type);
	var top = getObjectPositionTop(anchorDiv);
	var left = getObjectPositionLeft(anchorDiv);
	
	if(type == "Msg"){
		top=top+22;
		left=left-10;
	}else{
		top=top+22;
		left=left-30;
	}
	objDiv.style.display='';
	
	/********* force helper position to assuring visibility ********/
	if(document.body) {
		var objDivHeight = objDiv.clientHeight;
		if((top + objDivHeight) > document.body.clientHeight){
			top = (document.body.clientHeight-objDivHeight) > 0 ? (document.body.clientHeight-objDivHeight) : 0 ;
		}
		var objDivWidth = objDiv.clientWidth;
		if( (left + objDivWidth) > document.body.clientWidth) {
			left = (document.body.clientWidth-objDivWidth) > 0 ? (document.body.clientWidth-objDivWidth) : 0 ;
		}
	}
	/***************************************************************/
	objDiv.style.top=top;
	objDiv.style.left=left;
	
	var ifrRef = document.getElementById('DivViewPort');	
    ifrRef.style.width = objDiv.offsetWidth;
    ifrRef.style.height = objDiv.offsetHeight;
    ifrRef.style.top = objDiv.style.top;
    ifrRef.style.left = objDiv.style.left;
    ifrRef.style.zIndex = objDiv.style.zIndex - 1;
    ifrRef.style.display = "block";	
	
	curOpenedHelper = objDiv;	
}

function getObjectPositionTop(obj) {
	return getOffsetTop(obj)-getScrollTop(obj);
}

function getObjectPositionLeft(obj) {
	return getOffsetLeft(obj)-getScrollLeft(obj);	
}

function getOffsetTop(obj) {
	if(obj.offsetParent != null) {
		var ret = obj.offsetTop + getOffsetTop(obj.offsetParent);
		return ret;
	}	
	return obj.offsetTop;
}

function getScrollTop(obj) {
	if(obj == null)
		return 0;
	if(obj.scrollTop && obj.scrollTop > 0)
		return obj.scrollTop;
	return getScrollTop(obj.parentNode);		
}

function getOffsetLeft(obj) {
	if(obj.offsetParent != null) {
		var ret = obj.offsetLeft + getOffsetLeft(obj.offsetParent);
		return ret;
	}	
	return obj.offsetLeft;
}

function getScrollLeft(obj) {
	if(obj == null)
		return 0;
	if(obj.scrollLeft && obj.scrollLeft > 0)
		return obj.scrollLeft;
	return getScrollLeft(obj.parentNode);		
}

function showHelper(anchorName,type,propName){

	var objRow;
	var objCell;

	if(curOpenedHelper != null)
		curOpenedHelper.style.display = 'none';

	var objProp    = document.all(propName);
	var objDiv     = document.all("div"+type+"Helper");
	var objFldName = document.all("div"+type+"HelperField");
	var objAncName = document.all("div"+type+"HelperAnchor");
	var objTab     = document.all("tab"+type+"Helper");

	objFldName.value = propName;
	objAncName.value = anchorName;

	if(type == "Msg")
		anchorName = anchorName+"MsgHelperAnchor";
	else
		anchorName = anchorName+"HelperAnchor";

	var anchor = document.all(anchorName);
	var messages = anchor.messages;

	var totRow=objTab.rows.length;
	for(var x=0;x<totRow-1;x++){
		objTab.deleteRow(1);
	}

	for(var i=0;i<messages.length;i++){
		objRow   = objTab.insertRow();
		objCell  = objRow.insertCell();
		if(type == "War")
			objCell.className = 'helperWarningRow';
		else if(type == "Msg")
			objCell.className = 'helperMessageRow';
		else
			objCell.className = 'helperErrorRow';
		objCell.innerHTML = messages[i];
	}

	var left  = anchor.getBoundingClientRect().right;
	var right = anchor.getBoundingClientRect().right;
	var top   = anchor.style.pixelTop+30;
	if(type == "Msg")
		top = anchor.style.pixelTop+20;

	/********* force helper position to assuring visibility ********/
	if(document.body) {
		objDiv.style.display='';
		var objDivHeight = objDiv.clientHeight;
		if( (top + objDivHeight) > document.body.clientHeight) {
			top = (document.body.clientHeight-objDivHeight) > 0 ? (document.body.clientHeight-objDivHeight) : 0 ;
		}
		var objDivWidth = objDiv.clientWidth;
		if( (left + objDivWidth) > document.body.clientWidth) {
			left = (document.body.clientWidth-objDivWidth) > 0 ? (document.body.clientWidth-objDivWidth) : 0 ;
		}
	}
	/***************************************************************/

	objDiv.style.left=left;
	objDiv.style.top=top;
	objDiv.style.display='';
	
	var objDivWidth = objDiv.getBoundingClientRect().right - objDiv.getBoundingClientRect().left;
	if((left - objDivWidth) < 0)
		objDiv.style.left=left;
	else
		objDiv.style.left=(left-(objDivWidth))+60;

	var ifrRef = document.getElementById('DivViewPort');	
    ifrRef.style.width = objDiv.offsetWidth;
    ifrRef.style.height = objDiv.offsetHeight;
    ifrRef.style.top = objDiv.style.top;
    ifrRef.style.left = objDiv.style.left;
    ifrRef.style.zIndex = objDiv.style.zIndex - 1;
    ifrRef.style.display = "block";	

	curOpenedHelper = objDiv;
}

function showMessageHelper(anchorName,message){

	var objRow;
	var objCell;

	if(curOpenedHelper != null)
		curOpenedHelper.style.display = 'none';

	var objDiv     = document.all("divMsgHelper");
	var objFldName = document.all("divMsgHelperField");
	var objAncName = document.all("divMsgHelperAnchor");
	var objTab     = document.all("tabMsgHelper");

	var anchor = document.all(anchorName);

	var totRow=objTab.rows.length;
	for(var x=0;x<totRow-1;x++){
		objTab.deleteRow(1);
	}

	objRow   = objTab.insertRow();
	objCell  = objRow.insertCell();
	objCell.className = 'helperMessageRow';
	objCell.innerHTML = message;

	var left  = anchor.getBoundingClientRect().left;
	var top   = anchor.getBoundingClientRect().top+15;

	/********* force helper position to assuring visibility ********/
	if(document.body) {
		objDiv.style.display='';
		var objDivHeight = objDiv.clientHeight;
		if( (top + objDivHeight) > document.body.clientHeight) {
			top = (document.body.clientHeight-objDivHeight) > 0 ? (document.body.clientHeight-objDivHeight) : 0 ;
		}
		var objDivWidth = objDiv.clientWidth;
		if( (left + objDivWidth) > document.body.clientWidth) {
			left = (document.body.clientWidth-objDivWidth) > 0 ? (document.body.clientWidth-objDivWidth) : 0 ;
		}
	}
	/***************************************************************/

	objDiv.style.left=left;
	objDiv.style.top=top;
	objDiv.style.display='';
	
	var objDivWidth = objDiv.getBoundingClientRect().right - objDiv.getBoundingClientRect().left;
	if((left - objDivWidth) < 0)
		objDiv.style.left=left;
	else
		objDiv.style.left=(left-(objDivWidth))+60;

	var ifrRef = document.getElementById('DivViewPort');	
    ifrRef.style.width = objDiv.offsetWidth;
    ifrRef.style.height = objDiv.offsetHeight;
    ifrRef.style.top = objDiv.style.top;
    ifrRef.style.left = objDiv.style.left;
    ifrRef.style.zIndex = objDiv.style.zIndex - 1;
    ifrRef.style.display = "block";	

	curOpenedHelper = objDiv;
}

function closeHelper(type){
	var divObj = document.all("div"+type+"Helper");
	divObj.style.display='none';

	curOpenedHelper = null;

	var viewport = document.getElementById('DivViewPort');
	if(viewport != null)
		viewport.style.display='none'	
}

function showHelpersAnchor(){
	var anchorItems = document.getElementsByTagName("div");
	for(var i=0; i < anchorItems.length; i++) {
		if(anchorItems[i].anchor) {
			setAnchorPosition(anchorItems[i].anchorName, anchorItems[i].anchorPosition);
		}
		else if(anchorItems[i].msgAnchor) {
			objAnchor = document.all(anchorItems[i].anchorName);
			objDiv = document.all(anchorItems[i].anchorName+"MsgHelperAnchor");
			var top = objAnchor.getBoundingClientRect().top;
			var left = objAnchor.getBoundingClientRect().right;
	
			objDiv.style.top=top+3;
			objDiv.style.left=left+3;
			objDiv.style.display='';		
		}
	}
}

function showMessageHelpersAnchor(){
	var anchorItems = document.getElementsByTagName("div");
	for(var i=0; i < anchorItems.length; i++) {
		if(anchorItems[i].msgAnchor) {
			objAnchor = document.all(anchorItems[i].anchorName);
			objDiv = document.all(anchorItems[i].anchorName+"MsgHelperAnchor");
			var top = objAnchor.getBoundingClientRect().top;
			var left = objAnchor.getBoundingClientRect().right;
	
			objDiv.style.top=top+3;
			objDiv.style.left=left+3;
			objDiv.style.display='';		
		}
	}
}

function setAnchorPosition(helpersAnchor,pos){

	CENTER_ANCHOR = 1;
	TOP_LEFT_ANCHOR = 2;
	TOP_RIGHT_ANCHOR = 3;
	BOTTOM_LEFT_ANCHOR = 4;
	BOTTOM_RIGHT_ANCHOR = 5;
	TOP_CENTER_ANCHOR = 6;
	RIGHT_CENTER_ANCHOR = 7;
	LEFT_CENTER_ANCHOR = 8;
	BOTTOM_CENTER_ANCHOR = 9;

	var	objProp = document.all(helpersAnchor);
	var	objDiv = document.all(helpersAnchor+"HelperAnchor");
	var	ifrRef = document.all(helpersAnchor+"DivViewPort");

	var delta = 10;
	var top = objProp.getBoundingClientRect().top;
	var bottom = objProp.getBoundingClientRect().bottom;
	var left = objProp.getBoundingClientRect().left;
	var right = objProp.getBoundingClientRect().right;
	
	if(pos == CENTER_ANCHOR){
		top  = top+Math.floor(((bottom-top)/2)-delta);
		left = left+Math.floor(((right-left)/2)-delta);
	}else if(pos == TOP_LEFT_ANCHOR){
		top  = top-delta;
		left = left-delta;
	}else if(pos == TOP_RIGHT_ANCHOR){
		top  = top-delta;
		left = right-delta;
	}else if(pos == BOTTOM_LEFT_ANCHOR){
		top  = bottom-delta;
		left = left-delta;
	}else if(pos == BOTTOM_RIGHT_ANCHOR){
		top  = bottom-delta;
		left = right-delta;
	}else if(pos == TOP_CENTER_ANCHOR){
		top  = top-delta;
		left = left+Math.floor(((right-left)/2)-delta);
	}else if(pos == RIGHT_CENTER_ANCHOR){
		top  = top+Math.floor(((bottom-top)/2)-delta);
		left = right-delta;
	}else if(pos == LEFT_CENTER_ANCHOR){
		top  = top+Math.floor(((bottom-top)/2)-delta);
		left = left-delta;
	}else if(pos == BOTTOM_CENTER_ANCHOR){
		top  = bottom-delta;
		left = left+Math.floor(((right-left)/2)-delta);
	}

	objDiv.style.top  = top;
	objDiv.style.left = left;
	objDiv.style.display='';

    ifrRef.style.top = objDiv.style.top;
    ifrRef.style.left = objDiv.style.left;
    ifrRef.style.zIndex = objDiv.style.zIndex - 1;
    ifrRef.style.display = "block";	
}

function hideHelperAnchor(propName){
	try{
		if(document.modifiedFieldsArray){
			var trovato = false;
			for(var i=0;i<document.modifiedFieldsArray.length;i++){
				if(document.modifiedFieldsArray[i] == propName){
					trovato = true;
					break;
				}
			}
			if(!trovato)
				document.modifiedFieldsArray.push(propName);
		}
	}catch(e){}
	
	var skipFields = document.all('skippableFields');
	if(skipFields == null){
		return;
	}
	var fieldObj = document.all(propName);
	if(fieldObj != null){
		if(fieldObj.className == 'fieldHasError' ||
		   fieldObj.className == 'fieldHasWarning'){
			if(fieldObj.isDate == 'true'){
				var dateObj = document.all(propName+"GG");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;
				dateObj = document.all(propName+"MM");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;
				dateObj = document.all(propName+"AA");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;				
			}else if(fieldObj.isTime == 'true'){
				var dateObj = document.all(propName+"GG");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;
				dateObj = document.all(propName+"MM");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;
				dateObj = document.all(propName+"AA");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;				
				dateObj = document.all(propName+"HH");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;
				dateObj = document.all(propName+"MI");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;
				dateObj = document.all(propName+"SS");
				if(dateObj) dateObj.className = 'inputField';
				if(dateObj) dateObj.readOnly = false;				
			}else{
				fieldObj.className = 'inputField';
				fieldObj.readOnly = false;
			}
			removeSkippable(propName);
		}
	}
	var divObj = document.all(propName+"HelperAnchor");
	if(divObj != null)
		divObj.style.display='none';

	var	ifrRef = document.all(propName+"DivViewPort");
	if(ifrRef != null)
		ifrRef.style.display='none';
}

function hideFieldsetHelperAnchor(fieldsetName){
	var skipFields = document.all('skippableFields');
	if(skipFields == null){
		return;
	}
	var objs = document.all(fieldsetName);
	if(objs == null)
		return;

	objs = objs.all;
	if(objs == null || objs.length == undefined)
		return;
	for(i=0;i<objs.length;i++){
		if(objs[i].name != "")
			hideHelperAnchor(objs[i].name);
	}
}

////////////////////////////////////////////////////////////////////////////////
/////////////////////     Gestione campi skippabili   //////////////////////////
////////////////////////////////////////////////////////////////////////////////
function addSkippable(propName){
	var skipFields = document.all('skippableFields');
	if(skipFields == null){
		return;
	}
	var idx = skipFields.value.indexOf(propName);
	if(idx >= 0)
		return;
	skipFields.value = skipFields.value + propName + ",";
}
function removeSkippable(propName){
	var skipFields = document.all('skippableFields');
	if(skipFields == null){
		return;
	}
	var idx = skipFields.value.indexOf(propName);
	if(idx < 0)
		return;

	var s1 = skipFields.value.substr(0,idx);
	var s2 = skipFields.value.substr(idx+propName.length+1);
	skipFields.value = s1 + s2;
}

////////////////////////////////////////////////////////////////////////////////
/////////////////////        Gestione combobox      ////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function populateCombo(combo,el){

	combo.removeAll();
	if(el == "")
		return;

	var entries = el.split(";");
	var option;
	var currentEntry;
	var isValid,isSelected,cod,desc;
	var selectIndex = -1;
	for(var i=0;i<entries.length-1;i++){

		currentEntry = entries[i].split(",");

		isValid = true;
		if(currentEntry[0] == "f")
			isValid = false;
		isSelected = true;
		if(currentEntry[1] == "f")
			isSelected = false;

		cod  = currentEntry[2];
		desc = currentEntry[3];

		combo.add(cod,desc,isValid);
	}
}

////////////////////////////////////////////////////////////////////////////////
//////////////        Gestione "GET" Submit      ////////////////////////
////////////////////////////////////////////////////////////////////////////////
function doGetSubmit(hasRequestControl, browserInstance, form, target, action){
	var formTarget;
	if(target) {
		formTarget = target;
	}
	else {
		if(!form.target || form.target == "") {
			return;
		}
		else {
			formTarget = eval(form.target)
		}
	}

	var formAction;
	if(action) {
		formAction = new String(action);
	}
	else {
		if(!form.action || form.action == "") {
			return;
		}
		else {
			formAction = new String(form.action);
		}
	}

	var queryString = "";
	var queryPrefix = formAction.lastIndexOf("?")<0 ? "?" : "&";
	var browserInstanceQuery = (browserInstance!=null) ? queryPrefix+"BrowserInstance="+browserInstance : null;

	if(form && form.all.length>0) {
		
		if(browserInstanceQuery) { 
			queryString += browserInstanceQuery + "&";
		}
		else {
			queryString += queryPrefix;
		}
		
		var length = form.all.length;
		var id;
		var value;
		for(var i=0; i<form.all.length; i++) {
			if(form.all[i].nosubmit) continue;
			id = form.all[i].id;
			value = form.all[i].value;
			if(browserInstanceQuery && id=="BrowserInstance") continue;
			if(id != "" && value!=null) {
				queryString += id + "=" + encodeString(value);
				queryString += (i!=length -1) ? "&" : "";
			}
		}
		var queryLength = queryString.length;
		if(queryString.charAt(queryLength-1) == "&") {
			queryString = queryString.substring(0, queryLength-1);                  
		}
	}
	else {
		if(browserInstanceQuery) { 
			queryString += browserInstanceQuery;
		}
	}
	
	if(hasRequestControl) {
		if(isRequestPending())
			return;
		startRequest();
	}
	formTarget.location.href = formAction + queryString;
}

////////////////////////////////////////////////////////////////////////////////
//////////////        Gestione "Silent" Submit      ////////////////////////
////////////////////////////////////////////////////////////////////////////////
function silentSubmit(form, doStartRequest, submitMethod, isAsynchronous){
	try {
		
		var onSuccessCallback = eval(form.onsuccess);
		var formAction = form.action;
		var queryString = "";
		
		submitMethod = submitMethod == null ? "post" : submitMethod;
		isAsynchronous = isAsynchronous == null? true : isAsynchronous;

/*		var fieldName = new String(field.id);
		var lastUnderscoreIndex = fieldName.lastIndexOf("_");
		if(lastUnderscoreIndex < 0) {
			return field.id;
		}
*/
		if(submitMethod == "get") {
			var newFormAction = new String(formAction);
			var lastIndex = newFormAction.lastIndexOf("?");
			if(lastIndex >= 0) {
				formAction = newFormAction.substring(0, lastIndex);
				queryString = newFormAction.substring(lastIndex+1);
			}
		}
	
		if(form && form.all.length>0) {
			queryString = queryString == "" ? queryString : queryString + "&"; 
			queryString += createQueryString(form.getElementsByTagName("input"));
			queryString += createQueryString(form.getElementsByTagName("select"));
			queryString += createQueryString(form.getElementsByTagName("textarea"));
	
			var queryLength = queryString.length;
			if(queryString.charAt(queryLength-1) == "&") {
				queryString = queryString.substring(0, queryLength-1);                  
			}
		}
	
		if(doStartRequest) {
			if(isRequestPending())
				return;
			startRequest();
		}
	
		var options =	{	method: 			submitMethod
						,	parameters: 		queryString
						,	onSuccess: 			onSubmitSuccess
						,	asynchronous:		isAsynchronous
						,	xmlparsingEnabled: 	false
						,	source: 			form	
						};
	 	new Wfem.Request( formAction , options );
	}catch(e){
		stopRequest();
	} 	
}

function createQueryString(fields) {
	var queryString = "";
	var length = fields.length;
	var id;
	for(var i=0; i<length; i++) {
		var value=null;
		if(fields[i].nosubmit) continue;
		id = fields[i].id;
		if(id == ''){
			id = fields[i].name;
		}
		if(fields[i].type == 'radio'){
			 if(fields[i].checked == true)
				value = fields[i].value;
		} else if(fields[i].type == 'checkbox'){
			 if(fields[i].checked == true)
				value = 'true';
			else
				value = 'false';
		}else{
			value = fields[i].value;
		}
		if(id != "" && value!=null) {
			queryString += id + "=" + encodeString(value);
			queryString +=  "&";
		}
	}
	return queryString;
}

function onSubmitSuccess(transport, options){
	setRequestPending(false);
	var form = options.source;
	try {
		targetObj = eval(form.target);
		var responseText = transport.responseText;
		
	 	targetObj.innerHTML = responseText;

		var scriptText;
	 	var scripts = targetObj.getElementsByTagName("script");
	 	for (var i=0; i < scripts.length; i++) {
	 		scriptText = scripts[i].innerHTML;
	 		scriptText = (scriptText == "" && scripts[i].src != "") ? getScript(scripts[i].src) : scriptText;
			eval(scriptText);
	 	}
	}catch(e){}
	 
	try {
		if(form.onsuccess!=null && form.onsuccess!='')
			eval(form.onsuccess + "(transport, options)");
	}catch(e){} 	
 	stopRequest();
}

function getScript(src) {
	var options =	{	method: 			"get"
					,	parameters: 		""
					,	onSuccess: 			onGetScriptSuccess
					,	asynchronous:		false
					,	xmlparsingEnabled: 	false
					,	script:				new Array()
					};
	new Wfem.Request( src , options );
	return options.script.pop();
}
function onGetScriptSuccess(transport, options) {
	var scriptText = transport.responseText;
	options.script.push(scriptText);
}

////////////////////////////////////////////////////////////////////////////////
//////////////        Encoding utility      ////////////////////////
////////////////////////////////////////////////////////////////////////////////
function encodeString(str) {
	var encodedString = "";
	var charCode;
	var skip;
	for(var i=0; i<str.length; i++) {
		skip = false;
		charCode = str.charCodeAt(i);
		
		if(charCode > 47 && charCode <58) 		skip = true; // number
		else if(charCode > 64 && charCode <91) 	skip = true; // upper case
		else if(charCode > 96 && charCode <123) skip = true; // lower case

		if(charCode < 16)
			encodedString += skip ? str.charAt(i) : "%0"+new Number(charCode).toString(16);
		else
			encodedString += skip ? str.charAt(i) : "%"+new Number(charCode).toString(16);
	} 
	return encodedString;
}

////////////////////////////////////////////////////////////////////////////////
///////////////////////////////   Logger   //////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function Logger(){}
Logger.prototype.debug = function(message) { this.log(message); }
Logger.prototype.warn  = function(message) { this.log(message); }
Logger.prototype.info  = function(message) { this.log(message); }
Logger.prototype.error = function(message) { this.log(message); }
Logger.prototype.log = function(message){}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////   SEMAPHORE    ////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function Semaphore(ms){
	this.ms = ms;
	this.commandList = new Array();
	this.isActived = false;
	this.processID = null;
}

Semaphore.prototype.addScriptCommand = function (script, condition, precondition, elseScript) {
	var oldLength = this.commandList.length;
	this.commandList[oldLength] = new DefaultScriptCommand(script, condition, precondition, elseScript);
	return oldLength;
}

Semaphore.prototype.addCommand = function (command) {
	var oldLength = this.commandList.length;
	this.commandList[oldLength] = command;
	return oldLength;
}

Semaphore.prototype.removeCommand = function (index) {
	delete this.commandList[index];
}

Semaphore.prototype.start = function(){
	try{
		_this = this;
		if(!this.isActived) {
			this.processID = window.setInterval('_this.executeCommandList()',this.ms);
			this.isActived = true;
		}	
	}catch(e){} 	
}

Semaphore.prototype.stop = function(){
	try{
		window.clearInterval(this.processID);
		this.isActived = false;
	}catch(e){} 	
}

Semaphore.prototype.executeCommandList = function(){
	try{
		for(var i = 0 ; i < this.commandList.length ; i++) {
			var command = this.commandList[i];
			if(command){
				try{
					if(command.execute()) {
						 this.removeCommand(i);
					}
				} 
				catch(e){ 
					this.removeCommand(i);
				}										
			}	
		}
	}
	catch(e){
		this.stop();
	} 	
}
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

function DefaultScriptCommand(script, condition, precondition, elseScript) {
	this.script = script;
	this.condition = condition;
	this.precondition = precondition;
	this.elseScript = elseScript;
}

DefaultScriptCommand.prototype.execute = function() {
	if(eval(this.precondition)) {
		if(eval(this.condition)) {
			eval(this.script);
		}
		else if(this.elseScript) {
			eval(this.elseScript);
		}
		return true;
	}
	return false;
}

//////////////////////////////////////////////////////////////////////////
// Gestione campi TimestampType
//////////////////////////////////////////////////////////////////////////
function setTimestampField(datePropName){
	var dateObj = document.all(datePropName);

	var ggObj = document.all(datePropName+"GG");
	var mmObj = document.all(datePropName+"MM");
	var aaObj = document.all(datePropName+"AA");
	var hhObj = document.all(datePropName+"HH");
	var miObj = document.all(datePropName+"MI");
	var ssObj = document.all(datePropName+"SS");

	var gg = ggObj.value;
	if(parseInt(gg,10) == 0) gg = "";
	var mm = mmObj.value;
	if(parseInt(mm,10) == 0) mm = "";
	var aa = aaObj.value;
	if(parseInt(aa,10) == 0) aa = "";
	var hh = hhObj.value;
	var mi = miObj.value;
	var ss = ssObj.value;
	
	if(gg != ""){
		if(parseInt(gg,10) < 10)
			gg = "0"+parseInt(gg,10);
	}
	if(mm != "")
		if(parseInt(mm,10) < 10)
			mm = "0"+parseInt(mm,10);
	if(aa != ""){
		if(parseInt(aa,10)<100){
			aa=(parseInt(aa,10)+1900)+"";
		}
		else if((parseInt(aa,10)>=100)&&(parseInt(aa,10)<1000)){
			aa=(parseInt(aa,10)+1900)+"";
		}
	}
	
	if(hh != ""){
		if(hh < 10)
			hh = "0"+parseInt(hh,10);
	}
	
	if(mi != ""){
		if(mi < 10)
			mi = "0"+parseInt(mi,10);
	}
	
	if(ss != ""){
		if(ss < 10)
			ss = "0"+parseInt(ss,10);
	}

	ggObj.value = gg;
	mmObj.value = mm;
	aaObj.value = aa;
	hhObj.value = hh;
	miObj.value = mi;
	ssObj.value = ss;
	
	if(gg != "" && mm != "" && aa != "" && hh != "" && mi != "" && ss != "")
		dateObj.value = gg+"-"+mm+"-"+aa+" "+hh+":"+mi+":"+ss;
	else if(gg != "" && mm != "" && aa != "")
		dateObj.value = gg+"-"+mm+"-"+aa;
	else
		dateObj.value = "";

	hideHelperAnchor(datePropName);
}

function padLeft(obj,pad) {
	if(!obj.maxLength)return;
	if((trim(obj.value)).length == 0) return;
	while(obj.value.length<obj.maxLength){
		obj.value="" + pad + obj.value;
	}		
}

function trim(s){
	while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r')){
		s = s.substring(1,s.length);}
	
	while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r')){
		s = s.substring(0,s.length-1);}
	return s;
}

function getChecked(radio){
	if(radio){
		for(i=0;i<radio.length;i++){
			if(radio[i].checked)
				return radio[i].value;
		}
	}	
	return "";
}

function setChecked(name,value,doClick){
	var i = 0;
	var radio = document.all(name);
	while(radio[i]){
		if(radio[i].value == value){
			radio[i].checked = true;
			if(doClick)
				radio[i].click();
		}else{
			radio[i].checked = false;
		}
		i++;
	}
}

function disableRadio(radio,disable){
	if(radio){
		for(i=0;i<radio.length;i++)
			if(radio[i].disabled = disable);		
	}	
}

function clearIfZeroes(obj){
	var num = new Number(obj.value);
	if(isNaN(num) || num == 0)
		obj.value = '';
}

//nelle grid recupera l'indice dato l'oggetto riga
function getIdxByObj(obj){
	var idxTemp = obj.name.lastIndexOf('_'); 
	return obj.name.substring(idxTemp-1,idxTemp);
}

function clearHTMLContainer(fatherId,clearDisabledField,clearNoClear){
	try{
		var objFather = document.getElementById(fatherId);
		if (objFather){		
			// InputType
			var nodeInputItemList = objFather.getElementsByTagName("input");
			for ( i = 0 ; i< nodeInputItemList.length;i++){
				var readonly = nodeInputItemList[i].getAttribute("readOnly");
				var noclear = (nodeInputItemList[i].getAttribute("noclear") && !clearNoClear);
				// Text
				if((nodeInputItemList[i].getAttribute("type") == "text")||(nodeInputItemList[i].getAttribute("type") == "num")) {
					if((!readonly || clearDisabledField) && !noclear) 
						nodeInputItemList[i].setAttribute("value","");
				}
				// Radio and CheckBox
				if((!readonly || clearDisabledField) && !noclear){
					if(nodeInputItemList[i].getAttribute("type") == "radio"){
						objRadio = document.getElementsByName(nodeInputItemList[i].getAttribute("name"));
						objRadio[objRadio.length-1].checked = true ;
					}
					if(nodeInputItemList[i].getAttribute("type") == "checkbox"){
						nodeInputItemList[i].setAttribute("checked",false);
						var idHiddenCheckId = nodeInputItemList[i].id.substring(0,nodeInputItemList[i].name.indexOf("Check"));
						document.getElementById(idHiddenCheckId).value = "";
					}
				}	
			}
			// Select
			var nodeSelectItemList = objFather.getElementsByTagName("select");
			for ( i = 0 ; i< nodeSelectItemList.length;i++){
				var disabled = nodeSelectItemList[i].getAttribute("disabled");
				var noclear = (nodeSelectItemList[i].getAttribute("noclear") && !clearNoClear);
				if((!disabled || clearDisabledField) && !noclear) {
					nodeSelectItemList[i].setAttribute("selectedIndex","0");
					nodeSelectItemList[i].fireEvent("onchange");
				}	
			}			
		}
	}catch(e){}		
}

function disableHTMLContainer(section,disabled,disabledNoDisabled){
	try{
		var obj = document.getElementById(section);
		var className = (disabled?"outputField":"inputField");
		var nodeInputItemList = obj.getElementsByTagName("INPUT");
		for (var i = 0; i < nodeInputItemList.length; i++) { 
			var noDisabled = (nodeInputItemList[i].getAttribute("nodisabled") && !disabledNoDisabled);
			// InputType
			if (nodeInputItemList[i].getAttribute("type") == "text"){
				nodeInputItemList[i].setAttribute("readOnly",disabled);
				nodeInputItemList[i].setAttribute("className",className);
			// Radio
			}else if (nodeInputItemList[i].getAttribute("type") == "radio"){
				if(!noDisabled){
					// invisible
					if(nodeInputItemList[i].getBoundingClientRect().top > 0)
						nodeInputItemList[i].setAttribute("disabled",disabled);
				}	
			}else if (nodeInputItemList[i].getAttribute("type") == "checked"){
				if(!noDisabled){
					nodeInputItemList[i].setAttribute("readOnly",disabled);
					nodeInputItemList[i].setAttribute("className",className);
				}
			}
		}
		var nodeSelectItemList = obj.getElementsByTagName("SELECT");
		for (var i = 0; i < nodeSelectItemList.length; i++) { 
			var noDisabled = (nodeSelectItemList[i].getAttribute("nodisabled") && !disabledNoDisabled);
			if(!noDisabled){
				nodeSelectItemList[i].setAttribute("readOnly",disabled);
				nodeSelectItemList[i].setAttribute("className",className);
			}
		}
	}catch(e){}
}

function openApplicationManual(manualId,manualTd){
	var url= __retrieveGatewayUrl()+"call.wfem?wfemCmd=loadApplicationManual&applicationManualId="+manualId;
	var openWindow = window.open("","","titlebar=yes,scrollbars=yes,resizable=yes");
	if(manualTd)
		openWindow.document.write("<title>"+manualTd.innerHTML+"</title>");
	else
		openWindow.document.write("<title>Manuale operativo</title>");
	openWindow.document.write("<body style='margin:0; padding:0;'>");
	openWindow.document.write("<iframe style='width=100%; height:100%' src='"+url+"'/>");
	openWindow.document.write("</body>");
	openWindow.document.close();	
}

//////////////////////////////////////////////////////////////////////////
// FOOTER
//////////////////////////////////////////////////////////////////////////
var calendar = null;
function wfemFooterInitCalendar(){
	calendar = new CalendarPopup('calendarDiv');
	calendar.setTodayText(calToday);
	calendar.setMonthNames(calMonth[0],calMonth[1],calMonth[2],calMonth[3],calMonth[4],calMonth[5],calMonth[6],calMonth[7],calMonth[8],calMonth[9],calMonth[10],calMonth[11]);
	calendar.setDayHeaders(calDay[0],calDay[1],calDay[2],calDay[3],calDay[4],calDay[5],calDay[6]);
	calendar.showYearNavigation();
	calendar.offsetX += 8;
	calendar.setReturnFunction('fillDate');
}

function wfemFooterInitWait(waitString){
	document.getElementById('divwait').innerHTML =
					"<div style='z-index:2001;position:relative;width:300;height:80;'>"+
					"<table width='100%' height='100%' bgcolor='#F0F0F0' style='border:solid 1px #1A458F;'>"+
					  "<tr>"+
					    "<td class='text' align='center' valign='middle' style='font-size:11pt;'>"+
							"<img src='"+__retrieveWfemLayoutResourceUrl()+"/images/wait.gif'/>&nbsp;&nbsp;"+waitString+
					    "</td>"+
					  "</tr>"+
					"</table>"+
					"</div>"+
					"<div style='filter:alpha(opacity=60);z-index:2000;background-color:silver;position:absolute;left:10;top:10;width:300;height:80;'></div>";
}

function wfemFooterInitFields(errTitle,skippableFields,warTitle,warModify,warIgnore,msgTitle){
	document.getElementById('divErrHelper').innerHTML = 
					"<input type='hidden' name='divErrHelperAnchor' id='divErrHelperAnchor' value='' disabled>"+
					"<input type='hidden' name='divErrHelperField' id='divErrHelperField' value='' disabled>"+
					"<table cellspacing='0' cellpadding='0' border='1' width='220' style='background: lemonchiffon;'>"+
					"<tr><td style='padding:5pt;'>"+
						"<table border='1' id='tabErrHelper' name='tabErrHelper' cellspacing='0' cellpadding='0' width='100%' style='background:white;'>"+
					      "<tr>"+
					        "<td>"+
						        "<table width='100%' cellspacing='0' cellpadding='0' class='helperErrorHeader'>"+
						          "<tr>"+
							        "<td onClick='closeHelper(\"Err\")'>"+errTitle+"</td>"+
							        "<td onClick='closeHelper(\"Err\")' align='right'>X</td>"+
						          "</tr>"+
						        "</table>"+
					        "</td>"+
					      "</tr>"+
					    "</table>"+
				    "</td></tr>"+
				    "</table>";
				
	document.getElementById('divWarHelper').innerHTML = 
					"<input type='hidden' name='skippableFields' id='skippableFields' value='"+skippableFields+"'>"+
					"<input type='hidden' name='divWarHelperAnchor' id='divWarHelperAnchor' value='' disabled>"+
					"<input type='hidden' name='divWarHelperField' id='divWarHelperField' value='' disabled>"+
					"<table cellspacing='0' cellpadding='0' border='1' width='230' style='background: lemonchiffon;'>"+
					"<tr><td style='padding: 5pt;'> "+
						"<table border='1' id='tabWarHelper' name='tabWarHelper' cellspacing='0' cellpadding='0' width='100%' style='background: white;'>"+
					      "<tr>"+
					        "<td>"+
					          "<table width='100%' cellspacing='0' cellpadding='0' class='helperWarningHeader'>"+
						        "<td nowrap onClick='closeHelper(\"War\")'>"+warTitle+"</td>"+
						        "<td style='padding: 1pt;'>"+
						           "<input type='button' class='action' style='font-weight: normal; font-size: 8pt; width: 40pt; height: 15pt;'"+
						           		  "onclick='warningModify();' "+
						           		  "onmouseover='this.style.textDecoration=\"underline\";' onmouseout='this.style.textDecoration=\"\";'"+
						           		  "value='"+warModify+"'>"+
						        "</td>"+
						        "<td style='padding: 1pt;'>"+
						           "<input type='button' class='action' style='font-weight: normal; font-size: 8pt; width: 40pt; height: 15pt;'"+ 
						           		  "onclick='warningIgnore();' "+
						           		  "onmouseover='this.style.textDecoration=\"underline\";' onmouseout='this.style.textDecoration=\"\";'"+
						           		  "value='"+warIgnore+"'>"+
						        "</td>"+
						        "<td onClick='closeHelper(\"War\")' align='right'>X</td>"+
						      "</table>"+
						    "</td>"+
					      "</tr>"+
					    "</table>"+
				    "</td></tr>"+
				    "</table>";

	document.getElementById('divMsgHelper').innerHTML = 
					"<input type='hidden' name='divMsgHelperAnchor' id='divMsgHelperAnchor' value='' disabled>"+
					"<input type='hidden' name='divMsgHelperField' id='divMsgHelperField' value='' disabled>"+
					"<table cellspacing='0' cellpadding='0' border='1' width='220' style='background: lemonchiffon;'>"+
					"<tr><td style='padding: 5pt;'>"+
						"<table border='1' id='tabMsgHelper' name='tabMsgHelper' cellspacing='0' cellpadding='0' width='100%' style='background: white;'>"+
					      "<tr>"+
					        "<td>"+
					          "<table width='100%' cellspacing='0' cellpadding='0' class='helperMessageHeader'>"+
					            "<tr>"+
							        "<td onClick='closeHelper(\"Msg\")'>"+msgTitle+"</td>"+
							        "<td onClick='closeHelper(\"Msg\")'align='right'>X</td>"+
							    "</tr>"+
							  "</table>"+
							"</td>"+
					      "</tr>"+
					    "</table>"+
				    "</td></tr>"+
				    "</table>";
}

function wfemFooterEnd(statusMsg){
	try{document.modifiedFieldsArray = new Array();}catch(e){}
	try{window.defaultStatus=statusMsg;}catch(e){}
}
