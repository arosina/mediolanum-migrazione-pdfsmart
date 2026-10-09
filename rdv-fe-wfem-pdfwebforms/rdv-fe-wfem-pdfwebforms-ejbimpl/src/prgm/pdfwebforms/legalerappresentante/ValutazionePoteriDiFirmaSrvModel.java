package prgm.pdfwebforms.legalerappresentante;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class ValutazionePoteriDiFirmaSrvModel extends CommandDataModel {

	// Input
	private StringType  		userId = new StringType();
	
	private StringType			codiceNdgPersonaGiuridica = new StringType();
	private StringType			codiceNdgFirmatario = new StringType();
	private DateType			dataSottoscrizione = new DateType();
	private StringType			pdfInstanceId = new StringType();
	private IntegerType			codProdottoPrit = new IntegerType();
	private IntegerType			codOperazionePrit = new IntegerType();
	private DoubleType			importo = new DoubleType();
	
	// Output
	private StringType			esitoGlobale = new StringType();

	private StringType			esitoValutazioneFirmatario = new StringType();
	private ListType			erroriFirmatario = new ListType(ErroreFirmatarioModel.class);
	
	// Per WS
	private String callNonEffettuataMsg = null;
	private String systemErrorMsg = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getDataSottoscrizioneAAAAMMGG() {
		DateType ds = dataSottoscrizione.isNull() ? Tools.today() : dataSottoscrizione;
		return new StringType(ds.getAA()+"-"+ds.getMM()+"-"+ds.getGG());
	}

	public StringType getCodiceNdgPersonaGiuridica() {
		return codiceNdgPersonaGiuridica;
	}

	public void setCodiceNdgPersonaGiuridica(StringType codiceNdgPersonaGiuridica) {
		this.codiceNdgPersonaGiuridica = codiceNdgPersonaGiuridica;
	}

	public StringType getCodiceNdgFirmatario() {
		return codiceNdgFirmatario;
	}

	public void setCodiceNdgFirmatario(StringType codiceNdgFirmatario) {
		this.codiceNdgFirmatario = codiceNdgFirmatario;
	}

	public DateType getDataSottoscrizione() {
		return dataSottoscrizione;
	}

	public void setDataSottoscrizione(DateType dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}

	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}

	public IntegerType getCodProdottoPrit() {
		return codProdottoPrit;
	}

	public void setCodProdottoPrit(IntegerType codProdottoPrit) {
		this.codProdottoPrit = codProdottoPrit;
	}

	public IntegerType getCodOperazionePrit() {
		return codOperazionePrit;
	}

	public void setCodOperazionePrit(IntegerType codOperazionePrit) {
		this.codOperazionePrit = codOperazionePrit;
	}

	public DoubleType getImporto() {
		return importo;
	}

	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}

	public StringType getEsitoValutazioneFirmatario() {
		return esitoValutazioneFirmatario;
	}

	public void setEsitoValutazioneFirmatario(StringType esitoValutazioneFirmatario) {
		this.esitoValutazioneFirmatario = esitoValutazioneFirmatario;
	}

	public ListType getErroriFirmatario() {
		return erroriFirmatario;
	}

	public void setErroriFirmatario(ListType erroriFirmatario) {
		this.erroriFirmatario = erroriFirmatario;
	}

	public StringType getEsitoGlobale() {
		return esitoGlobale;
	}

	public void setEsitoGlobale(StringType esitoGlobale) {
		this.esitoGlobale = esitoGlobale;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

	public String getCallNonEffettuataMsg() {
		return callNonEffettuataMsg;
	}

	public void setCallNonEffettuataMsg(String callNonEffettuataMsg) {
		this.callNonEffettuataMsg = callNonEffettuataMsg;
	}

	public String getSystemErrorMsg() {
		return systemErrorMsg;
	}

	public void setSystemErrorMsg(String systemErrorMsg) {
		this.systemErrorMsg = systemErrorMsg;
	}

}
