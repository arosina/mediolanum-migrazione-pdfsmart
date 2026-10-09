package prgm.pdfwebforms.copernicoprocess.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class EndCopernicoProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		String html = "<html></html>";
		GenericCommandResponseModel resp = new GenericCommandResponseModel();
		resp.setContentType("text/html");
		resp.setContent(html.getBytes());
		resp.setContentLength(html.length());		
		setGenericCommandResponse(resp);
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return null;
	}

}
