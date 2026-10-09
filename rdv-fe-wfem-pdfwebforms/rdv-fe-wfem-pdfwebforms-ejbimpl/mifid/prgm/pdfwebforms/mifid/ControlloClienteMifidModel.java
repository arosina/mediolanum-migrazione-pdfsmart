package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class ControlloClienteMifidModel extends CommandDataModel {

	private StringType  esito 				= new StringType("OK");
	private StringType  codice 				= new StringType();
	
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public StringType getCodice() {
		return codice;
	}
	public void setCodice(StringType codice) {
		this.codice = codice;
	}
	
}
