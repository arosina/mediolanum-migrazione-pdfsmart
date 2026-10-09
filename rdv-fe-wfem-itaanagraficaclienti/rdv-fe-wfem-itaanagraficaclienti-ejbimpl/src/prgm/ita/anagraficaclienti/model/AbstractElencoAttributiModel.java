package prgm.ita.anagraficaclienti.model;

import java.util.Vector;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class AbstractElencoAttributiModel extends AbstractSectionModel {

	private ListType elencoAttributi;
	private IntegerType idx = new IntegerType(-1);
	private Vector elencoAttributiCancellati = new Vector();

	public IntegerType getIdx() {
		return idx;
	}

	public void setIdx(IntegerType idx) {
		this.idx = idx;
	}

	public ListType getElencoAttributi() {
		return elencoAttributi;
	}

	public void setElencoAttributi(ListType elencoAttributi) {
		this.elencoAttributi = elencoAttributi;
	}

	public Vector getElencoAttributiCancellati() {
		return elencoAttributiCancellati;
	}

	public void setElencoAttributiCancellati(Vector elencoAttributiCancellati) {
		this.elencoAttributiCancellati = elencoAttributiCancellati;
	}

}
