package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataResponse;
import prgm.pdfwebforms.sostituzioni.PreferenzaClienteSostituzioniModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ReportAdeguatezzaCallModel extends CommandDataModel {
	
	// Dati imopstati dal driver
	private ProvideReportAdeguatezzaDataResponse input = null;	// Ritornato dalla callback "provideReportAdeguatezzaData" del driver
	
	// Input
	private StringType  userId = new StringType();
	private StringType  codiceAgente = new StringType();
	private StringType  codiceCliente = new StringType();
	private StringType  codRaccomandazioneIdd = new StringType();
	private StringType  idSostituzione = new StringType();
	private ListType	ordini = new ListType(OrdineModel.class);
	private ListType	ordiniResult = new ListType(OrdineResultModel.class);
	private ListType 	elencoPreferenze = new ListType(PreferenzaClienteSostituzioniModel.class);
	
	// Dati manleva Mifid (vedi MifidCaller)
	private StringType	idAdeguatezzaRDA = new StringType();
	private StringType	flagManlevaKOESG = new StringType();
	
	// Output
    private StringType  idReportAdeguatezza = new StringType();
	private ListType 	costi = new ListType(CostoModel.class);
	
	// Utility
    private StringType  noRdaReason = new StringType();
    private StringType  idEcmReportAdeguatezza = new StringType();


    /***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}
	
	public ProvideReportAdeguatezzaDataResponse getInput() {
		return input;
	}
	public void setInput(ProvideReportAdeguatezzaDataResponse input) {
		this.input = input;
	}
	public StringType getCodiceAgente() {
		return codiceAgente;
	}
	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}
	public StringType getCodiceCliente() {
		return codiceCliente;
	}
	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	public ListType getOrdini() {
		return ordini;
	}
	public void setOrdini(ListType ordini) {
		this.ordini = ordini;
	}
	public StringType getIdReportAdeguatezza() {
		return idReportAdeguatezza;
	}
	public void setIdReportAdeguatezza(StringType idReportAdeguatezza) {
		this.idReportAdeguatezza = idReportAdeguatezza;
	}
	public ListType getCosti() {
		return costi;
	}
	public void setCosti(ListType costi) {
		this.costi = costi;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

	public StringType getNoRdaReason() {
		return noRdaReason;
	}

	public void setNoRdaReason(StringType noRdaReason) {
		this.noRdaReason = noRdaReason;
	}

	public ListType getOrdiniResult() {
		return ordiniResult;
	}

	public void setOrdiniResult(ListType ordiniResult) {
		this.ordiniResult = ordiniResult;
	}

	public StringType getCodRaccomandazioneIdd() {
		return codRaccomandazioneIdd;
	}

	public void setCodRaccomandazioneIdd(StringType codRaccomandazioneIdd) {
		this.codRaccomandazioneIdd = codRaccomandazioneIdd;
	}

	public StringType getIdEcmReportAdeguatezza() {
		return idEcmReportAdeguatezza;
	}

	public void setIdEcmReportAdeguatezza(StringType idEcmReportAdeguatezza) {
		this.idEcmReportAdeguatezza = idEcmReportAdeguatezza;
	}

	public StringType getIdSostituzione() {
		return idSostituzione;
	}

	public void setIdSostituzione(StringType idSostituzione) {
		this.idSostituzione = idSostituzione;
	}

	public ListType getElencoPreferenze() {
		return elencoPreferenze;
	}

	public void setElencoPreferenze(ListType elencoPreferenze) {
		this.elencoPreferenze = elencoPreferenze;
	}

	public StringType getIdAdeguatezzaRDA() {
		return idAdeguatezzaRDA;
	}

	public void setIdAdeguatezzaRDA(StringType idAdeguatezzaRDA) {
		this.idAdeguatezzaRDA = idAdeguatezzaRDA;
	}

	public StringType getFlagManlevaKOESG() {
		return flagManlevaKOESG;
	}

	public void setFlagManlevaKOESG(StringType flagManlevaKOESG) {
		this.flagManlevaKOESG = flagManlevaKOESG;
	}
	
}
