package prgm.ita.anagraficaclienti.questionari.facade;

import java.util.ArrayList;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.SmsSender;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.cedacri.adeguatezza.AdeguatezzaManager;
import prgm.cedacri.adeguatezza.model.ElementoQuestionarioRisposteModel;
import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioModel;
import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.facade.InfoLoader;
import prgm.ita.anagraficaclienti.facade.StatiPropostaAnagrafica;
import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.InfoPritModel;
import prgm.ita.anagraficaclienti.model.MailModel;
import prgm.ita.anagraficaclienti.pcp.ProfiloPcpClienteLoader;
import prgm.ita.anagraficaclienti.pcp.ProfiloPcpClienteModel;
import prgm.ita.anagraficaclienti.pcp.ProfiloPcpToCedacriMapper;
import prgm.ita.anagraficaclienti.questionari.model.AvvisoModel;
import prgm.ita.anagraficaclienti.questionari.model.PatrimonioModel;
import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;
import prgm.ita.anagraficaclienti.questionari.punteggi.Punteggi;
import prgm.ita.p.dac.service.DacServiceCaller;

/***********************************************************************************************/
/***********************************************************************************************/
@Stateless(name = "QuestionariFacade", mappedName = "QuestionariFacade")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuestionariFacadeBean extends FacadeObject implements QuestionariFacade{
	
	private static final String CAN_VEND 		= "P";
	private static final String CAN_DIR  		= "C";
	private static final String CAN_INTERNET  	= "I";
	
	private static final String CED_ESITO_OK 				= "000";
	private static final String CED_NESSUNA_RISPOSTA		= "004";
	private static final String CED_INCOMPLETO_COMPILATO 	= "105";
	private static final String CED_QUEST_DUPLICATO 		= "106";
	private static final String CED_TIT_STUDIO_NULL 		= "013";
	private static final String CED_ETA_NULL		 		= "014";
	
	private static final String DAO_XML_NAME = "ItaAnagraficaClienti.Verifiche";
	private static final String S_END_LI = "</li>";
	private static final String DAO_COGEST_XML_NAME = "ItaAnagraficaClienti.AnagraficaCogestione";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Deprecated
	public PatrimonioModel getPatrimonio(ClientSessionContext csc, ClienteKeyModel clienteKey) throws EJBException{
		try{
			
			if(clienteKey.getCodAgente().isNull())
				clienteKey.setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));

			AnagraficaClientiFacade anagBean = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel cliente = anagBean.leggiClienteLight(csc,clienteKey);

			PatrimonioModel patrimonio = new PatrimonioModel();
			patrimonio.setCliente(cliente);
			
			//Solo profili validati dalla sede
			OutputGetQuestionarioModel cedOutModel = innerGetProfilo(csc, clienteKey, CAN_DIR);
				
			patrimonio.getCliente().setVersioneQuestionario(cedOutModel.getRelease());			
			patrimonio.getCliente().setCodProfiloDiInvestimento(cedOutModel.getProfilo());
			patrimonio.getCliente().setCodClusterCedacri(cedOutModel.getCluster());
			patrimonio.getCliente().setDescrClusterCedacri(cedOutModel.getDesCluster());
			patrimonio.getCliente().setDFinValCedacri(cedOutModel.getDfinval());
			patrimonio.getCliente().setEspfina(cedOutModel.getEspfina());
			
			if(!cedOutModel.getSeCompi().equals("S")){
				//Profilo non assegnato...visualizzare messaggio				
				patrimonio.setDatiClienteInsufficientiMsg(patrimonio.getDatiClienteInsufficientiMsg().concat("<br><span style=\"color:red;\">Attenzione. Il cliente selezionato non presenta un profilo attivo validato dalla sede.</span>"));
			}else if(!cedOutModel.getSeValid().equals("S")){
				//Profilo provvisorio...visualizzare messaggio 
				patrimonio.setDatiClienteInsufficientiMsg(patrimonio.getDatiClienteInsufficientiMsg().concat("<br><span style=\"color:red;\">Attenzione. Il cliente selezionato non presenta un profilo attivo validato dalla sede.</span>"));
			}else if (cedOutModel.getProfilo().isNull()){
				//Profilo non assegnato...visualizzare messaggio
				patrimonio.setDatiClienteInsufficientiMsg(patrimonio.getDatiClienteInsufficientiMsg().concat("<br><span style=\"color:red;\">Attenzione. Il cliente selezionato non presenta un profilo attivo validato dalla sede</span>"));
			}
			
			return patrimonio;
			
		}catch(Exception e){
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public QuestionarioModel getQuestionario(ClientSessionContext csc, ClienteKeyModel clienteKey) throws EJBException{
		try{
			
			if(clienteKey.getCodAgente().isNull())
				clienteKey.setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));

			AnagraficaClientiFacade anagBean = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel cliente = anagBean.leggiClienteLight(csc,clienteKey);

			QuestionarioModel questionario = new QuestionarioModel();
			questionario.setCliente(cliente);

			if(cliente.getStato().equals(StatiPropostaAnagrafica.NON_TROVATA))
				return questionario;
			
			BooleanType isQuestionarioInFirmaDigitale = (BooleanType)clienteKey.getDynamicData().readProperty("isQuestionarioInFirmaDigitale");
			if(isQuestionarioInFirmaDigitale != null && isQuestionarioInFirmaDigitale.booleanValue())
				questionario.setInFirmaDigitale(true);
			
			OutputGetQuestionarioModel profiloModel = getProfiloModel(csc, clienteKey);
			questionario.getCliente().setVersioneQuestionario(profiloModel.getRelease());			
			questionario.getCliente().setCodProfiloDiInvestimento(profiloModel.getProfilo());
			questionario.getCliente().setCodClusterCedacri(profiloModel.getCluster());
			questionario.getCliente().setDescrClusterCedacri(profiloModel.getDesCluster());
			questionario.getCliente().setDFinValCedacri(profiloModel.getDfinval());	
			questionario.getCliente().setEspfina(profiloModel.getEspfina());
			
			ListType elementiQuestionarioCedacri = null;
			if(profiloModel.getRelease().intValue() < 10)
				elementiQuestionarioCedacri = ricavaElementiNuovoQuestionario(csc, questionario.isInFirmaDigitale() ? CAN_INTERNET : CAN_VEND);
			else
				elementiQuestionarioCedacri = ricavaElementiUltimoQuestionarioCliente(csc, clienteKey, questionario.isInFirmaDigitale() ? CAN_INTERNET : CAN_VEND);
			
			questionario.initFromElementiQuestionarioCedacri(csc,elementiQuestionarioCedacri);
			return questionario;
			
		}catch(Exception e){
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private ListType ricavaElementiUltimoQuestionarioCliente(ClientSessionContext csc, ClienteKeyModel clienteKey, 
															 String canVend) throws Exception{
		
		String codMediolanum = clienteKey.getCodMediolanum().toString();
		if(codMediolanum.length() == 0)
			codMediolanum = Tools.fillSx(codMediolanum,'0',11);			
		String codPotenziale = clienteKey.getCodPotenziale().toString();
		if(codPotenziale.length() == 0)
			codPotenziale = Tools.fillSx(codPotenziale,'0',16);
		InputGetQuestionarioModel input = new InputGetQuestionarioModel();
		input.setCanVend(new StringType(canVend));
		input.setCountry(new StringType(csc.getCountryCode()));
		input.setUsername(new StringType(csc.getUserCode()));
 		input.setNdgDoss(new StringType(codMediolanum));
 		input.setNdgTemp(new StringType(codPotenziale));
		
		AdeguatezzaManager adegBean = (AdeguatezzaManager)ROF.getManager(csc,AdeguatezzaManager.class);
 		OutputGetQuestionarioModel cedOut = adegBean.getQuestionario(csc,input);
 		return cedOut.getQuestionario();
	}
	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private ListType ricavaElementiNuovoQuestionario(ClientSessionContext csc, String canVend) throws Exception{
		
		InputGetNuovoQuestionarioModel input = new InputGetNuovoQuestionarioModel();
		input.setCanVend(new StringType(canVend));
		input.setCountry(new StringType(csc.getCountryCode()));
		input.setUsername(new StringType(csc.getUserCode()));
		
		AdeguatezzaManager adegBean = (AdeguatezzaManager)ROF.getManager(csc,AdeguatezzaManager.class);
		OutputGetNuovoQuestionarioModel cedOut = adegBean.getNuovoQuestionario(csc,input);
		return cedOut.getQuestionario();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public OutputGetQuestionarioModel getProfiloModel(ClientSessionContext csc, ClienteKeyModel clienteKey) throws EJBException{
		return innerGetProfilo(csc, clienteKey, CAN_VEND); 
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getProfilo(ClientSessionContext csc, ClienteKeyModel clienteKey) throws EJBException{
		return innerGetProfilo(csc, clienteKey, CAN_VEND).getProfilo(); 
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getProfiloSede(ClientSessionContext csc, ClienteKeyModel clienteKey) throws EJBException{
		return innerGetProfilo(csc, clienteKey, CAN_DIR).getProfilo(); 
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private OutputGetQuestionarioModel innerGetProfilo(ClientSessionContext csc, ClienteKeyModel clienteKey, 
													   String canVend) throws EJBException{
		try{
			
			String flagProvvisorio = "N";
			if(canVend.equals(CAN_VEND))
				flagProvvisorio = "S";
			ProfiloPcpClienteModel profiloPcp = ProfiloPcpClienteLoader.loadProfiloPcp(csc, clienteKey, flagProvvisorio);
			return ProfiloPcpToCedacriMapper.fromProfiloPcpToProfiloCedacri(profiloPcp);
			
		}catch(Exception e){
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public QuestionarioModel calcoloProfilo(ClientSessionContext csc, QuestionarioModel questionario) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,DAO_XML_NAME);
			dao.openConnection();

			Tools.resetTypesErrors(questionario);
			questionario.resetCommandErrors();
			questionario.setDomandaInErrore(new IntegerType(-1));

			ClienteModel cliente = questionario.getCliente();

			String codMediolanum = cliente.getCodMediolanum().toString();
			if(codMediolanum.length() == 0)
				codMediolanum = Tools.fillSx(codMediolanum,'0',11);			
			String codPotenziale = cliente.getCodPotenziale().toString();
			if(codPotenziale.length() == 0)
				codPotenziale = Tools.fillSx(codPotenziale,'0',16);			

			ListType risposteCedacri = questionario.retrieveRisposteCedacri();
			
			if(questionario.getCliente().getInfoPersonali().getCodTitoloStudio().isNull()){
				
				questionario.addCommandError("Per il cliente selezionato non e' presente il titolo di studio in anagrafica. Effettuare una variazione anagrafica");
				return questionario;
				
			}else{
				
				// Controllo l'adeguatezza del titolo di studio con l'anagrafica
				for(int i=0; i<risposteCedacri.size(); i++){
					ElementoQuestionarioRisposteModel rispTitStud = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
					if(rispTitStud.getNumElem().equals(QuestionarioModel.NUMERO_DOMANDA_TITOLO_DI_STUDIO)){
						ArrayList<String> codMed = MappaturaTitoliDiStudio.fromCedacriToMed(rispTitStud.getNumSele().intValue());
						if(!codMed.contains(cliente.getInfoPersonali().getCodTitoloStudio().toString())){
							questionario.setDomandaInErrore(new IntegerType(QuestionarioModel.NUMERO_DOMANDA_TITOLO_DI_STUDIO));
							questionario.getDomandaInErrore().addTypeError("Il titolo di studio selezionato non è congruente con quello presente in anagrafica");
							questionario.addCommandError("Verificare la compilazione dei campi");
							return questionario;
						}
						break;
					}
				}
			}
			
			InputCalcoloProfiloModel input = new InputCalcoloProfiloModel();
	 		input.setNdgDoss(new StringType(codMediolanum));
	 		input.setNdgTemp(new StringType(codPotenziale));
	 		input.setEta(questionario.getCliente().getEta());
	 		input.setTitStud(questionario.getCliente().getInfoPersonali().getCodTitoloStudio());

	 		input.setRisposte(risposteCedacri);
	 		
	 		if(questionario.isInFirmaDigitale())
	 			input.setCanVend(new StringType(CAN_INTERNET));
	 		else
	 			input.setCanVend(new StringType(CAN_VEND));
			input.setCountry(new StringType(csc.getCountryCode()));
			input.setUsername(new StringType(csc.getUserCode()));

			//System.out.println(Tools.xmlFromModel(input));
			
			AdeguatezzaManager bean = (AdeguatezzaManager)ROF.getManager(csc,AdeguatezzaManager.class);
			OutputCalcoloProfiloModel cedOut =  bean.calcoloProfilo(csc,input);
			questionario.setOutputCedacri(cedOut);
			if(!controllaErrori(questionario,cedOut.getEsito(),cedOut.getDescErr(),cedOut.getNumElem()))
				return questionario;

			//System.out.println(Tools.xmlFromModel(cedOut));
			
			questionario.setCodProfiloDiInvestimento(cedOut.getProfilo());
			questionario.setCodClusterCedacri(cedOut.getCluster());
			questionario.setDescrClusterCedacri(cedOut.getDesCluster());
			questionario.setDFinValCedacri(cedOut.getDfinval());
			
	 		/* MIFID3 CEDACRI - 20140829 aggiunta Disc */
			questionario.setObbtemp(cedOut.getObbtemp());
			questionario.setSitfina(cedOut.getSitfina());
			questionario.setObbinve(cedOut.getObbinve());
			questionario.setEspfina(cedOut.getEspfina());
	 		/* MIFID3 CEDACRI - 20140829 aggiunta Disc */
			
			if(!questionario.getAlertAfterCalcoloViewed().booleanValue()){
				
				boolean b2a = false;
				boolean b5d = false;
				boolean c1a = false;
				boolean c2a = false;
				
				for(int i=0; i<risposteCedacri.size(); i++){
					
					ElementoQuestionarioRisposteModel r = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
					
					if(r.getNumElem().intValue() == 5 && r.getNumSele().intValue() == 1){
						b2a = true;
					}

					if(r.getNumElem().intValue() == 8 && (r.getNumSele().intValue() == 8 || r.getNumSele().intValue() == 9 || r.getNumSele().intValue() == 10)){
						b5d = true;
					}
					
					if(r.getNumElem().intValue() == 9 && r.getNumSele().intValue() == 1){
						c1a = true;
					}
					
					if(r.getNumElem().intValue() == 10 && r.getNumSele().intValue() == 1){
						c2a = true;
					}
					
				}
				
				int punteggioD4 = Punteggi.getPunteggioDomandaD4(risposteCedacri);
				
				if(c1a)
					questionario.getAlertAfterCalcolo().add("Avendo risposto a) alla domanda C1, il Suo Orizzonte Temporale non pu&ograve; essere Lungo e inoltre Lei non pu&ograve; investire in prodotti illiquidi");
				
				if(punteggioD4 < 3 && b2a && c2a)
					questionario.getAlertAfterCalcolo().add("Avendo ottenuto complessivamente un punteggio inferiore a 3 rispondendo alla domanda D4 e avendo risposto contemporaneamente a) alle domande B2 e C2, il Suo Profilo di Investitore &egrave; CONSERVATORE");
				
				if(b2a && b5d)
					questionario.getAlertAfterCalcolo().add("Avendo risposto a) alla domanda B2 e d) alla domanda B5, il Suo Profilo di Investitore non pu&ograve; essere INTRAPRENDENTE");
			}
			
			return questionario;
	 	
		}catch(DAOException daoe){
			String errorMsg = getClass()+ "Eccezione DAO nel controllare il Titolo di Studio: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel controllare il Titolo di Studio: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public QuestionarioModel salvaQuestionario(ClientSessionContext csc, QuestionarioModel questionario) throws EJBException{
		try{
			
			Tools.resetTypesErrors(questionario);
			questionario.resetCommandErrors();
			questionario.setDomandaInErrore(new IntegerType(-1));
			
			questionario = calcoloProfilo(csc,questionario);
			if(questionario.hasCommandErrors()) // Errori nel calcolo profilo
				return questionario;
			
			if(questionario.getAlertAfterCalcolo().size() > 0){
				questionario.setCmdAfterCalcolo("SalvaQuestionario");
				return questionario;
			}
			
			//#99620 - vengono eliminati dei controlli sul questionario cartaceo che nella 9.7.0 eerano tutti commentati
			
			ClienteModel cliente = questionario.getCliente();
			
			String codMediolanum = cliente.getCodMediolanum().toString();
			if(codMediolanum.length() == 0)
				codMediolanum = Tools.fillSx(codMediolanum,'0',11);			
			String codPotenziale = "";
			if(cliente.getCodMediolanum().isNull())
				codPotenziale = cliente.getCodPotenziale().toString();
			if(codPotenziale.length() == 0)
				codPotenziale = Tools.fillSx(codPotenziale,'0',16);
						
	 		InputSalvaQuestionarioModel input = new InputSalvaQuestionarioModel();
	 		input.setBozza(new StringType("N"));
	 		input.setNdgDoss(new StringType(codMediolanum));
	 		input.setNdgTemp(new StringType(codPotenziale));
	 		input.setEta(questionario.getCliente().getEta());
	 		input.setProfilo(questionario.getCodProfiloDiInvestimento());
	 		input.setCluster(questionario.getCodClusterCedacri());
	 		input.setTitStud(questionario.getCliente().getInfoPersonali().getCodTitoloStudio());
	 		//#99620
	 		if(!questionario.isInFirmaDigitale()){
		 		input.setDataOraComp(Tools.now());
	 		}else{
 		 		input.setDataOraComp(questionario.getDataOraCompilazione());
	 			input.setNumSched(questionario.getNumSched());
	 		}
	 		
	 		/* MIFID3 CEDACRI - 20140829 aggiunta Disc */
	 		input.setObbtemp(questionario.getObbtemp());
	 		input.setSitfina(questionario.getSitfina());
	 		input.setObbinve(questionario.getObbinve());
	 		input.setEspfina(questionario.getEspfina());
	 		/* MIFID3 CEDACRI - 20140829 aggiunta Disc */

	 		input.setRisposte(questionario.retrieveRisposteCedacri());

	 		if(questionario.isInFirmaDigitale())
	 			input.setCanVend(new StringType(CAN_INTERNET));
	 		else
	 			input.setCanVend(new StringType(CAN_VEND));
			input.setCountry(new StringType(csc.getCountryCode()));
			input.setUsername(new StringType(csc.getUserCode()));
			
			AdeguatezzaManager bean = (AdeguatezzaManager)ROF.getManager(csc,AdeguatezzaManager.class);
			OutputSalvaQuestionarioModel cedOut =  bean.salvaQuestionario(csc,input);
			if(!controllaErrori(questionario,cedOut.getEsito(),cedOut.getDescErr(),cedOut.getNumElem()))
				return questionario;
	 		
			//La vera data di scadenza viene restituita solo dal salvaQuestionario. Quella del metodo calcolaProfilo si basa sulla
			//data di sistema quindi non attendibile
			questionario.setDFinValCedacri(cedOut.getDfinval());
			// 
			questionario.getCliente().setCodProfiloDiInvestimento(questionario.getCodProfiloDiInvestimento());
			questionario.getCliente().setCodClusterCedacri(questionario.getCodClusterCedacri());
			questionario.getCliente().setDescrClusterCedacri(questionario.getDescrClusterCedacri());
			questionario.getCliente().setDFinValCedacri(questionario.getDFinValCedacri());
			questionario.getCliente().setEspfina(questionario.getEspfina());
			questionario.setCodProfiloDiInvestimento(new StringType());
			questionario.setCodClusterCedacri(new StringType());
			questionario.setDescrClusterCedacri(new StringType());
			questionario.setDFinValCedacri(new IntegerType());
			questionario.addCommandMessage("Profilo salvato correttamente");
			questionario.setSalvato(true);
			
			if(!questionario.isInFirmaDigitale())
				inserisciPritQuestionario(csc,questionario);
			
			inviaAvvisoAlSenior(csc, questionario);
			
			try {
				cliente.setDataOraUltimaModifica(Tools.now());
				new DAOObject(csc, "ItaAnagraficaClienti.AnagraficaClientiOracle").executeTableInsertAccess("tracciaCreazioneProfilo", cliente);
			}catch(DAOException daoe){
				LOG.error(daoe);
			}
			
			return questionario;
			
		}catch(Exception e){
			throw new EJBException(e.toString());
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void inviaAvvisoAlSenior(ClientSessionContext csc, QuestionarioModel questionario) {
		
		ClienteModel cliente = questionario.getCliente();
		if(cliente.getCodAgente().equals(cliente.getAgente().getCodAgente())) // Se è se stesso nessuna comunicazione
			return;
					
		String testo = "Il Banker Consultant " + cliente.getAgente().getNomeAgente()+ " "+cliente.getAgente().getCognomeAgente() + " ha inserito un PCP per il cliente " +cliente.getNome()+" "+cliente.getCognome();
					
		AvvisoModel avvisoModel = new AvvisoModel();
		
		avvisoModel.setSorgenteEsterna(new StringType("none"));
		avvisoModel.setCodTipoAvviso(new StringType("04"));
		avvisoModel.setPortaInPrimoPiano(new BooleanType(false));
		avvisoModel.setCodUserDestinatario(cliente.getCodAgente()); // Cod FB Senior
		
		avvisoModel.setOggetto(new StringType("Operativita' del BC inserimento PCP")); 
		avvisoModel.setTesto(new StringType(testo)); 
		avvisoModel.setPrevistaAttivita(new BooleanType(false));
		
		avvisoModel.setCodiceAgente(cliente.getAgente().getCodAgente()); 
		avvisoModel.setNominativoAgente(cliente.getAgente().getNominativoAgente());
		
		avvisoModel.setCodiceCliente(cliente.getCodMediolanum());
		avvisoModel.setNominativoCliente(cliente.getNominativo());
		
		try {
			new DAOObject(csc, DAO_COGEST_XML_NAME).executeQueryAccess("popolaDatiUserDestinatario", avvisoModel);
		}catch(DAOException daoe) {
			questionario.appendMessaggioCentrale("<li>Si è verificato un errore di sistema nel recuperare il destinatario per il FB Senior con codice "+cliente.getCodAgente()+S_END_LI);
			return;
		}
			
		try {
									
			String serviceName = "/rdv-be-osb-ItaAvvisiWS/PS/PS_AvvisiWS";
			String accessName = "notificaAvviso";

			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_COGEST_XML_NAME).executeOSBAccess(accessName, avvisoModel);
			
			if (wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED) {
				questionario.appendMessaggioCentrale("<li>Si sono verificati problemi tecnici: Servizio " + serviceName + " disabilitato"+S_END_LI);
			} else if (wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK) {
				questionario.appendMessaggioCentrale("<li>Si sono verificati problemi tecnici: Errore di comunicazione con il sistema remoto " +serviceName+S_END_LI);
			} 	
							
			
		}catch(DAOException daoe){
			questionario.appendMessaggioCentrale("<li>Errore DAO nell'invio dell'avviso al FB Senior con codice "+cliente.getCodAgente()+ " " + daoe.getMessage()+ " " +S_END_LI);
		}catch(Exception e){
			questionario.appendMessaggioCentrale("<li>Errore nell'invio dell'avviso al FB Senior con codice "+cliente.getCodAgente()+ " " + e.getMessage()+ " " +S_END_LI);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void inviaMailSmsAlSenior(ClientSessionContext csc, QuestionarioModel questionario) {
		
		ClienteModel cliente = questionario.getCliente();
		if(cliente.getCodAgente().equals(cliente.getAgente().getCodAgente())) // Se è se stesso nessuna comunicazione
			return;
		
		AgenteModel senior = new AgenteModel();
		senior.setCodAgente(cliente.getCodAgente());
		try {
			new DAOObject(csc, DAO_COGEST_XML_NAME).executeQueryAccess("loadCellulareMailAgente", senior);
		}catch(DAOException daoe) {
			questionario.appendMessaggioCentrale("<li>Si è verificato un errore si sistema nel recuperare mail e cellulare dell'FB Senior con codice "+senior.getCodAgente()+S_END_LI);
			return;
		}
		
		if(senior.getEmail().isNull() && senior.getCellulare().isNull()) {
			questionario.appendMessaggioCentrale("<li>Nessuna mail e/o cellulare trovata per l'FB Senior con codice "+senior.getCodAgente()+S_END_LI);
			return;
		}
		
		String smsText = "E' stato inserito un PCP per il cliente "+cliente.getNome()+" "+cliente.getCognome()+" codice cliente "+cliente.getCodMediolanum()+" dal BC codice "+cliente.getAgente().getCodAgente();
		String mailSubject = "Notifica compilazione PCP da parte del BC";
		String mailBody = 	"Si notifica che è stato inserito un PCP per il cliente "+cliente.getNome()+" "+cliente.getCognome()+" codice cliente "+cliente.getCodMediolanum()+" da parte del BC con codice "+cliente.getAgente().getCodAgente()+".<br>"+ 
							"Cordiali saluti.<br><br>"+
							"Questa è una mail automatica, si prega di non rispondere.";
		
		if(!senior.getEmail().isNull()) {
			try {
				MailModel mail = new MailModel();
				mail.setMailStatus(new StringType("N"));
				mail.setSchedTime(Tools.now());      
				mail.setFrom(new StringType("bmed.comunicazioni@bancamediolanum.it"));
				mail.setContentType(new StringType("text/html"));
				mail.setTo(new StringType(senior.getEmail().toString().toString()));
				mail.setSubject(new StringType(mailSubject));				
				mail.setBody(new StringType(mailBody));
				mail.setUserIns(new StringType("PCP"));
		
				// Inserisco la mail 
				new DAOObject(csc,"ItaAnagraficaClienti.AnagraficaClienti").executeTableInsertAccess("mail", mail);
				
			}catch(DAOException daoe){
				questionario.appendMessaggioCentrale("<li>Si è verificato un errore DAO nell'invio della mail all'FB Senior con codice "+senior.getCodAgente()+S_END_LI);
			}catch(Exception e){
				questionario.appendMessaggioCentrale("<li>Si è verificato un errore nell'invio della mail all'FB Senior con codice "+senior.getCodAgente()+S_END_LI);
			}
		}

		if(!senior.getCellulare().isNull()) {
			try {
				new SmsSender().sendSMS(senior.getCellulare().toString(), smsText);
			}catch(Exception e) {
				questionario.appendMessaggioCentrale("<li>Si è verificato un errore nell'invio dell'sms all'FB Senior con codice "+senior.getCodAgente()+S_END_LI);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean controllaErrori(QuestionarioModel questionario,StringType esito, StringType descErrore, IntegerType numElem){
		if(esito.equals(CED_ESITO_OK))
			return true;

		String myDescr=null;
		if(esito.equals(CED_INCOMPLETO_COMPILATO)){
			if(!numElem.isNull()){
				questionario.setDomandaInErrore(numElem);
				questionario.getDomandaInErrore().addTypeError("Compilare tutte le risposte del questionario");
			}
			myDescr = "Non e' stata fornita la risposta ad una domanda. Rispondere a tutte le domande del questionario";
		}else if(esito.equals(CED_NESSUNA_RISPOSTA)){
			myDescr = "Rispondere a tutte le domande del questionario";			
		}else if(esito.equals(CED_QUEST_DUPLICATO)){
			questionario.getDataOraCompilazione().addTypeError("Esiste gia' un questionario compilato a quest&rsquo;ora");
			myDescr = "Verificare la compilazione dei campi";			
		}else if(esito.equals(CED_ETA_NULL)){
			myDescr = "Per il cliente selezionato non e' stata censita la Data di Nascita che, nel caso in cui non desideri rispondere al questionario, e' obbligatoria";			
		}else if(esito.equals(CED_TIT_STUDIO_NULL)){
			myDescr = "Per il cliente selezionato non e' stato censito il Titolo di Studio che, nel caso in cui non desideri rispondere al questionario, e' obbligatorio";			
		}

		if(myDescr == null)
			questionario.addCommandError("["+esito+"]: "+descErrore);
		else
			questionario.addCommandError(myDescr);
		
		questionario.setCodProfiloDiInvestimento(new StringType());
		questionario.setCodClusterCedacri(new StringType());	
		questionario.setDescrClusterCedacri(new StringType());
		questionario.setDFinValCedacri(new IntegerType());	
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void inserisciPritQuestionario(ClientSessionContext csc, QuestionarioModel questionario) throws Exception{

		String codCliente = null;
		if(!questionario.getCliente().getCodMediolanum().isNull())
			codCliente = questionario.getCliente().getCodMediolanum().toString();
		else
			codCliente = questionario.getCliente().getCodPotenziale().toString();
		
		prgm.ita.p.dac.service.Contratto contratto = new prgm.ita.p.dac.service.Contratto();
		prgm.ita.p.dac.service.Cliente cliente = new prgm.ita.p.dac.service.Cliente();
				
		InfoPritModel infoPritModel = InfoLoader.getInfoPrit(csc,new StringType(Costanti.CODICE_PRODOTTO),new StringType(Costanti.CHIAVE_PRIT_PERSONAL_PROFILE));
		try{
			contratto.setNumeroContratto(codCliente);
			contratto.setCodInforete(questionario.getNumSched().toString());
			contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
			contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
			
			if(!questionario.getCliente().getCodMediolanum().isNull())
				cliente.setCodMediolanum(questionario.getCliente().getCodMediolanum().toString());
			cliente.setCognome(questionario.getCliente().getCognome().toString());
			cliente.setNome(questionario.getCliente().getNome().toString());

			DacServiceCaller.inserisciDocumento(csc, questionario.getCliente().getAgente().getCodAgente().toString(), 
												Costanti.NOME_RISORSA, contratto,cliente,null);
			questionario.appendMessaggioCentrale("<li>Allegare il questionario</li>");			
		}catch(Throwable tr){
			questionario.appendMessaggioCentrale("<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa al questionario. Provare ad inserirla tramite l'applicazione Prit Promotore</li>");
		}
					
		/* Scheda Privacy Questionario se selezionato dall'utente */
		if(questionario.getIsPrivacyAllegata().booleanValue()){			
			infoPritModel = InfoLoader.getInfoPrit(csc,new StringType(Costanti.CODICE_PRODOTTO),new StringType(Costanti.CHIAVE_PRIT_PRIVACY_PERSONAL_PROFILE));
			try{
				contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
				contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
				
				DacServiceCaller.inserisciDocumento(csc, questionario.getCliente().getAgente().getCodAgente().toString(), 
													Costanti.NOME_RISORSA,contratto,cliente,null);
				questionario.appendMessaggioCentrale("<li>Allegare il modulo privacy per il questionario</li>");
			}catch(Throwable tr){
				questionario.appendMessaggioCentrale("<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa al modulo privacy per il questionario. Provare ad inserirla tramite l'applicazione Prit Promotore</li>");
			}
		}
		
		questionario.appendMessaggioCentrale("<li>Contestualmente al personal profile &egrave stato sottoscritto anche un prodotto?<br>In caso positivo &egrave necessario inviarli pinzati nello stesso prit onde evitare sospesi/respinti</li>");
		
					
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Deprecated
	public PatrimonioModel salvaPatrimonio(ClientSessionContext csc, PatrimonioModel patrimonio) throws EJBException{
		try{
			
			Tools.resetTypesErrors(patrimonio);
			patrimonio.resetCommandErrors();
			patrimonio.setDomandaInErrore(new IntegerType(-1));
						

			if(patrimonio.getNumSched().isNull()){
				patrimonio.getNumSched().addTypeError("Impostare il numero della scheda");
			}
			
			if(patrimonio.getDataOraCompilazione().isNull())
				patrimonio.getDataOraCompilazione().addTypeError("Impostare la data e l'ora di compilazione del questionario");
			else if(patrimonio.getDataOraCompilazione().isEmptyHour())
				patrimonio.getDataOraCompilazione().addTypeError("Impostare l'ora di compilazione del questionario");
			
			DateType oggi = Tools.today();
			if(patrimonio.getDataOraCompilazione().toDateType().compareTo(oggi) > 0)
				patrimonio.getDataOraCompilazione().addTypeError("La data non può essere maggiore della data odierna");
				
			if(Tools.containsTypeErrors(patrimonio)){
				patrimonio.addCommandError("Verificare la compilazione dei campi");
				return patrimonio;
			}
			
			ClienteModel cliente = patrimonio.getCliente();
			
			String codMediolanum = cliente.getCodMediolanum().toString();
			if(codMediolanum.length() == 0)
				codMediolanum = Tools.fillSx(codMediolanum,'0',11);			
			String codPotenziale = "";
			if(cliente.getCodMediolanum().isNull())
				codPotenziale = cliente.getCodPotenziale().toString();
			if(codPotenziale.length() == 0)
				codPotenziale = Tools.fillSx(codPotenziale,'0',16);
						
			InputAggiornaPatrimonioModel input = new InputAggiornaPatrimonioModel();
	 		
	 		input.setNdgDoss(new StringType(codMediolanum));
	 		input.setNdgTemp(new StringType(codPotenziale));
	 		input.setDataOraComp(patrimonio.getDataOraCompilazione());
	 		input.setNumSched(patrimonio.getNumSched());
	 		
	 		LOG.debug(getClass().getName()+": SALVATAGGIO PATRIMONIO: dati in input: ndg=["+input.getNdgDoss()+"] ndgTemp=["+input.getNdgTemp()+"]");
	 		
	 		input.setCanVend(new StringType(CAN_VEND));
			input.setCountry(new StringType(csc.getCountryCode()));
			input.setUsername(new StringType(csc.getUserCode()));

			IntegerType importo = new IntegerType(0);
			
			if(patrimonio.getElementiPatrimonio().getImporto1().isNull())
				input.setImporto1(importo);
			else
				input.setImporto1(patrimonio.getElementiPatrimonio().getImporto1());

			if(patrimonio.getElementiPatrimonio().getImporto2().isNull())
				input.setImporto2(importo);
			else
				input.setImporto2(patrimonio.getElementiPatrimonio().getImporto2());

			if(patrimonio.getElementiPatrimonio().getImporto3().isNull())
				input.setImporto3(importo);
			else
				input.setImporto3(patrimonio.getElementiPatrimonio().getImporto3());

			if(patrimonio.getElementiPatrimonio().getImporto4().isNull())
				input.setImporto4(importo);
			else
				input.setImporto4(patrimonio.getElementiPatrimonio().getImporto4());
			
			if(patrimonio.getElementiPatrimonio().getImporto5().isNull())
				input.setImporto5(importo);
			else
				input.setImporto5(patrimonio.getElementiPatrimonio().getImporto5());
			
			if(patrimonio.getElementiPatrimonio().getImporto6().isNull())
				input.setImporto6(importo);
			else
				input.setImporto6(patrimonio.getElementiPatrimonio().getImporto6());
			
			if(patrimonio.getElementiPatrimonio().getImporto7().isNull())
				input.setImporto7(importo);
			else
				input.setImporto7(patrimonio.getElementiPatrimonio().getImporto7());
			
			if(patrimonio.getElementiPatrimonio().getImporto8().isNull())
				input.setImporto8(importo);
			else
				input.setImporto8(patrimonio.getElementiPatrimonio().getImporto8());
			
			if(patrimonio.getElementiPatrimonio().getImporto9().isNull())
				input.setImporto9(importo);
			else
				input.setImporto9(patrimonio.getElementiPatrimonio().getImporto9());
			
			AdeguatezzaManager bean = (AdeguatezzaManager)ROF.getManager(csc,AdeguatezzaManager.class);
			OutputAggiornaPatrimonioModel cedOut =  bean.aggiornaPatrimonio(csc,input);
			if(!controllaErroriPatrimonio(patrimonio,cedOut.getEsito(),cedOut.getDescErr(),cedOut.getNumElem()))
				return patrimonio;
	 		
			//La vera data di scadenza viene restituita solo dal salvaQuestionario. Quella del metodo calcolaProfilo si basa sulla
			//data di sistema quindi non attendibile
			//patrimonio.setDFinValCedacri(cedOut.getDfinval());
			// 
			/*patrimonio.getCliente().setCodProfiloDiInvestimento(patrimonio.getCodProfiloDiInvestimento());
			patrimonio.getCliente().setCodClusterCedacri(patrimonio.getCodClusterCedacri());
			patrimonio.getCliente().setDescrClusterCedacri(patrimonio.getDescrClusterCedacri());
			patrimonio.getCliente().setDFinValCedacri(patrimonio.getDFinValCedacri());
			patrimonio.setCodProfiloDiInvestimento(new StringType());
			patrimonio.setCodClusterCedacri(new StringType());
			patrimonio.setDescrClusterCedacri(new StringType());
			patrimonio.setDFinValCedacri(new IntegerType());*/
			patrimonio.addCommandMessage("Patrimonio salvato correttamente");
			patrimonio.setSalvato(true);
			
			inserisciPritPatrimonio(csc,patrimonio);
			
			return patrimonio;
			
		}catch(Exception e){
			throw new EJBException(e.toString());
		}		
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Deprecated
	private boolean controllaErroriPatrimonio(PatrimonioModel patrimonio,StringType esito, StringType descErrore, IntegerType numElem){
		if(esito.equals(CED_ESITO_OK))
			return true;

		/*
		 * 004: campo non compilato, con il campo indicato nel “descErr”
		   007: operazione non valida, con il motivo indicato nel “descErr”
		   300: non trovato profilo sulla tabella TW00TBSK
           301: non trovati i dati sulla tabella TW00TBQK
		   302: non trovati i dati della risposta multipla sulla tabella TW00TBQM
		   006: inserimento non riuscito nella tabella, con la tabella indicata nel “descErr”
		   106: record già presente nella tabella, con la tabella indicata nel “descErr”
*/
		String myDescr=null;
		if(esito.equals(CED_INCOMPLETO_COMPILATO)){
			if(!numElem.isNull()){
				patrimonio.setDomandaInErrore(numElem);
				patrimonio.getDomandaInErrore().addTypeError("Compilare tutte le risposte del questionario");
			}
			myDescr = "Non è stata fornita la risposta ad una domanda. Rispondere a tutte le domande del questionario";
		}else if(esito.equals(CED_NESSUNA_RISPOSTA)){
			myDescr = "Rispondere a tutte le domande del questionario";			
		}else if(esito.equals(CED_QUEST_DUPLICATO)){
			patrimonio.getDataOraCompilazione().addTypeError("Esiste già un questionario compilato a quest&rsquo;ora");
			myDescr = "Verificare la compilazione dei campi";			
		}else if(esito.equals(CED_ETA_NULL)){
			myDescr = "Per il cliente selezionato non è stata censita la Data di Nascita che, nel caso in cui non desideri rispondere al questionario, è obbligatoria";			
		}else if(esito.equals(CED_TIT_STUDIO_NULL)){
			myDescr = "Per il cliente selezionato non è stato censito il Titolo di Studio che, nel caso in cui non desideri rispondere al questionario, è obbligatorio";			
		}

		if(myDescr == null)
			patrimonio.addCommandError("["+esito+"]: "+descErrore);
		else
			patrimonio.addCommandError(myDescr);
		
		patrimonio.setCodProfiloDiInvestimento(new StringType());
		patrimonio.setCodClusterCedacri(new StringType());	
		patrimonio.setDescrClusterCedacri(new StringType());
		patrimonio.setDFinValCedacri(new IntegerType());	
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Deprecated
	private void inserisciPritPatrimonio(ClientSessionContext csc, PatrimonioModel patrimonio) throws Exception{

		String codCliente = null;
		if(!patrimonio.getCliente().getCodMediolanum().isNull())
			codCliente = patrimonio.getCliente().getCodMediolanum().toString();
		else
			codCliente = patrimonio.getCliente().getCodPotenziale().toString();
		
		prgm.ita.p.dac.service.Contratto contratto = new prgm.ita.p.dac.service.Contratto();
		prgm.ita.p.dac.service.Cliente cliente = new prgm.ita.p.dac.service.Cliente();
		
		InfoPritModel infoPritModel = InfoLoader.getInfoPrit(csc,new StringType(Costanti.CODICE_PRODOTTO),new StringType(Costanti.CHIAVE_PRIT_VARIAZIONE_PATIMONIO));
		try{
			contratto.setNumeroContratto(codCliente);
			contratto.setCodInforete(patrimonio.getNumSched().toString());
			contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
			contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
			
			if(!patrimonio.getCliente().getCodMediolanum().isNull())
				cliente.setCodMediolanum(patrimonio.getCliente().getCodMediolanum().toString());
			cliente.setCognome(patrimonio.getCliente().getCognome().toString());
			cliente.setNome(patrimonio.getCliente().getNome().toString());

			DacServiceCaller.inserisciDocumento(csc, patrimonio.getCliente().getAgente().getCodAgente().toString(), 
												Costanti.NOME_RISORSA, contratto,cliente,null);
			patrimonio.appendMessaggioCentrale("<li>Allegare il questionario</li>");
		}catch(Throwable tr){
			patrimonio.appendMessaggioCentrale("<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa al questionario sul patrimonio. Provare ad inserirla tramite l'applicazione Prit Promotore</li>");
		}
					
		/* Scheda Privacy Questionario se selezionato dall'utente */
		if(patrimonio.getIsPrivacyAllegata().booleanValue()){			
			infoPritModel = InfoLoader.getInfoPrit(csc,new StringType(Costanti.CODICE_PRODOTTO),new StringType(Costanti.CHIAVE_PRIT_PRIVACY_PERSONAL_PROFILE));
			try{
				contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
				contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
				
				DacServiceCaller.inserisciDocumento(csc, patrimonio.getCliente().getAgente().getCodAgente().toString(), 
													Costanti.NOME_RISORSA,contratto,cliente,null);
				patrimonio.appendMessaggioCentrale("<li>Allegare il modulo privacy per il questionario</li>");
			}catch(Throwable tr){
				patrimonio.appendMessaggioCentrale("<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa al modulo privacy per il questionario. Provare ad inserirla tramite l'applicazione Prit Promotore</li>");
			}
		}
					
	}	
}
