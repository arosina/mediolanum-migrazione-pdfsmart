package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.aml.backend.AmlFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.display.PdfAttachments;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class GotoLastPdf extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;
            
   			((AmlFacade)FacadeLoader.getFacade(csc, AmlFacade.class)).removeAllAmlHiddenModulesFromBasket(csc, pdf);

   			pdf = Basket.lastDataEntryDispoPdf(pdf);
            
            pdf.clearEndProcessFields();

   			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			pdf = facade.gotoPdf(csc, pdf, pdf.getPdfData().getPdfs().size()-1, pdf.isMultiPdf() ? false : true);

			pdf.setScrollXValue(new IntegerType(0));
			pdf.setScrollYValue(new IntegerType(0));
			
			if(pdf.getPdfAttachments() != null && pdf.getPdfAttachments().size() > 0 && !pdf.allImplicitAttach()){
				setNextCommandClass(PdfAttachments.class);
				return pdf;
			}
			
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
