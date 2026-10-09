package prgm.pdfwebforms.legalerappresentante;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ErroreFirmatarioModel extends CommandDataModel {

	private StringType codiceErrore = new StringType();
	private StringType descrizioneErrore = new StringType();

	public StringType getCodiceErrore() {
		return codiceErrore;
	}

	public void setCodiceErrore(StringType codiceErrore) {
		this.codiceErrore = codiceErrore;
	}

	public StringType getDescrizioneErrore() {
		return descrizioneErrore;
	}

	public void setDescrizioneErrore(StringType descrizioneErrore) {
		this.descrizioneErrore = descrizioneErrore;
	}

}
