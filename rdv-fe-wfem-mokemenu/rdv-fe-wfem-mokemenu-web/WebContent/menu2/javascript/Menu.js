function Menu(){
	this.groups = new Array();
	this.currentGrp = null;
	this.currentApp = null;
	this.currentFnc = null;
}

Menu.prototype.addGroup = function(group){
	this.groups.push(group);
}

Menu.prototype.showGruppi = function(grpCode){
	if(!grpCode)
		this.currentGrp = this.groups[0];
	else
		this.currentGrp = this.getGroup(grpCode);
	if(this.currentGrp == null)
		return null;

	var html = "<table height='100%' cellpadding='0' cellspacing='0'><tr>";
	for(var i=0;i<this.groups.length;i++){
		if(this.groups[i].code == this.currentGrp.code)
			this.groups[i].selected=true;
		else
			this.groups[i].selected=false;
		html += this.groups[i].getHtml();
	}
	html += "</tr></table>";
	document.getElementById("gruppi").innerHTML = html;
	this.showApplicazioni();
	return this.currentGrp;
}

Menu.prototype.showApplicazioni = function(){
	var applications = this.currentGrp.applications;
	if(applications == null)
		return null;
	
	var html = "<table class='app' width='100%' height='100%' cellpadding='0' cellspacing='0'>"+
	           "<tr><td>"+
	           "<table><tr>";
	for(var i=0;i<applications.length;i++){
		html += applications[i].getHtml();
		if(i<(applications.length-1))
			html += "<td class='app' style='width:1px;'>"+
					"<span class='appSel' style='width:1px;'></span>"+
			        "</td>";
	}
	html += "</tr></table>";
	html += "</td></tr>";
	html += "</table>";
	document.getElementById("applicazioni").innerHTML = html;
	
	$(".app").bind({
				mouseenter: function(){
					menu.showFunzioni(this.id);
				}
	});
	return this.currentApp;
}

Menu.prototype.clearFunzioni = function(){
	if(this.currentApp == null)
		return;
	this.currentApp.clearFunzioni();
}

Menu.prototype.showFunzioni = function(appCode){
	if(this.currentApp != null)
		this.currentApp.clearFunzioni();
	this.currentApp = this.getApp(appCode);
	var functions = this.currentApp.functions;
	if(functions == null)
		return null;
	
	var html = "<table class='fnc' cellpadding='0' cellspacing='0' id='fncTable'>";
	for(var i=0;i<functions.length;i++){
		html += functions[i].getHtml();
	}
	html += "</table>";
	this.currentApp.showFunzioni(html);
	return this.currentFnc;
}

Menu.prototype.attivaFunzione = function(fncCode){
	var divWait = "<html>"+
				  "<center><br><br><br><br><br><br><br>"+
				  "<table bgcolor='#F0F0F0' cellspacing='0' cellpadding='0' border='1' width='300'>" +
				  "<tr><td align='center'><table>" +
				  "<tr><td>&nbsp;</td></tr>" +
				  "<tr><td align='center' style='font-family: Arial; font-size: 8pt; color: #1A458F; font-weight: normal'>Attendere prego...</td></tr>" +
				  "<tr><td>&nbsp;</td></tr>" +
				  "</table></td></tr>" +
				  "</table>"+
				  "</center>"+
				  "</html>";
	try{
		top.window.frames["clientarea"].document.open();
		top.window.frames["clientarea"].document.write(divWait);
	}catch(e){}
	if(this.currentApp != null)
		this.currentApp.clearFunzioni(true);
	var fnc = this.getFnc(fncCode);
	this.currentFnc = fnc;
	this.setPathLabel();
	
	resizeContainer();
	
	document.attivaFunzioneForm.code.value = fnc.code;
	document.attivaFunzioneForm.commandName.value = fnc.commandName;
	document.attivaFunzioneForm.commandType.value = fnc.commandType;
	document.attivaFunzioneForm.browserInstance.value = fnc.browserInstance;
	document.attivaFunzioneForm.isMultiTaskMode.value = getIsMultiTaskField();
	document.attivaFunzioneForm.submit();
}

Menu.prototype.getGroup = function(groupCode){
	for(var i=0;i<this.groups.length;i++){
		if(this.groups[i].code == groupCode)
			return this.groups[i];
	}
	return null;
}

Menu.prototype.getApp = function(appCode){
	var grp= this.currentGrp;
	for(var a=0;a<grp.applications.length;a++){
		var app = grp.applications[a];
		if(app.code == appCode)
			return app;
	}
	return null;
}

Menu.prototype.getFnc = function(fncCode){
	var app = this.currentApp;
	for(var f=0;f<app.functions.length;f++){
		var	fnc = app.functions[f];
		if(fnc.code == fncCode)
			return fnc;
	}
	return null;
}

Menu.prototype.setPathLabel = function(){
	var path = "";
	if(this.currentGrp != null){
		path += this.currentGrp.title;
		if(this.currentApp != null){
			path += " / "+this.currentApp.title;
			if(this.currentFnc != null){
				if(this.currentFnc.code != this.currentFnc.parentCode){
					var fnc = this.getFnc(this.currentFnc.parentCode);
					if(fnc != null)
						path += " / "+fnc.title;
				}
				path += " / <b>"+this.currentFnc.title+"</b>";
			}
		}
	}
	document.getElementById("pathLabel").innerHTML = path;
		
}
