package prgm.pdfwebforms.publisher.crafter.util;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopolaCrafterUtil extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
        try {
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PopolaCrafterUtilModel model = (PopolaCrafterUtilModel)dataModel;
            return model;
        } catch(Exception e) {
            throw new CommandException(e);
        }
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopolaCrafterUtilModel.class;
	}

}
