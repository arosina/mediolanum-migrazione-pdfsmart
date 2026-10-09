function aggingiChiocciola(){
	var email = document.dati.recapiti_email.value;
	if(email.indexOf('@') >= 0)
		return;
		
	email += "@";
	document.dati.recapiti_email.value = email;
	document.dati.recapiti_email.focus();
}

