function HorzTab(height){
	this.tabId = "";
	this.tabIds = new Array();
	this.tabTitles = new Array();
	this.scripts = new Array();
	this.images = new Array();
	this.disImages = new Array();
	this.tabHeight = "23px;";
	if((typeof(height) != "undefined") && (height != ""))
		this.tabHeight = height;
}

HorzTab.prototype.addTab = function(tabId,tabTitle,script,image){
	this.tabId += tabId;
	this.tabIds.push(tabId);
	if(tabTitle)
		this.tabTitles.push(tabTitle);
	else
		this.tabTitles.push(tabId);
	if(script)
		this.scripts.push(script);
	else
		this.scripts.push("");
	if(image){
		this.images.push(image);
		this.disImages.push(image.substring(0,image.length-4)+"Disabled"+image.substring(image.length-4));
	}else{
		this.images.push("");
		this.disImages.push("");
	}
}

HorzTab.prototype.appendTo = function(id){
	document.getElementById(id).innerHTML = this.htmlTab();
	var tabId = this.tabId;
	var tabs = $("td[name='"+tabId+"']");
	tabs.data('tabs',this);
	tabs.bind({
					click: function(){
						var tabs = $(this).data('tabs');
						tabs.selectTab(this.id.substring(0,this.id.length-"Tab".length),true);
					}
	});
}

HorzTab.prototype.htmlTab = function(varName){
	var onclick = "";
	var tabIds = this.tabIds;
	var tabTitles = this.tabTitles;
	var scripts = this.scripts;
	var images = this.images;
	var disImages = this.disImages;
	var res = "<table width='100%' height='"+this.tabHeight+"' cellspacing='0' cellpadding='0'><tr>";
	for(var i=0;i<tabIds.length;i++){
		if(varName && varName != "")
			onclick = "onclick='"+varName+".selectTab(\""+tabIds[i]+"\",true);";
		res += "<td class='htab' name='"+this.tabId+"' id='"+tabIds[i]+"Tab' valign='middle' "+onclick+scripts[i]+"'>";
		if(images[i] != "")
			res += "<img id='"+tabIds[i]+"TabImg' src='"+disImages[i]+"' style='vertical-align:middle;'>";
		res += "&nbsp;<span id='"+tabIds[i]+"TabTitle'>"+tabTitles[i]+"</span>&nbsp;";
		res += "<img id='"+tabIds[i]+"TabWaitImg' src='"+__retrieveWfemLayoutResourceUrl()+"/images/tab/empty.gif'>";
		res += "</td>";
	}				
	res += "<td class='htabBottomBar'>&nbsp;&nbsp;</td>"+
		   "</tr></table>";
	return res;	
}

HorzTab.prototype.selectTab = function(tabId,onclick){
	var tabIds = this.tabIds;
	var images = this.images;
	var disImages = this.disImages;
	for(var i=0;i<tabIds.length;i++){
		if(tabIds[i] == tabId){
			document.getElementById(tabIds[i]).style.display = "";
			document.getElementById(tabIds[i]+"Tab").className = "htabSelected";
			if(images[i] != '')
				document.getElementById(tabIds[i]+"TabImg").src = images[i];
		}else {
			document.getElementById(tabIds[i]).style.display = "none";
			document.getElementById(tabIds[i]+"Tab").className = "htab";
			if(disImages[i] != '')
				document.getElementById(tabIds[i]+"TabImg").src = disImages[i];
		}
	}	
	if(onclick){
		if(document.keepalive)
			document.keepalive.submit();
	}
}

HorzTab.prototype.setTabTitle = function(tabId,title){
	var tabIds = this.tabIds;
	for(var i=0;i<tabIds.length;i++){
		if(tabIds[i] == tabId){
			document.getElementById(tabIds[i]+"TabTitle").innerText = title;
			break;
		}
	}	
}
