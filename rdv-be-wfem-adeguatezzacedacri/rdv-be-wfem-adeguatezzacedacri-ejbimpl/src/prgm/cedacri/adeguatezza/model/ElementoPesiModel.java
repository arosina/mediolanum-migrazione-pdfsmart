package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
/*
 * Elemento della lista che contiene i pesi dati alle risposte
 */
public class ElementoPesiModel extends CommandDataModel  implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	private IntegerType numDoma = new IntegerType();
	private IntegerType numRisp = new IntegerType();
	private StringType  dominio = new StringType();
	private IntegerType pesoRis = new IntegerType();

	public StringType getDominio() {
		return dominio;
	}
	public void setDominio(StringType dominio) {
		this.dominio = dominio;
	}
	public IntegerType getNumDoma() {
		return numDoma;
	}
	public void setNumDoma(IntegerType numDoma) {
		this.numDoma = numDoma;
	}
	public IntegerType getNumRisp() {
		return numRisp;
	}
	public void setNumRisp(IntegerType numRisp) {
		this.numRisp = numRisp;
	}
	public IntegerType getPesoRis() {
		return pesoRis;
	}
	public void setPesoRis(IntegerType pesoRis) {
		this.pesoRis = pesoRis;
	}
	
}
