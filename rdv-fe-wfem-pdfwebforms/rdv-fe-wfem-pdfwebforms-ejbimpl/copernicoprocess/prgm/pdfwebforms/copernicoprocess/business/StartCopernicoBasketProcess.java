package prgm.pdfwebforms.copernicoprocess.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.copernicoprocess.display.PdfNoteFbCopernico;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartCopernicoBasketProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel currentPdf = (PdfModel)dataModel;
			
			Basket basket = currentPdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				StartCopernicoProcess cmd = new StartCopernicoProcess();
				be.setDispoPdf((PdfModel)cmd.execute(userSessionContext, be.getDispoPdf()));
		    	if((Class)cmd.getNextCommandObject() != PdfNoteFbCopernico.class) {
		    		if(be.getDispoPdf() != currentPdf) {
			    		currentPdf.addCommandErrors(be.getDispoPdf().getCommandErrors());
			    		currentPdf.addCommandWarnings(be.getDispoPdf().getCommandWarnings());
		    		}
		    		setNextCommandClass((Class)cmd.getNextCommandObject());
		    		return currentPdf;
		    	}
			}
			
			currentPdf.setPdfNoteFBCopernico(new StringType());
    		String noteFbBasket = Basket.noteCopernico(csc, currentPdf);
    		if(noteFbBasket != null) {
    			currentPdf.setPdfNoteFBCopernico(new StringType(noteFbBasket));
    			currentPdf.getPdfNoteFBCopernico().setEditable(false);
    		}
    		
    		setNextCommandClass(PdfNoteFbCopernico.class);
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
