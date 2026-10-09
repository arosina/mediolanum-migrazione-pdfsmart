package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.model.PdfDataModel;

/* **********************************************************************************************
 * Crea un nuovo pdf collegato a quello in input
 * **********************************************************************************************/
public class ClonaPdfBlank extends ClonaPdf implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		 PdfDataModel pdfData = (PdfDataModel)dataModel;
		 if(pdfData.getPdfs().size() > 0) {
			 PdfDataModel pdfDataEl = (PdfDataModel)pdfData.getPdfs().get(0);
			 pdfData.setCodDispositivaBMED(pdfDataEl.getCodDispositivaBMED());
		 }
		 return super.execute(userSessionContext, pdfData);
	}
	
}
