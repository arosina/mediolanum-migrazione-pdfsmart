package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DocumentoModel extends AbstractSectionModel{

	private static final long serialVersionUID = 1L;

	public static String MESSAGGIO_PATENTE_UCO = "Per le patenti europee formato card, ti ricordiamo di verificare la voce \"4c - rilasciata da\"; "+
												 "nel caso in cui sia scritto \"MIT-UCO\" il luogo di rilascio da inserire è Roma. ";
	
	private StringType  tipoDocumento = new StringType();
	private StringType  categoriaPatente = new StringType();	// Deprecato: Con FATCA: non più richiesto
	private StringType  numeroDocumento = new StringType();
	private ComuneModel luogoRilascio = new ComuneModel();
	private DateType    dataRilascio = new DateType();
	private DateType    dataScadenza = new DateType();
	private DateType    dataRinnovo = new DateType();			// Deprecato: Con FATCA: non più richiesto
	private StringType  codEnteRilasciante = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel(){

		frontendPropName.add("documento_tipoDocumento");
		frontendPropName.add("documento_numeroDocumento");
		frontendPropName.add("documento_luogoRilascio_comune");
		frontendPropName.add("documento_dataRilascio");
		frontendPropName.add("documento_dataScadenza");
		
		addCodDescField("tipoDocumento","TIPI_DOCUMENTO");
	}
	
	// Campi deprecati e non più utilizzati
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getCategoriaPatente() {
		return categoriaPatente;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setCategoriaPatente(StringType categoriaPatente) {
		this.categoriaPatente = categoriaPatente;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public DateType getDataRinnovo() {
		return dataRinnovo;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setDataRinnovo(DateType dataRinnovo) {
		this.dataRinnovo = dataRinnovo;
	}
	/********************************************************************/


	public DateType getDataRilascio() {
		return dataRilascio;
	}

	public void setDataRilascio(DateType dataRilascio) {
		this.dataRilascio = dataRilascio;
	}

	public StringType getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(StringType tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public DateType getDataScadenza() {
		return dataScadenza;
	}

	public ComuneModel getLuogoRilascio() {
		return luogoRilascio;
	}

	public StringType getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setDataScadenza(DateType dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public void setLuogoRilascio(ComuneModel luogoRilascio) {
		this.luogoRilascio = luogoRilascio;
	}

	public void setNumeroDocumento(StringType numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public StringType getCodEnteRilasciante() {
		return codEnteRilasciante;
	}

	public void setCodEnteRilasciante(StringType codEnteRilasciante) {
		this.codEnteRilasciante = codEnteRilasciante;
	}
}
