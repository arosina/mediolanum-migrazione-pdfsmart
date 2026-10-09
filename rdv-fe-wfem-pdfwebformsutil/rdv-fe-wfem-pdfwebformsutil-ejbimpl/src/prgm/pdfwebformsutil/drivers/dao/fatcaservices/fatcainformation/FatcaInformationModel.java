package prgm.pdfwebformsutil.drivers.dao.fatcaservices.fatcainformation;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class FatcaInformationModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * statiFatca "O" - "FuoriAmbito" statiFatca "V" - "ValutazioneInCorso"
	 * statiFatca "P" - "ValutazioneInCorso" statiFatca "K" - "ValutazioneInCorso"
	 * statiFatca "R" - "ValutazioneInCorso" statiFatca "S" - "UsPerson" statiFatca
	 * "N" - "NonUsPerson" statiFatca "X" - "VerificatoSenzaIndizi"
	 */

	private StringType webCookie = new StringType();
	private IntegerType resultCode = new IntegerType();
	private StringType tipoSoggetto = new StringType();
	private StringType codiceCliente = new StringType();
	private StringType cittadinanza1 = new StringType();
	private StringType cittadinanza2 = new StringType();
	private StringType nazioneResidenza = new StringType();
	private StringType nazioneResidenzaFiscale1 = new StringType();
	private StringType nazioneResidenzaFiscale2 = new StringType();
	private StringType nazioneResidenzaFiscale3 = new StringType();
	private StringType nazioneNascita = new StringType();
	private StringType prefissoTelefInternaz = new StringType();
	private StringType fatcaStatus = new StringType();
	private StringType indizioForteNonSuperabile = new StringType();
	private StringType indizioForteSuperabile = new StringType();
	private StringType indizioDebole = new StringType();
	private StringType tin = new StringType();

	public StringType getFatcaStatus() {
		return fatcaStatus;
	}

	public void setFatcaStatus(StringType fatcaStatus) {
		this.fatcaStatus = fatcaStatus;
	}

	public StringType getIndizioForteNonSuperabile() {
		return indizioForteNonSuperabile;
	}

	public void setIndizioForteNonSuperabile(StringType indizioForteNonSuperabile) {
		this.indizioForteNonSuperabile = indizioForteNonSuperabile;
	}

	public StringType getIndizioForteSuperabile() {
		return indizioForteSuperabile;
	}

	public void setIndizioForteSuperabile(StringType indizioForteSuperabile) {
		this.indizioForteSuperabile = indizioForteSuperabile;
	}

	public StringType getIndizioDebole() {
		return indizioDebole;
	}

	public void setIndizioDebole(StringType indizioDebole) {
		this.indizioDebole = indizioDebole;
	}

	public StringType getTin() {
		return tin;
	}

	public void setTin(StringType tin) {
		this.tin = tin;
	}

	public StringType getWebCookie() {
		return webCookie;
	}

	public void setWebCookie(StringType webCookie) {
		this.webCookie = webCookie;
	}

	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

	public StringType getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(StringType tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public StringType getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public StringType getCittadinanza1() {
		return cittadinanza1;
	}

	public void setCittadinanza1(StringType cittadinanza1) {
		this.cittadinanza1 = cittadinanza1;
	}

	public StringType getCittadinanza2() {
		return cittadinanza2;
	}

	public void setCittadinanza2(StringType cittadinanza2) {
		this.cittadinanza2 = cittadinanza2;
	}

	public StringType getNazioneResidenza() {
		return nazioneResidenza;
	}

	public void setNazioneResidenza(StringType nazioneResidenza) {
		this.nazioneResidenza = nazioneResidenza;
	}

	public StringType getNazioneResidenzaFiscale1() {
		return nazioneResidenzaFiscale1;
	}

	public void setNazioneResidenzaFiscale1(StringType nazioneResidenzaFiscale1) {
		this.nazioneResidenzaFiscale1 = nazioneResidenzaFiscale1;
	}

	public StringType getNazioneResidenzaFiscale2() {
		return nazioneResidenzaFiscale2;
	}

	public void setNazioneResidenzaFiscale2(StringType nazioneResidenzaFiscale2) {
		this.nazioneResidenzaFiscale2 = nazioneResidenzaFiscale2;
	}

	public StringType getNazioneResidenzaFiscale3() {
		return nazioneResidenzaFiscale3;
	}

	public void setNazioneResidenzaFiscale3(StringType nazioneResidenzaFiscale3) {
		this.nazioneResidenzaFiscale3 = nazioneResidenzaFiscale3;
	}

	public StringType getNazioneNascita() {
		return nazioneNascita;
	}

	public void setNazioneNascita(StringType nazioneNascita) {
		this.nazioneNascita = nazioneNascita;
	}

	public StringType getPrefissoTelefInternaz() {
		return prefissoTelefInternaz;
	}

	public void setPrefissoTelefInternaz(StringType prefissoTelefInternaz) {
		this.prefissoTelefInternaz = prefissoTelefInternaz;
	}

	public boolean isInValutazione() {
		return (getFatcaStatus().equals("V") || getFatcaStatus().equals("P") || getFatcaStatus().equals("K")
				|| getFatcaStatus().equals("R"));
	}

	public boolean isUSPerson() {
		return (getFatcaStatus().equals("S"));
	}
}
