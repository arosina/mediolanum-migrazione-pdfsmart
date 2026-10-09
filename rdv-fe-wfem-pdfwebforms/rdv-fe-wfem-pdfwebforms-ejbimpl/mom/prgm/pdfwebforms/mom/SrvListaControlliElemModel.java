package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SrvListaControlliElemModel extends CommandDataModel {

	private StringType tipoControllo = new StringType();
	private StringType idControllo = new StringType();
	
	public StringType getTipoControllo() {
		return tipoControllo;
	}
	public void setTipoControllo(StringType tipoControllo) {
		this.tipoControllo = tipoControllo;
	}
	public StringType getIdControllo() {
		return idControllo;
	}
	public void setIdControllo(StringType idControllo) {
		this.idControllo = idControllo;
	}
	
}
