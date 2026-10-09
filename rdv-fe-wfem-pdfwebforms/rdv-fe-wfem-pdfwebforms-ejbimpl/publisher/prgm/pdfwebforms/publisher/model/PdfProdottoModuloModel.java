package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class PdfProdottoModuloModel extends CommandDataModel {
	
	private StringType 	descrProdotto = new StringType();

	public StringType getDescrProdotto() {
		return descrProdotto;
	}

	public void setDescrProdotto(StringType descrProdotto) {
		this.descrProdotto = descrProdotto;
	}
}
