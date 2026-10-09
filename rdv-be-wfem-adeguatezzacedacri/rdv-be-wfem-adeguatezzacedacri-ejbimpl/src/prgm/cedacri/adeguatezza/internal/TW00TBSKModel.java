package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class TW00TBSKModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private IntegerType codisti = new IntegerType();
	private StringType  codappl = new StringType();
	private	StringType  ndgdoss = new StringType();
	private StringType  ndgtemp = new StringType();
	private	IntegerType dfinval = new IntegerType();
	private	IntegerType giosche = new IntegerType();
	private	TimestampType datsche = new TimestampType();
	private	StringType  schedal = new StringType();
	private	StringType  profilo = new StringType();
	private	StringType  risclie = new StringType();
	private	StringType  espfina = new StringType();
	private	StringType  obbinve = new StringType();
	private	StringType  obbtemp = new StringType();
	private	StringType  sitfina = new StringType();
	private	StringType  flskcli = new StringType();
	private	IntegerType filoper = new IntegerType();
	private	StringType  canvend = new StringType();
	private	StringType  sevalid = new StringType();
	private IntegerType release = new IntegerType();
	private StringType  numsche = new StringType();
	private	StringType  cluster = new StringType();
/*v12------------------------------------------------------------*/
	private StringType	esigliq = new StringType();
	private StringType	forzobt = new StringType();
	private StringType	origobt = new StringType();
	private StringType	origclu = new StringType();
/*v12------------------------------------------------------------*/
	
	public StringType getCanvend() {
		return canvend;
	}
	public void setCanvend(StringType canvend) {
		this.canvend = canvend;
	}
	public StringType getCodappl() {
		return codappl;
	}
	public void setCodappl(StringType codappl) {
		this.codappl = codappl;
	}
	public IntegerType getCodisti() {
		return codisti;
	}
	public void setCodisti(IntegerType codisti) {
		this.codisti = codisti;
	}
	public TimestampType getDatsche() {
		return datsche;
	}
	public void setDatsche(TimestampType datsche) {
		this.datsche = datsche;
	}
	public IntegerType getDfinval() {
		return dfinval;
	}
	public void setDfinval(IntegerType dfinval) {
		this.dfinval = dfinval;
	}
	public StringType getEspfina() {
		return espfina;
	}
	public void setEspfina(StringType espfina) {
		this.espfina = espfina;
	}
	public IntegerType getFiloper() {
		return filoper;
	}
	public void setFiloper(IntegerType filoper) {
		this.filoper = filoper;
	}
	public StringType getFlskcli() {
		return flskcli;
	}
	public void setFlskcli(StringType flskcli) {
		this.flskcli = flskcli;
	}
	public IntegerType getGiosche() {
		return giosche;
	}
	public void setGiosche(IntegerType giosche) {
		this.giosche = giosche;
	}
	public StringType getNdgdoss() {
		return ndgdoss;
	}
	public void setNdgdoss(StringType ndgdoss) {
		this.ndgdoss = ndgdoss;
	}
	public StringType getNdgtemp() {
		return ndgtemp;
	}
	public void setNdgtemp(StringType ndgtemp) {
		this.ndgtemp = ndgtemp;
	}
	public StringType getObbinve() {
		return obbinve;
	}
	public void setObbinve(StringType obbinve) {
		this.obbinve = obbinve;
	}
	public StringType getObbtemp() {
		return obbtemp;
	}
	public void setObbtemp(StringType obbtemp) {
		this.obbtemp = obbtemp;
	}
	public StringType getProfilo() {
		return profilo;
	}
	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
	public IntegerType getRelease() {
		return release;
	}
	public void setRelease(IntegerType release) {
		this.release = release;
	}
	public StringType getRisclie() {
		return risclie;
	}
	public void setRisclie(StringType risclie) {
		this.risclie = risclie;
	}
	public StringType getSchedal() {
		return schedal;
	}
	public void setSchedal(StringType schedal) {
		this.schedal = schedal;
	}
	public StringType getSevalid() {
		return sevalid;
	}
	public void setSevalid(StringType sevalid) {
		this.sevalid = sevalid;
	}
	public StringType getSitfina() {
		return sitfina;
	}
	public void setSitfina(StringType sitfina) {
		this.sitfina = sitfina;
	}
	public StringType getNumsche() {
		return numsche;
	}
	public void setNumsche(StringType numsche) {
		this.numsche = numsche;
	}
	public StringType getCluster() {
		return cluster;
	}
	public void setCluster(StringType cluster) {
		this.cluster = cluster;
	}
/*v12------------------------------------------------------------*/
	public StringType getEsigliq() {
		return esigliq;
	}
	public void setEsigliq(StringType esigliq) {
		this.esigliq = esigliq;
	}
	public StringType getForzobt() {
		return forzobt;
	}
	public void setForzobt(StringType forzobt) {
		this.forzobt = forzobt;
	}
	public StringType getOrigobt() {
		return origobt;
	}
	public void setOrigobt(StringType origobt) {
		this.origobt = origobt;
	}
	public StringType getOrigclu() {
		return origclu;
	}
	public void setOrigclu(StringType origclu) {
		this.origclu = origclu;
	}
/*v12------------------------------------------------------------*/
	
}
