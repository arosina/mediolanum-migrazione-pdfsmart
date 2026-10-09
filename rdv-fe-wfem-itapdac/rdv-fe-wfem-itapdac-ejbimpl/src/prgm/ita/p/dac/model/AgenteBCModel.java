package prgm.ita.p.dac.model;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/********************************************************************************/
/********************************************************************************/
public class AgenteBCModel extends AgenteModel {
	
	private StringType  idDac = new StringType();
	private IntegerType numDocumentiInDac = new IntegerType();
	
	/********************************************************************************/
	/********************************************************************************/
	public String getTestoPulsanteAperturaPrit() {
		return Tools.capitalize(getNominativo().toString())+" ("+getNumDocumentiInDac().intValue()+" document"+(getNumDocumentiInDac().intValue()==1?"o":"i")+")";
	}
	
	public StringType getIdDac() {
		return idDac;
	}
	public void setIdDac(StringType idDac) {
		this.idDac = idDac;
	}
	public IntegerType getNumDocumentiInDac() {
		return numDocumentiInDac;
	}
	public void setNumDocumentiInDac(IntegerType numDocumentiInDac) {
		this.numDocumentiInDac = numDocumentiInDac;
	}
}
