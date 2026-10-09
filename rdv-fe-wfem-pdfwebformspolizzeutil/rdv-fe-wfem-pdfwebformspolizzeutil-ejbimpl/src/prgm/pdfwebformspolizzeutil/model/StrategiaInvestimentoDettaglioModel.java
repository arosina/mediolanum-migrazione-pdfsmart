package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class StrategiaInvestimentoDettaglioModel extends CommandDataModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType 	codIsin 			= new StringType();
	private StringType 	ruoloFondo 			= new StringType();
	private StringType 	desFondoDettaglio 	= new StringType();
	private DoubleType 	impRataConvsProg 	= new DoubleType();
	private IntegerType progrMifid 			= new IntegerType();
	private StringType 	codNaturaFondo 		= new StringType();
	private DoubleType 	prcRipart 			= new DoubleType();
	private DoubleType 	impPianoResid 		= new DoubleType();
	
	
	public StringType getCodIsin() {
		return codIsin;
	}
	public StringType getRuoloFondo() {
		return ruoloFondo;
	}
	public StringType getDesFondoDettaglio() {
		return desFondoDettaglio;
	}
	public DoubleType getImpRataConvsProg() {
		return impRataConvsProg;
	}
	public IntegerType getProgrMifid() {
		return progrMifid;
	}
	public StringType getCodNaturaFondo() {
		return codNaturaFondo;
	}
	public void setCodIsin(StringType codIsin) {
		this.codIsin = codIsin;
	}
	public void setRuoloFondo(StringType ruoloFondo) {
		this.ruoloFondo = ruoloFondo;
	}
	public void setDesFondoDettaglio(StringType desFondoDettaglio) {
		this.desFondoDettaglio = desFondoDettaglio;
	}
	public void setImpRataConvsProg(DoubleType impRataConvsProg) {
		this.impRataConvsProg = impRataConvsProg;
	}
	public void setProgrMifid(IntegerType progrMifid) {
		this.progrMifid = progrMifid;
	}
	public void setCodNaturaFondo(StringType codNaturaFondo) {
		this.codNaturaFondo = codNaturaFondo;
	}
	public DoubleType getPrcRipart() {
		return prcRipart;
	}
	public DoubleType getImpPianoResid() {
		return impPianoResid;
	}
	public void setPrcRipart(DoubleType prcRipart) {
		this.prcRipart = prcRipart;
	}
	public void setImpPianoResid(DoubleType impPianoResid) {
		this.impPianoResid = impPianoResid;
	}
	
   
 
			
}
