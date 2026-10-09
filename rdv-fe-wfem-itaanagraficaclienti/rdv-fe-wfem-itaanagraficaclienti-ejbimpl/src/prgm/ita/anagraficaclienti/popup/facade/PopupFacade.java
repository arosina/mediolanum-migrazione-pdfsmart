package prgm.ita.anagraficaclienti.popup.facade;

import javax.ejb.Remote;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.ita.anagraficaclienti.popup.model.PopupAgentiModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;
import prgm.ita.anagraficaclienti.popup.model.PopupComuniModel;
import prgm.ita.anagraficaclienti.popup.model.PopupIndirizziClienteModel;
import prgm.ita.anagraficaclienti.popup.model.PopupUniversitaModel;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface PopupFacade extends Facade {

	public PopupAgentiModel  			getElencoAgenti(ClientSessionContext csc, PopupAgentiModel popupAgentiModel);
	public PopupClientiModel 			getElencoClienti(ClientSessionContext csc, PopupClientiModel popupClientiModel);
	public PopupClientiModel 			getElencoClientiSecondari(ClientSessionContext csc, PopupClientiModel popupClientiModel);
	public PopupClientiModel 			getElencoClientiPotenziali(ClientSessionContext csc, PopupClientiModel popupClientiModel);
	public PopupClientiModel 			getElencoClientiAgente(ClientSessionContext csc, PopupClientiModel popupClientiModel);
	public PopupComuniModel 			getElencoComuni(ClientSessionContext csc, PopupComuniModel popupComuniModel);	
	public PopupUniversitaModel 		getElencoUniversita(ClientSessionContext csc, PopupUniversitaModel popupUniversitaModel);	
	public PopupClientiModel 			getElencoMandatiM4U(ClientSessionContext csc, PopupClientiModel popupClientiModel);
	public PopupIndirizziClienteModel 	getElencoIndirizziCliente(ClientSessionContext csc, PopupIndirizziClienteModel popupIndirizziClienteModel);
	public PopupClientiModel			getElencoClientiAgentePrestitiPreapprovati(ClientSessionContext csc, PopupClientiModel popupClientiModel);
}
