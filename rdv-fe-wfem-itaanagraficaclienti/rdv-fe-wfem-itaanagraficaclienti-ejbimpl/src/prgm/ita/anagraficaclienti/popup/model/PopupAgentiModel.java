package prgm.ita.anagraficaclienti.popup.model;

import prgm.ita.anagraficaclienti.model.AgenteModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupAgentiModel extends CommandDataModel {

	private BooleanType isPrimaVolta = new BooleanType(true);	

	private PopupAgentiParamsModel params = new PopupAgentiParamsModel();
	private ListType elenco = new ListType(AgenteModel.class);
	
	public PopupAgentiParamsModel getParams() {
		return params;
	}
	public void setParams(PopupAgentiParamsModel params) {
		this.params = params;
	}
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
