package prgm.ita.p.dac.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class FirmaModel extends CommandDataModel {
	private StringType urlDocumento = new StringType();
	private StringType urlFirma = new StringType();
	
	public StringType getUrlDocumento() {
		return urlDocumento;
	}
	public void setUrlDocumento(StringType urlDocumento) {
		this.urlDocumento = urlDocumento;
	}
	public StringType getUrlFirma() {
		return urlFirma;
	}
	public void setUrlFirma(StringType urlFirma) {
		this.urlFirma = urlFirma;
	}
}
