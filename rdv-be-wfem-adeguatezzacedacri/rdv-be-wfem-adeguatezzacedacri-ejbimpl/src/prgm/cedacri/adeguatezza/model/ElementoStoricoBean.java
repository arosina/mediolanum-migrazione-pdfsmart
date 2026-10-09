package prgm.cedacri.adeguatezza.model;


/*
 * Elemento della lista che compone lo storico dei questionari
 */
public class ElementoStoricoBean implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Elemento del Storico
	 * profilo : profilo del cliente
	 * seBanca : flag banca
	 * datComp : data di compilazione
	 * canVend : canale di acquisizione
	 * filiale : filiale in cui risiede il cartaceo
	 * userKey : chiave del questionario
	 * release : versione del questionario
	 * cluster : Cluster del questionario 
//20111002:seValid
 	 * seValid : scheda valida 
	 */
	private String profilo = new String();
	private String seBanca = new String();
	private String datComp = new String();
	private String oraComp = new String();
	private String canVend = new String();
	private String filiale = new String();
	private String userKey = new String();
	private String cluster = new String();
	private String descluster = new String();
	private int    dfinval = 0;
	private int    release = 0;
//20111002:seValid
	private String seValid = new String();
	
	/* 20140829 aggiunta Disc */
	private String obbtemp	   = new String();
	private String sitfina	   = new String();
	private String obbinve	   = new String();
	private String espfina	   = new String();
	/* 20140829 aggiunta Disc */
	
	/*
	 * Metodi Set e Get
	 */
	public String getCanVend() {
		return canVend;
	}
	public void setCanVend(String canVend) {
		this.canVend = canVend;
	}
	public String getDatComp() {
		return datComp;
	}
	public void setDatComp(String datComp) {
		this.datComp = datComp;
	}
	public String getFiliale() {
		return filiale;
	}
	public void setFiliale(String filiale) {
		this.filiale = filiale;
	}
	public String getProfilo() {
		return profilo;
	}
	public void setProfilo(String profilo) {
		this.profilo = profilo;
	}
	public String getCluster() {
		return cluster;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public String getDesCluster() {
		return descluster;
	}
	public void setDesCluster(String descluster) {
		this.descluster = descluster;
	}
	public String getSeBanca() {
		return seBanca;
	}
	public void setSeBanca(String seBanca) {
		this.seBanca = seBanca;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getOraComp() {
		return oraComp;
	}
	public void setOraComp(String oraComp) {
		this.oraComp = oraComp;
	}
	public int getRelease() {
		return release;
	}
	public void setRelease(int release) {
		this.release = release;
	}
	public int getDfinval() {
		return dfinval;
	}
	public void setDfinval(int dfinval) {
		this.dfinval = dfinval;
	}
//20111002:seValid
	public String getSeValid() {
		return seValid;
	}
	public void setSeValid(String seValid) {
		this.seValid = seValid;
	}
	
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
