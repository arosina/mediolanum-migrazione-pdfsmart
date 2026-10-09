package prgm.pdfwebformsutil.drivers.dao.verifiche.cliente;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class VerificheClienteModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType inputCodiceCliente = new StringType();
	private StringType inputElencoCodiciCliente = new StringType();
	private StringType inputCodiceFiscale = new StringType();

	public StringType getInputElencoCodiciCliente() {
		return inputElencoCodiciCliente;
	}

	public void setInputElencoCodiciCliente(StringType inputElencoCodiciCliente) {
		this.inputElencoCodiciCliente = inputElencoCodiciCliente;
	}

	public StringType getInputCodiceCliente() {
		return inputCodiceCliente;
	}

	public void setInputCodiceCliente(StringType inputCodiceCliente) {
		this.inputCodiceCliente = inputCodiceCliente;
	}

	public StringType getInputCodiceFiscale() {
		return inputCodiceFiscale;
	}

	public void setInputCodiceFiscale(StringType inputCodiceFiscale) {
		this.inputCodiceFiscale = inputCodiceFiscale;
	}
}
