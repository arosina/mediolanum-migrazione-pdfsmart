package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DatiVariazioneModel extends CommandDataModel {

	private StringType codiceVariazione = new StringType();
	private StringType statoVariazione = new StringType();
	private TimestampType dataValidazione = new TimestampType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getCodiceVariazione() {
		return codiceVariazione;
	}
	public void setCodiceVariazione(StringType codiceVariazione) {
		this.codiceVariazione = codiceVariazione;
	}
	public StringType getStatoVariazione() {
		return statoVariazione;
	}
	public void setStatoVariazione(StringType statoVariazione) {
		this.statoVariazione = statoVariazione;
	}
	public TimestampType getDataValidazione() {
		return dataValidazione;
	}
	public void setDataValidazione(TimestampType dataValidazione) {
		this.dataValidazione = dataValidazione;
	}
	
}
