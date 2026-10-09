package prgm.ita.anagraficaclienti.popup.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupAgentiParamsModel extends CommandDataModel {

	// Parametri di ricerca di pagina
	private StringType  codAgente 		= new StringType();
	private StringType  cognomeAgente 	= new StringType();
	private StringType  tipoRicerca 	= new StringType();

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getCognomeAgente() {
		return cognomeAgente;
	}

	public void setCognomeAgente(StringType cognomeAgente) {
		this.cognomeAgente = cognomeAgente;
	}

	public StringType getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

}
