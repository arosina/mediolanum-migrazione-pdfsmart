package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************/
/*******************************************************************/
public class PrestitoAutocompleteModel extends AbstractAutocompleteModel {

	private StringType  ndgCliente = new StringType();
	private StringType  numero = new StringType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompleteLabel(){
		return getNumero().toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName){
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String createJsonData(String fieldName){
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append("\"numero\":\""+getNumero()+"\",");
		jsonObj.append("\"importoErogato\":\""+new DoubleType(propertyToString("importoErogato")).toScaledString(2)+"\",");
		jsonObj.append("\"debitoResiduo\":\""+new DoubleType(propertyToString("debitoResiduo")).toScaledString(2)+"\",");
		jsonObj.append("\"totaleRate\":\""+propertyToString("totaleRate")+"\",");
		jsonObj.append("\"ratePagate\":\""+propertyToString("ratePagate")+"\",");
		jsonObj.append("\"dataScadenza\":\""+propertyToString("dataScadenza")+"\",");
		jsonObj.append("\"scadenzaProssimaRata\":\""+propertyToString("scadenzaProssimaRata")+"\",");
		jsonObj.append("\"importoProssimaRata\":\""+new DoubleType(propertyToString("importoProssimaRata")).toScaledString(2)+"\",");
		jsonObj.append("\"progressivoPiano\":\""+propertyToString("progressivoPiano")+"\",");
		jsonObj.append("\"codStato\":\""+propertyToString("codStato")+"\",");
		jsonObj.append("\"tassoIniziale\":\""+new DoubleType(propertyToString("tassoIniziale"))+"\",");
		jsonObj.append("\"tassoCorrente\":\""+new DoubleType(propertyToString("tassoCorrente"))+"\",");
		jsonObj.append("\"taeg\":\""+new DoubleType(propertyToString("taeg"))+"\",");
		jsonObj.append("\"spread\":\""+new DoubleType(propertyToString("spread"))+"\",");
		jsonObj.append("\"codCategoria\":\""+propertyToString("codCategoria")+"\",");
		jsonObj.append("\"codSottocategoria\":\""+propertyToString("codSottocategoria")+"\",");
		jsonObj.append("\"codConvenzione\":\""+propertyToString("codConvenzione")+"\",");
		jsonObj.append("\"contoCorrenteRegolamento\":\""+propertyToString("contoCorrenteRegolamento")+"\",");		
		try{ jsonObj.append("\"value\":\""+Tools.getPropertyValue(this, fieldName)+"\","); }catch(Exception e){}
		jsonObj.append("\"label\":\""+autocompleteLabel()+"\"");
		return jsonObj.toString();
	}
	
	public StringType getNdgCliente() {
		return ndgCliente;
	}

	public void setNdgCliente(StringType ndgCliente) {
		this.ndgCliente = ndgCliente;
	}

	public StringType getNumero() {
		return numero;
	}

	public void setNumero(StringType numero) {
		this.numero = numero;
	}

}
