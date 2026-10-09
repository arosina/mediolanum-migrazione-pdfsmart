var curPropertyName;
function loadFileType(fileObj){
	if(isRequestPending())
		return;

	try{
		
		var komessage = null;
		var filename = "";
		var filesize = 0;
		
		if(window.ActiveXObject){
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
			var filenameok = /^[a-zA-Z0-9\s]+$/g.test(filename);
			if(!filenameok){
	           	komessage = filenameErrorLoadFileTypeMsg;
			}
		}
		
		var maxdim = uploadMaxSize;
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
			clearFileType(fileObj.id);
			return;
		}
		
	}catch(e){}

	startRequest();
	curPropertyName = fileObj.name;
	fileObj.parentNode.parentNode.parentNode.all(curPropertyName+'FileName').value=fileObj.value;
	var form = document.all(curPropertyName+"Form");
	form.encoding = 'multipart/form-data';
	form.all("wfemCmd").value = 'loadFileType';
	form.submit();
}
function updLoadFileType(propertyName){
	stopRequest();
	var form = document.all(propertyName+"Form");
	form.all(propertyName+"Clear").disabled = false;
	form.all(propertyName+"Preview").disabled = false;
	form.all(propertyName+"Clear").style.cursor = "hand";
	form.all(propertyName+"Preview").style.cursor = "hand";
}
function filenameErrorLoadFileType(fileName){
	stopRequest();
	alert(filenameErrorLoadFileTypeMsg);
	clearFileType(curPropertyName);	
}
function sizeErrorLoadFileType(msg){
	stopRequest();
	alert(sizeErrorLoadFileTypeMsg+msg);
	clearFileType(curPropertyName);
}
function typeErrorLoadFileType(fileTypes){
	stopRequest();
	alert(typeErrorLoadFileTypeMsg+fileTypes);
	clearFileType(curPropertyName);
}
function nameErrorLoadFileType(fileName){
	stopRequest();
	alert(nameErrorLoadFileTypeMsg);
	clearFileType(curPropertyName);	
}
function contentTypeErrorLoadFileType(fileName){
	stopRequest();
	alert(contentTypeErrorLoadFileTypeMsg);
	clearFileType(curPropertyName);	
}

function clearFileType(propertyName){
	if(isRequestPending())
		return;
	startRequest();
	var form = document.all(propertyName+"Form");
	form.encoding = 'application/x-www-form-urlencoded';
	form.all('wfemCmd').value = 'clearFileType';
	form.submit();
}
function updClearFileType(propertyName){
	stopRequest();
	var form = document.all(propertyName+"Form");
	form.reset();
	form.all(propertyName+"Clear").disabled = true;
	form.all(propertyName+"Clear").style.cursor = '';
	form.all(propertyName+"Preview").disabled = true;
	form.all(propertyName+"Preview").style.cursor = '';
}

function showFileType(propertyName,isVirusExamined){
	if(isRequestPending())
		return;
	if(!isVirusExamined){
		if(!window.confirm(virusWarningFileTypeMsg))
			return;
	}
	var fileName = "";
	try{
		fileName = document.all(propertyName+"Form").all(propertyName+"Filename").value;
		fileName = fileName.replace(/\\/g, "/");
		if(fileName.lastIndexOf("/") >= 0)
			fileName = fileName.substr(fileName.lastIndexOf("/")+1);
		fileName = escape(fileName).replace(/\+/g, "%2B");
	}catch(e){fileName = "";}
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=showFileType&propertyName="+propertyName+"&fileName="+fileName;
	var openWindow = window.open("","","titlebar=yes,scrollbars=yes,resizable=yes");
	openWindow.document.write("<title>Preview</title>");
	openWindow.document.write("<body style='margin:0; padding:0;' scroll='no'>");
	openWindow.document.write("<iframe style='width=100%; height:100%' src='"+url+"'/>");
	openWindow.document.write("</body>");
	openWindow.document.close();	
}
