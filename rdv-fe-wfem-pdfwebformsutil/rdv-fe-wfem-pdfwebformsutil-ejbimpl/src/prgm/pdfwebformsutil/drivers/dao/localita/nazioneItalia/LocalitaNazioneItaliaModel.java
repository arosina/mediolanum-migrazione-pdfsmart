package prgm.pdfwebformsutil.drivers.dao.localita.nazioneItalia;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class LocalitaNazioneItaliaModel extends CommandDataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private StringType comune = new StringType();
	private StringType cap = new StringType();
	private StringType provincia = new StringType();
	private StringType nazione = new StringType();
	private StringType codiceCatasto = new StringType();
	
	
	public StringType getComune() {
		return comune;
	}
	public void setComune(StringType comune) {
		this.comune = comune;
	}
	public StringType getCap() {
		return cap;
	}
	public void setCap(StringType cap) {
		this.cap = cap;
	}
	public StringType getProvincia() {
		return provincia;
	}
	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}
	public StringType getNazione() {
		return nazione;
	}
	public void setNazione(StringType nazione) {
		this.nazione = nazione;
	}
	public StringType getCodiceCatasto() {
		return codiceCatasto;
	}
	public void setCodiceCatasto(StringType codiceCatasto) {
		this.codiceCatasto = codiceCatasto;
	}
}
