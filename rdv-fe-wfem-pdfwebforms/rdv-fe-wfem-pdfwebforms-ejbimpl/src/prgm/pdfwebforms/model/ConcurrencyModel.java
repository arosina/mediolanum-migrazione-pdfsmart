package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ConcurrencyModel extends CommandDataModel {

	private IntegerType 	idCarrello = new IntegerType();
	private StringType 		pdfInstanceId = new StringType();

	private StringType 		stato = new StringType();
	private TimestampType 	dataOraUltimaModifica = new TimestampType();
	private StringType 		codUtenteUltimaModifica = new StringType();
	
	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}
	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}
	public StringType getStato() {
		return stato;
	}
	public void setStato(StringType stato) {
		this.stato = stato;
	}
	public TimestampType getDataOraUltimaModifica() {
		return dataOraUltimaModifica;
	}
	public void setDataOraUltimaModifica(TimestampType dataOraUltimaModifica) {
		this.dataOraUltimaModifica = dataOraUltimaModifica;
	}
	public StringType getCodUtenteUltimaModifica() {
		return codUtenteUltimaModifica;
	}
	public void setCodUtenteUltimaModifica(StringType codUtenteUltimaModifica) {
		this.codUtenteUltimaModifica = codUtenteUltimaModifica;
	}
	public IntegerType getIdCarrello() {
		return idCarrello;
	}
	public void setIdCarrello(IntegerType idCarrello) {
		this.idCarrello = idCarrello;
	}
}
