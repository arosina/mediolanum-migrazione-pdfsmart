package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.sostituzioni.SostituzioniCaller;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOnPriips extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
	        PdfModel pdf = (PdfModel)dataModel;
	        
	        PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
	        pdf = facade.savePdf(csc, pdf);
			if(pdf.isConcurrencyViolationFounded()) {
				setNextCommandClass(PdfConcurrencyViolation.class);
				return pdf;
			}
	        
	        if(pdf.mainPdfAnag().getHasPreferenzeInPriips().booleanValue())
	        	SostituzioniCaller.savePreferenzeCliente(csc, pdf);
	        
			setNextCommandClass(PdfPage.class);
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
