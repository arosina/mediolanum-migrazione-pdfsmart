package prgm.pdfwebformsutil.drivers.dao.posizionewealthservices.recuperaposizionewealth;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class PosizioneWealthModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IntegerType resultCode = new IntegerType();
	private StringType codiceCliente = new StringType();
	private StringType flagAttivoWealth = new StringType();
	private StringType flagPotenzialeWealth = new StringType();
	private StringType tipoWealth = new StringType();
	
	public StringType getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

	public StringType getFlagAttivoWealth() {
		return flagAttivoWealth;
	}

	public void setFlagAttivoWealth(StringType flagAttivoWealth) {
		this.flagAttivoWealth = flagAttivoWealth;
	}

	public StringType getFlagPotenzialeWealth() {
		return flagPotenzialeWealth;
	}

	public void setFlagPotenzialeWealth(StringType flagPotenzialeWealth) {
		this.flagPotenzialeWealth = flagPotenzialeWealth;
	}

	public boolean isAttivoWealth() {
		return getFlagAttivoWealth().equals("S");
	}

	public boolean isPotenzialeWealth() {
		return getFlagPotenzialeWealth().equals("S");
	}

	public StringType getTipoWealth() {
		return tipoWealth;
	}

	public void setTipoWealth(StringType tipoWealth) {
		this.tipoWealth = tipoWealth;
	}
}
