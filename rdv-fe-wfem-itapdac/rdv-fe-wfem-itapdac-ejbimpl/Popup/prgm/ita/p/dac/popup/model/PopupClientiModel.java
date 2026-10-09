package prgm.ita.p.dac.popup.model;

import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.ClienteModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class PopupClientiModel extends ParamsModel {
	
	public static final int RICERCA_MIEI_CLIENTI = 1;
	public static final int RICERCA_NON_MIEI_CLIENTI = 2;
	public static final int RICERCA_TUTTI_CLIENTI = 3;
	
	private boolean primaVolta = true;
	
	private AgenteModel agenteCollegato = new AgenteModel();
	
	private IntegerType tipoRicerca = new IntegerType(RICERCA_MIEI_CLIENTI);
	private StringType 	codAgente = new StringType();
	
	private StringType 	codMediolanum = new StringType();
	private StringType 	cognome = new StringType();
	private StringType 	nome = new StringType();
	private ListType	elencoClienti = new ListType(PopupClienteModel.class);
	
	private ClienteModel clienteSelezionato = new ClienteModel();
	private ListType	 elencoContrattiCliente = new ListType(PopupContrattoModel.class);
	
	/********************************************************************************/
	/********************************************************************************/
	public boolean esisteContrattoChiuso(){
		for(int i=0;i<getElencoContrattiCliente().size();i++){
			PopupContrattoModel c = (PopupContrattoModel)getElencoContrattiCliente().get(i);
			if(c.getIsChiuso().intValue() > 0)
				return true;
		}
		return false;
	}
	
	public boolean isPrimaVolta() {
		return primaVolta;
	}
	public void setPrimaVolta(boolean primaVolta) {
		this.primaVolta = primaVolta;
	}
	public IntegerType getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(IntegerType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCodMediolanum() {
		return codMediolanum;
	}
	public void setCodMediolanum(StringType codMediolanum) {
		this.codMediolanum = codMediolanum;
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
	public ListType getElencoClienti() {
		return elencoClienti;
	}
	public void setElencoClienti(ListType elencoClienti) {
		this.elencoClienti = elencoClienti;
	}
	public ListType getElencoContrattiCliente() {
		return elencoContrattiCliente;
	}
	public void setElencoContrattiCliente(ListType elencoContrattiCliente) {
		this.elencoContrattiCliente = elencoContrattiCliente;
	}
	public ClienteModel getClienteSelezionato() {
		return clienteSelezionato;
	}
	public void setClienteSelezionato(ClienteModel clienteSelezionato) {
		this.clienteSelezionato = clienteSelezionato;
	}
	public AgenteModel getAgenteCollegato() {
		return agenteCollegato;
	}
	public void setAgenteCollegato(AgenteModel agenteCollegato) {
		this.agenteCollegato = agenteCollegato;
	}
	
}
