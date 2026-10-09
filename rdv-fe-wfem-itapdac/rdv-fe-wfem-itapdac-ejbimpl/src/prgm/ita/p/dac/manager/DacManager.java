package prgm.ita.p.dac.manager;

import javax.ejb.Remote;

import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;


/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface DacManager extends Manager {
	public DacModel 		salvaDac(ClientSessionContext csc, DacModel dac);
	public DacModel 		inviaDac(ClientSessionContext csc, DacModel dac);
	public DacModel 		lavoraDac(ClientSessionContext csc, DacModel dac);
	public DacModel 		spuntaDac(ClientSessionContext csc, DacModel dac);
	
	public DocumentoModel	creaDocumento(ClientSessionContext csc, StringType idDac, AgenteModel agenteRiferimento, DocumentoModel doc, int ubicazione);
	public DocumentoModel	salvaDocumento(ClientSessionContext csc, DocumentoModel doc);
	public DocumentoModel 	esitaFirmaCliente(ClientSessionContext csc, DocumentoModel doc);
	public DocumentoModel 	esitaFirmaAgente(ClientSessionContext csc, DocumentoModel doc);
	public void			 	cancellaDocumento(ClientSessionContext csc, StringType idDac, DocumentoModel doc);
	public DacModel 		pinzaDocumenti(ClientSessionContext csc, DacModel dac);
	public DacModel 		spinzaDocumenti(ClientSessionContext csc, DacModel dac);
	
	//Sede
	public DocumentoModel	inserisciDocInDac(ClientSessionContext csc, StringType idDac, StringType idDoc, boolean inRicezione, boolean erratoSmistamento);
	public void				rimuoviDocDaDac(ClientSessionContext csc, StringType idDac, StringType idDoc);
	public DocumentoModel	spinzaDocInSpedizione(ClientSessionContext csc, DocumentoModel doc);
	public DacModel			spedisciDac(ClientSessionContext csc, DacModel dac);
	public DacModel			gestisciDac(ClientSessionContext csc, DacModel dac);
	public DacModel			chiudiDac(ClientSessionContext csc, DacModel dac);		
	public DocumentoModel 	aggiornaDatiDocInRicezione(ClientSessionContext csc, DocumentoModel docDaAggiornare);

}
