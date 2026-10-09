package prgm.ita.p.dac.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ContatoreModel extends CommandDataModel {
	
	private StringType 		nomeRisorsa = new StringType();
	private StringType 		utente 		= new StringType();
	private IntegerType 	incremento 	= new IntegerType(1);
	private StringType 		progressivo	= new StringType();
	
	public StringType getNomeRisorsa() {
		return nomeRisorsa;
	}
	public void setNomeRisorsa(StringType nomeRisorsa) {
		this.nomeRisorsa = nomeRisorsa;
	}
	public StringType getUtente() {
		return utente;
	}
	public void setUtente(StringType utente) {
		this.utente = utente;
	}
	public IntegerType getIncremento() {
		return incremento;
	}
	public void setIncremento(IntegerType incremento) {
		this.incremento = incremento;
	}
	public StringType getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(StringType progressivo) {
		this.progressivo = progressivo;
	}

}
