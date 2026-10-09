package prgm.pdfwebforms.publisher.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.publisher.common.CostantiPublisher;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/*******************************************************************/
/*******************************************************************/
public class SrvAggiornaModuloModel extends CommandDataModel {
	
	private StringType 		userId = new StringType();
	private PdfAnagModel 	pdfAnag = null;
	
	/*******************************************************************/
	private static final String TIMEZONE_SENZA_ORA = "T00:00:00+00:00";
	/*******************************************************************/
	public StringType getCodMeseMomVersion() {
		return new StringType((getPdfAnag().getPdfMomVersion().toString().substring(0,1)));
	}
	public StringType getCodAnnoMomVersion() {
		return new StringType((getPdfAnag().getPdfMomVersion().toString().substring(1)));
		
	}
	public IntegerType getNumMeseMomVersion() {
		int numMese = CostantiPublisher.CODICI_MESE.indexOf(getPdfAnag().getPdfMomVersion().toString().substring(0,1));
		return new IntegerType(numMese+1);
	}
	public StringType getNumAnnoMomVersion() {
		return new StringType("20"+(getPdfAnag().getPdfMomVersion().toString().substring(1)));
	}
	public StringType getPdfPubStartDateAsTimeString() {
		DateType d = getPdfAnag().getPdfPubStartDate();
		return new StringType(d.getAA()+"-"+d.getMM()+"-"+d.getGG()+TIMEZONE_SENZA_ORA);
	}
	public StringType getPdfDataFineAccettazionePubblPrecAsTimeString() {		
		DateType d = getPdfAnag().getPdfDataFineAccettazionePubblPrec();
		return new StringType(d.getAA()+"-"+d.getMM()+"-"+d.getGG()+TIMEZONE_SENZA_ORA);
	}
	
	public StringType getUserId() {
		return userId;
	}
	public void setUserId(StringType userId) {
		this.userId = userId;
	}
	public PdfAnagModel getPdfAnag() {
		return pdfAnag;
	}
	public void setPdfAnag(PdfAnagModel pdfAnag) {
		this.pdfAnag = pdfAnag;
	}
	
}
