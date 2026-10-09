package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DatiPianoPacModel extends CommandDataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7966109444337205008L;
	private IntegerType	numeroRateAnno = new IntegerType();
	private DoubleType	importoRata = new DoubleType();
	private StringType	conto = new StringType();

	public IntegerType getNumeroRateAnno() {
		return numeroRateAnno;
	}
	public void setNumeroRateAnno(IntegerType numeroRateAnno) {
		this.numeroRateAnno = numeroRateAnno;
	}
	public DoubleType getImportoRata() {
		return importoRata;
	}
	public void setImportoRata(DoubleType importoRata) {
		this.importoRata = importoRata;
	}
	public StringType getConto() {
		return conto;
	}
	public void setConto(StringType conto) {
		this.conto = conto;
	}
}
