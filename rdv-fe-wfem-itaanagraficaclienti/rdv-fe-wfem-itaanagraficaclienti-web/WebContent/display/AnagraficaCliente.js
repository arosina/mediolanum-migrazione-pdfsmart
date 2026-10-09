function resizeContainer(){
	try{
		parent.resizeContainer($('#htmlContainer').height());
	}catch(e){}
}

function selectTab(tab){
	var tabname = tab.getAttribute('name');
	if(document.dati.datiApplicativi_nomeTabCorrente.value == tabname)
		return;
	if(isRequestPending())
		return;
	startRequest();
	__fieldIntf.closeHelper("Err");
	__fieldIntf.closeHelper("War");
	document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.business.SelezionaTab.execute";
	document.dati.datiApplicativi_nomeTabCorrente.value = tabname;
	submitForm("tabLabelsContainer,tabContainer");
}
function padLeft(obj,pad) {
	if(!obj.maxLength)return;
	if((trim(obj.value)).length == 0) return;
	while(obj.value.length<obj.maxLength){
		obj.value="" + pad + obj.value;
	}		
}
function trim(s){
	while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r')){
		s = s.substring(1,s.length);}
	
	while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r')){
		s = s.substring(0,s.length-1);}
	return s;
}

function closeMessages(){
  var obj = document.getElementById('divMessaggi');
  if(obj != null)
	  obj.style.display = 'none';
  obj = document.getElementById('divMessaggiIF');
  if(obj != null)
	  obj.style.display = 'none';	
  try{parent.variazioneAnagraficaEffettuata();}catch(e){}
}

function aggiornaInfoResidenzeFiscali(){
	if(isRequestPending())
		return;
	startRequest();
	document.dati.wfemCmd.value = "prgm.ita.anagraficaclienti.display.AnagraficaCliente.execute";
	wfemHiddenSubmit(document.dati,"tabContainer");
}

