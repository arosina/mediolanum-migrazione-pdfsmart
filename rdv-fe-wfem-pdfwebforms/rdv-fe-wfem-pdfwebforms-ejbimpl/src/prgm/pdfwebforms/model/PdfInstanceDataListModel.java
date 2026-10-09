package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.aml.model.CoraModel;
import prgm.pdfwebforms.idd.IddCallModel;
import prgm.pdfwebforms.mifid.MifidCallModel;
import prgm.pdfwebforms.mifid.MifidManlevaDataModel;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCallModel;
import prgm.pdfwebforms.sostituzioni.SostituzioniCallModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfInstanceDataListModel extends CommandDataModel{
	
	private StringType 					pdfTitle = new StringType();
	private BooleanType					isMifidSkipped = null;
	private MifidCallModel 				mifidCallData = null;
	private MifidManlevaDataModel		mifidManlevaData = null;
	private IddCallModel 				iddCallData = null;
	private SostituzioniCallModel		sostituzioniCallData = null;
	private ReportAdeguatezzaCallModel 	reportAdeguatezzaCallData = null;
	private CoraModel					coraModel = null;
	private BooleanType  				hideSaveButton = new BooleanType();
	private StringType  				inviaInSedeButtonLabel = new StringType();
	private StringType  				firmaDigitaleButtonLabel = new StringType();
	private StringType  				copernicoButtonLabel = new StringType();
	private StringType  				pdfCompilationMode = new StringType();
	private PritMomInfoModel			pritMomInfo = null;
	private ListType 					pdfData = new ListType(PdfDataModel.class);
	private ListType 					pdfInstanceAttachments = new ListType(PdfInstanceAttachModel.class);
	private ListType 					pdfInstanceOriginalAttachments = new ListType(PdfInstanceAttachModel.class);
	private StringType  				tipoProcessoSedeMOM = new StringType();
	
	public ListType getPdfData() {
		return pdfData;
	}

	public void setPdfData(ListType pdfData) {
		this.pdfData = pdfData;
	}

	public StringType getPdfTitle() {
		return pdfTitle;
	}

	public void setPdfTitle(StringType pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	public MifidCallModel getMifidCallData() {
		return mifidCallData;
	}

	public void setMifidCallData(MifidCallModel mifidCallData) {
		this.mifidCallData = mifidCallData;
	}

	public ReportAdeguatezzaCallModel getReportAdeguatezzaCallData() {
		return reportAdeguatezzaCallData;
	}

	public void setReportAdeguatezzaCallData(
			ReportAdeguatezzaCallModel reportAdeguatezzaCallData) {
		this.reportAdeguatezzaCallData = reportAdeguatezzaCallData;
	}

	public IddCallModel getIddCallData() {
		return iddCallData;
	}

	public void setIddCallData(IddCallModel iddCallData) {
		this.iddCallData = iddCallData;
	}

	public BooleanType getHideSaveButton() {
		return hideSaveButton;
	}

	public void setHideSaveButton(BooleanType hideSaveButton) {
		this.hideSaveButton = hideSaveButton;
	}

	public PritMomInfoModel getPritMomInfo() {
		return pritMomInfo;
	}

	public void setPritMomInfo(PritMomInfoModel pritMomInfo) {
		this.pritMomInfo = pritMomInfo;
	}

	public StringType getInviaInSedeButtonLabel() {
		return inviaInSedeButtonLabel;
	}

	public void setInviaInSedeButtonLabel(StringType inviaInSedeButtonLabel) {
		this.inviaInSedeButtonLabel = inviaInSedeButtonLabel;
	}

	public StringType getFirmaDigitaleButtonLabel() {
		return firmaDigitaleButtonLabel;
	}

	public void setFirmaDigitaleButtonLabel(StringType firmaDigitaleButtonLabel) {
		this.firmaDigitaleButtonLabel = firmaDigitaleButtonLabel;
	}

	public StringType getCopernicoButtonLabel() {
		return copernicoButtonLabel;
	}

	public void setCopernicoButtonLabel(StringType copernicoButtonLabel) {
		this.copernicoButtonLabel = copernicoButtonLabel;
	}

	public BooleanType getIsMifidSkipped() {
		return isMifidSkipped;
	}

	public void setIsMifidSkipped(BooleanType isMifidSkipped) {
		this.isMifidSkipped = isMifidSkipped;
	}

	public StringType getPdfCompilationMode() {
		return pdfCompilationMode;
	}

	public void setPdfCompilationMode(StringType pdfCompilationMode) {
		this.pdfCompilationMode = pdfCompilationMode;
	}

	public SostituzioniCallModel getSostituzioniCallData() {
		return sostituzioniCallData;
	}

	public void setSostituzioniCallData(SostituzioniCallModel sostituzioniCallData) {
		this.sostituzioniCallData = sostituzioniCallData;
	}

	public ListType getPdfInstanceAttachments() {
		return pdfInstanceAttachments;
	}

	public void setPdfInstanceAttachments(ListType pdfInstanceAttachments) {
		this.pdfInstanceAttachments = pdfInstanceAttachments;
	}

	public StringType getTipoProcessoSedeMOM() {
		return tipoProcessoSedeMOM;
	}

	public void setTipoProcessoSedeMOM(StringType tipoProcessoSedeMOM) {
		this.tipoProcessoSedeMOM = tipoProcessoSedeMOM;
	}

	public ListType getPdfInstanceOriginalAttachments() {
		return pdfInstanceOriginalAttachments;
	}

	public void setPdfInstanceOriginalAttachments(ListType pdfInstanceOriginalAttachments) {
		this.pdfInstanceOriginalAttachments = pdfInstanceOriginalAttachments;
	}

	public CoraModel getCoraModel() {
		return coraModel;
	}

	public void setCoraModel(CoraModel coraModel) {
		this.coraModel = coraModel;
	}

	public MifidManlevaDataModel getMifidManlevaData() {
		return mifidManlevaData;
	}

	public void setMifidManlevaData(MifidManlevaDataModel mifidManlevaData) {
		this.mifidManlevaData = mifidManlevaData;
	}
}
