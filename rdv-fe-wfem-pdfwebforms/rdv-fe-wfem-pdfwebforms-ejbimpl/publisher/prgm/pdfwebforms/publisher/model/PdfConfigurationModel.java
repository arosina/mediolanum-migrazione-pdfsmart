package prgm.pdfwebforms.publisher.model;

import java.util.ArrayList;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class PdfConfigurationModel extends CommandDataModel {

	private String	profiloUtente = "";
	private boolean workingAreaHidden = false;
	private String	pdfValidationWarningMessage = "";
	private String	pdfValidationErrorMessage = "";
	private String	crafterErrorMessage = "";
	
	private PdfAnagModel 		pdfAnag = new PdfAnagModel();
	private ListType 			pdfPubblicationList = new ListType(PdfAnagModel.class);
	private IntegerType			pdfNumArchiviazioni = new IntegerType();
	private ListType 			pdfArchivedList = null;
	
	private BooleanType			pubblicaComeNuovaPubblicazione = new BooleanType();
	
	private	ArrayList<String> 	areeCrafter = null;
	
	/*******************************************************************/
	/*******************************************************************/
	public PdfConfigurationModel(){
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public PdfConfigurationModel(PdfAnagKeyModel pdfAnagKey){
		getPdfAnag().setPdfId(new StringType(pdfAnagKey.getPdfId().toString()));
		getPdfAnag().setPdfCode(new StringType(pdfAnagKey.getPdfCode().toString()));
		getPdfAnag().setPdfPublicationId(new IntegerType(pdfAnagKey.getPdfPublicationId().intValue()));
	}

	/*******************************************************************/
	/*******************************************************************/
	public PdfAnagModel getCurrentPdfPublication(){
		for(int i=0;i<getPdfPubblicationList().size();i++){
			PdfAnagModel pdf = (PdfAnagModel)getPdfPubblicationList().get(i);
			if(pdf.getPdfPublicationId().intValue() == pdf.getPdfPubIdCorrente().intValue())
				return pdf;
		}
		return null;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public boolean isAreaCrafter(){
		if(getAreeCrafter() == null || getPdfAnag() == null)
			return false;
		return areeCrafter.contains(getPdfAnag().getPdfArea().toString());
	}
	
	public PdfAnagModel getPdfAnag() {
		return pdfAnag;
	}
	public void setPdfAnag(PdfAnagModel pdfAnag) {
		this.pdfAnag = pdfAnag;
	}

	public boolean isWorkingAreaHidden() {
		return workingAreaHidden;
	}

	public void setWorkingAreaHidden(boolean workingAreaHidden) {
		this.workingAreaHidden = workingAreaHidden;
	}

	public String getPdfValidationErrorMessage() {
		return pdfValidationErrorMessage;
	}

	public void setPdfValidationErrorMessage(String pdfValidationErrorMessage) {
		this.pdfValidationErrorMessage = pdfValidationErrorMessage;
	}

	public ListType getPdfPubblicationList() {
		return pdfPubblicationList;
	}

	public void setPdfPubblicationList(ListType pdfPubblicationList) {
		this.pdfPubblicationList = pdfPubblicationList;
	}

	public BooleanType getPubblicaComeNuovaPubblicazione() {
		return pubblicaComeNuovaPubblicazione;
	}

	public void setPubblicaComeNuovaPubblicazione(
			BooleanType pubblicaComeNuovaPubblicazione) {
		this.pubblicaComeNuovaPubblicazione = pubblicaComeNuovaPubblicazione;
	}

	public IntegerType getPdfNumArchiviazioni() {
		return pdfNumArchiviazioni;
	}

	public void setPdfNumArchiviazioni(IntegerType pdfNumArchiviazioni) {
		this.pdfNumArchiviazioni = pdfNumArchiviazioni;
	}

	public ListType getPdfArchivedList() {
		return pdfArchivedList;
	}

	public void setPdfArchivedList(ListType pdfArchivedList) {
		this.pdfArchivedList = pdfArchivedList;
	}

	public String getPdfValidationWarningMessage() {
		return pdfValidationWarningMessage;
	}

	public void setPdfValidationWarningMessage(String pdfValidationWarningMessage) {
		this.pdfValidationWarningMessage = pdfValidationWarningMessage;
	}

	public String getProfiloUtente() {
		return profiloUtente;
	}

	public void setProfiloUtente(String profiloUtente) {
		this.profiloUtente = profiloUtente;
	}

	public String getCrafterErrorMessage() {
		return crafterErrorMessage;
	}

	public void setCrafterErrorMessage(String crafterErrorMessage) {
		this.crafterErrorMessage = crafterErrorMessage;
	}

	public ArrayList<String> getAreeCrafter() {
		return areeCrafter;
	}

	public void setAreeCrafter(ArrayList<String> areeCrafter) {
		this.areeCrafter = areeCrafter;
	}

}
