package prgm.cedacri.adeguatezza.model;
/*
 * Input Servizio AggiornaPatrimonio
 */
public class InputAggiornaPatrimonioBean implements java.io.Serializable
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
	private String ndgDoss  = new String();
	private String ndgTemp  = new String();
	private String canVend  = new String();
	private String dataOraComp = new String();
	private String country  = new String();
	private String username = new String();
	private String numSched = new String();   
	private int importo1 = 0;
	private int importo2 = 0;
	private int importo3 = 0;
	private int importo4 = 0;
	private int importo5 = 0;
	private int importo6 = 0;
	private int importo7 = 0;
	private int importo8 = 0;
	private int importo9 = 0;
	/*
	 * Metodi Set e Get
	 */
	public String getNdgDoss() {
		return ndgDoss;
	}
	public void setNdgDoss(String ndgDoss) {
		this.ndgDoss = ndgDoss;
	}
	public String getCanVend() {
		return canVend;
	}
	public void setCanVend(String canVend) {
		this.canVend = canVend;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNdgTemp() {
		return ndgTemp;
	}
	public void setNdgTemp(String ndgTemp) {
		this.ndgTemp = ndgTemp;
	}
	public String getDataOraComp() {
		return dataOraComp;
	}
	public void setDataOraComp(String dataOraComp) {
		this.dataOraComp = dataOraComp;
	}
	public String getNumSched() {
		return numSched;
	}
	public void setNumSched(String numSched) {
		this.numSched = numSched;
	}
	public int getImporto1() {
		return importo1;
	}
	public void setImporto1(int importo1) {
		this.importo1 = importo1;
	}
	public int getImporto2() {
		return importo2;
	}
	public void setImporto2(int importo2) {
		this.importo2 = importo2;
	}
	public int getImporto3() {
		return importo3;
	}
	public void setImporto3(int importo3) {
		this.importo3 = importo3;
	}
	public int getImporto4() {
		return importo4;
	}
	public void setImporto4(int importo4) {
		this.importo4 = importo4;
	}
	public int getImporto5() {
		return importo5;
	}
	public void setImporto5(int importo5) {
		this.importo5 = importo5;
	}
	public int getImporto6() {
		return importo6;
	}
	public void setImporto6(int importo6) {
		this.importo6 = importo6;
	}
	public int getImporto7() {
		return importo7;
	}
	public void setImporto7(int importo7) {
		this.importo7 = importo7;
	}
	public int getImporto8() {
		return importo8;
	}
	public void setImporto8(int importo8) {
		this.importo8 = importo8;
	}
	public int getImporto9() {
		return importo9;
	}
	public void setImporto9(int importo9) {
		this.importo9 = importo9;
	}

}
