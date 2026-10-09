package prgm.pdfwebforms.aml;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ConfirmCora extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
	        PdfModel pdf = (PdfModel)dataModel;

            if(!Basket.verifyMvcConsistency(this, pdf))
            	return pdf;

            setNextCommandClass(PdfPreviewContainer.class);
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
