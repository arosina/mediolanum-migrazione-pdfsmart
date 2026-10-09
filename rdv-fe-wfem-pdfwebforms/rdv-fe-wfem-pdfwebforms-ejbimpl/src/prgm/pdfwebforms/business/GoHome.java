package prgm.pdfwebforms.business;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoHome extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;
            pdf.clearEndProcessFields();
            
   			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			pdf = facade.gotoPdf(csc, pdf, 0, true);
            
            pdf.getFullProcessPersons().clear();
            pdf.setPdfCompilationMode(new StringType());

			pdf.setScrollXValue(new IntegerType(0));
			pdf.setScrollYValue(new IntegerType(0));
			
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
