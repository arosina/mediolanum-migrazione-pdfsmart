package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
/*
 * Output Servizio AggiornaPatrimonio
 */
public class OutputAggiornaPatrimonioModel extends CommandDataModel implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 * numElem : numero della domanda coinvolta nell'errore
	 */
	private StringType descErr = new StringType();
	private StringType esito   = new StringType();
	private IntegerType numElem = new IntegerType();
	
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
	public IntegerType getNumElem() {
		return numElem;
	}
	public void setNumElem(IntegerType numElem) {
		this.numElem = numElem;
	}	
}
