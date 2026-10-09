package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class PdfRuoloUtilizzatoreModel extends CommandDataModel {
	
	private StringType  pdfId 						 = new StringType();
	private BooleanType	isSelezionato 				 = new BooleanType();
	private StringType 	codiceRuoloUtilizzatore 	 = new StringType();
	private StringType 	descrizioneRuoloUtilizzatore = new StringType();
	
	public StringType getPdfId() {
		return pdfId;
	}
	public void setPdfId(StringType pdfId) {
		this.pdfId = pdfId;
	}
	public BooleanType getIsSelezionato() {
		return isSelezionato;
	}
	public void setIsSelezionato(BooleanType isSelezionato) {
		this.isSelezionato = isSelezionato;
	}
	public StringType getCodiceRuoloUtilizzatore() {
		return codiceRuoloUtilizzatore;
	}
	public void setCodiceRuoloUtilizzatore(StringType codiceRuoloUtilizzatore) {
		this.codiceRuoloUtilizzatore = codiceRuoloUtilizzatore;
	}
	public StringType getDescrizioneRuoloUtilizzatore() {
		return descrizioneRuoloUtilizzatore;
	}
	public void setDescrizioneRuoloUtilizzatore(StringType descrizioneRuoloUtilizzatore) {
		this.descrizioneRuoloUtilizzatore = descrizioneRuoloUtilizzatore;
	}
	
	
}
