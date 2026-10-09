function verifcaPatrimonio(){
	//verifica se il patrimonio presso terzi complessivo sia superiore a 1milione 
	var importo=0;
	for(var i=1;i<=9;i++){		
		if(document.getElementById("elementiPatrimonio_importo"+i).value!=''){
			importo+=parseInt(document.getElementById("elementiPatrimonio_importo"+i).value);
		}
	}
	if(importo>1000000){
		var result=confirm("Attenzione. Il valore complessivo del patrimonio mobiliare dichiarato presso terzi e' superiore a 1 milione di \u20AC");
		if(!result){
			return false;
		}
	}
	if(importo== 0){
		alert("Compilare almeno un importo del questionario.")
		return false;
	}
		
	return true;
}

function doGobackAction(){
	document.goback.wfemCmd.value = "showLastDisplay";
	document.goback.submit();
	return true;
}

function doSalvaPatrimonioAction(){
	var v=verifcaPatrimonio()//verifico il patrimonio
	if(v==false)return;//se false blocco l'esecuzione
	document.dati.scrollPosition.value = document.getElementById("patrimonio").scrollTop;
	document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.questionari.business.SalvaPatrimonio.execute";
	wfemHiddenSubmit(document.dati,'body');
	return true;
}

function setScrollPosition(sp){
	if(document.getElementById("patrimonio"))
		document.getElementById("patrimonio").scrollTop = sp;
}
function showHelpNumSched(f){
	if(document.getElementById("helpNumSched"))
		document.getElementById("helpNumSched").style.visibility='visible';
}
function hideHelpNumSched(f){
	if(document.getElementById("helpNumSched"))
		document.getElementById("helpNumSched").style.visibility='hidden';
}

function closeMessages(){
  var obj = document.getElementById('divMessaggi');
  if(obj != null)
	  obj.style.display = 'none';
  obj = document.getElementById('divMessaggiIF');
  if(obj != null)
	  obj.style.display = 'none';	
}