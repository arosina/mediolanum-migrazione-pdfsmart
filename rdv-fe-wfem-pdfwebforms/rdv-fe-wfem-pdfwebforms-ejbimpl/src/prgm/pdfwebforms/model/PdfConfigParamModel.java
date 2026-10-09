package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfConfigParamModel extends CommandDataModel {

	private static final long serialVersionUID = 1L;

	private StringType sezione = new StringType();
	private StringType parametro = new StringType();
	private StringType valore = new StringType();
	private StringType descrizione = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigParamModel(String sezione, String parametro, String descrizione){
		this.sezione = new StringType(sezione);
		this.parametro = new StringType(parametro);
		this.descrizione = new StringType(descrizione);
	}
	
	public StringType getSezione() {
		return sezione;
	}
	public void setSezione(StringType sezione) {
		this.sezione = sezione;
	}
	public StringType getParametro() {
		return parametro;
	}
	public void setParametro(StringType parametro) {
		this.parametro = parametro;
	}
	public StringType getValore() {
		return valore;
	}
	public void setValore(StringType valore) {
		this.valore = valore;
	}
	public StringType getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}
}
