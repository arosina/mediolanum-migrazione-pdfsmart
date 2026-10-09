package prgm.ita.p.dac.service;

import javax.ejb.Remote;

import prgm.ita.p.dac.model.DacModel;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface DacService extends Manager {
	public DacModel 	preparaInserimento(ClientSessionContext csc, String codAgente, Contratto contratto, Cliente cliente, MezzoPagamento[] mezziDiPagamento);
	public StringType  	inserisciDocumento(ClientSessionContext csc, DacModel dac);
}
