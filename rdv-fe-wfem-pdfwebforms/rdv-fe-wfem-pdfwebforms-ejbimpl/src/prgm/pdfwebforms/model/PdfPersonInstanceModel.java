package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPersonInstanceModel extends CommandDataModel {

	private StringType  idCensimento = new StringType();
	private StringType  ndg = new StringType();
	private StringType  codPotenziale = new StringType();
	private StringType  cognome = new StringType();
	private StringType  nome = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void instanceFromData(PdfPersonModel person) throws Exception{
		setIdCensimento(person.getIdCensimento());
		setNdg(person.getNdg());
		setCodPotenziale(person.getCodPotenziale());
		
		String val = person.readProperty("cognome") == null ? "" : person.readProperty("cognome").toString();
		setCognome(new StringType(val));
		val = person.readProperty("nome") == null ? "" : person.readProperty("nome").toString();
		setNome(new StringType(val));
	}
	
	public StringType getIdCensimento() {
		return idCensimento;
	}
	public void setIdCensimento(StringType idCensimento) {
		this.idCensimento = idCensimento;
	}
	public StringType getNdg() {
		return ndg;
	}
	public void setNdg(StringType ndg) {
		this.ndg = ndg;
	}

	public StringType getCodPotenziale() {
		return codPotenziale;
	}

	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}

	public StringType getCognome() {
		return cognome;
	}

	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}

	public StringType getNome() {
		return nome;
	}

	public void setNome(StringType nome) {
		this.nome = nome;
	}
}
