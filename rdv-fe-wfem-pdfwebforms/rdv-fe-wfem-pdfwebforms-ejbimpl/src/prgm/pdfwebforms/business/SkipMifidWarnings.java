package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.aml.AmlErrorPage;
import prgm.pdfwebforms.aml.AmlPage;
import prgm.pdfwebforms.aml.CoraPage;
import prgm.pdfwebforms.aml.backend.AmlFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.display.PdfErrorsOnPreview;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.idd.IddCaller;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SkipMifidWarnings extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;
            
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			
			String errorMsg = PdfDriverCaller.callBeforePreview(csc, pdf);
			if(errorMsg != null && errorMsg.length() > 0){
    			pdf.addCommandError(errorMsg);
    			setNextCommandClass(PdfErrorsOnPreview.class);
    			return pdf;
			}

			if(pdf.isMultiPdf())
				pdf = facade.gotoPdf(csc, pdf, 0, false);

			errorMsg = IddCaller.callGeneraRaccomandazioneIdd(csc, pdf);
			if(errorMsg != null && errorMsg.length() > 0){
    			pdf.addCommandError(errorMsg);
    			setNextCommandClass(PdfErrorsOnPreview.class);
    			return pdf;
			}

			((AmlFacade)FacadeLoader.getFacade(csc, AmlFacade.class)).createAmlAndCoraModel(csc, pdf);
			if(pdf.getAmlModel() != null) {
				if(pdf.getAmlModel().getErrorMsg() != null)
					setNextCommandClass(AmlErrorPage.class);
				else
					setNextCommandClass(AmlPage.class);
			}else if(pdf.getCoraModel() != null)
				setNextCommandClass(CoraPage.class);
			else
				setNextCommandClass(PdfPreviewContainer.class);
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
