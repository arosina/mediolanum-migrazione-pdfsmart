package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.types.IntegerType;

import prgm.pdfwebforms.model.PdfInstanceAttachModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AllegatoMOMModel extends PdfInstanceAttachModel {

	private IntegerType	progressivoAllegatoSrvMom = new IntegerType();

	public IntegerType getProgressivoAllegatoSrvMom() {
		return progressivoAllegatoSrvMom;
	}

	public void setProgressivoAllegatoSrvMom(IntegerType progressivoAllegatoSrvMom) {
		this.progressivoAllegatoSrvMom = progressivoAllegatoSrvMom;
	}


}
