package com.atosorigin.wfem.htmltopdf;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.pdf.AdobeFormCompiler;
import com.atosorigin.wfem.util.DbPdfWebResourceLoader;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class PdfFileController{

	private DocumentController documentController;
	private String pdfFileName;
	
	/*************************************************************************/
	/*************************************************************************/
	public PdfFileController(DocumentController documentController, String pdfFileName){
		this.documentController = documentController;
		this.pdfFileName = pdfFileName;
	}

	/*************************************************************************/
	/*************************************************************************/
	public void includePdfDocument() throws Exception {
		if(documentController.getAppContext() == null)
			return;

		try{
			CommandDataModel pageModel = documentController.getPageModel();
			InputStream pdfIn = DbPdfWebResourceLoader.getInputStream(documentController.getRequestManager().getRedirWebApp()+pdfFileName);
			if(pdfIn == null)
				pdfIn = documentController.getAppContext().getResourceAsStream(pdfFileName);
		    PdfReader reader = new PdfReader(pdfIn);
		    ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		    AdobeFormCompiler.fillAdobeForm(documentController.getAppContext(),stamp,pageModel);
			stamp.setFormFlattening(true);
		    stamp.close();
		    pdfIn.close();
		
		    PdfReader copyReader = new PdfReader(pdfOut.toByteArray());
		    copyReader.consolidateNamedDestinations();
		
		    for (int j=0;j<copyReader.getNumberOfPages();j++) {
		        PdfImportedPage page = documentController.getWriter().getImportedPage(copyReader, j+1);
		        float x=0;
		        float y=0;
		        documentController.getPdfDocument().newPage();
		        try{
			        Rectangle pageSize = documentController.getPdfDocument().getPageSize();
			        Rectangle pdfSize = page.getBoundingBox();
			        x = (pageSize.width()-pdfSize.width())/2;
			        y = (pageSize.height()-pdfSize.height())/2;
			        if(x < 0) x=0;
			        if(y < 0) y=0;
		        }catch(Exception e){x=0;y=0;}
		        documentController.getWriter().getDirectContent().addTemplate(page, x, y);
		    }
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return;
	}

}
