package prgm.pdfwebforms.model;

import com.atosorigin.wfem.types.DateType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfInstanceSearchParamsModel extends PdfInstanceModel{
	
	private DateType 	dataInizio = new DateType();
	private DateType 	dataFine = new DateType();
	
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
	
}
