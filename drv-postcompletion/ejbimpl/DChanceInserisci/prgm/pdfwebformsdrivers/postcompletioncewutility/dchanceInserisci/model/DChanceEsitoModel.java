package prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/******************************************************************************/
/******************************************************************************/
public class DChanceEsitoModel extends CommandDataModel {

	private StringType 		pdfInstanceId 		= new StringType();
	private TimestampType 	dataRichiestaApertura				= new TimestampType();
	private StringType		idCSCRichiestaApertura				= new StringType();
	
	private StringType		pdfCodAgente				= new StringType();
	private StringType		pdfCodiceDispositivaBMED	= new StringType();
	
	private StringType		statoPratica		= new StringType();
	private StringType		causaleStatoPratica	= new StringType();
	
	private StringType		errorCode		= new StringType();

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}
	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}
	
	public TimestampType getDataRichiestaApertura() {
		return dataRichiestaApertura;
	}
	public void setDataRichiestaApertura(TimestampType dataRichiestaApertura) {
		this.dataRichiestaApertura = dataRichiestaApertura;
	}

	public StringType getIdCSCRichiestaApertura() {
		return idCSCRichiestaApertura;
	}
	public void setIdCSCRichiestaApertura(StringType idCSCRichiestaApertura) {
		this.idCSCRichiestaApertura = idCSCRichiestaApertura;
	}
	public StringType getPdfCodAgente() {
		return pdfCodAgente;
	}
	public void setPdfCodAgente(StringType pdfCodAgente) {
		this.pdfCodAgente = pdfCodAgente;
	}
	public StringType getPdfCodiceDispositivaBMED() {
		return pdfCodiceDispositivaBMED;
	}
	public void setPdfCodiceDispositivaBMED(StringType pdfCodiceDispositivaBMED) {
		this.pdfCodiceDispositivaBMED = pdfCodiceDispositivaBMED;
	}
	public StringType getStatoPratica() {
		return statoPratica;
	}
	public void setStatoPratica(StringType statoPratica) {
		this.statoPratica = statoPratica;
	}
	public StringType getCausaleStatoPratica() {
		return causaleStatoPratica;
	}
	public void setCausaleStatoPratica(StringType causaleStatoPratica) {
		this.causaleStatoPratica = causaleStatoPratica;
	}
	public StringType getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(StringType errorCode) {
		this.errorCode = errorCode;
	}
	
}
