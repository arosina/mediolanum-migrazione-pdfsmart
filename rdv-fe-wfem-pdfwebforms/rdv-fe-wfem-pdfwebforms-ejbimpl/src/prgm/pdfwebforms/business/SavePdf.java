package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SavePdf extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;
			
            if(!Basket.verifyMvcConsistency(this, pdf))
            	return pdf;

            DataLoader.loadPersons(csc, pdf);

			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			Tools.resetTypesErrors(pdf.getPdfData());
			pdf = facade.savePdf(csc, pdf);
			if(pdf.isConcurrencyViolationFounded()) {
				setNextCommandClass(PdfConcurrencyViolation.class);
				return pdf;
			}
			if(pdf.hasCommandErrors()){
				setNextCommandClass(PdfPage.class);
				return pdf;
			}
			
			setForwardDisplay(new Integer(0));
			return pdf;
			
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
