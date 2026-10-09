package prgm.pdfwebforms.business;

import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.DoubleType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ZoomNormal extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			PdfModel model = (PdfModel)dataModel;
			model.setScaleFactor(new DoubleType(PdfModel.DEFAULT_SCALE_FACTOR));
			setForwardDisplay(new Integer(0));
			return model;
			
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
