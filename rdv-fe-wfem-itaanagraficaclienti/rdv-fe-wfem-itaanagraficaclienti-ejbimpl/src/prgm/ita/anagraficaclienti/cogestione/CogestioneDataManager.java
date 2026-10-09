package prgm.ita.anagraficaclienti.cogestione;

import javax.ejb.EJBException;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class CogestioneDataManager {

	private static final String DAO_XML_COGESTIONE = "ItaAnagraficaClienti.AnagraficaCogestione";
	private static final String S_LEGAMECOGESTIONEPROSPECT = "legameCogestioneProspect";
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private CogestioneDataManager() {}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void initCodAgenteTitolare(ClientSessionContext csc, ClienteKeyModel clienteKey) throws DAOException{
		if(!clienteKey.getCodAgeImpersonato().isNull() && !clienteKey.getCodMediolanum().isNull()) { // Solo per le variazioni accetto l'impersonato perchè non devo andare sul contratto di cogestione BC
			clienteKey.setCodAgente(new StringType(Tools.fillSx(clienteKey.getCodAgeImpersonato().toString(),'0',10)));
			return;
		}
		DAOObject dao = new DAOObject(csc,DAO_XML_COGESTIONE);
		DAOQueryResultModel qRes = dao.executeQueryAccess("loadCodAgenteTitolareCogestioneCliente", clienteKey);
		if(qRes.getResult().size() == 0 && clienteKey.getCodAgente().isNull())
			clienteKey.setCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void initContrattoCogestione(ClientSessionContext csc, CogestioneDataModel cogestioneData) throws DAOException{
		
		DAOObject dao = new DAOObject(csc,DAO_XML_COGESTIONE);
		DAOQueryResultModel qRes = dao.executeQueryAccess("loadAccordoCogestione", cogestioneData);
		if(qRes.getResult().size() > 0) {
			if(Tools.fillSx(csc.getCurrentLinkedUserCode(), '0', 10).equals(cogestioneData.getCodAgenteCogestore().toString()))
				cogestioneData.setIsUtenteCogestore(new BooleanType(true));
			else if(Tools.fillSx(csc.getCurrentLinkedUserCode(), '0', 10).equals(cogestioneData.getCodAgenteTitolare().toString()))
				cogestioneData.setIsUtenteTitolare(new BooleanType(true));
		}
	
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void initCogestioneData(ClientSessionContext csc, ClienteModel cliente) throws DAOException{
		
		initContrattoCogestione(csc, cliente.getCogestioneData());
	
		if(cliente.getIsEffettivo().booleanValue())
			return;
		
		CogestioneDataModel cogestioneData = cliente.getCogestioneData();
		if(cogestioneData.getIsUtenteCogestore().booleanValue() && 
		   cogestioneData.isContrattoCogestioneAttivo() && 
		   cliente.getCodPotenziale().isNull()) {
			cliente.setIsClienteInCogestione(new BooleanType(true));
		}else {
			cliente.setIsClienteInCogestione(new BooleanType(esisteCogestione(csc, cliente)));
		}
		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static boolean esisteCogestione(ClientSessionContext csc, ClienteModel cliente) throws DAOException{		
		DAOObject dao = new DAOObject(csc,DAO_XML_COGESTIONE);
		DAOTableResultModel tRes = dao.executeTableLoadAccess(S_LEGAMECOGESTIONEPROSPECT, cliente);
		return tRes.getResult().intValue() > 0 ? true : false;
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static boolean checkConcurrency(ClientSessionContext csc, ClienteModel cliente)  throws EJBException{
		cliente.setConcurrencyViolationFoundedWidth(null);
		if(cliente.getIsPotenziale().booleanValue() && !cliente.getCodPotenziale().isNull() && !cliente.getDataOraUltimaModifica().isNull()){
			try {
				ClienteModel loadLastAccessData = new ClienteModel();
				loadLastAccessData.setCodPotenziale(cliente.getCodPotenziale());
				ListType qRes = new DAOObject(csc,DAO_XML_COGESTIONE).executeQueryAccess("loadLastAccessData", loadLastAccessData).getResult();
				if(qRes.size() == 0) {
					cliente.setConcurrencyViolationFoundedWidth("notExist");
					cliente.getDatiApplicativi().setErroreCentrale("Rilevata concorrenza: la bozza è stata cancellata, non è possibile proseguire.");
					cliente.setModality(Template.READ_MODALITY);
					return true;
				}else if(!loadLastAccessData.getDataOraUltimaModificaAsString().isNull() &&
						 !cliente.getDataOraUltimaModifica().toString().equals(loadLastAccessData.getDataOraUltimaModificaAsString().toString())) {
					cliente.setConcurrencyViolationFoundedWidth(loadLastAccessData.getCodAgenteUltimaModifica().toString());
					cliente.getDatiApplicativi().setErroreCentrale("Rilevata concorrenza: la bozza è stata aperta e lavorata da "+loadLastAccessData.getCodAgenteUltimaModifica()+": non è possibile proseguire, è necessario uscire e riavviare la compilazione della bozza");
					cliente.setModality(Template.READ_MODALITY);
					return true;
				}
			}catch(DAOException daoe) {
				throw new EJBException(daoe.toString());
			}
		}
		return false;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void impostaCogestioneInScrittura(ClientSessionContext csc, ClienteModel cliente) throws EJBException{
		
		// Verifica della concorrenza
		if(checkConcurrency(csc, cliente))
			return;

		// Ultima modifica
		cliente.setCodAgenteUltimaModifica(new StringType(cliente.getAgente().getCodAgente().toString()));
		if(cliente.getCodAgenteUltimaModifica().isNull())
			cliente.setCodAgenteUltimaModifica(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
		cliente.setDataOraUltimaModifica(Tools.now());

		// Se è un effettivo siamo in variazione e la cogestione non va gestita 
		if(cliente.getIsEffettivo().booleanValue())
			return;
		
		// Se censimento da HUB o MHD viene assegnato all'agente impostato in "codAgente" che sono passati in input
		if(!CogestioneDataModel.isCensimentoHub(cliente) && !CogestioneDataModel.isCensimentoMhd(cliente)) {
			// Cogestione: switch tra un Fb e l'altro
			cliente.setCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
			if(cliente.getIsClienteInCogestione().booleanValue())
				cliente.setCodAgente(new StringType(cliente.getCogestioneData().getCodAgenteTitolare().toString()));
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void scriviCogestione(ClientSessionContext csc, ClienteModel cliente) throws DAOException{
		
		// Se è un effettivo siamo in variazione e la cogestione non va gestita
		if(!cliente.getIsPotenziale().booleanValue())
			return;
		
		DAOObject dao = new DAOObject(csc,DAO_XML_COGESTIONE);
		try {
			dao.executeTableDeleteAccess(S_LEGAMECOGESTIONEPROSPECT, cliente);
		}catch(NoRowsAffected nra) { 
			// Do nothing 
		}
		if(cliente.getIsClienteInCogestione().booleanValue())
			dao.executeTableInsertAccess(S_LEGAMECOGESTIONEPROSPECT, cliente);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void cancellaCogestione(ClientSessionContext csc, ClienteModel cliente) throws DAOException{		

		// Se è un effettivo siamo in variazione e la cogestione non va cancellata
		if(!cliente.getIsPotenziale().booleanValue())
			return;
		
		try {
			new DAOObject(csc,DAO_XML_COGESTIONE).executeTableDeleteAccess("cancellaLegamiCogestioneProspect", cliente);
		}catch(NoRowsAffected nra) {
			// Do nothing
		}
	}

}
