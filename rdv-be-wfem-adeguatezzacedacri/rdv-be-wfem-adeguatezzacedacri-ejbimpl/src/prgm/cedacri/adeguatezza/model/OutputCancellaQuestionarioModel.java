package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
/*
 * Output Servizio CancellaQuestionario 
 */
public class OutputCancellaQuestionarioModel extends CommandDataModel  implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 */
	private StringType descErr = new StringType();
	private StringType esito   = new StringType();
	
	/*
	 * Funzioni Get e Set
	 */
	public StringType getDescErr() {
		return descErr;
	}
	public void setDescErr(StringType descErr) {
		this.descErr = descErr;
	}
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}	
}
