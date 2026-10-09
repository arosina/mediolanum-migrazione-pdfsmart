package prgm.pdfwebforms.drivers;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado

 * Richiamato per l'elaborazione dati a valle del processo di dataentry
 * Ogni step che si desidera implementare va nominato con doStep1, doStep2 etc. 
 * Lo step può pilotare se bloccare l'elaborazione del pdf in caso di errori irreversibili (PostCompletionOutputData.STOP)
 * piuttosto che ritentarne l'esecuzione (PostCompletionOutputData.RETRY) piuttosto che ritornare OK per l'esecuzione dello step
 * successivo (PostCompletionOutputData.OK)
**************************************************************************************************/
public abstract class PdfBasePostCompletion{

	private PdfModel pdf;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initInstance(PdfModel pdf){
		this.pdf = pdf;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean existOtherPdf(String pdfCodeOrMomCode) throws Exception{
		if(!pdf.isMultiPdf())
			return false;

		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return true;
			}
		}		
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfAnagModel getOtherPdfAnag(String pdfCodeOrMomCode) {
		if(!pdf.isMultiPdf())
			return null;
		
		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return (PdfAnagModel)pdf.getPdfAnags().get(i);
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel getOtherPdfData(String pdfCodeOrMomCode) {
		if(!pdf.isMultiPdf())
			return null;

		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return pdfDataElement;
			}
		}
		return null;
	}


}
