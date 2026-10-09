package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DownloadCommandResponseModel;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.stream.PrintPdfResult;

/* **********************************************************************************************
 * Stesso comortamento del comando "PrintPdf" ma con response di tipo download
 * **********************************************************************************************/
public class PrintPdfAsDownload extends PrintPdf{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		PdfModel pdf = (PdfModel)super.execute(userSessionContext, dataModel);
		
		PrintPdfResult printPdfResult = new PrintPdfResult();
		printPdfResult.execute(userSessionContext, pdf);
		GenericCommandResponseModel originalResp = (GenericCommandResponseModel)printPdfResult.getNextCommandObject();

		DownloadCommandResponseModel resp = new DownloadCommandResponseModel();
		resp.setContentType("application/pdf");
		resp.setContentLength(originalResp.getContentLength());
		resp.setContent(originalResp.getContent());
		resp.setFileName(originalResp.getSuggestedFileName());
		setGenericCommandResponse(resp);
		return null;
	}
}
