package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/* **********************************************************************************************
 * Produce il pdf con i dati di validazione MOM (se esistenti)
 * **********************************************************************************************/
public class VisualizzaPdf extends ValidaPdf implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		PdfDataModel pdfData = (PdfDataModel)dataModel;
		PdfModel pdf = (PdfModel)super.execute(userSessionContext, pdfData);
		pdf.getPdfData().setReadonly(new BooleanType(true));
		return pdf;
	}

}
