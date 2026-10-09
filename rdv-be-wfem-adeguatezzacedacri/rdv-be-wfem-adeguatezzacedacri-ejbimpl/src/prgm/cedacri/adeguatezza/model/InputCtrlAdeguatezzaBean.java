package prgm.cedacri.adeguatezza.model;
/*
 * Input servizio CtrlAdeguatezza
 */
public class InputCtrlAdeguatezzaBean  implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDossn   : ndg del cliente
	 * ndgTempn   : ndg temporaneo del cliente
	 * filDoss    : filiale dossier
	 * contDoss   : conto dossier
	 * progDoss   : progressivo dossier
	 * canVend    : canale di vendita
	 * country    : sigla paese 3 caratteri usata per log
	 * username   : sigla utente 10 caratteri usata per log
	 * dataRif    : data sottoscrizione operazione 
	 * segnOrd    : tipo operazione
	 * numOrdg    : contatore operazioni
	 * tipStruStr : tipo strumento
	 * sotTipoStr : sottotipo strumento
	 * obbInveStr : obbiettivo di investimento
	 * riscStrStr : profilo di rischio
	 * obbTempStr : orizzonte temporale
	 * modVersStr : modalità di versamento
	 * compAziStr : componente azionaria
	 * codMercStr : mercato di trattazione
	 * obbInveCli : obbiettivo di investimento
	 * obbTempCli : orizzonte temporale
	 * modVersCli : modalità di versamento
	 * sitFinaCli : percentuale rispetto al patrimonio
	 * ctvlord    : controvalore dell'operazione
	 * divisOt    : divisa del controvalore
	 * posDisiCli : possiblita' disinvestimento cliente 
	 * attCons    : Ordine in Consulenza
	 * titCons    : Titolo consulenziabile
	 */
	private String ndgDoss1 = new String();
	private String ndgTemp1 = new String();
	private String ndgDoss2 = new String();
	private String ndgTemp2 = new String();
	private String ndgDoss3 = new String();
	private String ndgTemp3 = new String();
	private String ndgDoss4 = new String();
	private String ndgTemp4 = new String();
	private String filDoss  = new String();
	private String contDoss = new String();
	private String progDoss = new String();
	private String dataRif  = new String();
	private String segnOrd  = new String();
	private String canVend  = new String();
	private int    numOrdg  = 0;
	private String tipStruStr = new String(); 
	private String sotTipoStr = new String(); 
	private String obbInveStr = new String(); 
	private String riscStrStr = new String(); 
	private String obbTempStr = new String(); 
	private String modVersStr = new String(); 
	private String compAziStr = new String(); 
	private String codMercStr = new String(); 
	private String obbInveCli = new String(); 
	private String obbTempCli = new String(); 
	private String modVersCli = new String(); 
	private String sitFinaCli = new String(); 
	private int    ctvlord    = 0; 
	private String divisOt    = new String();
	private String posDisiCli = new String();
	private String attCons    = new String();
	private String titCons    = new String();
	private String country    = new String();
	private String username   = new String();
	
	public String getCanVend() {
		return canVend;
	}
	public void setCanVend(String canVend) {
		this.canVend = canVend;
	}
	public String getCodMercStr() {
		return codMercStr;
	}
	public void setCodMercStr(String codMercStr) {
		this.codMercStr = codMercStr;
	}
	public String getCompAziStr() {
		return compAziStr;
	}
	public void setCompAziStr(String compAziStr) {
		this.compAziStr = compAziStr;
	}
	public int getCtvlord() {
		return ctvlord;
	}
	public void setCtvlord(int ctvlord) {
		this.ctvlord = ctvlord;
	}
	public String getDataRif() {
		return dataRif;
	}
	public void setDataRif(String dataRif) {
		this.dataRif = dataRif;
	}
	public String getDivisOt() {
		return divisOt;
	}
	public void setDivisOt(String divisOt) {
		this.divisOt = divisOt;
	}
	public String getModVersCli() {
		return modVersCli;
	}
	public void setModVersCli(String modVersCli) {
		this.modVersCli = modVersCli;
	}
	public String getModVersStr() {
		return modVersStr;
	}
	public void setModVersStr(String modVersStr) {
		this.modVersStr = modVersStr;
	}
	public String getNdgDoss1() {
		return ndgDoss1;
	}
	public void setNdgDoss1(String ndgDoss1) {
		this.ndgDoss1 = ndgDoss1;
	}
	public String getNdgDoss2() {
		return ndgDoss2;
	}
	public void setNdgDoss2(String ndgDoss2) {
		this.ndgDoss2 = ndgDoss2;
	}
	public String getNdgDoss3() {
		return ndgDoss3;
	}
	public void setNdgDoss3(String ndgDoss3) {
		this.ndgDoss3 = ndgDoss3;
	}
	public String getNdgDoss4() {
		return ndgDoss4;
	}
	public void setNdgDoss4(String ndgDoss4) {
		this.ndgDoss4 = ndgDoss4;
	}
	public int getNumOrdg() {
		return numOrdg;
	}
	public void setNumOrdg(int numOrdg) {
		this.numOrdg = numOrdg;
	}
	public String getObbInveCli() {
		return obbInveCli;
	}
	public void setObbInveCli(String obbInveCli) {
		this.obbInveCli = obbInveCli;
	}
	public String getObbInveStr() {
		return obbInveStr;
	}
	public void setObbInveStr(String obbInveStr) {
		this.obbInveStr = obbInveStr;
	}
	public String getObbTempCli() {
		return obbTempCli;
	}
	public void setObbTempCli(String obbTempCli) {
		this.obbTempCli = obbTempCli;
	}
	public String getObbTempStr() {
		return obbTempStr;
	}
	public void setObbTempStr(String obbTempStr) {
		this.obbTempStr = obbTempStr;
	}
	public String getRiscStrStr() {
		return riscStrStr;
	}
	public void setRiscStrStr(String riscStrStr) {
		this.riscStrStr = riscStrStr;
	}
	public String getSegnOrd() {
		return segnOrd;
	}
	public void setSegnOrd(String segnOrd) {
		this.segnOrd = segnOrd;
	}
	public String getSitFinaCli() {
		return sitFinaCli;
	}
	public void setSitFinaCli(String sitFinaCli) {
		this.sitFinaCli = sitFinaCli;
	}
	public String getSotTipoStr() {
		return sotTipoStr;
	}
	public void setSotTipoStr(String sotTipoStr) {
		this.sotTipoStr = sotTipoStr;
	}
	public String getTipStruStr() {
		return tipStruStr;
	}
	public void setTipStruStr(String tipStruStr) {
		this.tipStruStr = tipStruStr;
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
	public String getNdgTemp1() {
		return ndgTemp1;
	}
	public void setNdgTemp1(String ndgTemp1) {
		this.ndgTemp1 = ndgTemp1;
	}
	public String getNdgTemp2() {
		return ndgTemp2;
	}
	public void setNdgTemp2(String ndgTemp2) {
		this.ndgTemp2 = ndgTemp2;
	}
	public String getNdgTemp3() {
		return ndgTemp3;
	}
	public void setNdgTemp3(String ndgTemp3) {
		this.ndgTemp3 = ndgTemp3;
	}
	public String getNdgTemp4() {
		return ndgTemp4;
	}
	public void setNdgTemp4(String ndgTemp4) {
		this.ndgTemp4 = ndgTemp4;
	}
	public String getContDoss() {
		return contDoss;
	}
	public String getFilDoss() {
		return filDoss;
	}
	public String getProgDoss() {
		return progDoss;
	}
	public void setContDoss(String contDoss) {
		this.contDoss = contDoss;
	}
	public void setFilDoss(String filDoss) {
		this.filDoss = filDoss;
	}
	public void setProgDoss(String progDoss) {
		this.progDoss = progDoss;
	} 
	public void setPosDisiCli(String posDisiCli) {
		this.posDisiCli = posDisiCli;
	} 	
	public String getPosDisiCli() {
		return posDisiCli;
	}
	public void setAttCons(String attCons) {
		this.attCons = attCons;
	} 	
	public String getAttCons() {
		return attCons;
	}
	public void setTitCons(String titCons) {
		this.titCons = titCons;
	} 	
	public String getTitCons() {
		return titCons;
	}
}
