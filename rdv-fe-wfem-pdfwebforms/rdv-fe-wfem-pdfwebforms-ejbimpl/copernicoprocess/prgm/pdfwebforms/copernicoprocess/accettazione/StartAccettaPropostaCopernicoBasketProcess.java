package prgm.pdfwebforms.copernicoprocess.accettazione;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.mifid.ControlliConcentrazioneFia;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartAccettaPropostaCopernicoBasketProcess extends BusinessCommand implements MenuCommand{

	public static String ERRORE_TECNICO_MSG = "Siamo spiacenti, il sistema non è in grado di soddisfare la richiesta. Non è possibile proseguire.";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		ClientSessionContext csc = userSessionContext.getClientSessionContext();
        PdfDataModel model = (PdfDataModel)dataModel;
		try{

			model.setProcessoAccettazioneCopernico(true);
            ListType elencoDispo = new DAOObject(csc,"PdfWebForms.PdfCopernicoProcess").executeQueryAccess("loadIdDispoBasketDaAccettare",model).getResult();
			if(elencoDispo.size() == 0) {
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(model);
				pdf.addCommandError("Il basket ["+model.getIdCarrello()+"] non esiste o non contiene dispositive");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}

			Class koPdfNextCommandClass = null;
			int koPdfIndex = -1;
			Basket basket = new Basket(model.getIdCarrello().toString());
			for(int i=0;i<elencoDispo.size();i++) {
				BasketElement basketElement = new BasketElement();
				PdfDataModel dispoModel = (PdfDataModel)elencoDispo.get(i);
				dispoModel.setSistemaClient(new StringType(model.getSistemaClient().toString()));
				// Mando in esecuzione lo "start" per ogni pdf del basket
				StartAccettaPropostaCopernicoProcess pdfcmd = new StartAccettaPropostaCopernicoProcess();
				pdfcmd.setInBasket(true);
				PdfModel pdf = (PdfModel)pdfcmd.execute(userSessionContext, dispoModel);
				if(koPdfIndex == -1 && 
					(Class)pdfcmd.getNextCommandObject() != PdfCopernicoAccettazioneWarnings.class && 
					(Class)pdfcmd.getNextCommandObject() != StartCopernicoSignProcess.class) {
					koPdfNextCommandClass = (Class)pdfcmd.getNextCommandObject();
					koPdfIndex = i;
				}
				basketElement.setDispoPdf(pdf);
				pdf.setBasket(basket);
				basket.getBasketElements().add(basketElement);
			}
			if(koPdfIndex >= 0) {
				setNextCommandClass(koPdfNextCommandClass);
				return basket.getBasketElements().get(koPdfIndex).getDispoPdf();
			}
			
			PdfModel firstPdf = basket.getBasketElements().get(0).getDispoPdf();
			String[] erroriAdeguatezza = MifidCaller.callOnAccettazioneBasketCopernico(csc, firstPdf);
			if(erroriAdeguatezza != null){
				if(erroriAdeguatezza[0] != null){
					if(erroriAdeguatezza[0].startsWith(MifidCaller.WARNING_INDICATOR)){
						firstPdf.addCommandWarning("L'operazione richiede un adeguato livello di conoscenze ed esperienza. Parlane con il tuo Family Banker affinchè "+
											  	   "ti possa fornire le informazioni utili per permetterti di agire nel tuo migliore interesse.");
					}else if(erroriAdeguatezza[0].startsWith(ControlliConcentrazioneFia.ERRORE_CONTROLLI_CONCENTRAZIONE_FIA_INDICATOR)){
						firstPdf.addCommandError("L'operazione che desideri effettuare non risulta adeguata in base ai limiti di concentrazione "+
												 "dei Fondi Alternativi previsti per il tuo portafoglio. "+
												 "Per ulteriori informazioni ti invitiamo a contattare il tuo Family Banker.");
					}else{
						firstPdf.addCommandError("In base alle regole di valutazione di adeguatezza degli investimenti adottate da Banca Mediolanum "+
												 "come richiesto dalla normativa di settore vigente, l'operazione che intendi eseguire risulta non adeguata "+
												 "rispetto al tuo Profilo di Investitore ed alla attuale composizione del tuo portafoglio prodotti. "+
												 "Per maggiori informazioni rivolgiti al tuo Family Banker.");
					}
				}
				if(erroriAdeguatezza[1] != null){
					firstPdf.addCommandError(erroriAdeguatezza[1]);
				}
				if(erroriAdeguatezza[3] != null){
					if(erroriAdeguatezza[3].startsWith(MifidCaller.WARNING_INDICATOR)){
						firstPdf.addCommandWarning(erroriAdeguatezza[3].substring(MifidCaller.WARNING_INDICATOR.length()));
					}else{
						firstPdf.addCommandError(erroriAdeguatezza[3]);
					}
				}
			}	
		
			if(firstPdf.hasCommandErrors()){
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return firstPdf;
			}

			if(!firstPdf.hasCommandWarnings())
				setNextCommandClass(StartCopernicoSignBasketProcess.class);
			else
				setNextCommandClass(PdfCopernicoAccettazioneWarnings.class);
			return firstPdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			PdfModel pdf = new PdfModel();
			pdf.setPdfData(model);
			pdf.addCommandError(StartAccettaPropostaCopernicoBasketProcess.ERRORE_TECNICO_MSG);
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return pdf;
		}catch(Exception e){
			LOG.error(e);
			PdfModel pdf = new PdfModel();
			pdf.setPdfData(model);
			pdf.addCommandError(StartAccettaPropostaCopernicoBasketProcess.ERRORE_TECNICO_MSG);
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return pdf;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}
	
}
