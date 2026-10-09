package prgm.pdfwebforms.signprocess.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;

import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.model.PdfModel;

/*******************************************************************/
/*******************************************************************/
public class PdfErrorsOnSign extends DisplayCommand{

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		try{
			PdfModel model = (PdfModel)dataModel;
			model.setWayoutEnabled(true);
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            BooleanType wayoutNonAttivo = PdfConfig.getParamAsBool(csc, "SIGN_PROCESS", "DISABILITA_WAYOUT_CARTACEO");
            if(wayoutNonAttivo != null && wayoutNonAttivo.booleanValue())
            	model.setWayoutEnabled(false);
            
		}catch(Throwable t){}
		
		return dataModel;
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class<CommandDataModel> getInputViewClass() {
		return CommandDataModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isStepCommand() {
		return true;
	}
}
