package prgm.ita.p.dac.popup.model;


import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupAgentiModel extends ParamsModel {

	private boolean primaVolta = true;	

	private StringType  codAgente = new StringType();
	private StringType  cognome = new StringType();
	
	private ListType elencoAgenti = new ListType(PopupAgenteModel.class);

	public boolean isPrimaVolta() {
		return primaVolta;
	}

	public void setPrimaVolta(boolean primaVolta) {
		this.primaVolta = primaVolta;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getCognome() {
		return cognome;
	}

	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}

	public ListType getElencoAgenti() {
		return elencoAgenti;
	}

	public void setElencoAgenti(ListType elencoAgenti) {
		this.elencoAgenti = elencoAgenti;
	}
	
}
