package prgm.cedacri.adeguatezza.model;
/*
 * Input Servizio Salva Questionario
 */
public class InputSalvaQuestionarioBean implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDoss : ndg del cliente
	 * ndgTemp : ndg temporaneo del cliente
	 * canVend : canale di vendita
	 * profilo : profilo del cliente
	 * risposte: risposte del questionario (classe ElementoQuestionarioRisposteBin)
	 * bozza   : se si vuole salvare in bozza
	 * eta      : eta del cliente per il calcolo del profilo di default
	 * titStud  : titolo di studio del cliente per il calcolo del profilo di default
	 * dataOraComp : data e ora di compilazione in formato GG-MM-AAAA HH:MM:SS
	 * country  : sigla paese 3 caratteri usata per log
	 * username : sigla utente 10 caratteri usata per log
	 * cluster : cluster del cliente
	 * pg : persona giuridica
*v12** esigliq : Esigenza di liquidità
*v12** forzobt : Valore della forzatura dell’obiettivo temporale
*v12** origobt : Obiettivo temporale originale
*v12** origclu : Cluster originale
	 */
	private String ndgDoss  = new String();
	private String ndgTemp  = new String();
	private String canVend  = new String();
	private String bozza    = new String();
	private String profilo  = new String();
	private ElementoQuestionarioRisposteBean[] risposte = null;
	private int    eta      = 0;
	private String titStud  = new String();
	private String dataOraComp = new String();
	private String country  = new String();
	private String username = new String();
	private String numSched = new String();   
	private String cluster  = new String();
	private String pg  = new String();
/*v12------------------------------------------------------------------*/
	private String esigliq	= new String();
	private String forzobt	= new String();
	private String origobt	= new String();
	private String origclu	= new String();
/*v12------------------------------------------------------------------*/
	
	/* 20140829 aggiunta Disc */
	private String obbtemp	   = new String();
	private String sitfina	   = new String();
	private String obbinve	   = new String();
	private String espfina	   = new String();
	/* 20140829 aggiunta Disc */
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
	public String getProfilo() {
		return profilo;
	}
	public void setProfilo(String profilo) {
		this.profilo = profilo;
	}
	public ElementoQuestionarioRisposteBean[] getRisposte() {
		return risposte;
	}
	public void setRisposte(ElementoQuestionarioRisposteBean[] risposte) {
		this.risposte = risposte;
	}
	public String getBozza() {
		return bozza;
	}
	public void setBozza(String bozza) {
		this.bozza = bozza;
	}
	public int getEta() {
		return eta;
	}
	public void setEta(int eta) {
		this.eta = eta;
	}
	public String getTitStud() {
		return titStud;
	}
	public void setTitStud(String titStud) {
		this.titStud = titStud;
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
	public String getCluster() {
		return cluster;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public String getPG() {
		return pg;
	}
	public void setPG(String pg) {
		this.pg = pg;
	}
/*v12------------------------------------------------------------------*/
	public String getEsigLiq() {
		return esigliq;
	}
	public void setEsigLiq(String esigliq) {
		this.esigliq = esigliq;
	}
	public String getForzObT() {
		return forzobt;
	}
	public void setForzObT(String forzobt) {
		this.forzobt = forzobt;
	}
	public String getOrigObT() {
		return origobt;
	}
	public void setOrigObT(String origobt) {
		this.origobt = origobt;
	}
	public String getOrigClu() {
		return origclu;
	}
	public void setOrigClu(String origclu) {
		this.origclu = origclu;
	}
/*v12------------------------------------------------------------------*/
	
	/* 20140829 aggiunta Disc */
	public String getObbtemp() {
		return obbtemp;
	}
	public void setObbtemp(String obbtemp) {
		this.obbtemp = obbtemp;
	}
	public String getEspfina() {
		return espfina;
	}
	public String getObbinve() {
		return obbinve;
	}
	public String getSitfina() {
		return sitfina;
	}
	public void setEspfina(String espfina) {
		this.espfina = espfina;
	}
	public void setObbinve(String obbinve) {
		this.obbinve = obbinve;
	}
	public void setSitfina(String sitfina) {
		this.sitfina = sitfina;
	}
	/* 20140829 aggiunta Disc */
}
