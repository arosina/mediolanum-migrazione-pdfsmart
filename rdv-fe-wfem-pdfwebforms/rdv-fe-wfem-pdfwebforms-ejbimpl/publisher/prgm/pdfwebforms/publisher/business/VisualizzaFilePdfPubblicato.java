package prgm.pdfwebforms.publisher.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DownloadStreamCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.FileType;

import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class VisualizzaFilePdfPubblicato extends DownloadStreamCommand{
	
	private FileType file;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfAnagModel pdf = (PdfAnagModel)dataModel;
			
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableLoadAccess("pdfFile",pdf);
			PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, pdf);

			file = pdf.getPdfContent();

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
		return PdfAnagModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getFileName() {
		return file.getFileName();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getFileLength() {
		return file.getFileContent().length;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public InputStream getInputStream() {
		return new ByteArrayInputStream(file.getFileContent());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void downloadTerminated() {
		
	}
}
