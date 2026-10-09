function VertTab(width){
	this.plus  = "&#9658;";
	this.minus = "&#9660;";
	this.width = "40%";
	if((typeof(width) != "undefined") && (width != ""))
		this.width = width;
	this.tabIds = new Array();
	this.tabTitles = new Array();
	this.scripts = new Array();
	this.initialized = false;
}

VertTab.prototype.addTab = function(tabId,tabTitle,script){
	this.tabIds.push(tabId);
	if(tabTitle)
		this.tabTitles.push(tabTitle);
	else
		this.tabTitles.push(tabId);
	if(script)
		this.scripts.push(script);
	else
		this.scripts.push("");
}

VertTab.prototype.showTabs = function(){
	var tabIds = this.tabIds;
	var tabTitles = this.tabTitles;
	var scripts = this.scripts;
	var res = "";
	for(var i=0;i<tabIds.length;i++){
//		var img = __retrieveWfemLayoutResourceUrl()+'/images/tab/plus.gif';
		document.getElementById(tabIds[i]).className = 'vtabContent'; 
		document.getElementById(tabIds[i]).style.display = 'none';
		document.getElementById(tabIds[i]+"Tab").innerHTML = this.htmlTab(tabIds[i],scripts[i],tabTitles[i],this.plus); 
		document.getElementById(tabIds[i]+"TabTable").isOpen = false; 
	}
	this.initialized = true;
	return;
}

VertTab.prototype.showTab = function(selectedTabId){
	var tabIds = this.tabIds;
	var tabTitles = this.tabTitles;
	var scripts = this.scripts;
	var res = "";
	if(this.initialized){
		for(var i=0;i<tabIds.length;i++){
			if(tabIds[i] == selectedTabId){
				document.getElementById(tabIds[i]).style.display = '';
				document.getElementById(tabIds[i]+"TabImg").innerHTML = this.minus;
//				document.getElementById(tabIds[i]+"TabImg").src = __retrieveWfemLayoutResourceUrl()+'/images/tab/minus.gif';
				document.getElementById(tabIds[i]+"TabTable").isOpen = true;
				return;
			}
		}		
	}else{
		for(var i=0;i<tabIds.length;i++){
			var img = this.plus;
//			var img = __retrieveWfemLayoutResourceUrl()+'/images/tab/plus.gif';
			var isOpen = false;
			if(tabIds[i] == selectedTabId){
				img = this.minus;
//				img = __retrieveWfemLayoutResourceUrl()+'/images/tab/minus.gif';
				isOpen = true;
				document.getElementById(tabIds[i]).style.display = '';
			}else{
				document.getElementById(tabIds[i]).style.display = 'none';
			}
			document.getElementById(tabIds[i]).className = 'vtabContent'; 
			document.getElementById(tabIds[i]+"Tab").innerHTML = this.htmlTab(tabIds[i],scripts[i],tabTitles[i],img); 
			document.getElementById(tabIds[i]+"TabTable").isOpen = isOpen; 
		}
	}
	this.initialized = true;
	return;	
}

VertTab.prototype.htmlTab = function(tabId,scriptCode,tabTitle,img){
	return "<table cellspacing='0' cellpadding='0' width='100%' style='padding-top:10;'>"+
			  "<tr><td width='100%' class='vtabRow'>"+
			     "<table id='"+tabId+"TabTable' width='"+this.width+"' class='vtab'  onclick='selectVertTab(this);"+scriptCode+"'><tr>"+
			       "<td width='20' id='"+tabId+"TabImg' style='font-size:8;'>"+img+"</td>"+
		   	       "<td align='left' style='white-space:nowrap;'>"+tabTitle+"</td>"+
		         "</tr></table>"+
		      "</td></tr>"+
			  "</table>";
}

function selectVertTab(tabObj){
	var cplus  = "&#9658;";
	var cminus = "&#9660;";
	
	var tabContId = tabObj.id.substring(0,(tabObj.id.length-"TabTable".length))
	var isOpen = tabObj.isOpen;
	if(isOpen){
		tabObj.isOpen = false;
		document.getElementById(tabContId+"TabImg").innerHTML = cplus;
//		document.getElementById(tabContId+"TabImg").src = __retrieveWfemLayoutResourceUrl()+'/images/tab/plus.gif';
		$(document.getElementById(tabContId)).hide('blind');
	}else{
		tabObj.isOpen = true;
		document.getElementById(tabContId+"TabImg").innerHTML = cminus;
//		document.getElementById(tabContId+"TabImg").src = __retrieveWfemLayoutResourceUrl()+'/images/tab/minus.gif';		
		$(document.getElementById(tabContId)).show('blind');
	}
}

