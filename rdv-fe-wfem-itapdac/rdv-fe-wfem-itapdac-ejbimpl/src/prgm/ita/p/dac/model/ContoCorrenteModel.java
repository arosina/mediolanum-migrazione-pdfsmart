package prgm.ita.p.dac.model;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ContoCorrenteModel extends AgenteModel {
	
	private StringType 	codMediolanum = new StringType();
	private StringType 	srvCode = new StringType();
	private StringType 	srvMessage = new StringType();
	private StringType 	srvSeverity = new StringType();
	private StringType 	ruoloSulConto = new StringType();
	private StringType 	numeroConto = new StringType();
	private ListType	firmeDelConto = new ListType(FirmaModel.class);
	private IntegerType isChiuso = new IntegerType();
	
	public StringType getNumeroConto() {
		return numeroConto;
	}

	public void setNumeroConto(StringType numeroConto) {
		this.numeroConto = numeroConto;
	}

	public StringType getRuoloSulConto() {
		return ruoloSulConto;
	}

	public void setRuoloSulConto(StringType ruoloSulConto) {
		this.ruoloSulConto = ruoloSulConto;
	}

	public ListType getFirmeDelConto() {
		return firmeDelConto;
	}

	public void setFirmeDelConto(ListType firmeDelConto) {
		this.firmeDelConto = firmeDelConto;
	}

	public StringType getSrvMessage() {
		return srvMessage;
	}

	public void setSrvMessage(StringType srvMessage) {
		this.srvMessage = srvMessage;
	}

	public StringType getSrvCode() {
		return srvCode;
	}

	public void setSrvCode(StringType srvCode) {
		this.srvCode = srvCode;
	}

	public StringType getSrvSeverity() {
		return srvSeverity;
	}

	public void setSrvSeverity(StringType srvSeverity) {
		this.srvSeverity = srvSeverity;
	}

	public IntegerType getIsChiuso() {
		return isChiuso;
	}

	public void setIsChiuso(IntegerType isChiuso) {
		this.isChiuso = isChiuso;
	}

	public StringType getCodMediolanum() {
		return codMediolanum;
	}

	public void setCodMediolanum(StringType codMediolanum) {
		this.codMediolanum = codMediolanum;
	}

}
