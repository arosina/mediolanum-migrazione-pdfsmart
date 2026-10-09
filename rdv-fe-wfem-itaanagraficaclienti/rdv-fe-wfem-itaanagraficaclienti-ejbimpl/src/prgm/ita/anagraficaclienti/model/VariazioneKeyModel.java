package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.*;

/***********************************************************************************************/
/***********************************************************************************************/
public class VariazioneKeyModel extends ClienteKeyModel {
	
	private IntegerType progressivo = new IntegerType();
	
	public IntegerType getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}

}
