package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
/*
 * Input Servizio AggiornaPatrimonio
 */
public class InputAggiornaPatrimonioModel extends CommandDataModel implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDoss : ndg del cliente
	 * ndgTemp : ndg temporaneo del cliente
	 * canVend : canale di vendita
	 * dataOraComp : data e ora di compilazione in formato GG-MM-AAAA HH:MM:SS
	 * country  : sigla paese 3 caratteri usata per log
	 * username : sigla utente 10 caratteri usata per log
	 * importo1 : importo 1
	 * importo2 : importo 2
	 * importo3 : importo 3
	 * importo4 : importo 4
	 * importo5 : importo 5
	 * importo6 : importo 6
	 * importo7 : importo 7
	 * importo8 : importo 8
	 * importo9 : importo 9
	 */
	private StringType  ndgDoss  = new StringType();
	private StringType  ndgTemp  = new StringType();
	private StringType  canVend  = new StringType();
	private TimestampType dataOraComp = new TimestampType(); 
	private StringType  country  = new StringType();
	private StringType  username = new StringType();
	private StringType  numSched = new StringType();   
	private IntegerType  importo1 = new IntegerType();
	private IntegerType  importo2 = new IntegerType();
	private IntegerType  importo3 = new IntegerType();
	private IntegerType  importo4 = new IntegerType();
	private IntegerType  importo5 = new IntegerType();
	private IntegerType  importo6 = new IntegerType();
	private IntegerType  importo7 = new IntegerType();
	private IntegerType  importo8 = new IntegerType();
	private IntegerType  importo9 = new IntegerType();
	/*
	 * Metodi Set e Get
	 */
	public StringType getNdgDoss() {
		return ndgDoss;
	}
	public void setNdgDoss(StringType ndgDoss) {
		this.ndgDoss = ndgDoss;
	}
	public StringType getCanVend() {
		return canVend;
	}
	public void setCanVend(StringType canVend) {
		this.canVend = canVend;
	}
	public StringType getCountry() {
		return country;
	}
	public void setCountry(StringType country) {
		this.country = country;
	}
	public StringType getUsername() {
		return username;
	}
	public void setUsername(StringType username) {
		this.username = username;
	}
	public StringType getNdgTemp() {
		return ndgTemp;
	}
	public void setNdgTemp(StringType ndgTemp) {
		this.ndgTemp = ndgTemp;
	}
	public TimestampType getDataOraComp() {
		return dataOraComp;
	}
	public void setDataOraComp(TimestampType dataOraComp) {
		this.dataOraComp = dataOraComp;
	}
	public StringType getNumSched() {
		return numSched;
	}
	public void setNumSched(StringType numSched) {
		this.numSched = numSched;
	}
	public IntegerType getImporto1() {
		return importo1;
	}
	public void setImporto1(IntegerType importo1) {
		this.importo1 = importo1;
	}
	public IntegerType getImporto2() {
		return importo2;
	}
	public void setImporto2(IntegerType importo2) {
		this.importo2 = importo2;
	}
	public IntegerType getImporto3() {
		return importo3;
	}
	public void setImporto3(IntegerType importo3) {
		this.importo3 = importo3;
	}
	public IntegerType getImporto4() {
		return importo4;
	}
	public void setImporto4(IntegerType importo4) {
		this.importo4 = importo4;
	}
	public IntegerType getImporto5() {
		return importo5;
	}
	public void setImporto5(IntegerType importo5) {
		this.importo5 = importo5;
	}
	public IntegerType getImporto6() {
		return importo6;
	}
	public void setImporto6(IntegerType importo6) {
		this.importo6 = importo6;
	}
	public IntegerType getImporto7() {
		return importo7;
	}
	public void setImporto7(IntegerType importo7) {
		this.importo7 = importo7;
	}
	public IntegerType getImporto8() {
		return importo8;
	}
	public void setImporto8(IntegerType importo8) {
		this.importo8 = importo8;
	}
	public IntegerType getImporto9() {
		return importo9;
	}
	public void setImporto9(IntegerType importo9) {
		this.importo9 = importo9;
	}
}
