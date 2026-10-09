package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class AgePersonAutocompleteModel extends AbstractAutocompleteModel {

	private StringType  codAgente = new StringType();
	private StringType  nome = new StringType();
	private StringType  cognome = new StringType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName){
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getNome() {
		return nome;
	}

	public void setNome(StringType nome) {
		this.nome = nome;
	}

	public StringType getCognome() {
		return cognome;
	}

	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}

}
