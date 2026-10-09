package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class RipartizioneStrategia extends CommandDataModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType codProdotto = new StringType();
	private StringType linea = new StringType();
	private StringType tipoServz = new StringType();
	private DoubleType importoDestinazione = new DoubleType(0);
	private DoubleType importoPartenza = new DoubleType(0);
	
	public StringType getCodProdotto() {
		return codProdotto;
	}
	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	public StringType getLinea() {
		return linea;
	}
	public DoubleType getImportoDestinazione() {
		return importoDestinazione;
	}
	public DoubleType getImportoPartenza() {
		return importoPartenza;
	}
	public void setLinea(StringType linea) {
		this.linea = linea;
	}
	public StringType getTipoServz() {
		return tipoServz;
	}
	public void setTipoServz(StringType tipoServz) {
		this.tipoServz = tipoServz;
	}
	public void setImportoDestinazione(DoubleType importoDestinazione) {
		this.importoDestinazione = importoDestinazione;
	}
	public void setImportoPartenza(DoubleType importoPartenza) {
		this.importoPartenza = importoPartenza;
	}

			
}
