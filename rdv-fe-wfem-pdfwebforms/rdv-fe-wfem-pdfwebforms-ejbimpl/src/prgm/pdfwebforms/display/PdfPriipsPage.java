package prgm.pdfwebforms.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;

import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.sostituzioni.SostituzioniCaller;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPriipsPage extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;
			SostituzioniCaller.loadPreferenze(csc, pdf);
			if(pdf.mainPdfAnag().getTipoPriips().equals(PdfAnagModel.TIPO_PRIIPS_PREVIDENZA))
				new DAOObject(csc, "PdfWebForms.PdfInstance").fillCodDesc(pdf.getPdfData());
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
