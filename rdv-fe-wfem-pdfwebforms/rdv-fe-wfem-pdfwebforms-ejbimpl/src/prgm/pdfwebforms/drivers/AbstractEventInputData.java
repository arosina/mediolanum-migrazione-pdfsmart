package prgm.pdfwebforms.drivers;

import java.util.ArrayList;
import java.util.Arrays;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractEventInputData {
	
	private PdfModel			pdf;
	private PdfDataModel 		pdfData;
	private ArrayList<String>  	editableFields;
	private ArrayList<String>  	uneditableFields;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AbstractEventInputData(PdfModel pdf){
		this.pdf = pdf;
		this.pdfData = pdf.getPdfData();
		
		String[] editableFields = this.pdfData.getEditableFields().toString().split("\\,");
		this.editableFields = new ArrayList<String>(Arrays.asList(editableFields));

		String[] uneditableFields = this.pdfData.getUneditableFields().toString().split("\\,");
		this.uneditableFields = new ArrayList<String>(Arrays.asList(uneditableFields)); 
		
		pdf.alignDynamicProcessProperties();		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AbstractEventInputData(PdfModel pdf, PdfDataModel pdfData){
		this.pdf = pdf;
		this.pdfData = pdfData;
		
		pdf.alignDynamicProcessProperties();		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isMultiPdf(){
		return this.pdf.isMultiPdf();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel getPdfData() {
		return this.pdfData;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel getPdf() {
		return this.pdf;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<PdfAnagModel> getPdfAnags() {
		return this.pdf.getPdfAnags();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<PdfDataModel> getPdfsData() {
		ArrayList<PdfDataModel> res = new ArrayList<PdfDataModel>();
		if(this.pdf.isMultiPdf()){
			for(int i=0;i<this.pdfData.getPdfs().size();i++)
				res.add((PdfDataModel)this.pdfData.getPdfs().get(i));
		}else{
			res.add(this.pdfData);
		}
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfAnagModel getPdfAnag() {
		return this.pdf.getPdfAnags().get(pdfData.getPdfIndex().intValue());
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<String> getEditableFields() {
		return editableFields;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<String> getUneditableFields() {
		return uneditableFields;
	}
}
