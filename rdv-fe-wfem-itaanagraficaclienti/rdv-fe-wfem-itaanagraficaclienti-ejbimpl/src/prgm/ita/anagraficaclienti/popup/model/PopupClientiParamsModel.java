package prgm.ita.anagraficaclienti.popup.model;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.model.AgenteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupClientiParamsModel extends CommandDataModel {
	
	// Il parametro codRete serve solo perchè la stored SP_INR_ELENCO_ANAGRAFICHE lo vuole
	// ma si potrebbe farne a meno
	StringType codRete = new StringType();
	
	// Parametri di ricerca non di pagina	
	private StringType  codAgenteSpv		 = new StringType();
	private StringType  codAgenteInp 		 = new StringType();
	private StringType  codAgente 			 = new StringType();
	private StringType  tipoRicerca 		 = new StringType();
	private StringType  tipoElementi 		 = new StringType();
	private IntegerType statoElementi 		 = new IntegerType();
	private StringType  tipoInclusioneAgenti = new StringType();
	private StringType  nomeFunzione		 = new StringType();
	private StringType  codAgenteCogestore	 	= new StringType(); // In output verranno evidenziati i cogestiti con questo agente (isCogestito)  
	private StringType  ruoloCogestione		 	= new StringType(); // FB, FPS, BC
	private StringType  tipoInclusioneCogestiti	= new StringType(); // P=Solo Personali, C=Solo Cogestiti, T=Tutti
	private StringType  tipoOrdinamentoCogestiti= new StringType(); // S=Standard, C=Prima cogestiti, P=Prima personali
	
	private BooleanType	codMediolanumIsCodPotenziale = new BooleanType();
	
	// Parametri di ricerca di pagina
	private StringType  cognome 		= new StringType();
	private StringType  nome 			= new StringType();
	private StringType  codMediolanum 	= new StringType();
	private StringType  codFiscale 		= new StringType();
	private StringType	codAgenteRoot	= new StringType();
	private StringType	agenteDiretto	= new StringType();
	
	// Dati tecnici
	private IntegerType maxRows 		= new IntegerType();
	
	// Dati dell'agente 
	private AgenteModel agente = new AgenteModel();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiParamsModel() {
		CodDescDataList statoClienteList = new CodDescDataList();
		CodDescData statoCliente = null;
		statoCliente = new CodDescData(); statoCliente.setCod("1"); statoCliente.setDescr("Bozze");
		statoClienteList.addCodDescData(statoCliente);
		statoCliente = new CodDescData(); statoCliente.setCod("2"); statoCliente.setDescr("Presale mutui");
		statoClienteList.addCodDescData(statoCliente);
		statoCliente = new CodDescData(); statoCliente.setCod("3"); statoCliente.setDescr("Inviati in sede");
		statoClienteList.addCodDescData(statoCliente);
		statoCliente = new CodDescData(); statoCliente.setCod("4"); statoCliente.setDescr("Effettivi");
		statoClienteList.addCodDescData(statoCliente);
		statoCliente = new CodDescData(); statoCliente.setCod("6"); statoCliente.setDescr("Cointestatari assegnati ad altro agente");
		statoClienteList.addCodDescData(statoCliente);
		addCodDescField("statoElementi",statoClienteList);
		
		addCodDescField("agenteDiretto","DIRETTI");
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodMediolanum() {
		return codMediolanum;
	}

	public StringType getCognome() {
		return cognome;
	}

	public IntegerType getMaxRows() {
		return maxRows;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodMediolanum(StringType codMediolanum) {
		this.codMediolanum = codMediolanum;
	}

	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}

	public void setMaxRows(IntegerType maxRows) {
		this.maxRows = maxRows;
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

	public BooleanType getCodMediolanumIsCodPotenziale() {
		return codMediolanumIsCodPotenziale;
	}

	public void setCodMediolanumIsCodPotenziale(BooleanType codMediolanumIsCodPotenziale) {
		this.codMediolanumIsCodPotenziale = codMediolanumIsCodPotenziale;
	}

	public AgenteModel getAgente() {
		return agente;
	}

	public void setAgente(AgenteModel agente) {
		this.agente = agente;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public StringType getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(StringType codFiscale) {
		this.codFiscale = codFiscale;
	}

	public StringType getNome() {
		return nome;
	}

	public void setNome(StringType nome) {
		this.nome = nome;
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

	public StringType getCodAgenteSpv() {
		return codAgenteSpv;
	}

	public void setCodAgenteSpv(StringType codAgenteSpv) {
		this.codAgenteSpv = codAgenteSpv;
	}

	public StringType getCodAgenteInp() {
		return codAgenteInp;
	}

	public void setCodAgenteInp(StringType codAgenteInp) {
		this.codAgenteInp = codAgenteInp;
	}

	public void setAgenteDiretto(StringType agenteDiretto) {
		this.agenteDiretto = agenteDiretto;
	}

	public StringType getAgenteDiretto() {
		return agenteDiretto;
	}

	public void setCodAgenteRoot(StringType codAgenteRoot) {
		this.codAgenteRoot = codAgenteRoot;
	}

	public StringType getCodAgenteRoot() {
		return codAgenteRoot;
	}

	public StringType getNomeFunzione() {
		return nomeFunzione;
	}

	public void setNomeFunzione(StringType nomeFunzione) {
		this.nomeFunzione = nomeFunzione;
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
