package prgm.pdfwebforms.reportadeguatezza;

import java.io.UnsupportedEncodingException;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RefreshStatoReportAdeguatezza extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;
			String msgerr = ReportAdeguatezzaCaller.callRecuperaReportAdeguatezza(csc, pdf);
			String jsonObj = null;
			if(msgerr != null)
				jsonObj = "{ \"stato\": \"ERROR\" , \"msgerr\": \""+msgerr+"\", \"linksReportAdeguatezza\": "+pdf.writeLinksReportAdeguatezza()+" }";
			else
				jsonObj = "{ \"stato\": \""+pdf.getStatoRecueroReportAdeguatezza()+"\" , \"msgerr\": \"\", \"linksReportAdeguatezza\": "+pdf.writeLinksReportAdeguatezza()+" }";
			
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
	@Override
	public Class getInputViewClass() {
		return PdfModel.class;
	}
	
}
