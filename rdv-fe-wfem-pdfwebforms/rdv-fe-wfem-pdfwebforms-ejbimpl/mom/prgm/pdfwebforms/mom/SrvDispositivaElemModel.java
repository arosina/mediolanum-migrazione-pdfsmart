package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SrvDispositivaElemModel extends CommandDataModel {

	private ListType allegati = null;
	
	// Input replicato su tutte le dispositive
	private transient SrvDispositivaCallModel globalData = null;
	
	// Input specifico per singola dispositiva impostato dal motore
	private StringType 	codiceProcessoOperativo = new StringType();
	private StringType 	codiceOperazioneDispositivaSistemaOrigine = new StringType();
	private StringType 	codiceTipoOperazioneProdottoServizio = new StringType();
	private StringType 	codiceSurrogatoProdottoServizio = new StringType();
	private StringType 	codiceTipoValoreDispositiva = new StringType();
	private StringType 	codiceScopoRapporto = new StringType();
	private StringType 	codTipoDocum = new StringType();
	
	// Input specifico per singola dispositiva impostato dal driver per le dispo oltre la "main"
	private StringType 	descrizioneNote = new StringType();
	private StringType 	contrNRapportoRiferimento = new StringType();
	private DoubleType 	valoreDispositiva = new DoubleType();
	private StringType 	codiceBarcode = new StringType();
	private StringType 	numeroProposta = new StringType();
	private StringType 	codicePropostaCorrelata = new StringType();
	private ListType   	soggetti = new ListType(SoggettoDispositivaCallModel.class);
	
	// Dati impostati dal driver e salvati lato motore
	private StringType 	mainPdfInstanceId = new StringType(); // Chiave di raggruppamento dati della tabella PDF_SRV_DISPOSITIVA_BMED_DATA
	private StringType 	tipoDispositiva = new StringType(); // Identifica il tipo. Utile per le dispo aggiuntive, viene salvato sulla tabella PDF_SRV_DISPOSITIVA_BMED_DATA
	
	// Transcodifiche per singola dispositiva
	private StringType	codProdottoPrit = new StringType();
	private StringType	codOperazionePrit = new StringType();
	
	// Oggetto in response relativo all'elemento dispositiva in input
	private SrvDispositivaResultElemModel dispositivaResult = null;
	
	// Riferimento al pdf, solo per i multiPdf e non le dispoAggiuntive specificate dai driver)
	private int pdfIndex = 0;
	
	/***********************************************************************************************/
	// Per far riferimento ai dati globali, ripetuti in tutte le dispositive
	/***********************************************************************************************/
	public void initGlobalData(SrvDispositivaCallModel globalData) {
		this.globalData = globalData;
	}

	/***********************************************************************************************/
	// Dati globali, ripetuti in tutte le dispositive
	/***********************************************************************************************/
	public StringType getCodiceModalitaRichiestaDispositiva() {
		return globalData == null ? new StringType() : globalData.getCodiceModalitaRichiestaDispositiva();
	}
	public StringType getCodiceTipoModalitaFirma() {
		return globalData == null ? new StringType() : globalData.getCodiceTipoModalitaFirma();
	}
	public StringType getCodiceCanaleOperativo() {
		return globalData == null ? new StringType() : globalData.getCodiceCanaleOperativo();
	}
	public StringType getCodiceOperazioneDispositiva() {
		return globalData == null ? new StringType() : globalData.getCodiceOperazioneDispositiva();
	}
	public StringType getDataSottoscrizione() {
		return globalData == null ? new StringType() : globalData.getDataSottoscrizione();
	}
	public StringType getCodiceGuidDocumento() {
		return globalData == null ? new StringType() : globalData.getCodiceGuidDocumento();
	}
	public StringType getCodiceUploadMulticanaleDocumento() {
		return globalData == null ? new StringType() : globalData.getCodiceUploadMulticanaleDocumento();
	}
	public StringType getDescrizioneXmlDocumento() {
		return globalData == null ? new StringType() : globalData.getDescrizioneXmlDocumento();
	}
	public StringType getDescrizioneDocumento() {
		return globalData == null ? new StringType() : globalData.getDescrizioneDocumento();
	}
	public StringType getDenominazioneFileDocumento() {
		return globalData == null ? new StringType() : globalData.getDenominazioneFileDocumento();
	}
	public StringType getCodTipoSistOrigDisp() {
		return globalData == null ? new StringType() : globalData.getCodTipoSistOrigDisp();
	}
	public IntegerType getCodiceCarrello() {
		return globalData == null ? new IntegerType() : globalData.getCodiceCarrello();
	}
	public StringType getCodiceReportAdeguatezza() {
		return globalData == null ? new StringType() : globalData.getCodiceReportAdeguatezza();
	}
	public ListType getListaControlli() {
		return globalData == null ? new ListType() : globalData.getListaControlli();
	}
	public ListType getListaDomandeCora() {
		return globalData == null ? new ListType() : globalData.getListaDomandeCora();
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	
	public StringType getDescrizioneNote() {
		return descrizioneNote;
	}
	public void setDescrizioneNote(StringType descrizioneNote) {
		this.descrizioneNote = descrizioneNote;
	}
	public StringType getCodiceOperazioneDispositivaSistemaOrigine() {
		return codiceOperazioneDispositivaSistemaOrigine;
	}
	public void setCodiceOperazioneDispositivaSistemaOrigine(StringType codiceOperazioneDispositivaSistemaOrigine) {
		this.codiceOperazioneDispositivaSistemaOrigine = codiceOperazioneDispositivaSistemaOrigine;
	}
	public StringType getCodiceTipoOperazioneProdottoServizio() {
		return codiceTipoOperazioneProdottoServizio;
	}
	public void setCodiceTipoOperazioneProdottoServizio(StringType codiceTipoOperazioneProdottoServizio) {
		this.codiceTipoOperazioneProdottoServizio = codiceTipoOperazioneProdottoServizio;
	}
	public StringType getCodiceSurrogatoProdottoServizio() {
		return codiceSurrogatoProdottoServizio;
	}
	public void setCodiceSurrogatoProdottoServizio(StringType codiceSurrogatoProdottoServizio) {
		this.codiceSurrogatoProdottoServizio = codiceSurrogatoProdottoServizio;
	}
	public StringType getContrNRapportoRiferimento() {
		return contrNRapportoRiferimento;
	}
	public void setContrNRapportoRiferimento(StringType contrNRapportoRiferimento) {
		this.contrNRapportoRiferimento = contrNRapportoRiferimento;
	}
	public DoubleType getValoreDispositiva() {
		return valoreDispositiva;
	}
	public void setValoreDispositiva(DoubleType valoreDispositiva) {
		this.valoreDispositiva = valoreDispositiva;
	}
	public StringType getCodiceTipoValoreDispositiva() {
		return codiceTipoValoreDispositiva;
	}
	public void setCodiceTipoValoreDispositiva(StringType codiceTipoValoreDispositiva) {
		this.codiceTipoValoreDispositiva = codiceTipoValoreDispositiva;
	}
	public StringType getNumeroProposta() {
		return numeroProposta;
	}
	public void setNumeroProposta(StringType numeroProposta) {
		this.numeroProposta = numeroProposta;
	}
	public StringType getCodicePropostaCorrelata() {
		return codicePropostaCorrelata;
	}
	public void setCodicePropostaCorrelata(StringType codicePropostaCorrelata) {
		this.codicePropostaCorrelata = codicePropostaCorrelata;
	}
	public StringType getCodiceBarcode() {
		return codiceBarcode;
	}
	public void setCodiceBarcode(StringType codiceBarcode) {
		this.codiceBarcode = codiceBarcode;
	}
	public ListType getSoggetti() {
		return soggetti;
	}
	public void setSoggetti(ListType soggetti) {
		this.soggetti = soggetti;
	}
	public StringType getCodProdottoPrit() {
		return codProdottoPrit;
	}
	public void setCodProdottoPrit(StringType codProdottoPrit) {
		this.codProdottoPrit = codProdottoPrit;
	}
	public StringType getCodOperazionePrit() {
		return codOperazionePrit;
	}
	public void setCodOperazionePrit(StringType codOperazionePrit) {
		this.codOperazionePrit = codOperazionePrit;
	}

	public StringType getTipoDispositiva() {
		return tipoDispositiva;
	}

	public void setTipoDispositiva(StringType tipoDispositiva) {
		this.tipoDispositiva = tipoDispositiva;
	}

	public StringType getMainPdfInstanceId() {
		return mainPdfInstanceId;
	}

	public void setMainPdfInstanceId(StringType mainPdfInstanceId) {
		this.mainPdfInstanceId = mainPdfInstanceId;
	}

	public SrvDispositivaResultElemModel getDispositivaResult() {
		return dispositivaResult;
	}

	public void setDispositivaResult(SrvDispositivaResultElemModel dispositivaResult) {
		this.dispositivaResult = dispositivaResult;
	}

	public StringType getCodiceScopoRapporto() {
		return codiceScopoRapporto;
	}

	public void setCodiceScopoRapporto(StringType codiceScopoRapporto) {
		this.codiceScopoRapporto = codiceScopoRapporto;
	}

	public int getPdfIndex() {
		return pdfIndex;
	}

	public void setPdfIndex(int pdfIndex) {
		this.pdfIndex = pdfIndex;
	}
	
	public ListType getAllegati() {
		return allegati;
	}

	public void setAllegati(ListType allegati) {
		this.allegati = allegati;
	}

	public StringType getCodiceProcessoOperativo() {
		return codiceProcessoOperativo;
	}

	public void setCodiceProcessoOperativo(StringType codiceProcessoOperativo) {
		this.codiceProcessoOperativo = codiceProcessoOperativo;
	}

	public StringType getCodTipoDocum() {
		return codTipoDocum;
	}

	public void setCodTipoDocum(StringType codTipoDocum) {
		this.codTipoDocum = codTipoDocum;
	}
	
}
