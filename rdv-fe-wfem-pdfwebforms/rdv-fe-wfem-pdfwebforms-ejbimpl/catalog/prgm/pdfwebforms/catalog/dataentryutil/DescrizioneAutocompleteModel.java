package prgm.pdfwebforms.catalog.dataentryutil;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DescrizioneAutocompleteModel extends CommandDataModel {

	private StringType 	codice 				= new StringType();
	private StringType  descrizione 		= new StringType();

	public StringType getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}

	public StringType getCodice() {
		return codice;
	}

	public void setCodice(StringType codice) {
		this.codice = codice;
	}


}
