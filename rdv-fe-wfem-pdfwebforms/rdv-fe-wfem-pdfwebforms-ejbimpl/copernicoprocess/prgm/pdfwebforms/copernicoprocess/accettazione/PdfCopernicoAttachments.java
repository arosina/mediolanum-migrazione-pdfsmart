package prgm.pdfwebforms.copernicoprocess.accettazione;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.IntegerType;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.core.PdfWebFormsException;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfCopernicoAttachments {

	/*******************************************************************/
	/*******************************************************************/
	private PdfCopernicoAttachments() {}
	
	/*******************************************************************/
	/*******************************************************************/
	public static byte[] addAttachments(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws DAOException, Exception {
		IntegerType numPages = getNumPages(csc, pdf);
		ByteArrayType originalPdf = getOriginalPdf(csc, pdf);
		if(numPages != null && numPages.intValue() > 1 && !originalPdf.isNull()) {
			byte[] attachmentsPdf = extractPdfAttachments(originalPdf.byteArrayValue(), numPages.intValue());
			if(attachmentsPdf.length > 0)
				pdfContent = addPdfAttachments(pdfContent, attachmentsPdf);
		}
		return pdfContent;
	}
			
	/*******************************************************************/
	/*******************************************************************/
	private static IntegerType getNumPages(ClientSessionContext csc, PdfModel pdf) throws DAOException{
		IntegerType numPages = null;
		if(pdf.isMultiPdf()) {
			numPages = (IntegerType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
					"select sum(NUM_PAGES) from PDF_INSTANCE_DETT where PDF_INSTANCE_ID='"+pdf.getPdfData().getPdfInstanceId()+"'", null , IntegerType.class).getSingleResult();
		}else {
			numPages = (IntegerType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
					"select NUM_PAGES from PDF_INSTANCE where PDF_INSTANCE_ID='"+pdf.getPdfData().getPdfInstanceId()+"'", null , IntegerType.class).getSingleResult();
		}
		return numPages;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static ByteArrayType getOriginalPdf(ClientSessionContext csc, PdfModel pdf) throws DAOException, Exception{
		ByteArrayType originalPdf = (ByteArrayType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
				"select PDF_CONTENT from PDF_INSTANCE where PDF_INSTANCE_ID='"+pdf.getPdfData().getPdfInstanceId()+"'", null , ByteArrayType.class).getSingleResult();
		if(originalPdf == null || originalPdf.isNull())
			originalPdf = new ByteArrayType(PdfNasUtil.PDF_INSTANCE.readPdfInstanceContent(csc, pdf.getPdfData().getPdfInstanceId()));
		return originalPdf;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static byte[] extractPdfAttachments(byte[] pdfContent, int lastPdfPage) throws PdfWebFormsException{     
		try {
			InputStream pdf = new ByteArrayInputStream(pdfContent);
			PdfReader reader = new PdfReader(pdf);
			int numPages = reader.getNumberOfPages();
			if(numPages <= lastPdfPage) {
				reader.close();
				return new byte[0];
			}
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			Document document = new Document(reader.getPageSizeWithRotation(1));
			PdfCopy writer = new PdfCopy(document, pdfOut);
			document.open();
	        for (int i=lastPdfPage+1;i<=numPages;i++){
			    PdfImportedPage page = writer.getImportedPage(reader,i);
			    writer.addPage(page);
	        }
			document.close();
			writer.close();
			reader.close();
			return pdfOut.toByteArray();
		}catch(Exception e) {
			throw new PdfWebFormsException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] addPdfAttachments(byte[] originalPdf, byte[] attachmentsPdf) throws PdfWebFormsException{
		try {
			ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
			PdfCopyFields copy = new PdfCopyFields(allPdfOut);
			copy.addDocument(new PdfReader(originalPdf));
			copy.addDocument(new PdfReader(attachmentsPdf));		
			copy.close();
			allPdfOut.close();
			return allPdfOut.toByteArray();
		}catch(Exception e) {
			throw new PdfWebFormsException(e.toString());
		}
	}

}
