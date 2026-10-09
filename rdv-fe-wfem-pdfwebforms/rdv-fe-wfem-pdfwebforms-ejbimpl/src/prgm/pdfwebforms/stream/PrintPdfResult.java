package prgm.pdfwebforms.stream;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class PrintPdfResult extends PdfGenerator {

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		this.caller = "PRINTPDFRESULT";
		return innerExecute(userSessionContext, dataModel);
	}
}
