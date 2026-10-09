package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

/***********************************************************************************************/
/***********************************************************************************************/
public class ContatoreModel extends CommandDataModel {
	
	private StringType 		codAgente	 	= new StringType();
	private StringType 		codRete 		= new StringType();
	private StringType 		nomeTabella 	= new StringType();
	private IntegerType 	contatore 		= new IntegerType();
	private TimestampType	dataVariazione	= new TimestampType();		
	private IntegerType 	numCaratteri	= new IntegerType();
	private StringType 		serverReplica 	= new StringType();
	private StringType 		prefisso 	= new StringType();

	private StringType 		nomeRisorsa = new StringType();
	private StringType 		utente 		= new StringType();
	private IntegerType 	incremento 	= new IntegerType();
	private StringType 		progressivo	= new StringType();

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getNomeTabella() {
		return nomeTabella;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setNomeTabella(StringType nomeTabella) {
		this.nomeTabella = nomeTabella;
	}

	public IntegerType getContatore() {
		return contatore;
	}

	public void setContatore(IntegerType contatore) {
		this.contatore = contatore;
	}

	public TimestampType getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(TimestampType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public IntegerType getNumCaratteri() {
		return numCaratteri;
	}

	public StringType getPrefisso() {
		return prefisso;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public void setNumCaratteri(IntegerType numCaratteri) {
		this.numCaratteri = numCaratteri;
	}

	public void setPrefisso(StringType prefisso) {
		this.prefisso = prefisso;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public IntegerType getIncremento() {
		return incremento;
	}

	public StringType getNomeRisorsa() {
		return nomeRisorsa;
	}

	public StringType getProgressivo() {
		return progressivo;
	}

	public StringType getUtente() {
		return utente;
	}

	public void setIncremento(IntegerType incremento) {
		this.incremento = incremento;
	}

	public void setNomeRisorsa(StringType nomeRisorsa) {
		this.nomeRisorsa = nomeRisorsa;
	}

	public void setProgressivo(StringType progressivo) {
		this.progressivo = progressivo;
	}

	public void setUtente(StringType utente) {
		this.utente = utente;
	}

}
