package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class CompartoCopernicoConvertitoModel extends CommandDataModel {
	private StringType 	codCompartoPartenza		= new StringType();
	private StringType 	codCompartoDestinazione = new StringType();
	private DoubleType 	importoComparto		 	= new DoubleType();
	private StringType 	numeroContratto         = new StringType();
	
	
	public StringType getCodCompartoPartenza() {
		return codCompartoPartenza;
	}
	public void setCodCompartoPartenza(StringType codCompartoPartenza) {
		this.codCompartoPartenza = codCompartoPartenza;
	}
	public StringType getCodCompartoDestinazione() {
		return codCompartoDestinazione;
	}
	public void setCodCompartoDestinazione(StringType codCompartoDestinazione) {
		this.codCompartoDestinazione = codCompartoDestinazione;
	}
	public DoubleType getImportoComparto() {
		return importoComparto;
	}
	public void setImportoComparto(DoubleType importoComparto) {
		this.importoComparto = importoComparto;
	}
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	
	

}
