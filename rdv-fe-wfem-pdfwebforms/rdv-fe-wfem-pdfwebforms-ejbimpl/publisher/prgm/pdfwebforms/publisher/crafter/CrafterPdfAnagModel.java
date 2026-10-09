package prgm.pdfwebforms.publisher.crafter;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/*******************************************************************/
/*******************************************************************/
public class CrafterPdfAnagModel extends CommandDataModel {
	
	private StringType  			pdfId = new StringType();
	private StringType  			pdfCode = new StringType();	// optional id "id" is setted
	private StringType  			pdfDescr = new StringType();
	
	private StringType  			pdfArea = new StringType();
	private DateType				pdfStartDate = new DateType();
	private DateType				pdfEndDate = new DateType();
	
	private BooleanType				pdfIsCartaLiberaEnabled = new BooleanType();
	private BooleanType				pdfIsCartaChimicaEnabled = new BooleanType();
	private BooleanType				pdfIsFirmaDigitaleEnabled = new BooleanType();
	private BooleanType				pdfIsCopernicoEnabled = new BooleanType();
	private BooleanType				pdfIsStampaEnabled = new BooleanType();
	
	private IntegerType				pdfCodProdottoPrit = new IntegerType();
	private IntegerType				pdfCodOperazionePrit = new IntegerType();
	private StringType 				pdfMomCode = new StringType();
	
	private IntegerType				pdfNumCopie = new IntegerType(1);
	private StringType  			pdfTestoCopia1 = new StringType();
	private StringType  			pdfTestoCopia2 = new StringType();
	private StringType  			pdfTestoCopia3 = new StringType();
	private StringType  			pdfTestoCopia4 = new StringType();

	private StringType 				externalLinkOnSignUrl = new StringType();
	private StringType 				externalLinkOnSignLabel = new StringType();
	
	private BooleanType				downloadAsFacsimile = new BooleanType();

	private BooleanType				sendCliSmsOnSign = new BooleanType();
	private StringType 				cliSmsOnSignText = new StringType();
	private BooleanType				sendFBMailOnSign = new BooleanType();
	private BooleanType				isControlliCompleti = new BooleanType();
	private BooleanType				signAll = new BooleanType();
	
	private BooleanType				isProspectEnabledOnCli1 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli2 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli3 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli4 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli5 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli6 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli7 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli8 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli9 = new BooleanType();

	private BooleanType 			facSimileOnPreview = new BooleanType();
	private BooleanType 			callSrvDispositivaBMED = new BooleanType();
	private BooleanType				hasDataSottoscrizioneOggi = new BooleanType();
	private BooleanType				hasPriips = new BooleanType();
	
	private StringType 				pdfCreationUser = new StringType();
	private TimestampType			pdfCreationTime = new TimestampType();
	private StringType 				pdfLastModUser = new StringType();
	private TimestampType			pdfLastModTime = new TimestampType();

	private StringType 				pdfDriverName = new StringType();

	private BooleanType				isTipoSoggettoPersonaFisica = new BooleanType();
	private BooleanType				isTipoSoggettoProfessional = new BooleanType();
	private BooleanType				isTipoSoggettoPersonaGiuridica = new BooleanType();
	private BooleanType				isTipoSoggettoFamilyBanker = new BooleanType();
	private BooleanType				isUsableInCrafterWayout = new BooleanType();
	
	private StringType 				pdfCodLineaBusiness = new StringType();
	private StringType 				pdfCodFaseCommerciale = new StringType();
	
	private ListType				elencoRuoliUtilizzatori = new ListType(CrafterPdfAnagRuoloUtilizzatoreModel.class);
	private ListType				pdfPubblicationList = new ListType(CrafterPdfPublicationModel.class);
	
	public StringType getPdfId() {
		return pdfId;
	}

	public void setPdfId(StringType pdfId) {
		this.pdfId = pdfId;
	}

	public StringType getPdfCode() {
		return pdfCode;
	}

	public void setPdfCode(StringType pdfCode) {
		this.pdfCode = pdfCode;
	}

	public StringType getPdfDescr() {
		return pdfDescr;
	}

	public void setPdfDescr(StringType pdfDescr) {
		this.pdfDescr = pdfDescr;
	}

	public DateType getPdfStartDate() {
		return pdfStartDate;
	}

	public void setPdfStartDate(DateType pdfStartDate) {
		this.pdfStartDate = pdfStartDate;
	}

	public DateType getPdfEndDate() {
		return pdfEndDate;
	}

	public void setPdfEndDate(DateType pdfEndDate) {
		this.pdfEndDate = pdfEndDate;
	}

	public BooleanType getPdfIsCartaLiberaEnabled() {
		return pdfIsCartaLiberaEnabled;
	}

	public void setPdfIsCartaLiberaEnabled(BooleanType pdfIsCartaLiberaEnabled) {
		this.pdfIsCartaLiberaEnabled = pdfIsCartaLiberaEnabled;
	}

	public BooleanType getPdfIsCartaChimicaEnabled() {
		return pdfIsCartaChimicaEnabled;
	}

	public void setPdfIsCartaChimicaEnabled(BooleanType pdfIsCartaChimicaEnabled) {
		this.pdfIsCartaChimicaEnabled = pdfIsCartaChimicaEnabled;
	}

	public BooleanType getPdfIsFirmaDigitaleEnabled() {
		return pdfIsFirmaDigitaleEnabled;
	}

	public void setPdfIsFirmaDigitaleEnabled(BooleanType pdfIsFirmaDigitaleEnabled) {
		this.pdfIsFirmaDigitaleEnabled = pdfIsFirmaDigitaleEnabled;
	}

	public BooleanType getPdfIsCopernicoEnabled() {
		return pdfIsCopernicoEnabled;
	}

	public void setPdfIsCopernicoEnabled(BooleanType pdfIsCopernicoEnabled) {
		this.pdfIsCopernicoEnabled = pdfIsCopernicoEnabled;
	}

	public BooleanType getPdfIsStampaEnabled() {
		return pdfIsStampaEnabled;
	}

	public void setPdfIsStampaEnabled(BooleanType pdfIsStampaEnabled) {
		this.pdfIsStampaEnabled = pdfIsStampaEnabled;
	}

	public IntegerType getPdfCodProdottoPrit() {
		return pdfCodProdottoPrit;
	}

	public void setPdfCodProdottoPrit(IntegerType pdfCodProdottoPrit) {
		this.pdfCodProdottoPrit = pdfCodProdottoPrit;
	}

	public IntegerType getPdfCodOperazionePrit() {
		return pdfCodOperazionePrit;
	}

	public void setPdfCodOperazionePrit(IntegerType pdfCodOperazionePrit) {
		this.pdfCodOperazionePrit = pdfCodOperazionePrit;
	}

	public StringType getPdfMomCode() {
		return pdfMomCode;
	}

	public void setPdfMomCode(StringType pdfMomCode) {
		this.pdfMomCode = pdfMomCode;
	}

	public IntegerType getPdfNumCopie() {
		return pdfNumCopie;
	}

	public void setPdfNumCopie(IntegerType pdfNumCopie) {
		this.pdfNumCopie = pdfNumCopie;
	}

	public StringType getPdfTestoCopia1() {
		return pdfTestoCopia1;
	}

	public void setPdfTestoCopia1(StringType pdfTestoCopia1) {
		this.pdfTestoCopia1 = pdfTestoCopia1;
	}

	public StringType getPdfTestoCopia2() {
		return pdfTestoCopia2;
	}

	public void setPdfTestoCopia2(StringType pdfTestoCopia2) {
		this.pdfTestoCopia2 = pdfTestoCopia2;
	}

	public StringType getPdfTestoCopia3() {
		return pdfTestoCopia3;
	}

	public void setPdfTestoCopia3(StringType pdfTestoCopia3) {
		this.pdfTestoCopia3 = pdfTestoCopia3;
	}

	public StringType getPdfTestoCopia4() {
		return pdfTestoCopia4;
	}

	public void setPdfTestoCopia4(StringType pdfTestoCopia4) {
		this.pdfTestoCopia4 = pdfTestoCopia4;
	}

	public StringType getExternalLinkOnSignUrl() {
		return externalLinkOnSignUrl;
	}

	public void setExternalLinkOnSignUrl(StringType externalLinkOnSignUrl) {
		this.externalLinkOnSignUrl = externalLinkOnSignUrl;
	}

	public StringType getExternalLinkOnSignLabel() {
		return externalLinkOnSignLabel;
	}

	public void setExternalLinkOnSignLabel(StringType externalLinkOnSignLabel) {
		this.externalLinkOnSignLabel = externalLinkOnSignLabel;
	}

	public BooleanType getDownloadAsFacsimile() {
		return downloadAsFacsimile;
	}

	public void setDownloadAsFacsimile(BooleanType downloadAsFacsimile) {
		this.downloadAsFacsimile = downloadAsFacsimile;
	}

	public BooleanType getSendCliSmsOnSign() {
		return sendCliSmsOnSign;
	}

	public void setSendCliSmsOnSign(BooleanType sendCliSmsOnSign) {
		this.sendCliSmsOnSign = sendCliSmsOnSign;
	}

	public StringType getCliSmsOnSignText() {
		return cliSmsOnSignText;
	}

	public void setCliSmsOnSignText(StringType cliSmsOnSignText) {
		this.cliSmsOnSignText = cliSmsOnSignText;
	}

	public BooleanType getSendFBMailOnSign() {
		return sendFBMailOnSign;
	}

	public void setSendFBMailOnSign(BooleanType sendFBMailOnSign) {
		this.sendFBMailOnSign = sendFBMailOnSign;
	}

	public BooleanType getIsControlliCompleti() {
		return isControlliCompleti;
	}

	public void setIsControlliCompleti(BooleanType isControlliCompleti) {
		this.isControlliCompleti = isControlliCompleti;
	}

	public BooleanType getIsProspectEnabledOnCli1() {
		return isProspectEnabledOnCli1;
	}

	public void setIsProspectEnabledOnCli1(BooleanType isProspectEnabledOnCli1) {
		this.isProspectEnabledOnCli1 = isProspectEnabledOnCli1;
	}

	public BooleanType getIsProspectEnabledOnCli2() {
		return isProspectEnabledOnCli2;
	}

	public void setIsProspectEnabledOnCli2(BooleanType isProspectEnabledOnCli2) {
		this.isProspectEnabledOnCli2 = isProspectEnabledOnCli2;
	}

	public BooleanType getIsProspectEnabledOnCli3() {
		return isProspectEnabledOnCli3;
	}

	public void setIsProspectEnabledOnCli3(BooleanType isProspectEnabledOnCli3) {
		this.isProspectEnabledOnCli3 = isProspectEnabledOnCli3;
	}

	public BooleanType getIsProspectEnabledOnCli4() {
		return isProspectEnabledOnCli4;
	}

	public void setIsProspectEnabledOnCli4(BooleanType isProspectEnabledOnCli4) {
		this.isProspectEnabledOnCli4 = isProspectEnabledOnCli4;
	}

	public BooleanType getIsProspectEnabledOnCli5() {
		return isProspectEnabledOnCli5;
	}

	public void setIsProspectEnabledOnCli5(BooleanType isProspectEnabledOnCli5) {
		this.isProspectEnabledOnCli5 = isProspectEnabledOnCli5;
	}

	public BooleanType getIsProspectEnabledOnCli6() {
		return isProspectEnabledOnCli6;
	}

	public void setIsProspectEnabledOnCli6(BooleanType isProspectEnabledOnCli6) {
		this.isProspectEnabledOnCli6 = isProspectEnabledOnCli6;
	}

	public BooleanType getIsProspectEnabledOnCli7() {
		return isProspectEnabledOnCli7;
	}

	public void setIsProspectEnabledOnCli7(BooleanType isProspectEnabledOnCli7) {
		this.isProspectEnabledOnCli7 = isProspectEnabledOnCli7;
	}

	public BooleanType getIsProspectEnabledOnCli8() {
		return isProspectEnabledOnCli8;
	}

	public void setIsProspectEnabledOnCli8(BooleanType isProspectEnabledOnCli8) {
		this.isProspectEnabledOnCli8 = isProspectEnabledOnCli8;
	}

	public BooleanType getIsProspectEnabledOnCli9() {
		return isProspectEnabledOnCli9;
	}

	public void setIsProspectEnabledOnCli9(BooleanType isProspectEnabledOnCli9) {
		this.isProspectEnabledOnCli9 = isProspectEnabledOnCli9;
	}

	public BooleanType getFacSimileOnPreview() {
		return facSimileOnPreview;
	}

	public void setFacSimileOnPreview(BooleanType facSimileOnPreview) {
		this.facSimileOnPreview = facSimileOnPreview;
	}

	public BooleanType getCallSrvDispositivaBMED() {
		return callSrvDispositivaBMED;
	}

	public void setCallSrvDispositivaBMED(BooleanType callSrvDispositivaBMED) {
		this.callSrvDispositivaBMED = callSrvDispositivaBMED;
	}

	public BooleanType getHasDataSottoscrizioneOggi() {
		return hasDataSottoscrizioneOggi;
	}

	public void setHasDataSottoscrizioneOggi(BooleanType hasDataSottoscrizioneOggi) {
		this.hasDataSottoscrizioneOggi = hasDataSottoscrizioneOggi;
	}

	public BooleanType getHasPriips() {
		return hasPriips;
	}

	public void setHasPriips(BooleanType hasPriips) {
		this.hasPriips = hasPriips;
	}

	public StringType getPdfCreationUser() {
		return pdfCreationUser;
	}

	public void setPdfCreationUser(StringType pdfCreationUser) {
		this.pdfCreationUser = pdfCreationUser;
	}

	public TimestampType getPdfCreationTime() {
		return pdfCreationTime;
	}

	public void setPdfCreationTime(TimestampType pdfCreationTime) {
		this.pdfCreationTime = pdfCreationTime;
	}

	public StringType getPdfLastModUser() {
		return pdfLastModUser;
	}

	public void setPdfLastModUser(StringType pdfLastModUser) {
		this.pdfLastModUser = pdfLastModUser;
	}

	public TimestampType getPdfLastModTime() {
		return pdfLastModTime;
	}

	public void setPdfLastModTime(TimestampType pdfLastModTime) {
		this.pdfLastModTime = pdfLastModTime;
	}

	public StringType getPdfDriverName() {
		return pdfDriverName;
	}

	public void setPdfDriverName(StringType pdfDriverName) {
		this.pdfDriverName = pdfDriverName;
	}

	public ListType getPdfPubblicationList() {
		return pdfPubblicationList;
	}

	public void setPdfPubblicationList(ListType pdfPubblicationList) {
		this.pdfPubblicationList = pdfPubblicationList;
	}

	public BooleanType getSignAll() {
		return signAll;
	}

	public void setSignAll(BooleanType signAll) {
		this.signAll = signAll;
	}

	public BooleanType getIsTipoSoggettoPersonaFisica() {
		return isTipoSoggettoPersonaFisica;
	}

	public void setIsTipoSoggettoPersonaFisica(BooleanType isTipoSoggettoPersonaFisica) {
		this.isTipoSoggettoPersonaFisica = isTipoSoggettoPersonaFisica;
	}

	public BooleanType getIsTipoSoggettoProfessional() {
		return isTipoSoggettoProfessional;
	}

	public void setIsTipoSoggettoProfessional(BooleanType isTipoSoggettoProfessional) {
		this.isTipoSoggettoProfessional = isTipoSoggettoProfessional;
	}

	public BooleanType getIsTipoSoggettoPersonaGiuridica() {
		return isTipoSoggettoPersonaGiuridica;
	}

	public void setIsTipoSoggettoPersonaGiuridica(BooleanType isTipoSoggettoPersonaGiuridica) {
		this.isTipoSoggettoPersonaGiuridica = isTipoSoggettoPersonaGiuridica;
	}

	public BooleanType getIsTipoSoggettoFamilyBanker() {
		return isTipoSoggettoFamilyBanker;
	}

	public void setIsTipoSoggettoFamilyBanker(BooleanType isTipoSoggettoFamilyBanker) {
		this.isTipoSoggettoFamilyBanker = isTipoSoggettoFamilyBanker;
	}

	public BooleanType getIsUsableInCrafterWayout() {
		return isUsableInCrafterWayout;
	}

	public void setIsUsableInCrafterWayout(BooleanType isUsableInCrafterWayout) {
		this.isUsableInCrafterWayout = isUsableInCrafterWayout;
	}

	public StringType getPdfArea() {
		return pdfArea;
	}

	public void setPdfArea(StringType pdfArea) {
		this.pdfArea = pdfArea;
	}

	public StringType getPdfCodLineaBusiness() {
		return pdfCodLineaBusiness;
	}

	public void setPdfCodLineaBusiness(StringType pdfCodLineaBusiness) {
		this.pdfCodLineaBusiness = pdfCodLineaBusiness;
	}

	public StringType getPdfCodFaseCommerciale() {
		return pdfCodFaseCommerciale;
	}

	public void setPdfCodFaseCommerciale(StringType pdfCodFaseCommerciale) {
		this.pdfCodFaseCommerciale = pdfCodFaseCommerciale;
	}

	public ListType getElencoRuoliUtilizzatori() {
		return elencoRuoliUtilizzatori;
	}

	public void setElencoRuoliUtilizzatori(ListType elencoRuoliUtilizzatori) {
		this.elencoRuoliUtilizzatori = elencoRuoliUtilizzatori;
	}

	
}
