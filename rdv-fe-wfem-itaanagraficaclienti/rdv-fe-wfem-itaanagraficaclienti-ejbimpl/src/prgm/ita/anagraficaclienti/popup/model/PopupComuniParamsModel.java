package prgm.ita.anagraficaclienti.popup.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupComuniParamsModel extends CommandDataModel {

	private BooleanType isRicercaIscrittiAlCatasto = new BooleanType();
	
	// Parametri di ricerca di pagina
	private StringType  cap 		= new StringType();
	private StringType  comune 	= new StringType();
	private StringType  provincia 	= new StringType();

	public StringType getCap() {
		return cap;
	}

	public StringType getComune() {
		return comune;
	}

	public StringType getProvincia() {
		return provincia;
	}

	public void setCap(StringType cap) {
		this.cap = cap;
	}

	public void setComune(StringType comune) {
		this.comune = comune;
	}

	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}

	public BooleanType getIsRicercaIscrittiAlCatasto() {
		return isRicercaIscrittiAlCatasto;
	}

	public void setIsRicercaIscrittiAlCatasto(BooleanType isRicercaIscrittiAlCatasto) {
		this.isRicercaIscrittiAlCatasto = isRicercaIscrittiAlCatasto;
	}

}
