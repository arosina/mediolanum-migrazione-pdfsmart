package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SrvDispositivaResultElemModel extends CommandDataModel {

	private StringType	codiceOperazioneDispositiva = new StringType();
	private StringType	codiceOperazioneDispositivaSistemaOrigine = new StringType();
	
	public StringType getCodiceOperazioneDispositiva() {
		return codiceOperazioneDispositiva;
	}
	public void setCodiceOperazioneDispositiva(StringType codiceOperazioneDispositiva) {
		this.codiceOperazioneDispositiva = codiceOperazioneDispositiva;
	}
	public StringType getCodiceOperazioneDispositivaSistemaOrigine() {
		return codiceOperazioneDispositivaSistemaOrigine;
	}
	public void setCodiceOperazioneDispositivaSistemaOrigine(StringType codiceOperazioneDispositivaSistemaOrigine) {
		this.codiceOperazioneDispositivaSistemaOrigine = codiceOperazioneDispositivaSistemaOrigine;
	}
	
}
