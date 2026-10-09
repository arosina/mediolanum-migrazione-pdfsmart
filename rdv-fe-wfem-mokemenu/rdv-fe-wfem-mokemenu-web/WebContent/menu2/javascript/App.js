var isMoz=(navigator.userAgent.toLowerCase().indexOf("gecko")>-1 || window.sidebar);

function App(code,title){
	this.selected = false;
	this.code = code;
	this.title = title;
	this.functions = new Array();
}

App.prototype.addFunction = function(funz){
	this.functions.push(funz);
}

App.prototype.getHtml = function(){
	var className = "app";
	if(this.selected)
		className = "appSel";
	var html = "<td>"+
				  "<div style='position:relative;z-index:1001;'>"+
		             "<div class='fnc' style='z-index:1002;position:absolute;left:3;top:10;height:"+fncDivHeight+";width:"+fncDivWidth+";overflow:auto;display:none;' id='"+this.code+"Fnc'>"+
		             "</div>"+
			      "</div>"+
			   "</td>"+
			   "<td class='app' id='"+this.code+"' style='cursor:pointer;' "+
			   "onmouseover='menu.showFunzioni(\""+this.code+"\");' "+
			   ">&nbsp;"+this.title+"&nbsp;</td>";
	return html;
}

App.prototype.showFunzioni = function(html){
	
	var objDiv = document.getElementById(this.code+'Fnc');
		
	document.getElementById(this.code).className = "appSel";
	
	objDiv.innerHTML = html;	
	objDiv.className='fnc';
	objDiv.style.border='solid 5px #C9D6EA';
	objDiv.style.display = "";		
	
	if(!isMoz){
		var ifrRef = document.getElementById('fncIFrame');
	    ifrRef.style.top = objDiv.getBoundingClientRect().top;
	    ifrRef.style.left = objDiv.getBoundingClientRect().left;
	    ifrRef.style.display = "block";	
	}
	
	objDiv.code = this.code;
	$(objDiv).bind({
					mouseleave: function(event) {
						menu.clearFunzioni();
					}
	});
}

App.prototype.clearFunzioni = function(movie){
	try{
		document.getElementById('fncIFrame').style.display = 'none';
		document.getElementById(this.code).className = "app";	
		var objDiv = document.getElementById(this.code+'Fnc');
		objDiv.style.display = "none";		
	}catch(e){}
}