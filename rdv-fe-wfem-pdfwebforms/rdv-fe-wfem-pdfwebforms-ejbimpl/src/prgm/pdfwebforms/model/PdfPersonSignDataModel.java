package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPersonSignDataModel extends CommandDataModel {

	private StringType 	statoCelluarePrimario = new StringType();
	private StringType 	numeroCelluarePrimario = new StringType();
	private StringType 	prefissoCellulareAnag = new StringType();
	private StringType 	numeroCellulareAnag = new StringType();
	private StringType 	idCertificationAuthority = new StringType();
	private StringType 	statoFirmaDigitale = new StringType();

	private boolean		stepPinSuperato = false;
	private boolean		stepFlagSignSuperato = false;
	private boolean		stepOtpSuperato = false;
	
	private StringType 	otpGenerato = new StringType();
	private StringType 	otpSerialNumber = new StringType();
	private StringType 	otpDigitato = new StringType();
	private StringType 	savOtpDigitato = new StringType();
	private IntegerType digit1DaChiedere = new IntegerType();
	private IntegerType digit2DaChiedere = new IntegerType();
	private StringType  digit1Digitato = new StringType();
	private StringType  digit2Digitato = new StringType();
	
	private StringType  chiaveVerificaPin = new StringType();
	private StringType 	esitoChiamataServizio = new StringType();
	private StringType 	messaggioChiamataServizio = new StringType();
	
	private BooleanType	leggiIlContrattoClicked = new BooleanType();
	
	public boolean isStepPinSuperato() {
		return stepPinSuperato;
	}
	public void setStepPinSuperato(boolean stepPinSuperato) {
		this.stepPinSuperato = stepPinSuperato;
	}
	public boolean isStepOtpSuperato() {
		return stepOtpSuperato;
	}
	public void setStepOtpSuperato(boolean stepOtpSuperato) {
		this.stepOtpSuperato = stepOtpSuperato;
	}
	public StringType getStatoFirmaDigitale() {
		return statoFirmaDigitale;
	}
	public void setStatoFirmaDigitale(StringType statoFirmaDigitale) {
		this.statoFirmaDigitale = statoFirmaDigitale;
	}
	public StringType getOtpGenerato() {
		return otpGenerato;
	}
	public void setOtpGenerato(StringType otpGenerato) {
		this.otpGenerato = otpGenerato;
	}
	public StringType getOtpSerialNumber() {
		return otpSerialNumber;
	}
	public void setOtpSerialNumber(StringType otpSerialNumber) {
		this.otpSerialNumber = otpSerialNumber;
	}
	public StringType getOtpDigitato() {
		return otpDigitato;
	}
	public void setOtpDigitato(StringType otpDigitato) {
		this.otpDigitato = otpDigitato;
	}
	public IntegerType getDigit1DaChiedere() {
		return digit1DaChiedere;
	}
	public void setDigit1DaChiedere(IntegerType digit1DaChiedere) {
		this.digit1DaChiedere = digit1DaChiedere;
	}
	public IntegerType getDigit2DaChiedere() {
		return digit2DaChiedere;
	}
	public void setDigit2DaChiedere(IntegerType digit2DaChiedere) {
		this.digit2DaChiedere = digit2DaChiedere;
	}
	public StringType getDigit1Digitato() {
		return digit1Digitato;
	}
	public void setDigit1Digitato(StringType digit1Digitato) {
		this.digit1Digitato = digit1Digitato;
	}
	public StringType getDigit2Digitato() {
		return digit2Digitato;
	}
	public void setDigit2Digitato(StringType digit2Digitato) {
		this.digit2Digitato = digit2Digitato;
	}
	public StringType getChiaveVerificaPin() {
		return chiaveVerificaPin;
	}
	public void setChiaveVerificaPin(StringType chiaveVerificaPin) {
		this.chiaveVerificaPin = chiaveVerificaPin;
	}
	public StringType getEsitoChiamataServizio() {
		return esitoChiamataServizio;
	}
	public void setEsitoChiamataServizio(StringType esitoChiamataServizio) {
		this.esitoChiamataServizio = esitoChiamataServizio;
	}
	public StringType getMessaggioChiamataServizio() {
		return messaggioChiamataServizio;
	}
	public void setMessaggioChiamataServizio(StringType messaggioChiamataServizio) {
		this.messaggioChiamataServizio = messaggioChiamataServizio;
	}
	public StringType getStatoCelluarePrimario() {
		return statoCelluarePrimario;
	}
	public void setStatoCelluarePrimario(StringType statoCelluarePrimario) {
		this.statoCelluarePrimario = statoCelluarePrimario;
	}
	public StringType getNumeroCelluarePrimario() {
		return numeroCelluarePrimario;
	}
	public void setNumeroCelluarePrimario(StringType numeroCelluarePrimario) {
		this.numeroCelluarePrimario = numeroCelluarePrimario;
	}
	public StringType getIdCertificationAuthority() {
		return idCertificationAuthority;
	}
	public void setIdCertificationAuthority(StringType idCertificationAuthority) {
		this.idCertificationAuthority = idCertificationAuthority;
	}
	public StringType getPrefissoCellulareAnag() {
		return prefissoCellulareAnag;
	}
	public void setPrefissoCellulareAnag(StringType prefissoCellulareAnag) {
		this.prefissoCellulareAnag = prefissoCellulareAnag;
	}
	public StringType getNumeroCellulareAnag() {
		return numeroCellulareAnag;
	}
	public void setNumeroCellulareAnag(StringType numeroCellulareAnag) {
		this.numeroCellulareAnag = numeroCellulareAnag;
	}
	public StringType getSavOtpDigitato() {
		return savOtpDigitato;
	}
	public void setSavOtpDigitato(StringType savOtpDigitato) {
		this.savOtpDigitato = savOtpDigitato;
	}
	public boolean isStepFlagSignSuperato() {
		return stepFlagSignSuperato;
	}
	public void setStepFlagSignSuperato(boolean stepFlagSignSuperato) {
		this.stepFlagSignSuperato = stepFlagSignSuperato;
	}
	public BooleanType getLeggiIlContrattoClicked() {
		return leggiIlContrattoClicked;
	}
	public void setLeggiIlContrattoClicked(BooleanType leggiIlContrattoClicked) {
		this.leggiIlContrattoClicked = leggiIlContrattoClicked;
	}
	
}
