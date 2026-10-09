package prgm.pdfwebforms.aml.model;

import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class CoraModel extends AbstractSezioneAmlModel{

	private StringType dispoHasCora = null; 	// Per gestire copernico: viene impstato negli xml delle dispo che hanno il co.ra. 
											// così da gestire l'accettazione copernico, processonel quale non esiste il modello co.ra. unico per tuti i pdf
	private StringType codRating = new StringType("1");

	public StringType getDispoHasCora() {
		return dispoHasCora;
	}

	public void setDispoHasCora(StringType dispoHasCora) {
		this.dispoHasCora = dispoHasCora;
	}
	
	public StringType getCodRating() {
		return codRating;
	}

	public void setCodRating(StringType codRating) {
		this.codRating = codRating;
	}

}
