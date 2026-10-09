package prgm.ita.anagraficaclienti.popup.model;

import prgm.ita.anagraficaclienti.model.UniversitaModel;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupUniversitaModel extends PopupUniversitaParamsModel {

	private BooleanType isPrimaVolta = new BooleanType(true);
	private ListType elenco = new ListType(UniversitaModel.class);
	
	public ListType getElenco() {
		return elenco;
	}
	public void setElenco(ListType elenco) {
		this.elenco = elenco;
	}
	public BooleanType getIsPrimaVolta() {
		return isPrimaVolta;
	}
	public void setIsPrimaVolta(BooleanType isPrimaVolta) {
		this.isPrimaVolta = isPrimaVolta;
	}
}
