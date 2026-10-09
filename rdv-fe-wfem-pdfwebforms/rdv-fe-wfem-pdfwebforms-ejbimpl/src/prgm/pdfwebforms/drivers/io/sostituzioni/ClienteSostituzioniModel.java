package prgm.pdfwebforms.drivers.io.sostituzioni;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ClienteSostituzioniModel extends CommandDataModel{
	
	private StringType codTipoSogg = new StringType();
	private StringType codSoggOrig = new StringType();
	private ListType   operazioni = new ListType(OperazioneSostituzioniModel.class);
	
	public StringType getCodTipoSogg() {
		return codTipoSogg;
	}
	public void setCodTipoSogg(StringType codTipoSogg) {
		this.codTipoSogg = codTipoSogg;
	}
	public StringType getCodSoggOrig() {
		return codSoggOrig;
	}
	public void setCodSoggOrig(StringType codSoggOrig) {
		this.codSoggOrig = codSoggOrig;
	}
	public ListType getOperazioni() {
		return operazioni;
	}
	public void setOperazioni(ListType operazioni) {
		this.operazioni = operazioni;
	}
}
