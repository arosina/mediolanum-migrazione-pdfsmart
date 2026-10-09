package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;

public class ComuneModel  extends CommandDataModel {
	
	private DoubleType codiceComune = new DoubleType();

	public DoubleType getCodiceComune() {
		return codiceComune;
	}

	public void setCodiceComune(DoubleType codiceComune) {
		this.codiceComune = codiceComune;
	}




}
