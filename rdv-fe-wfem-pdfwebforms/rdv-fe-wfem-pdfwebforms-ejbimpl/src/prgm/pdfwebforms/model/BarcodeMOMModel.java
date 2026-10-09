package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class BarcodeMOMModel extends CommandDataModel {

	private StringType canale = new StringType();
	private StringType momCode = new StringType();
	private StringType momVersion = new StringType();
	
	private StringType 	barcode = new StringType();
	private StringType 	esitoChiamataServizio = new StringType();
	private StringType 	messaggioChiamataServizio = new StringType();
	
	public StringType getCanale() {
		return canale;
	}
	public void setCanale(StringType canale) {
		this.canale = canale;
	}
	public StringType getMomCode() {
		return momCode;
	}
	public void setMomCode(StringType momCode) {
		this.momCode = momCode;
	}
	public StringType getMomVersion() {
		return momVersion;
	}
	public void setMomVersion(StringType momVersion) {
		this.momVersion = momVersion;
	}
	public StringType getBarcode() {
		return barcode;
	}
	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
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
	
}
