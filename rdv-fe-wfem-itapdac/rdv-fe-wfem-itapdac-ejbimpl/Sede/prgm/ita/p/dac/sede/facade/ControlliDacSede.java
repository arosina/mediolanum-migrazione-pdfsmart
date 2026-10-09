package prgm.ita.p.dac.sede.facade;

import prgm.ita.p.dac.model.DacModel;

import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface ControlliDacSede {
	public boolean controllaDacInSpedizione(ClientSessionContext csc, DacModel dac) throws Exception;
	public boolean controllaDacInChiusura(ClientSessionContext csc, DacModel dac) throws Exception;
}
