package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.io.mifid.OperazioneMifidModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class OperazioneBasketMifidModel extends OperazioneMifidModel{

	private StringType 	codTipoOperazione = null;
	private StringType 	contoDiAddebito = new StringType();
    private ListType 	clienti = new ListType(PdfPersonModel.class);	// Elenco dei clienti di riferiemnto della operazione

	public StringType getContoDiAddebito() {
		return contoDiAddebito;
	}

	public void setContoDiAddebito(StringType contoDiAddebito) {
		this.contoDiAddebito = contoDiAddebito;
	}
	
	public ListType getClienti() {
		return clienti;
	}

	public void setClienti(ListType clienti) {
		this.clienti = clienti;
	}

	public StringType getCodTipoOperazione() {
		return codTipoOperazione;
	}

	public void setCodTipoOperazione(StringType codTipoOperazione) {
		this.codTipoOperazione = codTipoOperazione;
	}

}
