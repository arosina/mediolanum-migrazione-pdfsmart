package prgm.ita.anagraficaclienti.facade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ReminderAddendumAVR{

	public static String REMINDER_MESSAGE = 
	"Soggetto da sottoporre alla procedura di <b>ADEGUATA VERIFICA RAFFORZATA</b> e alla compilazione dell'apposita modulistica disponibile all'interno del Catalogo Operazioni.<br>"+ 
	"Si rammenta che è richiesta in particolare la compilazione del modulo \"ADDENDUM alla scheda anagrafica e modulo per l'adeguata verifica della clientela\" a firma del cliente unitamente al modulo \"SCHEDA DI VALUTAZIONE ANTIRICICLAGGIO CLIENTE CON PROFILO ALTO\".<br>"+
	"In caso di assenza o incompleta compilazione della predetta modulistica non si potrà dare seguito all'operazione.";
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private ReminderAddendumAVR() {
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static void initReminder(ClientSessionContext csc, ClienteModel cliente) throws AnagraficaClientiException{
		cliente.getDatiApplicativi().setShowAlertAddendumAVR(false);		
		MapCommandDataModel srvModel = new MapCommandDataModel();		
		srvModel.addProperty("codiceResidenzaFiscale1", cliente.getResidenza().getIndirizzo().getCodNazione()); 
		srvModel.addProperty("codiceResidenzaFiscale2", cliente.getResidenza().getCodNazioneResidenzaFiscale2()); 
		srvModel.addProperty("codiceResidenzaFiscale3", cliente.getResidenza().getCodNazioneResidenzaFiscale3()); 
		srvModel.addProperty("flagPep", new StringType(cliente.getResidenza().getFlagPep().isNull()||cliente.getResidenza().getFlagPep().equals("N")?"false":"true")); 
		srvModel.addProperty("flagRelazioneAffari", new StringType(cliente.getAdempimentiNormativi().getHaLegamiAffariDiversiDaAttivitaPrincipale().equals("S")?"true":"false")); 

		srvModel.addProperty("flagAddendum", new StringType()); 
		
		try {
			DAOOSBResultModel osbRes = new DAOObject(csc,"ItaAnagraficaClienti.AnagraficaClienti").executeOSBAccess("verificaAddendumAMLCliente",srvModel);
			if(osbRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){
				String errmsg = "Errore ritornato dal servizio verificaAddendumAMLCliente:<br>"+osbRes.getWsCallData().getMessage();
				cliente.getCognome().addTypeError(errmsg);
				cliente.addCommandError(errmsg);
				return;
			}
			cliente.getDatiApplicativi().setShowAlertAddendumAVR(Boolean.parseBoolean(srvModel.readProperty("flagAddendum").toString()));
		}catch(DAOException daoe) {
			throw new AnagraficaClientiException("DAO Exceptio in verificaAddendumAMLCliente: "+daoe.toString());
		}
	}
}
