package prgm.cedacri.adeguatezza.model;
/*
 * Output Servizio Lettura Ultimo Questionario 
 */
public class OutputGetQuestionarioBean implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 */
	private String descErr = new String();
	private String esito   = new String();
	
	/*
	 * Output
	 * seCompi : flag che indica se il questionario è stato compilato
	 * release : versione del questionario
	 * questionario : contenuto del questionario (classe ElementoQuestionario)
	 * datComp : data di compilazione (solo seCompi = 'S')
	 * oraComp : ora di compilazione (solo seCompi = 'S')
	 * profilo : profilo precedente (solo seCompi = 'S')
	 * seBanca : indica se il profilo è stato attribuito d'ufficio
     * cluster : cluster precedente (solo seCompi = 'S')
*v12** esigliq : Esigenza di liquidità
*v12** forzobt : Esigenza di liquidità
*v12** origobt : Obiettivo temporale originale
*v12** origclu : Cluster originale
*v12** desorigclu : descrizione Cluster originale
     * dfinval : data fine validità
//20111002:seValid
 	 * seValid : scheda valida 
	 */
	private String seCompi      = new String();
	private int    release      = 0;
	private ElementoQuestionarioBean[] questionario = null;
	private String datComp      = new String();
	private String oraComp      = new String();
	private String profilo      = new String();
	private String cluster      = new String();
	private String seBanca      = new String();
	private String descluster   = new String();
/*v12-----------------------------------------------------------------*/
	private String esigliq	   = new String();
	private String forzobt	   = new String();
	private String origobt	   = new String();
	private String origclu	   = new String();
	private String desorigclu   = new String();
/*v12-----------------------------------------------------------------*/
	private int dfinval         = 0;
//	20111002:seValid
	private String seValid      = new String();
	
	/* 20140829 aggiunta Disc */
	private String obbtemp	   = new String();
	private String sitfina	   = new String();
	private String obbinve	   = new String();
	private String espfina	   = new String();
	/* 20140829 aggiunta Disc */
	
	
	public String getDatComp() {
		return datComp;
	}
	public void setDatComp(String datComp) {
		this.datComp = datComp;
	}
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
	public String getOraComp() {
		return oraComp;
	}
	public void setOraComp(String oraComp) {
		this.oraComp = oraComp;
	}
	public String getProfilo() {
		return profilo;
	}
	public void setProfilo(String profilo) {
		this.profilo = profilo;
	}
	public ElementoQuestionarioBean[] getQuestionario() {
		return questionario;
	}
	public void setQuestionario(ElementoQuestionarioBean[] questionario) {
		this.questionario = questionario;
	}
	public int getRelease() {
		return release;
	}
	public void setRelease(int release) {
		this.release = release;
	}
	public String getSeCompi() {
		return seCompi;
	}
	public void setSeCompi(String seCompi) {
		this.seCompi = seCompi;
	}
	public String getSeBanca() {
		return seBanca;
	}
	public void setSeBanca(String seBanca) {
		this.seBanca = seBanca;
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
	public int getDfinval() {
		return dfinval;
	}
	public void setDfinval(int dfinval) {
		this.dfinval = dfinval;
	}
//	20111002:seValid
	public String getSeValid() {
		return seValid;
	}
	public void setSeValid(String seValid) {
		this.seValid = seValid;
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
