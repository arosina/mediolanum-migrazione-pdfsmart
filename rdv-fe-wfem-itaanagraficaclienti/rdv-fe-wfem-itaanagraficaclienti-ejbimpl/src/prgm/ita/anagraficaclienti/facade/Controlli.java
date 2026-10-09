package prgm.ita.anagraficaclienti.facade;

import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface Controlli {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eseguiControllo(ClientSessionContext csc, ClienteModel model) throws Exception;
}
