package prgm.pdfwebforms.stream;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class PdfPrintPreview extends PdfGenerator {

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		this.caller = "PRINTPREVIEW";
		return innerExecute(userSessionContext, dataModel);
	}
}
