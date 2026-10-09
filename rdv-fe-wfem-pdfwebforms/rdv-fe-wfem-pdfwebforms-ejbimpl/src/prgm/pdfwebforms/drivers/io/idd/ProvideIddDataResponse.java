package prgm.pdfwebforms.drivers.io.idd;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideIddDataResponse extends CommandDataModel{
	
	public static int VERIFICA_IDD_RAMO_III 		= 1;
	public static int VERIFICA_IDD_RAMO_PROTEZIONE 	= 2;
	public static int VERIFICA_IDD_RAMO_PREVIDENZA 	= 3;
	
	public static int TIPO_DISPOSITIVA_INIZIALE 	= 1;
	public static int TIPO_DISPOSITIVA_AGGIUNTIVA 	= 2;
	public static int TIPO_DISPOSITIVA_VARIAZIONE 	= 3;
	
	private int				tipoVerfica = VERIFICA_IDD_RAMO_III;		
	private int				tipoDispositiva = TIPO_DISPOSITIVA_INIZIALE;
    private StringType      codiceApplicazioneChiamante = new StringType();
    private StringType      tariffa = new StringType();
    private StringType      prodotto = new StringType();	// Prodotto per il controllo IDD in caso di chiamata non Target Market
    private StringType      prodottoTM = new StringType();	// RFC #292576: Prodotto in caso di chiamata Target Market

	public int getTipoVerfica() {
		return tipoVerfica;
	}

	public void setTipoVerfica(int tipoVerfica) {
		this.tipoVerfica = tipoVerfica;
	}

	public StringType getCodiceApplicazioneChiamante() {
		return codiceApplicazioneChiamante;
	}

	public void setCodiceApplicazioneChiamante(
			StringType codiceApplicazioneChiamante) {
		this.codiceApplicazioneChiamante = codiceApplicazioneChiamante;
	}
	
	public StringType getProdotto() {
		return prodotto;
	}

	public void setProdotto(StringType prodotto) {
		this.prodotto = prodotto;
	}

	public StringType getTariffa() {
		return tariffa;
	}

	public void setTariffa(StringType tariffa) {
		this.tariffa = tariffa;
	}

	public StringType getProdottoTM() {
		return prodottoTM;
	}

	public void setProdottoTM(StringType prodottoTM) {
		this.prodottoTM = prodottoTM;
	}

	public int getTipoDispositiva() {
		return tipoDispositiva;
	}

	public void setTipoDispositiva(int tipoDispositiva) {
		this.tipoDispositiva = tipoDispositiva;
	}

}
