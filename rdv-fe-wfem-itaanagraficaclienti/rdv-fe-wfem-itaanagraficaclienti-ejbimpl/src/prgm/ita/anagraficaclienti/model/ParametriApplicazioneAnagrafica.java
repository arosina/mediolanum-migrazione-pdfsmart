package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class ParametriApplicazioneAnagrafica extends AbstractSectionModel {

	public static String TIPO_CARTA_DIGITALE = "D";
	
	private StringType  parentBrowserInstance = new StringType("0"); // 0 per coerenza con il vecchio portale non multi-applicazione
	private StringType  runtimeModality = new StringType(Costanti.PARAM_RUNTIME_MODALITY_FULL);
	private StringType  tipoCarta		= new StringType();	
	private BooleanType isDatoreDiLavoro = new BooleanType();
	private StringType  callingAppl = new StringType();
	private StringType  callingWS = new StringType(); // Deprecato
	private StringType  contestoApplicativo = new StringType();
	private BooleanType showBack    = new BooleanType();
	private BooleanType popupMode    = new BooleanType();
	private StringType  cantiere = new StringType();
	private StringType  flagClienteSegnalato = new StringType();

	private StringType  codPotenzialeTitolare = new StringType();
	private StringType  codMediolanumTitolare = new StringType();
	private StringType  codFiscaleTitolare = new StringType();
	
	// Impersonificazione e modalita ammesse (se vuoto tutte) *******
	private StringType  codAgeImpersonato = new StringType();
	private StringType  modalitaDiSottoscrizione = new StringType();
	// **************************************************************
	
	/****************************************************************** 
	 * Campi deprecati e non più utilizzati
	 ******************************************************************/	
	@Deprecated
	public StringType getCallingWS() {
		return callingWS;
	}
	@Deprecated
	public void setCallingWS(StringType callingWS) {
		this.callingWS = callingWS;
	}
	/********************************************************************/

	/********************************************************************/
	/********************************************************************/
	public boolean isDigitale() {
		return getTipoCarta().equals(TIPO_CARTA_DIGITALE);
	}
	
	public StringType getCallingAppl() {
		return callingAppl;
	}

	public BooleanType getShowBack() {
		return showBack;
	}

	public void setCallingAppl(StringType callingAppl) {
		this.callingAppl = callingAppl;
	}

	public void setShowBack(BooleanType showBack) {
		this.showBack = showBack;
	}

	public StringType getRuntimeModality() {
		return runtimeModality;
	}

	public void setRuntimeModality(StringType runtimeModality) {
		this.runtimeModality = runtimeModality;
	}

	public BooleanType getPopupMode() {
		return popupMode;
	}

	public void setPopupMode(BooleanType popupMode) {
		this.popupMode = popupMode;
	}

	public StringType getCodFiscaleTitolare() {
		return codFiscaleTitolare;
	}

	public void setCodFiscaleTitolare(StringType codFiscaleTitolare) {
		this.codFiscaleTitolare = codFiscaleTitolare;
	}

	public StringType getCodMediolanumTitolare() {
		return codMediolanumTitolare;
	}

	public void setCodMediolanumTitolare(StringType codMediolanumTitolare) {
		this.codMediolanumTitolare = codMediolanumTitolare;
	}

	public StringType getCodPotenzialeTitolare() {
		return codPotenzialeTitolare;
	}

	public void setCodPotenzialeTitolare(StringType codPotenzialeTitolare) {
		this.codPotenzialeTitolare = codPotenzialeTitolare;
	}

	public StringType getCantiere() {
		return cantiere;
	}

	public void setCantiere(StringType cantiere) {
		this.cantiere = cantiere;
	}

	public StringType getFlagClienteSegnalato() {
		return flagClienteSegnalato;
	}

	public void setFlagClienteSegnalato(StringType flagClienteSegnalato) {
		this.flagClienteSegnalato = flagClienteSegnalato;
	}

	public BooleanType getIsDatoreDiLavoro() {
		return isDatoreDiLavoro;
	}

	public void setIsDatoreDiLavoro(BooleanType isDatoreDiLavoro) {
		this.isDatoreDiLavoro = isDatoreDiLavoro;
	}

	public StringType getTipoCarta() {
		return tipoCarta;
	}

	public void setTipoCarta(StringType tipoCarta) {
		this.tipoCarta = tipoCarta;
	}

	public StringType getContestoApplicativo() {
		return contestoApplicativo;
	}

	public void setContestoApplicativo(StringType contestoApplicativo) {
		this.contestoApplicativo = contestoApplicativo;
	}
	public StringType getCodAgeImpersonato() {
		return codAgeImpersonato;
	}
	public void setCodAgeImpersonato(StringType codAgeImpersonato) {
		this.codAgeImpersonato = codAgeImpersonato;
	}
	public StringType getModalitaDiSottoscrizione() {
		return modalitaDiSottoscrizione;
	}
	public void setModalitaDiSottoscrizione(StringType modalitaDiSottoscrizione) {
		this.modalitaDiSottoscrizione = modalitaDiSottoscrizione;
	}
	public StringType getParentBrowserInstance() {
		return parentBrowserInstance;
	}
	public void setParentBrowserInstance(StringType parentBrowserInstance) {
		this.parentBrowserInstance = parentBrowserInstance;
	}

}
