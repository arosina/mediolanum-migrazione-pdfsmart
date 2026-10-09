package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class OutputDefaultProfiloModel extends CommandDataModel {
	private static final long serialVersionUID = 0;

	private StringType chiave = new StringType();
	private StringType profilo = new StringType();
	private StringType obbInve = new StringType();
	private StringType obbTemp = new StringType();
	private StringType sitFina = new StringType();
	private StringType cluster = new StringType();
	private StringType descluster = new StringType();
	private IntegerType dfinval = new IntegerType();
	
	public StringType getChiave() {
		return chiave;
	}
	public void setChiave(StringType chiave) {
		this.chiave = chiave;
	}
	public StringType getObbInve() {
		return obbInve;
	}
	public void setObbInve(StringType obbInve) {
		this.obbInve = obbInve;
	}
	public StringType getObbTemp() {
		return obbTemp;
	}
	public void setObbTemp(StringType obbTemp) {
		this.obbTemp = obbTemp;
	}
	public StringType getProfilo() {
		return profilo;
	}
	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
	public StringType getSitFina() {
		return sitFina;
	}
	public void setSitFina(StringType sitFina) {
		this.sitFina = sitFina;
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
}
