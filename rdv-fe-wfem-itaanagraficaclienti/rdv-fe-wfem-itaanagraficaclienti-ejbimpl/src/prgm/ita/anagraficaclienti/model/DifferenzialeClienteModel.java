package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class DifferenzialeClienteModel extends CommandDataModel  {
	
	private StringType codiceDifferenziale = new StringType();
	private IntegerType valoreDifferenziale = new IntegerType();
	private StringType flagLeva = new StringType();
	private IntegerType nMesiDurataDifferenziale = new IntegerType();
	
	public StringType getCodiceDifferenziale() {
		return codiceDifferenziale;
	}
	public void setCodiceDifferenziale(StringType codiceDifferenziale) {
		this.codiceDifferenziale = codiceDifferenziale;
	}
	public IntegerType getValoreDifferenziale() {
		return valoreDifferenziale;
	}
	public void setValoreDifferenziale(IntegerType valoreDifferenziale) {
		this.valoreDifferenziale = valoreDifferenziale;
	}
	public StringType getFlagLeva() {
		return flagLeva;
	}
	public void setFlagLeva(StringType flagLeva) {
		this.flagLeva = flagLeva;
	}
	public IntegerType getnMesiDurataDifferenziale() {
		return nMesiDurataDifferenziale;
	}
	public void setnMesiDurataDifferenziale(IntegerType nMesiDurataDifferenziale) {
		this.nMesiDurataDifferenziale = nMesiDurataDifferenziale;
	}

}
