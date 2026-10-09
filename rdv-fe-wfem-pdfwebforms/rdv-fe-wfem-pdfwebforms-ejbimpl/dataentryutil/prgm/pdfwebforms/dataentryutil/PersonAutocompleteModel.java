package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.CodRuoliImpersonati;

/*******************************************************************/
/*******************************************************************/
public class PersonAutocompleteModel extends AbstractAutocompleteModel {

	private StringType  codAgente = new StringType();
	private StringType  codRuoloImpersonato = new StringType();
	private StringType  ndg = new StringType();
	private StringType  idCensimento = new StringType();
	private StringType  nome = new StringType();
	private StringType  cognome = new StringType();
	private BooleanType escludiProspect = new BooleanType();
	private BooleanType escludiVariazioni = new BooleanType();
	private IntegerType personIdx = new IntegerType();
	private BooleanType includiNdgSpecifici = new BooleanType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIncludiSoloCogestiti() {
		return new BooleanType( getCodRuoloImpersonato().equals(CodRuoliImpersonati.BC)  || 
							    getCodRuoloImpersonato().equals(CodRuoliImpersonati.OS)  || 
							    getCodRuoloImpersonato().equals(CodRuoliImpersonati.TWP) || 
							   (getCodRuoloImpersonato().equals(CodRuoliImpersonati.FPS) && getPersonIdx().intValue() == 1));
	}
	
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

	public StringType getNdg() {
		return ndg;
	}

	public void setNdg(StringType ndg) {
		this.ndg = ndg;
	}

	public StringType getIdCensimento() {
		return idCensimento;
	}

	public void setIdCensimento(StringType idCensimento) {
		this.idCensimento = idCensimento;
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

	public BooleanType getEscludiProspect() {
		return escludiProspect;
	}

	public void setEscludiProspect(BooleanType escludiProspect) {
		this.escludiProspect = escludiProspect;
	}

	public StringType getCodRuoloImpersonato() {
		return codRuoloImpersonato;
	}

	public void setCodRuoloImpersonato(StringType codRuoloImpersonato) {
		this.codRuoloImpersonato = codRuoloImpersonato;
	}

	public IntegerType getPersonIdx() {
		return personIdx;
	}

	public void setPersonIdx(IntegerType personIdx) {
		this.personIdx = personIdx;
	}

	public BooleanType getEscludiVariazioni() {
		return escludiVariazioni;
	}

	public void setEscludiVariazioni(BooleanType escludiVariazioni) {
		this.escludiVariazioni = escludiVariazioni;
	}

	public BooleanType getIncludiNdgSpecifici() {
		return includiNdgSpecifici;
	}

	public void setIncludiNdgSpecifici(BooleanType includiNdgSpecifici) {
		this.includiNdgSpecifici = includiNdgSpecifici;
	}
}
