package prgm.pdfwebforms.copernicoprocess.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.copernicoprocess.backend.PdfCopernicoProcessFacade;
import prgm.pdfwebforms.copernicoprocess.common.CopernicoUtility;
import prgm.pdfwebforms.copernicoprocess.display.PdfEndCopernicoProcess;
import prgm.pdfwebforms.copernicoprocess.display.PdfEndVolatileCopernicoProcess;
import prgm.pdfwebforms.copernicoprocess.display.PdfErrorsOnCopernico;
import prgm.pdfwebforms.copernicoprocess.display.PdfNoteFbCopernico;
import prgm.pdfwebforms.core.PdfBarcodeUtils;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOnCopernicoProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;			
			pdf.resetCommandErrors();
        	
        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
        	pdf.getPdfNoteFBCopernico().resetTypeErrors();
        	if(pdf.getPdfNoteFBCopernico().isNull()){
        		pdf.getPdfNoteFBCopernico().addTypeError("Dato obbligatorio");
        		setNextCommandClass(PdfNoteFbCopernico.class);
    			return pdf;
        	}
       	
			if(!PdfBarcodeUtils.initBarcodeFirmaDigitale(csc, pdf)){
				setNextCommandClass(PdfErrorsOnCopernico.class);
				return pdf;
			}
			
    		pdf.setPdfCompilationMode(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO));

			// Chiamo il completamento sul driver del primo pdf
			String errorMsg = PdfDriverCaller.callPdfCompleted(csc, pdf);
			if(errorMsg != null && errorMsg.length() > 0){
    			pdf.addCommandError(errorMsg);
    			setNextCommandClass(PdfErrorsOnCopernico.class);
    			return pdf;
			}
			
			byte[] pdfContent = CopernicoUtility.firmaPdf(csc, pdf);
			if(pdf.hasCommandErrors() || pdfContent == null){
				setNextCommandClass(PdfErrorsOnCopernico.class);
				return pdf;
			}

			PdfCopernicoProcessFacade facade = (PdfCopernicoProcessFacade)FacadeLoader.getFacade(csc, PdfCopernicoProcessFacade.class);
			pdf = facade.sendToCliPdfInstance(csc, pdf, pdfContent);
			
			if(pdf.hasCommandErrors()){
    			setNextCommandClass(PdfErrorsOnCopernico.class);
    			return pdf;
			}
    		
			if(pdf.getCallbackData() != null){
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingContent", new ByteArrayType(pdfContent));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingCompilationMode", new StringType(pdf.getPdfCompilationMode().toString()));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingBarcodes", new StringType(pdf.getPdfGeneratedBarcodes().toString()));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingFlagWayout", new BooleanType(pdf.getFlagWayout().booleanValue()));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingNoteFBCopernico", new StringType(pdf.getPdfNoteFBCopernico().toString()));
			}
			
			if(pdf.getPdfData().getIsVolatile().booleanValue()){
				setNextCommandClass(PdfEndVolatileCopernicoProcess.class);
			}else{
				setNextCommandClass(PdfEndCopernicoProcess.class);
			}
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
