package prgm.pdfwebformsutil.drivers.dao.operazionedispositivaservices.creacodiceoperazionedispositivabmed;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class CodiceOperazioneDispositivaBMEDModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum CodiceModalitaRichiestaDispositivaEnum {
		INTERNET, CALL_CENTER, FAMILY_BANKER, FB_CARTA, FB_CE, FB_COPERNICO
	}

	public enum CodiceModalitaFirmaEnum {
		OLOGRAFA, DIGITALE, PIN
	}

	public enum CodiceCanaleOperativoEnum {
		COMMERCIALE, RECLAMI, FAX, SMART_TV, PORTALE_FB, MONITOR, INTERNET, TELEFONO, ANDROID, WINDOWS_PHONE_7, APPLE, WEB_OTTIMIZZATO, AUDIO_WEB, CARTACEO, VIDEO_WEB, FAMILY_BANKER, NETWORK_CENTER, TABLET, MAIL, CHAT, SEDE, SMS, WALLET_ANDROID, WALLET_IOS
	}

	public enum CodiceProcessoOperativoEnum {
		MOM, PEM, PEC, CSC, CD, RDV
	}

	private StringType descrizione = new StringType();
	private StringType canaleOperativo = new StringType();
	private StringType modalitaRichiestaDispositiva = new StringType();
	private StringType processoOperativo = new StringType();
	private StringType codiceProcessoOperativo = new StringType();
	private StringType modalitaFirma = new StringType();
	private StringType operazionePadre = new StringType();

	private IntegerType resultCode = new IntegerType();
	private StringType codiceOperazione = new StringType();

	public StringType getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}

	public StringType getCanaleOperativo() {
		return canaleOperativo;
	}

	public void setCanaleOperativo(StringType canaleOperativo) {
		this.canaleOperativo = canaleOperativo;
	}

	public StringType getModalitaRichiestaDispositiva() {
		return modalitaRichiestaDispositiva;
	}

	public void setModalitaRichiestaDispositiva(StringType modalitaRichiestaDispositiva) {
		this.modalitaRichiestaDispositiva = modalitaRichiestaDispositiva;
	}

	public StringType getProcessoOperativo() {
		return processoOperativo;
	}

	public void setProcessoOperativo(StringType processoOperativo) {
		this.processoOperativo = processoOperativo;
	}

	public StringType getCodiceProcessoOperativo() {
		return codiceProcessoOperativo;
	}

	public void setCodiceProcessoOperativo(StringType codiceProcessoOperativo) {
		this.codiceProcessoOperativo = codiceProcessoOperativo;
	}

	public StringType getModalitaFirma() {
		return modalitaFirma;
	}

	public void setModalitaFirma(StringType modalitaFirma) {
		this.modalitaFirma = modalitaFirma;
	}

	public StringType getOperazionePadre() {
		return operazionePadre;
	}

	public void setOperazionePadre(StringType operazionePadre) {
		this.operazionePadre = operazionePadre;
	}

	public StringType getCodiceOperazione() {
		return codiceOperazione;
	}

	public void setCodiceOperazione(StringType codiceOperazione) {
		this.codiceOperazione = codiceOperazione;
	}

	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}
}
