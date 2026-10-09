package prgm.pdfwebforms.reportadeguatezza;

import java.io.UnsupportedEncodingException;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SetReportAdeguatezzaClicked extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			PdfModel pdf = (PdfModel)dataModel;
			
			pdf.setReportAdeguatezzaClicked(true);
			
			String jsonObj = "\""+pdf.isReportAdeguatezzaClicked()+"\"";
			
			GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
			gcrm.setContentType("application/json");
			try{
				gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
			}catch(UnsupportedEncodingException uee){
				gcrm.setContent(jsonObj.toString().getBytes());
			}
			gcrm.setContentLength(jsonObj.length());
			setGenericCommandResponse(gcrm);
			return null;
			
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
