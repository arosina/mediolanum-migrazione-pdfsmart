package prgm.pdfwebforms.drivers.io.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ElementoControlliConcentrazioneFiaModel extends CommandDataModel{

	private static final long serialVersionUID = 1L;

	private StringType famiglia = new StringType();
	private StringType isin = new StringType();
	private DoubleType importo = new DoubleType();
	private DoubleType percentuale = new DoubleType();
	
	public StringType getFamiglia() {
		return famiglia;
	}
	public void setFamiglia(StringType famiglia) {
		this.famiglia = famiglia;
	}
	public StringType getIsin() {
		return isin;
	}
	public void setIsin(StringType isin) {
		this.isin = isin;
	}
	public DoubleType getImporto() {
		return importo;
	}
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	public DoubleType getPercentuale() {
		return percentuale;
	}
	public void setPercentuale(DoubleType percentuale) {
		this.percentuale = percentuale;
	}

}
