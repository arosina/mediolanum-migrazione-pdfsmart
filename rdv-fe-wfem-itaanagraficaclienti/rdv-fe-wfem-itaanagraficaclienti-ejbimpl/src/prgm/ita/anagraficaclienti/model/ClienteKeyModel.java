package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ClienteKeyModel extends ParametriApplicazioneAnagrafica {

	private static final long serialVersionUID = 1L;

	private StringType  codAgenteSpv = new StringType();
	
	// Campi chiave effettivi ***************************************
	private StringType  codAgente = new StringType();
	private StringType  codPotenziale = new StringType();
	private StringType  codMediolanum = new StringType();
	private StringType  codFiscale = new StringType();
	private StringType  partitaIva = new StringType();	
	// **************************************************************
	
	private StringType  codDisposizione = new StringType();
	private StringType  codInforete	= new StringType();	
	private StringType  cognome = new StringType();
	private StringType  nome = new StringType();
	
	private MapCommandDataModel dynamicData = new MapCommandDataModel();
	
	// SCRITTURA SU ORACLE **********************************************
	private String  esitoScritturaSuOracle = "";
	private String  msgErrScritturaSuOracle = "";
	// ******************************************************************
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getCodiceUnivoco(){
		if(!getCodMediolanum().isNull())
			return getCodMediolanum();
		
		if(!getCodInforete().isNull())
			return getCodInforete();
			
		return getCodPotenziale();
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getNominativo(){
		if(getCognome().isNull() && getNome().isNull())
			return new StringType();
			
		return new StringType(getCognome()+" "+getNome());
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getNominativoConCodice(){
		if(getCodMediolanum().isNull() && getCodInforete().isNull())
			return new StringType();
		
		if(!getCodMediolanum().isNull())
			return new StringType(getCodMediolanum()+" - "+getCognome()+" "+getNome());
		else
			return new StringType(getCodInforete()+" - "+getCognome()+" "+getNome());
	}
	
	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodFiscale() {
		return codFiscale;
	}

	public StringType getPartitaIva() {
		return partitaIva;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodFiscale(StringType codFiscale) {
		this.codFiscale = codFiscale;
	}

	public void setPartitaIva(StringType partitaIva) {
		this.partitaIva = partitaIva;
	}

	public StringType getCodPotenziale() {
		return codPotenziale;
	}

	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
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

	public StringType getCodInforete() {
		return codInforete;
	}

	public void setCodInforete(StringType codInforete) {
		this.codInforete = codInforete;
	}
	public StringType getCodAgenteSpv() {
		return codAgenteSpv;
	}
	public void setCodAgenteSpv(StringType codAgenteSpv) {
		this.codAgenteSpv = codAgenteSpv;
	}
	public MapCommandDataModel getDynamicData() {
		return dynamicData;
	}
	public void setDynamicData(MapCommandDataModel dynamicData) {
		this.dynamicData = dynamicData;
	}
	public StringType getCodDisposizione() {
		return codDisposizione;
	}
	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}
	public String getEsitoScritturaSuOracle() {
		return esitoScritturaSuOracle;
	}
	public void setEsitoScritturaSuOracle(String esitoScritturaSuOracle) {
		this.esitoScritturaSuOracle = esitoScritturaSuOracle;
	}
	public String getMsgErrScritturaSuOracle() {
		return msgErrScritturaSuOracle;
	}
	public void setMsgErrScritturaSuOracle(String msgErrScritturaSuOracle) {
		this.msgErrScritturaSuOracle = msgErrScritturaSuOracle;
	}
	
}
