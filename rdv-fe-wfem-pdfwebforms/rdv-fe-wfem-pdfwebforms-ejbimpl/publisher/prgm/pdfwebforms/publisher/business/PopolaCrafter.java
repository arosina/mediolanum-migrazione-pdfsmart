package prgm.pdfwebforms.publisher.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.publisher.crafter.CrafterPdfUtils;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopolaCrafter extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
        try {
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
            model.setCrafterErrorMessage(CrafterPdfUtils.moveToCrafter(csc, model, true));
            setForwardDisplay(new Integer(0));
            return model;
        } catch(Exception e) {
            throw new CommandException(e);
        }
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigurationModel.class;
	}

}
