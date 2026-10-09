package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class ResiduoPolizzaFondiModel extends CommandDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7247305247502969640L;
	private StringType prodCFondo = new StringType();
	private StringType tipologiaFondo = new StringType();
	private DoubleType residuoFondo = new DoubleType();
	
	public StringType getProdCFondo() {
		return prodCFondo;
	}
	public void setProdCFondo(StringType prodCFondo) {
		this.prodCFondo = prodCFondo;
	}
	public StringType getTipologiaFondo() {
		return tipologiaFondo;
	}
	public void setTipologiaFondo(StringType tipologiaFondo) {
		this.tipologiaFondo = tipologiaFondo;
	}
	public DoubleType getResiduoFondo() {
		return residuoFondo;
	}
	public void setResiduoFondo(DoubleType residuoFondo) {
		this.residuoFondo = residuoFondo;
	}	
	
}
