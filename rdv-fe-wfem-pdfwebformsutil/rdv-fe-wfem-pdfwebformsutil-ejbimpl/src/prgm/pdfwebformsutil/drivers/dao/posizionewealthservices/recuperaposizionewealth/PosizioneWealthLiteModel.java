package prgm.pdfwebformsutil.drivers.dao.posizionewealthservices.recuperaposizionewealth;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

@SuppressWarnings("serial")
public class PosizioneWealthLiteModel extends CommandDataModel {

	// input
	private StringType codiceCliente = null;
	private StringType praticheDigitali = null;

	// output
	private IntegerType resultCode = new IntegerType();
	private ListType listaTipoWealth = new ListType(TipoPosizioneWealthModel.class);

	public PosizioneWealthLiteModel() {
		super();
	}

	public StringType getCodiceCliente() {
		return codiceCliente;
	}

	public ListType getListaTipoWealth() {
		return listaTipoWealth;
	}

	public void setCodiceCliente(StringType codiceClienteInput) {
		this.codiceCliente = codiceClienteInput;
	}

	public void setListaTipoWealth(ListType listaTipoWealth) {
		this.listaTipoWealth = listaTipoWealth;
	}

	public StringType getPraticheDigitali() {
		return praticheDigitali;
	}

	public void setPraticheDigitali(StringType praticheDigitali) {
		this.praticheDigitali = praticheDigitali;
	}

	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

}