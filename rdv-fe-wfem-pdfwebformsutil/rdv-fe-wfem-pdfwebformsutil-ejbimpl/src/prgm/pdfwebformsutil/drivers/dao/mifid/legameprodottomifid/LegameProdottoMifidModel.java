package prgm.pdfwebformsutil.drivers.dao.mifid.legameprodottomifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class LegameProdottoMifidModel extends CommandDataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType inputCodProdotto = new StringType();
	private StringType inputChiaveNaturaProdotto = new StringType();
	private StringType inputChiaveNaturaPadre = new StringType();
	private StringType inputIsin = new StringType();
	private StringType inputProfilo = new StringType();	
	private StringType codSurrogatoProdotto = new StringType();
	private StringType inputFormaContrattuale  = new StringType();
	private StringType inputPiazza  = new StringType();
	private StringType inputDivisa  = new StringType();
	
	public StringType getInputCodProdotto() {
		return inputCodProdotto;
	}
	public void setInputCodProdotto(StringType inputCodProdotto) {
		this.inputCodProdotto = inputCodProdotto;
	}
	public StringType getInputChiaveNaturaProdotto() {
		return inputChiaveNaturaProdotto;
	}
	public void setInputChiaveNaturaProdotto(StringType inputChiaveNaturaProdotto) {
		this.inputChiaveNaturaProdotto = inputChiaveNaturaProdotto;
	}
	public StringType getInputChiaveNaturaPadre() {
		return inputChiaveNaturaPadre;
	}
	public void setInputChiaveNaturaPadre(StringType inputChiaveNaturaPadre) {
		this.inputChiaveNaturaPadre = inputChiaveNaturaPadre;
	}
	public StringType getInputIsin() {
		return inputIsin;
	}
	public void setInputIsin(StringType inputIsin) {
		this.inputIsin = inputIsin;
	}
	public StringType getCodSurrogatoProdotto() {
		return codSurrogatoProdotto;
	}
	public void setCodSurrogatoProdotto(StringType codSurrogatoProdotto) {
		this.codSurrogatoProdotto = codSurrogatoProdotto;
	}
	public StringType getInputProfilo() {
		return inputProfilo;
	}
	public void setInputProfilo(StringType inputProfilo) {
		this.inputProfilo = inputProfilo;
	}
	public StringType getInputFormaContrattuale() {
		return inputFormaContrattuale;
	}
	public void setInputFormaContrattuale(StringType inputFormaContrattuale) {
		this.inputFormaContrattuale = inputFormaContrattuale;
	}
	public StringType getInputPiazza() {
		return inputPiazza;
	}
	public void setInputPiazza(StringType inputPiazza) {
		this.inputPiazza = inputPiazza;
	}
	public StringType getInputDivisa() {
		return inputDivisa;
	}
	public void setInputDivisa(StringType inputDivisa) {
		this.inputDivisa = inputDivisa;
	}	
}
