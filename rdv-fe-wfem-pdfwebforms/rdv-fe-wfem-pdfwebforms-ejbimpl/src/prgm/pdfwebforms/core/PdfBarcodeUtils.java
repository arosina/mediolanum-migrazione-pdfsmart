package prgm.pdfwebforms.core;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.BarcodeMOMModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfBarcodeUtils{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean initBarcodeFirmaDigitale(ClientSessionContext csc, PdfModel pdf){
		
		if(pdf.getPdfData().getExternalEntityAppl().equals("ONBOARDINGAZIENDE"))
			return initBarcodesCartaceo(csc, pdf, "D");
		
		pdf.setPdfGeneratedBarcodes("");
		
		PdfAnagModel pdfMainAnag = pdf.mainPdfAnag();
		PdfDataModel pdfMainData = pdf.mainPdfData();
		
		String barcode = "";
		
		if(!pdfMainAnag.getPdfMomCode().isNull() && !pdfMainAnag.getPdfMomVersion().isNull()){
			if(pdf.isTestMode()){
				barcode = pdfMainAnag.getPdfMomCode().toString()+pdfMainAnag.getPdfMomVersion().toString()+"C0000000000";
			}else{
				try{
					barcode = generateBarcode(csc, pdfMainAnag.getPdfMomCode().toString(), pdfMainAnag.getPdfMomVersion().toString(), "D");
				}catch(DAOException daoe){
					pdf.addCommandError("Si e' verificato un errore DAO nel recuperare il barcode.<br>"+daoe.toString());
					return false;
				}
			}
		}
		
		pdf.getPdfData().setPdfBarcode(new StringType());
		if(pdf.isMultiPdf()){ // In FD clear barcode on all pdf
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
				PdfDataModel pd = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				pd.setPdfBarcode(new StringType());
			}
		}
		
		// Put barcode on the first
		pdfMainData.setPdfBarcode(new StringType(barcode));
		
		// Put barcodes on global
		pdf.setPdfGeneratedBarcodes(barcode);
		return true;
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean initBarcodesCartaceo(ClientSessionContext csc, PdfModel pdf){
		return initBarcodesCartaceo(csc, pdf, "B");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean initBarcodesCartaceo(ClientSessionContext csc, PdfModel pdf, String canale){

		pdf.setPdfGeneratedBarcodes("");
		String generatedBarcodes = "";
		
		if(pdf.pdfIsInCartaChimica()){				
			for(int i=0;i<pdf.getPdfAnags().size();i++){
				PdfAnagModel pdfAnag = (PdfAnagModel)pdf.getPdfAnags().get(i);
				pdfAnag.setPritBarcode(null);
			}
			return true;
		}
		
		
		PdfDataModel pdfMainData = pdf.mainPdfData();
		
		for(int i=0;i<pdf.getPdfAnags().size();i++){

			PdfAnagModel pdfAnag = pdf.getPdfAnags().get(i);
			PdfDataModel pdfData = pdf.isMultiPdf() ? (PdfDataModel)(pdf.getPdfData().getPdfs().get(i)) : pdfMainData;

			if(pdfData.getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.BARCODE+","+PdfPredefinedFields.BARCODE_IMAGE+","+PdfPredefinedFields.BARCODE_VALUE) == null)
				continue;
			
			String barcode = "";
			
			if(!pdfAnag.getPdfMomCode().isNull() && !pdfAnag.getPdfMomVersion().isNull()){
				if(pdf.isTestMode()){
					barcode = pdfAnag.getPdfMomCode().toString()+pdfAnag.getPdfMomVersion().toString()+"C0000000000";
				}else{
					try{
						barcode = generateBarcode(csc, pdfAnag.getPdfMomCode().toString(), pdfAnag.getPdfMomVersion().toString(), canale);
					}catch(DAOException daoe){
						pdf.addCommandError("Si e' verificato un errore DAO nel recuperare il barcode.<br>"+daoe.toString());
						return false;
					}
					pdfAnag.setPritBarcode(barcode);
				}				
			}
			
			generatedBarcodes += barcode+",";
			
			if(!pdf.isMultiPdf())
				pdf.getPdfData().setPdfBarcode(new StringType(barcode));
			else
				((PdfDataModel)pdf.getPdfData().getPdfs().get(i)).setPdfBarcode(new StringType(barcode));
			
		}
		
		// Put barcodes on global
		if(generatedBarcodes.length() > 0)
			generatedBarcodes = generatedBarcodes.substring(0,generatedBarcodes.length()-1);
		pdf.setPdfGeneratedBarcodes(generatedBarcodes);
		
		return true;
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String generateBarcode(ClientSessionContext csc, String momCode, String momVersion, String canale) throws DAOException{
		BarcodeMOMModel barodeMomModel = new BarcodeMOMModel();
		barodeMomModel.setMomCode(new StringType(momCode));
		new DAOObject(csc,"PdfWebForms.PdfWebForms").executeCallableAccess("getPdfBarcode",barodeMomModel);
		return momCode+momVersion+canale+barodeMomModel.getBarcode().toString();		
	}
}
