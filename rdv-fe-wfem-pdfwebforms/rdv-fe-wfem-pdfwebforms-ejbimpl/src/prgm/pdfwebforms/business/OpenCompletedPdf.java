package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.stream.PdfInstanceContentAsBinary;

/* **********************************************************************************************
 * Ritorna una pagina con il pdf memorizzato su db
 * **********************************************************************************************/
public class OpenCompletedPdf extends BusinessCommand implements MenuCommand{

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
            if(csc.isCliente())
            	throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);          
            
			PdfDataModel pdfData = (PdfDataModel)dataModel;

			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.setInitialErrorMsg(err);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}

			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(pdfData.getPdfInstanceId());
			setNextCommandClass(PdfInstanceContentAsBinary.class);
			return pdfInstance;

		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}
	
}
