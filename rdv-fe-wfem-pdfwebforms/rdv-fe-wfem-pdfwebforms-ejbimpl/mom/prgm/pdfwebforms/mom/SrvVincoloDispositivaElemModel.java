package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SrvVincoloDispositivaElemModel extends CommandDataModel {

	private StringType codiceOperazioneDispositivaSistemaOrigineVincolante = new StringType();
	private StringType codiceOperazioneDispositivaSistemaOrigineVincolata = new StringType();
	private StringType codiceTipologiaVincoloDispositiva = new StringType("P");
	
	public StringType getCodiceOperazioneDispositivaSistemaOrigineVincolante() {
		return codiceOperazioneDispositivaSistemaOrigineVincolante;
	}
	public void setCodiceOperazioneDispositivaSistemaOrigineVincolante(
			StringType codiceOperazioneDispositivaSistemaOrigineVincolante) {
		this.codiceOperazioneDispositivaSistemaOrigineVincolante = codiceOperazioneDispositivaSistemaOrigineVincolante;
	}
	public StringType getCodiceOperazioneDispositivaSistemaOrigineVincolata() {
		return codiceOperazioneDispositivaSistemaOrigineVincolata;
	}
	public void setCodiceOperazioneDispositivaSistemaOrigineVincolata(
			StringType codiceOperazioneDispositivaSistemaOrigineVincolata) {
		this.codiceOperazioneDispositivaSistemaOrigineVincolata = codiceOperazioneDispositivaSistemaOrigineVincolata;
	}
	public StringType getCodiceTipologiaVincoloDispositiva() {
		return codiceTipologiaVincoloDispositiva;
	}
	public void setCodiceTipologiaVincoloDispositiva(StringType codiceTipologiaVincoloDispositiva) {
		this.codiceTipologiaVincoloDispositiva = codiceTipologiaVincoloDispositiva;
	}

}
