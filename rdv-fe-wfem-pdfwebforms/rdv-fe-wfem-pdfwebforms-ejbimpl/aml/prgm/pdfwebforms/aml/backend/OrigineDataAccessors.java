package prgm.pdfwebforms.aml.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DoubleType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class OrigineDataAccessors {

	private PdfAnagModel pdfAnag = null;
	private PdfDataModel pdfData = null;
	
	private static Pattern INDICE_ELENCO = Pattern.compile("(.*)\\((\\d+)\\)");
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public OrigineDataAccessors(PdfAnagModel pdfAnag, PdfDataModel pdfData){
		this.pdfAnag = pdfAnag;
		this.pdfData = pdfData;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfAnagModel getPdfAnag() {
		return pdfAnag;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel getPdfData() {
		return pdfData;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean driver(String... driverNames) {
		for(String driverName : driverNames) {
			if(!pdfAnag.getPdfDriverName().isNull() && pdfAnag.getPdfDriverName().equals(driverName))
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String sval(String fieldName) {
		AbstractType p = pdfData.read(fieldName);
		if(p != null) 
			return p.toString();
		return "";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean equ(String fieldName, String fieldValue) {
		AbstractType d = pdfData.read(fieldName);
		if(d != null) 
			return d.equals(fieldValue);
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean exist(String... fieldNames) {
		for(String fieldName : fieldNames) {
			if(pdfData.fieldExist(fieldName))
				return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public double val(String fieldName) throws Exception{
		
		Matcher mat = INDICE_ELENCO.matcher(fieldName);
		if(mat.matches())
			return sum(mat.group(1), Integer.valueOf(mat.group(2)));
		
		return dVal(fieldName);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public double isnull(String... fieldNames) throws Exception{
		for(String fieldName : fieldNames) {
			double res = val(fieldName);
			if(res > 0)
				return res;
		}
		return 0;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public double sum(String fieldName, int firstIdx) throws Exception{
		double res = 0;
		for(int i=firstIdx;i<100;i++) {
			res += dVal(fieldName+i);
		}
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private double dVal(String fieldName) throws Exception{
		AbstractType d = pdfData.read(fieldName);
		if(d == null)
			return 0;
		if(!(d instanceof DoubleType))
			d = new DoubleType(d.toString());
		return ((DoubleType)d).doubleValue();
	}

}
