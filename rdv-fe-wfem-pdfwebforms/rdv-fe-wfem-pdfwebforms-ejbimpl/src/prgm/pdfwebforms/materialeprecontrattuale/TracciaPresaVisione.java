package prgm.pdfwebforms.materialeprecontrattuale;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class TracciaPresaVisione extends BusinessCommand {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
            TracciaPresaVisioneModel model = (TracciaPresaVisioneModel)dataModel;

			String respCont = "NN";
            if(!model.getNdg().isNull()){
            	model.setNdg(new StringType(Tools.fillSx(model.getNdg().toString(),'0',11)));
	    		try{
	    			new DAOObject(csc, "PdfWebForms.PdfWebForms").executeOSBAccess("tracciaPresaVisioneMaterialePrecontrattuale", model);
	    			respCont = model.getEsitoCode().toString();
	    		}catch(DAOException daoe){
	    			respCont = "EX";
	    		}
            }
            
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			resp.setContentType("text/html");
			resp.setContentLength(respCont.length());
			resp.setContent(respCont.getBytes());
			setGenericCommandResponse(resp);
			return null;            
			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return TracciaPresaVisioneModel.class;
	}

}
