package prgm.pdfwebforms.drivers.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SaldoContoModel extends CommandDataModel{

	private boolean		notFound = false;
	
	private DoubleType 	saldoDisponibile = new DoubleType();
	private DoubleType 	saldoContabile = new DoubleType();
	
	public boolean isNotFound() {
		return notFound;
	}
	public void setNotFound(boolean notFound) {
		this.notFound = notFound;
	}
	public DoubleType getSaldoDisponibile() {
		return saldoDisponibile;
	}
	public void setSaldoDisponibile(DoubleType saldoDisponibile) {
		this.saldoDisponibile = saldoDisponibile;
	}
	public DoubleType getSaldoContabile() {
		return saldoContabile;
	}
	public void setSaldoContabile(DoubleType saldoContabile) {
		this.saldoContabile = saldoContabile;
	}
	
}
