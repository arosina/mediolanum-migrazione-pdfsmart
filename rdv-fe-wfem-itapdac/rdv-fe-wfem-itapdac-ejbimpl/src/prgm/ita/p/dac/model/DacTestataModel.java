package prgm.ita.p.dac.model;

import prgm.ita.p.dac.facade.Costanti;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacTestataModel extends DacKeyModel {
	
	private StringType	  	codUtenteMittente = new StringType();
	
	private IntegerType     uffLavorazione = new IntegerType();
	private StringType	  	codUtenteLavorazione = new StringType();
	private TimestampType 	dataOraLavorazione = new TimestampType();
	
	private IntegerType     uffSpunta = new IntegerType();
	private StringType	  	codUtenteSpunta = new StringType();
	private TimestampType 	dataOraSpuntaDb = new TimestampType();

	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacTestataModel(){
		
		addCodDescField("stato","StatiDac");
		addCodDescField("esito","EsitiDac");
		
		addCodDescField("uffLavorazione","Uffici");
		addCodDescField("uffSpunta","Uffici");
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getDataOraSpunta() {
		if(!getDataOraSpuntaDb().isNull())
			return getDataOraSpuntaDb();
		if(getStato().intValue() == Costanti.STATO_LAVORATA)
			return getDataOraCambioStato();
		return new TimestampType();
	}

	public StringType getCodUtenteMittente() {
		return codUtenteMittente;
	}


	public void setCodUtenteMittente(StringType codUtenteMittente) {
		this.codUtenteMittente = codUtenteMittente;
	}


	public IntegerType getUffLavorazione() {
		return uffLavorazione;
	}


	public void setUffLavorazione(IntegerType uffLavorazione) {
		this.uffLavorazione = uffLavorazione;
	}


	public StringType getCodUtenteLavorazione() {
		return codUtenteLavorazione;
	}


	public void setCodUtenteLavorazione(StringType codUtenteLavorazione) {
		this.codUtenteLavorazione = codUtenteLavorazione;
	}


	public TimestampType getDataOraLavorazione() {
		return dataOraLavorazione;
	}


	public void setDataOraLavorazione(TimestampType dataOraLavorazione) {
		this.dataOraLavorazione = dataOraLavorazione;
	}


	public IntegerType getUffSpunta() {
		return uffSpunta;
	}


	public void setUffSpunta(IntegerType uffSpunta) {
		this.uffSpunta = uffSpunta;
	}


	public StringType getCodUtenteSpunta() {
		return codUtenteSpunta;
	}


	public void setCodUtenteSpunta(StringType codUtenteSpunta) {
		this.codUtenteSpunta = codUtenteSpunta;
	}


	public TimestampType getDataOraSpuntaDb() {
		return dataOraSpuntaDb;
	}


	public void setDataOraSpuntaDb(TimestampType dataOraSpuntaDb) {
		this.dataOraSpuntaDb = dataOraSpuntaDb;
	}
	
}
