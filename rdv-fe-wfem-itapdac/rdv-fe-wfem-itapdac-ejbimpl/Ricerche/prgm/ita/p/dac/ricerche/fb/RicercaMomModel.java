package prgm.ita.p.dac.ricerche.fb;

import prgm.ita.p.dac.model.AbstractRicercaDacModel;

import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaMomModel extends AbstractRicercaDacModel {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public RicercaMomModel(){
		setTipoRicerca(new StringType("MOM"));
		getParametri().addCodDescField("stato","StatiRicercaMom");
	}

}
