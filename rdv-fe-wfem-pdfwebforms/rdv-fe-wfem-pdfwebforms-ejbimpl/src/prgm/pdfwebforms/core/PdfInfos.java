package prgm.pdfwebforms.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.model.PdfDataModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfInfos implements Serializable{
	
	protected float[] pageWidths;
	protected float[] pageHeights;
	protected List<PdfFieldInfos> fieldInfos = new ArrayList<PdfFieldInfos>();
	protected List<PdfActionInfos> actionInfos = new ArrayList<PdfActionInfos>();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<PdfFieldInfos> getFieldInfos(String pdfFieldName){
		ArrayList<PdfFieldInfos> fields = new ArrayList<PdfFieldInfos>();
		for(PdfFieldInfos fi : getFieldInfos()){
			if(fields.contains(fi) || !fi.pdfFieldName.equals(pdfFieldName))
				continue;
			
			fields.add(fi);
			if(fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON || 
				(fi.fieldType == AcroFields.FIELD_TYPE_COMBO || fi.fieldType == AcroFields.FIELD_TYPE_LIST)){
				for(PdfFieldInfos fi2 : getFieldInfos()){
					if(fi.pdfFieldName.equals(fi2.pdfFieldName) && fi2 != fi)
						fields.add(fi2);
				}
			}
		}
		return fields;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfFieldInfos findFieldInfoByPdfName(String pdfFieldName){
		String[] pdfFieldNames = pdfFieldName.split("\\,");
		for(PdfFieldInfos fi : fieldInfos){
			if(fi.mainField != null)
				continue;
			for(int i=0;i<pdfFieldNames.length;i++){
				if(fi.pdfFieldName.equals(pdfFieldNames[i]))
					return fi;
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfFieldInfos findFieldInfoByHtmlName(String htmlFieldName){
		String[] htmlFieldNames = htmlFieldName.split("\\,");
		for(PdfFieldInfos fi : fieldInfos){
			if(fi.mainField != null)
				continue;
			for(int i=0;i<htmlFieldNames.length;i++){
				if(fi.htmlFieldName.equals(htmlFieldNames[i]))
					return fi;
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfFieldInfos findFieldInfoByFieldInfo(PdfFieldInfos otherFi){
		for(PdfFieldInfos fi : fieldInfos){
			if(fi.mainField != null)
				continue;
			if(fi.pdfFieldName.equals(otherFi.pdfFieldName) &&
			   fi.fieldType == otherFi.fieldType &&
			   fi.expValue.equals(otherFi.expValue))
				return fi;
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<PdfActionInfos> findActions(String actionName){
		ArrayList<PdfActionInfos> res = new ArrayList<PdfActionInfos>();
		for(PdfActionInfos ai : actionInfos){
			if(ai.pdfFieldName.equals(actionName))
				res.add(ai);
		}
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int numDeclaredSignFields(){
		int res = 0;
		for(PdfFieldInfos fi : fieldInfos){
			if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
				res++;
		}
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public int numSignFields(PdfDataModel pdfData){
		
		String[] fieldsToRemove = pdfData.getFieldsToRemove().toString().split("\\,");
		ArrayList<String> fieldsToRemoveAsArray = new ArrayList<String>(Arrays.asList(fieldsToRemove)); 

		int res = 0;
		for(PdfFieldInfos fi : fieldInfos){
			if( fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE &&
			   !fieldsToRemoveAsArray.contains(fi.htmlFieldName))
				res++;
		}
		return res;
	}

	public float[] getPageWidths() {
		return pageWidths;
	}

	public float[] getPageHeights() {
		return pageHeights;
	}

	public List<PdfFieldInfos> getFieldInfos() {
		return fieldInfos;
	}

	public List<PdfActionInfos> getActionInfos() {
		return actionInfos;
	}
    
}
