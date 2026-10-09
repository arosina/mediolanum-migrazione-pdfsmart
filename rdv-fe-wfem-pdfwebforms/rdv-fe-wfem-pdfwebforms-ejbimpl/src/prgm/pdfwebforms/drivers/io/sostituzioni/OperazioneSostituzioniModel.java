package prgm.pdfwebforms.drivers.io.sostituzioni;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class OperazioneSostituzioniModel extends CommandDataModel{
	
	private StringType  idDisposizione = new StringType();
	private StringType  tipoDisposizione = new StringType();
	private StringType  tipoOperazione = new StringType();
	private StringType  idContratto = new StringType();
	private StringType  idContrattoPartenza = new StringType();
	private StringType  derogaVersamentoIniziale = new StringType();
	private StringType  derogaPercentuale = new StringType();
	private IntegerType progressivo = new IntegerType();
	private ListType	prodotti = new ListType(ProdottoSostituzioniModel.class);
	
	public StringType getTipoDisposizione() {
		return tipoDisposizione;
	}
	public void setTipoDisposizione(StringType tipoDisposizione) {
		this.tipoDisposizione = tipoDisposizione;
	}
	public StringType getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(StringType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public StringType getIdContratto() {
		return idContratto;
	}
	public void setIdContratto(StringType idContratto) {
		this.idContratto = idContratto;
	}
	public StringType getDerogaVersamentoIniziale() {
		return derogaVersamentoIniziale;
	}
	public void setDerogaVersamentoIniziale(StringType derogaVersamentoIniziale) {
		this.derogaVersamentoIniziale = derogaVersamentoIniziale;
	}
	public StringType getDerogaPercentuale() {
		return derogaPercentuale;
	}
	public void setDerogaPercentuale(StringType derogaPercentuale) {
		this.derogaPercentuale = derogaPercentuale;
	}
	public IntegerType getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}
	public ListType getProdotti() {
		return prodotti;
	}
	public void setProdotti(ListType prodotti) {
		this.prodotti = prodotti;
	}
	public StringType getIdContrattoPartenza() {
		return idContrattoPartenza;
	}
	public void setIdContrattoPartenza(StringType idContrattoPartenza) {
		this.idContrattoPartenza = idContrattoPartenza;
	}
	public StringType getIdDisposizione() {
		return idDisposizione;
	}
	public void setIdDisposizione(StringType idDisposizione) {
		this.idDisposizione = idDisposizione;
	}
	
}
