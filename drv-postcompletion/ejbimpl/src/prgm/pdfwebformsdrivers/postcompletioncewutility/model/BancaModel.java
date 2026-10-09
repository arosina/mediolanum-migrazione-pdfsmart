package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class BancaModel extends CommandDataModel {

	private StringType codiceAbi = new StringType();
	private StringType codiceCab = new StringType();
	private StringType ragioneSociale = new StringType();
	private StringType descrizioneSportello = new StringType();
	private StringType indirizzo = new StringType();
	private StringType localita = new StringType();
	private StringType provincia = new StringType();
	public StringType getCodiceAbi() {
		return codiceAbi;
	}
	public void setCodiceAbi(StringType codiceAbi) {
		this.codiceAbi = codiceAbi;
	}
	public StringType getCodiceCab() {
		return codiceCab;
	}
	public void setCodiceCab(StringType codiceCab) {
		this.codiceCab = codiceCab;
	}
	public StringType getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(StringType ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public StringType getDescrizioneSportello() {
		return descrizioneSportello;
	}
	public void setDescrizioneSportello(StringType descrizioneSportello) {
		this.descrizioneSportello = descrizioneSportello;
	}
	public StringType getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(StringType indirizzo) {
		this.indirizzo = indirizzo;
	}
	public StringType getLocalita() {
		return localita;
	}
	public void setLocalita(StringType localita) {
		this.localita = localita;
	}
	public StringType getProvincia() {
		return provincia;
	}
	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}
	
}
