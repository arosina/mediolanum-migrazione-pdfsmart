package prgm.pdfwebformsutil.drivers.dao.strategieinvestimento;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class StrategiePerClienteModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StringType codCliente = new StringType();
	private ListType listaStrategie = new ListType(StrategieModel.class);	
	private IntegerType resultCode = new IntegerType();
	
	public StringType getCodCliente() {
		return codCliente;
	}
	public void setCodCliente(StringType codCliente) {
		this.codCliente = codCliente;
	}
	public ListType getListaStrategie() {
		return listaStrategie;
	}
	public void setListaStrategie(ListType listaStrategie) {
		this.listaStrategie = listaStrategie;
	}
	public IntegerType getResultCode() {
		return resultCode;
	}
	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}
}