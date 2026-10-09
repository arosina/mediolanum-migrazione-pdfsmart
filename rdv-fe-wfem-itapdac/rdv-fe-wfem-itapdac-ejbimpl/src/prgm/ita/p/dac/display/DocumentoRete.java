package prgm.ita.p.dac.display;

import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DocumentoRete extends DisplayCommand {

	private static final String DAO_DAC_XML_NAME = "ItaPDac.Dac";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DocumentoKeyModel docKey = (DocumentoKeyModel)dataModel;
			
			// Per capire se il documento aveva la gestione assegni
			// leggo la versione del prit nel quale il documento è stato creato
			StringType versione = new StringType();
			try{
				String sql = "select T.PT_TESTATA_VERSIONE from CEPE_PT_TESTATA T, CEPE_PT_PRIT P "+
							 "where  T.PT_TESTATA_N_PROGR = P.PT_TESTATA_N_PROGR "+
							 "and    P.PT_DETTAGLIO_N_PROGR = '"+docKey.getIdDocumento()+"' "+
							 "and    T.PT_TIPOPRIT_N_TIPOPRIT != 5";
				DAOQueryResultModel qRes= DAOObject.executeDynaQueryAccess(csc,"CEPE",sql,null,MapCommandDataModel.class);
				if(qRes.getResult().size() > 0){
					MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
					versione = (StringType)out.getPropertyValue("ptTestataVersione");
				}else{ // Provo su IQ
					qRes= DAOObject.executeDynaQueryAccess(csc,"IQ_PRIT",sql,null,MapCommandDataModel.class);
					if(qRes.getResult().size() > 0){
						MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
						versione = (StringType)out.getPropertyValue("ptTestataVersione");
					}
				}
			}catch(DAOException de){}
			
			DocumentoModel docRete = loadDocRetePerConfronto(csc,docKey,versione,true);
			if(docRete == null){
				docRete = new DocumentoModel();
				docRete.setModality(Template.READ_MODALITY);
				return docRete;
			}
			
			DocumentoModel docSede = loadDocRetePerConfronto(csc,docKey,versione,false);
			if(docSede == null)
				throw new CommandException("Documento con ID=["+docKey.getIdDocumento()+"] non trovato");
			
			docRete.initChangedProps(docSede);
			docRete.setModality(Template.READ_MODALITY);
			return docRete;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DocumentoKeyModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private DocumentoModel loadDocRetePerConfronto(ClientSessionContext csc, DocumentoKeyModel docKey, 
												   StringType versione, boolean isCopia) throws Exception{
		DAOObject dao = null;
		try{
			String docDaoAccessName = "documento";
			String pgDaoAccessName = "mezzoDiPagamento";
			if(isCopia){
				docDaoAccessName = "documentoCpyRete";
				pgDaoAccessName = "mezzoDiPagamentoCpyRete";
			}
			
			dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			dao.openConnection();
			DocumentoModel doc = new DocumentoModel();
			doc.setIdDocumento(docKey.getIdDocumento());
			DAOTableResultModel tRes = dao.executeTableLoadAccess(docDaoAccessName,doc);
			if(tRes.getResult().intValue() == 0){
				if(Configuration.getInstance().isOfflineEnvironment())
					return null;
				dao.closeConnection();
				dao.openConnection("IQ_PRIT");
				tRes = dao.executeTableLoadAccess(docDaoAccessName,doc);
				if(tRes.getResult().intValue() == 0)
					return null;
				doc.addParam(ParamsModel.storicizzato);
			}			
			
			if(doc.isDocumentoAssegno())
				dao.executeTableLoadAccess(pgDaoAccessName,doc.getDatiAssegno());
			else
				doc.setMezziPagamento(dao.executeTableLoadChildsAccess(pgDaoAccessName,doc,MezzoPagamentoModel.class).getChilds());
			doc.setVersione(versione); // Imposto la versione
			if(isCopia)
				dao.fillCodDesc(doc);
			return doc;
			
		}catch(DAOException daoe){
			throw new Exception(getClass().getName()+".loadDocRetePerConfronto: eccezione DAO "+daoe);
		}catch(Exception e){
			throw new Exception(getClass().getName()+".loadDocRetePerConfronto: eccezione "+e);
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
}
