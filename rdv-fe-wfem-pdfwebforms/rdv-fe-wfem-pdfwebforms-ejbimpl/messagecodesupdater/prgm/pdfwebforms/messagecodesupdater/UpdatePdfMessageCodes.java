package prgm.pdfwebforms.messagecodesupdater;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

/***********************************************************************************************/
/***********************************************************************************************/
public class UpdatePdfMessageCodes extends BusinessCommand {

	private static final String DAO_PDF_MESSAGE_CODES_ACCESS = "pdfMessageCodes";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfMessageCodeModel model = (PdfMessageCodeModel)dataModel;
			if(!model.getConfItemName().isNull()) {
				DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfMessageCodesUpdater");
				dao.executeTableUpdateAccess(DAO_PDF_MESSAGE_CODES_ACCESS, model);
				model.setOrigConfItemProperties(model.getConfItemProperties().toString());
				model.setResultMessage("Messaggi aggiornati");
			}
			setForwardDisplay(Integer.valueOf(0));
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
