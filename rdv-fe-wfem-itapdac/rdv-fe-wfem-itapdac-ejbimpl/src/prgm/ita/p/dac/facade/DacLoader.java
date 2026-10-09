package prgm.ita.p.dac.facade;

import javax.ejb.EJBException;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.model.PlicoModel;
import prgm.ita.p.dac.util.DacTools;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacLoader {
	
	private static final String DAO_DAC_XML_NAME = "ItaPDac.Dac";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static DAOObject loadDacEverywhere(ClientSessionContext csc,
										   	  DacKeyModel dacKey, DacModel dac, boolean lightRead) throws Exception {
		
		DacTools.loadUfficioUtente(csc,dacKey);
		dac.copyParams(dacKey);
		dac.setApriPritAttivo(dacKey.getApriPritAttivo());
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,DAO_DAC_XML_NAME);

			if(!dac.getApriPritAttivo().booleanValue()){
				if(csc.isAssistenteFB())
					dac.setUserMOM(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
				else
					dac.setUserMOM(new StringType(Tools.fillSx(csc.getUserCode(),'0',10)));
				DAOOSBResultModel osbRes = new DAOObject(csc,Costanti.DAO_XML_NAME_MOM).executeOSBAccess("loadPritMOM",dac);
				if(osbRes.getWsCallData().getStatus() == OSBCallData.STATUS_OK){
					if(!dac.getDataOraEmissione().isNull()){
						dac.copyParams(dacKey);
						DacTools.inizializzaPritMOM(csc, dac);				
						return dao;
					}
				}
			}

			dacKey.removeParam(ParamsModel.storicizzato);
			dao.openConnection();
			DAOTableResultModel tRes = dao.executeTableLoadAccess("dac",dac);
			if(tRes.getResult().intValue() == 0){
				if(Configuration.getInstance().isOfflineEnvironment())
					throw new EJBException("Prit/DAC con ID=["+dacKey.getIdDac()+"] non trovata");
				dao.closeConnection();
				dao.openConnection("IQ_PRIT");
				tRes = dao.executeTableLoadAccess("dac",dac);
				if(tRes.getResult().intValue() == 0)
					throw new EJBException("Prit/DAC con ID=["+dacKey.getIdDac()+"] non trovata");
				dac.addParam(ParamsModel.storicizzato);
			}
	
			if(lightRead)
				innerLoadDac(csc,dao,dacKey,dac,true,false,false);
			else
				innerLoadDac(csc,dao,dacKey,dac,true,true,true);
			return dao;
			
		}catch(DAOException daoe){
			if(dao != null) dao.closeConnection();
			throw new Exception(daoe.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void reloadDac(ClientSessionContext csc, DAOObject dao, 
						   	     ParamsModel params, DacModel dac) throws Exception {
		innerLoadDac(csc,dao,params,dac,false,false,true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void innerLoadDac(ClientSessionContext csc, DAOObject dao, 
						   	   		 ParamsModel params, DacModel dac, 
						   	   		 boolean testataGiaCaricata, boolean loadDatiAgente, boolean loadDocumenti) throws Exception {
		
		if(!testataGiaCaricata){
			DacTools.loadUfficioUtente(csc,params);
			dac.copyParams(params);
		}
		
		try{
			
			if(dao == null)
				dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			
			if(!testataGiaCaricata){
				DAOTableResultModel tRes = dao.executeTableLoadAccess("dac",dac);
				if(tRes.getResult().intValue() == 0)
					throw new EJBException("Prit/DAC con ID=["+dac.getIdDac()+"] non trovata");
			}
			
			if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE))
				dac.addCodDescField("box", "Box");
			
			dao.executeTableLoadAccess("loadDacRete",dac);
		
			if(Configuration.getInstance().isOnlineEnvironment() && !dac.isStoricizzato()) {
				dao.executeTableLoadAccess("dacSede",dac);
				
				if(dac.isDopoSpunta() || dac.getFnc().equals(Costanti.FNC_AUTORIZZA))
					loadErroriDocumentiDac(csc, dao, dac);

				if(dac.isDopoSpunta()){
					if(dac.isCtrlCassette() && dac.isSmistatore()){
						IntegerType codCassetta = (IntegerType)dao.executeQueryAccess("recuperaCassettaBox",dac).getSingleResult();
						if (codCassetta == null)
							dac.setCassettaBox(new IntegerType());
						else
							dac.setCassettaBox(codCassetta);
					}
				}
			}
			
			// Imposto "autoSpuntata" anche come parametro se è vero il flag su DB
			// solo se il Prit è ancora in bozza
			if(dac.getIsAutoSpuntata().booleanValue() && dac.getStato().equals(Costanti.STATO_INCORSO))
				dac.addParam(ParamsModel.autoSpuntata);

			if(loadDocumenti)
				loadDocumentiDac(csc,dao,dac);
			
			if(loadDatiAgente){
				dao.executeQueryAccess("loadAgente",dac.getAgenteRiferimento());
				if(DacTools.alwaysCanMakeForOther(csc))
					dac.getAgenteRiferimento().setCanMakeForOtherFb(new BooleanType(true));
			}
			return;
			
		}catch(DAOException daoe){
			String errorMsg = "prgm.ita.p.dac.facade.DacLoader: Eccezione DAO in loadDac: "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "prgm.ita.p.dac.facade.DacLoader: Eccezione in loadDac: "+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadErroriDocumentiDac(ClientSessionContext csc, DAOObject dao, DacModel dac) throws Exception {
		try{
			if(dao == null)
				dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			
			dac.setErroriDocumento((dao.executeQueryAccess("loadErroriDocumento",dac)).getResult());
			return;
			
		}catch(DAOException daoe){
			String errorMsg = "prgm.ita.p.dac.facade.DacLoader: Eccezione DAO in loadErroriDocumentiDac: "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "prgm.ita.p.dac.facade.DacLoader: Eccezione in loadErroriDocumentiDac: "+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadDocumentiDac(ClientSessionContext csc, DAOObject dao, DacModel dac) throws Exception {
		try{
			if(dao == null)
				dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			
			if (dac.getUfficio().equals(Costanti.UFFICIO_RETE) && !dac.getTipoDac().equals(Costanti.TIPO_DAC_SEDE))
				dac.setDocumenti(dao.executeQueryAccess("loadDocumentiDacRete",dac).getResult());
			else if(dac.isFaseDiSpunta())
				dac.setDocumenti(dao.executeQueryAccess("loadDocumentiDacSpunta",dac).getResult());
			else if(dac.isDopoSpunta())
				dac.setDocumenti(dao.executeQueryAccess("loadDocumentiDacSede",dac).getResult());
			else
				dac.setDocumenti(dao.executeQueryAccess("loadDocumentiDacRicerca",dac).getResult());
				
			
			for (int i=0; i<dac.getDocumenti().size(); i++){
				DocumentoModel doc =(DocumentoModel)dac.getDocumenti().get(i);
				doc.copyParams(dac);
				dao.executeQueryAccess("loadAgente",doc.getAgente());
				doc.getCliente().setNumContiCorrenti(new IntegerType());
				doc.getAgente().setNumContiCorrenti(new IntegerType());
				if(dac.isFaseDiSpunta() && !doc.getCliente().getCodMediolanum().isNull()){
					if(doc.isServizioFirmePerContoCorrente())
						dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getCliente());
					else
						doc.getCliente().setNumContiCorrenti(new IntegerType(1));
				}
					
				if(dac.isFaseDiSpunta() && !doc.getAgente().getCodMediolanum().isNull()){
					if(doc.isServizioFirmePerContoCorrente())
						dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getAgente());
					else
						doc.getAgente().setNumContiCorrenti(new IntegerType(1));
				}
				// Rinfresco i dati per il controllo firme agenti
				if(dac.isFaseDiSpunta())
					dao.executeQueryAccess("loadDatiDocumentoPerControlloFirmeAgentiInSpunta",doc);
			}
			
			DacTools.aggregaPlichi(dac);
			return;
			
		}catch(DAOException daoe){
			String errorMsg = "prgm.ita.p.dac.facade.DacLoader: Eccezione DAO in loadDocumentiDac: "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "prgm.ita.p.dac.facade.DacLoader: Eccezione in loadDOcumentiDac: "+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadMezziPgDocumento(ClientSessionContext csc, DAOObject dao, DocumentoModel doc) throws DAOException {
		DAOTableResultModel tRes = dao.executeTableLoadChildsAccess("mezzoDiPagamento", doc, MezzoPagamentoModel.class);
		doc.setMezziPagamento(tRes.getChilds());
		ListType mezziPg = doc.getMezziPagamento();
		for(int i=0;i<mezziPg.size();i++){
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			if(mezzoPg.getIdDocumentoAssegno().isNull())
				continue;
			DocumentoModel docAssegno = new DocumentoModel();
			docAssegno.setIdDocumento(mezzoPg.getIdDocumentoAssegno());
			dao.executeTableLoadAccess("datiDocumentoPadreAssegno",docAssegno);
			mezzoPg.setEsitoDocumentoAssegno(docAssegno.getEsito());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PlicoModel loadPlicoDocumento(ClientSessionContext csc, DAOObject dao, DocumentoModel doc, boolean alsoThisDoc) throws DAOException {
		PlicoModel result = new PlicoModel();
		if(doc.getCodAggregatore().isNull())
			return result;
		if(alsoThisDoc){
			DocumentoModel tmpDoc = new DocumentoModel();
			tmpDoc.setCodAggregatore(doc.getCodAggregatore());
			tmpDoc.setIdDocumento(new StringType("XXX"));
			result.setDocumenti(dao.executeQueryAccess("loadPlicoDocumento",tmpDoc).getResult());
		}else
			result.setDocumenti(dao.executeQueryAccess("loadPlicoDocumento",doc).getResult());
		return result;
	}
}
