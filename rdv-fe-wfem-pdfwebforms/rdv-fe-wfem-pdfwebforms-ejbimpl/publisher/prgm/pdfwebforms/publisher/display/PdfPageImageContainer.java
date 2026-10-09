package prgm.pdfwebforms.publisher.display;

import prgm.pdfwebforms.publisher.model.PdfPageAnagModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPageImageContainer extends DisplayCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		PdfPageAnagModel model = (PdfPageAnagModel)dataModel;
		return model;			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public Class getInputViewClass() {
		return PdfPageAnagModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public boolean isNoSubmitCommand() {
		return true;
	}

}
