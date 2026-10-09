package prgm.pdfwebformsutil.drivers.dao.cliente;

import com.atosorigin.wfem.command.CommandDataModel;

@SuppressWarnings("serial")
public class ClienteInAnagraficaInputModel extends CommandDataModel {

	// params comuni
	private String codiceCliente;
	private String codiceFiscale;

	// params ricerca censito
	private String codiceAgente;

	// params ricerca bozza
	private ClienteElencoAnagraficheInputModel paramsBozza = new ClienteElencoAnagraficheInputModel();

	// params ricerca riconciliato
	private boolean doRiconciliazione = true;
	
	public String getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCodiceAgente() {
		return codiceAgente;
	}

	public void setCodiceAgente(String codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	public boolean isDoRiconciliazione() {
		return doRiconciliazione;
	}

	public void setDoRiconciliazione(boolean searchRiconciliato) {
		this.doRiconciliazione = searchRiconciliato;
	}

	public ClienteElencoAnagraficheInputModel getParamsBozza() {
		return paramsBozza;
	}

	public void setParamsBozza(ClienteElencoAnagraficheInputModel paramsBozza) {
		this.paramsBozza = paramsBozza;
	}

}
