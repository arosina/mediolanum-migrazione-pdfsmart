package prgm.pdfwebforms.drivers.io;

import java.util.Map;

import prgm.pdfwebforms.drivers.AbstractEventOutputData;


/***********************************************************************************************/
/***********************************************************************************************/
public class PageLoadOutputData extends AbstractEventOutputData{
	
	private String[] 			globalJsScripts = null;
	private Map<String, String> fieldsJsScripts = null;

	/**************************************************************************************************
	 * Elenco dei JS privati. Escludere la webapp, viene impostata automaticamente come prefisso
	**************************************************************************************************/
	public void setGlobalJsScripts(String[] globalJsScripts) {
		this.globalJsScripts = globalJsScripts;
	}
	
	/**************************************************************************************************
	 * Elenco dei metodi javascript da associare ai campi del pdf
	**************************************************************************************************/
	public void setFieldsJsScripts(Map<String, String> fieldsJsScripts) {
		this.fieldsJsScripts = fieldsJsScripts;
	}

	public String[] getGlobalJsScripts() {
		return globalJsScripts;
	}

	public Map<String, String> getFieldsJsScripts() {
		return fieldsJsScripts;
	}

}
