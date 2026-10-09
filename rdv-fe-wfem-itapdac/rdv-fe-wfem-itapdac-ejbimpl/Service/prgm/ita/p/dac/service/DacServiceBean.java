package prgm.ita.p.dac.service;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.manager.DacManager;
import prgm.ita.p.dac.manager.DacManagerBean;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.util.DacTools;

@Stateless(name = "DacService", mappedName = "DacService")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
/***********************************************************************************************/
/***********************************************************************************************/
public class DacServiceBean extends ManagerObject implements DacService{

	private static final String DAO_DAC_XML_NAME = "ItaPDac.Dac";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel preparaInserimento(ClientSessionContext csc, String codAgente, Contratto contratto, 
									   Cliente cliente, MezzoPagamento[] mezziDiPagamento){
		try{
			
			DacModel dac = new DacModel();
			dac.setUfficio(new IntegerType(Costanti.UFFICIO_RETE));
			dac.setTipoDac(new IntegerType(Costanti.TIPO_DAC_STANDARD));

			DAOObject daoAss = new DAOObject(csc,DAO_DAC_XML_NAME);
			if(csc.isAssistenteFB()){
				ClientSessionContext cscAss = (ClientSessionContext)Tools.cloneObject(csc); 
				cscAss.setUserCode(csc.getCurrentLinkedUserCode().toString());
				daoAss = new DAOObject(cscAss,DAO_DAC_XML_NAME);
			}			
			DAOQueryResultModel qRes = null;
			if(Configuration.getInstance().isOfflineEnvironment())
				qRes = daoAss.executeQueryAccess("esisteDacCorrenteOffline",dac);
			else
				qRes = daoAss.executeQueryAccess("esisteDacCorrenteOnline",dac);
			
			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			
			StringType idDac = (StringType)qRes.getSingleResult();
			if(idDac == null || idDac.isNull()){
				dac.setUffMittente(new IntegerType(Costanti.UFFICIO_RETE));
				dac.setUffDestinatario(new IntegerType(Costanti.UFFICIO_CODING_SPUNTA));
				DacTools.impostaVersioneDac(csc,dac);
			}else{
				dac.setIdDac(idDac);
				dao.executeTableLoadAccess("dac",dac);
			}
			// Imposto l'agente di riferimento della Dac (quello collegato)
			dac.setAgenteRiferimento(DacTools.loadAgenteCollegato(csc,new IntegerType(Costanti.UFFICIO_RETE)));
			
			// Creo l'istanza di documento con l'agente passato
			DocumentoModel doc = new DocumentoModel();
			doc.copyParams(dac);
			doc.getAgente().setCodAgente(new StringType(codAgente));
			dao.executeQueryAccess("loadAgente",doc.getAgente());
			contratto.fillApplModel(doc);
			cliente.fillApplModel(doc.getCliente());
			
			if(mezziDiPagamento != null){
				for(int i=0;i<mezziDiPagamento.length;i++){
					MezzoPagamento mezzoPg = (MezzoPagamento)mezziDiPagamento[i];
					prgm.ita.p.dac.model.MezzoPagamentoModel mezzoPgDoc = new prgm.ita.p.dac.model.MezzoPagamentoModel();
					mezzoPg.fillDocModel(mezzoPgDoc);
					doc.getMezziPagamento().add(mezzoPgDoc);
				}
			}
			
			// Aggiunta del prefisso al codice aggregatore in caso di plico
			if(contratto.getAggregatorePlico() != null && contratto.getAggregatorePlico().length() > 0){
				doc.setContestoAggregatore(new StringType(contratto.getContestoPlico()));
				doc.setContestoAggregatoreOriginale(new StringType(contratto.getContestoPlico()));
				doc.setCodAggregatore(new StringType(contratto.getContestoPlico()+"-"+contratto.getAggregatorePlico()));
				doc.setCodAggregatoreOriginale(new StringType(contratto.getContestoPlico()+"-"+contratto.getAggregatorePlico()));
			}
			
			dac.setDocumento(doc);
			return dac;
			
		}catch(DAOException daoe){
			
			String errorMsg = getClass()+" Eccezione DAO in preparaInserimento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in preparaInserimento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public StringType inserisciDocumento(ClientSessionContext csc, DacModel dac) throws EJBException{
		try{
			
			DacManager manager = new DacManagerBean();
			if(dac.getIdDac().isNull())
				dac = manager.salvaDac(csc,dac);
			dac.getDocumento().copyParams(dac);
			DocumentoModel doc = manager.creaDocumento(csc,dac.getIdDac(),dac.getAgenteRiferimento(),dac.getDocumento(),Costanti.UFFICIO_RETE);
			return doc.getIdDocumento();
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in inserisciDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

}
