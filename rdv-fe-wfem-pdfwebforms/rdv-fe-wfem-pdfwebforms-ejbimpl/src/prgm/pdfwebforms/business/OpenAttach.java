package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;

import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class OpenAttach extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;         

            IntegerType attachIdx = pdf.getAttachIdx();
            if(attachIdx.isNull()){
				String message = "Id attach non valorizzato";
				GenericCommandResponseModel resp = new GenericCommandResponseModel();
				resp.setContentType("text/html");
				resp.setContent(message.getBytes());
				resp.setContentLength(message.length());
				setGenericCommandResponse(resp);
				return null;
            }
            
        	PdfAttachModel pdfAttach = (PdfAttachModel)pdf.getPdfAttachments().get(attachIdx.intValue());
        	byte[] fileContent = pdfAttach.getFile().getFileContent();

			GenericCommandResponseModel resp = new GenericCommandResponseModel();
        	resp.setContentType("application/pdf");
			resp.setContent(fileContent);
			resp.setContentLength(fileContent.length);
			resp.setSuggestedFileName(pdfAttach.getFile().getFileName());
			setGenericCommandResponse(resp);
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
