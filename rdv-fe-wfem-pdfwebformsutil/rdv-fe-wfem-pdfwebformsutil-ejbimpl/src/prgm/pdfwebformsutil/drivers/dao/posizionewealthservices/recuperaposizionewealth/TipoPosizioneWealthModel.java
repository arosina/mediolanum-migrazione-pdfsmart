package prgm.pdfwebformsutil.drivers.dao.posizionewealthservices.recuperaposizionewealth;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

@SuppressWarnings("serial")
public class TipoPosizioneWealthModel extends CommandDataModel {
	
	private StringType codiceCliente = null;
	private StringType tipoWealth = null;
	private StringType flagAddendum = null;
	private StringType percentualeMassimaTitoli = null;
	private StringType contrNAddebito = null;
	private StringType contrNServizio = null;

	public StringType getCodiceCliente() {
		return codiceCliente;
	}

	public StringType getTipoWealth() {
		return tipoWealth;
	}

	public StringType getFlagAddendum() {
		return flagAddendum;
	}

	public StringType getPercentualeMassimaTitoli() {
		return percentualeMassimaTitoli;
	}

	public StringType getContrNAddebito() {
		return contrNAddebito;
	}

	public StringType getContrNServizio() {
		return contrNServizio;
	}

	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public void setTipoWealth(StringType tipoWealth) {
		this.tipoWealth = tipoWealth;
	}

	public void setFlagAddendum(StringType flagAddendum) {
		this.flagAddendum = flagAddendum;
	}

	public void setPercentualeMassimaTitoli(StringType percentualeMassimaTitoli) {
		this.percentualeMassimaTitoli = percentualeMassimaTitoli;
	}

	public void setContrNAddebito(StringType contrNAddebito) {
		this.contrNAddebito = contrNAddebito;
	}

	public void setContrNServizio(StringType contrNServizio) {
		this.contrNServizio = contrNServizio;
	}

}
