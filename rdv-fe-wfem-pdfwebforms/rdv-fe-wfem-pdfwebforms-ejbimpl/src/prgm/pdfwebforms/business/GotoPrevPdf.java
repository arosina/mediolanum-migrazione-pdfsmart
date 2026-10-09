package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.display.PdfAttachments;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class GotoPrevPdf extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
  			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
            PdfModel pdf = (PdfModel)dataModel;

            if(!Basket.verifyMvcConsistency(this, pdf))
            	return pdf;
            
            pdf.alignCommonToAllPdfDataProperties();

            if(!Basket.isFirstDispoPdf(pdf) && pdf.getPdfData().getPdfIndex().intValue() == 0){
    			Tools.resetTypesErrors(pdf.getPdfData());
     			pdf = facade.savePdf(csc, pdf);
    			if(pdf.isConcurrencyViolationFounded()) {
    				setNextCommandClass(PdfConcurrencyViolation.class);
    				return pdf;
    			}
            	pdf = Basket.prevDataEntryDispoPdf(pdf);
            	pdf = facade.gotoPdf(csc, pdf, pdf.getPdfData().getPdfs().size()-1, pdf.isMultiPdf() ? false : true);
    			pdf.setScrollXValue(new IntegerType(0));
    			pdf.setScrollYValue(new IntegerType(0));
    			boolean isLastPdf = !pdf.isMultiPdf() ? true : pdf.getPdfData().getPdfIndex().intValue()==pdf.getPdfData().getPdfs().size()-1;
    			if(pdf.getPdfAttachments() != null && pdf.getPdfAttachments().size() > 0 && !pdf.allImplicitAttach() && isLastPdf){
    				setNextCommandClass(PdfAttachments.class);
    				return pdf;
    			}
    			setNextCommandClass(PdfPage.class);
            	return pdf;
            }
            
            pdf.clearEndProcessFields();
          
            boolean callOnLoadPdfOnDriver = pdf.getPdfData().getPdfIndex().intValue() == 1 ? true : false;
            
			Tools.resetTypesErrors(pdf.getPdfData());
 			pdf = facade.savePdf(csc, pdf);
			if(pdf.isConcurrencyViolationFounded()) {
				setNextCommandClass(PdfConcurrencyViolation.class);
				return pdf;
			}
			pdf = facade.gotoPdf(csc, pdf, pdf.getPdfData().getPdfIndex().intValue()-1, callOnLoadPdfOnDriver);

			pdf.setScrollXValue(new IntegerType(0));
			pdf.setScrollYValue(new IntegerType(0));
			
			boolean isLastPdf = !pdf.isMultiPdf() ? true : pdf.getPdfData().getPdfIndex().intValue()==pdf.getPdfData().getPdfs().size()-1;
			if(pdf.getPdfAttachments() != null && pdf.getPdfAttachments().size() > 0 && !pdf.allImplicitAttach() && isLastPdf){
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
