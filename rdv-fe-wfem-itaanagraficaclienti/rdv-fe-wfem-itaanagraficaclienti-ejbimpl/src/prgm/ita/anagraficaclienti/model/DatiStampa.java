package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DatiStampa extends MapCommandDataModel {

	private DateType   data = new DateType();
	private StringType codiceAgente = new StringType();
	private StringType codicePromotore = new StringType();
	
	private StringType tipoTelefono = new StringType();
	private StringType numeroTelefono = new StringType();
	private StringType altroNumeroTelefono = new StringType();
	private StringType codFiscPartIva = new StringType();

	public DateType getData() {
		return data;
	}
	public void setData(DateType data) {
		this.data = data;
	}
	public StringType getCodiceAgente() {
		return codiceAgente;
	}
	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}
	public StringType getCodicePromotore() {
		return codicePromotore;
	}
	public void setCodicePromotore(StringType codicePromotore) {
		this.codicePromotore = codicePromotore;
	}
	public StringType getTipoTelefono() {
		return tipoTelefono;
	}
	public void setTipoTelefono(StringType tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}
	public StringType getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(StringType numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public StringType getAltroNumeroTelefono() {
		return altroNumeroTelefono;
	}
	public void setAltroNumeroTelefono(StringType altroNumeroTelefono) {
		this.altroNumeroTelefono = altroNumeroTelefono;
	}
	public StringType getCodFiscPartIva() {
		return codFiscPartIva;
	}
	public void setCodFiscPartIva(StringType codFiscPartIva) {
		this.codFiscPartIva = codFiscPartIva;
	}
}
