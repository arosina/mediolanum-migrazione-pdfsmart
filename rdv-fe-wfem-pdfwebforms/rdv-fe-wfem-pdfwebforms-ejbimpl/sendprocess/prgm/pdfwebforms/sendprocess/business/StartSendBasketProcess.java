package prgm.pdfwebforms.sendprocess.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.sendprocess.display.PdfEndSendProcess;
import prgm.pdfwebforms.sendprocess.display.PdfErrorsOnSend;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartSendBasketProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel currentPdf = (PdfModel)dataModel;
			
			StringBuilder msgFineOperazione = new StringBuilder();
			Basket basket = currentPdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				StartSendProcess cmd = new StartSendProcess();
				be.setDispoPdf((PdfModel)cmd.execute(userSessionContext, be.getDispoPdf()));
				if((Class)cmd.getNextCommandObject() != PdfEndSendProcess.class) {
					if(be.getDispoPdf() != currentPdf) {
						currentPdf.addCommandErrors(be.getDispoPdf().getCommandErrors());
						currentPdf.addCommandWarnings(be.getDispoPdf().getCommandWarnings());
					}
					setNextCommandClass((Class)cmd.getNextCommandObject());
					return currentPdf;
				}
			}
		
			for(BasketElement be : basket.getBasketElements()) {
				be.setHasFreezeError(false);
				PdfModel pdf = be.getDispoPdf();
				StartSendProcess.doSend(csc, pdf, true, false);
				if(pdf.hasCommandErrors()) {
					be.setHasFreezeError(true);
					String title = pdf.getPdfData().getPdfTitle().isNull() ? pdf.mainPdfAnag().getPdfDescr().toString() : pdf.getPdfData().getPdfTitle().toString();
					msgFineOperazione.append("<br>"+title+": "+pdf.getCommandErrors().get(0));
				}
			}
			
			currentPdf.setMessaggioFineOperazione("Sono state create le righe di prit ed i dati inseriti sono stati inviati in sede.<br>Per perfezionare la pratica ricordati di stampare i moduli, farli firmare al cliente e spedirli in sede.");
			if(msgFineOperazione.length() > 0)
				currentPdf.setMessaggioFineOperazione(currentPdf.getMessaggioFineOperazione()+"<br>Alcune operazioni non sono andate a buon fine, torna nel 5D per riproporle."+msgFineOperazione);

			for(BasketElement be : basket.getBasketElements()) {
				if(!be.hasFreezeError()) {
					setNextCommandClass(PdfEndSendProcess.class);
					return currentPdf;
				}
			}

			setNextCommandClass(PdfErrorsOnSend.class);
			return currentPdf;		
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}	
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
