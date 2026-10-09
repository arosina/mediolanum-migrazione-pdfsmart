package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class IndirizzoModel extends ComuneModel{

	private static final long serialVersionUID = 1L;

	private boolean existOnDB = false;

	private StringType  	codAgente   	= new StringType();
	private StringType  	codPotenziale 	= new StringType();
	private StringType 		codRete			= new StringType();
	private StringType  	stato 			= new StringType();
	private StringType  	statoProposta	= new StringType();
	private IntegerType 	progressivo 	= new IntegerType();
	private StringType  	serverReplica	= new StringType();    
	private TimestampType 	dataVariazione  = new TimestampType();

	private IntegerType	progressivoProposta = new IntegerType();
			
	private StringType 	tipoIndirizzo = new StringType();
	private StringType 	toponimoIndirizzo = new StringType();
	private StringType 	descrizioneIndirizzo = new StringType();
	private StringType 	numeroCivico = new StringType();
	private StringType 	presso = new StringType();
	private StringType 	descrizioneComune = new StringType(); // Rappresenta il comune "accatastato" (il campo comune è in realtà la località) e con la rfc #119648 non viene più richiesto a front-end
	
	private StringType  statoConfermato = new StringType();

	private DatiApplicativiModel datiApplicativi = new DatiApplicativiModel();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public IndirizzoModel(){
		
		addCodDescField("tipoIndirizzo","TIPI_INDIRIZZO");
		addCodDescField("toponimoIndirizzo","TOPONIMI");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isIndirizzoSpecifico(StringType tipoIndirizzo){
		return  tipoIndirizzo.equals(Costanti.COD_INDIRIZZO_RESIDENZA) ||
				tipoIndirizzo.equals(Costanti.COD_INDIRIZZO_DOMICILIO);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getIndirizzoCompleto() {
		String result="";
		if(!getToponimoIndirizzo().isNull()) {
			String dToponimo = getDescValue("toponimoIndirizzo");
			if(dToponimo.equals(""))
				dToponimo = getToponimoIndirizzo().toString();
			result += dToponimo+" ";
		}
		result += getDescrizioneIndirizzo().toString();
		if(!getNumeroCivico().isNull())
			result += ", "+getNumeroCivico().toString();
		return new StringType(result);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getIndirizzoCompletoCsc() {
		String result="";
		if(!getToponimoIndirizzo().isNull())
			result += getToponimoIndirizzo()+" ";
		result += getDescrizioneIndirizzo().toString();
		if(!getNumeroCivico().isNull())
			result += ", "+getNumeroCivico().toString();
		return new StringType(result);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEmpty(){
		return  (getCodNazione().isNull() || getCodNazione().equals(Costanti.COD_NAZIONE_ITALIA)) &&
				getToponimoIndirizzo().isNull() &&
				getDescrizioneIndirizzo().isNull() &&
				getNumeroCivico().isNull() &&
				getComune().isNull() &&
				getComuneEstero().isNull();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEqual(IndirizzoModel other){
		return  getCodNazione().equals(other.getCodNazione()) &&
				getToponimoIndirizzo().equals(other.getToponimoIndirizzo()) &&
				getDescrizioneIndirizzo().equals(other.getDescrizioneIndirizzo()) &&
				getNumeroCivico().equals(other.getNumeroCivico()) &&
				getProvincia().equals(other.getProvincia()) &&
				getComune().equals(other.getComune()) &&
				getComuneEstero().equals(other.getComuneEstero()) &&
				getCap().equals(other.getCap());
	}
	
	/********************************************************************
	 * Il campo "descrizioneComune" rappresenta il comune "accatastato" e viene inserito da csc a valle delle proposte di censimento
	 * in quanto con la rfc #119648 non viene più richiesto a front-end
	 */
	public StringType getDescrizioneComune() {
		return descrizioneComune;
	}
	public void setDescrizioneComune(StringType descrizioneComune) {
		this.descrizioneComune = descrizioneComune;
	}
	/********************************************************************/

	public StringType getTipoIndirizzo() {
		return tipoIndirizzo;
	}

	public void setTipoIndirizzo(StringType tipoIndirizzo) {
		this.tipoIndirizzo = tipoIndirizzo;
	}

	public IntegerType getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}

	public IntegerType getProgressivoProposta() {
		return progressivoProposta;
	}

	public void setProgressivoProposta(IntegerType progressivoProposta) {
		this.progressivoProposta = progressivoProposta;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getStato() {
		return stato;
	}

	public StringType getStatoProposta() {
		return statoProposta;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setStato(StringType stato) {
		this.stato = stato;
	}

	public void setStatoProposta(StringType statoProposta) {
		this.statoProposta = statoProposta;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodPotenziale() {
		return codPotenziale;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}

	public TimestampType getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(TimestampType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public StringType getPresso() {
		return presso;
	}

	public void setPresso(StringType presso) {
		this.presso = presso;
	}

	public StringType getDescrizioneIndirizzo() {
		return descrizioneIndirizzo;
	}

	public void setDescrizioneIndirizzo(StringType descrizioneIndirizzo) {
		this.descrizioneIndirizzo = descrizioneIndirizzo;
	}

	public DatiApplicativiModel getDatiApplicativi() {
		return datiApplicativi;
	}

	public void setDatiApplicativi(DatiApplicativiModel datiApplicativi) {
		this.datiApplicativi = datiApplicativi;
	}

	public StringType getNumeroCivico() {
		return numeroCivico;
	}

	public void setNumeroCivico(StringType numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

	public StringType getToponimoIndirizzo() {
		return toponimoIndirizzo;
	}

	public void setToponimoIndirizzo(StringType toponimoIndirizzo) {
		this.toponimoIndirizzo = toponimoIndirizzo;
	}

	public StringType getStatoConfermato() {
		return statoConfermato;
	}

	public void setStatoConfermato(StringType statoConfermato) {
		this.statoConfermato = statoConfermato;
	}

	public boolean isExistOnDB() {
		return existOnDB;
	}

	public void setExistOnDB(boolean existOnDB) {
		this.existOnDB = existOnDB;
	}
}
