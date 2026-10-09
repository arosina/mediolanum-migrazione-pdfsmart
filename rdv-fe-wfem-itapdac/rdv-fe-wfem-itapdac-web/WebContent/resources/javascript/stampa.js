function stampaDac(idDac,isDac){
	var url = __retrieveGatewayUrl();
	if(isDac)
		url += "call.wfem?wfemCmd=prgm.ita.p.dac.print.StampaDacSede.executeOnPopup&idDac="+idDac;
	else
		url += "call.wfem?wfemCmd=prgm.ita.p.dac.print.StampaDac.executeOnPopup&idDac="+idDac;
	
	if(__isWlt){
		
		var title;
		if(isDac)
			title = "Dac N° "+idDac;
		else
			title = "Prit N° "+idDac;
		var w = 800;
		var h = 600;
		openPdfObject(url,title,h,w);
		
	}else{
		var w = screen.availWidth - 20; 
		var h = screen.availHeight - 50; 
		var openWindow = window.open("","","titlebar=yes, scrollbars=yes, resizable=yes, top=0, left=0, width="+w+", height="+h);
		if(isDac)
			openWindow.document.write("<title>Dac N° "+idDac+"</title>");
		else
			openWindow.document.write("<title>Prit N° "+idDac+"</title>");
		openWindow.document.write("<body style='margin:0; padding:0;'>");
		openWindow.document.write("<iframe style='width=100%; height:100%' src='"+url+"'/>");
		openWindow.document.write("</body>");
		openWindow.document.close();	
	}
}

