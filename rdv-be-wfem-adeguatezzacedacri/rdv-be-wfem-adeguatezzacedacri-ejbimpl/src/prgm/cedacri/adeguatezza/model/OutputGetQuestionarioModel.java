package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
/*
 * Output Servizio Lettura Ultimo Questionario 
 */
public class OutputGetQuestionarioModel extends CommandDataModel implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 */
	private StringType descErr = new StringType();
	private StringType esito   = new StringType();
	
	/*
	 * Output
	 * seCompi : flag che indica se il questionario è stato compilato
	 * release : versione del questionario
	 * questionario : contenuto del questionario (classe ElementoQuestionarioModel)
	 * datComp : data di compilazione (solo seCompi = 'S')
	 * oraComp : ora di compilazione (solo seCompi = 'S')
	 * profilo : profilo precedente (solo seCompi = 'S')
	 * cluster : cluster precedente (solo seCompi = 'S') 
	 * dfinval : data fine validità (solo seCompi = 'S')
	 * seBanca : indica se il profilo è stato attribuito d'ufficio
//20111002:seValid
     * seValid : scheda valida
*v12** esigliq : esigenza di liquidità
*v12** forzobt : Valore della forzatura dell’obiettivo temporale
*v12** origobt : Obiettivo temporale originale
*v12** origclu : cluster originale
*v12** desorigclu : descrizione cluster originale
     * 
	 */
	private StringType seCompi      = new StringType();
	private IntegerType release     = new IntegerType();
	private ListType   questionario = new ListType();
	private StringType datComp      = new StringType();
	private StringType oraComp      = new StringType();
	private StringType profilo      = new StringType();
	private StringType seBanca      = new StringType();
	private StringType cluster      = new StringType();
	private StringType descluster   = new StringType();
/*v12-------------------------------------------------------------*/
	private StringType esigliq	   = new StringType();
	private StringType forzobt	   = new StringType();
	private StringType origobt	   = new StringType();
	private StringType origclu	   = new StringType();
	private StringType desorigclu   = new StringType();	
/*v12-------------------------------------------------------------*/	
	private IntegerType dfinval     = new IntegerType();
//20111002:seValid
	private StringType seValid      = new StringType();
	
	/* 20140829 aggiunta Disc */
	private StringType obbtemp	   = new StringType();
	private StringType sitfina	   = new StringType();
	private StringType obbinve	   = new StringType();
	private StringType espfina	   = new StringType();
	/* 20140829 aggiunta Disc */
	
	/*
	 * Metodi Set e Get
	 */
	public StringType getDatComp() {
		return datComp;
	}
	public void setDatComp(StringType datComp) {
		this.datComp = datComp;
	}
	public StringType getOraComp() {
		return oraComp;
	}
	public void setOraComp(StringType oraComp) {
		this.oraComp = oraComp;
	}
	public StringType getProfilo() {
		return profilo;
	}
	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
	public ListType getQuestionario() {
		return questionario;
	}
	public void setQuestionario(ListType questionario) {
		this.questionario = questionario;
	}
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
	public ListType getElementoQuestionario() {
		return questionario;
	}
	public void setElementoQuestionario(ListType questionario) {
		this.questionario = questionario;
	}
	public IntegerType getRelease() {
		return release;
	}
	public void setRelease(IntegerType release) {
		this.release = release;
	}
	public StringType getSeCompi() {
		return seCompi;
	}
	public void setSeCompi(StringType seCompi) {
		this.seCompi = seCompi;
	}
	public StringType getSeBanca() {
		return seBanca;
	}
	public void setSeBanca(StringType seBanca) {
		this.seBanca = seBanca;
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
//20111002:seValid
	public StringType getSeValid() {
		return seValid;
	}
	public void setSeValid(StringType seValid) {
		this.seValid = seValid;
	}
/*v12----------------------------------------------------*/
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
/*v12----------------------------------------------------*/	
	
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
