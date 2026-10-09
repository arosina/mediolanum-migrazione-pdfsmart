package prgm.pdfwebforms.signprocess.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.signprocess.common.SignUtility;
import prgm.pdfwebforms.signprocess.display.PdfErrorsOnSign;
import prgm.pdfwebforms.signprocess.display.PdfPersonSign;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartSignBasketProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel currentPdf = (PdfModel)dataModel;
			  
			Basket basket = currentPdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				StartSignProcess cmd = new StartSignProcess();
				be.setDispoPdf((PdfModel)cmd.execute(userSessionContext, be.getDispoPdf()));
				if((Class)cmd.getNextCommandObject() != PdfPersonSign.class) {
					if(be.getDispoPdf() != currentPdf) {
						currentPdf.addCommandErrors(be.getDispoPdf().getCommandErrors());
						currentPdf.addCommandWarnings(be.getDispoPdf().getCommandWarnings());
					}
					setNextCommandClass((Class)cmd.getNextCommandObject());
					return currentPdf;
				}
			}
	    	
    		SignUtility.getDigitDaChiedere(csc, currentPdf, currentPdf.getPersonaCorrente());
    		if(currentPdf.hasCommandErrors()){
    			setNextCommandClass(PdfErrorsOnSign.class);
        		return currentPdf;            			
    		}

	    	basket.getPersoneGestite().clear();
	    	basket.setPersonaCorrente(currentPdf.getPersonaCorrente());
	    	setNextCommandClass(PdfPersonSign.class);
			return currentPdf;		
			
		}catch(Exception e) {
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
