package prgm.cedacri.adeguatezza.model;
/*
 * Output Servizio Calcolo Profilo
 */
public class OutputCalcoloProfiloBean implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 * numElem : numero della domanda coinvolta nell'errore
	 */
	private String descErr = new String();
	private String esito   = new String();
	private int    numElem = 0;
	/*
	 * Output
	 * rifiuto : (S/N)
	 * seCompi : (S/N)
	 * seInCon : (S/N)
	 * profilo : profilo del cliente
	 * cluster : cluster del cliente
	 * punteggi : punteggi del questionario (classe ElementoPunteggi)
	 * risposte : risposte del questionario (classe ElementoQuestionarioRisposte)
*v12** esigliq : Esigenza di liquidità
*v12** forzobt : Esigenza di liquidità
*v12** origobt : Obiettivo temporale originale
*v12** origclu : Cluster originale
*v12** desorigclu : descrizione Cluster originale 
	 */
	private String rifiuto  = new String();
	private String seCompi  = new String();
	private String seInCon  = new String();
	private String profilo  = new String();
	private String cluster  = new String();
	private String descluster  = new String();
	private int dfinval     = 0;
/*v12-----------------------------------------------------------------*/
	private String esigliq  = new String();
	private String forzobt  = new String();
	private String origobt  = new String();
	private String origclu  = new String();
	private String desorigclu  = new String(); 
/*v12-----------------------------------------------------------------*/
	private ElementoPunteggiBean[] punteggi = null;
	private ElementoQuestionarioRisposteBean[] risposte = null;
	
	/* 20140829 aggiunta Disc */
	private String obbtemp	   = new String();
	private String sitfina	   = new String();
	private String obbinve	   = new String();
	private String espfina	   = new String();
	/* 20140829 aggiunta Disc */
	
	/*
	 * Metodi Set e Get
	 */
	public String getDescErr() {
		return descErr;
	}
	public void setDescErr(String descErr) {
		this.descErr = descErr;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
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
/*v12-----------------------------------------------------------------*/
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
	public String getDesOrigClu() {
		return desorigclu;
	}
	public void setDesOrigClu(String desorigclu) {
		this.desorigclu = desorigclu;
	}
/*v12-----------------------------------------------------------------*/
	public int getDfinval() {
		return dfinval;
	}
	public void setDfinval(int dfinval) {
		this.dfinval = dfinval;
	}
	public ElementoPunteggiBean[] getPunteggi() {
		return punteggi;
	}
	public void setPunteggi(ElementoPunteggiBean[] punteggi) {
		this.punteggi = punteggi;
	}
	public String getRifiuto() {
		return rifiuto;
	}
	public void setRifiuto(String rifiuto) {
		this.rifiuto = rifiuto;
	}
	public ElementoQuestionarioRisposteBean[] getRisposte() {
		return risposte;
	}
	public void setRisposte(ElementoQuestionarioRisposteBean[] risposte) {
		this.risposte = risposte;
	}
	public String getSeCompi() {
		return seCompi;
	}
	public void setSeCompi(String seCompi) {
		this.seCompi = seCompi;
	}
	public String getSeInCon() {
		return seInCon;
	}
	public void setSeInCon(String seInCon) {
		this.seInCon = seInCon;
	}
	public int getNumElem() {
		return numElem;
	}
	public void setNumElem(int numElem) {
		this.numElem = numElem;
	}
	
	/* 20140829 aggiunta Disc */
	public String getEspfina() {
		return espfina;
	}
	public String getObbinve() {
		return obbinve;
	}
	public String getObbtemp() {
		return obbtemp;
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
	public void setObbtemp(String obbtemp) {
		this.obbtemp = obbtemp;
	}
	public void setSitfina(String sitfina) {
		this.sitfina = sitfina;
	}
	/* 20140829 aggiunta Disc */

}