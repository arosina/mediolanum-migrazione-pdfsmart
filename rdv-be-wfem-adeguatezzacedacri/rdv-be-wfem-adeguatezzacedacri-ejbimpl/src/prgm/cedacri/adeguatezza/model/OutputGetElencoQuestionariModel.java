package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
/*
 * Output servizio GetElencoQuestionari
 */
public class OutputGetElencoQuestionariModel extends CommandDataModel implements java.io.Serializable
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
	 * Output
	 * seCompi : flag che indica se il questionario è stato compilato
	 * storico : elenco dei questionari (classe ElementoStoricoModel)
	 */
	private StringType seCompi = new StringType();
	private ListType   storico = new ListType();
	
	/*
	 * Metodi Set e Get
	 */
	public ListType getStorico() {
		return storico;
	}
	public void setStorico(ListType storico) {
		this.storico = storico;
	}
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
	public StringType getSeCompi() {
		return seCompi;
	}
	public void setSeCompi(StringType seCompi) {
		this.seCompi = seCompi;
	}
}
