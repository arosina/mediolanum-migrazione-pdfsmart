package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
/*
 * Elemento della lista che compone i Punteggi
 */
public class ElementoPunteggiModel extends CommandDataModel implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Elemento del Punteggio
	 */
	private StringType dominio = new StringType();
	private IntegerType valDomi = new IntegerType();
	
	/*
	 * Metodi Set e Get
	 */
	public StringType getDominio() {
		return dominio;
	}
	public void setDominio(StringType dominio) {
		this.dominio = dominio;
	}
	public IntegerType getValDomi() {
		return valDomi;
	}
	public void setValDomi(IntegerType valDomi) {
		this.valDomi = valDomi;
	}
}
