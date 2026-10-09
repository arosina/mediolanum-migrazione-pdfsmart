package prgm.pdfwebformsutil.drivers.dao.cliente;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

// TODO: verificare se poterlo derivare
@SuppressWarnings("serial")
public class ClienteElencoAnagraficheInputModel extends CommandDataModel {
	
	private StringType  codAgente 			 = new StringType();

	private StringType  codMediolanum 	= new StringType();
	private StringType  cognome 		= new StringType();
	private StringType  nome 			= new StringType();
	private StringType  cliente 			= new StringType();
	
	private StringType  codRete 		= new StringType();
	private StringType  tipoRicerca 		 = new StringType();
	private StringType  tipoElementi 		 = new StringType();
	private IntegerType statoElementi 		 = new IntegerType();
	private StringType  tipoInclusioneAgenti = new StringType();
	private StringType  chiamante		 = new StringType();
	private StringType  codAgenteCogestore		 	= new StringType();
	private StringType  ruoloCogestione		 		= new StringType();
	private StringType  tipoInclusioneCogestiti		= new StringType();
	private StringType  tipoOrdinamentoCogestiti	= new StringType();
	
	// Dati tecnici
	private IntegerType maxRows 		= new IntegerType();

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

	public StringType getCliente() {
		return cliente;
	}

	public void setCliente(StringType cliente) {
		this.cliente = cliente;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public StringType getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public StringType getTipoElementi() {
		return tipoElementi;
	}

	public void setTipoElementi(StringType tipoElementi) {
		this.tipoElementi = tipoElementi;
	}

	public IntegerType getStatoElementi() {
		return statoElementi;
	}

	public void setStatoElementi(IntegerType statoElementi) {
		this.statoElementi = statoElementi;
	}

	public StringType getTipoInclusioneAgenti() {
		return tipoInclusioneAgenti;
	}

	public void setTipoInclusioneAgenti(StringType tipoInclusioneAgenti) {
		this.tipoInclusioneAgenti = tipoInclusioneAgenti;
	}

	public StringType getChiamante() {
		return chiamante;
	}

	public void setChiamante(StringType chiamante) {
		this.chiamante = chiamante;
	}

	public IntegerType getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(IntegerType maxRows) {
		this.maxRows = maxRows;
	}

	public StringType getCodAgenteCogestore() {
		return codAgenteCogestore;
	}

	public void setCodAgenteCogestore(StringType codAgenteCogestore) {
		this.codAgenteCogestore = codAgenteCogestore;
	}

	public StringType getRuoloCogestione() {
		return ruoloCogestione;
	}

	public void setRuoloCogestione(StringType ruoloCogestione) {
		this.ruoloCogestione = ruoloCogestione;
	}

	public StringType getTipoInclusioneCogestiti() {
		return tipoInclusioneCogestiti;
	}

	public void setTipoInclusioneCogestiti(StringType tipoInclusioneCogestiti) {
		this.tipoInclusioneCogestiti = tipoInclusioneCogestiti;
	}

	public StringType getTipoOrdinamentoCogestiti() {
		return tipoOrdinamentoCogestiti;
	}

	public void setTipoOrdinamentoCogestiti(StringType tipoOrdinamentoCogestiti) {
		this.tipoOrdinamentoCogestiti = tipoOrdinamentoCogestiti;
	}
	
	

}
