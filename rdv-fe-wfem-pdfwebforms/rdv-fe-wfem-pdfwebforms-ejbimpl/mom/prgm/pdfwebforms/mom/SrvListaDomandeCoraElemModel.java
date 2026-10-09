package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SrvListaDomandeCoraElemModel extends CommandDataModel {

	private StringType codiceDomanda = new StringType();
	private StringType codiceRisposta = new StringType();
	
	public StringType getCodiceDomanda() {
		return codiceDomanda;
	}
	public void setCodiceDomanda(StringType codiceDomanda) {
		this.codiceDomanda = codiceDomanda;
	}
	public StringType getCodiceRisposta() {
		return codiceRisposta;
	}
	public void setCodiceRisposta(StringType codiceRisposta) {
		this.codiceRisposta = codiceRisposta;
	}
	
}
