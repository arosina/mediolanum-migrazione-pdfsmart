package prgm.pdfwebforms.catalog;

import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;

/***********************************************************************************************/
/***********************************************************************************************/
public class OpenCompilationExample extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfCatalog");

			PdfAnagModel pdf = new PdfAnagModel((PdfAnagKeyModel)dataModel);
			DAOTableResultModel tRes = dao.executeTableLoadAccess("pdfCompilationExample",pdf);
			
			if(tRes.getResult().intValue() != 1){
				String message = "Esempio di compilazione non trovato";
				GenericCommandResponseModel resp = new GenericCommandResponseModel();
				resp.setContentType("text/html");
				resp.setContent(message.getBytes());
				resp.setContentLength(message.length());
				setGenericCommandResponse(resp);
				return null;
			}
			
			
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			String contentType = pdf.getPdfCompilationExample().getContentType();
			String fileName = pdf.getPdfCompilationExample().getFileName();
			byte[] content = pdf.getPdfCompilationExample().getFileContent();
			resp.setSuggestedFileName(fileName);
			resp.setContentType(contentType);
			resp.setContent(content);
			resp.setContentLength(content.length);
			setGenericCommandResponse(resp);
			pdf = null;
			return null;
			
		}catch(DAOException daoe){
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfAnagKeyModel.class;
	}

}
