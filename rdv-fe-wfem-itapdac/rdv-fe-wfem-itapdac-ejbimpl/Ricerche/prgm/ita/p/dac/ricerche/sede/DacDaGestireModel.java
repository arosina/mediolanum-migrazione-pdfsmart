package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.model.AbstractRicercaDacModel;

public class DacDaGestireModel extends AbstractRicercaDacModel {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacDaGestireModel(){
		getParametri().addCodDescField("stato","StatiDacSedeInRicezione");
		addCodDescField("ufficio", "Uffici");
	}
}
