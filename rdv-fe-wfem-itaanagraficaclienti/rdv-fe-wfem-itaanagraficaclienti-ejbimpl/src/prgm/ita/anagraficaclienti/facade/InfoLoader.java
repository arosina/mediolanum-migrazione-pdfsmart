package prgm.ita.anagraficaclienti.facade;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DominioModel;
import prgm.ita.anagraficaclienti.model.InfoPritModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class InfoLoader {
	
    private static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	
    
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static InfoPritModel getInfoPrit(ClientSessionContext csc, StringType codiceProdottoDispo , StringType chiave) throws Exception{														    	
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();

			InfoPritModel infoPrit = new InfoPritModel();			
			infoPrit.setCodiceProdottoDispo(codiceProdottoDispo);
			infoPrit.setChiave(chiave);
			dao.executeQueryAccess("loadInfoPrit",infoPrit);
			return infoPrit;
			
		}catch(DAOException daoe){
			String errorMsg = "InfoLoader - Eccezione DAO nel leggere le informazioni relative al prodotto ["+codiceProdottoDispo+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(Exception e){
			String errorMsg = "InfoLoader - Eccezione  nel leggere le informazioni relative al prodotto ["+codiceProdottoDispo+"]: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}			
		
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static CodDescDataList leggiCodDescListAteco(ClientSessionContext csc, ClienteModel cliente, boolean perDitta) {

		CodDescDataList dataList = new CodDescDataList();
		try{
			
			DAOQueryResultModel qRes = null;			
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			if (perDitta) {
				qRes = dao.executeQueryAccess("loadCodiciAtecoDitta",cliente);
			} else {
				qRes = dao.executeQueryAccess("loadCodiceAtecoPersonaFisica",cliente);
				if(qRes.getResult().size() == 0){ // Se la tabella non è valorizzata correttamente per le persone fisiche creo l'elemento tendina a mano
					DominioModel dominio = new DominioModel();
					dominio.setCod(new StringType(Costanti.CODICE_ATECO_NESSUN_CODICE));
					dominio.setDescr(new StringType("NESSUN CODICE"));
					qRes.getResult().add(dominio);
				}
			}
			
			ListType elenco = qRes.getResult();
			for (int i = 0; i < elenco.size(); i++) {
				DominioModel dominio = (DominioModel) elenco.get(i);
				CodDescData data = new CodDescData();
				data.setCod(dominio.getCod().toString());
				data.setDescr(dominio.getDescr().toString());
				dataList.addCodDescData(data);
			}
			
		}catch(Exception e){
			LOG.error(e);
		}catch(DAOException daoe){
			LOG.error(daoe);
		}
		
		if(!perDitta){
			cliente.getInfoPersonali().setCodAteco(new StringType(Costanti.CODICE_ATECO_NESSUN_CODICE));
			if(cliente.getDatiApplicativi().getClienteOriginale() != null)
				cliente.getDatiApplicativi().getClienteOriginale().getInfoPersonali().setCodAteco(new StringType(Costanti.CODICE_ATECO_NESSUN_CODICE));
			if(cliente.getDatiApplicativi().getClienteCaricato() != null)
				cliente.getDatiApplicativi().getClienteCaricato().getInfoPersonali().setCodAteco(new StringType(Costanti.CODICE_ATECO_NESSUN_CODICE));
		}
		
		// Per i datori di lavoro è comunque e sempre null
		if(cliente.getIsDatoreDiLavoro().booleanValue()) {
			cliente.getInfoPersonali().setCodAteco(new StringType());
			if(cliente.getDatiApplicativi().getClienteOriginale() != null)
				cliente.getDatiApplicativi().getClienteOriginale().getInfoPersonali().setCodAteco(new StringType());
			if(cliente.getDatiApplicativi().getClienteCaricato() != null)
				cliente.getDatiApplicativi().getClienteCaricato().getInfoPersonali().setCodAteco(new StringType());
		}

		return dataList;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static CodDescDataList leggiCodDescListSae(ClientSessionContext csc, ClienteModel cliente, boolean perDitta) {

		CodDescDataList dataList = new CodDescDataList();
		try{	
			DAOQueryResultModel qRes = null;			
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			if (perDitta) {
				qRes = dao.executeQueryAccess("loadCodiciSaeDitta",cliente);
			} else {
				qRes = dao.executeQueryAccess("loadCodiceSaePersonaFisica",cliente);				
				if(qRes.getResult().size() == 0){ // Se la tabella non è valorizzata correttamente per le persone fisiche creo l'elemento tendina a mano
					DominioModel dominio = new DominioModel();
					dominio.setCod(new StringType(Costanti.CODICE_SOTTOGRUPPO_ATTIVITA_CONSUMATORI));
					dominio.setDescr(new StringType(Costanti.CODICE_SOTTOGRUPPO_ATTIVITA_CONSUMATORI+" - FAMIGLIE CONSUMATRICI"));
					qRes.getResult().add(dominio);
				}
			}

			ListType elenco = qRes.getResult();
			for (int i = 0; i < elenco.size(); i++) {
				DominioModel dominio = (DominioModel) elenco.get(i);
				CodDescData data = new CodDescData();
				data.setCod(dominio.getCod().toString());
				data.setDescr(dominio.getDescr().toString());
				dataList.addCodDescData(data);
			}			
		}catch(Exception e){
			LOG.error(e);
		}catch(DAOException daoe){
			LOG.error(daoe);
		}
		
		if(!perDitta) {
			cliente.getInfoPersonali().setCodSottogruppoAttivita(new StringType(Costanti.CODICE_SOTTOGRUPPO_ATTIVITA_CONSUMATORI));
			if(cliente.getDatiApplicativi().getClienteOriginale() != null)
				cliente.getDatiApplicativi().getClienteOriginale().getInfoPersonali().setCodSottogruppoAttivita(new StringType(Costanti.CODICE_SOTTOGRUPPO_ATTIVITA_CONSUMATORI));
			if(cliente.getDatiApplicativi().getClienteCaricato() != null)
				cliente.getDatiApplicativi().getClienteCaricato().getInfoPersonali().setCodSottogruppoAttivita(new StringType(Costanti.CODICE_SOTTOGRUPPO_ATTIVITA_CONSUMATORI));
		}
		
		return dataList;		
	}	
		
	/*****************************************************************************************************/
	/*****************************************************************************************************/	
	public static CodDescDataList leggiCodDescListSettoriEconomici(ClientSessionContext csc, ClienteModel cliente, boolean onlyValid) {

		CodDescDataList dataList = new CodDescDataList();
		try{			
			DAOQueryResultModel qRes = null;			
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			if(cliente.getIsDitta().booleanValue()) {
				qRes = dao.executeQueryAccess("loadSettoriEconomiciDitta",cliente);
			}else {
				if(cliente.getInfoPersonali().getCodProfessione().isNull())
					return dataList;
				qRes = dao.executeQueryAccess("loadSettoriEconomiciPersonaFisica",cliente);
			}
			ListType elenco = qRes.getResult();
			for (int i = 0; i < elenco.size(); i++) {
				DominioModel dominio = (DominioModel) elenco.get(i);
				CodDescData data = new CodDescData();
				data.setCod(dominio.getCod().toString());
				data.setDescr(dominio.getDescr().toString());
				if (!dominio.getValidita().equals("S"))
					data.setValid(false);
				if (!onlyValid) {
					dataList.addCodDescData(data);
				} else {
					if (data.isValid())
						dataList.addCodDescData(data);
				}
			}				
		}catch(Exception e){
			LOG.error(e);
		}catch(DAOException daoe){
			LOG.error(daoe);
		}	
		return dataList;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String getNomeDAO(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.AnagraficaClienti";
	  	return xmlName;
	}
}
