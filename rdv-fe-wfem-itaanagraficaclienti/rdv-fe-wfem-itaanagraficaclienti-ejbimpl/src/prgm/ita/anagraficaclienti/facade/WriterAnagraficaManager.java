package prgm.ita.anagraficaclienti.facade;

import javax.ejb.Remote;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface WriterAnagraficaManager extends Manager {
	public ClienteModel 	  salva(ClientSessionContext csc, ClienteModel model);
	public ClienteModel 	  salvaFotografia(ClientSessionContext csc, ClienteModel model);
	public ClienteModel 	  conferma(ClientSessionContext csc, ClienteModel model);
	public ClienteModel 	  inviaInSede(ClientSessionContext csc, ClienteModel model);
	public VariazioneKeyModel cancella(ClientSessionContext csc, VariazioneKeyModel model, String nomeTabella);
	
	public void			 	  salvaOracle(ClientSessionContext csc, ClienteModel model);
	public void			 	  inviaInSedeOracle(ClientSessionContext csc, ClienteModel model);
	public void				  cancellaOracle(ClientSessionContext csc, ClienteModel model);
}
