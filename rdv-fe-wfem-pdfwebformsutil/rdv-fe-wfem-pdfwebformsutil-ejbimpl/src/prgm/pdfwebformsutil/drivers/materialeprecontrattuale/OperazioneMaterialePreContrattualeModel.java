package prgm.pdfwebformsutil.drivers.materialeprecontrattuale;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class OperazioneMaterialePreContrattualeModel extends CommandDataModel implements Comparable<OperazioneMaterialePreContrattualeModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -339603723912582416L;
    private StringType isin = new StringType();
    private StringType divisa = new StringType();
    private StringType piazza = new StringType();
        
	public StringType getIsin() {
		return isin;
	}
	
	public void setIsin(StringType isin) {
		this.isin = isin;
	}
			
	@Override
	public int compareTo(OperazioneMaterialePreContrattualeModel o) {		
		return this.getIsin().compareTo(o.getIsin());
	}

	public StringType getDivisa() {
		return divisa;
	}

	public void setDivisa(StringType divisa) {
		this.divisa = divisa;
	}

	public StringType getPiazza() {
		return piazza;
	}

	public void setPiazza(StringType piazza) {
		this.piazza = piazza;
	}  
}
