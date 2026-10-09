package prgm.pdfwebforms.basket;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class BasketElement {

	private BusinessCommand	cmd = null;
	private String 			par = null;
	private PdfModel 		dispoPdf = null;
	private boolean			hasFreezeError = false;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel createPdfDataModel() throws Exception{
		PdfDataModel res = new PdfDataModel();
		if(getPar() == null)
			return res;
		String[] pars = getPar().split("\\&");
		for(int i=0;i<pars.length;i++) {
			String[] propAndVal = pars[i].split("\\=");
			String propName = propAndVal[0];
			String value = propAndVal.length < 2 ? "" : propAndVal[1];
			
			if(propName.equals("gobackUrl") || propName.equals("gobackEndUrl"))
				value = decodeUrl(value);
			
			Class propertyType = Tools.getPropertyType(res,propName);
			if(propertyType == null)
				propertyType = StringType.class;			
			
			AbstractType prop = null;
			try{
				prop = (AbstractType)Tools.getPropertyValue(res,propName);
				if(prop != null){
					prop.setStringValue(value);
					Tools.setPropertyValue(res, propName, prop);					
				}
			}catch(Exception e){
				prop=null;
			}
			if(prop == null) {
				prop = AbstractType.newInstance(propertyType, value);
				Tools.setPropertyValue(res, propName, prop);
			}
		}
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String decodeUrl(String url) {
		return url.replace("%26", "&").replace("%3D", "=");
	}
	
	public BusinessCommand getCmd() {
		return cmd;
	}
	public void setCmd(BusinessCommand cmd) {
		this.cmd = cmd;
	}
	public String getPar() {
		return par;
	}
	public void setPar(String par) {
		this.par = par;
	}

	public PdfModel getDispoPdf() {
		return dispoPdf;
	}

	public void setDispoPdf(PdfModel dispoPdf) {
		this.dispoPdf = dispoPdf;
	}

	public boolean hasFreezeError() {
		return hasFreezeError;
	}

	public void setHasFreezeError(boolean hasFreezeError) {
		this.hasFreezeError = hasFreezeError;
	}

}
