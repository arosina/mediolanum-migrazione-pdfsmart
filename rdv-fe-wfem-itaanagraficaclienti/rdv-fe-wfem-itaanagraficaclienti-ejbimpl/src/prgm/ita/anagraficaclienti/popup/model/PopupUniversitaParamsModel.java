package prgm.ita.anagraficaclienti.popup.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupUniversitaParamsModel extends CommandDataModel {

	// Parametri di ricerca di pagina
	private StringType  ateneo = new StringType();
	private StringType  facolta = new StringType();
	private StringType  provincia = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupUniversitaParamsModel(){
		addCodDescField("facolta","FACOLTA");
		addCodDescField("provincia","PROVINCE");
	}
	
	public StringType getFacolta() {
		return facolta;
	}
	public void setFacolta(StringType facolta) {
		this.facolta = facolta;
	}
	public StringType getProvincia() {
		return provincia;
	}
	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}

	public StringType getAteneo() {
		return ateneo;
	}

	public void setAteneo(StringType ateneo) {
		this.ateneo = ateneo;
	}

	
}
