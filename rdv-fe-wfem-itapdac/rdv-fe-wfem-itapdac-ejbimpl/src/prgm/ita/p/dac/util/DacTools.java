package prgm.ita.p.dac.util;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.business.ApriDocumento;
import prgm.ita.p.dac.business.ChiudiDocumento;
import prgm.ita.p.dac.business.EsitaDocumento;
import prgm.ita.p.dac.business.NuovoDocumento;
import prgm.ita.p.dac.business.SalvaDocumento;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.DacLoader;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.model.PlicoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacTools {
	
	private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DAO_DAC_XML_NAME = "ItaPDac.Dac";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void inizializzaPritMOM(ClientSessionContext csc, DacModel dac) throws Exception{
		try{
			dac.setUfficio(new IntegerType(Costanti.UFFICIO_RETE));
			dac.setUffMittente(new IntegerType(Costanti.UFFICIO_RETE));
			dac.setUffDestinatario(new IntegerType(Costanti.UFFICIO_CODING_SPUNTA));
			dac.setUbicazione(new IntegerType(Costanti.UFFICIO_CODING_SPUNTA));
			dac.setIsPritMOM(new BooleanType(true));
			dac.setCodUtenteIns(new StringType(dac.getAgenteRiferimento().getCodAgente().toString()));
			DacTools.impostaVersioneDac(csc,dac);
			DacTools.aggregaPlichi(dac);
			
			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			dao.fillCodDesc(dac,true);
			
			dao.executeQueryAccess("loadAgente",dac.getAgenteRiferimento());
			for(int i=0;i<dac.getDocumenti().size();i++){
				DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);
				doc.setIdDac(new StringType(dac.getIdDac().toString()));
				dao.executeQueryAccess("loadAgente",doc.getAgente());
				doc.setDescrProdotto(new StringType(doc.getDescValue("codProdotto")));
				doc.setDescrOperazione(new StringType(doc.getDescValue("codOperazione")));
				
				double importoDoc = 0;
				for(int j=0;j<doc.getMezziPagamento().size();j++){
					MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)doc.getMezziPagamento().get(j);
					mezzoPg.setIdDocumento(new StringType(doc.getIdDocumento().toString()));
					importoDoc += mezzoPg.getImporto().doubleValue();
					if(!mezzoPg.getIdDocumentoAssegno().isNull() && mezzoPg.getCodTipoPagamento().intValue() == Costanti.MEZZO_PAGAMENTO_ASSEGNO)
						dao.executeTableLoadAccess("datiAssegnoNonSuMOM",mezzoPg);
				}
				
				if(doc.getCodProdotto().equals(Costanti.DOC_ASSEGNO))
					DocumentoAssegnoTools.impostaDatiDocumentoPadreDocAssegno(dac,doc,false);
				else
					doc.setImporto(new DoubleType(importoDoc));
			}
		}catch(DAOException daoe){
			throw new Exception("Eccezione DAO nel inizializzare un Prit MOM: "+daoe.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean alwaysCanMakeForOther(ClientSessionContext csc){
		// Utenti di sede che lavorano come se fossero promotori 
		String ageRif = csc.getCurrentLinkedUserCode();
		if(ageRif.equals("0000000108") || ageRif.equals("0000000322") || csc.isAssistenteFB())
			return true;
		try {
			BooleanType isProtectionSpecialist = (BooleanType)new DAOObject(csc, DAO_DAC_XML_NAME).executeQueryAccess("isProtectionSpecialist", null).getSingleResult();
			if(isProtectionSpecialist.booleanValue())
				return true;
		}catch(DAOException daoe) {
			return false;
		}
		return false;
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static void impostaVersioneDac(ClientSessionContext csc, DacModel dac){
		dac.setVersione(new StringType("2"));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static AgenteModel loadAgenteCollegato(ClientSessionContext csc, IntegerType ufficio) throws Exception{
		try{
			// Se non siamo in rete possiamo sempre fare i prit per altri fb
			if(!ufficio.isNull() && !ufficio.equals(Costanti.UFFICIO_RETE))
				return new AgenteModel();
			
			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			AgenteModel age = new AgenteModel();
			age.setCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadAgente",age);

			// Se il currentLinkedUserCode non è censito  (non credo sia mai vero) siamo sicuramente in sede e possiamo fare i prit per altri fb
			if(qRes.getResult().size() == 0)
				return new AgenteModel();
			
			// In ogni caso, se possiamo "sempre" fare i prit per altri, imposto la variabile a true
			if(DacTools.alwaysCanMakeForOther(csc))
				age.setCanMakeForOtherFb(new BooleanType(true));
			return age;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw new Exception(e.toString());			
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadUfficioUtente(ClientSessionContext csc, ParamsModel model) throws Exception{

		if(Configuration.getInstance().isOfflineEnvironment() ||
		   model.isStoricizzato())
			return;
			
		DAOObject dao = new DAOObject(csc,"ItaPDac.Dac");
		// Carico l'ufficio se non già impostato. Per la rete è sempre già impostato a RETE dai MenuCommand degli FB
		if(model.getUfficio().isNull()){
			try{
				if(model.getFnc().equals(Costanti.FNC_RICERCA)){ // In ricerca imposto CODING e non verifico se l'utente è in un ufficio. L'importante è che non sia RETE
					model.setUfficio(new IntegerType(Costanti.UFFICIO_CODING_SPUNTA));
				}else{
					DAOQueryResultModel qRes = dao.executeQueryAccess("loadUfficioUtente",model);
					if(qRes.getResult().size() == 0) // Utente non configurato
						throw new Exception("L'utente "+csc.getUserCode()+" non è associato ad un ufficio ");
				}
			}catch(DAOException daoe){
				throw new Exception(daoe.toString());
			}
		}
		
		// Leggo i parametri dell'ufficio dal DB (Solo online)
		if(Configuration.getInstance().isOnlineEnvironment()){
			try{
				dao.executeQueryAccess("loadParamsUfficio",model);
			}catch(DAOException daoe){
				throw new Exception(daoe.toString());			
			}
		}
		
		// Tutti gestiscono il barcode
		model.addParam(ParamsModel.gestoreBarcode);
		
		// Carico il parametro "Smistatore" se non già impostato
		if(!model.isSpeditore() && !model.isSmistatore() && model.getIsUfficioSmistatore().booleanValue())
			model.addParam(ParamsModel.smistatore);

		// Carico il parametro "Gestore plichi" se non già impostato
		if(!model.isMgmPlichi() && model.getIsUfficioMgmPlichi().booleanValue())
			model.addParam(ParamsModel.mgmPlichi);

		// Carico il parametro "Controllo cassette" se non già impostato
		if(!model.isSpeditore() && !model.isCtrlCassette() && model.getIsUfficioCtrlCassette().booleanValue())
			model.addParam(ParamsModel.ctrlCassette);
		
		// Carico il parametro "Controllo firme disattivo" se non già impostato
		if(!model.isCtrlFirmeDisattivo() && model.getIsUfficioCtrlFirmeDisatt().booleanValue())
			model.addParam(ParamsModel.noCtrlFirme);
		
		// Carico il parametro "Controllo firme agente disattivo" se non già impostato
		if(!model.isCtrlFirmeAgenteDisattivo() && model.getIsUfficioCtrlFirmeAgenteDisatt().booleanValue())
			model.addParam(ParamsModel.noCtrlFirmeAgente);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static DacModel salvaDocumentoModificato(UserSessionContext userSessionContext, DacModel dac) throws CommandException {
		try{
			if(dac.getDocumento().getIdDocModificato().isNull()){
				dac.getDocumento().resetCommandErrors();
				return dac;
			}
			StringType curIdDoc = dac.getDocumento().getIdDocumento();
			dac.getDocumento().setIdDocumento(dac.getDocumento().getIdDocModificato());
			
			// In spunta, se l'esito è già stato impostato come accettato o modificato, simulo l'evento di esitazione
			// in modo da tenere aggiornato l'esito "modificato"
			if(dac.getDocumento().getEsito().equals(Costanti.ESITO_DOC_ACCETTATO) || dac.getDocumento().getEsito().equals(Costanti.ESITO_DOC_MODIFICATO)){
				dac.getDocumento().setEsitoNew(new IntegerType(Costanti.ESITO_DOC_ACCETTATO));
				EsitaDocumento esitaDoc = new EsitaDocumento();
				dac = (DacModel)esitaDoc.execute(userSessionContext,dac);
			}else{
				SalvaDocumento salvaDoc = new SalvaDocumento();
				dac = (DacModel)salvaDoc.execute(userSessionContext,dac);
			}
			
			if(dac.getDocumento().hasCommandErrors())
				return dac;					
			dac.getDocumento().setIdDocumento(curIdDoc);
			DacLoader.loadDocumentiDac(userSessionContext.getClientSessionContext(),null,dac);
			return dac;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void aggregaPlichi(DacModel dac){
		
		// Riordino gli assegni in modo che siano sotto al documento padre
		// ed imposto l'id plico (cod-aggragatore) in modo da farli apparire sempre 
		// insieme al documento padre
		ListType dbResult = dac.getDocumenti();
		ListType elencoDocAss = new ListType(DocumentoModel.class);
		ListType elencoDoc = new ListType(DocumentoModel.class);

		for(int i=0;i<dbResult.size();i++){
			DocumentoModel doc = (DocumentoModel)dbResult.get(i);
			
			boolean isAssegno = doc.getCodProdotto().equals(Costanti.DOC_ASSEGNO);
			boolean isAssegnoSpinzato = isAssegno && doc.getCodAggregatore().isNull();
			
			if (!isAssegno || isAssegnoSpinzato) {
				elencoDoc.add(doc);
			} else {
				//Assegno pinzato ma nella DAC potrebbe non essere presente il padre 
				//caso possibile solo se viene aggiunto l'assegno prima del padre
				boolean padrePresente = false;
				for(int k=0;k<dbResult.size();k++){
					DocumentoModel docPadre = (DocumentoModel)dbResult.get(k);
					
					if (docPadre.getIdDocumento().equals(doc.getDatiAssegno().getIdDocumento())) {
						padrePresente=true;
						break;
					}
				}
				if (!padrePresente)
					elencoDoc.add(doc);
			}
		}		
		
		for(int i=0;i<elencoDoc.size();i++){
			DocumentoModel doc = (DocumentoModel)elencoDoc.get(i);
			elencoDocAss.add(doc);
			for(int j=dbResult.size()-1;j>=0;j--){
				DocumentoModel ass = (DocumentoModel)dbResult.get(j);
				if(ass.getCodProdotto().equals(Costanti.DOC_ASSEGNO) &&
				   !ass.getCodAggregatore().isNull() &&
				   ass.getDatiAssegno().getIdDocumento().equals(doc.getIdDocumento()) &&
				   !elencoDocAss.getElements().contains(ass)){
					elencoDocAss.add(ass);
				}
			}			
		}
		dac.setDocumenti(elencoDocAss);
		
		
		dbResult = dac.getDocumenti();
		
		boolean esisteAlmenoUnPlico=false;
		int countPlichi = 1;
		
		ListType tmpResult = new ListType(DocumentoModel.class);			
		for(int i=0;i<dbResult.size();i++){
			
			if(dbResult.get(i) == null) // Righe di plico già gestite
				continue;
			
			DocumentoModel doc = (DocumentoModel)dbResult.get(i);
			if(doc.getCodAggregatore().isNull()){ // Documento semplice
				StringType idPlicoDocInPlico = new StringType("DOC-"+Integer.toString(i)); 
				doc.setIdPlico(idPlicoDocInPlico);
				doc.setIsFirstInPlico(new BooleanType(true));
				doc.setIsLastInPlico(new BooleanType(true));
				tmpResult.add(doc);
				continue;
			}
			
			// Plico
			esisteAlmenoUnPlico = true;
			int countDocInPlico = 0;
			StringType codAggregatoreCorrente = doc.getCodAggregatore();
			StringType idPlico = new StringType(Integer.toString(countPlichi)); 
			PlicoModel plico = new PlicoModel();
			plico.setIdPlico(idPlico);
			for(int j=0;j<dbResult.size();j++){
				if(dbResult.get(j) == null) // Righe di plico già gestite
					continue;
				doc = (DocumentoModel)dbResult.get(j);
				if(doc.getCodAggregatore().equals(codAggregatoreCorrente)){
					if(countDocInPlico >= ALFABETO.length())
						countDocInPlico = 0;
					StringType idPlicoDocInPlico = new StringType(Integer.toString(countPlichi)+""+ALFABETO.charAt(countDocInPlico++)); 
					doc.setIdPlico(idPlicoDocInPlico);
					plico.getDocumenti().add(doc);
					dbResult.set(null,j);
				}
			}
			tmpResult.add(plico);
			if(plico.getDocumenti().size() > 1)
				countPlichi++;
		}
		
		if(!esisteAlmenoUnPlico){
			dac.setDocumenti(tmpResult);
			return;
		}
		
		ListType finalResult = new ListType(DocumentoModel.class);
		for(int i=0;i<tmpResult.size();i++){
			
			CommandDataModel el = (CommandDataModel)tmpResult.get(i);
			
			if(el instanceof DocumentoModel){
				finalResult.add(el);
				continue;
			}
			
			if(el instanceof PlicoModel){
				PlicoModel plico = (PlicoModel)el;
				for(int j=0;j<plico.getDocumenti().size();j++){
					DocumentoModel doc = (DocumentoModel)plico.getDocumenti().get(j);
					if(j == 0)
						doc.setIsFirstInPlico(new BooleanType(true));						
					if(j == (plico.getDocumenti().size()-1))
						doc.setIsLastInPlico(new BooleanType(true));
					finalResult.add(doc);
				}
				continue;					
			}
		}		
		dac.setDocumenti(finalResult);
		
		return;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void prossimoDocDaGestire(ClientSessionContext csc, BusinessCommand cmd, DacModel dac, 
										   boolean nuovoOnInserisci, boolean inClonazione) throws Exception{
		
		cmd.setForwardDisplay(new Integer(0));
		
		if(dac.getDocumento().hasCommandErrors())
			return;
		
		// Resetto il flag per la gestione del documento modificato		
		dac.getDocumento().setIdDocModificato(new StringType());
		
		if(dac.getDocumenti().size() == 0)
			return;

		if(!dac.isFaseDiSpunta()){
			if(nuovoOnInserisci){
				if(dac.isInClonazione()){
					cmd.setNextCommandClass(ChiudiDocumento.class);
					return;
				}else{
					dac.getDocumento().addCommandMessage("Inserimento effettuato correttamente");
					cmd.setNextCommandClass(NuovoDocumento.class);
					return;
				}
			}else{
				dac.getDocumento().addCommandMessage("Operazione effettuata correttamente");
			}
			return;
		}
		
		// Se il documento aperto non è ancora stato esitato del tutto non c'è un prossinmo doc da gestire
		int curDocIdx = -1;
		DocumentoModel curDoc = dac.getDocumento();
		ListType documenti = dac.getDocumenti();

		if(!curDoc.getIdDocumento().isNull()){
			if( curDoc.getEsito().isNull() || 
			   (curDoc.getHasControlloFirmeClienteAttivo().booleanValue() && curDoc.getEsitoFirmaCliente().isNull()) ||
			   (curDoc.getHasControlloFirmeAgenteAttivo().booleanValue() && curDoc.getEsitoFirmaAgente().isNull()))
				return;
			// Imposto l'indice del documento corrente
			for(int i=0;i<documenti.size();i++){
				DocumentoModel doc = (DocumentoModel)documenti.get(i);
				if(doc.getIdDocumento().equals(curDoc.getIdDocumento())){
					curDocIdx = i;
					break;
				}
			}
		}
		
		int docIdx = -1;
		for(int i=(curDocIdx+1); i<documenti.size(); i++){
			DocumentoModel doc = (DocumentoModel)documenti.get(i);
			if( doc.getEsito().isNull() || 
			   (doc.getHasControlloFirmeClienteAttivo().booleanValue() && doc.getEsitoFirmaCliente().isNull()) ||
			   (doc.getHasControlloFirmeAgenteAttivo().booleanValue() && doc.getEsitoFirmaAgente().isNull())){
				docIdx = i;
				break;
			}
		}
		if(docIdx < 0){ // Non ci sono altri doc da gestire
			dac.setDocumento(new DocumentoModel());
			if(nuovoOnInserisci){
				if(dac.isInClonazione()){
					cmd.setNextCommandClass(ChiudiDocumento.class);
					return;
				}else{
					dac.getDocumento().addCommandMessage("Inserimento effettuato correttamente");
					cmd.setNextCommandClass(NuovoDocumento.class);
					return;
				}
			}
			cmd.setNextCommandClass(ChiudiDocumento.class);
			return;
		}
		
		DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(docIdx);
		doc.copyParams(dac);
		if(dac.getDocumento().getIdDocumento().isNull()){
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class); 
			dac.setDocumento(facade.leggiDocumento(csc,(DocumentoKeyModel)doc));
		}else{
			dac.setDocumento(doc);
		}
		dac.getDocumento().copyParams(dac);
		dac.getDocumento().setVisible(true);
		cmd.setNextCommandClass(ApriDocumento.class);
		return;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	/*
	public static GenericCommandResponseModel manageVerticalViedoHeight(Command command, ParamsModel params){
		StringBuffer htmlResp = new StringBuffer();
		
		htmlResp.append("<html>");
		htmlResp.append("<body>");
		htmlResp.append("<table height='100%' width='100%'><tr><td valign='middle' align='center'>");
		htmlResp.append(	"<table bgcolor='#F0F0F0' cellspacing='0' cellpadding='0' border='1' width='300'>");
		htmlResp.append(	"<tr><td align='center'><table>");
		htmlResp.append(	"<tr><td>&nbsp;</td></tr>");
		htmlResp.append(	"<tr><td align='center' style='font-family:Arial;font-size:11pt;color: #1A458F;'>");
		htmlResp.append(		"<img src='/ItaPDac/images/waitMedium.gif'/>&nbsp;&nbsp;");
		htmlResp.append(		"Attendere prego...");
		htmlResp.append(	"</td></tr>");
		htmlResp.append(	"<tr><td>&nbsp;</td></tr>");
		htmlResp.append(	"</table></td></tr>");
		htmlResp.append(	"</table>");
		htmlResp.append("</td></tr></table>");
		
		htmlResp.append("<form name='resubmitForm'>");
		htmlResp.append("<input type='hidden' name='wfemCmd' value='"+command.getClass().getName()+".execute'>");
		htmlResp.append(params.htmlParams("",""));
		htmlResp.append("</form>");
		htmlResp.append("<script>");
		htmlResp.append("document.resubmitForm.verticalVideoHeight.value = screen.availHeight;");
		htmlResp.append("document.resubmitForm.submit();");
		htmlResp.append("</script>");
		
		htmlResp.append("</body>");
		
		byte[] bytes = htmlResp.toString().getBytes();
		GenericCommandResponseModel resp = new GenericCommandResponseModel();
		resp.setContentType("text/html");
		resp.setContent(bytes);
		resp.setContentLength(bytes.length);
		return resp;
	}
	*/
}

