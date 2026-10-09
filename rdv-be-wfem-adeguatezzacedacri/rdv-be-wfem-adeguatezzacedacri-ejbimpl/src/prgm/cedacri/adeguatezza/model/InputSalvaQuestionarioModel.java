package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
/*
 * Input Servizio Salva Questionario
 */
public class InputSalvaQuestionarioModel extends CommandDataModel implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDoss : ndg del cliente
	 * ndgTemp : ndg temporaneo del cliente
	 * canVend : canale di vendita
	 * profilo : profilo del cliente
	 * risposte: risposte del questionario (classe ElementoQuestionarioRisposteModel)
	 * eta      : eta del cliente per il calcolo del profilo di default
	 * titStud  : titolo di studio del cliente per il calcolo del profilo di default
	 * dataOraComp : data e ora di compilazione in formato GG-MM-AAAA HH:MM:SS
	 * country  : sigla paese 3 caratteri usata per log
	 * username : sigla utente 10 caratteri usata per log
	 * cluster : cluster del cliente
	 * pg : persona giuridica
*v12** esigliq : Esigenza di liquidità
*v12** forzobt : Esigenza di liquidità
*v12** origobt : Obiettivo temporale originale
*v12** origclu : Cluster originale
	 */
	private StringType  ndgDoss  = new StringType();
	private StringType  ndgTemp  = new StringType();
	private StringType  canVend  = new StringType();
	private StringType  bozza    = new StringType();
	private StringType  profilo  = new StringType();
	private ListType    risposte = new ListType();
	private IntegerType eta      = new IntegerType();
	private StringType  titStud  = new StringType();
	private TimestampType dataOraComp = new TimestampType(); 
	private StringType  country  = new StringType();
	private StringType  username = new StringType();
	private StringType  numSched = new StringType();   
	private StringType  cluster  = new StringType();
	private StringType  pg       = new StringType();
/*v12-----------------------------------------------------------------*/
	private StringType	esigliq	 = new StringType();
	private StringType	forzobt	 = new StringType();
	private StringType	origobt	 = new StringType();
	private StringType	origclu	 = new StringType();
/*v12-----------------------------------------------------------------*/
	
	/* 20140829 aggiunta Disc */
	private StringType obbtemp	   = new StringType();
	private StringType sitfina	   = new StringType();
	private StringType obbinve	   = new StringType();
	private StringType espfina	   = new StringType();
	/* 20140829 aggiunta Disc */
	
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
	public StringType getProfilo() {
		return profilo;
	}
	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
	public ListType getRisposte() {
		return risposte;
	}
	public void setRisposte(ListType risposte) {
		this.risposte = risposte;
	}
	public StringType getBozza() {
		return bozza;
	}
	public void setBozza(StringType bozza) {
		this.bozza = bozza;
	}
	public IntegerType getEta() {
		return eta;
	}
	public void setEta(IntegerType eta) {
		this.eta = eta;
	}
	public StringType getTitStud() {
		return titStud;
	}
	public void setTitStud(StringType titStud) {
		this.titStud = titStud;
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
	public StringType getCluster() {
		return cluster;
	}
	public void setCluster(StringType cluster) {
		this.cluster = cluster;
	}
	public StringType getPG() {
		return pg;
	}
	public void setPG(StringType pg) {
		this.pg = pg;
	}
/*v12-----------------------------------------------------------------*/
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
/*v12-----------------------------------------------------------------*/
	
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
