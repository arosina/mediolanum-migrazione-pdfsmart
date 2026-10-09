package prgm.pdfwebforms.catalog;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfContextIntf;

/********************************************************************************/
/********************************************************************************/
public class PdfCatalogParamsModel extends PdfContextIntf{
	
	private BooleanType doSearch = new BooleanType();

	private StringType 	orderField			= new StringType();
	private StringType 	orderType			= new StringType();
	
	private StringType 	codice 				= new StringType();
	private StringType 	descrizione 		= new StringType();
	private IntegerType codiceSegmento 		= new IntegerType();
	private IntegerType codiceProdotto 		= new IntegerType();
	private IntegerType codiceTipoModulo 	= new IntegerType();
	private StringType 	codAgente 			= new StringType();
	private StringType 	nominativoAgente 	= new StringType();
	private StringType 	cognomeCliente		= new StringType();
	private StringType 	nomeCliente			= new StringType();
	private DateType 	dataInizio 			= new DateType();
	private DateType 	dataFine 			= new DateType();
	private BooleanType mostraSoloInFD		= new BooleanType();
	
	/********************************************************************************/
	/********************************************************************************/
	public PdfCatalogParamsModel(){
		addCodDescField("codiceSegmento","SEGMENTI");
		addCodDescField("codiceProdotto","PRODOTTI");
		addCodDescField("codiceTipoModulo","TIPOMODULI");
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public StringType getDescrizioneForLike(){
		if(getDescrizione().isNull())
			return descrizione;
		String res = getDescrizione().toString().replaceAll("[\\s\\[\\]\\-\\_\\/\\(\\)\\.]","%");
		return new StringType(res.toString());
	}

	public StringType getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}

	public IntegerType getCodiceSegmento() {
		return codiceSegmento;
	}

	public void setCodiceSegmento(IntegerType codiceSegmento) {
		this.codiceSegmento = codiceSegmento;
	}

	public IntegerType getCodiceProdotto() {
		return codiceProdotto;
	}

	public void setCodiceProdotto(IntegerType codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
	}

	public IntegerType getCodiceTipoModulo() {
		return codiceTipoModulo;
	}

	public void setCodiceTipoModulo(IntegerType codiceTipoModulo) {
		this.codiceTipoModulo = codiceTipoModulo;
	}

	public StringType getCodice() {
		return codice;
	}

	public void setCodice(StringType codice) {
		this.codice = codice;
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

	public BooleanType getDoSearch() {
		return doSearch;
	}

	public void setDoSearch(BooleanType doSearch) {
		this.doSearch = doSearch;
	}

	public BooleanType getMostraSoloInFD() {
		return mostraSoloInFD;
	}

	public void setMostraSoloInFD(BooleanType mostraSoloInFD) {
		this.mostraSoloInFD = mostraSoloInFD;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getNominativoAgente() {
		return nominativoAgente;
	}

	public void setNominativoAgente(StringType nominativoAgente) {
		this.nominativoAgente = nominativoAgente;
	}

	public StringType getOrderField() {
		return orderField;
	}

	public void setOrderField(StringType orderField) {
		this.orderField = orderField;
	}

	public StringType getOrderType() {
		return orderType;
	}

	public void setOrderType(StringType orderType) {
		this.orderType = orderType;
	}
}
