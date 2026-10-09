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
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.display.PdfAttachments;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfErrorsOnPreview;
import prgm.pdfwebforms.display.PdfMifidResultPage;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.idd.IddCaller;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ConfirmPdfAttachments extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;         

        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
            
            boolean attachError = false;
            for(int i=0;i<pdf.getPdfAttachments().size(); i++){
            	PdfAttachModel pdfAttach = (PdfAttachModel)pdf.getPdfAttachments().get(i);
            	if(pdfAttach.isImplicitAttach())
            		continue;
            	pdfAttach.getFile().resetTypeErrors();
            	if(pdfAttach.getDriverAttachRef().isMandatory() && pdfAttach.getFile().isNull()){
            		pdfAttach.getFile().addTypeError("Allegato obbligatorio");
            		attachError = true;
            	}
            }
            if(attachError){
    			setNextCommandClass(PdfAttachments.class);
    			return pdf;
            }
            
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);

			String[] erroriAdeguatezza = MifidCaller.callOnPreview(csc, pdf);
			if(erroriAdeguatezza != null){
				pdf.setErroriAdeguatezza(erroriAdeguatezza);
				setNextCommandClass(PdfMifidResultPage.class);
				return pdf;
			}
			
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
			
			pdf.alignCommonToAllPdfDataProperties();
			PdfModel nextDispoPdf = Basket.nextDataEntryDispoPdf(userSessionContext, this, pdf);
			if(nextDispoPdf == null) {
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
			}
			return nextDispoPdf;
			
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
