package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************/
/*****************************************************************/
public class AdeguatezzaClienteModel extends CommandDataModel {

	private StringType 	codCliente		= new StringType();
	private StringType 	adeNote		  	= new StringType();
	private StringType 	errProf		   	= new StringType();
	private StringType 	desTogg		 	= new StringType();
	private StringType 	desDime		 	= new StringType();
	private StringType 	cliRuolo	 	= new StringType();
	
	
	/*****************************************************************/
	/*****************************************************************/
	public StringType getCodCliente() {
		return codCliente;
	}
	public void setCodCliente(StringType codCliente) {
		this.codCliente = codCliente;
	}
	public StringType getAdeNote() {
		return adeNote;
	}
	public void setAdeNote(StringType adeNote) {
		this.adeNote = adeNote;
	}
	public StringType getErrProf() {
		return errProf;
	}
	public void setErrProf(StringType errProf) {
		this.errProf = errProf;
	}
	public StringType getDesTogg() {
		return desTogg;
	}
	public void setDesTogg(StringType desTogg) {
		this.desTogg = desTogg;
	}
	public StringType getDesDime() {
		return desDime;
	}
	public void setDesDime(StringType desDime) {
		this.desDime = desDime;
	}
	public StringType getCliRuolo() {
		return cliRuolo;
	}
	public void setCliRuolo(StringType cliRuolo) {
		this.cliRuolo = cliRuolo;
	}
}
