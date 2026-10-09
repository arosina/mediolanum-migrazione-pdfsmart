package prgm.pdfwebforms.stream;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.model.PdfModel;

/*******************************************************************/
/*******************************************************************/
public class PdfTestContentAsBinary extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;
			
			byte[] result = pdf.getPdfTestContent();
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			resp.setContentType("application/pdf");
			resp.setContentLength(result.length);
			resp.setContent(result);
			String title = pdf.getPdfAnag().getPdfCode()+" "+pdf.getPdfAnag().getPdfDescr();
			String fileName = title.replaceAll("[\\\\/:*?\"<>|]","-");
			if(!fileName.toUpperCase().endsWith(".PDF"))
				fileName += ".pdf";
			resp.setSuggestedFileName(fileName);
			setGenericCommandResponse(resp);
			pdf.setPdfTestContent(null);
			return null;
			
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
