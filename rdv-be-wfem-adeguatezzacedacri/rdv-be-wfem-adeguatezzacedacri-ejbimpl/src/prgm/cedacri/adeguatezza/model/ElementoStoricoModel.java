package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
/*
 * Elemento della lista che compone lo storico dei questionari
 */
public class ElementoStoricoModel extends CommandDataModel implements java.io.Serializable
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
	 * release : release del questionario
	 * cluster : cluster del questionario  
//20111002:seValid
     * seValid : scheda valida 
	 */
	private StringType profilo = new StringType();
	private StringType seBanca = new StringType();
	private StringType datComp = new StringType();
	private StringType oraComp = new StringType();
	private StringType canVend = new StringType();
	private StringType filiale = new StringType();
	private StringType userKey = new StringType();
	private IntegerType release = new IntegerType();
	private StringType cluster = new StringType();
	private IntegerType dfinval = new IntegerType();
	private StringType descluster = new StringType();
//20111002:seValid
	private StringType seValid = new StringType();
	/* 20140829 aggiunta Disc */
	private StringType obbtemp	   = new StringType();
	private StringType sitfina	   = new StringType();
	private StringType obbinve	   = new StringType();
	private StringType espfina	   = new StringType();
	/* 20140829 aggiunta Disc */
	
	/*
	 * Metodi Set e Get
	 */
	public StringType getCanVend() {
		return canVend;
	}
	public void setCanVend(StringType canVend) {
		this.canVend = canVend;
	}
	public StringType getDatComp() {
		return datComp;
	}
	public void setDatComp(StringType datComp) {
		this.datComp = datComp;
	}
	public StringType getFiliale() {
		return filiale;
	}
	public void setFiliale(StringType filiale) {
		this.filiale = filiale;
	}
	public StringType getProfilo() {
		return profilo;
	}
	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
	public StringType getSeBanca() {
		return seBanca;
	}
	public void setSeBanca(StringType seBanca) {
		this.seBanca = seBanca;
	}
	public StringType getUserKey() {
		return userKey;
	}
	public void setUserKey(StringType userKey) {
		this.userKey = userKey;
	}
	public StringType getOraComp() {
		return oraComp;
	}
	public void setOraComp(StringType oraComp) {
		this.oraComp = oraComp;
	}
	public IntegerType getRelease() {
		return release;
	}
	public void setRelease(IntegerType release) {
		this.release = release;
	}
	public StringType getCluster() {
		return cluster;
	}
	public void setCluster(StringType cluster) {
		this.cluster = cluster;
	}
	public IntegerType getDfinval() {
		return dfinval;
	}
	public void setDfinval(IntegerType dfinval) {
		this.dfinval = dfinval;
	}
	public StringType getDesCluster() {
		return descluster;
	}
	public void setDesCluster(StringType descluster) {
		this.descluster = descluster;
	}
//20111002:seValid
	public StringType getSeValid() {
		return seValid;
	}
	public void setSeValid(StringType seValid) {
		this.seValid = seValid;
	}
	
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
