package prgm.ita.anagraficaclienti.stampamoduli;

import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class StampaModuliModel extends CommandDataModel {
	
	private StringType nomeModulo = new StringType();
	private StringType formatoPagina = new StringType("A4");
	private StringType   orientamentoPagina = new StringType("horizontal"); 
	private PopupClientiModel popupClientiModel = new PopupClientiModel();
	private StringType  tipoRicerca 		 = new StringType();
	private AgenteModel agenteCollegato = new AgenteModel();

	public StringType getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(StringType nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	public PopupClientiModel getPopupClientiModel() {
		return popupClientiModel;
	}

	public void setPopupClientiModel(PopupClientiModel popupClientiModel) {
		this.popupClientiModel = popupClientiModel;
	}

	public StringType getFormatoPagina() {
		return formatoPagina;
	}

	public void setFormatoPagina(StringType formatoPagina) {
		this.formatoPagina = formatoPagina;
	}

	public StringType getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public AgenteModel getAgenteCollegato() {
		return agenteCollegato;
	}

	public void setAgenteCollegato(AgenteModel agenteCollegato) {
		this.agenteCollegato = agenteCollegato;
	}
	public StringType getOrientamentoPagina() {
		return orientamentoPagina;
	}

	public void setOrientamentoPagina(StringType orientamentoPagina) {
		this.orientamentoPagina = orientamentoPagina;
	}
}
