package prgm.ita.p.dac.facade;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface ControlliDac {
	public boolean controllaDac(ClientSessionContext csc, DacModel dac) throws Exception;
	public boolean controllaDocumento(ClientSessionContext csc, DocumentoModel doc) throws Exception;
	public boolean controllaPlicoInDac(ClientSessionContext csc, DacModel dac) throws Exception;
	public boolean controllaPlicoInPinzatura(ClientSessionContext csc, DacModel dac, DocumentoModel doc) throws Exception;
	public void initHtmlJavascriptCampiObbligatori(ClientSessionContext csc, DacModel dac) throws Exception;
}
