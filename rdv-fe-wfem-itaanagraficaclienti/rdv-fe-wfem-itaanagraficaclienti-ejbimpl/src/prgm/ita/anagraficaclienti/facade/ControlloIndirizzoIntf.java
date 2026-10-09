package prgm.ita.anagraficaclienti.facade;

import prgm.ita.anagraficaclienti.model.IndirizzoModel;

import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface ControlloIndirizzoIntf {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eseguiControllo(ClientSessionContext csc, IndirizzoModel indirizzo,	boolean indirizzoObbligatorio) throws Exception;
}
