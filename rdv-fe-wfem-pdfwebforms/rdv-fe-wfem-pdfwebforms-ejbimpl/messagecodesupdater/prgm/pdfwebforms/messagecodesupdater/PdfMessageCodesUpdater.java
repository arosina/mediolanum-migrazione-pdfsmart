package prgm.pdfwebforms.messagecodesupdater;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfMessageCodesUpdater extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfMessageCodeModel model = (PdfMessageCodeModel)dataModel;
			DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfMessageCodesUpdater");
			if(model.isPrimaVolta()) {
				dao.fillCodDesc(model);
				model.setPrimaVolta(false);
			}
			
			model.setConfItemProperties(new StringType());
			model.setOrigConfItemProperties(null);
			if(!model.getConfItemName().isNull()) {
				dao.executeTableLoadAccess("pdfMessageCodes", model);
				model.setOrigConfItemProperties(model.getConfItemProperties().toString());
			}
			return model;
		}catch(DAOException daoe) {
			throw new CommandException(daoe.toString());
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfMessageCodeModel.class;
	}

}
