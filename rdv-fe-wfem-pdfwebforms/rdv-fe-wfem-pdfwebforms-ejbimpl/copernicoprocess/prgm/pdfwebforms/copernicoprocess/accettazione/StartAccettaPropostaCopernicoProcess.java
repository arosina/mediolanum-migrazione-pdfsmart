package prgm.pdfwebforms.copernicoprocess.accettazione;

import java.util.ArrayList;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.business.KeyInstanceRetriever;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.PdfBaseDriverUtil;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfOutputData;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;
import prgm.pdfwebforms.mifid.ControlliConcentrazioneFia;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.signprocess.business.StartSignProcess;
import prgm.pdfwebforms.signprocess.common.SignUtility;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartAccettaPropostaCopernicoProcess extends BusinessCommand implements MenuCommand{

	public static String ERRORE_TECNICO_MSG = "Siamo spiacenti, il sistema non è in grado di soddisfare la richiesta. Non è possibile proseguire.";
	private boolean inBasket = false;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PdfDataModel pdfData = (PdfDataModel)dataModel;
		try{

            if(!csc.isCliente()){
    			PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
    			pdf.addCommandError(PdfUtil.FUNZIONE_NON_DISPONIBILE);
    			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
    			return pdf;
            }
            
			pdfData.setProcessoAccettazioneCopernico(true);
			
			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.addCommandError("Nessuna istanza di pdf corrisponde alla chiave in input ["+pdfData.getPdfInstanceId()+"]");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}
			
			ListType pdfDBList = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
										"select top 1 STATO, CLI1_NDG, IN_BASKET from PDF_INSTANCE where PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"'",
										null, MapCommandDataModel.class).getResult();
			if(pdfDBList.size() == 0){
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.addCommandError("Nessuna istanza di pdf corrisponde alla chiave in input ["+pdfData.getPdfInstanceId()+"]");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;	 
			}
			
			MapCommandDataModel pdfDB = (MapCommandDataModel)pdfDBList.get(0);
			StringType ndgCli = (StringType)pdfDB.readProperty("cli1Ndg");
			if(ndgCli == null || !ndgCli.equals(csc.getUserCode())){
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.addCommandError("Il modulo con id ["+pdfData.getPdfInstanceId()+"] non appartiene al cliente");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;	 
			}

			StringType pdfStatus = (StringType)pdfDB.readProperty("stato");
			if(pdfStatus != null && !pdfStatus.equals(PdfInstanceModel.STATO_COMPLETATO_E_INVIATO_AL_CLIENTE)){
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.addCommandError("Lo stato del modulo con id ["+pdfData.getPdfInstanceId()+"] non risulta coerente con la funzione di accettazione copernico");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}

			StringType pdfInBasket = (StringType)pdfDB.readProperty("inBasket");
			if(!isInBasket() && pdfInBasket != null && pdfInBasket.equals("S")) {
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.addCommandError("Il modulo con id ["+pdfData.getPdfInstanceId()+"] risulta sottoscritto in un basket");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfModel pdf = facade.readPdf(csc, pdfData);
			if(pdf.getInitialErrorMsg() != null && pdf.getInitialErrorMsg().length() > 0){
				pdf.addCommandError(pdf.getInitialErrorMsg());
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}
			
			ArrayList<String> ctrlWar = new ArrayList<String>();
			pdf.resetCommandErrors();
			pdf.resetCommandWarnings();
			for(int maxTry=0;maxTry<100;maxTry++){
				DataLoader.loadPersons(csc, pdf);
				pdf.getPdfData().initPdfsFromData();
				VerifyCopernicoPdfOutputData eventOutput = PdfDriverCaller.callVerifyCopernicoPdf(csc, pdf);
				if(eventOutput != null){
					for(String drverr : eventOutput.getErrors())
						pdf.addCommandError(drverr);
					for(String drvwar : eventOutput.getWarnings())
						ctrlWar.add(drvwar);
					if(pdf.hasCommandErrors()){
						setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
						return pdf;
					}
				}
				boolean existOtherPdf = pdf.isMultiPdf() && pdf.getPdfData().getPdfIndex().intValue() < pdf.getPdfData().getPdfs().size()-1;
				if(existOtherPdf){
					pdf = facade.nextPdf(csc, pdf);			
					continue;
				}
				break;
			}
			if(pdf.isMultiPdf())
				pdf = facade.gotoPdf(csc, pdf, 0, false);
			
			if(pdf.isFirstDriverNotFound()){
				pdf.addCommandError("Il modulo prevede dei controlli che per problemi tecnici non sono attivi. Non è possibile proseguire");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}
			
			pdf.resetCommandErrors();
			ProvideMifidDataResponse mifidData = PdfDriverCaller.callProvideMifidData(csc, pdf);
			if(mifidData != null && !mifidData.getFlagAdeguatezza().equals("N")){ // Se integrato con mifid e non è appropriatezza
				boolean someProfiloErrors = false;
				ArrayList<String> putErrorOn = new ArrayList<String>();
				for(int i=0;i<mifidData.getClienti().size();i++){
					PdfPersonModel cliMifid = (PdfPersonModel)mifidData.getClienti().get(i);
					if(cliMifid.getNdg().isNull())
						continue;
					int ndgPos = findNdgPosition(pdf.mainPdfData(), cliMifid.getNdg());
					if(ndgPos == 0)
						continue;
					if(!new PdfBaseDriver().ctrl_profiloMifid(csc, pdf.mainPdfData(), ndgPos, null, putErrorOn, PdfBaseDriverUtil.PROFILO_MIFID_VALIDATO)){
						StringType nomeDB = (StringType)DAOObject.executeDynaQueryAccess(csc, "DBAZ_SOGG", 
													"select trim(cli_m_nom)||' '||trim(cli_m_cogn) from cll.cli where cli_c='"+Tools.fillSx(cliMifid.getNdg().toString(),'0',11)+"' and rownum=1", 
													null, StringType.class).getSingleResult();
						String nomeInMsg = "un cliente";
						if(nomeDB != null && !nomeDB.isNull())
							nomeInMsg = Tools.capitalize(nomeDB.toString());
						String c1 = Tools.fillSx(cliMifid.getNdg().toString(),'0',11);
						String c2 = Tools.fillSx(csc.getUserCode(),'0',11);
						String msgStart = "Il profilo dell'investitore di "+nomeInMsg+" risulta scaduto o non compilato, non è possibile procedere con la richiesta. Puoi aggiornarlo dalla sezione \"Profilo Investitore\" della tua ";
						String msgBody = "";
						if(c1.equals(c2))
							msgBody = "<span onclick='sendToNmol(\"gotoAreaPersonale\");' style='cursor:pointer;text-decoration:underline;'>Area Personale</span>";
						else
							msgBody = "area personale";
						pdf.addCommandError(msgStart+msgBody+".");
						someProfiloErrors = true;
					}
				}
				if(someProfiloErrors){
					setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
					return pdf;
				}
			}
				
			if(!inBasket) {
				String[] erroriAdeguatezza = MifidCaller.callOnAccettazioneCopernico(csc, pdf, mifidData);
				if(erroriAdeguatezza != null){
					if(erroriAdeguatezza[0] != null){
						if(erroriAdeguatezza[0].startsWith(MifidCaller.WARNING_INDICATOR)){
							pdf.addCommandWarning("L'operazione richiede un adeguato livello di conoscenze ed esperienza. Parlane con il tuo Family Banker affinchè "+
												  "ti possa fornire le informazioni utili per permetterti di agire nel tuo migliore interesse.");
						}else if(erroriAdeguatezza[0].startsWith(ControlliConcentrazioneFia.ERRORE_CONTROLLI_CONCENTRAZIONE_FIA_INDICATOR)){
							pdf.addCommandError("L'operazione che desideri effettuare non risulta adeguata in base ai limiti di concentrazione "+
												"dei Fondi Alternativi previsti per il tuo portafoglio. "+
												"Per ulteriori informazioni ti invitiamo a contattare il tuo Family Banker.");
						}else {
							pdf.addCommandError("In base alle regole di valutazione di adeguatezza degli investimenti adottate da Banca Mediolanum "+
												"come richiesto dalla normativa di settore vigente, l'operazione che intendi eseguire risulta non adeguata "+
												"rispetto al tuo Profilo di Investitore ed alla attuale composizione del tuo portafoglio prodotti. "+
												"Per maggiori informazioni rivolgiti al tuo Family Banker.");
						}
					}
					if(erroriAdeguatezza[1] != null){
						pdf.addCommandError(erroriAdeguatezza[1]);
					}
				}
			}
				
			if(pdf.hasCommandErrors()){
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}
				
			pdf.getPdfData().setSignAll(new BooleanType(true));
			if(pdf.isMultiPdf()){
				for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
					PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
					pdfElement.setSignAll(new BooleanType(true));
				}
			}

        	// Inizializzo i clienti che dovranno firmare
			ArrayList<PdfPersonModel> fullSignProcessPersonsSign = StartSignProcess.createFullSignProcessPersons(pdf);
    		if(fullSignProcessPersonsSign.size() == 0){
				pdf.addCommandError("Nessun soggetto previsto in firma");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
    		}
    		pdf.setFullProcessPersons(fullSignProcessPersonsSign);
    		
        	SignUtility.loadDatiFirmaDigitalePersone(csc,pdf);
        	if(pdf.hasCommandErrors()){
        		setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
        		return pdf;
        	}
			
        	for(String war : ctrlWar)
        		pdf.addCommandWarning(war);
        	
			pdf.saveAndSetDataSottoscrizione();
			
			if(!pdf.hasCommandWarnings())
				setNextCommandClass(StartCopernicoSignProcess.class);
			else
				setNextCommandClass(PdfCopernicoAccettazioneWarnings.class);
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			PdfModel pdf = new PdfModel();
			pdf.setPdfData(pdfData);
			pdf.addCommandError(StartAccettaPropostaCopernicoProcess.ERRORE_TECNICO_MSG);
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return pdf;
		}catch(Exception e){
			LOG.error(e);
			PdfModel pdf = new PdfModel();
			pdf.setPdfData(pdfData);
			pdf.addCommandError(StartAccettaPropostaCopernicoProcess.ERRORE_TECNICO_MSG);
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return pdf;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private int findNdgPosition(PdfDataModel pdfData, StringType ndgInput){
		for(int i=1;i<=DataLoader.MAX_NUM_CLIENTI;i++){
			AbstractType ndg = pdfData.read("ndgCliente"+i);
			if(ndg != null && !ndg.isNull()){
				if(Tools.fillSx(ndg.toString(), '0', 11).equals(Tools.fillSx(ndgInput.toString(),'0',11)))
					return i;
			}
		}
		return 0;
	}

	public boolean isInBasket() {
		return inBasket;
	}

	public void setInBasket(boolean inBasket) {
		this.inBasket = inBasket;
	}

}
