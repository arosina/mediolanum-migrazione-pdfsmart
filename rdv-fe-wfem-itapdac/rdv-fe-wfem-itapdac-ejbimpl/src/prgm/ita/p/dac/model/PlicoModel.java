package prgm.ita.p.dac.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class PlicoModel extends CommandDataModel {
	private StringType idPlico = new StringType();
	private ListType documenti = new ListType(DocumentoModel.class);

	public ListType getDocumenti() {
		return documenti;
	}

	public void setDocumenti(ListType documenti) {
		this.documenti = documenti;
	}

	public StringType getIdPlico() {
		return idPlico;
	}

	public void setIdPlico(StringType idPlico) {
		this.idPlico = idPlico;
	}
	
}
