function Fnc(code,title,menuLayer,commandName,commandType,browserInstance,parentCode,hasChilds){
	this.code = code;
	this.title = title;
	this.menuLayer = menuLayer;
	this.commandName = commandName;
	this.commandType = commandType;
	this.browserInstance = browserInstance;
	this.parentCode = parentCode;
	this.hasChilds = hasChilds;
}

Fnc.prototype.getHtml = function(){
	var html = "<tr>";
	
	var onclick = "";
	var onmouse = "";
	if(this.commandName != ""){
		onclick = "onclick='attivaFunzione(\""+this.code+"\");' style='cursor:pointer;'";
		onmouse = "onmouseover='this.className=\"fncSel\";' onmouseout='this.className=\"fnc\"' ";
	}
	
	if(this.menuLayer == '0'){
		var icon = "&#9642;";
		var iconStyle = "style='font-size:10pt;'";
		var titleStyle = "";
		if(this.hasChilds){
			icon = "&#9658;";
			iconStyle = "style='font-size:6pt;'";
			titleStyle = "style='font-weight:bold;'";
		}
		html += "<td "+iconStyle+">&nbsp;"+icon+"&nbsp;</td>"+
		        "<td class='fnc' "+titleStyle+" "+onmouse+onclick+" title=\""+this.title+"\">"+this.title+"</td>";		
	}else{
		html += "<td colspan='2'><table id='"+this.parentCode+"' cellspacing='0' cellpadding='0'><tr>"+
		        "<td>&nbsp;&nbsp;&nbsp;</td><td style='font-size:10pt;'>&#9642;&nbsp;</td>"+
		        "<td class='fnc' "+onmouse+onclick+" title=\""+this.title+"\">"+this.title+"</td>"+
		        "</tr></table></td>";
	}
	html += "</tr>";
	return html;
}
