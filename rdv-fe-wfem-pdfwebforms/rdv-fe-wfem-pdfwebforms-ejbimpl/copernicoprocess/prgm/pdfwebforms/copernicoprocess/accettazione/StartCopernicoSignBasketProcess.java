package prgm.pdfwebforms.copernicoprocess.accettazione;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.signprocess.common.SignUtility;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartCopernicoSignBasketProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel currentPdf = (PdfModel)dataModel;
			
			Basket basket = currentPdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				StartCopernicoSignProcess cmd = new StartCopernicoSignProcess();
				be.setDispoPdf((PdfModel)cmd.execute(userSessionContext, be.getDispoPdf()));
				if(be.getDispoPdf() != currentPdf)
					currentPdf.addCommandErrors(be.getDispoPdf().getCommandErrors());
			}
	    	if(currentPdf.hasCommandErrors()) {
	    		setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
	    		return currentPdf;
	    	}
	    	
    		SignUtility.generaOTP(csc,currentPdf,currentPdf.getPersonaCorrente());
    		if(currentPdf.hasCommandErrors()){
    			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
    			return currentPdf;
    		}
    		
    		SignUtility.getDigitDaChiedere(csc, currentPdf, currentPdf.getPersonaCorrente());
    		if(currentPdf.hasCommandErrors()){
    			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
        		return currentPdf;            			
    		}
	    	
	    	basket.getPersoneGestite().clear();
	    	basket.setPersonaCorrente(currentPdf.getPersonaCorrente());
	    	setNextCommandClass(PdfCopernicoPersonSign.class);
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
