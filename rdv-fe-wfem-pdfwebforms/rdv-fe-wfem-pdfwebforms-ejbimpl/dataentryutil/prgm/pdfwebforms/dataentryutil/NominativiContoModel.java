package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class NominativiContoModel extends AbstractAutocompleteModel {

	private StringType   ndgCliente = new StringType();
	private StringType   cognomeCliente = new StringType();
	private StringType   nomeCliente = new StringType();
	private StringType   codiceRuoloCliente = new StringType();
	private StringType   ruoloCliente = new StringType();
	
	private IntegerType  orderBy = new IntegerType();
	
	public IntegerType getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(IntegerType orderBy) {
		this.orderBy = orderBy;
	}
	public StringType getNdgCliente() {
		return ndgCliente;
	}
	public void setNdgCliente(StringType ndgCliente) {
		this.ndgCliente = ndgCliente;
	}
	public StringType getCognomeCliente() {
		return cognomeCliente;
	}
	public void setCognomeCliente(StringType cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}
	public StringType getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(StringType nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public StringType getRuoloCliente() {
		return ruoloCliente;
	}
	public void setRuoloCliente(StringType ruoloCliente) {
		this.ruoloCliente = ruoloCliente;
	}
	public StringType getCodiceRuoloCliente() {
		return codiceRuoloCliente;
	}
	public void setCodiceRuoloCliente(StringType codiceRuoloCliente) {
		this.codiceRuoloCliente = codiceRuoloCliente;
	}
	
}
