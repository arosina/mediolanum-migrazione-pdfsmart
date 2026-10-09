package prgm.ita.p.dac.model;

import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractRicercaDacModel extends ParamsModel {
	
	private StringType userMOM = new StringType();
	private StringType tipoRicerca = new StringType();
	private RicercaDacParamsModel parametri = new RicercaDacParamsModel();
	private ListType elencoDac = new ListType(DacModel.class);

	public ListType getElencoDac() {
		return elencoDac;
	}

	public void setElencoDac(ListType elencoDac) {
		this.elencoDac = elencoDac;
	}

	public RicercaDacParamsModel getParametri() {
		return parametri;
	}

	public void setParametri(RicercaDacParamsModel parametri) {
		this.parametri = parametri;
	}

	public StringType getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public StringType getUserMOM() {
		return userMOM;
	}

	public void setUserMOM(StringType userMOM) {
		this.userMOM = userMOM;
	}

}
