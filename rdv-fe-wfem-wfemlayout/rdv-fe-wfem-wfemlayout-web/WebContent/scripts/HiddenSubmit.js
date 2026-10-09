//////////////////////////////////////////////////////////////////////////
// Hidden submit
//////////////////////////////////////////////////////////////////////////
function wfemHiddenSubmit(objForm,objId){
	var whs = new _WHSClass(objForm,objId);
	whs.submitForm();
}

function __wfemHiddenSubmitEnd(whsObj,objId){
	whsObj = null;
 	try{
 		onWfemHiddenSubmitEnd(objId);
 	}catch(e){}
}

function _WHSClass(form,objId){
	this.form = form;
	if(typeof(objId) == 'undefined' || objId == null)
		this.objId = 'body';
	else
		this.objId = objId;
	this.isIe = true;
    if(!window.ActiveXObject)
        this.isIe = false;
}

_WHSClass.prototype.submitForm = function(){
	
	var form = this.form;
	var objId = this.objId;
	var isIe = this.isIe;
	
    var method = form.method ? form.method.toUpperCase() : "GET";
    var action = form.action ? form.action : document.URL;
    var dataString = this.loadFormData(form);
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
			 	if(xhr.getResponseHeader("WHSReplaceAsBody") == 'true'){
					var targetObj = document.body;
					targetObj.innerHTML = xhr.responseText;
					_WHSClassExecuteScripts(targetObj);
			    }else{
	    	 		var xmlResp = true;
	    	 		var xmlDoc;
	    	 		try{
	    	 			xmlDoc = xhr.responseXML;
		    	 		if(xmlDoc.getElementsByTagName("whsRoot").length == 0) // Html response
		    	 			xmlResp=false;
	    	 		}catch(e){
		    	 		xmlResp=false;    	 			
	    	 		}	
	    	 		if(!xmlResp){ 											// Html response
						var targetObj;
						if(objId.toLowerCase() == 'body')
							targetObj = document.body;
						else
							targetObj = document.getElementById(objId);
						targetObj.innerHTML = xhr.responseText;
					 	_WHSClassExecuteScripts(targetObj);
	    	 		}else{  				
						var ids = objId.split(',');
						var htmlParts = xmlDoc.getElementsByTagName("whsHtml");
						for(var i=0;i<ids.length;i++){
							var responseHtml = isIe ? htmlParts[i].text : htmlParts[i].textContent;
							var targetObj;
							if(ids[i].toUpperCase() == 'BODY')
								targetObj = document.body;
							else
								targetObj = document.getElementById(ids[i]);
						 	targetObj.innerHTML = responseHtml.toString();
						 	_WHSClassExecuteScripts(targetObj);
						}
	    	 		}
			    }
			 	try{if(isRequestPending()) stopRequest();}catch(e){};    	 					    	
    	 	}else{
    			alert("WfemHiddenSubmitClass.submitForm: Error on response: "+xhr.statusText+" Code: "+xhr.status);
    	 	}
	    	xhr.onreadystatechange = function(){};
	    	xhr = null;
			__wfemHiddenSubmitEnd(this,objId);
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

_WHSClass.prototype.loadFormData = function(form){

	var dataString = "";
	
	function addParam(name,value){
		dataString += (dataString.length > 0 ? "&" : "")
	      	       + escape(name).replace(/\+/g, "%2B") + "="
	               + escape(value ? value : "").replace(/\+/g, "%2B");
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
	
	manageParams(form.getElementsByTagName("input"));
	manageParams(form.getElementsByTagName("select"));
	manageParams(form.getElementsByTagName("textarea"));
    return dataString;
}
