package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfVariazioneModel extends CommandDataModel {

	private StringType codiceVariazione = new StringType();
	private FileType   pdfFile = new FileType();
	private DatiStampa datiStampa = new DatiStampa();

	public StringType getCodiceVariazione() {
		return codiceVariazione;
	}
	public void setCodiceVariazione(StringType codiceVariazione) {
		this.codiceVariazione = codiceVariazione;
	}
	public FileType getPdfFile() {
		return pdfFile;
	}
	public void setPdfFile(FileType pdfFile) {
		this.pdfFile = pdfFile;
	}
	public DatiStampa getDatiStampa() {
		return datiStampa;
	}
	public void setDatiStampa(DatiStampa datiStampa) {
		this.datiStampa = datiStampa;
	}
}
