package prgm.pdfwebforms.publisher.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.display.PdfPriipsPage;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class VerificaPdf extends BusinessCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfModel pdf = facade.newTestPdf(csc, pdfData, true);
			if(pdf.getInitialErrorMsg() != null){
				pdf.setPdfData(pdfData);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			pdf.setProfiloUtente(new StringType(pdfData.getProfiloUtente().toString()));
			pdfData.setProfiloUtente(new StringType());
			if(pdf.mainPdfAnag().getHasPriips().booleanValue()){
				setNextCommandClass(PdfPriipsPage.class);
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
		return PdfDataModel.class;
	}

}
