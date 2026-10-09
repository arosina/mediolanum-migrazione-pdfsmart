package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class PdfInstanceAttachModel extends CommandDataModel {

	private StringType	idFileNasAllegato = new StringType();
	private StringType	descrizioneAllegato = new StringType();
	private StringType	denominazioneFileAllegato = new StringType();
	private StringType 	codTipoDocum = new StringType(); // Per MOM

	public StringType getIdFileNasAllegato() {
		return idFileNasAllegato;
	}

	public void setIdFileNasAllegato(StringType idFileNasAllegato) {
		this.idFileNasAllegato = idFileNasAllegato;
	}

	public StringType getDescrizioneAllegato() {
		return descrizioneAllegato;
	}

	public void setDescrizioneAllegato(StringType descrizioneAllegato) {
		this.descrizioneAllegato = descrizioneAllegato;
	}

	public StringType getDenominazioneFileAllegato() {
		return denominazioneFileAllegato;
	}

	public void setDenominazioneFileAllegato(StringType denominazioneFileAllegato) {
		this.denominazioneFileAllegato = denominazioneFileAllegato;
	}

	public StringType getCodTipoDocum() {
		return codTipoDocum;
	}

	public void setCodTipoDocum(StringType codTipoDocum) {
		this.codTipoDocum = codTipoDocum;
	}

}
