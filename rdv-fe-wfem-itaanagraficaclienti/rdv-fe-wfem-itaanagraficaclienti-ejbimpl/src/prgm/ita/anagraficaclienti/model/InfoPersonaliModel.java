package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class InfoPersonaliModel extends AbstractSectionModel{

	private static final long serialVersionUID = 1L;

	private StringType  	codAgente = new StringType();

	private StringType  	codStatoCivile = new StringType();
	private StringType  	codTitoloStudio = new StringType();
	private StringType  	codOrigine = new StringType();
	private StringType  	referral = new StringType();
	private IntegerType 	numeroFamiliari = new IntegerType();
	private StringType  	haFigli = new StringType();
	private StringType  	codProfessione = new StringType();
	private StringType  	codProfessionePrecedente = new StringType();
	private StringType  	codSottogruppoAttivita = new StringType();
	private StringType  	codGruppoAttivita = new StringType();
	private StringType  	codAteco = new StringType();	
	private StringType  	codSettoreEconomico = new StringType();
	private UniversitaModel universita = new UniversitaModel();
	
	private ClienteKeyModel segnalatore = new ClienteKeyModel();
		
	// Questo campo viene usato per concatenare il codice origine e il codice cliente segnalatore
	// nella gestione delle campagne (MGM, M4U...)
	private StringType  	codOrigineECodCliente = new StringType();
	
	// Il vero campo email è nella RecapitiModel
	private StringType    	email = new StringType(); // Deprecato
	
	private StringType  	fasciaReddMensile		= new StringType();	// Deprecato
	private IntegerType  	percettoriDiReddito		= new IntegerType();	
	private StringType  	attualeAbitazione		= new StringType();	
	private StringType  	flagMutuo				= new StringType();		
	
	private StringType  	provinciaSvolgimentoProfessione = new StringType();		
	private StringType  	nazioneSvolgimentoProfessione = new StringType(Costanti.COD_UIC_NAZIONE_ITALIA);		
	
	// Campo DB per contenere o la nazione o la provincia (se nazione italia) di svolgimento professione
	private StringType  	nazOProvSvolgProf = new StringType();		
	
	private StringType 		flagCodicePromo 		= new StringType(); // Deprecato
	private	StringType		codicePromo				= new StringType(); // Deprecato
	
	private StringType 	fasciaPatrimonioComplessivo = new StringType();			// Tendina: 1 = Da 0 a 15.000, 2 = Compreso tra 15.001 e 30.000, 3 = Compreso tra 30.001 e 100.000,	4 = Compreso tra 100.001 e 500.000,	5 = Compreso tra 500.001 e 2.000.000, 6 = Superiore a 2.000.000
	private StringType 	fasciaRedditoAnnuale = new StringType();				// Tendina: 1 = Da 0 a 15.000, 2 = Da 15.001 € a 28.000, 3 = Da 28.001 € a 55.000, 4 = Da 55.001 € a 75.000, 5 = Oltre 75.000 
	private StringType 	combinazioneProvenienzaPatrimonio = new StringType();	// Checkbox: per ora da 1 a 7
	private BooleanType combinazione1ProvenienzaPatrimonio = new BooleanType();	// Checkbox 1
	private BooleanType combinazione2ProvenienzaPatrimonio = new BooleanType();	// Checkbox 2
	private BooleanType combinazione3ProvenienzaPatrimonio = new BooleanType();	// Checkbox 3
	private BooleanType combinazione4ProvenienzaPatrimonio = new BooleanType();	// Checkbox 4
	private BooleanType combinazione5ProvenienzaPatrimonio = new BooleanType();	// Checkbox 5
	private BooleanType combinazione6ProvenienzaPatrimonio = new BooleanType();	// Checkbox 6
	private BooleanType combinazione7ProvenienzaPatrimonio = new BooleanType();	// Checkbox 7
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public InfoPersonaliModel(){
		
		frontendPropName.add("infoPersonali_codStatoCivile");
		frontendPropName.add("infoPersonali_codTitoloStudio");
		frontendPropName.add("infoPersonali_haFigli");
		frontendPropName.add("infoPersonali_numeroFamiliari");
		frontendPropName.add("infoPersonali_percettoriDiReddito");
		frontendPropName.add("infoPersonali_attualeAbitazione");
		frontendPropName.add("infoPersonali_flagMutuo");
		frontendPropName.add("infoPersonali_codOrigine");
		frontendPropName.add("infoPersonali_segnalatore_codMediolanum");
		frontendPropName.add("infoPersonali_referral");
		frontendPropName.add("infoPersonali_codSottogruppoAttivita");
		frontendPropName.add("infoPersonali_codAteco");

		addCodDescField("codStatoCivile","STATI_CIVILI");
		addCodDescField("codTitoloStudio","TITOLI_STUDIO");
		addCodDescField("codOrigine","ORIGINI");
		addCodDescField("codProfessione","PROFESSIONI");
		addCodDescField("codProfessionePrecedente","PROFESSIONI");
		addCodDescField("nazioneSvolgimentoProfessione","NAZIONIUIC");
		addCodDescField("provinciaSvolgimentoProfessione","PROVINCE");
		addCodDescField("fasciaPatrimonioComplessivo","FASCE_PATRIMONIO_COMPLESSIVO");
		addCodDescField("fasciaRedditoAnnuale","FASCE_REDDITO_ANNUALE");
		
		CodDescDataList sinoList = new CodDescDataList();
		CodDescData sino = null;
		sino = new CodDescData(); sino.setCod("S"); sino.setDescr("Si");
		sinoList.addCodDescData(sino);
		sino = new CodDescData(); sino.setCod("N"); sino.setDescr("No");
		sinoList.addCodDescData(sino);		
		addCodDescField("haFigli",sinoList);
		addCodDescField("flagMutuo",sinoList);		
		
		CodDescDataList fasciaRedditoList = new CodDescDataList();
		CodDescData fasciaReddito = null;
		fasciaReddito = new CodDescData(); fasciaReddito.setCod("1"); fasciaReddito.setDescr("Sino a 1500");
		fasciaRedditoList.addCodDescData(fasciaReddito);
		fasciaReddito = new CodDescData(); fasciaReddito.setCod("2"); fasciaReddito.setDescr("Da 1500 a 3500");
		fasciaRedditoList.addCodDescData(fasciaReddito);
		fasciaReddito = new CodDescData(); fasciaReddito.setCod("3"); fasciaReddito.setDescr("Oltre 3500");
		fasciaRedditoList.addCodDescData(fasciaReddito);
		addCodDescField("fasciaReddMensile",fasciaRedditoList);		
		
		CodDescDataList attualeAbitazioneList = new CodDescDataList();
		CodDescData attualeAbit = null;
		attualeAbit = new CodDescData(); attualeAbit.setCod("A"); attualeAbit.setDescr("Affitto");
		attualeAbitazioneList.addCodDescData(attualeAbit);
		attualeAbit = new CodDescData(); attualeAbit.setCod("P"); attualeAbit.setDescr("Proprietà");
		attualeAbitazioneList.addCodDescData(attualeAbit);
		attualeAbit = new CodDescData(); attualeAbit.setCod("V"); attualeAbit.setDescr("Altro");
		attualeAbitazioneList.addCodDescData(attualeAbit);
		addCodDescField("attualeAbitazione",attualeAbitazioneList);		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initCombinazioneProvenienzaPatrimonioChecks(){
		
		setCombinazione1ProvenienzaPatrimonio(new BooleanType());
		if(getCombinazioneProvenienzaPatrimonio().toString().indexOf('1') >= 0)
			setCombinazione1ProvenienzaPatrimonio(new BooleanType(true));
		
		setCombinazione2ProvenienzaPatrimonio(new BooleanType());
		if(getCombinazioneProvenienzaPatrimonio().toString().indexOf('2') >= 0)
			setCombinazione2ProvenienzaPatrimonio(new BooleanType(true));
		
		setCombinazione3ProvenienzaPatrimonio(new BooleanType());
		if(getCombinazioneProvenienzaPatrimonio().toString().indexOf('3') >= 0)
			setCombinazione3ProvenienzaPatrimonio(new BooleanType(true));
		
		setCombinazione4ProvenienzaPatrimonio(new BooleanType());
		if(getCombinazioneProvenienzaPatrimonio().toString().indexOf('4') >= 0)
			setCombinazione4ProvenienzaPatrimonio(new BooleanType(true));
		
		setCombinazione5ProvenienzaPatrimonio(new BooleanType());
		if(getCombinazioneProvenienzaPatrimonio().toString().indexOf('5') >= 0)
			setCombinazione5ProvenienzaPatrimonio(new BooleanType(true));
		
		setCombinazione6ProvenienzaPatrimonio(new BooleanType());
		if(getCombinazioneProvenienzaPatrimonio().toString().indexOf('6') >= 0)
			setCombinazione6ProvenienzaPatrimonio(new BooleanType(true));
		
		setCombinazione7ProvenienzaPatrimonio(new BooleanType());
		if(getCombinazioneProvenienzaPatrimonio().toString().indexOf('7') >= 0)
			setCombinazione7ProvenienzaPatrimonio(new BooleanType(true));
	}
	
	/** 
	 * La mail inizialmente era nella sezione InfoPersonali. Poi
	 * è stata spostata nella sezione Recapiti.
	 * Per compatibilità con altre applicazioni rimane anche qui.
	 * Questo campo viene impostato alla lettura del cliente
	 * 
	 */
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getEmail() {
		return email;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setEmail(StringType email) {
		this.email = email;
	}
	/****************************************************************** 
	 * Campi deprecati e non più utilizzati
	 ******************************************************************/	
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getFasciaReddMensile() {
		return fasciaReddMensile;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setFasciaReddMensile(StringType fasciaReddMensile) {
		this.fasciaReddMensile = fasciaReddMensile;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getFlagCodicePromo() {
		return flagCodicePromo;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setFlagCodicePromo(StringType flagCodicePromo) {
		this.flagCodicePromo = flagCodicePromo;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getCodicePromo() {
		return codicePromo;
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setCodicePromo(StringType codicePromo) {
		this.codicePromo = codicePromo;
	}
	/********************************************************************/
	
	
	public StringType getCodGruppoAttivita() {
		return codGruppoAttivita;
	}

	public StringType getCodOrigine() {
		return codOrigine;
	}

	public StringType getCodProfessione() {
		return codProfessione;
	}

	public StringType getCodSottogruppoAttivita() {
		return codSottogruppoAttivita;
	}

	public StringType getCodStatoCivile() {
		return codStatoCivile;
	}

	public StringType getCodTitoloStudio() {
		return codTitoloStudio;
	}

	public StringType getHaFigli() {
		return haFigli;
	}

	public IntegerType getNumeroFamiliari() {
		return numeroFamiliari;
	}

	public StringType getReferral() {
		return referral;
	}

	public void setCodGruppoAttivita(StringType codGruppoAttivita) {
		this.codGruppoAttivita = codGruppoAttivita;
	}

	public void setCodOrigine(StringType codOrigine) {
		this.codOrigine = codOrigine;
	}

	public void setCodProfessione(StringType codProfessione) {
		this.codProfessione = codProfessione;
	}

	public void setCodSottogruppoAttivita(StringType codSottogruppoAttivita) {
		this.codSottogruppoAttivita = codSottogruppoAttivita;
	}

	public void setCodStatoCivile(StringType codStatoCivile) {
		this.codStatoCivile = codStatoCivile;
	}

	public void setCodTitoloStudio(StringType codTitoloStudio) {
		this.codTitoloStudio = codTitoloStudio;
	}

	public void setHaFigli(StringType haFigli) {
		this.haFigli = haFigli;
	}

	public void setNumeroFamiliari(IntegerType numeroFamiliari) {
		this.numeroFamiliari = numeroFamiliari;
	}

	public void setReferral(StringType referral) {
		this.referral = referral;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public ClienteKeyModel getSegnalatore() {
		return segnalatore;
	}

	public void setSegnalatore(ClienteKeyModel segnalatore) {
		this.segnalatore = segnalatore;
	}

	public StringType getCodOrigineECodCliente() {
		return codOrigineECodCliente;
	}

	public void setCodOrigineECodCliente(StringType codOrigineECodCliente) {
		this.codOrigineECodCliente = codOrigineECodCliente;
	}

	public UniversitaModel getUniversita() {
		return universita;
	}

	public void setUniversita(UniversitaModel universita) {
		this.universita = universita;
	}

	public StringType getCodProfessionePrecedente() {
		return codProfessionePrecedente;
	}

	public void setCodProfessionePrecedente(StringType codProfessionePrecedente) {
		this.codProfessionePrecedente = codProfessionePrecedente;
	}

	public StringType getCodSettoreEconomico() {
		return codSettoreEconomico;
	}

	public void setCodSettoreEconomico(StringType codSettoreEconomico) {
		this.codSettoreEconomico = codSettoreEconomico;
	}

	public StringType getFlagMutuo() {
		return flagMutuo;
	}

	public void setFlagMutuo(StringType flagMutuo) {
		this.flagMutuo = flagMutuo;
	}

	public StringType getProvinciaSvolgimentoProfessione() {
		return provinciaSvolgimentoProfessione;
	}

	public void setProvinciaSvolgimentoProfessione(
			StringType provinciaSvolgimentoProfessione) {
		this.provinciaSvolgimentoProfessione = provinciaSvolgimentoProfessione;
	}

	public StringType getNazioneSvolgimentoProfessione() {
		return nazioneSvolgimentoProfessione;
	}

	public void setNazioneSvolgimentoProfessione(
			StringType nazioneSvolgimentoProfessione) {
		this.nazioneSvolgimentoProfessione = nazioneSvolgimentoProfessione;
	}

	public StringType getAttualeAbitazione() {
		return attualeAbitazione;
	}

	public void setAttualeAbitazione(StringType attualeAbitazione) {
		this.attualeAbitazione = attualeAbitazione;
	}

	public IntegerType getPercettoriDiReddito() {
		return percettoriDiReddito;
	}

	public void setPercettoriDiReddito(IntegerType percettoriDiReddito) {
		this.percettoriDiReddito = percettoriDiReddito;
	}

	public StringType getNazOProvSvolgProf() {
		return nazOProvSvolgProf;
	}

	public void setNazOProvSvolgProf(StringType nazOProvSvolgProf) {
		this.nazOProvSvolgProf = nazOProvSvolgProf;
	}

	public StringType getCodAteco() {
		return codAteco;
	}

	public void setCodAteco(StringType codAteco) {
		this.codAteco = codAteco;
	}

	public StringType getFasciaPatrimonioComplessivo() {
		return fasciaPatrimonioComplessivo;
	}

	public void setFasciaPatrimonioComplessivo(
			StringType fasciaPatrimonioComplessivo) {
		this.fasciaPatrimonioComplessivo = fasciaPatrimonioComplessivo;
	}

	public StringType getFasciaRedditoAnnuale() {
		return fasciaRedditoAnnuale;
	}

	public void setFasciaRedditoAnnuale(StringType fasciaRedditoAnnuale) {
		this.fasciaRedditoAnnuale = fasciaRedditoAnnuale;
	}

	public StringType getCombinazioneProvenienzaPatrimonio() {
		return combinazioneProvenienzaPatrimonio;
	}

	public void setCombinazioneProvenienzaPatrimonio(
			StringType combinazioneProvenienzaPatrimonio) {
		this.combinazioneProvenienzaPatrimonio = combinazioneProvenienzaPatrimonio;
	}

	public BooleanType getCombinazione1ProvenienzaPatrimonio() {
		return combinazione1ProvenienzaPatrimonio;
	}

	public void setCombinazione1ProvenienzaPatrimonio(
			BooleanType combinazione1ProvenienzaPatrimonio) {
		this.combinazione1ProvenienzaPatrimonio = combinazione1ProvenienzaPatrimonio;
	}

	public BooleanType getCombinazione2ProvenienzaPatrimonio() {
		return combinazione2ProvenienzaPatrimonio;
	}

	public void setCombinazione2ProvenienzaPatrimonio(
			BooleanType combinazione2ProvenienzaPatrimonio) {
		this.combinazione2ProvenienzaPatrimonio = combinazione2ProvenienzaPatrimonio;
	}

	public BooleanType getCombinazione3ProvenienzaPatrimonio() {
		return combinazione3ProvenienzaPatrimonio;
	}

	public void setCombinazione3ProvenienzaPatrimonio(
			BooleanType combinazione3ProvenienzaPatrimonio) {
		this.combinazione3ProvenienzaPatrimonio = combinazione3ProvenienzaPatrimonio;
	}

	public BooleanType getCombinazione4ProvenienzaPatrimonio() {
		return combinazione4ProvenienzaPatrimonio;
	}

	public void setCombinazione4ProvenienzaPatrimonio(
			BooleanType combinazione4ProvenienzaPatrimonio) {
		this.combinazione4ProvenienzaPatrimonio = combinazione4ProvenienzaPatrimonio;
	}

	public BooleanType getCombinazione5ProvenienzaPatrimonio() {
		return combinazione5ProvenienzaPatrimonio;
	}

	public void setCombinazione5ProvenienzaPatrimonio(
			BooleanType combinazione5ProvenienzaPatrimonio) {
		this.combinazione5ProvenienzaPatrimonio = combinazione5ProvenienzaPatrimonio;
	}

	public BooleanType getCombinazione6ProvenienzaPatrimonio() {
		return combinazione6ProvenienzaPatrimonio;
	}

	public void setCombinazione6ProvenienzaPatrimonio(
			BooleanType combinazione6ProvenienzaPatrimonio) {
		this.combinazione6ProvenienzaPatrimonio = combinazione6ProvenienzaPatrimonio;
	}

	public BooleanType getCombinazione7ProvenienzaPatrimonio() {
		return combinazione7ProvenienzaPatrimonio;
	}

	public void setCombinazione7ProvenienzaPatrimonio(
			BooleanType combinazione7ProvenienzaPatrimonio) {
		this.combinazione7ProvenienzaPatrimonio = combinazione7ProvenienzaPatrimonio;
	}
}
