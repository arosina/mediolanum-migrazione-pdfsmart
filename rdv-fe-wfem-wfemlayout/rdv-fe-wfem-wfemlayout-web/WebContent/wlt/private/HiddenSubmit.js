/* ************************************************************************************ */
/* Interfaccia per Hidden Submit */
/* ************************************************************************************ */
HiddenSubmitIntf = function(){
}

HiddenSubmitIntf.prototype.wfemHiddenSubmitEnd = function(whsObj,objId){
	whsObj = null;
 	try{ onWfemHiddenSubmitEnd(objId); }catch(e){}
}

/* ************************************************************************************ */
/* Hidden Submit */
/* ************************************************************************************ */
_WHSClass = function(form,objId,callback){
	this.form = form;
	if(typeof(objId) == 'undefined' || objId == null)
		this.objId = 'body';
	else
		this.objId = objId;
	this.isIe = isIE();
	this.callback = callback;
}

_WHSClass.prototype.submitForm = function(){
	
	var form = this.form;
	var objId = this.objId;
	var isIe = this.isIe;
	var callback = this.callback;
	var jsonModel = null;
	
    var method = form.method ? form.method.toUpperCase() : "GET";
    var action = form.action ? form.action : document.URL;
    var dataString = this.loadFormData(true);
    if(objId != null)
	    dataString += "&wfemHiddenSubmitElementId="+objId;
	
    var url = action;
    if(dataString != "" && method == "GET")
        url += "?" + dataString;
    
	var xhr;
    if(isIe){
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    }else if (window.XMLHttpRequest){
        xhr = new XMLHttpRequest();
    }else{
    	alert("WfemHiddenSubmitClass.submitForm: Error getting XMLHttpRequest");
    }

    xhr.open(method,url,true);
   
    function _WHSClassSubmitCallback() {
    	if(xhr.readyState == 4){
    	 	if(xhr.status == 200){
		    	var targetObj;
			 	if(xhr.getResponseHeader("WHSReplaceAsBody") == 'true'){
			 		$.datepicker.initialized=false;
					targetObj = document.body;
					targetObj.innerHTML = xhr.responseText;
					_WHSClassExecuteScripts(targetObj);
			 	}else if(xhr.getResponseHeader("content-type") == 'application/json'){
			 		jsonModel = eval("("+xhr.responseText+")");
			    }else{
			    	
	    	 		var xmlResp = true;
	    	 		var xmlDoc;
	    	 		try{
	    	 			xmlDoc = xhr.responseXML;
		    	 		if(xmlDoc.getElementsByTagName("whsRoot").length == 0) // Check for html response
		    	 			xmlResp=false;
	    	 		}catch(e){
		    	 		xmlResp=false;    	 			
	    	 		}
	    	 			
	    	 		if(!xmlResp){ 											// Html response
						if(objId.toLowerCase() == 'body'){
					 		$.datepicker.initialized=false;
							targetObj = document.body;
						}else{
							targetObj = document.getElementById(objId);
						}
						if(targetObj != null){
							targetObj.innerHTML = xhr.responseText;
						 	_WHSClassExecuteScripts(targetObj);
						}
	    	 		}else{  												// Xml response
						var ids = objId.split(',');
						var htmlParts = xmlDoc.getElementsByTagName("whsHtml");
						for(var i=0;i<ids.length;i++){
							var responseHtml = isIe ? htmlParts[i].text : htmlParts[i].textContent;
							if(objId.toLowerCase() == 'body'){
						 		$.datepicker.initialized=false;
								targetObj = document.body;
							}else{
								targetObj = document.getElementById(ids[i]);
							}
							if(targetObj != null){
								var tagName = targetObj.tagName.toLowerCase();
								if(tagName != 'body' && tagName != 'div' && tagName != 'td'){
									alert("Error on WfemHiddenSubmit: target must be BODY or DIV or TD");
								}else{
									targetObj.innerHTML = responseHtml.toString();
								 	_WHSClassExecuteScripts(targetObj);
								}
							}
						}
	    	 		}
			    }
			 	try{if(isRequestPending()) stopRequest();}catch(e){};    	 					    	
    	 	}else{
    			alert("WfemHiddenSubmitClass.submitForm: Error on response: "+xhr.statusText+" Code: "+xhr.status);
    	 	}
	    	xhr.onreadystatechange = function(){};
	    	xhr = null;
			__hiddenSubmitIntf.wfemHiddenSubmitEnd(this,objId);
			try{
				if(callback){
					if(callback.callbackObj)
						callback.callbackFnc.apply(callback.callbackObj,[jsonModel]);
					else
						callback.apply(this,[jsonModel]);
				}
			}catch(e){}
    	}
    	return;
    }
    
	function _WHSClassExecuteScripts(targetObj){
		var scriptText;
	 	var scripts = targetObj.getElementsByTagName("script");
	 	for (var i=0; i < scripts.length; i++) {
	 		scriptText = scripts[i].innerHTML;
			if(scriptText != "" || scripts[i].src == ""){
			   eval(scriptText);	
			}
	 	}
	}
	
	if(objId != 'none')
    	xhr.onreadystatechange = _WHSClassSubmitCallback;
	
	xhr.setRequestHeader("Ajax-Request","Wfem");
    if (method == "POST") {
        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xhr.send(dataString);
    } else{
        xhr.send(null);
    }
    return;        
}

_WHSClass.prototype.loadFormData = function(isSubmit){

	var form = this.form;
	var dataString = "";
	
	function addParam(name,value){
		if(isSubmit){
			if(!isPUM()){
				if(isIE() || navigator.userAgent.indexOf("Chrome") >= 0){
					dataString += (dataString.length > 0 ? "&" : "")
								+ escape(name).replace(/\+/g, "%2B") + "="
								+ escape(value ? value : "").replace(/\+/g, "%2B");
				}else{
					dataString += (dataString.length > 0 ? "&" : "")
	      				+ encodeURIComponent(name) + "="
	      				+ encodeURIComponent(value ? value : "");
				}
			}else{
				dataString += (dataString.length > 0 ? "&" : "")
	   	       				+ encodeURIComponent(name) + "="
	   	       				+ encodeURIComponent(value ? value : "");
			}
		}else{
			if(isIE()){
				dataString += (dataString.length > 0 ? "&" : "")
							+ escape(name).replace(/\+/g, "%2B") + "="
							+ escape(value ? value : "").replace(/\+/g, "%2B");
			}else{
				dataString += (dataString.length > 0 ? "&" : "")
	   	       				+ encodeURIComponent(name) + "="
	   	       				+ encodeURIComponent(value ? value : "");
			}
		}
	}
	
	function manageParams(fields){
	    for (var i=0;i<fields.length;i++){
	    	var field = fields[i];
	    	 
			if(field.nosubmit) 
				continue;
				
			var value = null;
			var id = field.name;
			if(!id || id == '')
				id = field.id;
			if(!id || id == '')
				continue;
				
			if(field.type.toLowerCase() == 'button'){
				continue;
			}else if(field.type.toLowerCase() == 'radio'){
				if(field.checked)
					value = field.value;
			} else if(field.type.toLowerCase() == 'checkbox'){
				value = 'false';
				if(field.checked)
					value = 'true';
			}else{
				value = field.value;
			}
			if(value != null)
            	addParam(id,value);
	    }
	}

	var fields = new Array();
	for(var i=0;i<form.elements.length;i++){
		var tagName = form.elements[i].tagName; 
		if(tagName.toLowerCase() == "input" && form.elements[i].id == 'skippableFields')
			continue; 
		if(tagName.toLowerCase() == "input" || tagName.toLowerCase() == "select" || tagName.toLowerCase() == "textarea")
			fields.push(form.elements[i]);
	}
	manageParams(fields);
	var skipFields = document.getElementById('skippableFields');
	if(skipFields == null)	
    	return dataString;
    else
    	return dataString+"&skippableFields="+skipFields.value;
}
