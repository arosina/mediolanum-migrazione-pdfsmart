package prgm.pdfwebformsutil.drivers.dao.agente;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

public class AgenteModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType codAgente = new StringType();
	private DateType dataAbilitazioneIsvap = new DateType();

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public DateType getDataAbilitazioneIsvap() {
		return dataAbilitazioneIsvap;
	}

	public void setDataAbilitazioneIsvap(DateType dataAbilitazioneIsvap) {
		this.dataAbilitazioneIsvap = dataAbilitazioneIsvap;
	}
}