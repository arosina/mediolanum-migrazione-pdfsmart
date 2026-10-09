package prgm.pdfwebforms.catalog;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfCatalogModel extends MapCommandDataModel {
	
	public static String AREA_CATALOGO_MODULI = "CATALOGO_MODULI";
	public static String AREA_CATALOGO_OPERAZIONI = "CATALOGO_OPERAZIONI";
	
	private boolean primaVolta = true;
	
	private boolean filtered = false;
	private String filterTitle = "";
	
	private StringType 				selectedTab = new StringType("pdfListCont");
	
	private PdfCatalogParamsModel 	pdfListParams = new PdfCatalogParamsModel(); 
	private ListType 				pdfList = new ListType(PdfAnagModel.class);
	
	private PdfCatalogParamsModel 	pdfDraftListParams = new PdfCatalogParamsModel(); 
	private ListType 				pdfDraftList = new ListType(PdfInstanceModel.class);
	
	private PdfCatalogParamsModel 	pdfCompletedListParams = new PdfCatalogParamsModel(); 
	private ListType 				pdfCompletedList = new ListType(PdfInstanceModel.class);
	
	private PdfAnagModel			dettaglioModulo = new PdfAnagModel();
	private BooleanType				refreshDraft = new BooleanType();
	private StringType				idToDelete = new StringType();

	public ListType getPdfList() {
		return pdfList;
	}

	public void setPdfList(ListType pdfList) {
		this.pdfList = pdfList;
	}

	public boolean isPrimaVolta() {
		return primaVolta;
	}

	public void setPrimaVolta(boolean primaVolta) {
		this.primaVolta = primaVolta;
	}

	public PdfAnagModel getDettaglioModulo() {
		return dettaglioModulo;
	}

	public void setDettaglioModulo(PdfAnagModel dettaglioModulo) {
		this.dettaglioModulo = dettaglioModulo;
	}

	public ListType getPdfDraftList() {
		return pdfDraftList;
	}

	public void setPdfDraftList(ListType pdfDraftList) {
		this.pdfDraftList = pdfDraftList;
	}

	public ListType getPdfCompletedList() {
		return pdfCompletedList;
	}

	public void setPdfCompletedList(ListType pdfCompletedList) {
		this.pdfCompletedList = pdfCompletedList;
	}

	public PdfCatalogParamsModel getPdfListParams() {
		return pdfListParams;
	}

	public void setPdfListParams(PdfCatalogParamsModel pdfListParams) {
		this.pdfListParams = pdfListParams;
	}

	public PdfCatalogParamsModel getPdfDraftListParams() {
		return pdfDraftListParams;
	}

	public void setPdfDraftListParams(PdfCatalogParamsModel pdfDraftListParams) {
		this.pdfDraftListParams = pdfDraftListParams;
	}

	public PdfCatalogParamsModel getPdfCompletedListParams() {
		return pdfCompletedListParams;
	}

	public void setPdfCompletedListParams(
			PdfCatalogParamsModel pdfCompletedListParams) {
		this.pdfCompletedListParams = pdfCompletedListParams;
	}

	public StringType getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(StringType selectedTab) {
		this.selectedTab = selectedTab;
	}

	public boolean isFiltered() {
		return filtered;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}

	public BooleanType getRefreshDraft() {
		return refreshDraft;
	}

	public void setRefreshDraft(BooleanType refreshDraft) {
		this.refreshDraft = refreshDraft;
	}

	public StringType getIdToDelete() {
		return idToDelete;
	}

	public void setIdToDelete(StringType idToDelete) {
		this.idToDelete = idToDelete;
	}
	
}
