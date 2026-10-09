package prgm.pdfwebforms.core;

import java.util.ArrayList;
import java.util.Arrays;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPagination {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfPagination() {
		throw new IllegalStateException("PdfPagination class");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initPdfPagination(PdfModel pdf) {		
		if(pdf.isMultiPdf()) {
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++)
				initModulePagination(pdf, (PdfDataModel)pdf.getPdfData().getPdfs().get(i), i);
		}else {
			initModulePagination(pdf, pdf.getPdfData(), 0);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initModulePagination(PdfModel pdf, PdfDataModel pdfData, int pdfIndex) {
		if(pdf.getPdfAnags() == null || 
		   pdf.getPdfAnags().size() < (pdfIndex+1) ||
		   pdfData.getPdfInfos() == null || 
		   pdfData.getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.PAGINAZIONE+"1") == null)
			return;
		int numPages = pdf.getPdfAnags().get(pdfIndex).getPdfNumPages().intValue();
		String[] visiblePagesArray = new String[pdf.getPdfAnags().get(pdfIndex).getPdfNumPages().intValue()];
		if(!pdfData.getVisiblePages().isNull()) {
			visiblePagesArray = pdfData.getVisiblePages().toString().split(",");
		}else{
			for(int i=0;i<numPages;i++)
				visiblePagesArray[i] = ""+(i+1);
		}
		ArrayList<String> visiblePages = new ArrayList<>(Arrays.asList(visiblePagesArray));
		int pageNum = 1;
		for(int page=1;page<=numPages;page++) {
			if(!visiblePages.contains(""+page))
				continue;
			pdfData.write(PdfPredefinedFields.PAGINAZIONE+page, new StringType("Pagina "+pageNum+" di "+visiblePagesArray.length));
			pageNum++;
		}
	}

}
