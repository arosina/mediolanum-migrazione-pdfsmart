package prgm.pdfwebforms.idd;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DettaglioEsitoIddModel extends CommandDataModel {
	
	private StringType 	codice = new StringType();
	private StringType	descrizione = new StringType();
	
	public StringType getCodice() {
		return codice;
	}
	public void setCodice(StringType codice) {
		this.codice = codice;
	}
	public StringType getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}

}
