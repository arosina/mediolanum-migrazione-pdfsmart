package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
/*
 * Output Servizio Calcolo Profilo
 */
public class OutputCalcoloProfiloModel extends CommandDataModel implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 * numElem : numero della domanda coinvolta nell'errore
	 */
	private StringType  descErr = new StringType();
	private StringType  esito   = new StringType();
	private IntegerType numElem = new IntegerType();
	/*
	 * Output
	 * rifiuto : (S/N)
	 * seCompi : (S/N)
	 * seInCon : (S/N)
	 * profilo : profilo del cliente
	 * cluster : cluster del cliente
	 * punteggi : punteggi del questionario (classe ElementoPunteggiModel)
	 * risposte : risposte del questionario (classe ElementoQuestionarioRisposteModel)
*v12** esigliq : Esigenza di liquidità
*v12** forzobt : Valore della forzatura dell’obiettivo temporale
*v12** origobt : Obiettivo temporale originale
*v12** origclu : Cluster originale
*v12** desorigclu : descrizione Cluster originale 
	 */
	private StringType rifiuto  = new StringType();
	private StringType seCompi  = new StringType();
	private StringType seInCon  = new StringType();
	private StringType profilo  = new StringType();
	private ListType   punteggi = new ListType();
	private ListType   risposte = new ListType();
	private StringType cluster  = new StringType();
	private StringType descluster  = new StringType();
	private IntegerType dfinval = new IntegerType();
/*v12------------------------------------------------------------*/
	private StringType esigliq  = new StringType();
	private StringType forzobt  = new StringType();
	private StringType origobt  = new StringType();
	private StringType origclu  = new StringType();
	private StringType desorigclu  = new StringType();	
/*v12------------------------------------------------------------*/
	
	/* 20140829 aggiunta Disc */
	private StringType obbtemp	   = new StringType();
	private StringType sitfina	   = new StringType();
	private StringType obbinve	   = new StringType();
	private StringType espfina	   = new StringType();
	/* 20140829 aggiunta Disc */
	
	
	/*
	 * Metodi Set e Get
	 */
	public StringType getDescErr() {
		return descErr;
	}
	public void setDescErr(StringType descErr) {
		this.descErr = descErr;
	}
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public StringType getProfilo() {
		return profilo;
	}
	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
	public StringType getCluster() {
		return cluster;
	}
	public void setCluster(StringType cluster) {
		this.cluster = cluster;
	}
	public StringType getDesCluster() {
		return descluster;
	}
	public void setDesCluster(StringType descluster) {
		this.descluster = descluster;
	}
	public IntegerType getDfinval() {
		return dfinval;
	}
	public void setDfinval(IntegerType dfinval) {
		this.dfinval = dfinval;
	}
	public ListType getPunteggi() {
		return punteggi;
	}
	public void setPunteggi(ListType punteggi) {
		this.punteggi = punteggi;
	}
	public StringType getRifiuto() {
		return rifiuto;
	}
	public void setRifiuto(StringType rifiuto) {
		this.rifiuto = rifiuto;
	}
	public ListType getRisposte() {
		return risposte;
	}
	public void setRisposte(ListType risposte) {
		this.risposte = risposte;
	}
	public StringType getSeCompi() {
		return seCompi;
	}
	public void setSeCompi(StringType seCompi) {
		this.seCompi = seCompi;
	}
	public StringType getSeInCon() {
		return seInCon;
	}
	public void setSeInCon(StringType seInCon) {
		this.seInCon = seInCon;
	}
	public IntegerType getNumElem() {
		return numElem;
	}
	public void setNumElem(IntegerType numElem) {
		this.numElem = numElem;
	}
/*v12------------------------------------------------------------*/
	public StringType getEsigLiq() {
		return esigliq;
	}
	public void setEsigLiq(StringType esigliq) {
		this.esigliq = esigliq;
	}
	public StringType getForzObT() {
		return forzobt;
	}
	public void setForzObT(StringType forzobt) {
		this.forzobt = forzobt;
	}
	public StringType getOrigObT() {
		return origobt;
	}
	public void setOrigObT(StringType origobt) {
		this.origobt = origobt;
	}
	public StringType getOrigClu() {
		return origclu;
	}
	public void setOrigClu(StringType origclu) {
		this.origclu = origclu;
	}
	public StringType getDesOrigClu() {
		return desorigclu;
	}
	public void setDesOrigClu(StringType desorigclu) {
		this.desorigclu = desorigclu;
	}
/*v12------------------------------------------------------------*/
	
	/* 20140829 aggiunta Disc */
	public StringType getEspfina() {
		return espfina;
	}
	public StringType getObbinve() {
		return obbinve;
	}
	public StringType getObbtemp() {
		return obbtemp;
	}
	public StringType getSitfina() {
		return sitfina;
	}
	public void setEspfina(StringType espfina) {
		this.espfina = espfina;
	}
	public void setObbinve(StringType obbinve) {
		this.obbinve = obbinve;
	}
	public void setObbtemp(StringType obbtemp) {
		this.obbtemp = obbtemp;
	}
	public void setSitfina(StringType sitfina) {
		this.sitfina = sitfina;
	}
	/* 20140829 aggiunta Disc */
	
}
