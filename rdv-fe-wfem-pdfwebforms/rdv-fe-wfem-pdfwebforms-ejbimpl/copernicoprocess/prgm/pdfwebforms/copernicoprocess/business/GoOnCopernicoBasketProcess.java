package prgm.pdfwebforms.copernicoprocess.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.copernicoprocess.display.PdfEndCopernicoProcess;
import prgm.pdfwebforms.copernicoprocess.display.PdfNoteFbCopernico;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOnCopernicoBasketProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		try{
			PdfModel currentPdf = (PdfModel)dataModel;
			Basket basket = currentPdf.getBasket();
			currentPdf.getPdfNoteFBCopernico().resetTypeErrors();
	    	if(currentPdf.getPdfNoteFBCopernico().isNull()){
	    		currentPdf.getPdfNoteFBCopernico().addTypeError("Dato obbligatorio");
	    		setNextCommandClass(PdfNoteFbCopernico.class);
				return currentPdf;
	    	}
	
			for(BasketElement be : basket.getBasketElements()) {
				be.getDispoPdf().setPdfNoteFBCopernico(currentPdf.getPdfNoteFBCopernico());				
				GoOnCopernicoProcess cmd = new GoOnCopernicoProcess();
				be.setDispoPdf((PdfModel)cmd.execute(userSessionContext, be.getDispoPdf()));
		    	if((Class)cmd.getNextCommandObject() != PdfEndCopernicoProcess.class) {
		    		if(be.getDispoPdf() != currentPdf) {
			    		currentPdf.addCommandErrors(be.getDispoPdf().getCommandErrors());
			    		currentPdf.addCommandWarnings(be.getDispoPdf().getCommandWarnings());
		    		}
		    		setNextCommandClass((Class)cmd.getNextCommandObject());
		    		return currentPdf;
		    	}
			}

			setNextCommandClass(PdfEndCopernicoProcess.class);
			return currentPdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
