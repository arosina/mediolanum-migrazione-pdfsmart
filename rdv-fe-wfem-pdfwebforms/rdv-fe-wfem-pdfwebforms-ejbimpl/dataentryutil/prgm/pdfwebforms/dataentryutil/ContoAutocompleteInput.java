package prgm.pdfwebforms.dataentryutil;

/*******************************************************************/
/*******************************************************************/
public class ContoAutocompleteInput {

	public static String TIPO_CONTO_CONTO_CORRENTE = "CONTO_CORRENTE";
	
	boolean useCodAgente = true;
	String	ndgFieldName = "";
	String	tipoConto = "";
	String	ruoliAmmessi = "";
	String	divisaConto = "";
	String	contoFieldName = "";
	String	fieldName = "";
	
	public boolean isUseCodAgente() {
		return useCodAgente;
	}
	public void setUseCodAgente(boolean useCodAgente) {
		this.useCodAgente = useCodAgente;
	}
	public String getNdgFieldName() {
		return ndgFieldName;
	}
	public void setNdgFieldName(String ndgFieldName) {
		this.ndgFieldName = ndgFieldName;
	}
	public String getTipoConto() {
		return tipoConto;
	}
	public void setTipoConto(String tipoConto) {
		this.tipoConto = tipoConto;
	}
	public String getContoFieldName() {
		return contoFieldName;
	}
	public void setContoFieldName(String contoFieldName) {
		this.contoFieldName = contoFieldName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRuoliAmmessi() {
		return ruoliAmmessi;
	}
	public void setRuoliAmmessi(String ruoliAmmessi) {
		this.ruoliAmmessi = ruoliAmmessi;
	}
	public String getDivisaConto() {
		return divisaConto;
	}
	public void setDivisaConto(String divisaConto) {
		this.divisaConto = divisaConto;
	}
}
