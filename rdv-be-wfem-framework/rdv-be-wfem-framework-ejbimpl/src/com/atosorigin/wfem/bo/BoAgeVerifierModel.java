package com.atosorigin.wfem.bo;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class BoAgeVerifierModel extends CommandDataModel {

	private StringType 			  codAgenteCollegato = new StringType();
	private BoAgeParamVerifierModel params = new BoAgeParamVerifierModel();
	
	public StringType getCodAgenteCollegato() {
		return codAgenteCollegato;
	}
	public void setCodAgenteCollegato(StringType codAgenteCollegato) {
		this.codAgenteCollegato = codAgenteCollegato;
	}
	public BoAgeParamVerifierModel getParams() {
		return params;
	}
	public void setParams(BoAgeParamVerifierModel params) {
		this.params = params;
	}
	

}
