function gestioneCellePlichi(cell){
	try{
		if(cell.propertyName != 'idPlico')
			return;

		if(cell.row.isLastInPlico != 'true')
			cell.style.borderBottom = 'none';
		
		var checkboxBgColor = "#d0e4ef";
		if(cell.row.isFirstInPlico == 'true' && cell.row.isLastInPlico == 'true')
			checkboxBgColor = "white";
		var checkbox = "";	
		// Così capisco se sono in lettura o no.
		// 03-12-2009: il documento in errore smistamento non può essere pinzato e viene quindi trattato come se la DAC fosse in sola lettura
		if(document.getElementById("pinzaLabel") != null && (cell.row.isErroreSmistamento == 'false' || cell.row.isErroreSmistamento == null)){
			if(cell.row.isFirstInPlico == 'true'){
				checkbox = "<td title='Seleziona per pinzare/spinzare' bgcolor='"+checkboxBgColor+"' align='center' width='50%' style='cursor:pointer;'>"+ 
							 "<input type='checkbox' id='documenti"+cell.row.absIndex+"_isSelected' name='documenti"+cell.row.absIndex+"_isSelected' onclick='attivaPinzaLabels();'>"+
						   "</td>";
			}else{
				checkbox = "<td width='50%' bgcolor='"+checkboxBgColor+"'></td>";
			}
		}
		
		cell.onmouseover = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
		cell.onmouseout = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
		cell.onclick = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
		cell.style.padding = '0px';
		cell.style.cursor = 'default';

		var textStyle = "";
		if(cell.row.codAggregatore.indexOf("DACGUI-") < 0)		
			textStyle = "font-style:italic;";
		var fieldHtml = "<table class='text' width='100%' height='100%' cellpadding='0' cellspacing='0' style='background-color:#d0e4ef;"+textStyle+"'>" +
						"  <tr>"+
						"   <td title='Plico' align='center'>"+
						"      "+cell.innerHTML+
						"   </td>" +
						checkbox+
						"  </tr>" +
						"</table>";

		var html = "";
		if(cell.row.isFirstInPlico == 'true' && cell.row.isLastInPlico == 'true'){
			
			html =  "<table class='text' width='100%' height='100%' cellpadding='0' cellspacing='0' style='background-color:white;'>" +
					"  <tr>"+
					"   <td title='Plico' align='center'>---</td>" +
					checkbox+
					"  </tr>" +
					"</table>";
			
		}else if(cell.row.isFirstInPlico == 'true'){
			
			html += '<table width="100%" height="100%" cellpadding="0" cellspacing="0">'+ 
				      '<tr>'+
				        '<td valign="bottom"><img src="'+__retrieveResourceUrl()+'/images/roundborder/topLeft.gif"></td>'+
				        '<td valign="bottom" width="100%" style="background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/top.gif\');background-repeat:repeat-x;background-position: bottom right;"></td>'+
				        '<td valign="bottom"><img src="'+__retrieveResourceUrl()+'/images/roundborder/topRight.gif"></td>'+
				      '</tr>'+	
				      '<tr>'+
				        '<td style="width:6px;background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/left.gif\');background-repeat:repeat-y;background-position: top left;"></td>'+
		    	        '<td>';
			html += fieldHtml;
			html +=     '</td>'+
		    		    '<td style="width:6px;background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/right.gif\');background-repeat:repeat-y;background-position: top right;"></td>'+
		  		 	  '</tr>'+
					'</table>';
					
		}else if(cell.row.isLastInPlico == 'true'){
			
			html += '<table width="100%" height="100%" cellpadding="0" cellspacing="0">'+ 
				      '<tr>'+
				        '<td style="width:6px;background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/left.gif\');background-repeat:repeat-y;background-position: top left;"></td>'+
		    	        '<td>';
			html += fieldHtml;
			html +=     '</td>'+
		    		    '<td style="width:6px;background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/right.gif\');background-repeat:repeat-y;background-position: top right;"></td>'+
		  		 	  '</tr>'+
		  			  '<tr>'+
		    		     '<td valign="top"><img src="'+__retrieveResourceUrl()+'/images/roundborder/bottomLeft.gif"></td>'+
		    			 '<td valign="top" width="100%" style="background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/bottom.gif\');background-repeat:repeat-x;background-position: top right;"></td>'+
		    			 '<td valign="top"><img src="'+__retrieveResourceUrl()+'/images/roundborder/bottomRight.gif"></td>'+
		  			  '</tr>'+
					'</table>';
					
		}else{

			html += '<table width="100%" height="100%" cellpadding="0" cellspacing="0">'+ 
				      '<tr>'+
				        '<td style="width:6px;background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/left.gif\');background-repeat:repeat-y;background-position: top left;"></td>'+
		    	        '<td>';
			html += fieldHtml;
			html +=     '</td>'+
		    	        '<td style="width:6px;background-image:url(\''+__retrieveResourceUrl()+'/images/roundborder/right.gif\');background-repeat:repeat-y;background-position: top right;"></td>'+
		  		 	  '</tr>'+
					'</table>';
			
		}	
		
		cell.innerHTML = html;
		
	}catch(e){}
}

function attivaPinzaLabels(){
	var plichiSelezionati = 0;
	var docSelezionati = 0;
	var rows = document.documentiGrid.tableModel.rows;
	for(var i=0;i<rows.length;i++){
		var c = document.getElementById("documenti"+i+"_isSelected");
		if(c == null)
			continue;
		if(c.checked){
			if(document.datiRead.fnc.value == 'spunta' || document.datiRead.isAutoSpuntata.value == 'true' || document.datiRead.tipoDac.value == '5'){
				if(rows[i].codAggregatore != '')
					plichiSelezionati++;
				else
					docSelezionati++;
			}else{
				if(rows[i].codAggregatore != '' && rows[i].codAggregatore.indexOf("DACGUI-") >= 0)
					plichiSelezionati++;
				else
					docSelezionati++;
			}
		}
	}
	if(plichiSelezionati+docSelezionati <= 1){
		attivaPinza(false);
	}else{
		attivaPinza(true);
	}

	if(docSelezionati == 0 && plichiSelezionati > 0){
		attivaSpinza(true);
	}else{
		attivaSpinza(false);
	}
}

function attivaPinza(attiva){
	if(attiva){
		document.getElementById("pinzaLabel").style.color='#F4670A';
		document.getElementById("pinzaLabel").style.cursor='pointer';
		document.getElementById("pinzaLabel").style.textDecoration='underline';
		document.getElementById("pinzaLabel").onclick=pinzaDocumenti;
	}else{
		document.getElementById("pinzaLabel").style.color='silver';
		document.getElementById("pinzaLabel").style.cursor='default';
		document.getElementById("pinzaLabel").style.textDecoration='none';		
		document.getElementById("pinzaLabel").onclick=new function(){};
	}
}

function attivaSpinza(attiva){
	if(attiva){
		document.getElementById("spinzaLabel").style.color='#F4670A';
		document.getElementById("spinzaLabel").style.cursor='pointer';
		document.getElementById("spinzaLabel").style.textDecoration='underline';
		document.getElementById("spinzaLabel").onclick=spinzaDocumenti;
	}else{
		document.getElementById("spinzaLabel").style.color='silver';
		document.getElementById("spinzaLabel").style.cursor='default';
		document.getElementById("spinzaLabel").style.textDecoration='none';		
		document.getElementById("spinzaLabel").onclick=new function(){};
	}
}

function pinzaDocumenti(){
	var	ret = testChangeDoc('ok','Il documento &egrave; stato variato.<br>Effettuare il salvataggio o chiuderlo prima di pinzare il plico');
	if(ret != 'continue')
		return;
	startRequest();
	document.documentiForm.wfemCmd.value='prgm.ita.p.dac.business.PinzaDocumenti.execute';
	wfemHiddenSubmit(document.documentiForm,'documentiCont,finalsDacScriptsCont');
}

function spinzaDocumenti(){
	var	ret = testChangeDoc('ok','Il documento &egrave; stato variato.<br>Effettuare il salvataggio o chiuderlo prima di spinzare il plico');
	if(ret != 'continue')
		return;
	startRequest();
	document.documentiForm.wfemCmd.value='prgm.ita.p.dac.business.SpinzaDocumenti.execute';
	wfemHiddenSubmit(document.documentiForm,'documentiCont,finalsDacScriptsCont');
}
