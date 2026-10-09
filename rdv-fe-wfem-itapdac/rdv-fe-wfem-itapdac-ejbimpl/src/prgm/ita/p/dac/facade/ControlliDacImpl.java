package prgm.ita.p.dac.facade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;
import prgm.ita.p.dac.model.PlicoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlliDacImpl implements ControlliDac{

    private static final String DAO_DAC_XML_NAME = "ItaPDac.Dac";
    private static final String DATO_ASSEGNO_OBBLIGATORIO = "Attenzione! Il campo è obbligatorio e deve riportare quanto presente sull'assegno, qualora il titolo sia sprovvisto di tale dato dovrà essere restituito al cliente.";
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean controllaDac(ClientSessionContext csc, DacModel dac) throws Exception{
		
		DocumentoModel savDocumento = dac.getDocumento();
		ListType savDocumenti = dac.getDocumenti();
		dac.setDocumento(new DocumentoModel());
		dac.setDocumenti(new ListType());
		
		dac.resetCommandErrors();
		Tools.resetTypesErrors(dac);
		Tools.checkCodDescFieldsValidity(dac);
		
		if(dac.isDopoSpunta()){
			if (dac.isSmistatore()) {
				if(dac.getBox().isNull())
					dac.getBox().addTypeError("Campo obbligatorio");
			} else {
				if(dac.getUffDestinatario().isNull())
					dac.getUffDestinatario().addTypeError("Campo obbligatorio");
			}
		}

		boolean result = true;
		if(Tools.containsTypeErrors(dac))
			result = false;

		dac.setDocumento(savDocumento);
		dac.setDocumenti(savDocumenti);
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean controllaDocumento(ClientSessionContext csc, DocumentoModel doc) throws Exception{
		DAOObject dao = null;
		DAOObject daoIq = null;
		try{
			doc.resetCommandMessages();
			doc.resetCommandErrors();
			Tools.resetTypesErrors(doc);
			Tools.checkCodDescFieldsValidity(doc);
			doc.setErrorsOnDati(false);
			doc.setErrorsOnMezziPg(false);

			dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			dao.openConnection();

			if(doc.isFaseDiSpunta()){
				
				if(doc.isGestoreBarcode()){
					if(doc.isEsitoDocumentoMancante()){
						doc.setBarcode(new StringType());
						doc.setCodCassetta(new IntegerType());
					}else{
						if(doc.getBarcode().isNull()){
							doc.getBarcode().addTypeError("Specificare il codice a barre");
							doc.setErrorsOnDati(true);
						}else{
							if(doc.getBarcode().equals("X")){
								doc.getBarcode().addTypeError("Valore riservato");
								doc.setErrorsOnDati(true);
							}else{
								DAOQueryResultModel qRes = dao.executeQueryAccess("esisteBarcode",doc);
								if(((IntegerType)qRes.getSingleResult()).intValue() > 0){
									doc.getBarcode().addTypeError("Il barcode specificato è già stato assegnato ad un altro documento");
									doc.setErrorsOnDati(true);
								}else{ // Verifico sullo storico
									daoIq = new DAOObject(csc,DAO_DAC_XML_NAME);
									daoIq.openConnection("IQ_PRIT");
									qRes = daoIq.executeQueryAccess("esisteBarcode",doc);
									if(((IntegerType)qRes.getSingleResult()).intValue() > 0){
										doc.getBarcode().addTypeError("Il barcode specificato è già stato assegnato ad un altro documento");
										doc.setErrorsOnDati(true);
									}									
								}
							}
						}
							
						if(doc.isCtrlCassette() && doc.getCodCassetta().isNull()){
							doc.getCodCassetta().addTypeError("Specificare la cassetta");
							doc.setErrorsOnDati(true);
						}
					}
				}
			}
			
			// Se manca il cartaceo non verifico i controlli (tanto se mancano dati l'operatore non saprebbe cosa fare)
			if(doc.isEsitoDocumentoMancante()){
				return true;
			}
			
			// ************************************************************ //
			// **     GESTIONE CONTROLLI DOCUMENTO ASSEGNO     ************ //
			// ************************************************************ //
			if(doc.isDocumentoAssegno()){
				if(!doc.getCodInforeteEsterno().isNull()){
					return true;
				}
				boolean datiAssegnoOk = controllaDatiMezzoPgAssegno(doc.getDatiAssegno()); 
				if(!datiAssegnoOk || doc.isErrorsOnDati()){
					doc.setErrorsOnDati(true);
					return false;
				}else
					return true;
			}
			
			if(doc.getCodProdotto().isNull()){
				doc.getCodProdotto().addTypeError("Specificare il prodotto");
				doc.setErrorsOnDati(true);
			}
			
			if(doc.getCodOperazione().isNull()){
				doc.getCodOperazione().addTypeError("Specificare l'operazione");
				doc.setErrorsOnDati(true);
			}
			
			if(doc.isDocumentoReportAdeguatezza()){
				if(doc.getIdReportAdeguatezza().isNull()){
					doc.getIdReportAdeguatezza().addTypeError("Per proseguire inserisci l'ID del Report di Adeguatezza");
					doc.setErrorsOnDati(true);
				}
			}else{
				doc.setIdReportAdeguatezza(new StringType());				
			}
			
			if(doc.isDocumentoRaccomandazioneIdd()){
				if(doc.getNumeroContratto().isNull()){
					doc.getNumeroContratto().addTypeError("Per proseguire inserisci l'ID della raccomandazione");
					doc.setErrorsOnDati(true);
				}
			}

			// Riverifico la corretta compilazione di prodotto / operazione
			if(!doc.getCodProdotto().isNull() && !doc.getCodOperazione().isNull()){
				DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
														"select	PT_PRODOPER_F_VALIDITA "+
														"from CEPE_PT_RETEP_PRODOPER "+
														"where PT_PRODOTTO_N_PT_PRODOTTO = "+doc.getCodProdotto()+
														"and   PT_OPERAZIONE_N_PT_OPERAZIONE = "+doc.getCodOperazione(),
														null, MapCommandDataModel.class);
				if(qRes.getResult().size() == 0){
					doc.getCodOperazione().addTypeError("L'operazione non è compatibile con il prodotto selezionato");
				}else{
					MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
					StringType validita = (StringType)out.getPropertyValue("ptProdoperFValidita");
					if(!validita.equals("S")){
						qRes = dao.executeQueryAccess("loadDescrizioneOperazione",doc);
						StringType descrOp = (StringType)qRes.getSingleResult();
						if(descrOp != null && !descrOp.isNull())
							doc.getCodOperazione().addTypeError("L'operazione <b>&#9658; "+descrOp.toString()+" &#9668;</b> specificata nel documento, non è pi&ugrave; valida");
						else
							doc.getCodOperazione().addTypeError("L'operazione specificata nel documento, non è pi&ugrave; valida");
					}
				}
			}

			if(doc.getAgente().getCodAgente().isNull()){
				doc.getAgente().getCodAgente().addTypeError("Selezionare il Family Banker");
				doc.setErrorsOnDati(true);
			}
			
			boolean obblDati = false; boolean obblCli = false; boolean obblAlmenoUnMezzoPg = false; boolean obblSpese = false;
			char fobblDati = 'N'; char fobblCli = 'N'; char fobblMezzoPg = 'N'; char fobblSpese = 'N';
			DAOQueryResultModel qRes = dao.executeQueryAccess("regoleControlli",doc);
			StringType sregole = (StringType)qRes.getSingleResult();
			if(sregole != null && !sregole.isNull()){
				String regchars = sregole.toString().toUpperCase();
				fobblDati = regchars.charAt(0);
				fobblCli = regchars.charAt(1);
				fobblMezzoPg = regchars.charAt(2);
				fobblSpese = regchars.charAt(3);
			}
			
			boolean siamoInSede = true;
			if(doc.getUfficio().equals(Costanti.UFFICIO_RETE) || !doc.isAutoSpuntataParams())
				siamoInSede = false;
			
			// 'E'=Entrambi / 'S'=Sede / 'R'=Rete / 'N' o null = Nessun controllo
			if(fobblDati == 'E')
				obblDati = true;
			else if(fobblDati == 'S' && siamoInSede)
				obblDati = true;
			else if(fobblDati == 'R' && !siamoInSede)
				obblDati = true;
			
			if(fobblCli == 'E')
				obblCli = true;
			else if(fobblCli == 'S' && siamoInSede)
				obblCli = true;
			else if(fobblCli == 'R' && !siamoInSede)
				obblCli = true;
	
			if(fobblMezzoPg == 'E')
				obblAlmenoUnMezzoPg = true;
			else if(fobblMezzoPg == 'S' && siamoInSede)
				obblAlmenoUnMezzoPg = true;
			else if(fobblMezzoPg == 'R' && !siamoInSede)
				obblAlmenoUnMezzoPg = true;
	
			// 'S'=Obbligatorio / 'N' o null = Non obbligatorio
			if(doc.getUfficio().equals(Costanti.UFFICIO_RETE) && fobblSpese == 'S')
				obblSpese = true;
			
			if(obblDati){
				if(doc.getNumeroContratto().isNull()){
					doc.getNumeroContratto().addTypeError("Specificare il numero di contratto/polizza");
					doc.setErrorsOnDati(true);
				}
			}
			
			if(obblCli){
				if(doc.getCliente().getCognome().isNull()){
					doc.getCliente().getCognome().addTypeError("Selezionare il cliente");
					doc.setErrorsOnDati(true);
				}
			}
			
			if(obblSpese){
				if(doc.getSpese().isNull()){
					doc.getSpese().addTypeError("Specificare le spese");
					doc.setErrorsOnDati(true);
				}
			}
			
			if(obblAlmenoUnMezzoPg){
				if(doc.getMezziPagamento().size() == 0){
					doc.getNessunMezzoDiPagamento().addTypeError("Specificare almeno un mezzo di pagamento");
					doc.setErrorsOnMezziPg(true);
				}
			}
			
			// Controllo sui mezzi di pagamento
			for(int i=0;i<doc.getMezziPagamento().size();i++){
				MezzoPagamentoModel m = (MezzoPagamentoModel)doc.getMezziPagamento().get(i);
				if(m.getCodTipoPagamento().isNull()){
					m.getCodTipoPagamento().addTypeError("Specificare il mezzo di pagamento");
					doc.setErrorsOnMezziPg(true);
				}else if(m.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) || m.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO)){

					if(!controllaDatiMezzoPgAssegno(m))
						doc.setErrorsOnMezziPg(true);
					
					// Verifico che non ci sia più di una volta lo stesso numero assegno
//					int cc = 0;
//					for(int j=0;j<doc.getMezziPagamento().size();j++){
//						MezzoPagamentoModel m2 = (MezzoPagamentoModel)doc.getMezziPagamento().get(j);
//						if(!m2.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) || m.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO))
//							continue;
//						if(m2.getNumAssegno().equals(m.getNumAssegno()))
//							cc++;
//					}
//					if(cc > 1){
//						m.getNumAssegno().addTypeError("Numero assegno ripetuto");
//						doc.setErrorsOnMezziPg(true);
//					}
					
				}
			}
			
			if(Tools.containsTypeErrors(doc))
				return false;
			
			
			//CLAUDIO
			//Se sono promotore, non è presente il disp_c_disp e non vi sono errori visualizzo l'eventuale messaggio.......chiedere come fare a riconoscerlo...
			if(doc.getUfficio().equals(Costanti.UFFICIO_RETE) && doc.getCodInforeteEsterno().isNull()){ 

				DAOQueryResultModel qResAlert = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
						"select	PT_PRODOPER_F_ALERT_CDEPOSITO  "+
						"from CEPE_PT_RETEP_PRODOPER "+
						"where PT_PRODOTTO_N_PT_PRODOTTO = "+doc.getCodProdotto()+
						"and   PT_OPERAZIONE_N_PT_OPERAZIONE = "+doc.getCodOperazione(),
						null, MapCommandDataModel.class);
	
				if(qResAlert.getResult().size() > 0){
					MapCommandDataModel out = (MapCommandDataModel)qResAlert.getResult().get(0);
					StringType visualizzaAlert = (StringType)out.getPropertyValue("ptProdoperFAlertCdeposito");
					if(visualizzaAlert.equals("S"))	
						doc.setVisualizzaAlert(true);
				}
			}	
			//FINE CLAUDIO
			
			return true;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}finally{
			if(dao != null)   dao.closeConnection();
			if(daoIq != null) daoIq.closeConnection();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean controllaDatiMezzoPgAssegno(MezzoPagamentoModel mezzoPg){
		boolean result = true;
		if(mezzoPg.getNumAssegno().isNull()){
			mezzoPg.getNumAssegno().addTypeError(DATO_ASSEGNO_OBBLIGATORIO);
			result = false;
		}
		if(mezzoPg.getImporto().doubleValue() == 0){
			mezzoPg.getImporto().addTypeError(DATO_ASSEGNO_OBBLIGATORIO);
			result = false;
		}
		if(mezzoPg.getBanca().isNull()){
			mezzoPg.getBanca().addTypeError(DATO_ASSEGNO_OBBLIGATORIO);
			result = false;
		}
		if(mezzoPg.getLuogoEmissione().isNull()){
			mezzoPg.getLuogoEmissione().addTypeError(DATO_ASSEGNO_OBBLIGATORIO);
			result = false;
		}
		if(mezzoPg.getDataEmissione().isNull()){
			mezzoPg.getDataEmissione().addTypeError(DATO_ASSEGNO_OBBLIGATORIO);
			result = false;
		}
		if(mezzoPg.getFlagTrasferibile().isNull()){
			mezzoPg.getFlagTrasferibile().addTypeError(DATO_ASSEGNO_OBBLIGATORIO);
			result = false;
		}else{
			if(mezzoPg.getBeneficiariAssegno().isNull() && mezzoPg.getFlagTrasferibile().equals("N")){
				mezzoPg.getBeneficiariAssegno().addTypeError("Attenzione! Il campo è obbligatorio se l'assegno che si intende versare riporta la clausola NT. L'assenza di tale indicazione comporta la violazione dell'art. 49 D.lgs. 231/07 e successive modifiche/integrazioni, nonchè sanzioni amministrativeda parte del MEF verso traente/beneficiario");
				result = false;
			}
		}
		
		if(mezzoPg.getCodTipoPagamento().intValue() == Costanti.MEZZO_PAGAMENTO_ASSEGNO && 
		   mezzoPg.getImporto().doubleValue() >= 1000 && mezzoPg.getFlagTrasferibile().equals("S")){
			mezzoPg.getFlagTrasferibile().addTypeError("Per importi superiori o uguali a 1.000,00 euro l'assegno deve essere NON trasferibile");
			result = false;
		}
		
		if(mezzoPg.getCodTipoPagamento().intValue() == Costanti.MEZZO_PAGAMENTO_ASSEGNO)
			mezzoPg.setCodDivisa(new StringType(Costanti.COD_DIVISA_EURO));

		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean controllaPlicoInDac(ClientSessionContext csc, DacModel dac) throws Exception{
		DocumentoModel doc = dac.getDocumento();
		PlicoModel plico = dac.getPlico(doc,false);
		ListType docInPlico = plico.getDocumenti();
		
		// Verifica dell'esito
		for(int i=0;i<docInPlico.size();i++){
			DocumentoModel d = (DocumentoModel)docInPlico.get(i);
			if(d.getIdDocumento().equals(doc.getIdDocumento()) &&   // Se il documento corrente è in un plico e gli stiamo impostando 
			   doc.isEsitoDocumentoMancante()){						// un esito non compatibile con i plichi: errore
				doc.addCommandError("Attenzione! L'esito specificato non è valido poichè il documento appartiene ad un plico. Spinzare il documento prima di esitarlo");
				return false;
			}
				
			if(d.isEsitoDocumentoMancante()){
				doc.addCommandError("Attenzione! L'esito specificato non è valido poichè il documento appartiene ad un plico. Spinzare il documento prima di esitarlo");
				return false;
			}
		}
		
		// Verifica della cassetta
		if(dac.isCtrlCassette() && dac.isGestoreBarcode()){
			for(int i=0;i<docInPlico.size();i++){
				DocumentoModel d = (DocumentoModel)docInPlico.get(i);
				if(!d.getCodCassetta().isNull() &&
				   !d.getIdDocumento().equals(doc.getIdDocumento()) && 
					d.getCodCassetta().intValue() != doc.getCodCassetta().intValue()){
					doc.getCodCassetta().addTypeError("Tutti i documenti in un plico devono avere la stessa cassetta");
					doc.setErrorsOnDati(true);
					doc.addCommandError("Attenzione! Tutti i documenti in un plico devono avere la stessa cassetta");
					return false;
				}
			}
		}
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean controllaPlicoInPinzatura(ClientSessionContext csc, DacModel dac, DocumentoModel docSelezionato) throws Exception{
		PlicoModel plico = dac.getPlico(docSelezionato,true);
		ListType docInPlico = plico.getDocumenti();

		// Verifica dell'esito
		for(int i=0;i<docInPlico.size();i++){
			DocumentoModel d = (DocumentoModel)docInPlico.get(i);
			if(d.isEsitoDocumentoMancante()){
				dac.addCommandError("Attenzione! Il plico non puo' contenere documenti non pervenuti");
				return false;
			}
		}
		
		// Verifica della cassetta
		if(dac.isCtrlCassette() && dac.isGestoreBarcode()){
			for(int i=0;i<docInPlico.size();i++){
				DocumentoModel d = (DocumentoModel)docInPlico.get(i);
				if(!d.getCodCassetta().isNull() &&
					d.getCodCassetta().intValue() != docSelezionato.getCodCassetta().intValue()){
					dac.addCommandError("Attenzione! Il plico contiene documenti con cassette diverse");
					return false;					
				}
			}
		}
		return true;					
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initHtmlJavascriptCampiObbligatori(ClientSessionContext csc, DacModel dac) throws Exception {
		try{
			
			boolean siamoInSede = true;
			if(dac.getUfficio().equals(Costanti.UFFICIO_RETE) || !dac.isAutoSpuntataParams())
				siamoInSede = false;
			
			StringBuffer html = new StringBuffer("var obblOpeArray = new Array();");
			if(dac.getModality() == Template.READ_MODALITY){
				dac.setHtmlJavascriptCampiObbligatori(html.toString());
				return;
			}
				
			html.append("var el = null;");
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
												"select PT_OPERAZIONE_N_PT_OPERAZIONE," +
												"		isnull(PT_OPERAZIONE_F_CNTRLDATIRICH,'N') dati,"+
											   	"		isnull(PT_OPERAZIONE_F_CNTRLCLIAGE,'N') cli,"+
											   	"		isnull(PT_OPERAZIONE_F_CNTRLMEZZOPG,'N') mezzo,"+
											   	"		isnull(PT_OPERAZIONE_F_CNTRLSPESE,'N') spese "+
											   	"from 	CEPE_PT_RETEP_OPERAZIONE " +
											   	"where  PT_OPERAZIONE_F_VALIDITA = 'S'",
												null, MapCommandDataModel.class);
			ListType elenco = qRes.getResult();
			for(int i=0;i<elenco.size();i++){
				MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(i);
				
				char fobblDati = out.getPropertyValue("dati").toString().charAt(0); 
				char fobblCli = out.getPropertyValue("cli").toString().charAt(0);
				char fobblMezzoPg = out.getPropertyValue("mezzo").toString().charAt(0);
				char fobblSpese = out.getPropertyValue("spese").toString().charAt(0);
				
				html.append("el = new Object();");
				
				// 'E'=Entrambi / 'S'=Sede / 'R'=Rete / 'N' o null = Nessun controllo
				html.append("el.obblDati = false;");
				if(fobblDati == 'E')
					html.append("el.obblDati = true;");
				else if(fobblDati == 'S' && siamoInSede)
					html.append("el.obblDati = true;");
				else if(fobblDati == 'R' && !siamoInSede)
					html.append("el.obblDati = true;");
				
				html.append("el.obblCli = false;");
				if(fobblCli == 'E')
					html.append("el.obblCli = true;");
				else if(fobblCli == 'S' && siamoInSede)
					html.append("el.obblCli = true;");
				else if(fobblCli == 'R' && !siamoInSede)
					html.append("el.obblCli = true;");
		
				html.append("el.obblMezzoPg = false;");
				if(fobblMezzoPg == 'E')
					html.append("el.obblMezzoPg = true;");
				else if(fobblMezzoPg == 'S' && siamoInSede)
					html.append("el.obblMezzoPg = true;");
				else if(fobblMezzoPg == 'R' && !siamoInSede)
					html.append("el.obblMezzoPg = true;");
		
				// 'S'=Obbligatorio / 'N' o null = Non obbligatorio
				html.append("el.obblSpese = false;");
				if(dac.getUfficio().equals(Costanti.UFFICIO_RETE) && fobblSpese == 'S')
					html.append("el.obblSpese = true;");
				
				html.append("obblOpeArray['"+out.getPropertyValue("ptOperazioneNPtOperazione").toString()+"'] = el; ");
			}
			
			dac.setHtmlJavascriptCampiObbligatori(html.toString());
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}
	
	
}
