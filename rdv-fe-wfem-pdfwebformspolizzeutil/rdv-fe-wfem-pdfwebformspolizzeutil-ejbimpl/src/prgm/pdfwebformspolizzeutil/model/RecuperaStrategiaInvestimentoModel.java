package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;

public class RecuperaStrategiaInvestimentoModel extends CommandDataModel{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ListType infoCompletamentoInput = new ListType(StrategiaInvestimentoInputModel.class);
	private ListType infoCompletamentoResponse = new ListType(StrategiaInvestimentoOutputModel.class);
	
	public ListType getInfoCompletamentoInput() {
		return infoCompletamentoInput;
	}
	public ListType getInfoCompletamentoResponse() {
		return infoCompletamentoResponse;
	}
	public void setInfoCompletamentoInput(ListType infoCompletamentoInput) {
		this.infoCompletamentoInput = infoCompletamentoInput;
	}
	public void setInfoCompletamentoResponse(ListType infoCompletamentoResponse) {
		this.infoCompletamentoResponse = infoCompletamentoResponse;
	}
	
	
}
