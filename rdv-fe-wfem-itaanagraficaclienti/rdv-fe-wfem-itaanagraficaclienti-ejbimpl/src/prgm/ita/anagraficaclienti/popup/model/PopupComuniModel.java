package prgm.ita.anagraficaclienti.popup.model;

import prgm.ita.anagraficaclienti.model.ComuneModel;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupComuniModel extends PopupComuniParamsModel {

	private BooleanType isPrimaVolta = new BooleanType(true);
	
	private ListType elenco = new ListType(ComuneModel.class);
	
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
