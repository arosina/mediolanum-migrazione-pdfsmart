package com.atosorigin.wfem.bo;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class BoAgeParamVerifierModel extends CommandDataModel {

	private StringType  codAgente 		= new StringType();
	private StringType  cognomeAgente 	= new StringType();
	
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCognomeAgente() {
		return cognomeAgente;
	}
	public void setCognomeAgente(StringType cognomeAgente) {
		this.cognomeAgente = cognomeAgente;
	}
	
}
