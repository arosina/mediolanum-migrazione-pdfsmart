function doGobackAction(){
	document.goback.wfemCmd.value = "showLastDisplay";
	document.goback.submit();
	return true;
}
//#99620
function doStampaPCPAction(){	
	var title = "Stampa Questionario PCP";
	var url = "call.wfem?wfemCmd=prgm.ita.anagraficaclienti.questionari.business.StampaPCP.executeOnPopup";
	url += "&codMediolanum="+dati.codMediolanum.value;
	url += "&codPotenziale="+dati.codPotenziale.value;
	url += "&partitaIva="+dati.partitaIva.value;
	url += "&codFiscale="+dati.codFiscale.value; 
	var h = screen.height-100;
	var w = screen.width-60;
	openNewWindow(url,title,'titlebar=yes,scrollbars=yes,resizable=yes,top=10,left=10,width='+w+',height='+h);
}

function doCalcolaProfiloAction(){
	document.dati.scrollPosition.value = document.getElementById("questionario").scrollTop;
	document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.questionari.business.CalcoloProfilo.execute";
	wfemHiddenSubmit(document.dati,'body');
	return true;
}

function doSalvaQuestionarioAction(){
	document.dati.scrollPosition.value = document.getElementById("questionario").scrollTop;
	document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.questionari.business.SalvaQuestionario.execute";
	wfemHiddenSubmit(document.dati,'body');
	return true;
}

function setScrollPosition(sp){
	if(document.getElementById("questionario"))
		document.getElementById("questionario").scrollTop = sp;
}

function manageRisposteMultiple(risp){
	setBool(risp);
	return;
}

function setBool(check){
	var pn = check.getAttribute("propname");
	document.getElementById(pn).value = (check.checked ? 'S' : 'N');
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

function alertAfterCalcoloCallback(ret){
	if(ret == null)
		return;

	$("#dati input:radio").attr('disabled',false);
	$("#dati input:checkbox").attr('disabled',false);
	
	document.dati.alertAfterCalcoloViewed.value = "true";
	document.dati.wfemCmd.value = ret.cmdAfterCalcolo;
	startRequest();
	document.dati.submit();
	return;
}