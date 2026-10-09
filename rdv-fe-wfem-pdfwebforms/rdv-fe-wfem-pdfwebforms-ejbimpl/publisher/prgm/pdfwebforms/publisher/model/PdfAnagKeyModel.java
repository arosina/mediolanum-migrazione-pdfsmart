package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class PdfAnagKeyModel extends CommandDataModel {
	
	public static int NO_PUBLICATION = -1;
	
	private StringType  pdfId = new StringType();
	private StringType  pdfCode = new StringType();	// optional id "id" is setted
	private IntegerType pdfPublicationId = new IntegerType(NO_PUBLICATION);
	private StringType 	pdfMomCode = new StringType();
	private StringType	generaBarcode = new StringType();

	// Dati di comodo
	private IntegerType pdfPublicationIdForArch = new IntegerType();
	private String		crafterErrorMessage = "";
	private StringType 	pdfMomVersion = new StringType();
	
	// Per il download
	private BooleanType asFacsimile = null;
	
	/*******************************************************************/
	/*******************************************************************/
	public PdfAnagKeyModel(){
		super();
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public PdfAnagKeyModel(PdfAnagKeyModel anag){
		setPdfId(new StringType(anag.getPdfId().toString()));
		setPdfCode(new StringType(anag.getPdfCode().toString()));
		setPdfPublicationId(new IntegerType(anag.getPdfPublicationId().intValue()));
	}
	
	public StringType getPdfCode() {
		return pdfCode;
	}
	public void setPdfCode(StringType pdfCode) {
		this.pdfCode = pdfCode;
	}

	public IntegerType getPdfPublicationId() {
		return pdfPublicationId;
	}

	public void setPdfPublicationId(IntegerType pdfPublicationId) {
		this.pdfPublicationId = pdfPublicationId;
	}

	public StringType getPdfId() {
		return pdfId;
	}

	public void setPdfId(StringType pdfId) {
		this.pdfId = pdfId;
	}

	public IntegerType getPdfPublicationIdForArch() {
		return pdfPublicationIdForArch;
	}

	public void setPdfPublicationIdForArch(IntegerType pdfPublicationIdForArch) {
		this.pdfPublicationIdForArch = pdfPublicationIdForArch;
	}

	public BooleanType getAsFacsimile() {
		return asFacsimile;
	}

	public void setAsFacsimile(BooleanType asFacsimile) {
		this.asFacsimile = asFacsimile;
	}

	public StringType getPdfMomCode() {
		return pdfMomCode;
	}

	public void setPdfMomCode(StringType pdfMomCode) {
		this.pdfMomCode = pdfMomCode;
	}

	public String getCrafterErrorMessage() {
		return crafterErrorMessage;
	}

	public void setCrafterErrorMessage(String crafterErrorMessage) {
		this.crafterErrorMessage = crafterErrorMessage;
	}

	public StringType getGeneraBarcode() {
		return generaBarcode;
	}

	public void setGeneraBarcode(StringType generaBarcode) {
		this.generaBarcode = generaBarcode;
	}

	public StringType getPdfMomVersion() {
		return pdfMomVersion;
	}

	public void setPdfMomVersion(StringType pdfMomVersion) {
		this.pdfMomVersion = pdfMomVersion;
	}


}
