function MotivazionePep(){
	// This is intentional
}

MotivazionePep.prototype.manageCasoDecesso = function(){
	manageMotivazionePep("Beneficiario");
}

MotivazionePep.prototype.manageCasoVita = function(){
	manageMotivazionePep("BeneficiarioVita");
}

MotivazionePep.prototype.manage = function(prefissoBeneficiario){
	manageMotivazionePep(prefissoBeneficiario);
}

MotivazionePep.prototype.manageIsPepCasoDecesso = function(indiceBeneficiario){
	manageIsPep("Beneficiario", indiceBeneficiario);
}

MotivazionePep.prototype.manageIsPepCasoVita = function(indiceBeneficiario){
	manageIsPep("BeneficiarioVita", indiceBeneficiario);
}

MotivazionePep.prototype.manageIsPep = function(prefissoBeneficiario, indiceBeneficiario){
	manageIsPep(prefissoBeneficiario, indiceBeneficiario);
}

MotivazionePep.prototype.manageIsPepTitolare = function(prefissoBeneficiario, indiceBeneficiario, indiceTitolare){
	manageIsPepTitolare(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
}

MotivazionePep.prototype.changeAnagraficaPep = function(prefissoBeneficiario, indiceBeneficiario){
	changeAnagraficaPep(prefissoBeneficiario, indiceBeneficiario);
}

MotivazionePep.prototype.changeAnagraficaTitolarePep = function(prefissoBeneficiario, indiceBeneficiario, indiceTitolare){
	changeAnagraficaTitolarePep(prefissoBeneficiario, indiceBeneficiario, indiceTitolare);
}

var motivazionePep = new MotivazionePep();
