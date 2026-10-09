package com.atosorigin.wfem.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.tierlog.MiddleTierAccessLogger;
import com.atosorigin.wfem.tierlog.TierAccessLoggerInfo;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.pam.core.Pam;

/**************************************************************************************************/
/**************************************************************************************************/
public class PdfTools {

	/**************************************************************************************************/
	/**************************************************************************************************/
	private PdfTools() {
	    throw new IllegalStateException("PdfTools class");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static byte[] extractPages(byte[] pdfContent, int numPages) throws Exception{
		int[] pages = new int[numPages];
		for(int i=0;i<numPages;i++)
			pages[i] = i+1;
        return extractPages(pdfContent, pages);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static byte[] extractPages(byte[] pdfContent, int[] pages) throws Exception{
		InputStream pdf = new ByteArrayInputStream(pdfContent);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		PdfReader reader = new PdfReader(pdf);
		int numPages = reader.getNumberOfPages();
        Document document = new Document(reader.getPageSizeWithRotation(1));
        PdfCopy writer = new PdfCopy(document, pdfOut);
        document.open();
        for(int i=0;i<pages.length;i++){
        	if(pages[i] > numPages)
        		continue;
            PdfImportedPage page = writer.getImportedPage(reader,pages[i]);
            writer.addPage(page);
        }
        document.close();
        writer.close();
        return pdfOut.toByteArray();
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static byte[] rendiPdfAccessibile(ClientSessionContext csc, byte[] pdfContent, String titoloPdf, String idPdf){
		String tliData = idPdf;
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"PAM","PROCESSDOCUMENT");
		try {			
			InputStream pdfInput = new ByteArrayInputStream(pdfContent);
			ByteArrayOutputStream pdfAccessibile = new Pam().ProcessDocument(pdfInput, titoloPdf);
			if(pdfAccessibile == null)
				throw new Exception(idPdf+": L'api Pam.ProcessDocument non ha restutuito dati");
			pdfContent = pdfAccessibile.toByteArray();			
		}catch(Throwable e) {
			tli.exception();
			tliData = idPdf+": "+e.toString();
		}
		tli.stop(new StringBuffer(tliData));
		return pdfContent;
	}
	
}
