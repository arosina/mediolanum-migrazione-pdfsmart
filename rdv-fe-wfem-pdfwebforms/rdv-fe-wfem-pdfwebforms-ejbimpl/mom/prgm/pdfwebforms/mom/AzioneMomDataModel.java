package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class AzioneMomDataModel extends CommandDataModel {

	private StringType	azione = new StringType();
	private ListType	parametri = new ListType(ParametroAzioneMomDataModel.class);
	
	public StringType getAzione() {
		return azione;
	}
	public void setAzione(StringType azione) {
		this.azione = azione;
	}
	public ListType getParametri() {
		return parametri;
	}
	public void setParametri(ListType parametri) {
		this.parametri = parametri;
	}
	
}
