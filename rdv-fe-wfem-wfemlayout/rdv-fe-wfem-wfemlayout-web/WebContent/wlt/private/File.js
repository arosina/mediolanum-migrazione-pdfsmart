/* ************************************************************************************ */
/* Public calback methos called from servlet */
/* ************************************************************************************ */
function nameErrorLoadFileType(fileName){
	stopRequest();
	alert(nameErrorLoadFileTypeMsg);
	__fileIntf.clearFileType(__fileIntf.curPropertyName);	
}

function filenameErrorLoadFileType(fileName){
	stopRequest();
	alert(filenameErrorLoadFileTypeMsg);
	__fileIntf.clearFileType(__fileIntf.curPropertyName);	
}

function contentTypeErrorLoadFileType(fileName){
	stopRequest();
	alert(contentTypeErrorLoadFileTypeMsg);
	__fileIntf.clearFileType(__fileIntf.curPropertyName);	
}

function updLoadFileType(propertyName){
	stopRequest();
	try{
		document.getElementById(propertyName+"Clear").disabled = false;
		document.getElementById(propertyName+"Clear").style.cursor = "pointer";
	}catch(e){}
	try{
		document.getElementById(propertyName+"Preview").disabled = false;
		document.getElementById(propertyName+"Preview").style.cursor = "pointer";
	}catch(e){}
	
	try{
		document.getElementById(propertyName).onchangeCallbackFn();
	}catch(e){}
}

function typeErrorLoadFileType(fileTypes){
	stopRequest();
	alert(typeErrorLoadFileTypeMsg+fileTypes);
	__fileIntf.clearFileType(__fileIntf.curPropertyName);
}

function sizeErrorLoadFileType(msg){
	stopRequest();
	alert(sizeErrorLoadFileTypeMsg+msg);
	__fileIntf.clearFileType(__fileIntf.curPropertyName);
}

function updClearFileType(propertyName){
	if(isIE()){
		var fld = document.getElementById(propertyName);
		fld.form.reset();
		try{fld.focus();}catch(e){}
	}else{
		document.getElementById(propertyName).value='';
	}
	try{
		document.getElementById(propertyName+"Clear").disabled = true;
		document.getElementById(propertyName+"Clear").style.cursor = '';
	}catch(e){}
	try{
		document.getElementById(propertyName+"Preview").disabled = true;
		document.getElementById(propertyName+"Preview").style.cursor = '';
	}catch(e){}
	stopRequest();
}


/* ************************************************************************************ */
/* Upload interface object */
/* ************************************************************************************ */
FileIntf = function(){
	var curPropertyName;
}

FileIntf.prototype.loadFileType = function(fileObj){
	if(isRequestPending())
		return;
	
	try{
		
		var komessage = null;
		var filename = "";
		var filesize = 0;
		
		if(isIE()){
			var myFSO = new ActiveXObject("Scripting.FileSystemObject");
			var filepath = fileObj.value;
			var file = myFSO.getFile(filepath);
			filename = file.name;
			filesize = file.size;
		}else{
			var file = fileObj.files[0];
			filename = file.name;
			filesize = file.size;
		}
		
		var ext = "";
		var pIdx = filename.lastIndexOf(".");
		if(pIdx >= 0){
			ext = filename.substring(pIdx+1);
			filename = filename.substring(0,pIdx);
		}
		
		var filetypes = fileObj.getAttribute("filetypes");
		if(filetypes == null)
			filetypes = "";
		if(filetypes != "*"){
			if(ext == "" || filetypes.toLowerCase().indexOf(ext.toLowerCase()) < 0){
				komessage = typeErrorLoadFileTypeMsg + filetypes;
			}
		}

		if(komessage == null){
			var fileNameWithoutAcceptableChars = filename.replace(/[_-]/g, "");
			var filenameok = /^[a-zA-Z0-9\s]+$/g.test(fileNameWithoutAcceptableChars);
			if(!filenameok){
	           	komessage = filenameErrorLoadFileTypeMsg;
			}
		}
		
		var maxdim = fileObj.getAttribute("maxdim");
		if(maxdim == null)
			maxdim = uploadMaxSize;
		else
			maxdim = parseInt(maxdim, 10);
		if(maxdim > uploadMaxSize)
			maxdim = uploadMaxSize;
		if(maxdim <= 0)
			maxdim =  750*1024;
		if(komessage == null && filesize > maxdim){
			var measure = "Kb";
			var kb = Math.floor( maxdim / 1024 );
			if(kb >= 1000){
				measure = "Mb";
				kb = Math.floor( maxdim / (1024*1000) );
				if(kb >= 1000000){
					measure = "Gb";
					kb = Math.floor( maxdim / (1024*1000000) );					
				}
			}
			measure = ""+kb+" "+measure;
			komessage = sizeErrorLoadFileTypeMsg + measure;
		}
		
		if(komessage != null){
			alert(komessage);
			__fileIntf.clearFileType(fileObj.id);
			return;
		}
		
	}catch(e){}
	
	try{
		fileObj.onchangeCallbackFn = function(){ eval(fileObj.getAttribute("onchangeCallback")); };
		__fileIntf.curPropertyName = fileObj.id;
		var form = document.getElementById(fileObj.id+"Form");
		form.wfemCmd.value = 'loadFileType';
		form.submit();
		startRequest();
	}catch(e){}
}

FileIntf.prototype.clearFileType = function(propertyName){
	if(isRequestPending())
		return;
	var form = document.getElementById(propertyName+"xForm");
	form.wfemCmd.value = 'clearFileType';
	form.submit();
}

FileIntf.prototype.showFileType = function(propertyName,isVirusExamined){
	if(isRequestPending())
		return;
	if(!isVirusExamined){
		if(!window.confirm(virusWarningFileTypeMsg))
			return;
	}
	var fileName = "";
	try{
		fileName = document.getElementById(propertyName).value;
		fileName = fileName.replace(/\\/g, "/");
		if(fileName.lastIndexOf("/") >= 0)
			fileName = fileName.substr(fileName.lastIndexOf("/")+1);
		fileName = escape(fileName).replace(/\+/g, "%2B");
	}catch(e){fileName = "";}
	if(fileName == ""){
		alert("Filename not specified");
		return;
	}
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=showFileType&propertyName="+propertyName+"&fileName="+fileName;
	document.getElementById("utilIFrame").src=url;	
}
