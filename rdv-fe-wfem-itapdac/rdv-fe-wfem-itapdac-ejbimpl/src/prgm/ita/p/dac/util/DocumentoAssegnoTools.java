package prgm.ita.p.dac.util;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.ClienteModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class DocumentoAssegnoTools {

	/********************************************************************************************************/
	/********************************************************************************************************/
	public static DocumentoModel loadDocumentoPadreAssegno(DAOObject dao, DocumentoModel docAssegno, boolean loadMezziPg) throws DAOException{
		DocumentoModel docPadreAssegno = new DocumentoModel();
		docPadreAssegno.setIdDocumento(docAssegno.getDatiAssegno().getIdDocumento());
		docPadreAssegno.copyParams(docAssegno);
		dao.executeTableLoadAccess("datiDocumentoPadreAssegno",docPadreAssegno);
		if(docAssegno.isFaseDiSpunta())
			dao.executeTableLoadAccess("datiDocumentoPadreAssegnoSede",docPadreAssegno);
		if(loadMezziPg)
			docPadreAssegno.setMezziPagamento(dao.executeTableLoadChildsAccess("mezzoDiPagamento",docPadreAssegno,MezzoPagamentoModel.class).getChilds());
		return docPadreAssegno;
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static void impostaAggregatoreDocPadreAssegno(ClientSessionContext csc, DocumentoModel doc){
		if(doc.getFnc().equals(Costanti.FNC_SPUNTA))
			return;
		if(!doc.isDocAssegniAbilitati())
			return;
		boolean almenoUnAssegno = false;
		ListType mezziPg = doc.getMezziPagamento();
		for(int i=0;i<mezziPg.size();i++){
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			if(mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) || mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO)){
				almenoUnAssegno = true;
				break;
			}
		}
		if(!almenoUnAssegno){
			if(doc.getContestoAggregatore().isNull() || 
			   doc.getContestoAggregatore().equals(Costanti.CONTESTO_PLICO_DAC) ||
			   doc.getContestoAggregatore().equals(Costanti.CONTESTO_PLICO_ASS)){ // Il documento non è già aggregato ad altri documenti. Ripristino l'originale
				doc.setContestoAggregatore(new StringType(doc.getContestoAggregatoreOriginale().toString()));
				doc.setCodAggregatore(new StringType(doc.getCodAggregatoreOriginale().toString()));
			}
		}else{
			if(doc.getContestoAggregatore().isNull()){ // Il documento non è già aggregato ad altri documenti. 
				if(!doc.getContestoAggregatoreOriginale().isNull()){
					doc.setContestoAggregatore(new StringType(doc.getContestoAggregatoreOriginale().toString()));
					doc.setCodAggregatore(new StringType(doc.getCodAggregatoreOriginale().toString()));
				}else{
					doc.setContestoAggregatore(new StringType(Costanti.CONTESTO_PLICO_ASS));
					doc.setCodAggregatore(new StringType(Costanti.CONTESTO_PLICO_ASS+"-"+doc.getIdDocumento()));
				}
			}
		}
	}

	/***********************************************************************************************/
	/*
	 * Quando vedo un documento-assegno, faccio vedere anche i dati del documento padre del mezzoPg
	 * cui fa riferimento
	 */
	/***********************************************************************************************/
	public static void impostaDatiDocumentoPadreDocAssegno(DAOObject dao, DocumentoModel docAssegno) throws DAOException{
		dao.executeTableLoadAccess("mezzoDiPagamento",docAssegno.getDatiAssegno());

		// Imposto i dati del documento con quelli del documento padre datiDocumentoAssegno
		DocumentoModel docPadreAssegno = loadDocumentoPadreAssegno(dao,docAssegno,false);

		docAssegno.setNumeroContratto(docPadreAssegno.getNumeroContratto());
		docAssegno.setCodProdotto(docPadreAssegno.getCodProdotto());
		docAssegno.setCodOperazione(docPadreAssegno.getCodOperazione());
		if(docAssegno.getCodCassetta().isNull())
			docAssegno.setCodCassetta(docPadreAssegno.getCodCassetta());
		return;
	}
	
	/***********************************************************************************************/
	/*
	 * Stessa versione metodo precedente ma per i prit in memoria (Prit MOM)
	 */
	/***********************************************************************************************/
	public static void impostaDatiDocumentoPadreDocAssegno(DacModel dac, DocumentoModel docAssegno, boolean forDettaglio) throws Exception{
		// Cerco il documento padre dell'assegno per i prit MOM
		for(int i=0;i<dac.getDocumenti().size();i++){
			DocumentoModel docPadreAssegno = (DocumentoModel)dac.getDocumenti().get(i);
			if(!docPadreAssegno.getIdDocumento().equals(docAssegno.getDatiAssegno().getIdDocumento()))
				continue;
			
			// Cerco il mezzo pg di riferimento dell'assegno
			for(int j=0;j<docPadreAssegno.getMezziPagamento().size();j++){
				MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)docPadreAssegno.getMezziPagamento().get(j);
				if(!mezzoPg.getIdDocumentoAssegno().equals(docAssegno.getIdDocumento()))
					continue;
				mezzoPg = (MezzoPagamentoModel)Tools.cloneObject(mezzoPg);
				docAssegno.getDatiAssegno().setCodTipoPagamento(mezzoPg.getCodTipoPagamento());
				docAssegno.getDatiAssegno().setNumAssegno(mezzoPg.getNumAssegno());
				docAssegno.getDatiAssegno().setImporto(mezzoPg.getImporto());
				docAssegno.getDatiAssegno().setCodDivisa(mezzoPg.getCodDivisa());
				docAssegno.getDatiAssegno().setBanca(mezzoPg.getBanca());
				docAssegno.getDatiAssegno().setLuogoEmissione(mezzoPg.getLuogoEmissione());
				docAssegno.getDatiAssegno().setDataEmissione(mezzoPg.getDataEmissione());
				docAssegno.getDatiAssegno().setFlagTrasferibile(mezzoPg.getFlagTrasferibile());
				break;
			}
			
			// Imposto i dati del documento assegno con quelli del documento padre datiDocumentoAssegno
			// solo se sto visualizzando un dettaglio di un assegno
			if(forDettaglio){
				docAssegno.setBarcode(new StringType(docPadreAssegno.getBarcode().toString()));
				docAssegno.setNumeroContratto(new StringType(docPadreAssegno.getNumeroContratto().toString()));
				docAssegno.setCodProdotto(new IntegerType(docPadreAssegno.getCodProdotto().intValue()));
				docAssegno.setCodOperazione(new IntegerType(docPadreAssegno.getCodOperazione().intValue()));
			}
			if(docAssegno.getCodCassetta().isNull())
				docAssegno.setCodCassetta(new IntegerType(docPadreAssegno.getCodCassetta().intValue()));
			break;
		}
		return;
	}
	
	/***********************************************************************************************/
	/*
	 * Imposto i dati della CEPE_PT_DETTAGLIO di un documento-assegno, ereditati dal mezzoPg
	 */
	/***********************************************************************************************/
	public static void impostaDatiDocAssegno(DocumentoModel docAssegno) throws DAOException{
		docAssegno.setCodProdotto(new IntegerType(Costanti.DOC_ASSEGNO));
		docAssegno.setCodOperazione(new IntegerType(Costanti.DOC_ASSEGNO));
		docAssegno.setNumeroContratto(new StringType(docAssegno.getDatiAssegno().getNumAssegno().toString()));
		docAssegno.setDescrContratto(new StringType("Assegno n&deg; "+docAssegno.getDatiAssegno().getNumAssegno()+" - "+docAssegno.getDatiAssegno().getImporto()+" "+docAssegno.getDatiAssegno().getCodDivisa()+" - "+docAssegno.getDatiAssegno().getBanca()));
	}
	
	/***********************************************************************************************/
	/*
	 * Aggiorno il mezzoPg relativo ad un documento-assegno (Sul "salva/accetta") di un documento-assegno
	 */
	/***********************************************************************************************/
	public static void allineaMezzoDiPagmentoAssociato(DAOObject dao, DocumentoModel docAssegno) throws DAOException{
		MezzoPagamentoModel mezzoPg = new MezzoPagamentoModel();
		mezzoPg.setIdDocumento(new StringType(docAssegno.getDatiAssegno().getIdDocumento().toString()));
		mezzoPg.setIdMezzoPg(new IntegerType(docAssegno.getDatiAssegno().getIdMezzoPg().intValue()));
		DAOTableResultModel tRes = dao.executeTableLoadAccess("mezzoDiPagamento",mezzoPg);
		if(tRes.getResult().intValue() == 0) // Il mezzo pagamento potrebbe non esserci ancora quando arriviamo dalla "salvaMezziPg"
			return;
		mezzoPg.setNumAssegno(new StringType(docAssegno.getDatiAssegno().getNumAssegno().toString()));
		mezzoPg.setImporto(new DoubleType(docAssegno.getDatiAssegno().getImporto().doubleValue()));
		mezzoPg.setCodDivisa(new StringType(docAssegno.getDatiAssegno().getCodDivisa().toString()));
		mezzoPg.setBanca(new StringType(docAssegno.getDatiAssegno().getBanca().toString()));
		mezzoPg.setLuogoEmissione(new StringType(docAssegno.getDatiAssegno().getLuogoEmissione().toString()));
		mezzoPg.setDataEmissione(new DateType(docAssegno.getDatiAssegno().getDataEmissione().toString()));
		mezzoPg.setFlagTrasferibile(new StringType(docAssegno.getDatiAssegno().getFlagTrasferibile().toString()));
		mezzoPg.setBeneficiariAssegno(new StringType(docAssegno.getDatiAssegno().getBeneficiariAssegno().toString()));
		dao.executeTableUpdateAccess("mezzoDiPagamento",mezzoPg);
	}
	
	/***********************************************************************************************/
	/*
	 * Crea/Legge il documento-assegno relativo al documento padre del mezzoPg specificato
	 */
	/***********************************************************************************************/	
	public static DocumentoModel initDocumentoAssegnoAssociatoAMezzoPg(ClientSessionContext csc, DAOObject dao,
																	   DocumentoModel docPadre, MezzoPagamentoModel mezzoPg) throws Exception, DAOException{

		if(!docPadre.isDocAssegniAbilitati())
			return null;

		DocumentoModel docAssegno = new DocumentoModel();
		docAssegno.copyParams(docPadre);
		
		// Carico i dati del mezzoPg
		docAssegno.setDatiAssegno((MezzoPagamentoModel)Tools.cloneObject(mezzoPg));
		
		// Verifico se il documento assegno associato al mezzoPg esiste già e se si lo leggo
		docAssegno.getDatiAssegno().setIdDocumento(new StringType(docPadre.getIdDocumento().toString()));
		docAssegno.getDatiAssegno().setIdMezzoPg(new IntegerType(mezzoPg.getIdMezzoPg().intValue()));
		StringType idDocAssegno = (StringType)dao.executeQueryAccess("getIdDocumentoAssegno",docAssegno).getSingleResult();
		if(idDocAssegno != null && !idDocAssegno.isNull()){
			docAssegno.setIdDocumento(idDocAssegno);
			dao.executeTableLoadAccess("documento",docAssegno);
			if(!docAssegno.getUfficio().equals(Costanti.UFFICIO_RETE))
				dao.executeTableLoadAccess("documentoSede",docAssegno);
		}else{
			// In spunta si possono solo aggiungere assegni e non cancellarli
			if(docAssegno.isAutoSpuntataParams())
				docAssegno.setEsito(new IntegerType(Costanti.ESITO_DOC_ACCETTATO));
			else if(docAssegno.isFaseDiSpunta()){
				// In spunta non creo i documenti assegno per quelli inseriti dalla rete
				// Per capire quali sono verifico se esiste la copia nella mezzoPgCpyRete
				MezzoPagamentoModel copiaMezzoPg = new MezzoPagamentoModel();
				copiaMezzoPg.setIdDocumento(docPadre.getIdDocumento());
				copiaMezzoPg.setIdMezzoPg(mezzoPg.getIdMezzoPg());
				DAOTableResultModel tRes = dao.executeTableLoadAccess("mezzoDiPagamentoCpyRete", copiaMezzoPg);
				if(tRes.getResult().intValue() == 1){ // Se c'è la copia torno null in modo da non creare il documento-assegno
					if(copiaMezzoPg.getCodTipoPagamento().equals(mezzoPg.getCodTipoPagamento()))
						return null;
				}
				docAssegno.setEsito(new IntegerType(Costanti.ESITO_DOC_AGGIUNTO));
			}
		}
		
		// Dati del documento assegno ereditati dal mezzo di pagamento.
		docAssegno.getDatiAssegno().setImporto(new DoubleType(mezzoPg.getImporto().doubleValue()));
		docAssegno.getDatiAssegno().setCodDivisa(new StringType(mezzoPg.getCodDivisa().toString()));
		
		// Dati del documento assegno ereditati dal documento padre. In spunta non creo i plichi
		if(!docAssegno.getFnc().equals(Costanti.FNC_SPUNTA)){
			docAssegno.setContestoAggregatore(new StringType(docPadre.getContestoAggregatore().toString()));
			docAssegno.setCodAggregatore(new StringType(docPadre.getCodAggregatore().toString()));
		}
		
		docAssegno.setIdDac(new StringType(docPadre.getIdDac().toString()));
		docAssegno.setAgenteRiferimento((AgenteModel)Tools.cloneObject(docPadre.getAgenteRiferimento()));
		docAssegno.setUbicazione(new IntegerType(docPadre.getUbicazione()));
		docAssegno.setCodInforeteEsterno(new StringType(docPadre.getCodInforeteEsterno().toString()));
		docAssegno.setCliente((ClienteModel)Tools.cloneObject(docPadre.getCliente()));
		docAssegno.setAgente((AgenteModel)Tools.cloneObject(docPadre.getAgente()));
		impostaDatiDocAssegno(docAssegno);
		return docAssegno;
	}
	
}
