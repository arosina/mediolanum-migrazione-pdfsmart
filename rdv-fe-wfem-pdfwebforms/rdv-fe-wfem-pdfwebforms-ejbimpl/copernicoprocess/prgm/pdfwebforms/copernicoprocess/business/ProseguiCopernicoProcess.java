package prgm.pdfwebforms.copernicoprocess.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.copernicoprocess.display.PdfErrorsOnCopernico;
import prgm.pdfwebforms.copernicoprocess.display.PdfNoteFbCopernico;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.reportadeguatezza.Costanti;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCaller;

/***********************************************************************************************/
/***********************************************************************************************/
public class ProseguiCopernicoProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;
        	pdf.resetCommandErrors();
        	pdf.resetCommandWarnings();
        	
        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
    		// Richiamo la generazione del report adeguatezza, se non già fatto o passato in input
			String erroriReportAdeguatezza = ReportAdeguatezzaCaller.callGeneraReportAdeguatezzaOnCopernico(csc, pdf);
			if(erroriReportAdeguatezza != null){
				pdf.addCommandError(erroriReportAdeguatezza);
				setNextCommandClass(PdfErrorsOnCopernico.class);
				return pdf;
			}      	
    		
			// Richiamo il primo tentativo di recupero del report adeguatezza
			erroriReportAdeguatezza = ReportAdeguatezzaCaller.callRecuperaReportAdeguatezza(csc, pdf);
			if(erroriReportAdeguatezza != null){
				pdf.addCommandError(Costanti.MESSAGGIO_ERRORE);
				setNextCommandClass(PdfErrorsOnCopernico.class);
				return pdf;
			}
        	
        	setNextCommandClass(PdfNoteFbCopernico.class);
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
