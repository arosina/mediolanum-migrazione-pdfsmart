package prgm.pdfwebforms.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.model.PdfConfigModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfConfig extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			if(!csc.isSede()){
				throw new CommandException("Comando non disponibile per l'utente collegato");
            }			
			
			PdfConfigModel model = (PdfConfigModel)dataModel;
			
			DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfWebForms");
			dao.executeTableLoadAccess("pdfConfig", model.getPraticheDigitaliDisabilitaCallSrvDispositiva());
			dao.executeTableLoadAccess("pdfConfig", model.getPraticheDigitaliDisabilitaSemaforoMom());
			
			model.setConfigLoaded(true);
			return model;
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigModel.class;
	}

}
