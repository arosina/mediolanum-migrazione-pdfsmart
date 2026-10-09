package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class MifidManlevaDataModel extends CommandDataModel {
	
	private StringType	idAdeguatezzaPadre = new StringType();
	private StringType	flagManlevaKOESG = new StringType();
	
	public StringType getIdAdeguatezzaPadre() {
		return idAdeguatezzaPadre;
	}
	public void setIdAdeguatezzaPadre(StringType idAdeguatezzaPadre) {
		this.idAdeguatezzaPadre = idAdeguatezzaPadre;
	}
	public StringType getFlagManlevaKOESG() {
		return flagManlevaKOESG;
	}
	public void setFlagManlevaKOESG(StringType flagManlevaKOESG) {
		this.flagManlevaKOESG = flagManlevaKOESG;
	}
    
}
