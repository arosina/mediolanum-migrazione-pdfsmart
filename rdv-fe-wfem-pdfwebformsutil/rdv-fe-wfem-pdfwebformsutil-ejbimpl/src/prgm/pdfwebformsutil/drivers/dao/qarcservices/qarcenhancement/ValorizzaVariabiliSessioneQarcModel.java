package prgm.pdfwebformsutil.drivers.dao.qarcservices.qarcenhancement;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class ValorizzaVariabiliSessioneQarcModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType webCookie = new StringType();
	private ListType elencoVariabili = new ListType(VariabileSessioneQarcModel.class);

	private IntegerType resultCode = new IntegerType();
	private StringType esito = new StringType();

	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

	public StringType getWebCookie() {
		return webCookie;
	}

	public void setWebCookie(StringType webCookie) {
		this.webCookie = webCookie;
	}

	public ListType getElencoVariabili() {
		return elencoVariabili;
	}

	public void setElencoVariabili(ListType elencoVariabili) {
		this.elencoVariabili = elencoVariabili;
	}

	public StringType getEsito() {
		return esito;
	}

	public void setEsito(StringType esito) {
		this.esito = esito;
	}
}
