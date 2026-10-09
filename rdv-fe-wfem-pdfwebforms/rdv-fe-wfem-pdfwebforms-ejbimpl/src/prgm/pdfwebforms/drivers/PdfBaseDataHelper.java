package prgm.pdfwebforms.drivers;

import com.atosorigin.wfem.types.AbstractType;

import prgm.pdfwebforms.model.PdfDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class PdfBaseDataHelper {
	
	private PdfDataModel pdfData;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfBaseDataHelper(PdfDataModel pdfData){
		this.pdfData = pdfData;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AbstractType read(String fieldName) throws PdfAcroFieldNotFoundException {
		AbstractType field = pdfData.read(fieldName);
		if(field == null)
			throw new PdfAcroFieldNotFoundException("Field ["+fieldName+"] not found");
		return field;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void write(String fieldName, AbstractType fieldValue){
		pdfData.write(fieldName, fieldValue);
		return;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel getPdfData() {
		return pdfData;
	}
}
