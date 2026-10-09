package prgm.ita.anagraficaclienti.display;

import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class FlagPepFrontendStatus {
	
	private static final String S_DISABLED = "disabled";
	private static final String S_MODALITY_READ = " modality='read' ";
	private static final String S_NO = "N";
	private static final String S_CHECKED = " checked ";
	
	private String flagPepEnabled = "";
	private String flagPepSiChecked = "";
	private String flagPepSiValue = "V";
	private String flagPepNoChecked = "";
	private String flagPepNoValue = S_NO;
	private String motivazionePepEnabled = "";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static FlagPepFrontendStatus flagPepFrontendStatus(ClienteModel model, boolean modificabile, StringType flagPep, StringType originalFlagPep) {
		
		FlagPepFrontendStatus feStatus = new FlagPepFrontendStatus();
		
		// Il flag pep, in variazione, può valere S,I,V che in ogni caso significano PEP=SI. 
		// In questo caso il valore del flag non deve cambiare
		String flagPepValue = flagPep.toString();
		
		if(model.getDatiApplicativi().isVariazione()){ // Se sto mostrando una variqzione già effettuata -> non editabili
			feStatus.setFlagPepEnabled(S_DISABLED);
			feStatus.setMotivazionePepEnabled(S_MODALITY_READ);
		}else{
			// in variazione se PEP era = SI mantengo i valori del flag e lo disabilito
			if(model.getIsEffettivo().booleanValue()){ 
				flagPepFrontendStatusCliEffettivo(feStatus, flagPepValue, originalFlagPep.toString());				
			}else{
				if(flagPepValue.length() == 0 || flagPepValue.equals(S_NO))
					feStatus.setMotivazionePepEnabled(S_MODALITY_READ);
			}
		}
		
		if(flagPepValue.length() > 0){
			if(flagPepValue.equals(S_NO))
				feStatus.setFlagPepNoChecked(S_CHECKED);
			else
				feStatus.setFlagPepSiChecked(S_CHECKED);
		}
		
		if(!modificabile){ // Se pagina non editabile -> non editabii a prescindere
			feStatus.setFlagPepEnabled(S_DISABLED);
			feStatus.setMotivazionePepEnabled(S_MODALITY_READ);
		}
		return feStatus;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void flagPepFrontendStatusCliEffettivo(FlagPepFrontendStatus feStatus, String flagPepValue, String originalFlagPepValue) {
		if(originalFlagPepValue.length() > 0 && !originalFlagPepValue.equals(S_NO)){
			feStatus.setFlagPepEnabled(S_DISABLED);
			feStatus.setFlagPepSiValue(flagPepValue);
			feStatus.setMotivazionePepEnabled(S_MODALITY_READ);
		}else if(flagPepValue.length() == 0 || flagPepValue.equals(S_NO)){
			feStatus.setMotivazionePepEnabled(S_MODALITY_READ);
		}
	}
	
	public String getFlagPepEnabled() {
		return flagPepEnabled;
	}
	public void setFlagPepEnabled(String flagPepEnabled) {
		this.flagPepEnabled = flagPepEnabled;
	}
	public String getFlagPepSiChecked() {
		return flagPepSiChecked;
	}
	public void setFlagPepSiChecked(String flagPepSiChecked) {
		this.flagPepSiChecked = flagPepSiChecked;
	}
	public String getFlagPepSiValue() {
		return flagPepSiValue;
	}
	public void setFlagPepSiValue(String flagPepSiValue) {
		this.flagPepSiValue = flagPepSiValue;
	}
	public String getFlagPepNoChecked() {
		return flagPepNoChecked;
	}
	public void setFlagPepNoChecked(String flagPepNoChecked) {
		this.flagPepNoChecked = flagPepNoChecked;
	}
	public String getFlagPepNoValue() {
		return flagPepNoValue;
	}
	public void setFlagPepNoValue(String flagPepNoValue) {
		this.flagPepNoValue = flagPepNoValue;
	}
	public String getMotivazionePepEnabled() {
		return motivazionePepEnabled;
	}
	public void setMotivazionePepEnabled(String motivazionePepEnabled) {
		this.motivazionePepEnabled = motivazionePepEnabled;
	}
}
