package prgm.pdfwebforms.publisher.crafter;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class CrafterPdfAnagRuoloUtilizzatoreModel extends CommandDataModel {
	
	private StringType codiceRuoloUtilizzatore = new StringType();
	
	public StringType getCodiceRuoloUtilizzatore() {
		return codiceRuoloUtilizzatore;
	}
	public void setCodiceRuoloUtilizzatore(StringType codiceRuoloUtilizzatore) {
		this.codiceRuoloUtilizzatore = codiceRuoloUtilizzatore;
	}
}
