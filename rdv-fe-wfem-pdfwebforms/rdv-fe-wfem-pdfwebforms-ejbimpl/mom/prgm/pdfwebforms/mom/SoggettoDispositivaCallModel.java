package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SoggettoDispositivaCallModel extends CommandDataModel {

	private StringType codiceRuoloSoggetto = new StringType();
	private StringType progressivoSoggettoDispositiva = new StringType();
	private StringType codiceTipoSoggetto = new StringType();
	private StringType codiceSoggettoOriginale = new StringType();
	private StringType codiceSoggettoEsterno = new StringType();
	private StringType denominazioneNomeSoggettoEsterno = new StringType();
	private StringType denominazioneCognomeSoggettoEsterno = new StringType();
	
	public StringType getCodiceRuoloSoggetto() {
		return codiceRuoloSoggetto;
	}
	public void setCodiceRuoloSoggetto(StringType codiceRuoloSoggetto) {
		this.codiceRuoloSoggetto = codiceRuoloSoggetto;
	}
	public StringType getProgressivoSoggettoDispositiva() {
		return progressivoSoggettoDispositiva;
	}
	public void setProgressivoSoggettoDispositiva(
			StringType progressivoSoggettoDispositiva) {
		this.progressivoSoggettoDispositiva = progressivoSoggettoDispositiva;
	}
	public StringType getCodiceTipoSoggetto() {
		return codiceTipoSoggetto;
	}
	public void setCodiceTipoSoggetto(StringType codiceTipoSoggetto) {
		this.codiceTipoSoggetto = codiceTipoSoggetto;
	}
	public StringType getCodiceSoggettoOriginale() {
		return codiceSoggettoOriginale;
	}
	public void setCodiceSoggettoOriginale(StringType codiceSoggettoOriginale) {
		this.codiceSoggettoOriginale = codiceSoggettoOriginale;
	}
	public StringType getCodiceSoggettoEsterno() {
		return codiceSoggettoEsterno;
	}
	public void setCodiceSoggettoEsterno(StringType codiceSoggettoEsterno) {
		this.codiceSoggettoEsterno = codiceSoggettoEsterno;
	}
	public StringType getDenominazioneNomeSoggettoEsterno() {
		return denominazioneNomeSoggettoEsterno;
	}
	public void setDenominazioneNomeSoggettoEsterno(StringType denominazioneNomeSoggettoEsterno) {
		this.denominazioneNomeSoggettoEsterno = denominazioneNomeSoggettoEsterno;
	}
	public StringType getDenominazioneCognomeSoggettoEsterno() {
		return denominazioneCognomeSoggettoEsterno;
	}
	public void setDenominazioneCognomeSoggettoEsterno(StringType denominazioneCognomeSoggettoEsterno) {
		this.denominazioneCognomeSoggettoEsterno = denominazioneCognomeSoggettoEsterno;
	}

}
