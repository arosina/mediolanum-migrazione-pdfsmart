package prgm.pdfwebforms.publisher.nasutil.moveblob;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class MoveBlobModel extends CommandDataModel{

	private DateType dataInizio = new DateType();
	private DateType dataFine = new DateType();

	private IntegerType pdfElaborati = new IntegerType();

	private StringType moveType = new StringType();
	private String resultMessage = null;
	private boolean attivitaInterrotta = false;

	public DateType getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(DateType dataInizio) {
		this.dataInizio = dataInizio;
	}
	public DateType getDataFine() {
		return dataFine;
	}
	public void setDataFine(DateType dataFine) {
		this.dataFine = dataFine;
	}
	public IntegerType getPdfElaborati() {
		return pdfElaborati;
	}
	public void setPdfElaborati(IntegerType pdfElaborati) {
		this.pdfElaborati = pdfElaborati;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public boolean isAttivitaInterrotta() {
		return attivitaInterrotta;
	}
	public void setAttivitaInterrotta(boolean attivitaInterrotta) {
		this.attivitaInterrotta = attivitaInterrotta;
	}
	public StringType getMoveType() {
		return moveType;
	}
	public void setMoveType(StringType moveType) {
		this.moveType = moveType;
	}
	
}
