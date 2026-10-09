package prgm.pdfwebforms.publisher.business;

import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class ClearFilePdf extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
			PdfAnagModel pdf = model.getPdfAnag();
			pdf.getPdfContent().clear();
			setForwardDisplay(new Integer(0));
			return model;
			
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigurationModel.class;
	}

}
