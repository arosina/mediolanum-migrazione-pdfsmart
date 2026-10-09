package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class MomEventParamModel extends CommandDataModel {

	private StringType	nomeValore = new StringType();

	public StringType getNomeValore() {
		return nomeValore;
	}

	public void setNomeValore(StringType nomeValore) {
		this.nomeValore = nomeValore;
	}

}
