function Grp(code,title){
	this.selected = false;
	this.code = code;
	this.title = title;
	this.applications = new Array();
}

Grp.prototype.addApplication = function(application){
	this.applications.push(application);
}

Grp.prototype.getHtml = function(){
	var	style = "style='cursor:pointer;text-decoration:underline;'"
	var sfondoClass = "grpSf";
	var tabClass = "grp";
	if(this.selected){
		style="";
		sfondoClass = "grpSelSf"
		tabClass = "grpSel";
	}
	var html = 
	"<td class='separaTAB'>&nbsp;</td>"+
	"<td align='left' valign='top' class='"+sfondoClass+"'><img src='/"+webApp+"/menu2/images/angolosx.gif' width='2' height='2'></td>"+
	"<td nowrap align='center' class='"+tabClass+"' style='cursor:pointer;'>"+
  	  "&nbsp;<span align='center' class='"+tabClass+"' "+style+"' onclick='menu.showGruppi(\""+this.code+"\");'>"+this.title+"</span>&nbsp;"+
	"</td>"+
	"<td align='right' valign='top' class='"+sfondoClass+"'><img src='/"+webApp+"/menu2/images/angolodx.gif' width='2' height='2'></td>";
	return html;
}
