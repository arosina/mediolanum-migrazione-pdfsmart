package prgm.ita.p.dac.ricerche.fb;

import prgm.ita.p.dac.model.AbstractRicercaDacModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaStoricoDacModel extends AbstractRicercaDacModel {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public RicercaStoricoDacModel(){
		getParametri().addCodDescField("stato","StatiStoricoDac");
		getParametri().addCodDescField("esito","EsitiDac");
	}

}
