package prgm.pdfwebforms.copernicoprocess.propostecopernicosmart;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PropostaCopernicoKeyModel extends CommandDataModel {

	private StringType sorgente = new StringType();
	private StringType chiave = new StringType();
	private IntegerType chiaveAsNum = new IntegerType();
	
	public StringType getSorgente() {
		return sorgente;
	}
	public void setSorgente(StringType sorgente) {
		this.sorgente = sorgente;
	}
	public StringType getChiave() {
		return chiave;
	}
	public void setChiave(StringType chiave) {
		this.chiave = chiave;
	}
	public IntegerType getChiaveAsNum() {
		return chiaveAsNum;
	}
	public void setChiaveAsNum(IntegerType chiaveAsNum) {
		this.chiaveAsNum = chiaveAsNum;
	}

}
