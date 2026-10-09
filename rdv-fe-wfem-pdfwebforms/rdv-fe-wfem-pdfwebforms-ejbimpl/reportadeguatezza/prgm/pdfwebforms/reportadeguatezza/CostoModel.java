package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class CostoModel extends CommandDataModel {

	// <v11:costo tipo="?" importo-min="?" importo-max="?" perc-min="?" perc-max="?"/>
	private StringType tipo = new StringType();	
	private DoubleType importoMin = new DoubleType();
	private DoubleType importoMax = new DoubleType();
	private DoubleType percMin = new DoubleType();
	private DoubleType percMax = new DoubleType();
	
	public StringType getTipo() {
		return tipo;
	}
	public void setTipo(StringType tipo) {
		this.tipo = tipo;
	}
	public DoubleType getImportoMin() {
		return importoMin;
	}
	public void setImportoMin(DoubleType importoMin) {
		this.importoMin = importoMin;
	}
	public DoubleType getImportoMax() {
		return importoMax;
	}
	public void setImportoMax(DoubleType importoMax) {
		this.importoMax = importoMax;
	}
	public DoubleType getPercMin() {
		return percMin;
	}
	public void setPercMin(DoubleType percMin) {
		this.percMin = percMin;
	}
	public DoubleType getPercMax() {
		return percMax;
	}
	public void setPercMax(DoubleType percMax) {
		this.percMax = percMax;
	}
	
}
