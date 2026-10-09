package prgm.pdfwebforms.mom;

import java.util.UUID;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.nasstorage.SaveFileInfo;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.aml.model.CoraModel;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SrvDispositivaCallModel extends CommandDataModel {

	// Header
	private StringType  	uuid = new StringType();
	private StringType  	timestamp = new StringType();
	private StringType  	canale = new StringType();
	private StringType  	userId = new StringType();

	// Input unico per tutte le dispositive
	private StringType 	codiceModalitaRichiestaDispositiva = new StringType();
	private StringType 	codiceTipoModalitaFirma = new StringType();
	private StringType 	codiceCanaleOperativo = new StringType();
	private StringType 	codiceOperazioneDispositiva = new StringType(); // Codice Dispositiva BMED (ChiaveK) in input: per ora non gestibile come input differenziato sulle enne dispositive
	private StringType 	dataSottoscrizione = new StringType();
	private StringType 	codiceGuidDocumento = new StringType();
	private StringType 	codiceUploadMulticanaleDocumento = new StringType();
	private StringType 	descrizioneXmlDocumento = new StringType();
	private StringType 	descrizioneDocumento = new StringType();
	private StringType 	denominazioneFileDocumento = new StringType();
	private StringType 	codTipoSistOrigDisp = new StringType("PRT");
	private IntegerType codiceCarrello = new IntegerType();
	private StringType 	codiceReportAdeguatezza = new StringType();
	private ListType	listaControlli = new ListType(SrvListaControlliElemModel.class);
	private ListType	listaDomandeCora = new ListType(SrvListaDomandeCoraElemModel.class);
	
	// Input 
	private StringType 	azione = new StringType();
	private ListType	elencoDispositive = new ListType(SrvDispositivaElemModel.class);
	private ListType	elencoVincoliDispositive = new ListType(SrvVincoloDispositivaElemModel.class);
	
	// Output
	private StringType	esito = new StringType();
	private ListType	elencoDispositiveResult = new ListType(SrvDispositivaResultElemModel.class);
	
	// Eccezioni MOM
	private StringType  codProd = new StringType();
	private StringType	chiaveEccezioniMom = new StringType();
	private StringType	codProdottoPrit = new StringType();
	private StringType	codOperazionePrit = new StringType();
	
	// Messaggio di esito gliobale dell'interazione con il sevizio. Se null -> tutto ok
	private String		callSrvErrorMessage = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getCodDispositivaBMED() {
		if(getElencoDispositiveResult().size() == 0)
			return new StringType();
		SrvDispositivaResultElemModel mainDispoResult = (SrvDispositivaResultElemModel)getElencoDispositiveResult().get(0);
		return mainDispoResult.getCodiceOperazioneDispositiva();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getCodDispositivaBMED(int pdfIndex) {
		for(int i=0; i<getElencoDispositiveResult().size(); i++) {
			SrvDispositivaElemModel dispoElem = (SrvDispositivaElemModel)getElencoDispositive().get(i);
			if(dispoElem.getPdfIndex() == pdfIndex) {
				SrvDispositivaResultElemModel dispoResult = dispoElem.getDispositivaResult();
				return dispoResult.getCodiceOperazioneDispositiva();
			}
		}
		return new StringType();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initCallModelFromCSS(ClientSessionContext csc) { 
		if(csc.getLoginName() != null)
			setUserId(new StringType(csc.getLoginName().toLowerCase()));
		else
			setUserId(new StringType(csc.getUserCode()));
		if(csc.isSede())
			setCanale(new StringType("ITR"));
		else
			setCanale(new StringType("ITN"));
		setCodiceCanaleOperativo(new StringType(csc.getUserType().equals(ClientSessionContext.USER_TYPE_SEDE) ? "S" : "R"));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initCallModel(PdfInstanceModel pdfInstance, PdfModel pdf, SaveFileInfo saveFileInfo,
							  String azione, boolean isProcessononVirtuoso) {
		
		setUuid(new StringType(UUID.randomUUID().toString()));
		setTimestamp(new StringType(""+new java.util.Date().getTime()));
		setAzione(new StringType(azione));
		
		setCodiceOperazioneDispositiva(pdfInstance.getCodDispositivaBMED());
		setCodiceCarrello(pdfInstance.getIdCarrello());
		setCodiceReportAdeguatezza(pdfInstance.getIdReportAdeguatezza());

		String codiceModalitaRichDisp = pdfInstance.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) ? "3" : "2";
		if(isProcessononVirtuoso)
			codiceModalitaRichDisp = "1";
		setCodiceModalitaRichiestaDispositiva(new StringType(codiceModalitaRichDisp));

		String codTipModFirma = "00001";
		if(pdfInstance.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) ||
		   pdfInstance.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE))
			codTipModFirma = "00002";
		else if(pdfInstance.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE))
			codTipModFirma = "00004";
		setCodiceTipoModalitaFirma(new StringType(codTipModFirma));

		String dataSott = Tools.today().getAA()+"-"+Tools.today().getMM()+"-"+Tools.today().getGG();
		DateType ds = (DateType)pdf.mainPdfData().readProperty(PdfPredefinedFields.DATA_SOTTOSCRIZIONE);
		if(ds != null && !ds.isNull())
			dataSott = ds.getAA()+"-"+ds.getMM()+"-"+ds.getGG();
		setDataSottoscrizione(new StringType(dataSott+"T00:00:00"));

		setCodiceGuidDocumento(new StringType(pdf.getCodiceGuidDocumento()));
		setCodiceUploadMulticanaleDocumento(new StringType(saveFileInfo.getIdFile()));
		String descrDoc = pdfInstance.getPdfAnag().getPdfDescr().toString();
		if(descrDoc.length() > 250)
			descrDoc = descrDoc.substring(0, 250);
		setDescrizioneDocumento(new StringType(descrDoc));
		setDenominazioneFileDocumento(new StringType(saveFileInfo.getFileName()));
		
		initCallModelListaControlli(pdfInstance, pdf);
		initCallModelListaDomandeCora(pdf);
		
		if(azione.equals("risottometti"))
			setCodTipoSistOrigDisp(new StringType());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void initCallModelListaControlli(PdfInstanceModel pdfInstance, PdfModel pdf) {
		
		getListaControlli().clear();
		
		if(!pdfInstance.getIdSostituzione().isNull()) {
			SrvListaControlliElemModel ctrl = new SrvListaControlliElemModel();
			ctrl.setTipoControllo(new StringType("M01"));
			ctrl.setIdControllo(pdfInstance.getIdSostituzione());
			getListaControlli().add(ctrl);
		}

		if(pdf.getMifidCallModel() != null && !pdf.getMifidCallModel().getIdEsito().isNull()) {
			SrvListaControlliElemModel ctrl = new SrvListaControlliElemModel();
			ctrl.setTipoControllo(new StringType("M05"));
			ctrl.setIdControllo(pdf.getMifidCallModel().getIdEsito());
			getListaControlli().add(ctrl);
		}
		
		if(!pdfInstance.getIdReportAdeguatezza().isNull()) {
			SrvListaControlliElemModel ctrl = new SrvListaControlliElemModel();
			ctrl.setTipoControllo(new StringType("M04"));
			ctrl.setIdControllo(pdfInstance.getIdReportAdeguatezza());
			getListaControlli().add(ctrl);
		}

		if(pdf.getIddCallModel() != null && !pdf.getIddCallModel().getIdQuestionarioIdd().isNull()) {
			SrvListaControlliElemModel ctrl = new SrvListaControlliElemModel();
			ctrl.setTipoControllo(new StringType("M07"));
			ctrl.setIdControllo(pdf.getIddCallModel().getIdQuestionarioIdd());
			getListaControlli().add(ctrl);
		}
		
		if(pdf.getIddCallModel() != null && !pdf.getIddCallModel().getIdRaccomandazioneIdd().isNull()) {
			SrvListaControlliElemModel ctrl = new SrvListaControlliElemModel();
			ctrl.setTipoControllo(new StringType("M08"));
			ctrl.setIdControllo(pdf.getIddCallModel().getIdRaccomandazioneIdd());
			getListaControlli().add(ctrl);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void initCallModelListaDomandeCora(PdfModel pdf) {

		getListaDomandeCora().clear();
		
		// Il cora lo aggiungiamo solo per le dispo del basket che sono presenti nella lista del modello 
		// In caso di basket il modello CoraModel viene impostato in tutte le dispo a prescindere. 
		// In caso di accettazione copernico viene impostato negli xml delle specifiche dispo
		CoraModel cm = pdf.getCoraModel();
		if(cm != null && (cm.getDispoHasCora() != null || pdf == cm.findPdf(pdf))) {
			SrvListaDomandeCoraElemModel cora = new SrvListaDomandeCoraElemModel();
			cora.setCodiceDomanda(new StringType("1"));
			cora.setCodiceRisposta(pdf.getCoraModel().getCodRating());
			getListaDomandeCora().add(cora);
		}
	}
	
	public StringType getUuid() {
		return uuid;
	}

	public void setUuid(StringType uuid) {
		this.uuid = uuid;
	}

	public StringType getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(StringType timestamp) {
		this.timestamp = timestamp;
	}

	public StringType getCanale() {
		return canale;
	}

	public void setCanale(StringType canale) {
		this.canale = canale;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

	public StringType getAzione() {
		return azione;
	}

	public void setAzione(StringType azione) {
		this.azione = azione;
	}

	public StringType getCodiceModalitaRichiestaDispositiva() {
		return codiceModalitaRichiestaDispositiva;
	}

	public void setCodiceModalitaRichiestaDispositiva(StringType codiceModalitaRichiestaDispositiva) {
		this.codiceModalitaRichiestaDispositiva = codiceModalitaRichiestaDispositiva;
	}

	public StringType getCodiceTipoModalitaFirma() {
		return codiceTipoModalitaFirma;
	}

	public void setCodiceTipoModalitaFirma(StringType codiceTipoModalitaFirma) {
		this.codiceTipoModalitaFirma = codiceTipoModalitaFirma;
	}

	public StringType getCodiceCanaleOperativo() {
		return codiceCanaleOperativo;
	}

	public void setCodiceCanaleOperativo(StringType codiceCanaleOperativo) {
		this.codiceCanaleOperativo = codiceCanaleOperativo;
	}

	public StringType getCodiceOperazioneDispositiva() {
		return codiceOperazioneDispositiva;
	}

	public void setCodiceOperazioneDispositiva(StringType codiceOperazioneDispositiva) {
		this.codiceOperazioneDispositiva = codiceOperazioneDispositiva;
	}

	public StringType getDataSottoscrizione() {
		return dataSottoscrizione;
	}

	public void setDataSottoscrizione(StringType dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}

	public StringType getCodiceGuidDocumento() {
		return codiceGuidDocumento;
	}

	public void setCodiceGuidDocumento(StringType codiceGuidDocumento) {
		this.codiceGuidDocumento = codiceGuidDocumento;
	}

	public StringType getCodiceUploadMulticanaleDocumento() {
		return codiceUploadMulticanaleDocumento;
	}

	public void setCodiceUploadMulticanaleDocumento(StringType codiceUploadMulticanaleDocumento) {
		this.codiceUploadMulticanaleDocumento = codiceUploadMulticanaleDocumento;
	}

	public StringType getDescrizioneXmlDocumento() {
		return descrizioneXmlDocumento;
	}

	public void setDescrizioneXmlDocumento(StringType descrizioneXmlDocumento) {
		this.descrizioneXmlDocumento = descrizioneXmlDocumento;
	}

	public StringType getDescrizioneDocumento() {
		return descrizioneDocumento;
	}

	public void setDescrizioneDocumento(StringType descrizioneDocumento) {
		this.descrizioneDocumento = descrizioneDocumento;
	}

	public StringType getDenominazioneFileDocumento() {
		return denominazioneFileDocumento;
	}

	public void setDenominazioneFileDocumento(StringType denominazioneFileDocumento) {
		this.denominazioneFileDocumento = denominazioneFileDocumento;
	}

	public ListType getElencoDispositive() {
		return elencoDispositive;
	}

	public void setElencoDispositive(ListType elencoDispositive) {
		this.elencoDispositive = elencoDispositive;
	}

	public StringType getCodProd() {
		return codProd;
	}

	public void setCodProd(StringType codProd) {
		this.codProd = codProd;
	}

	public StringType getEsito() {
		return esito;
	}

	public void setEsito(StringType esito) {
		this.esito = esito;
	}

	public ListType getElencoDispositiveResult() {
		return elencoDispositiveResult;
	}

	public void setElencoDispositiveResult(ListType elencoDispositiveResult) {
		this.elencoDispositiveResult = elencoDispositiveResult;
	}

	public StringType getChiaveEccezioniMom() {
		return chiaveEccezioniMom;
	}

	public void setChiaveEccezioniMom(StringType chiaveEccezioniMom) {
		this.chiaveEccezioniMom = chiaveEccezioniMom;
	}

	public ListType getElencoVincoliDispositive() {
		return elencoVincoliDispositive;
	}

	public void setElencoVincoliDispositive(ListType elencoVincoliDispositive) {
		this.elencoVincoliDispositive = elencoVincoliDispositive;
	}

	public String getCallSrvErrorMessage() {
		return callSrvErrorMessage;
	}

	public void setCallSrvErrorMessage(String callSrvErrorMessage) {
		this.callSrvErrorMessage = callSrvErrorMessage;
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

	public StringType getCodTipoSistOrigDisp() {
		return codTipoSistOrigDisp;
	}

	public void setCodTipoSistOrigDisp(StringType codTipoSistOrigDisp) {
		this.codTipoSistOrigDisp = codTipoSistOrigDisp;
	}

	public IntegerType getCodiceCarrello() {
		return codiceCarrello;
	}

	public void setCodiceCarrello(IntegerType codiceCarrello) {
		this.codiceCarrello = codiceCarrello;
	}

	public StringType getCodiceReportAdeguatezza() {
		return codiceReportAdeguatezza;
	}

	public void setCodiceReportAdeguatezza(StringType codiceReportAdeguatezza) {
		this.codiceReportAdeguatezza = codiceReportAdeguatezza;
	}

	public ListType getListaControlli() {
		return listaControlli;
	}

	public void setListaControlli(ListType listaControlli) {
		this.listaControlli = listaControlli;
	}

	public ListType getListaDomandeCora() {
		return listaDomandeCora;
	}

	public void setListaDomandeCora(ListType listaDomandeCora) {
		this.listaDomandeCora = listaDomandeCora;
	}

}
