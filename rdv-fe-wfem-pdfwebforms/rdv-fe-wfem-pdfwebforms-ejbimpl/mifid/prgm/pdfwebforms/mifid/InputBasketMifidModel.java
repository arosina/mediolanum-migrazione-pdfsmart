package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class InputBasketMifidModel extends CommandDataModel{
	
    private StringType      flagAdeguatezza = new StringType("S");								// Impostare a "S" se si vuole calcolare l'adeguatezza. A "N" per l'appropriatezza
    private ListType    	acquisti = new ListType(OperazioneBasketMifidModel.class);			// Elenco delle operazioni di acquisto
    private ListType	    disinvestimenti = new ListType(OperazioneBasketMifidModel.class);	// Elenco delle operazioni di disinvestimento contestuali agli acquisti

    public StringType getFlagAdeguatezza() {
		return flagAdeguatezza;
	}
	public void setFlagAdeguatezza(StringType flagAdeguatezza) {
		this.flagAdeguatezza = flagAdeguatezza;
	}
	public ListType getAcquisti() {
		return acquisti;
	}
	public void setAcquisti(ListType acquisti) {
		this.acquisti = acquisti;
	}
	public ListType getDisinvestimenti() {
		return disinvestimenti;
	}
	public void setDisinvestimenti(ListType disinvestimenti) {
		this.disinvestimenti = disinvestimenti;
	}

}
