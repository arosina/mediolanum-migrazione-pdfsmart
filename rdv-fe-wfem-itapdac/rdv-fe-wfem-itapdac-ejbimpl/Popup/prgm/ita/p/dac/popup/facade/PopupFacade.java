package prgm.ita.p.dac.popup.facade;

import java.rmi.RemoteException;

import prgm.ita.p.dac.popup.model.PopupAgentiModel;
import prgm.ita.p.dac.popup.model.PopupClientiModel;
import prgm.ita.p.dac.popup.model.PopupContrattiModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PopupFacade extends Facade {
	public PopupContrattiModel 	cercaContratti(ClientSessionContext csc, PopupContrattiModel popupContrattiModel) throws RemoteException;
	public PopupClientiModel 	cercaClienti(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws RemoteException;
	public PopupClientiModel 	cercaContrattiCliente(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws RemoteException;
	public PopupAgentiModel 	cercaAgenti(ClientSessionContext csc, PopupAgentiModel popupAgentiModel) throws RemoteException;
}
