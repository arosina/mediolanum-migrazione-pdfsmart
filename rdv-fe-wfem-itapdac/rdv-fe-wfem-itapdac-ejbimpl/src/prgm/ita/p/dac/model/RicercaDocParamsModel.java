package prgm.ita.p.dac.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaDocParamsModel extends CommandDataModel {
	
	private BooleanType isOfflineEnvironment = new BooleanType();
	
	private StringType 		idDocumento = new StringType();
	private StringType  	barcode = new StringType();
	
	private IntegerType   	ubicazioneDoc = new IntegerType();
	private IntegerType   	statoDoc = new IntegerType();
	private IntegerType   	esitoDoc = new IntegerType();

	private DateType   		dataInizio = new DateType();
	private DateType   		dataFine = new DateType();

	private StringType 		numeroContratto = new StringType();
	private IntegerType		codProdotto = new IntegerType();
	private IntegerType		codOperazione = new IntegerType();
	private StringType 		codMediolanum = new StringType();
	private StringType 		cognomeCliente = new StringType();
	private StringType 		nomeCliente = new StringType();
	private StringType 		codiceAgente = new StringType();
	
	private DoubleType 		importo = new DoubleType();
	private StringType 		numeroAssegno = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public RicercaDocParamsModel(){
		addCodDescField("codProdotto","ProdottiInRicercaDoc");
		addCodDescField("codOperazione","Operazioni");
		addCodDescField("ubicazioneDoc","Uffici");
		addCodDescField("statoDoc","StatiDocumento");
		addCodDescField("esitoDoc","EsitiDocumento");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsRicercaPerDocumento(){
		if(getIsRicercaPerMezzoPg().booleanValue())
			return new BooleanType(true);
		if(getBarcode().isNull() && 
		   getIdDocumento().isNull() &&
		   getNumeroContratto().isNull() && getCodProdotto().isNull() && getCodOperazione().isNull() &&
		   getCodMediolanum().isNull() && getCognomeCliente().isNull() && getNomeCliente().isNull() && 
		   getCodiceAgente().isNull())
			return new BooleanType(false);
		return new BooleanType(true);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsRicercaPerMezzoPg(){
		if(!getIdDocumento().isNull() || !getBarcode().isNull())
			return new BooleanType(false);
		if(getImporto().isNull() && getNumeroAssegno().isNull())
			return new BooleanType(false);
		return new BooleanType(true);
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

	public StringType getCodiceAgente() {
		return codiceAgente;
	}

	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	public StringType getNumeroContratto() {
		return numeroContratto;
	}

	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}

	public DoubleType getImporto() {
		return importo;
	}

	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}

	public StringType getNumeroAssegno() {
		return numeroAssegno;
	}

	public void setNumeroAssegno(StringType numeroAssegno) {
		this.numeroAssegno = numeroAssegno;
	}

	public IntegerType getCodProdotto() {
		return codProdotto;
	}

	public void setCodProdotto(IntegerType codProdotto) {
		this.codProdotto = codProdotto;
	}

	public IntegerType getCodOperazione() {
		return codOperazione;
	}

	public void setCodOperazione(IntegerType codOperazione) {
		this.codOperazione = codOperazione;
	}

	public StringType getCodMediolanum() {
		return codMediolanum;
	}

	public void setCodMediolanum(StringType codMediolanum) {
		this.codMediolanum = codMediolanum;
	}

	public StringType getBarcode() {
		return barcode;
	}

	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
	}

	public DateType getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(DateType dataInizio) {
		this.dataInizio = dataInizio;
	}

	public DateType getDataFine() {
		return dataFine;
	}

	public void setDataFine(DateType dataFine) {
		this.dataFine = dataFine;
	}

	public IntegerType getEsitoDoc() {
		return esitoDoc;
	}

	public void setEsitoDoc(IntegerType esitoDoc) {
		this.esitoDoc = esitoDoc;
	}

	public BooleanType getIsOfflineEnvironment() {
		return isOfflineEnvironment;
	}

	public void setIsOfflineEnvironment(BooleanType isOfflineEnvironment) {
		this.isOfflineEnvironment = isOfflineEnvironment;
	}

	public StringType getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(StringType idDocumento) {
		this.idDocumento = idDocumento;
	}

	public IntegerType getStatoDoc() {
		return statoDoc;
	}

	public void setStatoDoc(IntegerType statoDoc) {
		this.statoDoc = statoDoc;
	}

	public IntegerType getUbicazioneDoc() {
		return ubicazioneDoc;
	}

	public void setUbicazioneDoc(IntegerType ubicazioneDoc) {
		this.ubicazioneDoc = ubicazioneDoc;
	}
	
}
