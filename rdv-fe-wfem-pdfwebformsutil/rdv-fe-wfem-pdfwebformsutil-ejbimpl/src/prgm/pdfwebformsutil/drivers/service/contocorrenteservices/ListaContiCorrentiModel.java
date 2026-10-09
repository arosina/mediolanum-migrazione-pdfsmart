package prgm.pdfwebformsutil.drivers.service.contocorrenteservices;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteModel;

public class ListaContiCorrentiModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3652002153176129264L;
	private transient ContoAutocompleteInput input = new ContoAutocompleteInput();
	private ListType items = new ListType(ContoAutocompleteModel.class);

	public ListType getItems() {
		return items;
	}

	public void setItems(ListType items) {
		this.items = items;
	}

	public ContoAutocompleteInput getInput() {
		return input;
	}

	public void setInput(ContoAutocompleteInput input) {
		this.input = input;
	}
}
