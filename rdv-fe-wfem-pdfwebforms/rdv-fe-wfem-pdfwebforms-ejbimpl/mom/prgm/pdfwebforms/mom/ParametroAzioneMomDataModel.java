package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ParametroAzioneMomDataModel extends CommandDataModel {

	private StringType	nome = new StringType();
	private StringType	valore = new StringType();
	
	public StringType getNome() {
		return nome;
	}
	public void setNome(StringType nome) {
		this.nome = nome;
	}
	public StringType getValore() {
		return valore;
	}
	public void setValore(StringType valore) {
		this.valore = valore;
	}
	
}
